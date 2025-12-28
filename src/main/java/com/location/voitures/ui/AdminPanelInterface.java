package com.location.voitures.ui;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
public class AdminPanelInterface extends Application {
    private Stage primaryStage;
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Panel Administration - Location de Voitures");
        BorderPane root = new BorderPane();
        HBox header = createAdminHeader();
        root.setTop(header);
        ScrollPane scrollPane = new ScrollPane();
        VBox voituresContainer = createAdminVoituresContainer();
        scrollPane.setContent(voituresContainer);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);
        HBox footer = createFooter();
        root.setBottom(footer);
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add("data:text/css," + 
            ".root { -fx-font-family: 'Segoe UI', Arial, sans-serif; }" +
            ".scroll-pane { -fx-background: #ecf0f1; -fx-background-color: #ecf0f1; }"
        );
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private HBox createAdminHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: linear-gradient(to right, #e74c3c, #c0392b); -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");
        Label title = new Label("⚙️ Administration - Gestion des Voitures");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button logoutBtn = new Button("🚪 Déconnexion");
        logoutBtn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 20;"
        );
        logoutBtn.setOnAction(e -> primaryStage.close());
        header.getChildren().addAll(title, spacer, logoutBtn);
        return header;
    }
    private VBox createAdminVoituresContainer() {
        VBox container = new VBox(30);
        container.setPadding(new Insets(30));
        container.setStyle("-fx-background-color: #ecf0f1;");
        HBox row1 = new HBox(20);
        row1.setAlignment(Pos.CENTER);
        row1.getChildren().addAll(
            createAdminVoitureComponent("Renault Clio", "DISPONIBLE", 35.00, "Rouge", "AB-123-CD"),
            createAdminVoitureComponent("Peugeot 308", "DISPONIBLE", 45.00, "Bleu", "EF-456-GH")
        );
        HBox row2 = new HBox(20);
        row2.setAlignment(Pos.CENTER);
        row2.getChildren().addAll(
            createAdminVoitureComponent("BMW Série 3", "NON DISPONIBLE", 80.00, "Noir", "MN-012-OP"),
            createAdminVoitureComponent("Audi A4", "DISPONIBLE", 75.00, "Gris", "QR-345-ST")
        );
        HBox row3 = new HBox(20);
        row3.setAlignment(Pos.CENTER);
        row3.getChildren().addAll(
            createAdminVoitureComponent("Mercedes Classe A", "DISPONIBLE", 85.00, "Blanc", "UV-678-WX"),
            createAdminVoitureComponent("Citroën C3", "DISPONIBLE", 30.00, "Blanc", "IJ-789-KL")
        );
        container.getChildren().addAll(row1, row2, row3);
        return container;
    }
    private VBox createAdminVoitureComponent(String modele, String etat, double prix, String couleur, String immatriculation) {
        VBox voitureBox = new VBox(15);
        voitureBox.setPadding(new Insets(20));
        voitureBox.setAlignment(Pos.CENTER);
        voitureBox.setPrefWidth(350);
        voitureBox.setStyle(
            "-fx-background-color: white; -fx-background-radius: 15; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 5);"
        );
        StackPane imageContainer = new StackPane();
        imageContainer.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10;");
        imageContainer.setPrefSize(300, 180);
        Label carIcon = new Label("🚗");
        carIcon.setStyle("-fx-font-size: 60px;");
        imageContainer.getChildren().add(carIcon);
        VBox infoBox = new VBox(8);
        infoBox.setAlignment(Pos.CENTER);
        Label modeleLabel = new Label(modele);
        modeleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        Label detailsLabel = new Label(couleur + " • " + immatriculation);
        detailsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
        Label etatLabel = new Label(etat);
        if (etat.equals("DISPONIBLE")) {
            etatLabel.setStyle(
                "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-padding: 5 15; -fx-background-radius: 20; -fx-font-size: 12px;"
            );
        } else {
            etatLabel.setStyle(
                "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-padding: 5 15; -fx-background-radius: 20; -fx-font-size: 12px;"
            );
        }
        Label prixLabel = new Label(prix + "€/jour");
        prixLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        HBox actionButtons = new HBox(8);
        actionButtons.setAlignment(Pos.CENTER);
        Button updateBtn = new Button("✏️");
        updateBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-background-radius: 15; -fx-padding: 8;");
        updateBtn.setOnAction(e -> updateVoiture(modele));
        Button deleteBtn = new Button("🗑️");
        deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 15; -fx-padding: 8;");
        deleteBtn.setOnAction(e -> deleteVoiture(modele));
        Button historyBtn = new Button("📊");
        historyBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 15; -fx-padding: 8;");
        historyBtn.setOnAction(e -> showHistory(modele));
        Button statsBtn = new Button("📈");
        statsBtn.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-background-radius: 15; -fx-padding: 8;");
        statsBtn.setOnAction(e -> showStats(modele));
        actionButtons.getChildren().addAll(updateBtn, deleteBtn, historyBtn, statsBtn);
        infoBox.getChildren().addAll(modeleLabel, detailsLabel, etatLabel, prixLabel);
        voitureBox.getChildren().addAll(imageContainer, infoBox, actionButtons);
        return voitureBox;
    }
    private HBox createFooter() {
        HBox footer = new HBox();
        footer.setPadding(new Insets(20));
        footer.setAlignment(Pos.CENTER);
        footer.setStyle("-fx-background-color: #34495e;");
        Button addVoitureBtn = new Button("➕ Ajouter une Voiture");
        addVoitureBtn.setPrefWidth(250);
        addVoitureBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #27ae60, #2ecc71); " +
            "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 25; " +
            "-fx-padding: 15 30; -fx-font-size: 16px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );
        addVoitureBtn.setOnAction(e -> openAddVoitureForm());
        footer.getChildren().add(addVoitureBtn);
        return footer;
    }
    private void updateVoiture(String modele) {
        showAlert("Modifier", "Modification de: " + modele);
    }
    private void deleteVoiture(String modele) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Supprimer");
        confirm.setContentText("Êtes-vous sûr de vouloir supprimer: " + modele + " ?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                showAlert("Supprimé", modele + " a été supprimé.");
            }
        });
    }
    private void showHistory(String modele) {
        showAlert("Historique", "Historique des réservations pour: " + modele);
    }
    private void showStats(String modele) {
        showAlert("Statistiques", "Statistiques pour: " + modele);
    }
    private void openAddVoitureForm() {
        AddVoitureDialog addDialog = new AddVoitureDialog();
        addDialog.show();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
