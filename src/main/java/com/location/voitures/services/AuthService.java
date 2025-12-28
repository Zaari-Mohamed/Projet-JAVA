package com.location.voitures.services;
import com.location.voitures.dao.UtilisateurDAO;
import com.location.voitures.entities.Admin;
import com.location.voitures.entities.Utilisateur;
import com.location.voitures.utils.PasswordUtil;
import java.util.Optional;
public class AuthService {
    private final UtilisateurDAO utilisateurDAO;
    public AuthService(UtilisateurDAO utilisateurDAO) {
        this.utilisateurDAO = utilisateurDAO;
    }
    public Optional<Utilisateur> login(String email, String password) {
        Optional<Utilisateur> userOpt = utilisateurDAO.findByEmail(email);
        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            if (PasswordUtil.verifyPassword(password, user.getMotDePasseHashed())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
    public Optional<Admin> loginAdmin(String email, String password) {
        System.out.println("loginAdmin - email: " + email);
        Optional<Utilisateur> userOpt = utilisateurDAO.findByEmail(email);
        System.out.println("Utilisateur trouvé: " + userOpt.isPresent());
        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            System.out.println("Type utilisateur: " + user.getClass().getSimpleName());
            System.out.println("Hash en base: " + user.getMotDePasseHashed());
            boolean passwordMatch = PasswordUtil.verifyPassword(password, user.getMotDePasseHashed());
            System.out.println("Mot de passe correct: " + passwordMatch);
            if (passwordMatch && user instanceof Admin) {
                System.out.println("Admin authentifié avec succès");
                return Optional.of((Admin) user);
            }
        }
        System.out.println("Échec authentification admin");
        return Optional.empty();
    }
    public void registerUser(Utilisateur newUser, String plainPassword) {
        if (utilisateurDAO.findByEmail(newUser.getEmail()).isPresent()) {
            throw new IllegalArgumentException("L'email est déjà utilisé.");
        }
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        newUser.setMotDePasseHashed(hashedPassword); 
        utilisateurDAO.save(newUser);
    }
    public void resetPassword(String email, String newPassword) {
        Optional<Utilisateur> userOpt = utilisateurDAO.findByEmail(email);
        userOpt.ifPresent(user -> {
            if (newPassword == null || newPassword.length() < 6) {
                 throw new IllegalArgumentException("Le nouveau mot de passe est trop court.");
            }
            String hashedPassword = PasswordUtil.hashPassword(newPassword);
            user.setMotDePasseHashed(hashedPassword);
            utilisateurDAO.update(user);
        });
    }
}
