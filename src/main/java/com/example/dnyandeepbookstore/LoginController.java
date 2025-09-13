package com.example.dnyandeepbookstore;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LoginController  {
    @FXML AnchorPane pane=new AnchorPane();
    @FXML Label bookstoreName=new Label();
    @FXML TextField usernametf=new TextField();
    @FXML PasswordField passwordtf=new PasswordField();
    @FXML Button login=new Button();

    public void cancelClick(ActionEvent e){
        Stage stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
        stage.hide();
    }

    public void loginClick(ActionEvent e) throws IOException {
        String username=usernametf.getText();
        String password=passwordtf.getText();
        if(username.equals( "db") && password.equals( "db")){
            FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("dashboard.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 400);
            scene.getStylesheets().add(getClass().getResource("/com/example/dnyandeepbookstore/dashboard.css").toExternalForm());
            Stage stage=new Stage();
            stage.setMaximized(true);
            stage.getIcons().clear();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
            Stage loginStage = (Stage) ((Button) e.getSource()).getScene().getWindow();
            loginStage.close();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password!!");
            alert.setTitle("Error");

            DialogPane alertDialogPane = alert.getDialogPane();
            alertDialogPane.getStylesheets().add(getClass().getResource("/com/example/dnyandeepbookstore/alert.css").toExternalForm());
            alertDialogPane.getStyleClass().add("custom-alert");

            alert.showAndWait();
        }
    }



}