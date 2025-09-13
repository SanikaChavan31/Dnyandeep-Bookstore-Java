package com.example.dnyandeepbookstore;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Bill {
    private final StringProperty bookId;
    private final StringProperty title;
    private final StringProperty quantity;
    private final StringProperty price;


    public Bill(String bookId, String title, String quantity, String price) {
        this.bookId = new SimpleStringProperty(bookId);
        this.title = new SimpleStringProperty(title);
        this.quantity = new SimpleStringProperty(quantity);
        this.price = new SimpleStringProperty(price);
    }

    public String getBookId() {
        return bookId.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getQuantity() {
        return quantity.get();
    }

    public String getPrice() {
        return price.get();
    }

    public StringProperty bookIdProperty() {
        return bookId;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty quantityProperty() {
        return quantity;
    }

    public StringProperty priceProperty() {
        return price;
    }
}
