package dto;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "Product")
public class Product {

    @Id
    @Column(name = "id")
    @Type(type ="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "isAvailable")
    private boolean isAvailable;

    protected Product(){
    }

    public Product(UUID id, String name, double price, boolean isAvailable){
        this.id = id;
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }


}
