package com.example.dnyandeepbookstore;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BillController {
    @FXML Label bookstoreName = new Label();
    @FXML AnchorPane cartPane = new AnchorPane();
    @FXML Label stocksAvailable = new Label();
    @FXML TextField quantityEnter = new TextField();
    @FXML private TextField BookId;
    @FXML private TextField customerNameField;
    @FXML private TextField customerMobileField;
    @FXML private Label receiptCustomerName;
    @FXML private Label receiptCustomerMobile;
    @FXML private TableView<Bill> bookDetail;
    @FXML private TableColumn<Bill, String> bookId;
    @FXML private TableColumn<Bill, String> title;
    @FXML private TableColumn<Bill, String> quantity;
    @FXML private TableColumn<Bill, String> price;
    @FXML private TableView<Bill> printBill;
    @FXML private TableColumn<Bill, String> printBooktitle;
    @FXML private TableColumn<Bill, String> printquantity;
    @FXML private TableColumn<Bill, String> printprice;

    private final ObservableList<Bill> cartData = FXCollections.observableArrayList();
    private final ObservableList<Bill> printData = FXCollections.observableArrayList();

    private final String URL = "jdbc:mysql://localhost:3306/bookbillingsystem";
    private final String USER = "root";
    private final String PASSWORD = "sanika123";

    public void initialize() {
        bookId.setCellValueFactory(data -> data.getValue().bookIdProperty());
        title.setCellValueFactory(data -> data.getValue().titleProperty());
        quantity.setCellValueFactory(data -> data.getValue().quantityProperty());
        price.setCellValueFactory(data -> data.getValue().priceProperty());
        bookDetail.setItems(cartData);


        printBooktitle.setCellValueFactory(data -> data.getValue().titleProperty());
        printquantity.setCellValueFactory(data -> data.getValue().quantityProperty());
        printprice.setCellValueFactory(data -> data.getValue().priceProperty());
        printBill.setItems(printData);
    }

    @FXML
    private void onAddToCart() {
        String id = BookId.getText().trim();
        if (id.isEmpty()) {
            showAlert("Please enter a Book ID.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT Title, Price, Stocks FROM bookdetails WHERE BookId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String title = rs.getString("Title");
                String price = rs.getString("Price");
                String stocks = rs.getString("Stocks");

                stocksAvailable.setText(stocks);

                int availableQty = Integer.parseInt(stocks);
                int enteredQty = 1;
                try {
                    enteredQty = Integer.parseInt(quantityEnter.getText().trim());
                } catch (NumberFormatException e) {
                }

                if (availableQty >= enteredQty) {
                    Bill book = new Bill(id, title, String.valueOf(enteredQty), price);
                    cartData.add(book);
                    BookId.clear();
                    quantityEnter.clear();
                } else if(availableQty==0) {
                    showAlert("No Stock available!!");
                }else{
                    showAlert("Not enough stock available! Current stock: " + availableQty);

                }

            } else {
                showAlert("Book ID not found!");
                stocksAvailable.setText("N/A");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database error occurred.");
        }
    }


    @FXML private Label totalAmountField;
    @FXML public Label todaysDate;


    @FXML
    private void onMakeBill() {
        boolean nameValidity = customerNameField.getText().matches("[a-zA-Z ]+");

        if (customerNameField.getText().isEmpty() || customerMobileField.getText().isEmpty()) {
            showAlert("Please enter customer name and mobile number.");

        } else if (!nameValidity) {
            showAlert("Please enter the customer name correctly");
        } else if (customerMobileField.getText().length() != 10) {
            showAlert("Please enter valid mobile number");
        } else{


            printData.clear();
        printData.addAll(cartData);


        receiptCustomerName.setText(customerNameField.getText());
        receiptCustomerMobile.setText(customerMobileField.getText());

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        todaysDate.setText(today.format(formatter));

        double total = 0;
        for (Bill item : printData) {
            try {
                int qty = Integer.parseInt(item.getQuantity());
                double price = Double.parseDouble(item.getPrice());
                total += qty * price;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        totalAmountField.setText(String.format("%.2f", total));

        StringBuilder bookIds = new StringBuilder();
        for (Bill item : bookDetail.getItems()) {
            bookIds.append(item.getBookId()).append(", ");
        }

        if (bookIds.length() > 0) {
            bookIds.setLength(bookIds.length() - 2);
            purchasedBooks.setText(bookIds.toString());
        } else {
            purchasedBooks.setText("N/A");
        }
    }
}


    @FXML
    private void onClearCart() {
        cartData.clear();
        printData.clear();
        BookId.clear();
        customerNameField.clear();
        customerMobileField.clear();
        receiptCustomerName.setText("");
        receiptCustomerMobile.setText("");
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.setContentText(msg);

        DialogPane alertDialogPane = alert.getDialogPane();
        alertDialogPane.getStylesheets().add(getClass().getResource("/com/example/dnyandeepbookstore/alert.css").toExternalForm());
        alertDialogPane.getStyleClass().add("custom-alert");

        alert.showAndWait();
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

    @FXML private AnchorPane receiptPane;

    @FXML
    private void onPrintPDF() {
        Printer printer = Printer.getDefaultPrinter();
        if (printer == null) {
            showAlert("No printer found.");
            return;
        }

        PageLayout pageLayout = printer.createPageLayout(
                Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT
        );

        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if (job == null || !job.showPrintDialog(receiptPane.getScene().getWindow())) {
            return;
        }

        receiptPane.applyCss();
        receiptPane.layout();

        double scaleX = pageLayout.getPrintableWidth() / receiptPane.getLayoutBounds().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / receiptPane.getLayoutBounds().getHeight();
        double scale = Math.min(scaleX, scaleY);

        double oldX = receiptPane.getScaleX();
        double oldY = receiptPane.getScaleY();
        receiptPane.setScaleX(scale);
        receiptPane.setScaleY(scale);

        boolean success = job.printPage(pageLayout, receiptPane);

        receiptPane.setScaleX(oldX);
        receiptPane.setScaleY(oldY);

        if (success) {
            job.endJob();
            showAlert("PDF saved successfully.");
        } else {
            showAlert("Print failed.");
        }
    }



    @FXML private Label purchasedBooks;
    @FXML
    private void saveSaleToDatabase() {
        String customer = receiptCustomerName.getText();
        String mobile = receiptCustomerMobile.getText();
        String date = todaysDate.getText();
        String books = purchasedBooks.getText();  // Comma-separated if multiple
        String totalText = totalAmountField.getText();

        if (totalText == null || totalText.isEmpty()) {
            showAlert("Total amount is empty. Please generate bill first.");
            return;
        } else if (customer.isEmpty() || mobile.isEmpty()) {
            showAlert("Name and mobile is not on receipt");
            return;
        }

        double transactionAmount;
        try {
            transactionAmount = Double.parseDouble(totalText.replace("₹", "").trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            showAlert("Invalid total amount.");
            return;
        }

        String insertSale = "INSERT INTO sales (Customer_Name, Mobile_No, DateOfPurchase, Purchased_Book, Total_Amount) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookbillingsystem", "root", "sanika123");
             PreparedStatement pst = conn.prepareStatement(insertSale)) {

            pst.setString(1, customer);
            pst.setString(2, mobile);
            pst.setString(3, date);
            pst.setString(4, books);
            pst.setString(5, String.valueOf(transactionAmount));
            pst.executeUpdate();

            showAlert("Sale saved successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error saving sale: " + e.getMessage());
            return;
        }

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookbillingsystem", "root", "sanika123")) {

            String checkQuery = "SELECT Total FROM daily_totals WHERE Date = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, today);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                double currentTotal = rs.getDouble("Total");
                double newTotal = currentTotal + transactionAmount;

                String updateQuery = "UPDATE daily_totals SET Total = ? WHERE Date = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setDouble(1, newTotal);
                updateStmt.setString(2, today);
                updateStmt.executeUpdate();
            } else {
                String insertQuery = "INSERT INTO daily_totals (Date, Total) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setString(1, today);
                insertStmt.setDouble(2, transactionAmount);
                insertStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error updating daily totals.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookbillingsystem", "root", "sanika123")) {
            for (Bill item : printData) {
                String bookId = item.getBookId();
                int quantitySold = Integer.parseInt(item.getQuantity());

                String selectQuery = "SELECT Stocks FROM bookdetails WHERE BookId = ?";
                PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
                selectStmt.setString(1, bookId);
                ResultSet rs = selectStmt.executeQuery();

                if (rs.next()) {
                    int currentStock = Integer.parseInt(rs.getString("Stocks"));
                    int newStock = currentStock - quantitySold;
                    if (newStock < 0) newStock = 0;  // Prevent negative stock

                    String updateQuery = "UPDATE bookdetails SET Stocks = ? WHERE BookId = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                    updateStmt.setString(1, String.valueOf(newStock));
                    updateStmt.setString(2, bookId);
                    updateStmt.executeUpdate();

                    System.out.println("Stock updated for Book ID " + bookId + ": " + newStock + " remaining.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error updating stock after sale.");
        }
    }
}
