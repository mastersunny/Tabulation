/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.test;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author sunny
 */
public class AlertMessage {

    public static void showAlertMessage(AlertType type,String message) {

        Alert alert = new Alert(type);
        alert.setHeaderText(message);
        alert.showAndWait();

    }

}
