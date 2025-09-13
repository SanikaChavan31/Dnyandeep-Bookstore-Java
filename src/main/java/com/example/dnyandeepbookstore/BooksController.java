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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class BooksController implements Initializable {

    public void deleteButton(ActionEvent e) throws IOException {
        String id = bookIdtf.getText();

        if (id == null || id.trim().isEmpty()) {
            FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("deletebook.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 300, 200);
            scene.getStylesheets().add(getClass().getResource("/com/example/dnyandeepbookstore/alert.css").toExternalForm());
            showAlert("Warning", "Please enter a valid Book ID.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookbillingsystem", "root", "sanika123")) {
            String query = "DELETE FROM bookdetails WHERE BookId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id.trim());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Success", "Book deleted successfully.");
            } else {
                showAlert("Error", "No book found with that ID.");
            }

        } catch (SQLException se) {
            showAlert("Error", "Failed to delete book: " + se.getMessage());
        }
        Stage deleteBook = (Stage) ((Button) e.getSource()).getScene().getWindow();
        deleteBook.close();

    }

    public void deleteBookButton(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("deletebook.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 200);
        scene.getStylesheets().add(getClass().getResource("/com/example/dnyandeepbookstore/books.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/com/example/dnyandeepbookstore/alert.css").toExternalForm());
        Stage stage=new Stage();
        stage.getIcons().clear();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setTitle(title);

        DialogPane alertDialogPane = alert.getDialogPane();
        alertDialogPane.getStylesheets().add(getClass().getResource("/com/example/dnyandeepbookstore/alert.css").toExternalForm());
        alertDialogPane.getStyleClass().add("custom-alert");

        alert.showAndWait();
    }



    public void addBookButton(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("addbook.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 615, 459);
        scene.getStylesheets().add(getClass().getResource("/com/example/dnyandeepbookstore/books.css").toExternalForm());
        Stage stage=new Stage();
        stage.getIcons().clear();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

    }

    public void cancelClick(ActionEvent e){
        Stage stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
        stage.hide();
    }

    @FXML TextField bookIdtf=new TextField();
    @FXML TextField titletf=new TextField();
    @FXML TextField authortf=new TextField();
    @FXML TextField publishertf=new TextField();
    @FXML TextField pricetf=new TextField();
    @FXML TextField isbntf=new TextField();
    @FXML TextField stocktf=new TextField();

    public void addClick(ActionEvent e){
        String bookid=bookIdtf.getText();
        String title=titletf.getText();
        String author=authortf.getText();
        String publisher=publishertf.getText();
        String price=pricetf.getText();
        String isbn=isbntf.getText();
        String stock=stocktf.getText();


        if(bookid.isEmpty()||title.isEmpty()||author.isEmpty()||publisher.isEmpty()||price.isEmpty()||isbn.isEmpty()||stock.isEmpty()){
            showAlert("Error","Please Enter all the details");
        }else {
            try {
                Conn conn = new Conn();

                String query = "insert into bookdetails values('" + bookid + "','" + title + "','" + author + "','" + publisher + "','" + price + "','" + isbn + "','" + stock + "')";

                conn.s.executeUpdate(query);


            } catch (Exception ee) {
                ee.printStackTrace();
            }

            Stage addBook = (Stage) ((Button) e.getSource()).getScene().getWindow();
            addBook.close();
        }
    }

    @FXML AnchorPane bookdetailspane=new AnchorPane();
    @FXML TableView<Books> BookTable;
    @FXML TableColumn<Books,String> BookId;
    @FXML TableColumn<Books,String> Title;
    @FXML TableColumn<Books,String> Author;
    @FXML TableColumn<Books,String> Publisher;
    @FXML TableColumn<Books,String> Price;
    @FXML TableColumn<Books,String> ISBN;
    @FXML TableColumn<Books,String> Stocks;

    ObservableList<Books> bookList = FXCollections.observableArrayList();
    private void loadBooks() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookbillingsystem", "root", "sanika123");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM bookdetails");

            while (rs.next()) {
                bookList.add(new Books(
                        rs.getString("BookId"),
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getString("Publisher"),
                        rs.getString("Price"),
                        rs.getString("ISBN"),
                        rs.getString("Stocks")
                ));
            }

            BookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
            Title.setCellValueFactory(new PropertyValueFactory<>("title"));
            Author.setCellValueFactory(new PropertyValueFactory<>("author"));
            Publisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
            Price.setCellValueFactory(new PropertyValueFactory<>("price"));
            ISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
            Stocks.setCellValueFactory(new PropertyValueFactory<>("stocks"));

            BookTable.setItems(bookList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadBooks();
    }
    public void refreshBookTable() {
        bookList.clear();
        loadBooks();
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

    @FXML
    private TextField searchField;

    @FXML
    public void handleSearch() {
        String keyword = searchField.getText().trim();
        bookList.clear();

        if (keyword.isEmpty()) {
            loadBooks();
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookbillingsystem", "root", "sanika123")) {
            String query = "SELECT * FROM (" +
                    "SELECT *, " +
                    "CASE " +
                    " WHEN Title LIKE ? THEN 1 " +
                    " WHEN Author LIKE ? THEN 2 " +
                    " WHEN Publisher LIKE ? THEN 3 " +
                    " ELSE 4 " +
                    "END AS relevance_rank " +
                    "FROM bookdetails " +
                    "WHERE Title LIKE ? OR Author LIKE ? OR Publisher LIKE ?" +
                    ") AS ranked_books " +
                    "ORDER BY relevance_rank";


            PreparedStatement stmt = conn.prepareStatement(query);
            String likePattern = "%" + keyword + "%";

            stmt.setString(1, likePattern);
            stmt.setString(2, likePattern);
            stmt.setString(3, likePattern);
            stmt.setString(4, likePattern);
            stmt.setString(5, likePattern);
            stmt.setString(6, likePattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookList.add(new Books(
                        rs.getString("BookId"),
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getString("Publisher"),
                        rs.getString("Price"),
                        rs.getString("ISBN"),
                        rs.getString("Stocks")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
