package com.location.voitures.test;
import com.location.voitures.dao.UtilisateurDAO;
import com.location.voitures.entities.Utilisateur;
import com.location.voitures.utils.PasswordUtil;
import java.util.List;
public class DatabaseTest {
    public static void main(String[] args) {
        UtilisateurDAO dao = new UtilisateurDAO();
        System.out.println("=== TOUS LES UTILISATEURS ===");
        List<Utilisateur> users = dao.findAll();
        for (Utilisateur user : users) {
            System.out.println("Email: " + user.getEmail());
            System.out.println("Type: " + user.getClass().getSimpleName());
            System.out.println("Hash: " + user.getMotDePasseHashed());
            System.out.println("---");
        }
        System.out.println("\n=== TEST PASSWORDS ===");
        String hash = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi.";
        System.out.println("Test 'hello': " + PasswordUtil.verifyPassword("hello", hash));
        System.out.println("Test 'admin': " + PasswordUtil.verifyPassword("admin", hash));
    }
}
