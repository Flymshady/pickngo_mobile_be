package cz.uhk.fim.bs.pickngo_mobile_be.Item;

import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteItem.BaguetteItem;
import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteItem.BaguetteItemRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder.BaguetteOrder;
import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder.BaguetteOrderRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.Customer.Customer;
import cz.uhk.fim.bs.pickngo_mobile_be.Customer.CustomerRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.Ingredient.Ingredient;
import cz.uhk.fim.bs.pickngo_mobile_be.Ingredient.IngredientRepository;
import cz.uhk.fim.bs.pickngo_mobile_be.IngredientType.IngredientType;
import cz.uhk.fim.bs.pickngo_mobile_be.IngredientType.IngredientTypeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository underTest;
    @Autowired
    private BaguetteItemRepository baguetteItemRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BaguetteOrderRepository baguetteOrderRepository;
    @Autowired
    private IngredientTypeRepository ingredientTypeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    @AfterEach
    void tearDown(){
        customerRepository.deleteAll();
        ingredientTypeRepository.deleteAll();
        baguetteItemRepository.deleteAll();
        underTest.deleteAll();
        ingredientRepository.deleteAll();
        baguetteOrderRepository.deleteAll();
    }

    @Test
    void findAllByBaguetteItem_Id() {
        Customer customer = new Customer("name", "email", "eshort");
        customerRepository.save(customer);
        BaguetteOrder baguetteOrder = new BaguetteOrder(customer, 2.0, new Date(), 0, "note");
        baguetteOrderRepository.save(baguetteOrder);
        BaguetteItem baguetteItem = new BaguetteItem(baguetteOrder, 2.0, false);
        baguetteItemRepository.save(baguetteItem);
        IngredientType ingredientType = new IngredientType("ingredientTypeName");
        ingredientTypeRepository.save(ingredientType);
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        ingredientRepository.save(ingredient);
        Item item = new Item(2, 2.0,  "name", baguetteItem, ingredient);
        underTest.save(item);
        Optional<List<Item>> list = underTest.findAllByBaguetteItem_Id(baguetteItem.getId());
        assertThat(list.get()).contains(item);
    }

    @Test
    void findByIngredient_IngredientType_NameAndBaguetteItem_Id() {
        Customer customer = new Customer("name", "email", "eshort");
        customerRepository.save(customer);
        BaguetteOrder baguetteOrder = new BaguetteOrder(customer, 2.0, new Date(), 0, "note");
        baguetteOrderRepository.save(baguetteOrder);
        BaguetteItem baguetteItem = new BaguetteItem(baguetteOrder, 2.0, false);
        baguetteItemRepository.save(baguetteItem);
        String ingredientTypeName = "ingredientTypeName";
        IngredientType ingredientType = new IngredientType(ingredientTypeName);
        ingredientTypeRepository.save(ingredientType);
        Ingredient ingredient = new Ingredient("name", 2.0, ingredientType);
        ingredientRepository.save(ingredient);
        Item item = new Item(2, 2.0,  "name", baguetteItem, ingredient);
        underTest.save(item);
        Optional<List<Item>> list = underTest.findByIngredient_IngredientType_NameAndBaguetteItem_Id(ingredientTypeName, baguetteItem.getId());
        assertThat(list.get()).contains(item);
    }
}