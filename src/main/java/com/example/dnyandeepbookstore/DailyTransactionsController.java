package com.example.dnyandeepbookstore;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.sql.*;

public class DailyTransactionsController {

    @FXML Label bookstoreName=new Label();
    @FXML Button back=new Button();
    @FXML private TableView<DailyTransactions> dailyTotalTable;
    @FXML private TableColumn<DailyTransactions, String> dateCol;
    @FXML private TableColumn<DailyTransactions, Double> totalCol;

    ObservableList<DailyTransactions> summaryList = FXCollections.observableArrayList();

    public void initialize() {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        loadDailyTotals();
    }

    private void loadDailyTotals() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookbillingsystem", "root", "sanika123")) {
            String query = "SELECT * FROM daily_totals ORDER BY Date DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                summaryList.add(new DailyTransactions(
                        rs.getString("Date"),
                        rs.getDouble("Total")
                ));
            }

            dailyTotalTable.setItems(summaryList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
