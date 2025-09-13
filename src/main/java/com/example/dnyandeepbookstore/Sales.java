package com.example.dnyandeepbookstore;

import javafx.beans.property.SimpleStringProperty;


    public class Sales {
        private final SimpleStringProperty customerName;
        private final SimpleStringProperty mobile;
        private final SimpleStringProperty date;
        private final SimpleStringProperty book;
        private final SimpleStringProperty total;

        public Sales(String customerName, String mobile, String date, String book, String total) {
            this.customerName = new SimpleStringProperty(customerName);
            this.mobile = new SimpleStringProperty(mobile);
            this.date = new SimpleStringProperty(date);
            this.book = new SimpleStringProperty(book);
            this.total = new SimpleStringProperty(total);
        }

        public String getCustomerName() { return customerName.get(); }
        public String getMobile() { return mobile.get(); }
        public String getDate() { return date.get(); }
        public String getBook() { return book.get(); }
        public String getTotal() { return total.get(); }
    }


