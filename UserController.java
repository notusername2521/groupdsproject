package Controller;

import Db.Database;
import Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    public void criarTabelaSeNaoExiste() {
        try (Connection conn = Database.getConnection()) {
            String sql = "CREATE TABLE IF NOT EXISTS Usuario (" +
                        "id SERIAL PRIMARY KEY, " +
                        "name VARCHAR(100) NOT NULL, " +
                        "email VARCHAR(150) UNIQUE NOT NULL)";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("Table Usuario ready.");
        } catch (Exception e) {
            System.err.println("Error creating table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<User> listarUsuarios() {
        List<User> usuarios = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM Usuario"; 
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                User u = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email")
                );
                usuarios.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    public User createUser(String name, String email) {
        User newUser = null;
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO Usuario (name, email) VALUES (?, ?) RETURNING id";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                newUser = new User(id, name, email);
                System.out.println("Successfully created user: " + name);
            }
            
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return newUser;
    }

    public int deleteUsersByNames(List<String> names) {
        int deletedCount = 0;
        try (Connection conn = Database.getConnection()) {
            String placeholders = String.join(",", names.stream()
                .map(n -> "?")
                .toArray(String[]::new));
            String sql = "DELETE FROM Usuario WHERE name IN (" + placeholders + ")";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < names.size(); i++) {
                pstmt.setString(i + 1, names.get(i));
            }
            
            deletedCount = pstmt.executeUpdate();
            System.out.println("Successfully deleted " + deletedCount + " user(s).");
            
        } catch (Exception e) {
            System.err.println("Error deleting users: " + e.getMessage());
            e.printStackTrace();
        }
        
        return deletedCount;
    }
}
