package cz.uhk.fim.bs.pickngo_mobile_be.CustomUser;

import cz.uhk.fim.bs.pickngo_mobile_be.Customer.Customer;
import cz.uhk.fim.bs.pickngo_mobile_be.Customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    CustomerRepository customerRepository;

    @Autowired
    public CustomUserDetailsService(CustomerRepository customerRepository){
        this.customerRepository=customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Customer> customerOptional = customerRepository.findCustomerByEmail(email);
        customerOptional.orElseThrow(()-> new UsernameNotFoundException("Username not found"));
        return customerOptional.map(customer -> new CustomUserDetails(customer)).get();
    }
}
