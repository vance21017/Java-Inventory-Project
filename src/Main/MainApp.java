package Main;

import Model.InHouse;
import Model.Inventory;
import Model.OutSourced;
import Model.Product;
import View_Controller.MainScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /** Instantiate a new inventory and load with test data */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Inventory inv = new Inventory();
        testData(inv);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/MainScreenView.fxml"));
        MainScreenController mainController = new MainScreenController(inv);
        loader.setController(mainController);
        Parent newRoot = loader.load();

        Scene mainScene = new Scene(newRoot);
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }

    /** test data */
    void testData(Inventory inv){
        InHouse part1 = new InHouse(10, "InHouse", 0.2, 1, 1, 3, 4);
        OutSourced part2 = new OutSourced(11, "OutSourced", 0.3, 2, 2, 4, "testCO.1");
        OutSourced part3 = new OutSourced(12, "notAssociated", 0.3, 3, 3, 5, "testCO.2");
        inv.addPart(part1);
        inv.addPart(part2);
        inv.addPart(part3);
        Product prod1 = new Product(1000, "Product One", 0.333, 1, 2, 5);
        prod1.addAssociatedPart(part1);
        prod1.addAssociatedPart(part2);
        inv.addProduct(prod1);
    }



}
