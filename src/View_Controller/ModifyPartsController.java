package View_Controller;

import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifyPartsController implements Initializable {
    Inventory inv;
    Part selectedPart;

    /** Pass inventory and selected part to modify to the controller*/
    public ModifyPartsController(Inventory inv, Part p){
        this.inv = inv;
        this.selectedPart = p;
    }

    @FXML private TextField maxField;
    @FXML private TextField minField;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField stockField;
    @FXML private TextField priceField;
    @FXML private TextField machineIdField;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    @FXML private RadioButton inHouseRadio;
    @FXML private ToggleGroup part;
    @FXML private RadioButton outSourcedRadio;
    @FXML private Text machId_CoName;

    @FXML
    void goToMainScreen(MouseEvent event) throws IOException {
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
    void modifyPart(MouseEvent event) throws IOException {
        try {

            if (selectedPart instanceof InHouse) {
                /** validate data before modifying the Part */
                boolean checked = false;
                int issues = 0;

                StringBuilder alertMessage = new StringBuilder();
                alertMessage.append("You have the following issues:\n\n");

                while (!checked) {
                    if (nameField.getText().isEmpty()) {
                        alertMessage.append("- Name is empty\n");
                        issues++;
                    }
                    if (inHouseRadio.isSelected()) {
                        if (machineIdField.getText().isEmpty()) {
                            alertMessage.append("- Machine ID is empty\n");
                            issues++;
                        }
                    }
                    if (outSourcedRadio.isSelected()) {
                        if (machineIdField.getText().isEmpty()) {
                            alertMessage.append("- Company Name is empty\n");
                            issues++;
                        }
                    }
                    if (Integer.parseInt(maxField.getText()) < Integer.parseInt(minField.getText())) {
                        alertMessage.append("- Your max inventory value is greater than the minimum\n");
                        issues++;
                    }
                    if(inHouseRadio.isSelected()) {
                        if (Integer.parseInt(machineIdField.getText()) < 0 || Double.parseDouble(priceField.getText()) < 0 || Integer.parseInt(maxField.getText()) < 0 || Integer.parseInt(minField.getText()) < 0 || Integer.parseInt(stockField.getText()) < 0) {
                            alertMessage.append("- Negative values are not allowed\n");
                            issues++;
                        }
                    }
                    if(outSourcedRadio.isSelected()) {
                        if (Double.parseDouble(priceField.getText()) < 0 || Integer.parseInt(maxField.getText()) < 0 || Integer.parseInt(minField.getText()) < 0 || Integer.parseInt(stockField.getText()) < 0) {
                            alertMessage.append("- Negative values are not allowed\n");
                            issues++;
                        }
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

                /** if checks pass, modify the Part
                 * If the part is of same type, simply update. If the type of part has changed from In-house to Outsourced
                 * or vice-versa, then create a new instance of Part and remove the old one, and update all Products with this
                 * new modified Part*/
                if (inHouseRadio.isSelected()) {
                    ((InHouse) selectedPart).setMachineId(Integer.parseInt(machineIdField.getText()));
                    selectedPart.setMax(Integer.parseInt(maxField.getText()));
                    selectedPart.setMin(Integer.parseInt(minField.getText()));
                    selectedPart.setName(nameField.getText());
                    selectedPart.setPrice(Double.parseDouble(priceField.getText()));
                    selectedPart.setStock(Integer.parseInt(stockField.getText()));
                }
                if (outSourcedRadio.isSelected()){
                    OutSourced newPar = new OutSourced();
                    newPar.setId(selectedPart.getId());
                    newPar.setCompanyName(machineIdField.getText());
                    newPar.setMax(Integer.parseInt(maxField.getText()));
                    newPar.setMin(Integer.parseInt(minField.getText()));
                    newPar.setName(nameField.getText());
                    newPar.setPrice(Double.parseDouble(priceField.getText()));
                    newPar.setStock(Integer.parseInt(stockField.getText()));
                    inv.addPart(newPar);
                    for (Product p : inv.getAllProducts()) {
                        if (p.getAllAssociatedParts().contains(selectedPart)) {
                            p.deleteAssociatedPart(selectedPart);
                            p.addAssociatedPart(newPar);
                        }
                    }
                    inv.deletePart(selectedPart);
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
            if (selectedPart instanceof OutSourced) {
                boolean checked = false;
                int issues = 0;

                StringBuilder alertMessage = new StringBuilder();
                alertMessage.append("You have the following issues:\n\n");

                while (!checked) {
                    if (nameField.getText().isEmpty()) {
                        alertMessage.append("- Name is empty\n");
                        issues++;
                    }
                    if (machineIdField.getText().isEmpty()) {
                        alertMessage.append("- Company Name is empty\n");
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
                if (outSourcedRadio.isSelected()) {
                    ((OutSourced) selectedPart).setCompanyName(machineIdField.getText());
                    selectedPart.setMax(Integer.parseInt(maxField.getText()));
                    selectedPart.setMin(Integer.parseInt(minField.getText()));
                    selectedPart.setName(nameField.getText());
                    selectedPart.setPrice(Double.parseDouble(priceField.getText()));
                    selectedPart.setStock(Integer.parseInt(stockField.getText()));
                }
                if (inHouseRadio.isSelected()){
                    InHouse newPar = new InHouse();
                    newPar.setId(selectedPart.getId());
                    newPar.setMachineId(Integer.parseInt(machineIdField.getText()));
                    newPar.setMax(Integer.parseInt(maxField.getText()));
                    newPar.setMin(Integer.parseInt(minField.getText()));
                    newPar.setName(nameField.getText());
                    newPar.setPrice(Double.parseDouble(priceField.getText()));
                    newPar.setStock(Integer.parseInt(stockField.getText()));
                    inv.addPart(newPar);
                    for (Product p : inv.getAllProducts()){
                        if (p.getAllAssociatedParts().contains(selectedPart)){
                            p.deleteAssociatedPart(selectedPart);
                            p.addAssociatedPart(newPar);
                        }
                    }
                    inv.deletePart(selectedPart);
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
        catch (IOException e){}
        /** catch for int and double datatypes */
        catch (NumberFormatException n){
            Alert a = new Alert(Alert.AlertType.INFORMATION,"You must enter valid numbers!", ButtonType.OK);
            a.show();
        }
    }
    @FXML
    void typeSwitchOtoI(ActionEvent event) {
        machId_CoName.textProperty().set("Machine ID");
    }

    @FXML
    void typeSwitchedItoO(ActionEvent event) {
        machId_CoName.textProperty().set("Company Name");
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /** initialize view based on type of Part being modified */

        if (selectedPart instanceof InHouse){
            inHouseRadio.setSelected(true);
            machId_CoName.textProperty().set("Machine ID");

            idField.setText(String.valueOf(selectedPart.getId()));
            idField.editableProperty().setValue(false);
            idField.setDisable(true);
            nameField.setText(selectedPart.getName());
            stockField.setText(String.valueOf(selectedPart.getStock()));
            priceField.setText(String.valueOf(selectedPart.getPrice()));
            maxField.setText(String.valueOf(selectedPart.getMax()));
            minField.setText(String.valueOf(selectedPart.getMin()));
            machineIdField.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));


        }
        if (selectedPart instanceof OutSourced){
            outSourcedRadio.setSelected(true);
            machId_CoName.textProperty().set("Company Name");

            idField.setText(String.valueOf(selectedPart.getId()));
            idField.editableProperty().setValue(false);
            idField.setDisable(true);
            nameField.setText(selectedPart.getName());
            stockField.setText(String.valueOf(selectedPart.getStock()));
            priceField.setText(String.valueOf(selectedPart.getPrice()));
            maxField.setText(String.valueOf(selectedPart.getMax()));
            minField.setText(String.valueOf(selectedPart.getMin()));
            machineIdField.setText(String.valueOf(((OutSourced) selectedPart).getCompanyName()));
        }
    }

}
