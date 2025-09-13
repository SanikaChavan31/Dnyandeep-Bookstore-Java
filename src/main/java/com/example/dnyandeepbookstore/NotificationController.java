package com.example.dnyandeepbookstore;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class NotificationController implements Initializable {

    @FXML Label bookstoreName=new Label();
    @FXML private VBox notificationBox;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadStockNotifications();
    }

    private void loadStockNotifications() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookbillingsystem", "root", "sanika123")) {
            String query = "SELECT BookId, Title, Stocks FROM bookdetails";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String title = rs.getString("Title");
                int stock = Integer.parseInt(rs.getString("Stocks"));

                if (stock == 0) {
                    addNotification(title + " is out of Stock*");
                } else if (stock < 10) {
                    addNotification( title + " has Low Stock: " + stock);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            addNotification("Error in fetching stocks: " + e.getMessage());
        }
    }

    private void addNotification(String message) {
        Label alertLabel = new Label(message);
        alertLabel.setStyle("-fx-text-fill: #b30000;");
        alertLabel.setWrapText(true);

        Button closeBtn = new Button("✖");
        closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #b30000; -fx-font-weight: bold;");
        closeBtn.setOnAction(e -> notificationBox.getChildren().remove(closeBtn.getParent())); // Remove this alert HBox

        HBox alertBox = new HBox(10, alertLabel, closeBtn);
        alertBox.setStyle("-fx-background-color: #ffe6e6; -fx-border-color: #ff4d4d; -fx-padding: 10; -fx-background-radius: 8;");
        alertBox.setPrefWidth(540);
        alertBox.setSpacing(10);
        alertBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        notificationBox.getChildren().add(alertBox);
        notificationBox.setSpacing(15);
    }

    public void backToDashboard(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 400);
        scene.getStylesheets().add(getClass().getResource("/com/example/dnyandeepbookstore/dashboard.css").toExternalForm());
        Stage stage=new Stage();
        stage.setMaximized(true);
        stage.getIcons().clear();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        Stage bookTable = (Stage) ((Button) e.getSource()).getScene().getWindow();
        bookTable.close();
    }

}
