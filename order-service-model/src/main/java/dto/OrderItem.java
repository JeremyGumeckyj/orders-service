package dto;

public class OrderItem {
    private String id;
    private int quantity;
    private String productId;
    public OrderItem() {}
    public OrderItem(String id, int quantity,String productId) {
        this.productId= productId;
        this.id = id;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
