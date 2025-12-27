package com.location.voitures.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddVoitureDialog {
    
    private Stage dialogStage;
    
    public AddVoitureDialog() {
        createDialog();
    }
    
    private void createDialog() {
        dialogStage = new Stage();
        dialogStage.setTitle("Ajouter une Voiture");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        
        ScrollPane scrollPane = new ScrollPane();
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f8f9fa;");
        
        // Header
        Label titleLabel = new Label("🚗 Ajouter une Nouvelle Voiture");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Formulaire
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.setPadding(new Insets(25));
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        
        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.CENTER);
        
        // Champs du formulaire
        TextField marqueField = createFormField("Marque", "Renault", 0, 0, gridPane);
        TextField modeleField = createFormField("Modèle", "Clio", 1, 0, gridPane);
        TextField anneeField = createFormField("Année", "2023", 0, 1, gridPane);
        TextField couleurField = createFormField("Couleur", "Rouge", 1, 1, gridPane);
        TextField immatriculationField = createFormField("Immatriculation", "AB-123-CD", 0, 2, gridPane);
        TextField prixField = createFormField("Prix/jour (€)", "35.00", 1, 2, gridPane);
        
        // ComboBoxes
        Label carburantLabel = new Label("Type de carburant:");
        carburantLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        ComboBox<String> carburantCombo = new ComboBox<>();
        carburantCombo.getItems().addAll("ESSENCE", "DIESEL", "ELECTRIQUE", "HYBRIDE");
        carburantCombo.setValue("ESSENCE");
        carburantCombo.setStyle("-fx-background-radius: 8;");
        
        Label transmissionLabel = new Label("Transmission:");
        transmissionLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        ComboBox<String> transmissionCombo = new ComboBox<>();
        transmissionCombo.getItems().addAll("MANUELLE", "AUTOMATIQUE");
        transmissionCombo.setValue("MANUELLE");
        transmissionCombo.setStyle("-fx-background-radius: 8;");
        
        Label placesLabel = new Label("Nombre de places:");
        placesLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        Spinner<Integer> placesSpinner = new Spinner<>(2, 9, 5);
        placesSpinner.setStyle("-fx-background-radius: 8;");
        
        CheckBox disponibleCheck = new CheckBox("Disponible");
        disponibleCheck.setSelected(true);
        disponibleCheck.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Boutons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button ajouterBtn = new Button("✅ Ajouter la Voiture");
        ajouterBtn.setPrefWidth(200);
        ajouterBtn.setStyle(
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
        
        // Actions
        ajouterBtn.setOnAction(e -> {
            if (validateForm(marqueField, modeleField, anneeField, couleurField, immatriculationField, prixField)) {
                addVoiture(
                    marqueField.getText(), modeleField.getText(), anneeField.getText(),
                    couleurField.getText(), immatriculationField.getText(), prixField.getText(),
                    carburantCombo.getValue(), transmissionCombo.getValue(), 
                    placesSpinner.getValue(), disponibleCheck.isSelected()
                );
                dialogStage.close();
            }
        });
        
        annulerBtn.setOnAction(e -> dialogStage.close());
        
        buttonBox.getChildren().addAll(ajouterBtn, annulerBtn);
        
        // Ajout des composants au formulaire
        VBox extraFields = new VBox(10);
        extraFields.getChildren().addAll(
            carburantLabel, carburantCombo,
            transmissionLabel, transmissionCombo,
            placesLabel, placesSpinner,
            disponibleCheck
        );
        
        formBox.getChildren().addAll(gridPane, new Separator(), extraFields, buttonBox);
        root.getChildren().addAll(titleLabel, formBox);
        
        scrollPane.setContent(root);
        scrollPane.setFitToWidth(true);
        
        Scene scene = new Scene(scrollPane, 500, 650);
        dialogStage.setScene(scene);
    }
    
    private TextField createFormField(String label, String placeholder, int col, int row, GridPane grid) {
        Label lbl = new Label(label + ":");
        lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.setPrefWidth(180);
        field.setStyle("-fx-padding: 8; -fx-background-radius: 8;");
        
        grid.add(lbl, col * 2, row * 2);
        grid.add(field, col * 2, row * 2 + 1);
        
        return field;
    }
    
    private boolean validateForm(TextField... fields) {
        for (TextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs obligatoires!");
                return false;
            }
        }
        return true;
    }
    
    private void addVoiture(String marque, String modele, String annee, String couleur, 
                           String immatriculation, String prix, String carburant, 
                           String transmission, int places, boolean disponible) {
        
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Voiture Ajoutée");
        successAlert.setHeaderText("Nouvelle voiture ajoutée avec succès!");
        successAlert.setContentText(
            "Marque: " + marque + "\n" +
            "Modèle: " + modele + "\n" +
            "Année: " + annee + "\n" +
            "Couleur: " + couleur + "\n" +
            "Immatriculation: " + immatriculation + "\n" +
            "Prix: " + prix + "€/jour\n" +
            "Carburant: " + carburant + "\n" +
            "Transmission: " + transmission + "\n" +
            "Places: " + places + "\n" +
            "Disponible: " + (disponible ? "Oui" : "Non")
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