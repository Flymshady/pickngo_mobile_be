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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

    public Optional<List<BaguetteOrder>> getBaguetteOrders(String email) {
        Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
        if (!customer.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<List<BaguetteOrder>> baguetteOrderList = baguetteOrderRepository.findBaguetteOrderByCustomer_Email(email);
        return baguetteOrderList;

    }

    public Optional<List<BaguetteOrder>> getBaguetteOrdersByState(String email, int state) {
        Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
        if (!customer.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<List<BaguetteOrder>> baguetteOrderList = baguetteOrderRepository.findAllByStateAndCustomer_Email(state, email);
        return baguetteOrderList;
    }

    public Optional<BaguetteOrder> getBaguetteOrderId(Long baguetteOrderId, String email) {
        Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
        if (!customer.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteOrder> baguetteOrder = baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrderId, email);
        if(!baguetteOrder.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka nenelazena");
        }
        return baguetteOrder;
    }

    @Transactional
    public void removeBaguetteOrder(Long baguetteOrderId, String email) {
        Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
        if (!customer.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteOrder> baguetteOrder = baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrderId, email);
        if(!baguetteOrder.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka nenelazena");
        }
        if (baguetteOrder.get().getState()==0){
            Optional<List<BaguetteItem>> baguetteItemList = baguetteItemRepository.findAllByBaguetteOrder_Id(baguetteOrderId);
            if(baguetteItemList.isPresent() && !baguetteItemList.get().isEmpty()){
                for(BaguetteItem baguetteItem : baguetteItemList.get()){
                    Optional<List<Item>> items = itemRepository.findAllByBaguetteItem_Id(baguetteItem.getId());
                    if(items.isPresent() && !items.get().isEmpty()) {
                        for (Item item : items.get()) {
                            itemRepository.delete(item);
                        }
                    }
                    baguetteItemRepository.delete(baguetteItem);
                }
            }
            baguetteOrderRepository.delete(baguetteOrder.get());
        }else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávku nelze odstranit");
        }
    }

    @Transactional
    public void updateBaguetteOrder(Long baguetteOrderId, String note, String email) {

        Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
        if (!customer.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteOrder> baguetteOrder = baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrderId, email);
        if(!baguetteOrder.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka nenelazena");
        }
        if (baguetteOrder.get().getState()==0){

            baguetteOrder.get().setNote(note);
            baguetteOrderRepository.save(baguetteOrder.get());

        }else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávku nelze upravit");
        }
    }

    @Transactional
    public void confirmBaguetteOrder(Long baguetteOrderId, String date, String email) {
        Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
        if (!customer.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteOrder> baguetteOrder = baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrderId, email);
        if(!baguetteOrder.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávka nenelazena");
        }
        if (baguetteOrder.get().getState()==0){

            DateFormat format = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss", Locale.ENGLISH);
            Date dateS=null;
            try{
                dateS = format.parse(date);
            }catch (Exception ex){
                System.out.println(ex.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Date fail");
            }
            baguetteOrder.get().setDate(dateS);
            baguetteOrder.get().setState(1);
            baguetteOrderRepository.save(baguetteOrder.get());

        }else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Objednávku nelze upravit");
        }
    }

    public BaguetteOrder createBaguetteOrder(String email) {
        Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
        if (!customer.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<List<BaguetteOrder>> baguetteOrderList = baguetteOrderRepository.findAllByStateAndCustomer_Email(0, email);
        if (baguetteOrderList.isPresent() && !baguetteOrderList.get().isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "nejprve dokončete objednávku");
        }

        BaguetteOrder baguetteOrder =  new BaguetteOrder();
        baguetteOrder.setState(0);
        baguetteOrder.setCustomer(customer.get());
        baguetteOrderRepository.save(baguetteOrder);
        return baguetteOrder;
    }

    public Optional<BaguetteOrder> getBaguetteOrderActual(String email) {
        Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
        if (!customer.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteOrder> baguetteOrderOptional =baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(0, email);
        if (!baguetteOrderOptional.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, objednávka nenalezena");
        }
        return baguetteOrderOptional;

    }
}
