package View;

import Model.Contato;
import java.util.List;

public class ContatoView {

    public void mostrarContatos(List<Contato> contatos) {
        System.out.println("\n=== Lista de Contatos ===");
        if (contatos.isEmpty()) {
            System.out.println("Nenhum contato cadastrado.");
        } else {
            for (Contato contato : contatos) {
                System.out.println(contato);
            }
            System.out.println("Total: " + contatos.size() + " contato(s)");
        }
    }

    public void mostrarMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
