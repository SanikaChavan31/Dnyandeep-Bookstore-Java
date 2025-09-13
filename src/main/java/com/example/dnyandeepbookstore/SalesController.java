package com.example.dnyandeepbookstore;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SalesController implements Initializable {
    @FXML Label bookstoreName=new Label();

    @FXML private TableView<Sales> salesTable;
    @FXML private TableColumn<Sales, String> nameCol;
    @FXML private TableColumn<Sales, String> mobileCol;
    @FXML private TableColumn<Sales, String> dateCol;
    @FXML private TableColumn<Sales, String> bookCol;
    @FXML private TableColumn<Sales, String> totalCol;
    @FXML private Label receiptCustomerName;
    @FXML private Label receiptCustomerMobile;
    @FXML private Label todaysDate;
    @FXML private Label totalAmountField;
    @FXML private Label purchasedBooks;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onSeeSales();
    }

    private void showAlert(String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    private void onSeeSales() {
        ObservableList<Sales> data = FXCollections.observableArrayList();

        String query = "SELECT * FROM sales ORDER BY DateOfPurchase DESC";  // Sorted by date

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookbillingsystem", "root", "sanika123");
             PreparedStatement pst = conn.prepareStatement(query)) {

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                data.add(new Sales(
                        rs.getString("Customer_Name"),
                        rs.getString("Mobile_No"),
                        rs.getString("DateOfPurchase"),
                        rs.getString("Purchased_Book"),
                        rs.getString("Total_Amount")
                ));
            }

            nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
            dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            bookCol.setCellValueFactory(new PropertyValueFactory<>("book"));
            totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));

            salesTable.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error loading sales data.");
        }
    }

    public void backToDashboard(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 400);
        scene.getStylesheets().add(getClass().getResource("/com/example/dnyandeepbookstore/dashboard.css").toExternalForm());
        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.getIcons().clear();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        Stage currentWindow = (Stage) ((Button) e.getSource()).getScene().getWindow();
        currentWindow.close();
    }
}
