package cz.uhk.fim.bs.pickngo_mobile_be.Customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    private CustomerService underTest;

    @BeforeEach
    void setUp(){underTest = new CustomerService(customerRepository);}

    @Test
    void getCustomers() {
        underTest.getCustomers();
        verify(customerRepository).findAll();
    }

    @Test
    void addNewCustomer() {

        Customer customer = new Customer(
                "name",
                "email@email.cz",
                "shortemail@shortemail.cz"
        );
        underTest.addNewCustomer(customer);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerArgumentCaptor.capture());
        Customer capturedCus = customerArgumentCaptor.getValue();

        assertThat(capturedCus).isEqualTo(customer);
    }
    @Test
    void ThrowWhenAddNewCustomer() {

        Customer customer = new Customer(
                "name",
                "email@email.cz",
                "shortemail@shortemail.cz"
        );
        given(customerRepository.findCustomerByEmail(customer.getEmail())).willReturn(Optional.of(customer));
        assertThatThrownBy(() ->underTest.addNewCustomer(customer))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("email taken");

        verify(customerRepository, never()).save(any());
    }


    @Test
    void updateCustomer() {
        String emailTrue = "email@email.cz";
        Customer customer = new Customer(
                1L,
                "name",
                "email@email.cz",
                "shortemail@shortemail.cz"
        );
        Customer customer2 = new Customer(
                1L,
                "name2",
                "email2@email.cz",
                "shortemail2@shortemail.cz"
        );
        given(customerRepository.findById(customer2.getId())).willReturn(Optional.of(customer));
   //     given(customerRepository.findCustomerByEmail(customer2.getEmail())).willReturn(Optional.empty());
    //    given(customerRepository.findCustomerByEmailShort(customer.getEmailShort())).willReturn(Optional.empty());

        underTest.updateCustomer(customer2.getId(), customer2.getName(), customer2.getEmail(), customer2.getEmailShort(), emailTrue);
    }
    @Test
    void ThrowUpdateCustomer() {
        String emailTrue = "neplatnyemail";
        Customer customer = new Customer(
                1L,
                "name",
                "email@email.cz",
                "shortemail@shortemail.cz"
        );
        Customer customer2 = new Customer(
                1L,
                "name2",
                "email2@email.cz",
                "shortemail2@shortemail.cz"
        );
        given(customerRepository.findById(customer2.getId())).willReturn(Optional.of(customer));
        //     given(customerRepository.findCustomerByEmail(customer2.getEmail())).willReturn(Optional.empty());
        //    given(customerRepository.findCustomerByEmailShort(customer.getEmailShort())).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.updateCustomer(customer2.getId(), customer2.getName(), customer2.getEmail(), customer2.getEmailShort(), emailTrue))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("přístup zamítnut");

        verify(customerRepository, never()).save(any());

    }
    @Test
    void Throw2UpdateCustomer() {
        String emailTrue = "email@email.cz";
        Customer customer = new Customer(
                1L,
                "name",
                "email@email.cz",
                "shortemail@shortemail.cz"
        );
        Customer customer2 = new Customer(
                1L,
                "name2",
                "email3@email.cz",
                "shortemail2@shortemail.cz"
        );
        Customer customer3 = new Customer(
                2L,
                "name3",
                "email3@email.cz",
                "shortemail3@shortemail.cz"
        );
    //    given(customerRepository.findById(customer2.getId())).willReturn(Optional.of(customer));
     //   given(customerRepository.findCustomerByEmail(customer2.getEmail())).willReturn(Optional.of(customer3));
        //    given(customerRepository.findCustomerByEmailShort(customer.getEmailShort())).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.updateCustomer(customer2.getId(), customer2.getName(), customer2.getEmail(), customer2.getEmailShort(), emailTrue))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("email taken");

        verify(customerRepository, never()).save(any());

    }
    @Test
    void Throw3UpdateCustomer() {
        String emailTrue = "email@email.cz";
        Customer customer = new Customer(
                1L,
                "name",
                "email@email.cz",
                "shortemail@shortemail.cz"
        );
        Customer customer2 = new Customer(
                1L,
                "name2",
                "email2@email.cz",
                "shortemail3@shortemail.cz"
        );
        Customer customer3 = new Customer(
                2L,
                "name3",
                "email3@email.cz",
                "shortemail3@shortemail.cz"
        );
        //    given(customerRepository.findById(customer2.getId())).willReturn(Optional.of(customer));
        //   given(customerRepository.findCustomerByEmail(customer2.getEmail())).willReturn(Optional.of(customer3));
        //    given(customerRepository.findCustomerByEmailShort(customer.getEmailShort())).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.updateCustomer(customer2.getId(), customer2.getName(), customer2.getEmail(), customer2.getEmailShort(), emailTrue))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("email taken");

        verify(customerRepository, never()).save(any());

    }
    @Test
    void Throw4UpdateCustomer() {
        String emailTrue = "email@email.cz";
        Customer customer = new Customer(
                1L,
                "name",
                "email@email.cz",
                "shortemail@shortemail.cz"
        );
        Customer customer2 = new Customer(
                1L,
                "name2",
                "email2@email.cz",
                "shortemail3@shortemail.cz"
        );
        Customer customer3 = new Customer(
                2L,
                "name3",
                "email3@email.cz",
                "shortemail3@shortemail.cz"
        );
            given(customerRepository.findById(customer2.getId())).willReturn(Optional.empty());
        //   given(customerRepository.findCustomerByEmail(customer2.getEmail())).willReturn(Optional.of(customer3));
        //    given(customerRepository.findCustomerByEmailShort(customer.getEmailShort())).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.updateCustomer(customer2.getId(), customer2.getName(), customer2.getEmail(), customer2.getEmailShort(), emailTrue))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("customer with id "+ customer2.getId()+ "doesnt exist");

        verify(customerRepository, never()).save(any());

    }

}