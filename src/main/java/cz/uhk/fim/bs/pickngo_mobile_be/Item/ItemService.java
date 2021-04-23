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


    public List<Item> getItems(Long baguetteItemId, String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteItem> baguetteItem = baguetteItemRepository.findById(baguetteItemId);
        if (!baguetteItem.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, položka nenalezena");
        }
        if(!baguetteItem.get().getBaguetteOrder().getCustomer().equals(customer)){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba oprávnění");
        }
        return itemRepository.findAllByBaguetteItem_Id(baguetteItemId);
    }


    @Transactional
    public Item addNewItem(Item item, String email, Long baguetteItemId) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteItem> baguetteItemOpt = baguetteItemRepository.findById(baguetteItemId);
        if (!baguetteItemOpt.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, položka nenalezena");
        }
        BaguetteItem baguetteItem = baguetteItemRepository.getOne(baguetteItemId);
        BaguetteOrder baguetteOrder = baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteItemOpt.get().getBaguetteOrder().getId(), email);
        if (baguetteOrder == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, objednávka nenalezena");
        }else if (baguetteOrder.getState()!=0){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, objednávku nelze měnit");
        }

        baguetteItem.setPrice(baguetteItem.getPrice() + item.getPrice()*item.getAmount());
        baguetteItemRepository.save(baguetteItem);
        baguetteOrder.setPrice(baguetteOrder.getPrice() + baguetteItem.getPrice());
        baguetteOrderRepository.save(baguetteOrder);

        Item itemNew = new Item();
        itemNew.setAmount(item.getAmount());
        itemNew.setIngredient(item.getIngredient());
        itemNew.setName(item.getName());
        itemNew.setPrice(item.getPrice());
        itemNew.setBaguetteItem(baguetteItem);
        return itemRepository.save(itemNew);
    }

    @Transactional
    public void updateItem(Long itemId, String email, int amount){
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }

        BaguetteOrder optBaguetteOrder = baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(0, email);
        if(optBaguetteOrder == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, objednávka nenalezena");
        }
        Item item = itemRepository.getOne(itemId);
        if(item == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, položka nenalezena");
        }
        if(amount<=0){
            removeItem(itemId, email);
        }else {

            int amountPrev = item.getAmount();
            double itemPrice = item.getPrice();

            Optional<BaguetteItem> baguetteItemOpt = baguetteItemRepository.findById(item.getBaguetteItem().getId());
            if(!baguetteItemOpt.isPresent()){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "chyba, položka nenalezena");
            }
            BaguetteItem baguetteItem = baguetteItemRepository.getOne(baguetteItemOpt.get().getId());
            baguetteItem.setPrice(baguetteItem.getPrice() - amountPrev*itemPrice+itemPrice*amount);
            baguetteItemRepository.save(baguetteItem);

            Optional<BaguetteOrder> baguetteOrderOptional = baguetteOrderRepository.findById(baguetteItem.getBaguetteOrder().getId());
            if(!baguetteOrderOptional.isPresent()){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "chyba, objednávka nenalezena");
            }
            BaguetteOrder baguetteOrder = baguetteOrderRepository.getOne(baguetteOrderOptional.get().getId());
            baguetteOrder.setPrice(baguetteOrder.getPrice() - amountPrev*itemPrice + itemPrice*amount);

            baguetteOrderRepository.save(baguetteOrder);
            item.setAmount(amount);
            itemRepository.save(item);
        }

    }

    @Transactional
    public void removeItem(Long itemId, String email){
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }

        BaguetteOrder optBaguetteOrder = baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(0, email);
        if(optBaguetteOrder == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, objednávka nenalezena");
        }
        Item item = itemRepository.getOne(itemId);
        if(item == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, položka nenalezena");
        }
        BaguetteItem baguetteItem = baguetteItemRepository.getOne(item.getBaguetteItem().getId());
        if(baguetteItem == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, položka nenalezena");
        }

        double price = item.getPrice() * item.getAmount();
        int amount = item.getAmount();

        baguetteItem.getItems().remove(item);
        itemRepository.delete(item);
        if(baguetteItem.getItems().isEmpty()){
            optBaguetteOrder.getBaguetteItems().remove(baguetteItem);
            baguetteItemRepository.delete(baguetteItem);
            baguetteOrderRepository.save(optBaguetteOrder);
        }
        if(optBaguetteOrder.getBaguetteItems().isEmpty()){
            baguetteOrderRepository.delete(optBaguetteOrder);
        }else{
            baguetteItem.setPrice(baguetteItem.getPrice()-amount*price);
            baguetteItemRepository.save(baguetteItem);
            optBaguetteOrder.setPrice(optBaguetteOrder.getPrice() - amount*price);
            baguetteOrderRepository.save(optBaguetteOrder);
        }

    }

}
