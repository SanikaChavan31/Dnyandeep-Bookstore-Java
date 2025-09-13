package com.example.dnyandeepbookstore;

import javafx.beans.property.*;

public class DailyTransactions {
    private final StringProperty date;
    private final DoubleProperty total;

    public DailyTransactions(String date, double total) {
        this.date = new SimpleStringProperty(date);
        this.total = new SimpleDoubleProperty(total);
    }

    public String getDate() {
        return date.get();
    }

    public double getTotal() {
        return total.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public DoubleProperty totalProperty() {
        return total;
    }
}
