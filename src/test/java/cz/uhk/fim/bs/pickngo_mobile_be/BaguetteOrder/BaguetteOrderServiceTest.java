package cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder;

import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteItem.BaguetteItemRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.Customer.Customer;
import cz.uhk.fim.bs.pickngo_mobile_be.Customer.CustomerRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.Item.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
        String email ="email";

        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.of(baguetteOrder));

        underTest.removeBaguetteOrder(baguetteOrder.getId(), email);
        verify(baguetteOrderRepository).delete(baguetteOrder);
    }

    @Test
    void ThrowRemoveBaguetteOrder() {
        String email ="email";
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.empty());
        assertThatThrownBy(() ->underTest.removeBaguetteOrder(baguetteOrder.getId(), email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, uživatel nenalezen");
        verify(baguetteOrderRepository, never()).delete(any());
    }
    @Test
    void Throw2RemoveBaguetteOrder() {
        String email ="email_";
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.removeBaguetteOrder(baguetteOrder.getId(), email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("Objednávka nenelazena");
        verify(baguetteOrderRepository, never()).delete(any());
    }
    @Test
    void Throw3RemoveBaguetteOrder() {
        String email ="email";
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 1,"note");

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.of(baguetteOrder));

        assertThatThrownBy(() ->underTest.removeBaguetteOrder(baguetteOrder.getId(), email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("Objednávku nelze odstranit");
        verify(baguetteOrderRepository, never()).delete(any());
    }

    @Test
    void updateBaguetteOrder() {
        String email ="email";
        Long baguetteOrderId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(baguetteOrderId, customer, 2.0, new Date(), 0,"note");
        BaguetteOrder baguetteOrder2 = new BaguetteOrder(baguetteOrderId, customer, 2.0, new Date(), 0,"note2");

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.of(baguetteOrder));

        underTest.updateBaguetteOrder(baguetteOrder2.getId(),baguetteOrder2.getNote(), email);
        ArgumentCaptor<BaguetteOrder> baguetteOrderArgumentCaptor = ArgumentCaptor.forClass(BaguetteOrder.class);
        verify(baguetteOrderRepository).save(baguetteOrderArgumentCaptor.capture());
        BaguetteOrder capturedBaguetteOrder = baguetteOrderArgumentCaptor.getValue();

        assertThat(capturedBaguetteOrder).isExactlyInstanceOf(BaguetteOrder.class);
        assertThat(capturedBaguetteOrder.getNote()).isEqualTo(baguetteOrder2.getNote());
    }

    @Test
    void ThrowUpdateBaguetteOrder() {
        String email ="email";
        Long baguetteOrderId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(baguetteOrderId, customer, 2.0, new Date(), 0,"note");
        BaguetteOrder baguetteOrder2 = new BaguetteOrder(baguetteOrderId, customer, 2.0, new Date(), 0,"note2");

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.empty());
        assertThatThrownBy(() ->underTest.updateBaguetteOrder(baguetteOrder.getId(),baguetteOrder2.getNote(), email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, uživatel nenalezen");
        verify(baguetteOrderRepository, never()).save(any());
    }

    @Test
    void Throw2UpdateBaguetteOrder() {
        String email ="email";
        Long baguetteOrderId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(baguetteOrderId, customer, 2.0, new Date(), 0,"note");
        BaguetteOrder baguetteOrder2 = new BaguetteOrder(baguetteOrderId, customer, 2.0, new Date(), 0,"note2");

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.updateBaguetteOrder(baguetteOrder.getId(),baguetteOrder2.getNote(), email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("Objednávka nenelazena");
        verify(baguetteOrderRepository, never()).save(any());
    }
    @Test
    void Throw3UpdateBaguetteOrder() {
        String email ="email";
        Long baguetteOrderId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(baguetteOrderId, customer, 2.0, new Date(), 1,"note");
        BaguetteOrder baguetteOrder2 = new BaguetteOrder(baguetteOrderId, customer, 2.0, new Date(), 1,"note2");

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.updateBaguetteOrder(baguetteOrder.getId(),baguetteOrder2.getNote(), email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("Objednávku nelze upravit");
        verify(baguetteOrderRepository, never()).save(any());
    }

    @Test
    void confirmBaguetteOrder() {
        String email ="email";
        Long baguetteOrderId = 1L;
        String datestring = "02-05-2021, 18:54:35";
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(baguetteOrderId, customer, 2.0, new Date(), 0,"note");
        BaguetteOrder baguetteOrder2 = new BaguetteOrder(baguetteOrderId, customer, 2.0, new Date(), 0,"note2");

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.of(baguetteOrder));

        underTest.confirmBaguetteOrder(baguetteOrder2.getId(),datestring, email);
        ArgumentCaptor<BaguetteOrder> baguetteOrderArgumentCaptor = ArgumentCaptor.forClass(BaguetteOrder.class);
        verify(baguetteOrderRepository).save(baguetteOrderArgumentCaptor.capture());
        BaguetteOrder capturedBaguetteOrder = baguetteOrderArgumentCaptor.getValue();

        assertThat(capturedBaguetteOrder).isExactlyInstanceOf(BaguetteOrder.class);

    }
    @Test
    void WontConfirmBaguetteOrder() {
        String email = "email";
        Long baguetteOrderId = 1L;
        String datestring = "02-05-2021, 18:54:35";
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(baguetteOrderId, customer, 2.0, new Date(), 0, "note");

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.confirmBaguetteOrder(baguetteOrder.getId(), datestring, email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, uživatel nenalezen");
        verify(baguetteOrderRepository, never()).save(any());

    }
    @Test
    void Wont2ConfirmBaguetteOrder() {
        String email = "email";
        Long baguetteOrderId = 1L;
        String datestring = "02-05-2021, 18:54:35";
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(baguetteOrderId, customer, 2.0, new Date(), 0, "note");

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.confirmBaguetteOrder(baguetteOrder.getId(), datestring, email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("Objednávka nenelazena");
        verify(baguetteOrderRepository, never()).save(any());
    }
    @Test
    void Wont3ConfirmBaguetteOrder() {
        String email = "email";
        Long baguetteOrderId = 1L;
        String datestring = "02-05-2021, 18:54:35";
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(baguetteOrderId, customer, 2.0, new Date(), 1, "note");

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.of(baguetteOrder));

        assertThatThrownBy(() ->underTest.confirmBaguetteOrder(baguetteOrder.getId(), datestring, email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("Objednávku nelze upravit");
        verify(baguetteOrderRepository, never()).save(any());
    }

    @Test
    void createBaguetteOrder() {
        List<BaguetteOrder> baguetteOrderList = new ArrayList<>();
        String email ="email";
        Long baguetteOrderId = 1L;
        String datestring = "02-05-2021, 18:54:35";
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(baguetteOrderId, customer, 2.0, new Date(), 0,"note");
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findAllByStateAndCustomer_Email(0, email)).willReturn(Optional.empty());

        underTest.createBaguetteOrder(email);
        ArgumentCaptor<BaguetteOrder> baguetteOrderArgumentCaptor = ArgumentCaptor.forClass(BaguetteOrder.class);
        verify(baguetteOrderRepository).save(baguetteOrderArgumentCaptor.capture());
        BaguetteOrder capturedBaguetteOrder = baguetteOrderArgumentCaptor.getValue();

        assertThat(capturedBaguetteOrder).isExactlyInstanceOf(BaguetteOrder.class);
        assertThat(capturedBaguetteOrder.getState()).isEqualTo(baguetteOrder.getState());
        assertThat(capturedBaguetteOrder.getCustomer()).isEqualTo(baguetteOrder.getCustomer());

    }
    @Test
    void ThrowCreateBaguetteOrder() {
        String email ="email";
        Long baguetteOrderId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.empty());
        assertThatThrownBy(() ->underTest.createBaguetteOrder(email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, uživatel nenalezen");
        verify(baguetteOrderRepository, never()).save(any());

    }
    @Test
    void Throw2CreateBaguetteOrder() {
        List<BaguetteOrder> baguetteOrderList = new ArrayList<>();
        String email ="email";
        Long baguetteOrderId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(baguetteOrderId, customer, 2.0, new Date(), 0,"note");
        baguetteOrderList.add(baguetteOrder);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findAllByStateAndCustomer_Email(0, email)).willReturn(Optional.of(baguetteOrderList));

        assertThatThrownBy(() ->underTest.createBaguetteOrder(email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("nejprve dokončete objednávku");
        verify(baguetteOrderRepository, never()).save(any());

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