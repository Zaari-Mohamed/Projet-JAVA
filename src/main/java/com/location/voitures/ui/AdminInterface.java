package com.location.voitures.ui;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
public class AdminInterface extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Interface Admin - Location de Voitures");
        BorderPane root = new BorderPane();
        HBox header = createHeader();
        root.setTop(header);
        VBox sidebar = createSidebar();
        root.setLeft(sidebar);
        VBox mainContent = createMainContent();
        root.setCenter(mainContent);
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(15));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #34495e;");
        Label title = new Label("Administration");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        header.getChildren().add(title);
        return header;
    }
    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #ecf0f1;");
        sidebar.setPrefWidth(200);
        Button gestionVoituresBtn = new Button("Gestion Voitures");
        Button gestionUsersBtn = new Button("Gestion Utilisateurs");
        Button reservationsBtn = new Button("Réservations");
        Button statistiquesBtn = new Button("Statistiques");
        String buttonStyle = "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-pref-width: 160px;";
        gestionVoituresBtn.setStyle(buttonStyle);
        gestionUsersBtn.setStyle(buttonStyle);
        reservationsBtn.setStyle(buttonStyle);
        statistiquesBtn.setStyle(buttonStyle);
        sidebar.getChildren().addAll(gestionVoituresBtn, gestionUsersBtn, reservationsBtn, statistiquesBtn);
        return sidebar;
    }
    private VBox createMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        Label welcomeLabel = new Label("Bienvenue dans l'interface d'administration");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        TableView<String> voituresTable = new TableView<>();
        TableColumn<String, String> modeleCol = new TableColumn<>("Modèle");
        TableColumn<String, String> etatCol = new TableColumn<>("État");
        TableColumn<String, String> prixCol = new TableColumn<>("Prix/jour");
        voituresTable.getColumns().addAll(modeleCol, etatCol, prixCol);
        voituresTable.setPrefHeight(300);
        HBox actionButtons = new HBox(10);
        actionButtons.setAlignment(Pos.CENTER_LEFT);
        Button ajouterBtn = new Button("Ajouter Voiture");
        Button modifierBtn = new Button("Modifier");
        Button supprimerBtn = new Button("Supprimer");
        String actionButtonStyle = "-fx-background-color: #27ae60; -fx-text-fill: white;";
        ajouterBtn.setStyle(actionButtonStyle);
        modifierBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
        supprimerBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        actionButtons.getChildren().addAll(ajouterBtn, modifierBtn, supprimerBtn);
        content.getChildren().addAll(welcomeLabel, voituresTable, actionButtons);
        return content;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
