package com.example.dnyandeepbookstore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Conn {
    Connection c;
    Statement s;

    public Conn(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql:///bookbillingsystem","username","password");
            s = c.createStatement();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
