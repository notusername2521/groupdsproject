package Db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    public static Connection getConnection() throws SQLException {
        String url = System.getenv("DATABASE_URL");
        
        if (url == null || url.isEmpty()) {
            throw new SQLException("DATABASE_URL environment variable not set!");
        }

        try {
            Class.forName("org.postgresql.Driver"); 
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL não encontrado!", e);
        }

        return DriverManager.getConnection(url);
    }
}
