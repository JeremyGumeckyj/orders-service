package dto;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String id;
    private String name;
    private String userId;
    private List<String> orderItemIds;
    public Order() {
    }

    public Order(String id, String name, String userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.orderItemIds = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public List<String> getOrderItemIds() {
        return orderItemIds;
    }
}
