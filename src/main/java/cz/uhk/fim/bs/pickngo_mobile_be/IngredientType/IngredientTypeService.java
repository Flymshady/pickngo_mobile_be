package cz.uhk.fim.bs.pickngo_mobile_be.IngredientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientTypeService {

    private final IngredientTypeRepository ingredientTypeRepository;

    @Autowired
    public IngredientTypeService(IngredientTypeRepository ingredientTypeRepository) {
        this.ingredientTypeRepository = ingredientTypeRepository;
    }

    public List<IngredientType> getIngredientTypes() {
        return ingredientTypeRepository.findAll();
    }
}
