package cz.uhk.fim.bs.pickngo_mobile_be.Item;

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
    private final BaguetteOrderRepository baguetteOrderRepository;

    @Autowired
    public ItemService(CustomerRepository customerRepository, ItemRepository itemRepository, BaguetteOrderRepository baguetteOrderRepository) {
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
        this.baguetteOrderRepository = baguetteOrderRepository;
    }


    public List<Item> getItems(Long baguetteOrderId, String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        Optional<BaguetteOrder> baguetteOrder = baguetteOrderRepository.findById(baguetteOrderId);
        if (!baguetteOrder.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, objednávka nenalezena");
        }
        if(!baguetteOrder.get().getCustomer().equals(customer)){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba oprávnění");
        }
        return itemRepository.findAllByBaguetteOrder_Id(baguetteOrderId);
    }


    @Transactional
    public Item addNewItem(Item item, String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "chyba, uživatel nenalezen");
        }
        BaguetteOrder optBaguetteOrder = baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(0, email);
        if(optBaguetteOrder == null){
            BaguetteOrder baguetteOrder= new BaguetteOrder();
            baguetteOrder.setCustomer(customer);
            baguetteOrder.setState(0);
            baguetteOrderRepository.save(baguetteOrder);

            item.setBaguetteOrder(baguetteOrder);
            return itemRepository.save(item);
        }else {
            item.setBaguetteOrder(optBaguetteOrder);
            return itemRepository.save(item);
        }

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
            optBaguetteOrder.setPrice(optBaguetteOrder.getPrice() - amountPrev * itemPrice + itemPrice * amount);
            baguetteOrderRepository.save(optBaguetteOrder);
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
        double price = item.getPrice() * item.getAmount();
        int amount = item.getAmount();

        itemRepository.delete(item);
        if(optBaguetteOrder.getItems().isEmpty()){
            baguetteOrderRepository.delete(optBaguetteOrder);
        }else{
            optBaguetteOrder.setPrice(optBaguetteOrder.getPrice() - amount*price);
            baguetteOrderRepository.save(optBaguetteOrder);
        }

    }

}
