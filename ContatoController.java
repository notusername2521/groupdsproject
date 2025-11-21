package Controller;

import Db.Database;
import Model.Contato;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContatoController {

    public void criarTabelaSeNaoExiste() {
        try (Connection conn = Database.getConnection()) {
            String sql = "CREATE TABLE IF NOT EXISTS Contatos (" +
                        "id SERIAL PRIMARY KEY, " +
                        "nome VARCHAR(100) NOT NULL, " +
                        "email VARCHAR(150), " +
                        "telefone VARCHAR(20), " +
                        "endereco VARCHAR(200))";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("Tabela Contatos pronta.");
        } catch (Exception e) {
            System.err.println("Erro ao criar tabela: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Contato> listarContatos() {
        List<Contato> contatos = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM Contatos ORDER BY id"; 
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Contato c = new Contato(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("endereco")
                );
                contatos.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return contatos;
    }

    public Contato criarContato(String nome, String email, String telefone, String endereco) {
        Contato novoContato = null;
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO Contatos (nome, email, telefone, endereco) VALUES (?, ?, ?, ?) RETURNING id";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nome);
            pstmt.setString(2, email);
            pstmt.setString(3, telefone);
            pstmt.setString(4, endereco);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                novoContato = new Contato(id, nome, email, telefone, endereco);
                System.out.println("Contato criado com sucesso: " + nome);
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao criar contato: " + e.getMessage());
            e.printStackTrace();
        }
        
        return novoContato;
    }

    public boolean atualizarContato(int id, String nome, String email, String telefone, String endereco) {
        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE Contatos SET nome = ?, email = ?, telefone = ?, endereco = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nome);
            pstmt.setString(2, email);
            pstmt.setString(3, telefone);
            pstmt.setString(4, endereco);
            pstmt.setInt(5, id);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Contato atualizado com sucesso.");
                return true;
            } else {
                System.out.println("Nenhum contato encontrado com ID: " + id);
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao atualizar contato: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public int deletarContatosPorNome(List<String> nomes) {
        int deletedCount = 0;
        try (Connection conn = Database.getConnection()) {
            String placeholders = String.join(",", nomes.stream()
                .map(n -> "?")
                .toArray(String[]::new));
            String sql = "DELETE FROM Contatos WHERE nome IN (" + placeholders + ")";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < nomes.size(); i++) {
                pstmt.setString(i + 1, nomes.get(i));
            }
            
            deletedCount = pstmt.executeUpdate();
            System.out.println(deletedCount + " contato(s) deletado(s) com sucesso.");
            
        } catch (Exception e) {
            System.err.println("Erro ao deletar contatos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return deletedCount;
    }

    public boolean deletarContatoPorId(int id) {
        try (Connection conn = Database.getConnection()) {
            String sql = "DELETE FROM Contatos WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Contato deletado com sucesso.");
                return true;
            } else {
                System.out.println("Nenhum contato encontrado com ID: " + id);
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao deletar contato: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
