package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    Inventory inv;

    public MainScreenController(Inventory inv){
        this.inv = inv;
    }

    @FXML private AnchorPane mainPane;
    @FXML private Button searchForPartButton;
    @FXML private TextField searchPartTextField;
    @FXML private Button addPartButton;
    @FXML private Button modifyPartButton;
    @FXML private Button deletePartButton;
    @FXML private Button searchForProductButton;
    @FXML private TextField searchProductTextField;
    @FXML private Button addProductButton;
    @FXML private Button modifyProductButton;
    @FXML private Button deleteProduct;

    /* observable lists for all and searched Parts and Products */
    private ObservableList<Part> partObservableList = FXCollections.observableArrayList();
    private ObservableList<Product> productObservableList = FXCollections.observableArrayList();
    private ObservableList<Part> partSearchObservableList = FXCollections.observableArrayList();
    private ObservableList<Product> productSearchObservableList = FXCollections.observableArrayList();


    /* configure Parts table of MainScreenView */
    @FXML private TableView<Part> mainPartsTableView;
    @FXML private TableColumn<Part, Integer> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partInventoryColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn;

    /* configure Products table of MainScreenView */
    @FXML private TableView<Product> mainProductsTableView;
    @FXML private TableColumn<Product, Integer> productIdColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productInventoryColumn;
    @FXML private TableColumn<Product, Double> productPriceColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //configure Parts tableview columns
        partIdColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        //configure Products tableview columns
        productIdColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productInventoryColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));

        //Add inventory Parts to Parts observablelist and populate tableview
        partObservableList.setAll(inv.getAllParts());
        mainPartsTableView.setItems(partObservableList);

        //Add inventory Products to Product observablelist and populate tableview
        productObservableList.setAll(inv.getAllProducts());
        mainProductsTableView.setItems(productObservableList);

    }

    @FXML
    void deleteSelectedPart(MouseEvent event){
        Part selectedPart = mainPartsTableView.getSelectionModel().getSelectedItem();
        inv.deletePart(selectedPart);
        mainPartsTableView.setItems(inv.getAllParts());
        for (Product p : inv.getAllProducts()){
            p.deleteAssociatedPart(selectedPart);
        }
    }
    @FXML void deleteSelectedProduct(MouseEvent event) {
        Product selectedProduct = mainProductsTableView.getSelectionModel().getSelectedItem();
        inv.deleteProduct(selectedProduct);
        mainProductsTableView.setItems(inv.getAllProducts());
    }

    @FXML
    void goToAddPartsView(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/AddPartsView.fxml"));
        AddPartsController partsController = new AddPartsController(inv);
        loader.setController(partsController);
        Parent newRoot = loader.load();

        Scene addPartsScene = new Scene(newRoot);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(addPartsScene);
        window.show();
    }

    @FXML
    void goToAddProductsView(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/AddProductsView.fxml"));
        AddProductsController productsController = new AddProductsController(inv);
        loader.setController(productsController);
        Parent newRoot = loader.load();

        Scene addProducts = new Scene(newRoot);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(addProducts);
        window.show();
    }


    /* modify Part, and show alert of a part is not selected */
    @FXML
    void goToModifyPartsView(MouseEvent event) throws IOException  {

        if (!mainPartsTableView.selectionModelProperty().get().isEmpty()) {

            Part selectedPart = mainPartsTableView.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/ModifyPartsView.fxml"));
            ModifyPartsController modPartsController = new ModifyPartsController(inv, selectedPart);
            loader.setController(modPartsController);
            Parent newRoot = loader.load();

            Scene modPartsScene = new Scene(newRoot);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(modPartsScene);
            window.show();
        }
        else {
            Alert a = new Alert(Alert.AlertType.NONE,"You must select a Part to modify!",ButtonType.OK);
            a.show();
        }
    }
    /* modify Product, and show alert of a part is not selected */
    @FXML
    void goToModifyProductsView(MouseEvent event) throws IOException  {
        try {
            if (!mainProductsTableView.selectionModelProperty().get().isEmpty()) {

                Product selectedProduct = mainProductsTableView.getSelectionModel().getSelectedItem();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/ModifyProductsView.fxml"));
                ModifyProductsController modProductsController = new ModifyProductsController(inv, selectedProduct);
                loader.setController(modProductsController);
                Parent newRoot = loader.load();

                Scene modPartsScene = new Scene(newRoot);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(modPartsScene);
                window.show();
            } else {
                Alert a = new Alert(Alert.AlertType.NONE, "You must select a Product to modify!", ButtonType.OK);
                a.show();
            }
        }
        catch (IOException e){}
    }
    /* Search methods for Mouse (search button) or Action (enter key) events
    * if the entered text does not parse to integer, catch in Number format exception and call the string parameter
    * version of lookup */
    @FXML
    void searchForPartClick(MouseEvent event) {
        try {
            if (!searchPartTextField.getText().trim().isEmpty()) {
                partSearchObservableList.clear();
                int lookupId = (int) Integer.parseInt(searchPartTextField.getText());
                Part p = inv.lookupPart(lookupId);
                partSearchObservableList.add(p);
                mainPartsTableView.setItems(partSearchObservableList);
                mainPartsTableView.refresh();
            }
            if (searchPartTextField.getText().isEmpty()) {
                mainPartsTableView.setItems(partObservableList);
                mainPartsTableView.refresh();
            }
        }
        catch (NumberFormatException e){
            partSearchObservableList.clear();
            partSearchObservableList.setAll(inv.lookupPart(searchPartTextField.getText().toLowerCase().trim()));
            mainPartsTableView.setItems(partSearchObservableList);
            mainPartsTableView.refresh();
        }
        catch (NullPointerException ee){
            Alert a = new Alert(Alert.AlertType.INFORMATION,"That Part ID does not exist!", ButtonType.OK);
            a.show();
            searchPartTextField.clear();
            mainPartsTableView.setItems(partObservableList);
            mainPartsTableView.refresh();
        }
    }
    @FXML
    void searchForPartAction(ActionEvent event) {
        try {
            if (!searchPartTextField.getText().trim().isEmpty()) {
                partSearchObservableList.clear();
                int lookupId = (int) Integer.parseInt(searchPartTextField.getText());
                Part p = inv.lookupPart(lookupId);
                partSearchObservableList.add(p);
                mainPartsTableView.setItems(partSearchObservableList);
                mainPartsTableView.refresh();
            }
            if (searchPartTextField.getText().isEmpty()) {
                mainPartsTableView.setItems(partObservableList);
                mainPartsTableView.refresh();
            }
        }
        catch (NumberFormatException e){
            partSearchObservableList.clear();
            partSearchObservableList.setAll(inv.lookupPart(searchPartTextField.getText().toLowerCase().trim()));
            mainPartsTableView.setItems(partSearchObservableList);
            mainPartsTableView.refresh();
        }
        catch (NullPointerException ee){
            Alert a = new Alert(Alert.AlertType.INFORMATION,"That Part ID does not exist!", ButtonType.OK);
            a.show();
            searchPartTextField.clear();
            mainPartsTableView.setItems(partObservableList);
            mainPartsTableView.refresh();
        }
    }

    @FXML
    void searchForProductClick(MouseEvent event) {
        try {
            if (!searchProductTextField.getText().trim().isEmpty()) {
                productSearchObservableList.clear();
                int lookupId = (int) Integer.parseInt(searchProductTextField.getText());
                Product p = inv.lookupProduct(lookupId);
                productSearchObservableList.add(p);
                mainProductsTableView.setItems(productSearchObservableList);
                mainProductsTableView.refresh();
            }
            if (searchProductTextField.getText().isEmpty()) {
                mainProductsTableView.setItems(productObservableList);
                mainProductsTableView.refresh();
            }
        }
        catch (NumberFormatException e){
            productSearchObservableList.clear();
            productSearchObservableList.setAll(inv.lookupProduct(searchProductTextField.getText().toLowerCase().trim()));
            mainProductsTableView.setItems(productSearchObservableList);
            mainProductsTableView.refresh();
        }
        catch (NullPointerException ee){
            Alert a = new Alert(Alert.AlertType.INFORMATION,"That Part ID does not exist!", ButtonType.OK);
            a.show();
            searchProductTextField.clear();
            mainProductsTableView.setItems(productObservableList);
            mainProductsTableView.refresh();
        }
    }
    @FXML
    void searchForProductAction(ActionEvent event) {
        try {
            if (!searchProductTextField.getText().trim().isEmpty()) {
                productSearchObservableList.clear();
                int lookupId = (int) Integer.parseInt(searchProductTextField.getText());
                Product p = inv.lookupProduct(lookupId);
                productSearchObservableList.add(p);
                mainProductsTableView.setItems(productSearchObservableList);
                mainProductsTableView.refresh();
            }
            if (searchProductTextField.getText().isEmpty()) {
                mainProductsTableView.setItems(productObservableList);
                mainProductsTableView.refresh();
            }
        }
        catch (NumberFormatException e){
            productSearchObservableList.clear();
            productSearchObservableList.setAll(inv.lookupProduct(searchProductTextField.getText().toLowerCase().trim()));
            mainProductsTableView.setItems(productSearchObservableList);
            mainProductsTableView.refresh();
        }
        catch (NullPointerException ee){
            Alert a = new Alert(Alert.AlertType.INFORMATION,"That Part ID does not exist!", ButtonType.OK);
            a.show();
            searchProductTextField.clear();
            mainProductsTableView.setItems(productObservableList);
            mainProductsTableView.refresh();
        }
    }

    @FXML
    void exitApp(MouseEvent event) {
        Platform.exit();
    }
}
