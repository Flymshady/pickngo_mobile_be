package cz.uhk.fim.bs.pickngo_mobile_be.Order;

import cz.uhk.fim.bs.pickngo_mobile_be.User.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Order {

    @Id
    @SequenceGenerator(name="order_sequence", sequenceName = "order_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequence")
    private Long id;
    private User user;
    private double price;
    private Date date;

    public Order(User user, double price, Date date) {
        this.user = user;
        this.price = price;
        this.date = date;
    }

    public Order(Long id, User user, double price, Date date) {
        this.id = id;
        this.user = user;
        this.price = price;
        this.date = date;
    }

    public Order(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}
