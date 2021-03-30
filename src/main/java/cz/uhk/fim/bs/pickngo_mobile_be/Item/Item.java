package cz.uhk.fim.bs.pickngo_mobile_be.Item;

import cz.uhk.fim.bs.pickngo_web_be.Ingredient.Ingredient;
import cz.uhk.fim.bs.pickngo_web_be.Order.Order;

import javax.persistence.*;

@Entity
@Table
public class Item {

    @Id
    @SequenceGenerator(name="item_sequence", sequenceName = "item_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_sequence")
    private Long id;
    private int amount;
    private Order order;
    private Ingredient ingredient;

    public Item(Long id, int amount, Order order, Ingredient ingredient) {
        this.id = id;
        this.amount = amount;
        this.order = order;
        this.ingredient = ingredient;
    }

    public Item(int amount, Order order, Ingredient ingredient) {
        this.amount = amount;
        this.order = order;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", amount=" + amount +
                ", order=" + order +
                ", ingredient=" + ingredient +
                '}';
    }
}
