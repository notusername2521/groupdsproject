package Model;

import Db.Database;
import java.sql.*;

public class User {
    private int id;
      String name;
    private String email;
 

    // parenteses = valores que serão passados para o construtor
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    
    }

    // Getters e setters
    public int getId() 
    { return id; }
    public void setId(int id)
    { this.id = id; }

    public String getName()
    { return name; }
    public void setName(String name)
    { this.name = name; }

    public String getEmail() 
    { return email; }
    
    public void setEmail(String email) 
    { this.email = email; }

    // Sobrescrevendo toString para exibir dados do usuário
    @Override
    public String toString() {
        return "ID: " + id + ", Nome: " + name + ", Email: " + email;
    }
}
