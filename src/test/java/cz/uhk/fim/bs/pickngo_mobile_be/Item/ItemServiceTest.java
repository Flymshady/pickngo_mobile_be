package cz.uhk.fim.bs.pickngo_mobile_be.Item;

import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteItem.BaguetteItem;
import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteItem.BaguetteItemRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder.BaguetteOrder;
import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder.BaguetteOrderRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.Customer.Customer;
import cz.uhk.fim.bs.pickngo_mobile_be.Customer.CustomerRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.Ingredient.Ingredient;
import cz.uhk.fim.bs.pickngo_mobile_be.IngredientType.IngredientType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private BaguetteItemRepository baguetteItemRepository;
    @Mock
    private BaguetteOrderRepository baguetteOrderRepository;

    private ItemService underTest;

    @BeforeEach
    void setUp(){underTest = new ItemService(customerRepository, itemRepository, baguetteItemRepository, baguetteOrderRepository);}

    @Test
    void getItems() {
        String email ="email";
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(1L, baguetteOrder, 20.0, false);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.of(baguetteItem));

        underTest.getItems(baguetteItem.getId(), email);
        verify(itemRepository).findAllByBaguetteItem_Id(baguetteItem.getId());
    }
    @Test
    void ThrowGetItems() {
        String email ="email";
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(1L, baguetteOrder, 20.0, false);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.getItems(baguetteItem.getId(), email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, uživatel nenalezen");
        verify(itemRepository, never()).findAllByBaguetteItem_Id(baguetteItem.getId());
    }
    @Test
    void Throw2GetItems() {
        String email ="email";
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(1L, baguetteOrder, 20.0, false);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.getItems(baguetteItem.getId(), email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, položka nenalezena");
        verify(itemRepository, never()).findAllByBaguetteItem_Id(baguetteItem.getId());
    }
    @Test
    void Throw3GetItems() {
        String email ="email_jiny";
        Customer customer = new Customer("name", "email", "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(1L, baguetteOrder, 20.0, false);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.getItems(baguetteItem.getId(), email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba oprávnění");
        verify(itemRepository, never()).findAllByBaguetteItem_Id(baguetteItem.getId());

    }

    @Test
    void addNewItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(2, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.of(baguetteItem));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.of(baguetteOrder));

        underTest.addNewItem(item, email, baguetteItemId);

        ArgumentCaptor<Item> itemArgumentCaptor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository).save(itemArgumentCaptor.capture());
        Item capturedItem = itemArgumentCaptor.getValue();

        assertThat(capturedItem).isExactlyInstanceOf(Item.class);
        assertThat(capturedItem.getAmount()).isEqualTo(item.getAmount());
        assertThat(capturedItem.getPrice()).isEqualTo(item.getPrice());
        assertThat(capturedItem.getName()).isEqualTo(item.getName());
        assertThat(capturedItem.getIngredient()).isEqualTo(item.getIngredient());
        assertThat(capturedItem.getBaguetteItem()).isEqualTo(item.getBaguetteItem());
        assertThat(capturedItem.getId()).isEqualTo(item.getId());
    }

    @Test
    void ThrowAddNewItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(2, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.addNewItem(item, email, baguetteItemId))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, uživatel nenalezen");
        verify(itemRepository, never()).save(any());
    }
    @Test
    void Throw2AddNewItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(2, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.addNewItem(item, email, baguetteItemId))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, položka s id "+baguetteItemId+" nenalezena");
        verify(itemRepository, never()).save(any());
    }

    @Test
    void Throw3AddNewItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, true);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(2, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.of(baguetteItem));

        assertThatThrownBy(() ->underTest.addNewItem(item, email, baguetteItemId))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("Specialní nabídku nelze měnit");
        verify(itemRepository, never()).save(any());
    }

    @Test
    void Throw4AddNewItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(2, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.of(baguetteItem));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.addNewItem(item, email, baguetteItemId))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, objednávka nenalezena");
        verify(itemRepository, never()).save(any());
    }

    @Test
    void Throw5AddNewItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 1,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(2, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.of(baguetteItem));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.addNewItem(item, email, baguetteItemId))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, objednávku nelze měnit");
        verify(itemRepository, never()).save(any());
    }

    @Test
    void Throw6AddNewItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("Pečivo");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(2, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.of(baguetteItem));
        given(baguetteOrderRepository.findBaguetteOrderByIdAndCustomer_Email(baguetteOrder.getId(), email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.addNewItem(item, email, baguetteItemId))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("Nelze přidat více pečiva do jedné bagety :)");
        verify(itemRepository, never()).save(any());
    }

    @Test
    void updateItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(1L,2, 2.0,"name", baguetteItem,ingredient);
        Item item2 = new Item(1L,40, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(baguetteOrder.getState(), email)).willReturn(Optional.of(baguetteOrder));
        given(itemRepository.findById(item2.getId())).willReturn(Optional.of(item));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.of(baguetteItem));
        given(baguetteOrderRepository.findById(baguetteOrder.getId())).willReturn(Optional.of(baguetteOrder));

        underTest.updateItem(item2.getId(), email, item2.getAmount());

        ArgumentCaptor<Item> itemArgumentCaptor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository).save(itemArgumentCaptor.capture());
        Item capturedItem = itemArgumentCaptor.getValue();

        assertThat(capturedItem).isExactlyInstanceOf(Item.class);
        assertThat(capturedItem.getAmount()).isEqualTo(item2.getAmount());
        assertThat(capturedItem.getPrice()).isEqualTo(item2.getPrice());
        assertThat(capturedItem.getName()).isEqualTo(item2.getName());
        assertThat(capturedItem.getIngredient()).isEqualTo(item2.getIngredient());
        assertThat(capturedItem.getBaguetteItem()).isEqualTo(item2.getBaguetteItem());
        assertThat(capturedItem.getId()).isEqualTo(item2.getId());
    }
    @Test
    void ThrowUpdateItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(1L,2, 2.0,"name", baguetteItem,ingredient);
        Item item2 = new Item(1L,40, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.updateItem(item2.getId(), email, item2.getAmount()))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, uživatel nenalezen");
        verify(itemRepository, never()).save(any());
    }
    @Test
    void Throw2UpdateItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 1,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(1L,2, 2.0,"name", baguetteItem,ingredient);
        Item item2 = new Item(1L,40, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(0, email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.updateItem(item2.getId(), email, item2.getAmount()))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, objednávka nenalezen");
        verify(itemRepository, never()).save(any());
    }
    @Test
    void Throw3UpdateItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(1L,2, 2.0,"name", baguetteItem,ingredient);
        Item item2 = new Item(1L,40, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(0, email)).willReturn(Optional.of(baguetteOrder));

        assertThatThrownBy(() ->underTest.updateItem(item2.getId(), email, item2.getAmount()))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, objednávka nenalezen");
        verify(itemRepository, never()).save(any());
    }
    @Test
    void Throw4UpdateItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(1L,2, 2.0,"name", baguetteItem,ingredient);
        Item item2 = new Item(1L,40, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(0, email)).willReturn(Optional.of(baguetteOrder));
        given(itemRepository.findById(item2.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.updateItem(item2.getId(), email, item2.getAmount()))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, položka nenalezena");
        verify(itemRepository, never()).save(any());
    }

    @Test
    void Throw5UpdateItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("Pečivo");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(1L,2, 2.0,"name", baguetteItem,ingredient);
        Item item2 = new Item(1L,40, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(0, email)).willReturn(Optional.of(baguetteOrder));
        given(itemRepository.findById(item2.getId())).willReturn(Optional.of(item));

        assertThatThrownBy(() ->underTest.updateItem(item2.getId(), email, item2.getAmount()))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("Nelze přidat více pečiva do jedné bagety :)");
        verify(itemRepository, never()).save(any());
    }

    @Test
    void Throw6UpdateItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(1L,2, 2.0,"name", baguetteItem,ingredient);
        Item item2 = new Item(1L,40, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(0, email)).willReturn(Optional.of(baguetteOrder));
        given(itemRepository.findById(item2.getId())).willReturn(Optional.of(item));
        given(baguetteItemRepository.findById(baguetteItemId)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.updateItem(item2.getId(), email, item2.getAmount()))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, položka nenalezena");
        verify(itemRepository, never()).save(any());
    }

    @Test
    void Throw7UpdateItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, true);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(1L,2, 2.0,"name", baguetteItem,ingredient);
        Item item2 = new Item(1L,40, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(0, email)).willReturn(Optional.of(baguetteOrder));
        given(itemRepository.findById(item2.getId())).willReturn(Optional.of(item));
        given(baguetteItemRepository.findById(baguetteItemId)).willReturn(Optional.of(baguetteItem));

        assertThatThrownBy(() ->underTest.updateItem(item2.getId(), email, item2.getAmount()))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, položka nenalezena");
        verify(itemRepository, never()).save(any());
    }

    @Test
    void Throw8UpdateItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(1L,2, 2.0,"name", baguetteItem,ingredient);
        Item item2 = new Item(1L,40, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(0, email)).willReturn(Optional.of(baguetteOrder));
        given(itemRepository.findById(item2.getId())).willReturn(Optional.of(item));
        given(baguetteItemRepository.findById(baguetteItemId)).willReturn(Optional.of(baguetteItem));
        given(baguetteOrderRepository.findById(baguetteOrder.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.updateItem(item2.getId(), email, item2.getAmount()))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, objednávka nenalezena");
        verify(itemRepository, never()).save(any());
    }

    @Test
    void Throw9UpdateItem() {
        String email ="email_jiny";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("Pečivo");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(1L,2, 2.0,"name", baguetteItem,ingredient);
        Item item2 = new Item(1L,40, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(0, email)).willReturn(Optional.of(baguetteOrder));
        given(itemRepository.findById(item2.getId())).willReturn(Optional.of(item));

        assertThatThrownBy(() ->underTest.updateItem(item2.getId(), email, item2.getAmount()))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba oprávnění");
        verify(itemRepository, never()).save(any());
    }
    @Test
    void removeItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(1L,2, 2.0,"name", baguetteItem,ingredient);
        Item item2 = new Item(1L,40, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(baguetteOrder.getState(), email)).willReturn(Optional.of(baguetteOrder));
        given(itemRepository.findById(item2.getId())).willReturn(Optional.of(item));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.of(baguetteItem));

        underTest.removeItem(item2.getId(), email);
        verify(itemRepository).delete(item);
    }
    @Test
    void ThrowRemoveItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(1L,2, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.empty());
        assertThatThrownBy(() ->underTest.removeItem(item.getId(), email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, uživatel nenalezen");
        verify(itemRepository, never()).delete(any());
    }
    @Test
    void Throw2RemoveItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(1L,2, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(baguetteOrder.getState(), email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.removeItem(item.getId(), email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, objednávka nenalezena");
        verify(itemRepository, never()).delete(any());
    }
    @Test
    void Throw3RemoveItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(1L,2, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(baguetteOrder.getState(), email)).willReturn(Optional.of(baguetteOrder));
        given(itemRepository.findById(item.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() ->underTest.removeItem(item.getId(), email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, položka nenalezena");
        verify(itemRepository, never()).delete(any());
    }

    @Test
    void Throw4RemoveItem() {
        String email ="email_jiny";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(1L,2, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(baguetteOrder.getState(), email)).willReturn(Optional.of(baguetteOrder));
        given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));

        assertThatThrownBy(() ->underTest.removeItem(item.getId(), email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba oprávnění");
        verify(itemRepository, never()).delete(any());
    }
    @Test
    void Throw5RemoveItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, false);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(1L,2, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(baguetteOrder.getState(), email)).willReturn(Optional.of(baguetteOrder));
        given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.empty());


        assertThatThrownBy(() ->underTest.removeItem(item.getId(), email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("chyba, položka nenalezena");
        verify(itemRepository, never()).delete(any());
    }
    @Test
    void Throw6RemoveItem() {
        String email ="email";
        Long baguetteItemId = 1l;
        Customer customer = new Customer("name", email, "emailshort");
        BaguetteOrder baguetteOrder = new BaguetteOrder(1L, customer, 2.0, new Date(), 0,"note");
        BaguetteItem baguetteItem= new BaguetteItem(baguetteItemId, baguetteOrder, 20.0, true);
        IngredientType ingredientType = new IngredientType("name");
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        Item item = new Item(1L,2, 2.0,"name", baguetteItem,ingredient);

        given(customerRepository.findCustomerByEmail(email)).willReturn(Optional.of(customer));
        given(baguetteOrderRepository.findBaguetteOrderByStateAndCustomer_Email(baguetteOrder.getState(), email)).willReturn(Optional.of(baguetteOrder));
        given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));
        given(baguetteItemRepository.findById(baguetteItem.getId())).willReturn(Optional.of(baguetteItem));


        assertThatThrownBy(() ->underTest.removeItem(item.getId(), email))
                .isInstanceOf(ResponseStatusException.class).withFailMessage("Specialní nabídku nelze měnit");
        verify(itemRepository, never()).delete(any());
    }
}