package com.example.dnyandeepbookstore;

public class Books {

    private String bookId;
    private String title;
    private String author;
    private String publisher;
    private String price;
    private String isbn;
    private String stocks;

    public Books(String bookId, String title, String author, String publisher, String price, String isbn, String stocks) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.isbn = isbn;
        this.stocks = stocks;
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPrice() {
        return price;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getStocks() {
        return stocks;
    }
}
