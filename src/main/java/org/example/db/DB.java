package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521/freepdb1";

    public static Connection getConnection(String user, String password) throws SQLException {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle JDBC Driver not found", e);
        }

        Properties props = new Properties();
        props.put("user", user.toUpperCase());
        props.put("password", password);
        props.put("oracle.net.disableOob", "true");

        return DriverManager.getConnection(URL, props);
    }
}
