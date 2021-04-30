package cz.uhk.fim.bs.pickngo_mobile_be.IngredientType;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class IngredientTypeRepositoryTest {

    @Autowired
    private IngredientTypeRepository underTest;

    @AfterEach
    void tearDown(){
        underTest.deleteAll();
    }

    @Test
    void findIngredientTypeByName() {
        //given
        String name = "nazev";
        IngredientType ingredientType = new IngredientType(name);

        underTest.save(ingredientType);
        //when
        Optional<IngredientType> ingredientTypeOptional = underTest.findIngredientTypeByName(name);
        //then
        assertThat(ingredientTypeOptional).isPresent();
    }


    @Test
    void itShouldNotFindIngredientTypeByName() {
        //given
        String name= "nazev";
        //when
        Optional<IngredientType> optionalIngredientType = underTest.findIngredientTypeByName(name);
        //then
        assertThat(optionalIngredientType).isNotPresent();
    }
}