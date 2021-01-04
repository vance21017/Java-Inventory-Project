package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int min, max, stock, id;
    private String name;
    private double price = 0.0;

    public Product(){
    }
       
    public Product(int id, String name, double price, int stock, int min, int max){
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
    }
        
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    public void deleteAssociatedPart(Part part){
        associatedParts.remove(part);
        }

    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
        }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
