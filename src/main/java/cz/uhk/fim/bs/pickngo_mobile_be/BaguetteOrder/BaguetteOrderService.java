package cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder;

import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteItem.BaguetteItem;
import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteItem.BaguetteItemRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.Customer.Customer;
import cz.uhk.fim.bs.pickngo_mobile_be.Customer.CustomerRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.Item.Item;
import cz.uhk.fim.bs.pickngo_mobile_be.Item.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class BaguetteOrderService {

    private final BaguetteOrderRepository baguetteOrderRepository;
    private final CustomerRepository customerRepository;
    private final BaguetteItemRepository baguetteItemRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public BaguetteOrderService(BaguetteOrderRepository baguetteOrderRepository, CustomerRepository customerRepository, BaguetteItemRepository baguetteItemRepository, ItemRepository itemRepository) {
        this.baguetteOrderRepository = baguetteOrderRepository;
        this.customerRepository = customerRepository;
        this.baguetteItemRepository =baguetteItemRepository;
        this.itemRepository = itemRepository;
    }

    public List<BaguetteOrder> getBaguetteOrders(String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        List<BaguetteOrder> baguetteOrderList = baguetteOrderRepository.findBaguetteOrderByCustomer_Email(email);
        return baguetteOrderList;

    }

    public List<BaguetteOrder> getBaguetteOrdersByState(String email, int state) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        List<BaguetteOrder> baguetteOrderList = baguetteOrderRepository.findAllByStateAndCustomer_Email(state, email);
        return baguetteOrderList;
    }

    public BaguetteOrder getBaguetteOrderId(Long baguetteOrderId, String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        BaguetteOrder baguetteOrder = baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrderId, email);
        if(baguetteOrder == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka nenelazena");
        }
        return baguetteOrder;
    }

    @Transactional
    public void removeBaguetteOrder(Long baguetteOrderId, String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        BaguetteOrder baguetteOrder = baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrderId, email);
        if(baguetteOrder == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka nenelazena");
        }
        if (baguetteOrder.getState()==0){
            List<BaguetteItem> baguetteItemList = baguetteItemRepository.findAllByBaguetteOrder_Id(baguetteOrderId);
            if(!baguetteItemList.isEmpty()){
                for(BaguetteItem baguetteItem : baguetteItemList){
                    List<Item> items = itemRepository.findAllByBaguetteItem_Id(baguetteItem.getId());
                    if(!items.isEmpty()) {
                        for (Item item : items) {
                            itemRepository.delete(item);
                        }
                    }
                    baguetteItemRepository.delete(baguetteItem);
                }

            }
            baguetteOrderRepository.delete(baguetteOrder);
        }else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávku nelze odstranit");
        }
    }

    @Transactional
    public void updateBaguetteOrder(Long baguetteOrderId, String note, String email) {

        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        BaguetteOrder baguetteOrder = baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrderId, email);
        if(baguetteOrder == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka nenelazena");
        }
        if (baguetteOrder.getState()==0){

            baguetteOrder.setNote(note);
            baguetteOrderRepository.save(baguetteOrder);

        }else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávku nelze upravit");
        }
    }

    @Transactional
    public void confirmBaguetteOrder(Long baguetteOrderId, String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        BaguetteOrder baguetteOrder = baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrderId, email);
        if(baguetteOrder == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka nenelazena");
        }
        if (baguetteOrder.getState()==0){

            baguetteOrder.setState(1);
            baguetteOrderRepository.save(baguetteOrder);

        }else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávku nelze upravit");
        }
    }

    public BaguetteOrder createBaguetteOrder(String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        List<BaguetteOrder> baguetteOrderList = baguetteOrderRepository.findAllByStateAndCustomer_Email(0, email);
        if (!baguetteOrderList.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "nejprve dokončete objednávku");
        }

        BaguetteOrder baguetteOrder =  new BaguetteOrder();
        baguetteOrder.setState(0);
        baguetteOrder.setCustomer(customer);
        baguetteOrderRepository.save(baguetteOrder);
        return baguetteOrder;
    }

    public Optional<BaguetteOrder> getBaguetteOrderActual(String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteOrder> baguetteOrderOptional = Optional.ofNullable(baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(0, email));
        return baguetteOrderOptional;

    }
}
