package View_Controller;

import Main.IdGenerator;
import Model.Inventory;
import Model.Part;
import Model.Product;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddProductsController implements Initializable {
    Inventory inv;

    public AddProductsController(Inventory inv) {
        this.inv = inv;
    }

    private ObservableList<Part> partsObservableList = FXCollections.observableArrayList();
    private ObservableList<Part> associatedPartList = FXCollections.observableArrayList();
    private ObservableList<Part> partSearchObservableList = FXCollections.observableArrayList();


    @FXML private TextField maxField;
    @FXML private TextField minField;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField stockField;
    @FXML private TextField priceField;
    @FXML private Button deletePartButton;
    @FXML private Button cancel;
    @FXML private Button saveProduct;
    @FXML private Button searchButton;
    @FXML private TextField searchTextField;
    @FXML private Button addPartButton;

    @FXML private TableView<Part> partsTableView;
    @FXML private TableView<Part> associatedPartsTableView;

    @FXML
    void cancelNewProduct(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/MainScreenView.fxml"));
        MainScreenController mainController = new MainScreenController(inv);
        loader.setController(mainController);
        Parent newRoot = loader.load();

        Scene mainScene = new Scene(newRoot);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }

    @FXML void associatePart(MouseEvent event) {

        if (partsTableView.getSelectionModel().getSelectedItem()==null){
            Alert a = new Alert(Alert.AlertType.INFORMATION, "You must select a Part to add!", ButtonType.OK);
            a.show();
            return;
        }

        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();

        if (associatedPartList.size() == 0) {
            associatedPartList.add(selectedPart);
            associatedPartsTableView.setItems(associatedPartList);
            associatedPartsTableView.refresh();
            return;
        }

        if (associatedPartList.size() > 0 && selectedPart instanceof Part) {
            if (associatedPartList.contains(selectedPart)) {
                Alert a = new Alert(Alert.AlertType.NONE, "Selected Part is already in the Product", ButtonType.OK);
                a.show();
            } else {
                associatedPartList.add(selectedPart);
                associatedPartsTableView.setItems(associatedPartList);
                associatedPartsTableView.refresh();
            }
        }

    }

    @FXML void createNewProduct(MouseEvent event) throws IOException {
        try {

            boolean checked = false;
            int issues = 0;

            StringBuilder alertMessage = new StringBuilder();
            alertMessage.append("You have the following issues:\n\n");

            while (!checked) {
                if (nameField.getText().isEmpty()) {
                    alertMessage.append("- Name is empty\n");
                    issues++;
                 }
                if (Integer.parseInt(maxField.getText()) < Integer.parseInt(minField.getText())) {
                    alertMessage.append("- Your max inventory value is greater than the minimum\n");
                    issues++;
                }
                if (Double.parseDouble(priceField.getText()) < 0 || Integer.parseInt(maxField.getText()) < 0 || Integer.parseInt(minField.getText()) < 0 || Integer.parseInt(stockField.getText()) < 0) {
                    alertMessage.append("- Negative values are not allowed\n");
                    issues++;
                }
                if (issues == 1) {
                    alertMessage.append("\n\nYou have " + issues + " issue to resolve before saving");
                    Alert a = new Alert(Alert.AlertType.NONE, alertMessage.toString(), ButtonType.OK);
                    a.show();
                    return;
                }
                if (issues > 1) {
                    alertMessage.append("\n\nYou have " + issues + " issues to resolve before saving");
                    Alert a = new Alert(Alert.AlertType.NONE, alertMessage.toString(), ButtonType.OK);
                    a.show();
                    return;
                }
                checked = true;
            }

            if (associatedPartList.size() == 0){
                Alert a = new Alert(Alert.AlertType.INFORMATION,"You must add at least 1 Part!", ButtonType.OK);
                a.show();
                return;
            }
            else {

                Product product = new Product();

                int nextProdId = Integer.parseInt(new IdGenerator().genProdId());

                product.setId(nextProdId);
                product.setName(nameField.getText());
                product.setStock(Integer.parseInt(stockField.getText()));
                product.setMin(Integer.parseInt(minField.getText()));
                product.setMax(Integer.parseInt(maxField.getText()));
                product.setPrice(Double.parseDouble(priceField.getText()));

                inv.addProduct(product);

                for (Part p : associatedPartList){
                    product.addAssociatedPart(p);
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/MainScreenView.fxml"));
                MainScreenController mainController = new MainScreenController(inv);
                loader.setController(mainController);
                Parent newRoot = loader.load();

                Scene mainScene = new Scene(newRoot);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(mainScene);
                window.show();
            }
        }
        catch (IOException e){
        }
        catch (NumberFormatException n){
            Alert a = new Alert(Alert.AlertType.INFORMATION,"You must enter valid numbers!", ButtonType.OK);
            a.show();
        }

    }
    @FXML
    void searchForPartAction(ActionEvent event) {
        try {
            if (!searchTextField.getText().trim().isEmpty()) {
                partSearchObservableList.clear();
                int lookupId = (int) Integer.parseInt(searchTextField.getText());
                Part p = inv.lookupPart(lookupId);
                partSearchObservableList.add(p);
                partsTableView.setItems(partSearchObservableList);
                partsTableView.refresh();
            }
            if (searchTextField.getText().isEmpty()) {
                partsTableView.setItems(partsObservableList);
                partsTableView.refresh();
            }
        }
        catch (NumberFormatException e){
            partSearchObservableList.clear();
            partSearchObservableList.setAll(inv.lookupPart(searchTextField.getText().toLowerCase().trim()));
            partsTableView.setItems(partSearchObservableList);
            partsTableView.refresh();
        }
        catch (NullPointerException ee){
            Alert a = new Alert(Alert.AlertType.INFORMATION,"That Part ID does not exist!", ButtonType.OK);
            a.show();
            searchTextField.clear();
            partsTableView.setItems(partsObservableList);
            partsTableView.refresh();
        }
    }
    @FXML
    void unassociatePart(MouseEvent event) {
        if (associatedPartList.size() == 1){
            Alert a = new Alert (Alert.AlertType.NONE,"You must have at least 1 Part!", ButtonType.OK);
            a.show();
            return;
        }
        Part selectedPart = associatedPartsTableView.getSelectionModel().getSelectedItem();

        associatedPartList.remove(selectedPart);
        associatedPartsTableView.setItems(associatedPartList);
        associatedPartsTableView.refresh();
    }
    @FXML
    void searchForPartClick(MouseEvent event) {
        try {
            if (!searchTextField.getText().trim().isEmpty()) {
                partSearchObservableList.clear();
                int lookupId = (int) Integer.parseInt(searchTextField.getText());
                Part p = inv.lookupPart(lookupId);
                partSearchObservableList.add(p);
                partsTableView.setItems(partSearchObservableList);
                partsTableView.refresh();
            }
            if (searchTextField.getText().isEmpty()) {
                partsTableView.setItems(partsObservableList);
                partsTableView.refresh();
            }
        }
        catch (NumberFormatException e){
            partSearchObservableList.clear();
            partSearchObservableList.setAll(inv.lookupPart(searchTextField.getText().toLowerCase().trim()));
            partsTableView.setItems(partSearchObservableList);
            partsTableView.refresh();
        }
        catch (NullPointerException ee){
            Alert a = new Alert(Alert.AlertType.INFORMATION,"That Part ID does not exist!", ButtonType.OK);
            a.show();
            searchTextField.clear();
            partsTableView.setItems(partsObservableList);
            partsTableView.refresh();
        }
    }


    //make Part and Product observable lists

    //configure All Parts Table
    @FXML private TableColumn<Part, Integer> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partInventoryColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn;

    @FXML private TableColumn<Part, Integer> asoPartIdColumn;
    @FXML private TableColumn<Part, String> asoPartNameColumn;
    @FXML private TableColumn<Part, Integer> asoPartInventoryColumn;
    @FXML private TableColumn<Part, Double> asoPartPriceColumn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idField.setDisable(true);
        idField.setText("Auto Generated");

        partIdColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        partsObservableList.setAll(inv.getAllParts());
        partsTableView.setItems(partsObservableList);

        asoPartIdColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        asoPartNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        asoPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        asoPartPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
    }
}