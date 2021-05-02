package cz.uhk.fim.bs.pickngo_mobile_be.IngredientType;

import cz.uhk.fim.bs.pickngo_mobile_be.Ingredient.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IngredientTypeServiceTest {

    @Mock
    private IngredientTypeRepository ingredientTypeRepository;
    @Mock private IngredientRepository ingredientRepository;
    private IngredientTypeService underTest;

    @BeforeEach
    void setUp() {
        underTest = new IngredientTypeService(ingredientTypeRepository, ingredientRepository);
    }

    @Test
    void getIngredientTypes() {
        //when
        underTest.getIngredientTypes();
        //then
        verify(ingredientTypeRepository).findAll();
    }
}