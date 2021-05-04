package cz.uhk.fim.bs.pickngo_mobile_be.BaguetteItem;

import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder.BaguetteOrder;
import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder.BaguetteOrderRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder.BaguetteOrderService;
import cz.uhk.fim.bs.pickngo_mobile_be.Customer.Customer;
import cz.uhk.fim.bs.pickngo_mobile_be.Customer.CustomerRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.Ingredient.Ingredient;
import cz.uhk.fim.bs.pickngo_mobile_be.IngredientType.IngredientType;
import cz.uhk.fim.bs.pickngo_mobile_be.Item.Item;
import cz.uhk.fim.bs.pickngo_mobile_be.Item.ItemRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.SpecialOffer.SpecialOffer;
import cz.uhk.fim.bs.pickngo_mobile_be.SpecialOffer.SpecialOfferRepository;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BaguetteItemServiceTest {

    @Mock
    private BaguetteItemRepository baguetteItemRepository;
    @Mock
    private BaguetteOrderRepository baguetteOrderRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private SpecialOfferRepository specialOfferRepository;

    private BaguetteItemService underTest;

    @BeforeEach
    void setUp(){underTest = new BaguetteItemService(baguetteItemRepository, baguetteOrderRepository, customerRepository, itemRepository, specialOfferRepository);}

    @Test
    void getBaguetteItems() {
        List<BaguetteItem> baguetteItemList = new ArrayList<>();
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        baguetteItemList.add(baguetteItem);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findById(baguetteOrder.getId())).willReturn(Optional.of(baguetteOrder));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(),email)).willReturn(Optional.of(baguetteOrder));
        given(baguetteItemRepository.findAllByBaguetteOrder_Id(baguetteOrder.getId())).willReturn(Optional.of(baguetteItemList));

        underTest.getBaguetteItems(baguetteOrder.getId(),email);
        verify(baguetteItemRepository).findAllByBaguetteOrder_Id(baguetteOrder.getId());
    }

    @Test
    void ThrowGetBaguetteItems() {
        List<BaguetteItem> baguetteItemList = new ArrayList<>();
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        baguetteItemList.add(baguetteItem);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.getBaguetteItems(baguetteOrder.getId(),email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, uživatel nenalezen");
        verify(baguetteItemRepository, never()).findAllByBaguetteOrder_Id(baguetteOrder.getId());
    }
    @Test
    void Throw2GetBaguetteItems() {
        List<BaguetteItem> baguetteItemList = new ArrayList<>();
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        baguetteItemList.add(baguetteItem);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findById(baguetteOrder.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.getBaguetteItems(baguetteOrder.getId(),email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, objednávka nenalezena");
        verify(baguetteItemRepository, never()).findAllByBaguetteOrder_Id(baguetteOrder.getId());
    }
    @Test
    void Throw3GetBaguetteItems() {
        List<BaguetteItem> baguetteItemList = new ArrayList<>();
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        baguetteItemList.add(baguetteItem);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findById(baguetteOrder.getId())).willReturn(Optional.of(baguetteOrder));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(),email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.getBaguetteItems(baguetteOrder.getId(),email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, objednávka nenalezena");
        verify(baguetteItemRepository, never()).findAllByBaguetteOrder_Id(baguetteOrder.getId());
    }

    @Test
    void getBaguetteItem() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.of(baguetteItem));

        underTest.getBaguetteItem(baguetteItemId,email);
        verify(baguetteItemRepository).findById(baguetteItemId);
    }
    @Test
    void ThrowGetBaguetteItem() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.getBaguetteItem(baguetteItemId,email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, uživatel nenalezen");
        verify(baguetteItemRepository, never()).findById(baguetteItemId);
    }
    @Test
    void Throw2GetBaguetteItem() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.getBaguetteItem(baguetteItemId,email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, položka nenalezena");

    }

    @Test
    void Throw3GetBaguetteItem() {
        String email ="email_";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", "email", "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(baguetteItemId, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.of(baguetteItem));

        assertThatThrownBy(() ->underTest.getBaguetteItem(baguetteItemId,email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, štné údaje");
    }

    @Test
    void removeBaguetteItem() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.of(baguetteItem));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.of(baguetteOrder));
        given(baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(baguetteOrder.getState(), email)).willReturn(Optional.of(baguetteOrder));

        underTest.removeBaguetteItem(baguetteItemId,email);
        verify(baguetteItemRepository).delete(baguetteItem);

    }
    @Test
    void ThrowRemoveBaguetteItem() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.removeBaguetteItem(baguetteItemId,email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, uživatel nenalezen");

    }
    @Test
    void Throw2RemoveBaguetteItem() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.removeBaguetteItem(baguetteItemId,email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, položka nenalezena");

    }
    @Test
    void Throw3RemoveBaguetteItem() {
        String email ="email_";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", "email", "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.of(baguetteItem));

        assertThatThrownBy(() ->underTest.removeBaguetteItem(baguetteItemId,email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, špatné údaje");
    }
    @Test
    void Throw4RemoveBaguetteItem() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", "email", "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.of(baguetteItem));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.removeBaguetteItem(baguetteItemId,email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("Objednávka nenelazena");
    }
    @Test
    void Throw5RemoveBaguetteItem() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", "email", "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 1,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.of(baguetteItem));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.of(baguetteOrder));

        assertThatThrownBy(() ->underTest.removeBaguetteItem(baguetteItemId,email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("Objednávka již nelze měnit");
    }
    @Test
    void Throw6RemoveBaguetteItem() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", "email", "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.of(baguetteItem));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.of(baguetteOrder));
        given(baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(baguetteOrder.getState(), email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.removeBaguetteItem(baguetteItemId,email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("Objednávka nenelazena");
    }

    @Test
    void createBaguetteItem() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", "email", "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findById(baguetteOrder.getId())).willReturn(Optional.of(baguetteOrder));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.of(baguetteOrder));

        underTest.createBaguetteItem(baguetteItemId,email);
        ArgumentCaptor<BaguetteItem> baguetteItemArgumentCaptor = ArgumentCaptor.forClass(BaguetteItem.class);
        verify(baguetteItemRepository).save(baguetteItemArgumentCaptor.capture());
        BaguetteItem capturedBaguetteItem = baguetteItemArgumentCaptor.getValue();

        assertThat(capturedBaguetteItem).isExactlyInstanceOf(BaguetteItem.class);
        assertThat(capturedBaguetteItem.isOffer()).isEqualTo(baguetteItem.isOffer());
        assertThat(capturedBaguetteItem.getBaguetteOrder()).isEqualTo(baguetteOrder);
    }
    @Test
    void ThrowCreateBaguetteItem() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.createBaguetteItem(baguetteOrder.getId(),email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, uživatel nenalezen");
    }
    @Test
    void Throw2CreateBaguetteItem() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findById(baguetteOrder.getId())).willReturn(Optional.empty());
        assertThatThrownBy(() ->underTest.createBaguetteItem(baguetteOrder.getId(),email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, objednávka nenalezena");
    }
    @Test
    void Throw3CreateBaguetteItem() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findById(baguetteOrder.getId())).willReturn(Optional.of(baguetteOrder));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.createBaguetteItem(baguetteOrder.getId(),email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("Objednávka nenalezena");
    }
    @Test
    void Throw4CreateBaguetteItem() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 1,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findById(baguetteOrder.getId())).willReturn(Optional.of(baguetteOrder));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.of(baguetteOrder));

        assertThatThrownBy(() ->underTest.createBaguetteItem(baguetteOrder.getId(),email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("Objednávka již nelze měnit");
    }


    @Test
    void createBaguetteItemFromSpecialOffer() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", "email", "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, true);
        List<BaguetteItem> baguetteItemList = new ArrayList<>();
        baguetteItemList.add(baguetteItem);
        baguetteOrder.setBaguetteItems(baguetteItemList);
        List<Item> items = new ArrayList<>();
        SpecialOffer specialOffer = new SpecialOffer(1L,"name", 20.0, items,true);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findById(baguetteOrder.getId())).willReturn(Optional.of(baguetteOrder));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.of(baguetteOrder));
        given(specialOfferRepository.findById(specialOffer.getId())).willReturn(Optional.of(specialOffer));

        underTest.createBaguetteItemFromSpecialOffer(baguetteOrder.getId(), specialOffer.getId(),email);
        ArgumentCaptor<BaguetteItem> baguetteItemArgumentCaptor = ArgumentCaptor.forClass(BaguetteItem.class);
        verify(baguetteItemRepository).save(baguetteItemArgumentCaptor.capture());
        BaguetteItem capturedBaguetteItem = baguetteItemArgumentCaptor.getValue();

        assertThat(capturedBaguetteItem).isExactlyInstanceOf(BaguetteItem.class);
        assertThat(capturedBaguetteItem.isOffer()).isEqualTo(baguetteItem.isOffer());
        assertThat(capturedBaguetteItem.getPrice()).isEqualTo(specialOffer.getPrice());
        assertThat(capturedBaguetteItem.getItems()).isEqualTo(items);
    }
    @Test
    void ThrowCreateBaguetteItemFromSpecialOffer() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", "email", "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, true);
        List<BaguetteItem> baguetteItemList = new ArrayList<>();
        baguetteItemList.add(baguetteItem);
        baguetteOrder.setBaguetteItems(baguetteItemList);
        List<Item> items = new ArrayList<>();
        SpecialOffer specialOffer = new SpecialOffer(1L,"name", 20.0, items,true);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.createBaguetteItemFromSpecialOffer(baguetteOrder.getId(), specialOffer.getId(),email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, uživatel nenalezen");
    }
    @Test
    void Throw2CreateBaguetteItemFromSpecialOffer() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", "email", "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, true);
        List<BaguetteItem> baguetteItemList = new ArrayList<>();
        baguetteItemList.add(baguetteItem);
        baguetteOrder.setBaguetteItems(baguetteItemList);
        List<Item> items = new ArrayList<>();
        SpecialOffer specialOffer = new SpecialOffer(1L,"name", 20.0, items,true);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findById(baguetteOrder.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.createBaguetteItemFromSpecialOffer(baguetteOrder.getId(), specialOffer.getId(),email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, objednávka nenalezena");
    }
    @Test
    void Throw3CreateBaguetteItemFromSpecialOffer() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", "email", "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, true);
        List<BaguetteItem> baguetteItemList = new ArrayList<>();
        baguetteItemList.add(baguetteItem);
        baguetteOrder.setBaguetteItems(baguetteItemList);
        List<Item> items = new ArrayList<>();
        SpecialOffer specialOffer = new SpecialOffer(1L,"name", 20.0, items,true);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findById(baguetteOrder.getId())).willReturn(Optional.of(baguetteOrder));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.createBaguetteItemFromSpecialOffer(baguetteOrder.getId(), specialOffer.getId(),email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("Objednávka nenelazena");
    }
    @Test
    void Throw4CreateBaguetteItemFromSpecialOffer() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", "email", "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 1,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, true);
        List<BaguetteItem> baguetteItemList = new ArrayList<>();
        baguetteItemList.add(baguetteItem);
        baguetteOrder.setBaguetteItems(baguetteItemList);
        List<Item> items = new ArrayList<>();
        SpecialOffer specialOffer = new SpecialOffer(1L,"name", 20.0, items,true);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findById(baguetteOrder.getId())).willReturn(Optional.of(baguetteOrder));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.of(baguetteOrder));

        assertThatThrownBy(() ->underTest.createBaguetteItemFromSpecialOffer(baguetteOrder.getId(), specialOffer.getId(),email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("Objednávka již nelze měnit");
    }
    @Test
    void Throw5CreateBaguetteItemFromSpecialOffer() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", "email", "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, true);
        List<BaguetteItem> baguetteItemList = new ArrayList<>();
        baguetteItemList.add(baguetteItem);
        baguetteOrder.setBaguetteItems(baguetteItemList);
        List<Item> items = new ArrayList<>();
        SpecialOffer specialOffer = new SpecialOffer(1L,"name", 20.0, items,true);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findById(baguetteOrder.getId())).willReturn(Optional.of(baguetteOrder));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.of(baguetteOrder));
        given(specialOfferRepository.findById(specialOffer.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.createBaguetteItemFromSpecialOffer(baguetteOrder.getId(), specialOffer.getId(),email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("Speciální nabídka nenelazena");
    }
    @Test
    void Throw6CreateBaguetteItemFromSpecialOffer() {
        String email ="email";
        Long baguetteItemId = 1L;
        Customer customer = new Customer("name", "email", "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, true);
        List<BaguetteItem> baguetteItemList = new ArrayList<>();
        baguetteItemList.add(baguetteItem);
        baguetteOrder.setBaguetteItems(baguetteItemList);
        List<Item> items = new ArrayList<>();
        SpecialOffer specialOffer = new SpecialOffer(1L,"name", 20.0, items,false);
        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findById(baguetteOrder.getId())).willReturn(Optional.of(baguetteOrder));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.of(baguetteOrder));
        given(specialOfferRepository.findById(specialOffer.getId())).willReturn(Optional.of(specialOffer));

        assertThatThrownBy(() ->underTest.createBaguetteItemFromSpecialOffer(baguetteOrder.getId(), specialOffer.getId(),email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("Objednávka nenelazena");
    }
}