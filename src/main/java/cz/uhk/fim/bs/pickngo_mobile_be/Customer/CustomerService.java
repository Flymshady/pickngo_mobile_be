package cz.uhk.fim.bs.pickngo_mobile_be.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }


    public Customer addNewCustomer(Customer customer) {
        Customer customerOptional = customerRepository.findCustomerByEmail(customer.getEmail());
        if (customerOptional != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "email taken");
        }
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long customerId, String name, String email, String emailShort){
        Customer customer = customerRepository.findById(customerId).orElseThrow(()->
                new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "customer with id "+ customerId+ "doesnt exist"));

        if(email.length() < emailShort.length()) {
            String i;
            i = emailShort;
            emailShort = email;
            email = i;
        }

        if (name !=null && name.length() > 0 && !Objects.equals(customer.getName(), name)){
            customer.setName(name);
        }
        if (email !=null && email.length() > 0 && !Objects.equals(customer.getEmail(), email)){
            Customer customerOptional = customerRepository.findCustomerByEmail(email);
            if (customerOptional != null){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "email taken");
            }
            customer.setEmail(email);
        }
        if (emailShort !=null && emailShort.length() > 0 && !Objects.equals(customer.getEmailShort(), emailShort)){
            Customer customerOptional = customerRepository.findCustomerByEmailShort(emailShort);
            if (customerOptional != null){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "email taken");
            }
            customer.setEmailShort(emailShort);
        }

        return customerRepository.save(customer);


    }
}
