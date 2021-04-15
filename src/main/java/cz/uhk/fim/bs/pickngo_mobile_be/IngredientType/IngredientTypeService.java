package cz.uhk.fim.bs.pickngo_mobile_be.IngredientType;

import cz.uhk.fim.bs.pickngo_mobile_be.Ingredient.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientTypeService {

    private final IngredientTypeRepository ingredientTypeRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientTypeService(IngredientTypeRepository ingredientTypeRepository, IngredientRepository ingredientRepository) {
        this.ingredientTypeRepository = ingredientTypeRepository;
        this.ingredientRepository = ingredientRepository;
    }


    public List<IngredientType> getIngredientTypes() {
        return ingredientTypeRepository.findAll();
    }
}
