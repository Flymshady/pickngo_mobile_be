package cz.uhk.fim.bs.pickngo_mobile_be.Item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteItem.BaguetteItem;
import cz.uhk.fim.bs.pickngo_mobile_be.Ingredient.Ingredient;
import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder.BaguetteOrder;
import cz.uhk.fim.bs.pickngo_mobile_be.SpecialOffer.SpecialOffer;

import javax.persistence.*;

@Entity
@Table
public class Item {

    @Id
    @SequenceGenerator(name="item_sequence", sequenceName = "item_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_sequence")
    private Long id;
    private int amount;
    private double price;
    private String name;
    @JsonIgnore
    @ManyToOne
    private BaguetteItem baguetteItem;
    @JsonIgnore
    @ManyToOne
    private SpecialOffer specialOffer;
    @ManyToOne
    private Ingredient ingredient;

    public Item(Long id, int amount, double price, String name, BaguetteItem baguetteItem,SpecialOffer specialOffer, Ingredient ingredient) {
        this.id = id;
        this.amount = amount;
        this.price = price;
        this.name=name;
        this.baguetteItem = baguetteItem;
        this.specialOffer=specialOffer;
        this.ingredient = ingredient;
    }

    public Item(int amount, double price, String name, BaguetteItem baguetteItem,SpecialOffer specialOffer, Ingredient ingredient) {
        this.amount = amount;
        this.price = price;
        this.name = name;
        this.baguetteItem = baguetteItem;
        this.specialOffer =specialOffer;
        this.ingredient = ingredient;
    }

    public Item(int amount, double price, String name, BaguetteItem baguetteItem, Ingredient ingredient) {
        this.amount = amount;
        this.price = price;
        this.name = name;
        this.baguetteItem = baguetteItem;
        this.ingredient = ingredient;
    }

    public Item(Long id, int amount, double price, String name, BaguetteItem baguetteItem, Ingredient ingredient) {
        this.id=id;
        this.amount = amount;
        this.price = price;
        this.name = name;
        this.baguetteItem = baguetteItem;
        this.ingredient = ingredient;
    }

    public Item(int amount, double price, String name, Ingredient ingredient) {
        this.amount = amount;
        this.price = price;
        this.name = name;
        this.ingredient = ingredient;
    }

    public Item(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BaguetteItem getBaguetteItem() {
        return baguetteItem;
    }

    public void setBaguetteItem(BaguetteItem baguetteItem) {
        this.baguetteItem = baguetteItem;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SpecialOffer getSpecialOffer() {
        return specialOffer;
    }

    public void setSpecialOffer(SpecialOffer specialOffer) {
        this.specialOffer = specialOffer;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", amount=" + amount +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", baguetteItem=" + baguetteItem +
                ", specialOffer=" + specialOffer +
                ", ingredient=" + ingredient +
                '}';
    }
}
