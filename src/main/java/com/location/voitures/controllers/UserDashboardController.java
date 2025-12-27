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
import javafx.stage.Stage;

import java.util.List;

public class UserDashboardController {

    @FXML private VBox voituresContainer;

    private VoitureService voitureService;

    public void initialize() {
        System.out.println("UserDashboardController initialize() appelé");
        voitureService = new VoitureService();
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
        
        HBox currentRow = null;
        for (int i = 0; i < voitures.size(); i++) {
            System.out.println("Création carte pour: " + voitures.get(i).getMarque() + " " + voitures.get(i).getModele());
            if (i % 2 == 0) {
                currentRow = new HBox(20.0);
                currentRow.setAlignment(Pos.CENTER);
                voituresContainer.getChildren().add(currentRow);
            }
            currentRow.getChildren().add(createVoitureCard(voitures.get(i)));
        }
    }

    private VBox createVoitureCard(Voiture voiture) {
        VBox card = new VBox(15.0);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(280.0);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 5);");
        
        // Image
        try {
            String imagePath = "/images/" + voiture.getMarque().toLowerCase() + "_" + voiture.getModele().toLowerCase() + ".jpg";
            System.out.println("Tentative de chargement: " + imagePath);
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            
            if (!image.isError()) {
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(120.0);
                imageView.setFitWidth(200.0);
                imageView.setPreserveRatio(true);
                card.getChildren().add(imageView);
                System.out.println("Image chargée avec succès: " + imagePath);
            } else {
                throw new Exception("Image error");
            }
        } catch (Exception e) {
            System.out.println("Erreur chargement image: " + e.getMessage());
            Label carIcon = new Label("🚗");
            carIcon.setStyle("-fx-font-size: 60px;");
            card.getChildren().add(carIcon);
        }
        
        // Informations
        Label nomLabel = new Label(voiture.getMarque() + " " + voiture.getModele());
        nomLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Label statutLabel = new Label(voiture.getDisponible() ? "DISPONIBLE" : "NON DISPONIBLE");
        statutLabel.setStyle(voiture.getDisponible() ? 
            "-fx-background-color: #56ab2f; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 15; -fx-background-radius: 20; -fx-font-size: 12px;" :
            "-fx-background-color: #ff416c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 15; -fx-background-radius: 20; -fx-font-size: 12px;");
        
        Label prixLabel = new Label(voiture.getTauxJournalier() + "€/jour");
        prixLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #667eea; -fx-font-weight: bold;");
        
        Button reserverBtn = new Button(voiture.getDisponible() ? "📅 Réserver" : "❌ Indisponible");
        reserverBtn.setPrefWidth(200.0);
        reserverBtn.setDisable(!voiture.getDisponible());
        
        if (voiture.getDisponible()) {
            reserverBtn.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2); -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 12 20;");
            reserverBtn.setOnAction(e -> handleReserver(voiture));
        } else {
            reserverBtn.setStyle("-fx-background-color: #bdc3c7; -fx-text-fill: #7f8c8d; -fx-font-size: 14px; -fx-background-radius: 25; -fx-padding: 12 20;");
        }
        
        card.getChildren().addAll(nomLabel, statutLabel, prixLabel, reserverBtn);
        return card;
    }

    @FXML
    private void handleRefresh() {
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
            // Refresh the list after reservation
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