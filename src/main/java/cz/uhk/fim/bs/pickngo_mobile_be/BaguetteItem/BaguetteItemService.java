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

    public List<BaguetteItem> getBaguetteItems(Long baguetteOrderId, String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteOrder> baguetteOrderOptional = baguetteOrderRepository.findById(baguetteOrderId);
        if (!baguetteOrderOptional.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, objednávka nenalezena");
        }
        BaguetteOrder orderByIdAndCustomer = baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrderId,email);
        if(orderByIdAndCustomer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, objednávka nenalezena");
        }
        List<BaguetteItem> baguetteItemList = baguetteItemRepository.findAllByBaguetteOrder_Id(orderByIdAndCustomer.getId());
        return baguetteItemList;

    }

    public BaguetteItem getBaguetteItem(Long baguetteItemId, String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteItem> baguetteItem = baguetteItemRepository.findById(baguetteItemId);
        if(baguetteItem.get().getBaguetteOrder().getCustomer().getEmail() != email){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, špatné údaje");
        }
        return baguetteItemRepository.getOne(baguetteItemId);
    }

    @Transactional
    public void removeBaguetteItem(Long baguetteItemId, String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteItem> baguetteItemOpt = baguetteItemRepository.findById(baguetteItemId);
        if(baguetteItemOpt.get().getBaguetteOrder().getCustomer().getEmail() != email){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, špatné údaje");
        }
        BaguetteItem baguetteItem = baguetteItemRepository.getOne(baguetteItemId);
        BaguetteOrder baguetteOrder = baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteItemOpt.get().getBaguetteOrder().getId(), email);
        if(baguetteOrder == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka nenelazena");
        }
        if (baguetteOrder.getState()!=0){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka již nelze měnit");
        }
        List<Item> items = itemRepository.findAllByBaguetteItem_Id(baguetteItemId);
        if(!items.isEmpty()) {
            for (Item item : items) {
                itemRepository.delete(item);
            }
        }
        baguetteItemRepository.delete(baguetteItem);
        BaguetteOrder baguetteOrder1 = baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(0, email);
        if(baguetteOrder1==null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka nenelazena");
        }
        if(baguetteOrder1.getBaguetteItems().isEmpty()){
            baguetteOrderRepository.delete(baguetteOrder1);
        }

    }

    @Transactional
    public BaguetteItem createBaguetteItem(Long baguetteOrderId, String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteOrder> baguetteOrderOpt = baguetteOrderRepository.findById(baguetteOrderId);
        if(!baguetteOrderOpt.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, objednávka nenalezena");
        }
        BaguetteOrder baguetteOrder = baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrderOpt.get().getId(), email);
        if(baguetteOrder == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka nenelazena");
        }
        if (baguetteOrder.getState()!=0){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka již nelze měnit");
        }

        BaguetteItem baguetteItem = new BaguetteItem();
        baguetteItem.setBaguetteOrder(baguetteOrder);

        baguetteOrder.getBaguetteItems().add(baguetteItem);
        baguetteOrderRepository.save(baguetteOrder);
        baguetteItemRepository.save(baguetteItem);

        return baguetteItem;

    }

    public BaguetteItem createBaguetteItemFromSpecialOffer(Long baguetteOrderId, Long specialOfferId, String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteOrder> baguetteOrderOpt = baguetteOrderRepository.findById(baguetteOrderId);
        if(!baguetteOrderOpt.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, objednávka nenalezena");
        }
        BaguetteOrder baguetteOrder = baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrderOpt.get().getId(), email);
        if(baguetteOrder == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka nenelazena");
        }
        if (baguetteOrder.getState()!=0){
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

        List<Item> items = specialOfferOptional.get().getItems();
        double price = specialOfferOptional.get().getPrice();
        BaguetteItem baguetteItem = new BaguetteItem();
        baguetteItem.setPrice(price);
        baguetteItem.setOffer(true);
        baguetteItem.setItems(items);
        baguetteOrder.getBaguetteItems().add(baguetteItem);
        baguetteOrder.setPrice(baguetteOrder.getPrice()+price);
        baguetteOrderRepository.save(baguetteOrder);
        baguetteItemRepository.save(baguetteItem);
        return baguetteItem;
    }
}
