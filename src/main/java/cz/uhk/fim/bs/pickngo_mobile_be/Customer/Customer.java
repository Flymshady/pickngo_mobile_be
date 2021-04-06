package cz.uhk.fim.bs.pickngo_mobile_be.Customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder.BaguetteOrder;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Customer {
    @Id
    @SequenceGenerator(name="customer_sequence", sequenceName = "customer_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_sequence")
    private Long id;
    private String name;
    private String email;
    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<BaguetteOrder> baguetteOrders;

    public Customer() {
    }

    public Customer(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Customer(String name, String email, List<BaguetteOrder> baguetteOrders) {
        this.name = name;
        this.email = email;
        this.baguetteOrders = baguetteOrders;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", baguetteOrders=" + baguetteOrders +
                '}';
    }
}
