package dto;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @Column(name = "id")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "userId")
    private UUID userId;

    public Order() {
    }

    public Order(UUID id, String name, UUID userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}