package View_Controller;

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

public class ModifyProductsController implements Initializable {
    Inventory inv;
    Product selectedProduct;

    public ModifyProductsController(Inventory inv, Product p){
        this.inv = inv;
        this.selectedProduct = p;
    }

    private ObservableList<Part> allPartList = FXCollections.observableArrayList();
    private ObservableList<Part> associatedPartList = FXCollections.observableArrayList();
    private ObservableList<Part> partSearchObservableList = FXCollections.observableArrayList();


    @FXML private TextField maxField;
    @FXML private TextField minField;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField stockField;
    @FXML private TextField priceField;
    @FXML private TextField searchPartTextField;

    @FXML private TableView<Part> allPartsTableView;
    @FXML private TableColumn<Part, Integer> allPartIdColumn;
    @FXML private TableColumn<Part, String> allPartNameColumn;
    @FXML private TableColumn<Part, Integer> allPartStockColumn;
    @FXML private TableColumn<Part, Double> allPartPriceColumn;

    @FXML private TableView<Part> associatedPartsTableView;
    @FXML private TableColumn<Part, Integer> asoPartIdColumn;
    @FXML private TableColumn<Part, String> asoPartNameColumn;
    @FXML private TableColumn<Part, Integer> asoPartInventoryColumn;
    @FXML private TableColumn<Part, Double> asoPartPriceColumn;


    @FXML void associatePart(MouseEvent event) {
        Part selectedPart = allPartsTableView.getSelectionModel().getSelectedItem();

        if (associatedPartList.size() == 0){
            associatedPartList.add(selectedPart);
            associatedPartsTableView.setItems(associatedPartList);
            associatedPartsTableView.refresh();
            return;
        }

        if (selectedPart != null) {

            for (Part p : associatedPartList) {
                if (associatedPartList.contains(selectedPart)) {
                    Alert a = new Alert(Alert.AlertType.NONE, "Selected Part is already in the Product", ButtonType.OK);
                    a.show();
                } else {
                    associatedPartList.add(selectedPart);
                    associatedPartsTableView.setItems(associatedPartList);
                    associatedPartsTableView.refresh();
                }
                return;
            }
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
    void cancelAddProduct(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/MainScreenView.fxml"));
        MainScreenController mainController = new MainScreenController(inv);
        loader.setController(mainController);
        Parent newRoot = loader.load();

        Scene mainScene = new Scene(newRoot);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }

    @FXML
    void saveModProduct(MouseEvent event) throws IOException{
        try {
            if (associatedPartList.size() > 0) {
                boolean checked = false;
                int issues = 0;

                StringBuilder alertMessage = new StringBuilder();
                alertMessage.append("You have the following issues:\n\n");

                while (!checked) {
                    if (nameField.getText().isEmpty()) {
                        alertMessage.append("- Name is empty\n");
                        issues++;
                    }
                    if (priceField.getText().isEmpty()) {
                        alertMessage.append("- You did not set a price\n");
                        issues++;
                    }
                    if (maxField.getText().isEmpty()) {
                        alertMessage.append("- You did not set a valid maximum number of inventory\n");
                        issues++;
                    }
                    if (minField.getText().isEmpty()) {
                        alertMessage.append("- You did not set a valid minimum number of inventory\n");
                        issues++;
                    }
                    if (stockField.getText().isEmpty()) {
                        alertMessage.append("- You did not set a valid inventory count\n");
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

                selectedProduct.setName(nameField.getText());
                selectedProduct.setPrice(Double.parseDouble(priceField.getText()));
                selectedProduct.setMax(Integer.parseInt(maxField.getText()));
                selectedProduct.setMin(Integer.parseInt(minField.getText()));
                selectedProduct.setStock(Integer.parseInt(stockField.getText()));

                selectedProduct.getAllAssociatedParts().clear();

                for (Part p : associatedPartList) {
                    selectedProduct.addAssociatedPart(p);
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/MainScreenView.fxml"));
                MainScreenController mainController = new MainScreenController(inv);
                loader.setController(mainController);
                Parent newRoot = loader.load();

                Scene mainScene = new Scene(newRoot);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(mainScene);
                window.show();
            } else {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "You must have at least one part in the Product!", ButtonType.OK);
                a.show();
            }
        }
        catch (IOException e){}
        catch (NumberFormatException n){
            Alert a = new Alert(Alert.AlertType.INFORMATION,"You must enter valid numbers!", ButtonType.OK);
            a.show();
        }
    }
    @FXML
    void searchForPartClicked(MouseEvent event) {
        try {
            if (!searchPartTextField.getText().trim().isEmpty()) {
                partSearchObservableList.clear();
                int lookupId = (int) Integer.parseInt(searchPartTextField.getText());
                Part p = inv.lookupPart(lookupId);
                partSearchObservableList.add(p);
                allPartsTableView.setItems(partSearchObservableList);
                allPartsTableView.refresh();
            }
            if (searchPartTextField.getText().isEmpty()) {
                allPartsTableView.setItems(allPartList);
                allPartsTableView.refresh();
            }
        }
        catch (NumberFormatException e){
            partSearchObservableList.clear();
            partSearchObservableList.setAll(inv.lookupPart(searchPartTextField.getText().toLowerCase().trim()));
            allPartsTableView.setItems(partSearchObservableList);
            allPartsTableView.refresh();
        }
        catch (NullPointerException ee){
            Alert a = new Alert(Alert.AlertType.INFORMATION,"That Part ID does not exist!", ButtonType.OK);
            a.show();
            searchPartTextField.clear();
            allPartsTableView.setItems(allPartList);
            allPartsTableView.refresh();
        }
    }
    @FXML
    void searchForPartActioned(ActionEvent event) {
        try {
            if (!searchPartTextField.getText().trim().isEmpty()) {
                partSearchObservableList.clear();
                int lookupId = (int) Integer.parseInt(searchPartTextField.getText());
                Part p = inv.lookupPart(lookupId);
                partSearchObservableList.add(p);
                allPartsTableView.setItems(partSearchObservableList);
                allPartsTableView.refresh();
            }
            if (searchPartTextField.getText().isEmpty()) {
                allPartsTableView.setItems(allPartList);
                allPartsTableView.refresh();
            }
        }
        catch (NumberFormatException e){
            partSearchObservableList.clear();
            partSearchObservableList.setAll(inv.lookupPart(searchPartTextField.getText().toLowerCase().trim()));
            allPartsTableView.setItems(partSearchObservableList);
            allPartsTableView.refresh();
        }
        catch (NullPointerException ee){
            Alert a = new Alert(Alert.AlertType.INFORMATION,"That Part ID does not exist!", ButtonType.OK);
            a.show();
            searchPartTextField.clear();
            allPartsTableView.setItems(allPartList);
            allPartsTableView.refresh();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idField.setDisable(true);

        maxField.setText(String.valueOf(selectedProduct.getMax()));
        minField.setText(String.valueOf(selectedProduct.getMin()));
        idField.setText(String.valueOf(selectedProduct.getId()));
        nameField.setText(String.valueOf(selectedProduct.getName()));
        stockField.setText(String.valueOf(selectedProduct.getStock()));
        priceField.setText(String.valueOf(selectedProduct.getPrice()));

        allPartIdColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        allPartNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        allPartStockColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        allPartPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        allPartList.setAll(inv.getAllParts());
        allPartsTableView.setItems(allPartList);

        asoPartIdColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        asoPartNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        asoPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        asoPartPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        associatedPartList.setAll(selectedProduct.getAllAssociatedParts());
        associatedPartsTableView.setItems(associatedPartList);

    }
}
