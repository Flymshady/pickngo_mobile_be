package cz.uhk.fim.bs.pickngo_mobile_be.Ingredient;

import cz.uhk.fim.bs.pickngo_mobile_be.IngredientType.IngredientType;
import cz.uhk.fim.bs.pickngo_mobile_be.IngredientType.IngredientTypeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class IngredientRepositoryTest {

    @Autowired
    private IngredientRepository underTest;
    @Autowired
    private IngredientTypeRepository ingredientTypeRepository;

    @AfterEach
    void tearDown(){
        ingredientTypeRepository.deleteAll();
        underTest.deleteAll();
    }


    @Test
    void findIngredientByName() {

        IngredientType ingredientType = new IngredientType("ingredientTypeName");
        ingredientTypeRepository.save(ingredientType);

        //given
        String name = "nazev";
        Ingredient ingredient = new Ingredient(name, 1.0, ingredientType );

        underTest.save(ingredient);
        //when
        Optional<Ingredient> ingredientOptional = underTest.findIngredientByName(name);
        //then
        assertThat(ingredientOptional).isPresent();
    }
}