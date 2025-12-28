-- Script d'insertion de données d'exemple pour l'application de location de voitures

USE location_voitures;

-- Insertion d'utilisateurs d'exemple
INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, telephone, adresse) VALUES
('Dupont', 'Jean', 'jean.dupont@email.com', 'password123', '0123456789', '123 Rue de la Paix, Paris'),
('Martin', 'Marie', 'marie.martin@email.com', 'password456', '0987654321', '456 Avenue des Champs, Lyon'),
('Bernard', 'Pierre', 'pierre.bernard@email.com', 'password789', '0147258369', '789 Boulevard Saint-Germain, Marseille');

-- Insertion d'administrateurs d'exemple
INSERT INTO admins (nom, prenom, email, mot_de_passe, niveau_acces) VALUES
('Admin', 'Super', 'admin@location.com', 'admin123', 'SUPER_ADMIN'),
('Gestionnaire', 'Local', 'gestionnaire@location.com', 'gest456', 'ADMIN');

-- Insertion de voitures d'exemple
INSERT INTO voitures (marque, modele, annee, couleur, immatriculation, prix_par_jour, disponible, type_carburant, nombre_places, transmission) VALUES
('Renault', 'Clio', 2022, 'Rouge', 'AB-123-CD', 35.00, TRUE, 'ESSENCE', 5, 'MANUELLE'),
('Peugeot', '308', 2023, 'Bleu', 'EF-456-GH', 45.00, TRUE, 'DIESEL', 5, 'AUTOMATIQUE'),
('Citroën', 'C3', 2021, 'Blanc', 'IJ-789-KL', 30.00, TRUE, 'ESSENCE', 5, 'MANUELLE'),
('BMW', 'Série 3', 2023, 'Noir', 'MN-012-OP', 80.00, TRUE, 'DIESEL', 5, 'AUTOMATIQUE'),
('Audi', 'A4', 2022, 'Gris', 'QR-345-ST', 75.00, TRUE, 'ESSENCE', 5, 'AUTOMATIQUE'),
('Mercedes', 'Classe A', 2023, 'Blanc', 'UV-678-WX', 85.00, FALSE, 'DIESEL', 5, 'AUTOMATIQUE');

-- Insertion de réservations d'exemple
INSERT INTO reservations (utilisateur_id, voiture_id, date_debut, date_fin, prix_total, statut) VALUES
(1, 1, '2024-01-15', '2024-01-20', 175.00, 'CONFIRMEE'),
(2, 3, '2024-01-18', '2024-01-22', 120.00, 'EN_COURS'),
(3, 4, '2024-01-25', '2024-01-30', 400.00, 'EN_ATTENTE');

