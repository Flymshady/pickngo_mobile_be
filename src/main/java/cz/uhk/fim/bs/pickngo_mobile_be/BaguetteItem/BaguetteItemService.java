package cz.uhk.fim.bs.pickngo_mobile_be.BaguetteItem;

import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder.BaguetteOrder;
import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder.BaguetteOrderRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.Customer.Customer;
import cz.uhk.fim.bs.pickngo_mobile_be.Customer.CustomerRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.Item.Item;
import cz.uhk.fim.bs.pickngo_mobile_be.Item.ItemRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.SpecialOffer.SpecialOffer;
import cz.uhk.fim.bs.pickngo_mobile_be.SpecialOffer.SpecialOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BaguetteItemService {

    private final BaguetteItemRepository baguetteItemRepository;
    private final BaguetteOrderRepository baguetteOrderRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final SpecialOfferRepository specialOfferRepository;

    @Autowired
    public BaguetteItemService(BaguetteItemRepository baguetteItemRepository, BaguetteOrderRepository baguetteOrderRepository, CustomerRepository customerRepository, ItemRepository itemRepository, SpecialOfferRepository specialOfferRepository) {
        this.baguetteItemRepository = baguetteItemRepository;
        this.baguetteOrderRepository = baguetteOrderRepository;
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
        this.specialOfferRepository = specialOfferRepository;
    }

    public Optional<List<BaguetteItem>> getBaguetteItems(Long baguetteOrderId, String email) {
        Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
        if (!customer.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteOrder> baguetteOrderOptional = baguetteOrderRepository.findById(baguetteOrderId);
        if (!baguetteOrderOptional.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, objednávka nenalezena");
        }
        Optional<BaguetteOrder> orderByIdAndCustomer = baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrderId,email);
        if(!orderByIdAndCustomer.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, objednávka nenalezena");
        }
        Optional<List<BaguetteItem>> baguetteItemList = baguetteItemRepository.findAllByBaguetteOrder_Id(orderByIdAndCustomer.get().getId());
        if(!baguetteItemList.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, položka nenalezena");
        }
        return baguetteItemList;

    }

    public Optional<BaguetteItem> getBaguetteItem(Long baguetteItemId, String email) {
        Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
        if (!customer.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteItem> baguetteItem = baguetteItemRepository.findById(baguetteItemId);
        if(!baguetteItem.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, položka nenalezena");
        }
        if(!baguetteItem.get().getBaguetteOrder().getCustomer().getEmail().equals(email)){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, špatné údaje");
        }
        return baguetteItem;
    }

    @Transactional
    public void removeBaguetteItem(Long baguetteItemId, String email) {
        Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
        if (!customer.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteItem> baguetteItemOpt = baguetteItemRepository.findById(baguetteItemId);
        if(!baguetteItemOpt.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, položka nenalezena");
        }
        if(!baguetteItemOpt.get().getBaguetteOrder().getCustomer().getEmail().equals(email)){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, špatné údaje");
        }
        Optional<BaguetteOrder> baguetteOrder = baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteItemOpt.get().getBaguetteOrder().getId(), email);
        if(!baguetteOrder.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka nenelazena");
        }
        if (baguetteOrder.get().getState()!=0){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka již nelze měnit");
        }
        Optional<BaguetteOrder> baguetteOrder1 = baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(0, email);
        if(!baguetteOrder1.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka nenelazena");
        }
        double pricePrev = baguetteItemOpt.get().getPrice();
        Optional<List<Item>> items = itemRepository.findAllByBaguetteItem_Id(baguetteItemId);
        if(items.isPresent() && !items.get().isEmpty()) {
            for (Item item : items.get()) {
                itemRepository.delete(item);
            }
        }
        baguetteOrder.get().setPrice(baguetteOrder.get().getPrice()-pricePrev);
        baguetteOrderRepository.save(baguetteOrder.get());
        baguetteItemRepository.delete(baguetteItemOpt.get());

        if(baguetteOrder1.get().getBaguetteItems()!=null){
            if(baguetteOrder1.get().getBaguetteItems().isEmpty()){
                baguetteOrderRepository.delete(baguetteOrder1.get());
            }
        }
    }

    @Transactional
    public BaguetteItem createBaguetteItem(Long baguetteOrderId, String email) {
        Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
        if (!customer.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteOrder> baguetteOrderOpt = baguetteOrderRepository.findById(baguetteOrderId);
        if(!baguetteOrderOpt.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, objednávka nenalezena");
        }
        Optional<BaguetteOrder> baguetteOrder = baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrderOpt.get().getId(), email);
        if(!baguetteOrder.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka nenelazena");
        }
        if (baguetteOrder.get().getState()!=0){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka již nelze měnit");
        }

        BaguetteItem baguetteItem = new BaguetteItem();
        baguetteItem.setBaguetteOrder(baguetteOrder.get());
        baguetteItem.setOffer(false);
        if(baguetteOrder.get().getBaguetteItems()!=null){
            baguetteOrder.get().getBaguetteItems().add(baguetteItem);
        }else {
            List<BaguetteItem> baguetteItemList = new ArrayList<>();
            baguetteItemList.add(baguetteItem);
            baguetteOrder.get().setBaguetteItems(baguetteItemList);
        }

        baguetteOrderRepository.save(baguetteOrder.get());
        baguetteItemRepository.save(baguetteItem);

        return baguetteItem;

    }

    @Transactional
    public BaguetteItem createBaguetteItemFromSpecialOffer(Long baguetteOrderId, Long specialOfferId, String email) {
        Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
        if (!customer.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteOrder> baguetteOrderOpt = baguetteOrderRepository.findById(baguetteOrderId);
        if(!baguetteOrderOpt.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, objednávka nenalezena");
        }
        Optional<BaguetteOrder> baguetteOrder = baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrderOpt.get().getId(), email);
        if(!baguetteOrder.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka nenelazena");
        }
        if (baguetteOrder.get().getState()!=0){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka již nelze měnit");
        }
        Optional<SpecialOffer> specialOfferOptional = specialOfferRepository.findById(specialOfferId);
        if(!specialOfferOptional.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Speciální nabídka nenelazena");
        }
        if(!specialOfferOptional.get().isActive()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Speciální nabídka není k dispozici");
        }
        double price = specialOfferOptional.get().getPrice();
        BaguetteItem baguetteItem = new BaguetteItem();
        Optional<List<Item>> items = itemRepository.findAllBySpecialOffer_Id(specialOfferOptional.get().getId());
        List<Item> itemsNew = new ArrayList<>();
        if(items.isPresent()) {
            for (Item item : items.get()) {
                Item itemNew = new Item();
                itemNew.setBaguetteItem(baguetteItem);
                itemNew.setPrice(item.getPrice());
                itemNew.setName(item.getName());
                itemNew.setIngredient(item.getIngredient());
                itemNew.setAmount(item.getAmount());
                itemsNew.add(itemNew);
            }
        }
        baguetteItem.setItems(itemsNew);
        baguetteItem.setPrice(price);
        baguetteItem.setOffer(true);
        baguetteItem.setBaguetteOrder(baguetteOrder.get());
        baguetteOrder.get().getBaguetteItems().add(baguetteItem);
        baguetteOrder.get().setPrice(baguetteOrder.get().getPrice()+price);
        baguetteItemRepository.save(baguetteItem);
        baguetteOrderRepository.save(baguetteOrder.get());
        for(Item item2 : itemsNew){
            itemRepository.save(item2);
        }
        return baguetteItem;
    }
}
