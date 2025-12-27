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
        // Recherche l'utilisateur par email
        Optional<Utilisateur> userOpt = utilisateurDAO.findByEmail(email);

        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            // Vérifie le mot de passe hashé
            if (PasswordUtil.verifyPassword(password, user.getMotDePasseHashed())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
    
    public Optional<Admin> loginAdmin(String email, String password) {
        System.out.println("loginAdmin - email: " + email);
        // Recherche directement l'utilisateur
        Optional<Utilisateur> userOpt = utilisateurDAO.findByEmail(email);
        System.out.println("Utilisateur trouvé: " + userOpt.isPresent());
        
        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            System.out.println("Type utilisateur: " + user.getClass().getSimpleName());
            System.out.println("Hash en base: " + user.getMotDePasseHashed());
            
            // Vérification du mot de passe
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
        // Vérifie l'unicité de l'email avant l'inscription
        if (utilisateurDAO.findByEmail(newUser.getEmail()).isPresent()) {
            throw new IllegalArgumentException("L'email est déjà utilisé.");
        }
        
        // Hashe le mot de passe
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        newUser.setMotDePasseHashed(hashedPassword); 
        
        // Sauvegarde dans la base de données
        utilisateurDAO.save(newUser);
    }
    
    public void resetPassword(String email, String newPassword) {
        Optional<Utilisateur> userOpt = utilisateurDAO.findByEmail(email);
        userOpt.ifPresent(user -> {
            // Validation simple de la longueur du mot de passe
            if (newPassword == null || newPassword.length() < 6) {
                 throw new IllegalArgumentException("Le nouveau mot de passe est trop court.");
            }
            // Hashe le nouveau mot de passe
            String hashedPassword = PasswordUtil.hashPassword(newPassword);
            user.setMotDePasseHashed(hashedPassword);
            // Met à jour l'utilisateur
            utilisateurDAO.update(user);
        });
    }
}