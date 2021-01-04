package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Inventory {
    private ObservableList<Part> allParts;
    private ObservableList<Product> allProducts;

    public Inventory(){
        allParts = FXCollections.observableArrayList();
        allProducts = FXCollections.observableArrayList();
    }


    public ObservableList<Part> getAllParts(){
        return allParts;
    }
    public ObservableList<Product> getAllProducts(){
        return allProducts;
    }

    public void addPart(Part p){
        if (p != null){
            allParts.add(p);
        }

    }
    public void addProduct(Product p){
        if (p != null){
            allProducts.add(p);
        }
    }

    /* use array to hold return value if looking up by ID */
    public Part lookupPart(int partID) {
        Part[] arr = new Part[1];
        for (Part p : this.allParts){
            if (partID == p.getId()){
                arr[0] = p;
            }
            else{
                return null;
            }
        }
        return arr[0];
    }

    /* use arraylist to hold parts that match the substring in the search box */
    public ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> obsli = FXCollections.observableArrayList();
        obsli.clear();
        for (Part p : this.getAllParts()) {
            if (p.getName().toLowerCase().contains(partName)) {
                obsli.add(p);
            }
        }
        return obsli;
    }

    public Product lookupProduct(int productID){
        Product[] arr = new Product[1];
        for (Product p : this.allProducts){
            if (productID == p.getId()){
                arr[0] = p;
            }
            else{
                return null;
            }
        }
        return arr[0];
    }

    public ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> obsli = FXCollections.observableArrayList();
        obsli.clear();
        for (Product p : this.getAllProducts()) {
            if (p.getName().toLowerCase().contains(productName)) {
                obsli.add(p);
            }
        }
        return obsli;
    }

    public void updatePart(Part selected){
        this.addPart(selected);
    }
    public void updateProduct(Product selected){
        this.addProduct(selected);
    }
    public void deletePart(Part p){
        allParts.remove(p);
    }
    public void deleteProduct(Product p){
        allProducts.remove(p);
    }


}
