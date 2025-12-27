package com.location.voitures;

import com.location.voitures.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Optional;

public class MainApp extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("MainApp start() appelé");
        
        // Test de connexion base
        try {
            com.location.voitures.services.VoitureService vs = new com.location.voitures.services.VoitureService();
            System.out.println("Nombre de voitures en base: " + vs.findAll().size());
            
            // Ajouter admin si n'existe pas
            com.location.voitures.dao.UtilisateurDAO utilisateurDAO = new com.location.voitures.dao.UtilisateurDAO();
            if (utilisateurDAO.findByEmail("admin@email.com").isEmpty()) {
                System.out.println("Création admin...");
                com.location.voitures.services.AuthService authService = new com.location.voitures.services.AuthService(utilisateurDAO);
                com.location.voitures.entities.Admin admin = new com.location.voitures.entities.Admin();
                admin.setNom("Admin");
                admin.setPrenom("Admin");
                admin.setEmail("admin@email.com");
                admin.setTelephone("0123456789");
                admin.setNiveauAcces(1);
                authService.registerUser(admin, "admin");
            } else {
                System.out.println("Admin existe déjà");
            }
            
            // Ajouter des données de test si base vide
            if (vs.findAll().size() < 4) {
                System.out.println("Ajout de données de test...");
                
                // Ajouter voitures
                vs.saveVoiture(new com.location.voitures.entities.Voiture("Renault", "Clio", 2020, "AB-123-CD", 35.0, true));
                vs.saveVoiture(new com.location.voitures.entities.Voiture("Peugeot", "308", 2021, "EF-456-GH", 45.0, true));
                vs.saveVoiture(new com.location.voitures.entities.Voiture("BMW", "Serie3", 2022, "IJ-789-KL", 80.0, false));
                vs.saveVoiture(new com.location.voitures.entities.Voiture("Audi", "A4", 2021, "MN-012-OP", 75.0, true));
                
                System.out.println("Données de test ajoutées !");
            }
        } catch (Exception e) {
            System.out.println("Erreur connexion base: " + e.getMessage());
        }
        
        // Start with user dashboard - users can see cars, admin button for login
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserDashboard.fxml"));
            Parent root = loader.load();
            
            primaryStage.setTitle("Location de Voitures");
            primaryStage.setScene(new Scene(root, 1000, 700));
            primaryStage.setResizable(true);
            
            // Gérer la restauration de la fenêtre maximisée
            primaryStage.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {
                if (!isNowMaximized && wasMaximized) {
                    // Quand on restaure depuis maximisé, repositionner la fenêtre
                    primaryStage.setX(50);
                    primaryStage.setY(50);
                }
            });
            
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("ERROR loading UserDashboard: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("--- Application de Location de Voitures ---");
        launch(args);
    }
}