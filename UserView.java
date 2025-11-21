package View;

import Model.User;
import java.util.List;

public class UserView {

    public void mostrarUsuarios(List<User> usuarios) {
        System.out.println("=== Lista de Usuários ===");
        for (User u : usuarios) {
            System.out.println(u); 
        }
    }
}
