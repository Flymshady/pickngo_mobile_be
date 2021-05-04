package cz.uhk.fim.bs.pickngo_mobile_be.Ingredient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock private IngredientRepository ingredientRepository;
    private IngredientService underTest;

    @BeforeEach
    void setUp() {
        underTest = new IngredientService(ingredientRepository);
    }

    @Test
    void getIngredients() {
        //when
        underTest.getIngredients();
        //then
        verify(ingredientRepository).findAll();
    }

    @Test
    void getIngredientsByName() {
        String name = "name";
        //when
        underTest.getIngredientsByName(name);
        //then
        verify(ingredientRepository).findIngredientByName(name);
    }
}