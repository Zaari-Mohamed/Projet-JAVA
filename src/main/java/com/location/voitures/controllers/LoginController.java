package com.location.voitures.controllers;

import com.location.voitures.dao.UtilisateurDAO;
import com.location.voitures.entities.Admin;
import com.location.voitures.services.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    private AuthService authService;
    private String context;

    public void initialize() {
        authService = new AuthService(new UtilisateurDAO());
    }

    public void setContext(String context) {
        this.context = context;
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        
        System.out.println("Tentative de connexion avec: " + email);

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            System.out.println("Appel loginAdmin...");
            Optional<Admin> admin = authService.loginAdmin(email, password);
            System.out.println("Résultat loginAdmin: " + admin.isPresent());
            
            if (admin.isPresent()) {
                System.out.println("Admin trouvé, context: " + context);
                if ("admin".equals(context)) {
                    openAdminDashboard();
                } else {
                    openClientForm();
                }
                closeWindow();
            } else {
                showAlert("Erreur", "Identifiants administrateur invalides.");
            }
        } catch (Exception e) {
            System.out.println("Exception dans handleLogin: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Erreur de connexion : " + e.getMessage());
        }
    }

    private void openAdminDashboard() {
        try {
            System.out.println("Tentative d'ouverture AdminDashboard...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminDashboard.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Administration - Gestion des Voitures");
            stage.setScene(new Scene(root, 1000, 700));
            stage.setResizable(true);
            
            // Gérer la restauration de la fenêtre maximisée
            stage.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {
                if (!isNowMaximized && wasMaximized) {
                    // Quand on restaure depuis maximisé, repositionner la fenêtre
                    stage.setX(50);
                    stage.setY(50);
                }
            });
            
            stage.show();
            System.out.println("AdminDashboard ouvert avec succès");
        } catch (Exception e) {
            System.out.println("Erreur ouverture AdminDashboard: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir l'interface d'administration: " + e.getMessage());
        }
    }

    private void openClientForm() {
        showAlert("Info", "Formulaire client à implémenter");
    }

    private void closeWindow() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert.AlertType alertType = title.equals("Info") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR;
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}