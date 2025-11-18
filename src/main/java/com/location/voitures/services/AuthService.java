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
        // Utilise la connexion de base
        Optional<Utilisateur> userOpt = login(email, password); 

        // Vérifie si l'utilisateur est bien un Admin
        if (userOpt.isPresent() && userOpt.get() instanceof Admin) {
            return Optional.of((Admin) userOpt.get());
        }
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