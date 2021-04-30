package cz.uhk.fim.bs.pickngo_mobile_be.Customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @AfterEach
    void tearDown(){
        underTest.deleteAll();
    }

    @Test
    void findCustomerByEmail() {
        //given
        String email = "email@email.cz";
        Customer c = new Customer("jmeno", email , "emailshort");

        underTest.save(c);
        //when
        Optional<Customer> optionalCustomer = underTest.findCustomerByEmail(email);
        //then
        assertThat(optionalCustomer).isPresent();

    }

    @Test
    void findCustomerByEmailShort() {
        //given
        String emailshort = "emailshort@emailshort";
        Customer c = new Customer("jmeno", "email@email.cz" , emailshort);

        underTest.save(c);
        //when
        Optional<Customer> optionalCustomer = underTest.findCustomerByEmailShort(emailshort);
        //then
        assertThat(optionalCustomer).isPresent();
    }

    @Test
    void itShouldNotFindEmployeeByLogin() {
        //given
        String email = "email@email.cz";
        //when
        Optional<Customer> optionalCustomer = underTest.findCustomerByEmail(email);
        //then
        assertThat(optionalCustomer).isNotPresent();
    }
}