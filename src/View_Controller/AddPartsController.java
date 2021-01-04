package View_Controller;

import Main.IdGenerator;
import Model.InHouse;
import Model.Inventory;
import Model.OutSourced;
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

public class AddPartsController implements Initializable {
    @FXML private Text idLabel;
    @FXML private RadioButton outSourcedRadio;
    @FXML private RadioButton inHouseRadio;
    @FXML private TextField maxTextField;
    @FXML private TextField minTextField;
    @FXML private TextField partIdTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField stockTextField;
    @FXML private TextField priceTextField;
    @FXML private TextField machineIdTextField;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    @FXML private ToggleGroup part;

//    private Scene mainScene, addPartsScene, modifyPartsScene, addProductsScene, modifyProductsScene;

    Inventory inv;

    public AddPartsController(Inventory inv){
        this.inv = inv;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idLabel.setText("Machine ID");
    }


    //When Save Selected, Add part and return to main scene
    @FXML
    void savePart(MouseEvent event) throws IOException {
        try {
            if (inHouseRadio.isSelected()) {

                boolean checked = false;
                int issues = 0;

                StringBuilder alertMessage = new StringBuilder();
                alertMessage.append("You have the following issues:\n\n");

                while (!checked) {
                    if (nameTextField.getText().isEmpty()) {
                        alertMessage.append("- Name is empty\n");
                        issues++;
                    }
                    if (machineIdTextField.getText().isEmpty()) {
                        alertMessage.append("- Machine ID is empty\n");
                        issues++;
                    }
                    if (Integer.parseInt(maxTextField.getText()) < Integer.parseInt(minTextField.getText())) {
                        alertMessage.append("- Your max inventory value is greater than the minimum\n");
                        issues++;
                    }
                    if (Integer.parseInt(machineIdTextField.getText()) < 0 || Double.parseDouble(priceTextField.getText()) < 0 || Integer.parseInt(maxTextField.getText()) < 0 || Integer.parseInt(minTextField.getText()) < 0 || Integer.parseInt(stockTextField.getText()) < 0) {
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

                InHouse newPart = new InHouse();
                int nextPartId = Integer.parseInt(new IdGenerator().genPartId());

                newPart.setId(nextPartId);
                newPart.setName(nameTextField.getText());
                newPart.setPrice(Double.parseDouble(priceTextField.getText()));
                newPart.setStock(Integer.parseInt(stockTextField.getText()));
                newPart.setMin(Integer.parseInt(minTextField.getText()));
                newPart.setMax(Integer.parseInt(maxTextField.getText()));
                newPart.setMachineId(Integer.parseInt(machineIdTextField.getText()));

                inv.addPart(newPart);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/MainScreenView.fxml"));
                MainScreenController mainController = new MainScreenController(inv);
                loader.setController(mainController);
                Parent newRoot = loader.load();

                Scene mainScene = new Scene(newRoot);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(mainScene);
                window.show();
            }
            if (outSourcedRadio.isSelected()) {
                boolean checked = false;
                int issues = 0;

                StringBuilder alertMessage = new StringBuilder();
                alertMessage.append("You have the following issues:\n\n");

                while (!checked) {
                    if (nameTextField.getText().isEmpty()) {
                        alertMessage.append("- Name is empty\n");
                        issues++;
                    }
                    if (machineIdTextField.getText().isEmpty()) {
                        alertMessage.append("- Company Name is empty\n");
                        issues++;
                    }
                    if (Integer.parseInt(maxTextField.getText()) < Integer.parseInt(minTextField.getText())) {
                        alertMessage.append("- Your max inventory value is greater than the minimum\n");
                        issues++;
                    }
                    if (Double.parseDouble(priceTextField.getText()) < 0 || Integer.parseInt(maxTextField.getText()) < 0 || Integer.parseInt(minTextField.getText()) < 0 || Integer.parseInt(stockTextField.getText()) < 0) {
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

                OutSourced newPart = new OutSourced();
                int nextPartId = Integer.parseInt(new IdGenerator().genPartId());

                newPart.setId(nextPartId);
                newPart.setName(nameTextField.getText());
                newPart.setPrice(Double.parseDouble(priceTextField.getText()));
                newPart.setStock(Integer.parseInt(stockTextField.getText()));
                newPart.setMin(Integer.parseInt(minTextField.getText()));
                newPart.setMax(Integer.parseInt(maxTextField.getText()));
                newPart.setCompanyName(machineIdTextField.getText());

                inv.addPart(newPart);

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
        catch (NumberFormatException n){
            Alert a = new Alert(Alert.AlertType.INFORMATION,"You must use valid numbers!", ButtonType.OK);
            a.show();
        }
    }

    //Cancel returns to main screen
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
    //Label switches base on radio button selection
    @FXML
    void setLabel(MouseEvent event) {
        if (inHouseRadio.isSelected()){
            idLabel.setText("Machine ID");
        }
        if (outSourcedRadio.isSelected()){
            idLabel.setText("Company Name");
        }
    }

}

