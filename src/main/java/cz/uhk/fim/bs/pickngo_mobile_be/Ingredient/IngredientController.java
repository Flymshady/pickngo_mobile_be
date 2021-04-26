package cz.uhk.fim.bs.pickngo_mobile_be.Ingredient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path="/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Ingredient> getIngredients() {
        return ingredientService.getIngredients();
    }

    @RequestMapping(value = "/detail/{ingredientName}", method = RequestMethod.GET)
    public Optional<Ingredient> getIngredientByName(@PathVariable("ingredientName") String ingredientName) {
        return ingredientService.getIngredientsByName(ingredientName);
    }
}
