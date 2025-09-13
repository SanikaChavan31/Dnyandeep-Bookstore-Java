package com.example.dnyandeepbookstore;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class DashboardController implements Initializable {

    @FXML private AnchorPane dpane;
    @FXML private AnchorPane option;
    @FXML private Label bookstoreName;
    @FXML private AnchorPane charts;
    @FXML private Label todaysDate;
    @FXML private BarChart<String, Integer> salesChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        todaysDate.setText(today.format(formatter));

        Map<LocalDate, Integer> salesMap = new TreeMap<>();

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookbillingsystem", "root", "sanika123");

            PreparedStatement ps = conn.prepareStatement("SELECT Date, Total FROM daily_totals");
            ResultSet rs = ps.executeQuery();

            DateTimeFormatter dbFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

            while (rs.next()) {
                String dateStr = rs.getString("Date");
                double total = rs.getDouble("Total");

                try {
                    LocalDate date = LocalDate.parse(dateStr, dbFormatter);
                    if (!date.isBefore(today.minusDays(6)) && !date.isAfter(today)) {
                        salesMap.put(date, (int) total);
                    }
                } catch (Exception e) {
                    System.out.println("Invalid date in DB: " + dateStr);
                }
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            salesMap.putIfAbsent(date, 0);
        }

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Last 7 Days Sales (x100 ₹)");

        for (Map.Entry<LocalDate, Integer> entry : salesMap.entrySet()) {
            String label = entry.getKey().format(DateTimeFormatter.ofPattern("E")); // Mon, Tue...
            int scaledValue = entry.getValue() / 100;
            series.getData().add(new XYChart.Data<>(label, scaledValue));
        }

        salesChart.getData().clear();
        salesChart.getData().add(series);

    }

    public void bookClick(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("books.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 1000);
        scene.getStylesheets().add(getClass().getResource("/com/example/dnyandeepbookstore/books.css").toExternalForm());
        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.getIcons().clear();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }

    public void logoutClick(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 400);
        Stage stage = new Stage();
        scene.getStylesheets().add(getClass().getResource("/com/example/dnyandeepbookstore/login.css").toExternalForm());
        stage.getIcons().clear();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }

    public void billClick(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("bill.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        scene.getStylesheets().add(getClass().getResource("/com/example/dnyandeepbookstore/bill.css").toExternalForm());
        Stage stage = new Stage();
        stage.getIcons().clear();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }

    public void salesClick(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("sales.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        scene.getStylesheets().add(getClass().getResource("/com/example/dnyandeepbookstore/sales.css").toExternalForm());
        Stage stage = new Stage();
        stage.getIcons().clear();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }

    public void notificationClick(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("notification.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        scene.getStylesheets().add(getClass().getResource("/com/example/dnyandeepbookstore/notification.css").toExternalForm());
        Stage stage = new Stage();
        stage.getIcons().clear();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }

    public void dailyTransactionClick(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("dailytransactions.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        scene.getStylesheets().add(getClass().getResource("/com/example/dnyandeepbookstore/dailytransactions.css").toExternalForm());
        Stage stage = new Stage();
        stage.getIcons().clear();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }
}
