package edu.uga.cs.roommateshoppingdemo;

public class Shopping {
    private String key;
    private String itemName;
    private String category;
    private int quantity;
    private double price;

    public Shopping(){
        this.key = null;
        this.itemName = null;
        this.category = null;
        this.quantity = 0;
        this.price = 0.0;
    }

    public Shopping( String itemName, String category, int quantity, double price ) {
        this.key = null;
        this.itemName = itemName;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
    }

    public void setKey(String key) { this.key = key; }

    public String getKey() { return key; };

    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getItemName(){ return itemName; }

    public void setCategory(String category){ this.category = category; }

    public String getCategory(){ return category; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getQuantity(){ return quantity; }

    public void setPrice(double price) { this.price = price; }

    public double getPrice(){ return price; }
}
