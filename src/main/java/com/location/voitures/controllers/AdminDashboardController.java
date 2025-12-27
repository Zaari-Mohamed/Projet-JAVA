package com.location.voitures.controllers;

import com.location.voitures.dao.ReservationDAO;
import com.location.voitures.dao.UtilisateurDAO;
import com.location.voitures.entities.Reservation;
import com.location.voitures.entities.Utilisateur;
import com.location.voitures.entities.Voiture;
import com.location.voitures.services.ReservationService;
import com.location.voitures.services.VoitureService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminDashboardController {

    @FXML private TextField marqueField;
    @FXML private TextField modeleField;
    @FXML private TextField anneeField;
    @FXML private TextField immatriculationField;
    @FXML private TextField prixField;
    @FXML private TextField imagePathField;
    @FXML private TableView<Voiture> voituresTable;
    @FXML private TableColumn<Voiture, Long> idCol;
    @FXML private TableColumn<Voiture, String> marqueCol;
    @FXML private TableColumn<Voiture, String> modeleCol;
    @FXML private TableColumn<Voiture, Integer> anneeCol;
    @FXML private TableColumn<Voiture, String> immatriculationCol;
    @FXML private TableColumn<Voiture, Double> prixCol;
    @FXML private TableColumn<Voiture, Boolean> disponibleCol;
    @FXML private TableColumn<Voiture, String> imageCol;

    private VoitureService voitureService;
    private ObservableList<Voiture> voituresList;
    private String selectedImagePath;

    public void initialize() {
        System.out.println("AdminDashboardController initialize() appelé");
        try {
            voitureService = new VoitureService();
            setupTable();
            loadVoitures();
            createInitialContent();
            
            // Vérification des boutons
            System.out.println("gestionVoituresBtn: " + (gestionVoituresBtn != null));
            System.out.println("gestionUsersBtn: " + (gestionUsersBtn != null));
            System.out.println("reservationsBtn: " + (reservationsBtn != null));
            System.out.println("mainContent: " + (mainContent != null));
            
            // Ajout d'un event handler explicite pour le bouton réservations
            if (reservationsBtn != null) {
                System.out.println("reservationsBtn isVisible: " + reservationsBtn.isVisible());
                System.out.println("reservationsBtn isDisabled: " + reservationsBtn.isDisabled());
                reservationsBtn.setOnAction(event -> {
                    System.out.println("Clic manuel sur bouton réservations détecté - event handler");
                    showGestionReservations();
                });
                System.out.println("Event handler ajouté au bouton réservations");
            }
            
            // Test des autres boutons
            if (gestionVoituresBtn != null) {
                gestionVoituresBtn.setOnAction(event -> {
                    System.out.println("Clic manuel sur bouton voitures détecté");
                    showGestionVoitures();
                });
            }
            if (gestionUsersBtn != null) {
                gestionUsersBtn.setOnAction(event -> {
                    System.out.println("Clic manuel sur bouton utilisateurs détecté");
                    showGestionUsers();
                });
            }
            
            System.out.println("AdminDashboard initialisé avec succès");
        } catch (Exception e) {
            System.out.println("Erreur initialisation AdminDashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void createInitialContent() {
        if (!mainContent.getChildren().isEmpty()) {
            gestionVoituresContent = (VBox) mainContent.getChildren().get(0);
        }
        updateButtonStyles(gestionVoituresBtn);
    }

    private void setupTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        marqueCol.setCellValueFactory(new PropertyValueFactory<>("marque"));
        modeleCol.setCellValueFactory(new PropertyValueFactory<>("modele"));
        anneeCol.setCellValueFactory(new PropertyValueFactory<>("annee"));
        immatriculationCol.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));
        prixCol.setCellValueFactory(new PropertyValueFactory<>("tauxJournalier"));
        disponibleCol.setCellValueFactory(new PropertyValueFactory<>("disponible"));
        
        voituresList = FXCollections.observableArrayList();
        voituresTable.setItems(voituresList);
        
        voituresTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFields(newSelection);
            }
        });
    }

    private void loadVoitures() {
        List<Voiture> voitures = voitureService.findAll();
        System.out.println("AdminDashboard - Nombre de voitures chargées: " + voitures.size());
        voituresList.clear();
        voituresList.addAll(voitures);
    }

    @FXML
    private void selectImage() {
        System.out.println("selectImage() appelé");
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir une image");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            
            Stage stage = (Stage) imagePathField.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);
            
            if (selectedFile != null) {
                selectedImagePath = selectedFile.getAbsolutePath();
                imagePathField.setText(selectedFile.getName());
                System.out.println("Image sélectionnée: " + selectedImagePath);
            }
        } catch (Exception e) {
            System.out.println("Erreur selectImage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void ajouterVoiture() {
        System.out.println("ajouterVoiture() appelé");
        try {
            if (marqueField.getText().isEmpty() || modeleField.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir au moins la marque et le modèle.");
                return;
            }
            
            String imageName = copyImageToResources();
            
            Voiture voiture = new Voiture(
                marqueField.getText(),
                modeleField.getText(),
                Integer.parseInt(anneeField.getText().isEmpty() ? "2020" : anneeField.getText()),
                immatriculationField.getText().isEmpty() ? "XX-000-XX" : immatriculationField.getText(),
                Double.parseDouble(prixField.getText().isEmpty() ? "50.0" : prixField.getText()),
                true
            );
            
            System.out.println("Sauvegarde voiture: " + voiture.getMarque() + " " + voiture.getModele());
            voitureService.saveVoiture(voiture);
            System.out.println("Voiture sauvegardée avec succès");
            loadVoitures();
            System.out.println("Liste rechargée");
            clearFields();
            showAlert("Succès", "Voiture ajoutée avec succès !");
            
        } catch (Exception e) {
            System.out.println("Erreur ajouterVoiture: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @FXML
    private void modifierVoiture() {
        Voiture selected = voituresTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner une voiture à modifier.");
            return;
        }
        
        try {
            selected.setMarque(marqueField.getText());
            selected.setModele(modeleField.getText());
            selected.setAnnee(Integer.parseInt(anneeField.getText()));
            selected.setImmatriculation(immatriculationField.getText());
            selected.setTauxJournalier(Double.parseDouble(prixField.getText()));
            
            if (selectedImagePath != null) {
                copyImageToResources();
            }
            
            voitureService.updateVoiture(selected);
            loadVoitures();
            clearFields();
            showAlert("Succès", "Voiture modifiée avec succès !");
            
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la modification : " + e.getMessage());
        }
    }

    @FXML
    private void supprimerVoiture() {
        Voiture selected = voituresTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner une voiture à supprimer.");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setContentText("Êtes-vous sûr de vouloir supprimer cette voiture ?");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            voitureService.deleteVoiture(selected.getId());
            loadVoitures();
            clearFields();
            showAlert("Succès", "Voiture supprimée avec succès !");
        }
    }

    @FXML private VBox mainContent;
    @FXML private Button gestionVoituresBtn;
    @FXML private Button gestionUsersBtn;
    @FXML private Button reservationsBtn;
    
    private VBox gestionVoituresContent;
    private VBox gestionUsersContent;
    private VBox gestionReservationsContent;
    
    @FXML
    private void showGestionVoitures() {
        System.out.println("showGestionVoitures() appelé");
        showContent(gestionVoituresContent);
        updateButtonStyles(gestionVoituresBtn);
    }
    
    @FXML
    private void showGestionUsers() {
        System.out.println("showGestionUsers() appelé");
        if (gestionUsersContent == null) {
            gestionUsersContent = createGestionUsersContent();
        }
        showContent(gestionUsersContent);
        updateButtonStyles(gestionUsersBtn);
    }
    
    @FXML
    private void showGestionReservations() {
        System.out.println("showGestionReservations() appelé");
        try {
            if (gestionReservationsContent == null) {
                System.out.println("Création du contenu des réservations...");
                gestionReservationsContent = createGestionReservationsContent();
                System.out.println("Contenu des réservations créé");
            }
            showContent(gestionReservationsContent);
            updateButtonStyles(reservationsBtn);
            System.out.println("Affichage des réservations terminé");
        } catch (Exception e) {
            System.out.println("Erreur dans showGestionReservations: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void updateButtonStyles(Button activeButton) {
        gestionVoituresBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px;");
        gestionUsersBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px;");
        reservationsBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px;");
        activeButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
    }
    
    private void showContent(VBox content) {
        System.out.println("showContent appelé avec content: " + (content != null));
        if (mainContent != null) {
            System.out.println("mainContent children avant clear: " + mainContent.getChildren().size());
            mainContent.getChildren().clear();
            System.out.println("mainContent children après clear: " + mainContent.getChildren().size());
            mainContent.getChildren().add(content);
            System.out.println("mainContent children après add: " + mainContent.getChildren().size());
        } else {
            System.out.println("ERREUR: mainContent est null!");
        }
    }

    private String copyImageToResources() throws IOException {
        if (selectedImagePath == null) return null;
        
        File sourceFile = new File(selectedImagePath);
        String fileName = marqueField.getText().toLowerCase() + "_" + modeleField.getText().toLowerCase() + ".jpg";
        
        Path targetPath = Paths.get("src/main/resources/images/" + fileName);
        Files.createDirectories(targetPath.getParent());
        Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        
        return fileName;
    }

    private void fillFields(Voiture voiture) {
        marqueField.setText(voiture.getMarque());
        modeleField.setText(voiture.getModele());
        anneeField.setText(voiture.getAnnee().toString());
        immatriculationField.setText(voiture.getImmatriculation());
        prixField.setText(voiture.getTauxJournalier().toString());
    }

    private void clearFields() {
        marqueField.clear();
        modeleField.clear();
        anneeField.clear();
        immatriculationField.clear();
        prixField.clear();
        imagePathField.clear();
        selectedImagePath = null;
    }

    private VBox createGestionUsersContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        Label title = new Label("👥 Gestion des Utilisateurs");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        TableView<Utilisateur> usersTable = new TableView<>();
        
        TableColumn<Utilisateur, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);
        
        TableColumn<Utilisateur, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nomCol.setPrefWidth(120);
        
        TableColumn<Utilisateur, String> prenomCol = new TableColumn<>("Prénom");
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        prenomCol.setPrefWidth(120);
        
        TableColumn<Utilisateur, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(200);
        
        TableColumn<Utilisateur, String> telCol = new TableColumn<>("Téléphone");
        telCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        telCol.setPrefWidth(120);
        
        TableColumn<Utilisateur, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> {
            Utilisateur u = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(u.getClass().getSimpleName());
        });
        typeCol.setPrefWidth(100);
        
        usersTable.getColumns().addAll(idCol, nomCol, prenomCol, emailCol, telCol, typeCol);
        
        // Load users
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
        List<Utilisateur> users = utilisateurDAO.findAll();
        usersTable.setItems(FXCollections.observableArrayList(users));
        
        Button refreshBtn = new Button("🔄 Actualiser");
        refreshBtn.setOnAction(e -> {
            List<Utilisateur> updatedUsers = utilisateurDAO.findAll();
            usersTable.setItems(FXCollections.observableArrayList(updatedUsers));
        });
        
        content.getChildren().addAll(title, usersTable, refreshBtn);
        return content;
    }
    
    private VBox createGestionReservationsContent() {
        System.out.println("=== DÉBUT createGestionReservationsContent() ===");
        try {
            VBox content = new VBox(20);
            content.setPadding(new Insets(20));
            content.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
            System.out.println("Conteneur VBox créé");
            
            Label title = new Label("📋 Gestion des Réservations");
            title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            System.out.println("Titre créé: " + title.getText());
            
            TableView<Reservation> reservationsTable = new TableView<>();
            System.out.println("TableView créée");
            
            TableColumn<Reservation, Long> idCol = new TableColumn<>("ID");
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            idCol.setPrefWidth(50);
            System.out.println("Colonne ID créée");
            
            TableColumn<Reservation, String> voitureCol = new TableColumn<>("Voiture");
            voitureCol.setCellValueFactory(cellData -> {
                Reservation r = cellData.getValue();
                Voiture v = r.getVoiture();
                String voitureInfo = (v != null) ? v.getMarque() + " " + v.getModele() : "Voiture inconnue";
                return new javafx.beans.property.SimpleStringProperty(voitureInfo);
            });
            voitureCol.setPrefWidth(150);
            System.out.println("Colonne Voiture créée");
            
            TableColumn<Reservation, String> clientCol = new TableColumn<>("Client");
            clientCol.setCellValueFactory(cellData -> {
                Reservation r = cellData.getValue();
                Utilisateur u = r.getUtilisateur();
                String clientInfo = (u != null) ? u.getNom() + " " + u.getPrenom() : "Client inconnu";
                return new javafx.beans.property.SimpleStringProperty(clientInfo);
            });
            clientCol.setPrefWidth(150);
            System.out.println("Colonne Client créée");
            
            TableColumn<Reservation, String> dateDebutCol = new TableColumn<>("Date début");
            dateDebutCol.setCellValueFactory(cellData -> {
                Reservation r = cellData.getValue();
                String dateInfo = (r.getDateDebut() != null) ? 
                    r.getDateDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "Date inconnue";
                return new javafx.beans.property.SimpleStringProperty(dateInfo);
            });
            dateDebutCol.setPrefWidth(130);
            System.out.println("Colonne Date début créée");
            
            TableColumn<Reservation, String> dateFinCol = new TableColumn<>("Date fin");
            dateFinCol.setCellValueFactory(cellData -> {
                Reservation r = cellData.getValue();
                String dateInfo = (r.getDateFin() != null) ? 
                    r.getDateFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "Date inconnue";
                return new javafx.beans.property.SimpleStringProperty(dateInfo);
            });
            dateFinCol.setPrefWidth(130);
            System.out.println("Colonne Date fin créée");
            
            TableColumn<Reservation, Double> montantCol = new TableColumn<>("Montant (€)");
            montantCol.setCellValueFactory(new PropertyValueFactory<>("coutTotal"));
            montantCol.setPrefWidth(100);
            System.out.println("Colonne Montant créée");
            
            TableColumn<Reservation, String> statutCol = new TableColumn<>("Statut");
            statutCol.setCellValueFactory(cellData -> {
                Reservation r = cellData.getValue();
                String statutInfo = (r.getStatut() != null) ? r.getStatut().toString() : "Statut inconnu";
                return new javafx.beans.property.SimpleStringProperty(statutInfo);
            });
            statutCol.setPrefWidth(120);
            System.out.println("Colonne Statut créée");
            
            reservationsTable.getColumns().addAll(idCol, voitureCol, clientCol, dateDebutCol, dateFinCol, montantCol, statutCol);
            System.out.println("Toutes les colonnes ajoutées à la table");
            
            // Load reservations
            ReservationDAO reservationDAO = new ReservationDAO();
            List<Reservation> reservations = reservationDAO.findAll();
            System.out.println("AdminDashboard - Nombre de réservations chargées: " + reservations.size());
            for (Reservation r : reservations) {
                Voiture v = r.getVoiture();
                String voitureInfo = (v != null) ? v.getMarque() + " " + v.getModele() : "Voiture null";
                String statutInfo = (r.getStatut() != null) ? r.getStatut().toString() : "Statut null";
                System.out.println("Réservation ID: " + r.getId() + ", Voiture: " + voitureInfo + ", Statut: " + statutInfo);
            }
            reservationsTable.setItems(FXCollections.observableArrayList(reservations));
            System.out.println("Données chargées dans la table");
            
            // Buttons for status management
            HBox buttonBox = new HBox(10);
            Button refreshBtn = new Button("🔄 Actualiser");
            refreshBtn.setOnAction(e -> {
                List<Reservation> updatedReservations = reservationDAO.findAll();
                reservationsTable.setItems(FXCollections.observableArrayList(updatedReservations));
                System.out.println("Table actualisée");
            });
            System.out.println("Bouton Actualiser créé");
            
            Button confirmerBtn = new Button("✅ Confirmer");
            confirmerBtn.setOnAction(e -> {
                Reservation selected = reservationsTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    System.out.println("Confirmation de la réservation ID: " + selected.getId());
                    selected.setStatut(Reservation.StatutReservation.CONFIRMEE);
                    reservationDAO.update(selected);
                    
                    // Update car availability
                    Voiture voiture = selected.getVoiture();
                    if (voiture != null) {
                        voiture.setDisponible(false);
                        voitureService.updateVoiture(voiture);
                        System.out.println("Voiture marquée comme indisponible");
                    }
                    
                    refreshBtn.fire();
                    showAlert("Succès", "Réservation confirmée et voiture marquée comme indisponible !");
                } else {
                    System.out.println("Aucune réservation sélectionnée pour confirmation");
                }
            });
            System.out.println("Bouton Confirmer créé");
            
            Button annulerBtn = new Button("❌ Annuler");
            annulerBtn.setOnAction(e -> {
                Reservation selected = reservationsTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    System.out.println("Annulation de la réservation ID: " + selected.getId());
                    // Make car available again if it was confirmed
                    if (selected.getStatut() == Reservation.StatutReservation.CONFIRMEE) {
                        Voiture voiture = selected.getVoiture();
                        if (voiture != null) {
                            voiture.setDisponible(true);
                            voitureService.updateVoiture(voiture);
                            System.out.println("Voiture remise disponible");
                        }
                    }
                    
                    selected.setStatut(Reservation.StatutReservation.ANNULEE);
                    reservationDAO.update(selected);
                    
                    refreshBtn.fire();
                    showAlert("Succès", "Réservation annulée !");
                } else {
                    System.out.println("Aucune réservation sélectionnée pour annulation");
                }
            });
            System.out.println("Bouton Annuler créé");
            
            buttonBox.getChildren().addAll(refreshBtn, confirmerBtn, annulerBtn);
            System.out.println("Boîte de boutons créée");
            
            content.getChildren().addAll(title, reservationsTable, buttonBox);
            System.out.println("Contenu des réservations créé avec succès");
            return content;
            
        } catch (Exception e) {
            System.out.println("ERREUR dans createGestionReservationsContent: " + e.getMessage());
            e.printStackTrace();
            // Return a simple error content
            VBox errorContent = new VBox(20);
            errorContent.setPadding(new Insets(20));
            errorContent.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
            Label errorLabel = new Label("❌ Erreur lors du chargement des réservations: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red;");
            errorContent.getChildren().add(errorLabel);
            return errorContent;
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Succès") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}