package cz.uhk.fim.bs.pickngo_mobile_be.Ingredient;

import cz.uhk.fim.bs.pickngo_mobile_be.IngredientType.IngredientType;

import javax.persistence.*;

@Entity
@Table
public class Ingredient {

    @Id
    @SequenceGenerator(name="ingredient_sequence", sequenceName = "ingredient_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingredient_sequence")
    private Long id;
    private String name;
    private double price;
    private IngredientType ingredientType;

    public Ingredient(String name, double price, IngredientType ingredientType) {
        this.name = name;
        this.price = price;
        this.ingredientType = ingredientType;
    }

    public Ingredient(Long id, String name, double price, IngredientType ingredientType) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ingredientType = ingredientType;
    }

    public Ingredient(){

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public IngredientType getIngredientType() {
        return ingredientType;
    }

    public void setIngredientType(IngredientType ingredientType) {
        this.ingredientType = ingredientType;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", ingredientType=" + ingredientType +
                '}';
    }
}
