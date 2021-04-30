package cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder;

import cz.uhk.fim.bs.pickngo_mobile_be.Customer.Customer;
import cz.uhk.fim.bs.pickngo_mobile_be.Customer.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BaguetteOrderRepositoryTest {

    @Autowired
    private BaguetteOrderRepository underTest;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    void tearDown(){
        customerRepository.deleteAll();
        underTest.deleteAll();
    }


    @Test
    void findBaguetteOrderByStateAndCustomer_Email() {
        Customer customer = new Customer("name", "email", "emailshort");
        customerRepository.save(customer);

        BaguetteOrder baguetteOrder = new BaguetteOrder(customer, 1.0, new Date(),0, "note");

        underTest.save(baguetteOrder);
        //
        Optional<BaguetteOrder> order = underTest.findBaguetteOrderByStateAndCustomer_Email(0, customer.getEmail());

        //then
        assertThat(order.get()).isEqualTo(baguetteOrder);
    }

    @Test
    void findBaguetteOrderByCustomer_Email() {
        Customer customer = new Customer("name", "email", "emailshort");
        customerRepository.save(customer);

        BaguetteOrder baguetteOrder = new BaguetteOrder(customer, 1.0, new Date(),0, "note");

        underTest.save(baguetteOrder);
        //
        Optional<List<BaguetteOrder>> list = underTest.findBaguetteOrderByCustomer_Email(customer.getEmail());

        //then
        assertThat(list.get()).contains(baguetteOrder);
    }

    @Test
    void findAllByStateAndCustomer_Email() {
        Customer customer = new Customer("name", "email", "emailshort");
        customerRepository.save(customer);

        BaguetteOrder baguetteOrder = new BaguetteOrder(customer, 1.0, new Date(),0, "note");

        underTest.save(baguetteOrder);
        //
        Optional<List<BaguetteOrder>> list = underTest.findAllByStateAndCustomer_Email(0, customer.getEmail());

        //then
        assertThat(list.get()).contains(baguetteOrder);
    }

    @Test
    void findBaguetteOrderByIdAndCustomer_Email() {
        Customer customer = new Customer("name", "email", "emailshort");
        customerRepository.save(customer);

        BaguetteOrder baguetteOrder = new BaguetteOrder(customer, 1.0, new Date(),0, "note");

        underTest.save(baguetteOrder);
        //
        Optional<BaguetteOrder> order = underTest.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), customer.getEmail());

        //then
        assertThat(order.get()).isEqualTo(baguetteOrder);
    }

}