package cz.uhk.fim.bs.pickngo_mobile_be.IngredientType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.uhk.fim.bs.pickngo_web_be.Ingredient.Ingredient;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class IngredientType {

    @Id
    @SequenceGenerator(name="ingredient_type_sequence", sequenceName = "ingredient_type_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingredient_type_sequence")
    private Long id;
    private String name;
    @JsonIgnore
    private List<Ingredient> ingredients;

    public IngredientType(String name, List<Ingredient> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public IngredientType(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public IngredientType(String name) {
        this.name = name;
    }

    public IngredientType(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "IngredientType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }
}
