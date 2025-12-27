package com.location.voitures.ui;

import com.location.voitures.dao.UtilisateurDAO;
import com.location.voitures.entities.Admin;
import com.location.voitures.services.AuthService;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class AdminPanelLoginDialog {
    
    private Stage dialogStage;
    private boolean loginSuccess = false;
    private AuthService authService;
    
    public AdminPanelLoginDialog() {
        authService = new AuthService(new UtilisateurDAO());
        createDialog();
    }
    
    private void createDialog() {
        dialogStage = new Stage();
        dialogStage.setTitle("Accès Administration");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        
        VBox root = new VBox(25);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea, #764ba2);");
        
        // Header
        Label titleLabel = new Label("⚙️ Administration");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Label subtitleLabel = new Label("Accès au panel d'administration");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #ecf0f1;");
        
        // Formulaire de connexion
        VBox formBox = new VBox(20);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(30));
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");
        
        Label emailLabel = new Label("📧 Email Administrateur:");
        emailLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-font-size: 14px;");
        TextField emailField = new TextField();
        emailField.setPromptText("admin@email.com");
        emailField.setPrefWidth(320);
        emailField.setStyle("-fx-padding: 12; -fx-background-radius: 10; -fx-font-size: 14px;");
        
        Label passwordLabel = new Label("🔒 Mot de passe:");
        passwordLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-font-size: 14px;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        passwordField.setPrefWidth(320);
        passwordField.setStyle("-fx-padding: 12; -fx-background-radius: 10; -fx-font-size: 14px;");
        
        // Boutons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button connecterBtn = new Button("🚀 Accéder au Panel");
        connecterBtn.setPrefWidth(180);
        connecterBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #27ae60, #2ecc71); " +
            "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 25; " +
            "-fx-padding: 15 25; -fx-font-size: 14px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );
        
        Button annulerBtn = new Button("❌ Annuler");
        annulerBtn.setPrefWidth(120);
        annulerBtn.setStyle(
            "-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; " +
            "-fx-background-radius: 25; -fx-padding: 15 25; -fx-font-size: 14px;"
        );
        
        // Actions
        connecterBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            
            if (email.isEmpty() || password.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }
            
            try {
                Optional<Admin> admin = authService.loginAdmin(email, password);
                if (admin.isPresent()) {
                    loginSuccess = true;
                    dialogStage.close();
                    openAdminPanel();
                } else {
                    showAlert("Erreur d'authentification", "Email ou mot de passe incorrect!");
                }
            } catch (Exception ex) {
                showAlert("Erreur", "Erreur de connexion : " + ex.getMessage());
            }
        });
        
        annulerBtn.setOnAction(e -> dialogStage.close());
        
        buttonBox.getChildren().addAll(connecterBtn, annulerBtn);
        formBox.getChildren().addAll(emailLabel, emailField, passwordLabel, passwordField, buttonBox);
        root.getChildren().addAll(titleLabel, subtitleLabel, formBox);
        
        Scene scene = new Scene(root, 450, 400);
        dialogStage.setScene(scene);
    }
    
    private void openAdminPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminDashboard.fxml"));
            Parent root = loader.load();
            
            Stage adminStage = new Stage();
            adminStage.setTitle("Administration - Gestion des Voitures");
            adminStage.setScene(new Scene(root, 1200, 800));
            adminStage.show();
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ouvrir le panel admin: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void show() {
        dialogStage.showAndWait();
    }
    
    public boolean isLoginSuccess() {
        return loginSuccess;
    }
}