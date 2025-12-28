package com.location.voitures.ui;
import com.location.voitures.dao.UtilisateurDAO;
import com.location.voitures.entities.Reservation;
import com.location.voitures.entities.Utilisateur;
import com.location.voitures.entities.Voiture;
import com.location.voitures.services.AuthService;
import com.location.voitures.services.ReservationService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
public class ReservationDialog {
    private Stage dialogStage;
    private Voiture voiture;
    private ReservationService reservationService;
    private AuthService authService;
    private DatePicker dateDebutPicker;
    private DatePicker dateFinPicker;
    private TextField nomField;
    private TextField prenomField;
    private TextField emailField;
    private TextField telephoneField;
    private Label prixTotalLabel;
    public ReservationDialog(Voiture voiture) {
        this.voiture = voiture;
        this.reservationService = new ReservationService();
        this.authService = new AuthService(new UtilisateurDAO());
        createDialog();
    }
    private void createDialog() {
        dialogStage = new Stage();
        dialogStage.setTitle("Réservation - " + voiture.getMarque() + " " + voiture.getModele());
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f8f9fa;");
        Label titleLabel = new Label("📅 Réserver " + voiture.getMarque() + " " + voiture.getModele());
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        Label clientLabel = new Label("Informations client:");
        clientLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
        form.add(clientLabel, 0, 0, 2, 1);
        form.add(new Label("Nom:"), 0, 1);
        nomField = new TextField();
        nomField.setPrefWidth(200);
        form.add(nomField, 1, 1);
        form.add(new Label("Prénom:"), 0, 2);
        prenomField = new TextField();
        prenomField.setPrefWidth(200);
        form.add(prenomField, 1, 2);
        form.add(new Label("Email:"), 0, 3);
        emailField = new TextField();
        emailField.setPrefWidth(200);
        form.add(emailField, 1, 3);
        form.add(new Label("Téléphone:"), 0, 4);
        telephoneField = new TextField();
        telephoneField.setPrefWidth(200);
        form.add(telephoneField, 1, 4);
        Label datesLabel = new Label("Période de location:");
        datesLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
        form.add(datesLabel, 0, 5, 2, 1);
        form.add(new Label("Date de début:"), 0, 6);
        dateDebutPicker = new DatePicker(LocalDate.now());
        dateDebutPicker.setPrefWidth(200);
        dateDebutPicker.setOnAction(e -> calculatePrice());
        form.add(dateDebutPicker, 1, 6);
        form.add(new Label("Date de fin:"), 0, 7);
        dateFinPicker = new DatePicker(LocalDate.now().plusDays(1));
        dateFinPicker.setPrefWidth(200);
        dateFinPicker.setOnAction(e -> calculatePrice());
        form.add(dateFinPicker, 1, 7);
        prixTotalLabel = new Label("Prix total: " + voiture.getTauxJournalier() + "€");
        prixTotalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");
        form.add(prixTotalLabel, 0, 8, 2, 1);
        calculatePrice();
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        Button reserverBtn = new Button("✅ Confirmer la réservation");
        reserverBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 25; -fx-background-radius: 5;");
        reserverBtn.setOnAction(e -> handleReservation());
        Button annulerBtn = new Button("❌ Annuler");
        annulerBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 25; -fx-background-radius: 5;");
        annulerBtn.setOnAction(e -> dialogStage.close());
        buttonBox.getChildren().addAll(reserverBtn, annulerBtn);
        root.getChildren().addAll(titleLabel, form, buttonBox);
        Scene scene = new Scene(root, 500, 550);
        dialogStage.setScene(scene);
    }
    private void calculatePrice() {
        if (dateDebutPicker.getValue() != null && dateFinPicker.getValue() != null) {
            LocalDate debut = dateDebutPicker.getValue();
            LocalDate fin = dateFinPicker.getValue();
            if (fin.isAfter(debut) || fin.isEqual(debut)) {
                long days = ChronoUnit.DAYS.between(debut, fin);
                if (days == 0) days = 1; 
                double total = days * voiture.getTauxJournalier();
                prixTotalLabel.setText(String.format("Prix total: %.2f€ (%d jour(s))", total, days));
            } else {
                prixTotalLabel.setText("Date de fin invalide");
            }
        }
    }
    private void handleReservation() {
        if (nomField.getText().trim().isEmpty() || 
            prenomField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir au moins le nom, prénom et email.");
            return;
        }
        if (dateDebutPicker.getValue() == null || dateFinPicker.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner les dates de réservation.");
            return;
        }
        if (!dateFinPicker.getValue().isAfter(dateDebutPicker.getValue())) {
            showAlert("Erreur", "La date de fin doit être après la date de début.");
            return;
        }
        try {
            LocalDateTime dateDebut = dateDebutPicker.getValue().atStartOfDay();
            LocalDateTime dateFin = dateFinPicker.getValue().atTime(23, 59);
            long days = ChronoUnit.DAYS.between(dateDebutPicker.getValue(), dateFinPicker.getValue());
            if (days == 0) days = 1;
            double coutTotal = days * voiture.getTauxJournalier();
            Utilisateur utilisateur;
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
            Optional<Utilisateur> existingUserOpt = utilisateurDAO.findByEmail(emailField.getText().trim());
            if (existingUserOpt.isPresent()) {
                utilisateur = existingUserOpt.get();
                System.out.println("Utilisateur existant trouvé: " + utilisateur.getEmail());
                utilisateur.setNom(nomField.getText().trim());
                utilisateur.setPrenom(prenomField.getText().trim());
                utilisateur.setTelephone(telephoneField.getText().trim());
                utilisateurDAO.update(utilisateur);
                System.out.println("Utilisateur existant mis à jour");
            } else {
                System.out.println("Création d'un nouvel utilisateur: " + emailField.getText().trim());
                utilisateur = new Utilisateur();
                utilisateur.setNom(nomField.getText().trim());
                utilisateur.setPrenom(prenomField.getText().trim());
                utilisateur.setEmail(emailField.getText().trim());
                utilisateur.setTelephone(telephoneField.getText().trim());
                authService.registerUser(utilisateur, "temp123");
                System.out.println("Nouvel utilisateur enregistré avec ID: " + utilisateur.getId());
            }
            Reservation reservation = new Reservation(
                utilisateur,
                voiture,
                dateDebut,
                dateFin,
                coutTotal
            );
            Optional<Reservation> created = reservationService.creerReservation(reservation);
            if (created.isPresent()) {
                System.out.println("Réservation créée avec succès - ID: " + created.get().getId() + ", Statut: " + created.get().getStatut());
                showAlert("Succès", "Réservation créée avec succès !\n" +
                    "Montant: " + String.format("%.2f", coutTotal) + "€\n" +
                    "Statut: EN_ATTENTE");
                dialogStage.close();
            } else {
                System.out.println("Échec création réservation - voiture non disponible");
                showAlert("Erreur", "La voiture n'est pas disponible pour ces dates.");
            }
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la création de la réservation: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Succès") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void showAndWait() {
        dialogStage.showAndWait();
    }
}
