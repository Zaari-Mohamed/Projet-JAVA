
# Architecture du projet "Location Voitures"

## Structure du projet

Le projet est structuré en plusieurs modules pour séparer les responsabilités et faciliter le travail en équipe. Voici la structure du projet :
location-voitures/
│
├─ pom.xml   <-- (Configuration Maven) 
│
├─ src/
│  ├─ main/
│  │   ├─ java/
│  │   │    └─ com/location/voitures/
│  │   │         ├─ entities/
│  │   │         │    ├─ Voiture.java               // Membre 1
│  │   │         │    ├─ Utilisateur.java           // Membre 1 (structure générale)
│  │   │         │    ├─ Admin.java                 // Membre 2
│  │   │         │    ├─ Superviseur.java           // Membre 2
│  │   │         │    ├─ Reservation.java           // Membre 2
│  │   │         │    └─ Paiement.java              // Membre 2
│  │   │         │
│  │   │         ├─ dao/
│  │   │         │    ├─ GenericDAO.java            // Membre 2
│  │   │         │    ├─ VoitureDAO.java            // Membre 2
│  │   │         │    ├─ UtilisateurDAO.java        // Membre 2
│  │   │         │    └─ ReservationDAO.java        // Membre 2
│  │   │         │
│  │   │         ├─ services/
│  │   │         │    ├─ AuthService.java           // Membre 2
│  │   │         │    ├─ ReservationService.java    // Membre 2
│  │   │         │    └─ PaymentService.java        // Membre 2
│  │   │         │
│  │   │         ├─ controllers/
│  │   │         │    ├─ LoginController.java       // Membre 5
│  │   │         │    ├─ AdminDashboardController.java // Membre 5
│  │   │         │    └─ UserDashboardController.java  // Membre 5
│  │   │         │
│  │   │         ├─ utils/
│  │   │         │    ├─ HibernateUtil.java         // Membre 3
│  │   │         │    └─ PasswordUtil.java          // Membre 3 (optionnel)
│  │   │         │
│  │   │         └─ MainApp.java                    // Membre 5 (JavaFX main)
│  │   │
│  │   ├─ resources/
│  │   │    ├─ hibernate.cfg.xml                    // Membre 3
│  │   │    └─ META-INF/
│  │   │         └─ persistence.xml (si JPA)        // Membre 3 (optionnel)
│  │   │
│  │   └─ resources-ui/
│  │        ├─ views/
│  │        │    ├─ LoginView.fxml                  // Membre 5
│  │        │    ├─ AdminDashboard.fxml             // Membre 5
│  │        │    └─ UserDashboard.fxml              // Membre 5
│  │        │
│  │        └─ styles/
│  │             └─ app.css                         // Membre 5
│  │
│  └─ test/
│      └─ java/
│           └─ com/location/voitures/
│                └─ DatabaseTest.java               // Membre 4
│
└─ db/
   └─ scripts/
        ├─ create_tables.sql                        // Membre 4
        └─ sample_data.sql                          // Membre 4  on veut cette  repartition 




### 2. **Description des composants**

- **Maven** (`pom.xml`) : Utilisé pour gérer les dépendances et la configuration du projet.
  
- **Entities** (Membre 1) : Contient les entités Java qui représentent les objets du domaine, telles que `Voiture`, `Utilisateur`, `Admin`, `Superviseur`, etc.

- **DAO (Data Access Objects)** (Membre 2) : Gestion de l'accès aux données dans la base de données. Ce module contient les classes pour interagir avec la base de données (par exemple, `VoitureDAO`, `UtilisateurDAO`).

- **Services** (Membre 2) : Fournit la logique métier pour la gestion des utilisateurs, des réservations, et des paiements, par exemple via des classes comme `AuthService`, `ReservationService`, etc.

- **Contrôleurs JavaFX** (Membre 5) : Contient les contrôleurs qui gèrent les interactions utilisateur dans l'interface graphique avec JavaFX, comme `LoginController` et `AdminDashboardController`.

- **Utils** (Membre 3) : Contient des utilitaires comme `HibernateUtil` et `PasswordUtil`, utilisés pour la configuration de Hibernate et le traitement des mots de passe.

- **Base de données et scripts SQL** (Membre 4) : Scripts SQL pour créer la base de données (`create_tables.sql`) et insérer des données (`sample_data.sql`).

## Relations entre les entités

### Entités principales :

- **Voiture** : Représente une voiture disponible à la location.
- **Utilisateur** : Utilisateur général de l'application, qui peut effectuer des réservations.
- **Admin / Superviseur** : Rôles d'administrateur et de superviseur, avec des privilèges différents.
- **Reservation** : Liée à un utilisateur et une voiture.
- **Paiement** : Associe une réservation à un paiement effectué.
