package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private DB() {}

    public static Connection connect(String url, String user, String pass) throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }
}