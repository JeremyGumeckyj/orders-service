package dto;

public class Product {
    private String id;
    private String name;
    private double price;
    private boolean isAvailable;

    protected Product(){}

    public Product(String id, String name,double price,boolean isAvailable){
        this.id = id;
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
    }
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setId(String id) {
        this.id = id;
    }
}
