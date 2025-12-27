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

public class UserInterface extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Location de Voitures");

        // Layout principal
        BorderPane root = new BorderPane();
        
        // Header avec bouton Admin
        HBox header = createHeader();
        root.setTop(header);
        
        // Zone principale avec les voitures
        ScrollPane scrollPane = new ScrollPane();
        VBox voituresContainer = createVoituresContainer();
        scrollPane.setContent(voituresContainer);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add("data:text/css," + 
            ".root { -fx-font-family: 'Segoe UI', Arial, sans-serif; }" +
            ".scroll-pane { -fx-background: #f8f9fa; -fx-background-color: #f8f9fa; }" +
            ".scroll-pane .viewport { -fx-background-color: #f8f9fa; }" +
            ".scroll-pane .content { -fx-background-color: #f8f9fa; }"
        );
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER_RIGHT);
        header.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2); -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");

        Label title = new Label("🚗 Location de Voitures");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold; -fx-font-family: 'Segoe UI';");
        
        Button adminBtn = new Button("⚙️ Admin");
        adminBtn.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ff6b6b, #ee5a52); " +
            "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; " +
            "-fx-background-radius: 25; -fx-padding: 10 20; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);"
        );
        adminBtn.setOnMouseEntered(e -> adminBtn.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ff5252, #d32f2f); " +
            "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; " +
            "-fx-background-radius: 25; -fx-padding: 10 20; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0, 0, 3); -fx-scale-x: 1.05; -fx-scale-y: 1.05;"
        ));
        adminBtn.setOnMouseExited(e -> adminBtn.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ff6b6b, #ee5a52); " +
            "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; " +
            "-fx-background-radius: 25; -fx-padding: 10 20; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2); -fx-scale-x: 1.0; -fx-scale-y: 1.0;"
        ));
        adminBtn.setOnAction(e -> openAdminInterface());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(title, spacer, adminBtn);
        return header;
    }

    private VBox createVoituresContainer() {
        VBox container = new VBox(30);
        container.setPadding(new Insets(30));
        container.setStyle("-fx-background-color: #f8f9fa;");
        
        // Créer une grille de voitures (2 par ligne)
        HBox row1 = new HBox(20);
        row1.setAlignment(Pos.CENTER);
        row1.getChildren().addAll(
            createVoitureComponent("Renault Clio", "DISPONIBLE", 35.00, "images/clio.jpg"),
            createVoitureComponent("Peugeot 308", "DISPONIBLE", 45.00, "images/308.jpg")
        );
        
        HBox row2 = new HBox(20);
        row2.setAlignment(Pos.CENTER);
        row2.getChildren().addAll(
            createVoitureComponent("BMW Série 3", "NON DISPONIBLE", 80.00, "images/bmw.jpg"),
            createVoitureComponent("Audi A4", "DISPONIBLE", 75.00, "images/audi.jpg")
        );
        
        HBox row3 = new HBox(20);
        row3.setAlignment(Pos.CENTER);
        row3.getChildren().addAll(
            createVoitureComponent("Mercedes Classe A", "DISPONIBLE", 85.00, "images/mercedes.jpg"),
            createVoitureComponent("Citroën C3", "DISPONIBLE", 30.00, "images/c3.jpg")
        );

        container.getChildren().addAll(row1, row2, row3);
        return container;
    }

    private VBox createVoitureComponent(String modele, String etat, double prix, String imagePath) {
        VBox voitureBox = new VBox(15);
        voitureBox.setPadding(new Insets(20));
        voitureBox.setAlignment(Pos.CENTER);
        voitureBox.setPrefWidth(280);
        voitureBox.setMaxWidth(280);
        voitureBox.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 15; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 5);"
        );
        
        // Effet hover
        voitureBox.setOnMouseEntered(e -> voitureBox.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 15; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 20, 0, 0, 8); " +
            "-fx-scale-x: 1.02; -fx-scale-y: 1.02;"
        ));
        voitureBox.setOnMouseExited(e -> voitureBox.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 15; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 5); " +
            "-fx-scale-x: 1.0; -fx-scale-y: 1.0;"
        ));

        // Image de la voiture
        StackPane imageContainer = new StackPane();
        imageContainer.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10;");
        imageContainer.setPrefSize(240, 160);
        
        ImageView imageView = new ImageView();
        try {
            Image image = new Image(getClass().getResourceAsStream("/" + imagePath));
            imageView.setImage(image);
        } catch (Exception e) {
            // Image par défaut avec icône voiture
            Label carIcon = new Label("🚗");
            carIcon.setStyle("-fx-font-size: 60px;");
            imageContainer.getChildren().add(carIcon);
        }
        imageView.setFitWidth(220);
        imageView.setFitHeight(140);
        imageView.setPreserveRatio(true);
        if (imageView.getImage() != null) {
            imageContainer.getChildren().add(imageView);
        }

        // Informations de la voiture
        VBox infoBox = new VBox(8);
        infoBox.setAlignment(Pos.CENTER);

        Label modeleLabel = new Label(modele);
        modeleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-font-family: 'Segoe UI';");

        // Badge d'état
        Label etatLabel = new Label(etat);
        if (etat.equals("DISPONIBLE")) {
            etatLabel.setStyle(
                "-fx-background-color: linear-gradient(to right, #56ab2f, #a8e6cf); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 15; " +
                "-fx-background-radius: 20; -fx-font-size: 12px;"
            );
        } else {
            etatLabel.setStyle(
                "-fx-background-color: linear-gradient(to right, #ff416c, #ff4757); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 15; " +
                "-fx-background-radius: 20; -fx-font-size: 12px;"
            );
        }

        Label prixLabel = new Label(prix + "€/jour");
        prixLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #667eea; -fx-font-weight: bold;");

        Button reserverBtn = new Button(etat.equals("DISPONIBLE") ? "📅 Réserver" : "❌ Indisponible");
        reserverBtn.setPrefWidth(200);
        
        if (etat.equals("DISPONIBLE")) {
            reserverBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
                "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; " +
                "-fx-background-radius: 25; -fx-padding: 12 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
            );
            reserverBtn.setOnMouseEntered(e -> reserverBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #5a67d8, #6b46c1); " +
                "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; " +
                "-fx-background-radius: 25; -fx-padding: 12 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 3);"
            ));
            reserverBtn.setOnMouseExited(e -> reserverBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
                "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; " +
                "-fx-background-radius: 25; -fx-padding: 12 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
            ));
        } else {
            reserverBtn.setStyle(
                "-fx-background-color: #bdc3c7; -fx-text-fill: #7f8c8d; " +
                "-fx-font-size: 14px; -fx-background-radius: 25; -fx-padding: 12 20;"
            );
            reserverBtn.setDisable(true);
        }
        
        reserverBtn.setOnAction(e -> reserverVoiture(modele));

        infoBox.getChildren().addAll(modeleLabel, etatLabel, prixLabel);
        voitureBox.getChildren().addAll(imageContainer, infoBox, reserverBtn);
        
        return voitureBox;
    }

    private Image createDefaultCarImage() {
        // Créer une image par défaut simple
        return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==");
    }

    private void openAdminInterface() {
        AdminPanelLoginDialog loginDialog = new AdminPanelLoginDialog();
        loginDialog.show();
    }

    private void reserverVoiture(String modele) {
        // Ouvrir la boîte de dialogue de connexion admin
        AdminLoginDialog loginDialog = new AdminLoginDialog(modele);
        loginDialog.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}