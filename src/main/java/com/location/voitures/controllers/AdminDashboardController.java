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
import java.util.Optional;

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
            
            System.out.println("AdminDashboard initialisé avec succès");
        } catch (Exception e) {
            System.out.println("Erreur initialisation AdminDashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void createInitialContent() {
        // Créer le contenu initial dynamiquement
        if (gestionVoituresContent == null) {
            gestionVoituresContent = createGestionVoituresContent();
        }
        showContent(gestionVoituresContent);
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
        selectImage(imagePathField);
    }

    private void selectImage(TextField imagePathFieldLocal) {
        System.out.println("selectImage() appelé");
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir une image");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            
            // Trouver la fenêtre actuelle
            Stage stage = null;
            if (imagePathFieldLocal.getScene() != null) {
                stage = (Stage) imagePathFieldLocal.getScene().getWindow();
            } else {
                // Fallback: chercher une fenêtre active
                stage = new Stage();
            }
            
            File selectedFile = fileChooser.showOpenDialog(stage);
            
            if (selectedFile != null) {
                selectedImagePath = selectedFile.getAbsolutePath();
                imagePathFieldLocal.setText(selectedFile.getName());
                System.out.println("Image sélectionnée: " + selectedImagePath);
            }
        } catch (Exception e) {
            System.out.println("Erreur selectImage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void ajouterVoiture() {
        ajouterVoiture(marqueField, modeleField, anneeField, immatriculationField, prixField, imagePathField);
    }

    private void ajouterVoiture(TextField marqueFieldLocal, TextField modeleFieldLocal, TextField anneeFieldLocal,
                               TextField immatFieldLocal, TextField prixFieldLocal, TextField imagePathFieldLocal) {
        System.out.println("ajouterVoiture() appelé");
        try {
            if (marqueFieldLocal.getText().isEmpty() || modeleFieldLocal.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir au moins la marque et le modèle.");
                return;
            }
            
            String imageName = copyImageToResources(marqueFieldLocal.getText(), modeleFieldLocal.getText());
            
            Voiture voiture = new Voiture(
                marqueFieldLocal.getText(),
                modeleFieldLocal.getText(),
                Integer.parseInt(anneeFieldLocal.getText().isEmpty() ? "2020" : anneeFieldLocal.getText()),
                immatFieldLocal.getText().isEmpty() ? "XX-000-XX" : immatFieldLocal.getText(),
                Double.parseDouble(prixFieldLocal.getText().isEmpty() ? "50.0" : prixFieldLocal.getText()),
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
        modifierVoiture(marqueField, modeleField, anneeField, immatriculationField, prixField, voituresTable);
    }

    private void modifierVoiture(TextField marqueFieldLocal, TextField modeleFieldLocal, TextField anneeFieldLocal,
                                TextField immatFieldLocal, TextField prixFieldLocal, TableView<Voiture> voituresTableLocal) {
        Voiture selected = voituresTableLocal.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner une voiture à modifier.");
            return;
        }
        
        try {
            selected.setMarque(marqueFieldLocal.getText());
            selected.setModele(modeleFieldLocal.getText());
            selected.setAnnee(Integer.parseInt(anneeFieldLocal.getText()));
            selected.setImmatriculation(immatFieldLocal.getText());
            selected.setTauxJournalier(Double.parseDouble(prixFieldLocal.getText()));
            
            if (selectedImagePath != null) {
                copyImageToResources(marqueFieldLocal.getText(), modeleFieldLocal.getText());
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
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Supprimer une voiture");
        dialog.setHeaderText("Entrez l'immatriculation de la voiture à supprimer");
        dialog.setContentText("Immatriculation:");
        
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            String immatriculation = result.get().trim();
            
            Optional<Voiture> voitureOpt = voitureService.findByImmatriculation(immatriculation);
            if (voitureOpt.isPresent()) {
                Voiture voiture = voitureOpt.get();
                
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirmation");
                confirm.setContentText("Êtes-vous sûr de vouloir supprimer la voiture " + voiture.getMarque() + " " + voiture.getModele() + " (immatriculation: " + immatriculation + ") ?");
                
                if (confirm.showAndWait().get() == ButtonType.OK) {
                    voitureService.deleteVoiture(voiture.getId());
                    loadVoitures();
                    clearFields();
                    showAlert("Succès", "Voiture supprimée avec succès !");
                }
            } else {
                showAlert("Erreur", "Aucune voiture trouvée avec l'immatriculation: " + immatriculation);
            }
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
        // Créer le contenu voitures dynamiquement à chaque fois pour éviter les problèmes de références @FXML
        gestionVoituresContent = createGestionVoituresContent();
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

    private String copyImageToResources(String marque, String modele) throws IOException {
        if (selectedImagePath == null) return null;
        
        File sourceFile = new File(selectedImagePath);
        String fileName = marque.toLowerCase() + "_" + modele.toLowerCase() + ".jpg";
        
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

    private VBox createGestionVoituresContent() {
        System.out.println("Création du contenu de gestion des voitures");
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // Formulaire d'ajout de voiture
        VBox formBox = new VBox(15);
        Label formTitle = new Label("Ajouter une voiture");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox row1 = new HBox(15);
        VBox col1 = new VBox(5);
        Label marqueLabel = new Label("Marque :");
        TextField marqueFieldLocal = new TextField();
        marqueFieldLocal.setPrefWidth(150);
        col1.getChildren().addAll(marqueLabel, marqueFieldLocal);

        VBox col2 = new VBox(5);
        Label modeleLabel = new Label("Modèle :");
        TextField modeleFieldLocal = new TextField();
        modeleFieldLocal.setPrefWidth(150);
        col2.getChildren().addAll(modeleLabel, modeleFieldLocal);

        VBox col3 = new VBox(5);
        Label anneeLabel = new Label("Année :");
        TextField anneeFieldLocal = new TextField();
        anneeFieldLocal.setPrefWidth(100);
        col3.getChildren().addAll(anneeLabel, anneeFieldLocal);

        row1.getChildren().addAll(col1, col2, col3);

        HBox row2 = new HBox(15);
        VBox col4 = new VBox(5);
        Label immatLabel = new Label("Immatriculation :");
        TextField immatFieldLocal = new TextField();
        immatFieldLocal.setPrefWidth(150);
        col4.getChildren().addAll(immatLabel, immatFieldLocal);

        VBox col5 = new VBox(5);
        Label prixLabel = new Label("Prix/jour (€) :");
        TextField prixFieldLocal = new TextField();
        prixFieldLocal.setPrefWidth(100);
        col5.getChildren().addAll(prixLabel, prixFieldLocal);

        VBox col6 = new VBox(5);
        Label imageLabel = new Label("Image :");
        HBox imageBox = new HBox(10);
        TextField imagePathFieldLocal = new TextField();
        imagePathFieldLocal.setPrefWidth(200);
        imagePathFieldLocal.setEditable(false);
        Button selectImageBtnLocal = new Button("Choisir...");
        imageBox.getChildren().addAll(imagePathFieldLocal, selectImageBtnLocal);
        col6.getChildren().addAll(imageLabel, imageBox);

        row2.getChildren().addAll(col4, col5, col6);

        HBox buttonRow = new HBox(10);
        Button ajouterBtnLocal = new Button("Ajouter");
        ajouterBtnLocal.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 10 20;");
        Button modifierBtnLocal = new Button("Modifier");
        modifierBtnLocal.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-padding: 10 20;");
        Button supprimerBtnLocal = new Button("Supprimer");
        supprimerBtnLocal.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10 20;");
        buttonRow.getChildren().addAll(ajouterBtnLocal, modifierBtnLocal, supprimerBtnLocal);

        formBox.getChildren().addAll(formTitle, row1, row2, buttonRow);

        // Tableau des voitures
        TableView<Voiture> voituresTableLocal = new TableView<>();
        voituresTableLocal.setPrefHeight(400);

        TableColumn<Voiture, Long> idColLocal = new TableColumn<>("ID");
        idColLocal.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColLocal.setPrefWidth(50);

        TableColumn<Voiture, String> marqueColLocal = new TableColumn<>("Marque");
        marqueColLocal.setCellValueFactory(new PropertyValueFactory<>("marque"));
        marqueColLocal.setPrefWidth(100);

        TableColumn<Voiture, String> modeleColLocal = new TableColumn<>("Modèle");
        modeleColLocal.setCellValueFactory(new PropertyValueFactory<>("modele"));
        modeleColLocal.setPrefWidth(100);

        TableColumn<Voiture, Integer> anneeColLocal = new TableColumn<>("Année");
        anneeColLocal.setCellValueFactory(new PropertyValueFactory<>("annee"));
        anneeColLocal.setPrefWidth(80);

        TableColumn<Voiture, String> immatriculationColLocal = new TableColumn<>("Immatriculation");
        immatriculationColLocal.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));
        immatriculationColLocal.setPrefWidth(120);

        TableColumn<Voiture, Double> prixColLocal = new TableColumn<>("Prix/jour");
        prixColLocal.setCellValueFactory(new PropertyValueFactory<>("tauxJournalier"));
        prixColLocal.setPrefWidth(80);

        TableColumn<Voiture, Boolean> disponibleColLocal = new TableColumn<>("Disponible");
        disponibleColLocal.setCellValueFactory(new PropertyValueFactory<>("disponible"));
        disponibleColLocal.setPrefWidth(80);

        voituresTableLocal.getColumns().addAll(idColLocal, marqueColLocal, modeleColLocal, anneeColLocal,
                                               immatriculationColLocal, prixColLocal, disponibleColLocal);

        // Charger les voitures et lier le tableau
        loadVoitures();
        voituresTableLocal.setItems(voituresList);

        // Gestionnaire de sélection
        voituresTableLocal.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Remplir les champs du formulaire avec les données de la voiture sélectionnée
                marqueFieldLocal.setText(newSelection.getMarque());
                modeleFieldLocal.setText(newSelection.getModele());
                anneeFieldLocal.setText(newSelection.getAnnee() != null ? newSelection.getAnnee().toString() : "");
                immatFieldLocal.setText(newSelection.getImmatriculation());
                prixFieldLocal.setText(newSelection.getTauxJournalier() != null ? newSelection.getTauxJournalier().toString() : "");
            }
        });

        // Event handlers pour les boutons
        ajouterBtnLocal.setOnAction(e -> {
            ajouterVoiture(marqueFieldLocal, modeleFieldLocal, anneeFieldLocal, immatFieldLocal, prixFieldLocal, imagePathFieldLocal);
            loadVoitures();
            voituresTableLocal.refresh();
        });

        modifierBtnLocal.setOnAction(e -> {
            modifierVoiture(marqueFieldLocal, modeleFieldLocal, anneeFieldLocal, immatFieldLocal, prixFieldLocal, voituresTableLocal);
            loadVoitures();
            voituresTableLocal.refresh();
        });

        supprimerBtnLocal.setOnAction(e -> supprimerVoiture());

        selectImageBtnLocal.setOnAction(e -> {
            selectImage(imagePathFieldLocal);
        });

        content.getChildren().addAll(formBox, voituresTableLocal);
        System.out.println("Contenu de gestion des voitures créé avec " + voituresList.size() + " voitures");
        return content;
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