package cz.uhk.fim.bs.pickngo_mobile_be.Item;

import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteItem.BaguetteItem;
import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteItem.BaguetteItemRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder.BaguetteOrder;
import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder.BaguetteOrderRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.Customer.Customer;
import cz.uhk.fim.bs.pickngo_mobile_be.Customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final BaguetteItemRepository baguetteItemRepository;
    private final BaguetteOrderRepository baguetteOrderRepository;

    @Autowired
    public ItemService(CustomerRepository customerRepository, ItemRepository itemRepository, BaguetteItemRepository baguetteItemRepository, BaguetteOrderRepository baguetteOrderRepository) {
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
        this.baguetteItemRepository = baguetteItemRepository;
        this.baguetteOrderRepository = baguetteOrderRepository;
    }


    public Optional<List<Item>> getItems(Long baguetteItemId, String email) {
        Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
        if (!customer.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteItem> baguetteItem = baguetteItemRepository.findById(baguetteItemId);
        if (!baguetteItem.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, položka nenalezena");
        }
        if(!baguetteItem.get().getBaguetteOrder().getCustomer().equals(customer.get())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba oprávnění");
        }
        return itemRepository.findAllByBaguetteItem_Id(baguetteItemId);
    }


    @Transactional
    public Item addNewItem(Item item, String email, Long baguetteItemId) {
        Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
        if (!customer.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteItem> baguetteItemOpt = baguetteItemRepository.findById(baguetteItemId);
        if (!baguetteItemOpt.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, položka nenalezena");
        }
        if(baguetteItemOpt.get().isOffer()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Specialní nabídku nelze měnit");
        }
        Optional<BaguetteOrder> baguetteOrder = baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteItemOpt.get().getBaguetteOrder().getId(), email);
        if (!baguetteOrder.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, objednávka nenalezena");
        }else if (baguetteOrder.get().getState()!=0){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, objednávku nelze měnit");
        }
        if(item.getIngredient().getIngredientType().getName().equals("Pečivo")) {
            Optional<List<Item>> pecivoList = itemRepository.findByIngredient_IngredientType_NameAndBaguetteItem_Id("Pečivo", baguetteItemId);
            if (!pecivoList.isPresent()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Nelze přidat více pečiva do jedné bagety :)");
            }
        }

        baguetteItemOpt.get().setPrice(baguetteItemOpt.get().getPrice() + item.getPrice()*item.getAmount());
        baguetteItemRepository.save(baguetteItemOpt.get());
        baguetteOrder.get().setPrice(baguetteOrder.get().getPrice() + baguetteItemOpt.get().getPrice());
        baguetteOrderRepository.save(baguetteOrder.get());

        Item itemNew = new Item();
        itemNew.setAmount(item.getAmount());
        itemNew.setIngredient(item.getIngredient());
        itemNew.setName(item.getName());
        itemNew.setPrice(item.getPrice());
        itemNew.setBaguetteItem(baguetteItemOpt.get());
        itemRepository.save(itemNew);
        return itemNew;
    }

    @Transactional
    public void updateItem(Long itemId, String email, int amount){
        Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
        if (!customer.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }

        Optional<BaguetteOrder> optBaguetteOrder = baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(0, email);
        if(!optBaguetteOrder.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, objednávka nenalezena");
        }
        Optional<Item> item = itemRepository.findById(itemId);
        if(!item.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, položka nenalezena");
        }
        if(amount<=0){
            removeItem(itemId, email);
        }else {
            if(item.get().getIngredient().getIngredientType().getName().equals("Pečivo")){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Nelze přidat více pečiva do jedné bagety :)");
            }
            int amountPrev = item.get().getAmount();
            double itemPrice = item.get().getPrice();

            Optional<BaguetteItem> baguetteItemOpt = baguetteItemRepository.findById(item.get().getBaguetteItem().getId());
            if(!baguetteItemOpt.isPresent()){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "chyba, položka nenalezena");
            }
            if(baguetteItemOpt.get().isOffer()){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Specialní nabídku nelze měnit");
            }

            baguetteItemOpt.get().setPrice(baguetteItemOpt.get().getPrice() - amountPrev*itemPrice+itemPrice*amount);
            baguetteItemRepository.save(baguetteItemOpt.get());

            Optional<BaguetteOrder> baguetteOrderOptional = baguetteOrderRepository.findById(baguetteItemOpt.get().getBaguetteOrder().getId());
            if(!baguetteOrderOptional.isPresent()){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "chyba, objednávka nenalezena");
            }
            baguetteOrderOptional.get().setPrice(baguetteOrderOptional.get().getPrice() - amountPrev*itemPrice + itemPrice*amount);

            baguetteOrderRepository.save(baguetteOrderOptional.get());
            item.get().setAmount(amount);
            itemRepository.save(item.get());
        }

    }

    @Transactional
    public void removeItem(Long itemId, String email){
        Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
        if (!customer.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }

        Optional<BaguetteOrder> optBaguetteOrder = baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(0, email);
        if(!optBaguetteOrder.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, objednávka nenalezena");
        }
        Optional<Item> item = itemRepository.findById(itemId);
        if(!item.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, položka nenalezena");
        }
        Optional<BaguetteItem> baguetteItem = baguetteItemRepository.findById(item.get().getBaguetteItem().getId());
        if(!baguetteItem.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, položka nenalezena");
        }
        if(baguetteItem.get().isOffer()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Specialní nabídku nelze měnit");
        }

        double price = item.get().getPrice() * item.get().getAmount();
        int amount = item.get().getAmount();

        baguetteItem.get().getItems().remove(item.get());
        itemRepository.delete(item.get());
        if(baguetteItem.get().getItems().isEmpty()){
            optBaguetteOrder.get().getBaguetteItems().remove(baguetteItem.get());
            baguetteItemRepository.delete(baguetteItem.get());
            baguetteOrderRepository.save(optBaguetteOrder.get());
        }
        if(optBaguetteOrder.get().getBaguetteItems().isEmpty()){
            baguetteOrderRepository.delete(optBaguetteOrder.get());
        }else{
            baguetteItem.get().setPrice(baguetteItem.get().getPrice()-amount*price);
            baguetteItemRepository.save(baguetteItem.get());
            optBaguetteOrder.get().setPrice(optBaguetteOrder.get().getPrice() - amount*price);
            baguetteOrderRepository.save(optBaguetteOrder.get());
        }

    }

}
