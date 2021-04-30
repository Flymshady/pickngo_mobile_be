package cz.uhk.fim.bs.pickngo_mobile_be.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path="/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void registerNewCustomer(@RequestBody Customer customer){
        customerService.addNewCustomer(customer);
    }

    @RequestMapping(value = "/update/{customerId}", method = RequestMethod.PUT)
    public void updateCustomer(
            @PathVariable("customerId") Long customerId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String emailShort) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;
        String emailTrue = authentication.getPrincipal().getAttribute("email");
        customerService.updateCustomer(customerId, name, email, emailShort, emailTrue);
    }

}
