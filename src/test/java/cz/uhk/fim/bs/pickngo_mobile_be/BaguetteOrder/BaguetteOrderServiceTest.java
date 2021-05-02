package cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder;

import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteItem.BaguetteItem;
import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteItem.BaguetteItemRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.Customer.Customer;
import cz.uhk.fim.bs.pickngo_mobile_be.Customer.CustomerRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.Item.ItemRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.Item.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BaguetteOrderServiceTest {
    @Mock
    private BaguetteOrderRepository baguetteOrderRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private BaguetteItemRepository baguetteItemRepository;
    @Mock
    private ItemRepository itemRepository;

    private BaguetteOrderService underTest;

    @BeforeEach
    void setUp(){underTest = new BaguetteOrderService(baguetteOrderRepository, customerRepository, baguetteItemRepository, itemRepository);}


    @Test
    void getBaguetteOrders() {
        String email ="email";
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(customer, 2.0, new Date(), 0,"note");
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));

        underTest.getBaguetteOrders(email);
        verify(baguetteOrderRepository).findBaguetteOrderByCustomer_Email(email);
    }

    @Test
    void ThrowGetBaguetteOrders() {
        String email ="email";
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(customer, 2.0, new Date(), 0,"note");
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.getBaguetteOrders(email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, uživatel nenalezen");
        verify(baguetteOrderRepository, never()).findBaguetteOrderByCustomer_Email(email);
    }

    @Test
    void getBaguetteOrdersByState() {
        String email ="email";
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(customer, 2.0, new Date(), 0,"note");
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));

        underTest.getBaguetteOrdersByState(email, 0);
        verify(baguetteOrderRepository).findAllByStateAndCustomer_Email(0, email);

    }

    @Test
    void ThrowGetBaguetteOrdersByState() {
        String email = "email";
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(customer, 2.0, new Date(), 0, "note");
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getBaguetteOrdersByState(email, 0))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, uživatel nenalezen");
        verify(baguetteOrderRepository, never()).findBaguetteOrderByStateAndCustomer_Email(0, email);

    }

    @Test
    void getBaguetteOrderId() {
        String email ="email";
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(1L, email)).willReturn(Optional.of(baguetteOrder));

        underTest.getBaguetteOrderId(1L, email);
        verify(baguetteOrderRepository).findBaguetteOrderByIdAndCustomer_Email(1L, email);

    }
    @Test
    void ThrowGetBaguetteOrderId() {
        String email ="email";
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getBaguetteOrderId(1L, email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, uživatel nenalezen");
        verify(baguetteOrderRepository, never()).findBaguetteOrderByIdAndCustomer_Email(1L, email);

    }


    @Test
    void removeBaguetteOrder() {
    }

    @Test
    void updateBaguetteOrder() {
    }

    @Test
    void confirmBaguetteOrder() {
    }

    @Test
    void createBaguetteOrder() {
    }

    @Test
    void getBaguetteOrderActual() {
        String email ="email";
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(0, email)).willReturn(Optional.of(baguetteOrder));

        underTest.getBaguetteOrderActual(email);
        verify(baguetteOrderRepository).findBaguetteOrderByStateAndCustomer_Email(0, email);
    }
    @Test
    void ThrowGetBaguetteOrderActual() {
        String email ="email";
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getBaguetteOrderActual( email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, uživatel nenalezen");
        verify(baguetteOrderRepository, never()).findBaguetteOrderByStateAndCustomer_Email(0, email);

    }

}