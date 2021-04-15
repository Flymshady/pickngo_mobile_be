package cz.uhk.fim.bs.pickngo_mobile_be.Ingredient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> getIngredients() {
        return ingredientRepository.findAll();
    }

    public Optional<Ingredient> getIngredientsByName(String ingredientName) {
        return ingredientRepository.findIngredientByName(ingredientName);
    }

}
