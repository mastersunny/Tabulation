/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author sunny
 */
public class Controller implements Initializable {

    @FXML
    private MenuItem home;

    @FXML
    private BorderPane mainLayout;

    @FXML
    private MenuBar menuBar;

    private FileChooser fileChooser;
    private Stage window;

    @FXML
    private MenuItem exportAsPDF;

    ViewService service = new ViewService();

    @FXML
    private void home(ActionEvent event) throws IOException {

        Stage stage;
        Parent root;

        stage = (Stage) menuBar.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("MainFXML.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private void openCSV(ActionEvent event) {

        exportAsPDF.setVisible(false);
        fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(window);

        if (file != null) {
            service.openFileForCourse(file, mainLayout, exportAsPDF);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        exportAsPDF.setVisible(false);
    }

    public void configureFileChooser(final FileChooser fileChooser) {

        fileChooser.setTitle("Open Course List");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV", "*.csv")
        );

    }

}
