package cz.uhk.fim.bs.pickngo_mobile_be.CustomUser;

import cz.uhk.fim.bs.pickngo_mobile_be.Customer.Customer;
import cz.uhk.fim.bs.pickngo_mobile_be.Customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomOidcUserService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        try {
            return processOidcUser(userRequest, oidcUser);
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OidcUser processOidcUser(OidcUserRequest userRequest, OidcUser oidcUser) {
        AzureUserInfo azureUserInfo = new AzureUserInfo(oidcUser.getAttributes());

        // see what other data from userRequest or oidcUser you need

        Optional<Customer> customerOptional = customerRepository.findCustomerByEmail(azureUserInfo.getEmail());
        Optional<Customer> customerOptional1 = customerRepository.findCustomerByEmail(azureUserInfo.getEmailShort());
        Optional<Customer> customerOptional2 = customerRepository.findCustomerByEmailShort(azureUserInfo.getEmail());
        Optional<Customer> customerOptional3 = customerRepository.findCustomerByEmailShort(azureUserInfo.getEmailShort());

        if (!customerOptional.isPresent() && !customerOptional1.isPresent() && !customerOptional2.isPresent() && !customerOptional3.isPresent()) {
            Customer customer = new Customer();
            customer.setEmail(azureUserInfo.getEmail());
            customer.setName(azureUserInfo.getName());
            customer.setEmailShort(azureUserInfo.getEmailShort());

            // set other needed data

            customerRepository.save(customer);
        }

        return oidcUser;

    }
}
