-- Script de création de la base de données pour l'application de location de voitures

-- Création de la base de données
CREATE DATABASE IF NOT EXISTS location_voitures;
USE location_voitures;

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS utilisateurs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    telephone VARCHAR(20),
    adresse TEXT,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des administrateurs
CREATE TABLE IF NOT EXISTS admins (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    niveau_acces VARCHAR(50) DEFAULT 'ADMIN',
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des voitures
CREATE TABLE IF NOT EXISTS voitures (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    marque VARCHAR(50) NOT NULL,
    modele VARCHAR(50) NOT NULL,
    annee INT NOT NULL,
    couleur VARCHAR(30),
    immatriculation VARCHAR(20) UNIQUE NOT NULL,
    prix_par_jour DECIMAL(10,2) NOT NULL,
    disponible BOOLEAN DEFAULT TRUE,
    type_carburant VARCHAR(20),
    nombre_places INT DEFAULT 5,
    transmission VARCHAR(20) DEFAULT 'MANUELLE',
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des réservations
CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    utilisateur_id BIGINT NOT NULL,
    voiture_id BIGINT NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    prix_total DECIMAL(10,2) NOT NULL,
    statut VARCHAR(20) DEFAULT 'EN_ATTENTE',
    date_reservation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    FOREIGN KEY (voiture_id) REFERENCES voitures(id) ON DELETE CASCADE
);

-- Table des paiements
CREATE TABLE IF NOT EXISTS paiements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_id BIGINT NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    methode_paiement VARCHAR(50) NOT NULL,
    statut_paiement VARCHAR(20) DEFAULT 'EN_ATTENTE',
    date_paiement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reference_transaction VARCHAR(100),
    FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE
);

-- Index pour améliorer les performances
CREATE INDEX idx_utilisateur_email ON utilisateurs(email);
CREATE INDEX idx_admin_email ON admins(email);
CREATE INDEX idx_voiture_disponible ON voitures(disponible);
CREATE INDEX idx_reservation_dates ON reservations(date_debut, date_fin);
CREATE INDEX idx_reservation_statut ON reservations(statut);