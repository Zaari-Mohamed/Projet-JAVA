package com.location.voitures.controllers;
import com.location.voitures.entities.Voiture;
import com.location.voitures.services.VoitureService;
import com.location.voitures.ui.ReservationDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import java.util.List;
public class UserDashboardController {
    @FXML private FlowPane voituresContainer;
    @FXML private Button refreshButton;
    private VoitureService voitureService;
    public void initialize() {
        System.out.println("UserDashboardController initialize() appelé");
        voitureService = new VoitureService();
        if (refreshButton != null) {
            refreshButton.setOnAction(event -> handleRefresh());
        }
        loadVoitures();
    }
    private void loadVoitures() {
        System.out.println("Chargement des voitures...");
        List<Voiture> voitures = voitureService.findAll();
        System.out.println("Nombre de voitures trouvées: " + voitures.size());
        voituresContainer.getChildren().clear();
        if (voitures.isEmpty()) {
            Label noVoitures = new Label("Aucune voiture disponible");
            noVoitures.setStyle("-fx-font-size: 18px;");
            voituresContainer.getChildren().add(noVoitures);
            return;
        }
        for (Voiture voiture : voitures) {
            System.out.println("Création carte pour: " + voiture.getMarque() + " " + voiture.getModele());
            VBox card = createVoitureCard(voiture);
            voituresContainer.getChildren().add(card);
        }
    }
    private VBox createVoitureCard(Voiture voiture) {
        VBox card = new VBox(15.0);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(280.0);
        card.setMinWidth(200.0);
        card.setPrefWidth(250.0);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 5);");
        try {
            String imagePath = "/images/" + voiture.getMarque().toLowerCase() + "_" + voiture.getModele().toLowerCase() + ".jpg";
            System.out.println("Tentative de chargement: " + imagePath);
            java.net.URL resourceUrl = getClass().getResource(imagePath);
            if (resourceUrl != null) {
                java.nio.file.Path filePath = java.nio.file.Paths.get(resourceUrl.toURI());
                byte[] imageBytes = java.nio.file.Files.readAllBytes(filePath);
                Image image = new Image(new java.io.ByteArrayInputStream(imageBytes));
                if (!image.isError()) {
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(100.0);
                    imageView.setFitWidth(180.0);
                    imageView.setPreserveRatio(true);
                    card.getChildren().add(imageView);
                    System.out.println("Image chargée avec succès: " + imagePath);
                } else {
                    System.out.println("Erreur Image: " + image.getException());
                    throw new Exception("Image error");
                }
            } else {
                throw new Exception("Resource not found");
            }
        } catch (Exception e) {
            System.out.println("Erreur chargement image: " + e.getMessage());
            Label carIcon = new Label("🚗");
            carIcon.setStyle("-fx-font-size: 50px;");
            card.getChildren().add(carIcon);
        }
        Label nomLabel = new Label(voiture.getMarque() + " " + voiture.getModele());
        nomLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        nomLabel.setMaxWidth(Double.MAX_VALUE);
        nomLabel.setAlignment(Pos.CENTER);
        Label statutLabel = new Label(voiture.getDisponible() ? "DISPONIBLE" : "NON DISPONIBLE");
        statutLabel.setStyle(voiture.getDisponible() ? 
            "-fx-background-color: #56ab2f; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 3 10; -fx-background-radius: 15; -fx-font-size: 10px;" :
            "-fx-background-color: #ff416c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 3 10; -fx-background-radius: 15; -fx-font-size: 10px;");
        Label prixLabel = new Label(voiture.getTauxJournalier() + "€/jour");
        prixLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #667eea; -fx-font-weight: bold;");
        Button reserverBtn = new Button(voiture.getDisponible() ? "📅 Réserver" : "❌ Indisponible");
        reserverBtn.setMaxWidth(Double.MAX_VALUE);
        reserverBtn.setPrefHeight(35.0);
        reserverBtn.setDisable(!voiture.getDisponible());
        if (voiture.getDisponible()) {
            reserverBtn.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2); -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 20;");
            reserverBtn.setOnAction(e -> handleReserver(voiture));
        } else {
            reserverBtn.setStyle("-fx-background-color: #bdc3c7; -fx-text-fill: #7f8c8d; -fx-font-size: 12px; -fx-background-radius: 20;");
        }
        card.getChildren().addAll(nomLabel, statutLabel, prixLabel, reserverBtn);
        return card;
    }
    @FXML
    public void handleRefresh() {
        System.out.println("Actualisation des voitures...");
        loadVoitures();
    }
    @FXML
    private void handleReserver(Voiture voiture) {
        openReservationDialog(voiture);
    }
    private void openReservationDialog(Voiture voiture) {
        try {
            ReservationDialog dialog = new ReservationDialog(voiture);
            dialog.showAndWait();
            loadVoitures();
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ouvrir le formulaire de réservation: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void handleAdmin() {
        openAdminLogin("admin");
    }
    private void openAdminLogin(String context) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
            Parent root = loader.load();
            LoginController controller = loader.getController();
            controller.setContext(context);
            Stage stage = new Stage();
            stage.setTitle("Connexion Administrateur");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ouvrir la connexion admin.");
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
