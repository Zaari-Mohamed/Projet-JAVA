package com.location.voitures.ui;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
public class AdminLoginDialog {
    private Stage dialogStage;
    private boolean loginSuccess = false;
    private String selectedVoiture;
    public AdminLoginDialog(String voitureModele) {
        this.selectedVoiture = voitureModele;
        createDialog();
    }
    private void createDialog() {
        dialogStage = new Stage();
        dialogStage.setTitle("Connexion Admin - Réservation");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f8f9fa;");
        Label titleLabel = new Label("🔐 Connexion Administrateur");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        Label subtitleLabel = new Label("Réservation: " + selectedVoiture);
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #667eea; -fx-font-weight: bold;");
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(20));
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        TextField emailField = new TextField();
        emailField.setPromptText("admin@location.com");
        emailField.setPrefWidth(300);
        emailField.setStyle("-fx-padding: 10; -fx-background-radius: 8;");
        Label passwordLabel = new Label("Mot de passe:");
        passwordLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        passwordField.setPrefWidth(300);
        passwordField.setStyle("-fx-padding: 10; -fx-background-radius: 8;");
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        Button connecterBtn = new Button("🔑 Se connecter");
        connecterBtn.setPrefWidth(150);
        connecterBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
            "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 25; " +
            "-fx-padding: 12 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );
        Button annulerBtn = new Button("❌ Annuler");
        annulerBtn.setPrefWidth(150);
        annulerBtn.setStyle(
            "-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; " +
            "-fx-background-radius: 25; -fx-padding: 12 20;"
        );
        connecterBtn.setOnAction(e -> {
            if (validateLogin(emailField.getText(), passwordField.getText())) {
                loginSuccess = true;
                dialogStage.close();
                openClientForm();
            } else {
                showAlert("Erreur", "Email ou mot de passe incorrect!");
            }
        });
        annulerBtn.setOnAction(e -> dialogStage.close());
        buttonBox.getChildren().addAll(connecterBtn, annulerBtn);
        formBox.getChildren().addAll(emailLabel, emailField, passwordLabel, passwordField, buttonBox);
        root.getChildren().addAll(titleLabel, subtitleLabel, formBox);
        Scene scene = new Scene(root, 400, 350);
        dialogStage.setScene(scene);
    }
    private boolean validateLogin(String email, String password) {
        return "admin@location.com".equals(email) && "admin123".equals(password);
    }
    private void openClientForm() {
        ClientFormDialog clientForm = new ClientFormDialog(selectedVoiture);
        clientForm.show();
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
