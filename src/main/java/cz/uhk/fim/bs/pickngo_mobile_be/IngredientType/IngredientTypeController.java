package cz.uhk.fim.bs.pickngo_mobile_be.IngredientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/ingredientType")
public class IngredientTypeController {

    private final IngredientTypeService ingredientTypeService;

    @Autowired
    public IngredientTypeController(IngredientTypeService ingredientTypeService) {
        this.ingredientTypeService = ingredientTypeService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<IngredientType> getIngredientTypes() {
        return ingredientTypeService.getIngredientTypes();
    }
}
