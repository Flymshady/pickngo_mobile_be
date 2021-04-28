package cz.uhk.fim.bs.pickngo_mobile_be.SpecialOffer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.uhk.fim.bs.pickngo_mobile_be.Item.Item;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class SpecialOffer {
    @Id
    @SequenceGenerator(name="special_offer_sequence", sequenceName = "special_offer_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "special_offer_sequence")
    private Long id;
    private String name;
    private double price;
    @JsonIgnore
    @OneToMany(mappedBy = "specialOffer")
    private List<Item> items;
    private boolean active;

    public SpecialOffer(String name,double price, List<Item> items, boolean active) {
        this.name=name;
        this.price = price;
        this.items = items;
        this.active =active;
    }
    public SpecialOffer(Long id,String name, double price, List<Item> items, boolean active) {
        this.id = id;
        this.name=name;
        this.price = price;
        this.items = items;
        this.active=active;
    }

    public SpecialOffer(Long id, String name, double price, boolean active) {
        this.id = id;
        this.name=name;
        this.price = price;
        this.active=active;
    }

    public SpecialOffer(){
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "SpecialOffer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", items=" + items +
                ", active=" + active +
                '}';
    }
}
