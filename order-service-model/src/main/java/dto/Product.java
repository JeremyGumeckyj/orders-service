package dto;

public class Product {
    private long id;
    private String name;
    private double price;
    private boolean isAvailable;

    protected Product(){}

    public Product(String name,double price,boolean isAvailable){
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
    }
    public long getId(){
        return id;
    }
    public String getName(){
        return name;
    }
}
