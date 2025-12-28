package com.location.voitures.ui;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;
public class ClientFormDialog {
    private Stage dialogStage;
    private String selectedVoiture;
    public ClientFormDialog(String voitureModele) {
        this.selectedVoiture = voitureModele;
        createDialog();
    }
    private void createDialog() {
        dialogStage = new Stage();
        dialogStage.setTitle("Informations Client - Réservation");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        ScrollPane scrollPane = new ScrollPane();
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f8f9fa;");
        Label titleLabel = new Label("👤 Informations Client");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        Label subtitleLabel = new Label("Réservation: " + selectedVoiture);
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #667eea; -fx-font-weight: bold;");
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.setPadding(new Insets(25));
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        Label infoLabel = new Label("📋 Informations personnelles");
        infoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        TextField nomField = createTextField("Nom", 0, 0, gridPane);
        TextField prenomField = createTextField("Prénom", 1, 0, gridPane);
        TextField emailField = createTextField("Email", 0, 1, gridPane);
        TextField telephoneField = createTextField("Téléphone", 1, 1, gridPane);
        Label adresseLabel = new Label("Adresse:");
        adresseLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        TextArea adresseArea = new TextArea();
        adresseArea.setPromptText("Adresse complète");
        adresseArea.setPrefRowCount(2);
        adresseArea.setPrefWidth(400);
        adresseArea.setStyle("-fx-background-radius: 8;");
        Label datesLabel = new Label("📅 Période de location");
        datesLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        HBox datesBox = new HBox(15);
        datesBox.setAlignment(Pos.CENTER);
        VBox dateDebutBox = new VBox(5);
        Label dateDebutLabel = new Label("Date début:");
        dateDebutLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        DatePicker dateDebutPicker = new DatePicker(LocalDate.now());
        dateDebutPicker.setStyle("-fx-background-radius: 8;");
        dateDebutBox.getChildren().addAll(dateDebutLabel, dateDebutPicker);
        VBox dateFinBox = new VBox(5);
        Label dateFinLabel = new Label("Date fin:");
        dateFinLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        DatePicker dateFinPicker = new DatePicker(LocalDate.now().plusDays(1));
        dateFinPicker.setStyle("-fx-background-radius: 8;");
        dateFinBox.getChildren().addAll(dateFinLabel, dateFinPicker);
        datesBox.getChildren().addAll(dateDebutBox, dateFinBox);
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        Button confirmerBtn = new Button("✅ Confirmer Réservation");
        confirmerBtn.setPrefWidth(200);
        confirmerBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #27ae60, #2ecc71); " +
            "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 25; " +
            "-fx-padding: 12 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );
        Button annulerBtn = new Button("❌ Annuler");
        annulerBtn.setPrefWidth(150);
        annulerBtn.setStyle(
            "-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; " +
            "-fx-background-radius: 25; -fx-padding: 12 20;"
        );
        confirmerBtn.setOnAction(e -> {
            if (validateForm(nomField, prenomField, emailField, telephoneField, adresseArea)) {
                processReservation(nomField.getText(), prenomField.getText(), emailField.getText(), 
                                 telephoneField.getText(), adresseArea.getText(), 
                                 dateDebutPicker.getValue(), dateFinPicker.getValue());
                dialogStage.close();
            }
        });
        annulerBtn.setOnAction(e -> dialogStage.close());
        buttonBox.getChildren().addAll(confirmerBtn, annulerBtn);
        formBox.getChildren().addAll(
            infoLabel, gridPane, adresseLabel, adresseArea,
            new Separator(), datesLabel, datesBox, buttonBox
        );
        root.getChildren().addAll(titleLabel, subtitleLabel, formBox);
        scrollPane.setContent(root);
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane, 500, 600);
        dialogStage.setScene(scene);
    }
    private TextField createTextField(String label, int col, int row, GridPane grid) {
        Label lbl = new Label(label + ":");
        lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        TextField field = new TextField();
        field.setPromptText(label);
        field.setPrefWidth(180);
        field.setStyle("-fx-padding: 8; -fx-background-radius: 8;");
        grid.add(lbl, col * 2, row * 2);
        grid.add(field, col * 2, row * 2 + 1);
        return field;
    }
    private boolean validateForm(TextField nom, TextField prenom, TextField email, 
                                TextField telephone, TextArea adresse) {
        if (nom.getText().trim().isEmpty() || prenom.getText().trim().isEmpty() || 
            email.getText().trim().isEmpty() || telephone.getText().trim().isEmpty() ||
            adresse.getText().trim().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires!");
            return false;
        }
        return true;
    }
    private void processReservation(String nom, String prenom, String email, String telephone, 
                                  String adresse, LocalDate dateDebut, LocalDate dateFin) {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Réservation Confirmée");
        successAlert.setHeaderText("Réservation réussie!");
        successAlert.setContentText(
            "Client: " + prenom + " " + nom + "\n" +
            "Voiture: " + selectedVoiture + "\n" +
            "Période: " + dateDebut + " au " + dateFin + "\n\n" +
            "La voiture est maintenant réservée."
        );
        successAlert.showAndWait();
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
}
