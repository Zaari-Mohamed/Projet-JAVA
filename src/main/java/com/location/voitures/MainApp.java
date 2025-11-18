package com.location.voitures;

import com.location.voitures.entities.Reservation;
import com.location.voitures.entities.Utilisateur;
import com.location.voitures.entities.Voiture;
import com.location.voitures.services.AuthService;
import com.location.voitures.services.ReservationService;
import com.location.voitures.services.VoitureService;
import com.location.voitures.utils.HibernateUtil;
import com.location.voitures.dao.UtilisateurDAO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MainApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static AuthService authService;
    private static VoitureService voitureService;
    private static ReservationService reservationService;
    private static Utilisateur utilisateurConnecte = null;

    public static void main(String[] args) {
        System.out.println("--- Application de Location de Voitures (Mode CLI) ---");

        try {
            HibernateUtil.getSessionFactory();
            
            // Initialisation des services
            voitureService = new VoitureService();
            authService = new AuthService(new UtilisateurDAO());
            reservationService = new ReservationService();

            // Étape essentielle : assure que la base contient au moins une voiture pour les
            // tests
            initialiserDonneesTest();

            menuPrincipal();

        } catch (Exception e) {
            System.err.println("Une erreur critique est survenue: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
            System.out.println("\n--- SessionFactory fermée. Application terminée. ---");
            if (scanner != null)
                scanner.close();
        }
    }

    // --- METHODES D'AIDE ET INITIALISATION ---

    private static void initialiserDonneesTest() {
        // Cette méthode assure qu'il y a des données minimales quand
        // hbm2ddl.auto=create est utilisé.
        System.out.println("\n--- Initialisation des données de test ---");
        try {
            Voiture testVoiture = new Voiture("Renault", "Clio V", 2022, "AB-123-CD", 45.00, true);

            // Sauvegarde de la voiture. Si vous utilisez hbm2ddl.auto=update, ajoutez une
            // vérification ici.
            voitureService.saveVoiture(testVoiture);
            System.out.println("-> Voiture de test 'Clio V' ajoutée/vérifiée.");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation de la voiture : " + e.getMessage());
        }
    }

    private static LocalDateTime saisirDate(DateTimeFormatter formatter, String prompt) {
        LocalDateTime date = null;
        while (date == null) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                date = LocalDateTime.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.err.println("Format invalide. Veuillez suivre le format JJ/MM/AAAA HH:MM.");
            }
        }
        return date;
    }

    // Méthode de calcul du coût (temporairement ici)
    private static double calculerCoutTotal(Voiture voiture, LocalDateTime debut, LocalDateTime fin) {
        Duration duration = Duration.between(debut, fin);
        long hours = duration.toHours();
        double jours = hours / 24.0;

        if (voiture.getTauxJournalier() == null) {
            return 0.0;
        }
        // Arrondir au jour supérieur pour le tarif
        return Math.ceil(jours) * voiture.getTauxJournalier();
    }

    // --- MENU PRINCIPAL ET SAISIES ---

    private static void menuPrincipal() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Menu Principal ---");
            if (utilisateurConnecte == null) {
                // Menu Déconnecté
                System.out.println("1. S'inscrire");
                System.out.println("2. Se connecter");
                System.out.println("3. Quitter");
                System.out.print("Choix: ");
                // ... (Logique de choix)
            } else {
                // Menu Connecté
                System.out.println("Bienvenue, " + utilisateurConnecte.getPrenom() + ".");
                System.out.println("4. Consulter les voitures disponibles");
                System.out.println("5. Faire une réservation");
                System.out.println("6. Se déconnecter");
                System.out.println("7. Quitter");
                System.out.print("Choix: ");
                // ... (Logique de choix)
            }
            // Logique de switch-case pour le menu... (omis pour la concision)
            String choix = scanner.nextLine();
            switch (choix) {
                case "1":
                    saisirInscription();
                    break;
                case "2":
                    saisirConnexion();
                    break;
                case "3":
                    running = false;
                    break;
                case "4":
                    if (utilisateurConnecte != null)
                        afficherVoituresDisponibles();
                    break;
                case "5":
                    if (utilisateurConnecte != null)
                        saisirReservation();
                    break;
                case "6":
                    if (utilisateurConnecte != null) {
                        utilisateurConnecte = null;
                        System.out.println("Déconnexion réussie.");
                    }
                    break;
                case "7":
                    running = false;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    private static void saisirInscription() {
        System.out.println("\n--- Inscription ---");
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Prénom: ");
        String prenom = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String motDePasse = scanner.nextLine();
        System.out.print("Téléphone (Optionnel): ");
        String telephone = scanner.nextLine();

        Utilisateur newUser = new Utilisateur(nom, prenom, email, motDePasse, telephone);
        try {
            authService.registerUser(newUser, motDePasse);
            utilisateurConnecte = newUser;
            System.out.println("Inscription réussie. Bienvenue, " + newUser.getPrenom() + "!");
        } catch (IllegalArgumentException e) {
            System.err.println("Échec de l'inscription: " + e.getMessage());
        }
    }

    private static void saisirConnexion() {
        System.out.println("\n--- Connexion ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String motDePasse = scanner.nextLine();

        Optional<Utilisateur> optionalUser = authService.login(email, motDePasse);
        if (optionalUser.isPresent()) {
            utilisateurConnecte = optionalUser.get();
            System.out.println("Connexion réussie. Bienvenue, " + utilisateurConnecte.getPrenom() + "!");
        } else {
            System.err.println("Email ou mot de passe incorrect.");
        }
    }

    private static void afficherVoituresDisponibles() {
        System.out.println("\n--- Voitures Disponibles ---");
        List<Voiture> voitures = voitureService.findAllAvailable();

        if (voitures == null || voitures.isEmpty()) {
            System.out.println("Aucune voiture n'est actuellement disponible.");
        } else {
            System.out.printf("%-5s | %-10s | %-15s | %-10s | %-8s\n", "ID", "MARQUE", "MODÈLE", "ANNÉE", "PRIX/JOUR");
            System.out.println("-------------------------------------------------------");
            for (Voiture v : voitures) {
                double taux = v.getTauxJournalier() != null ? v.getTauxJournalier() : 0.0;
                System.out.printf("%-5d | %-10s | %-15s | %-10d | %.2f\n",
                        v.getId(), v.getMarque(), v.getModele(), v.getAnnee(), taux);
            }
        }
    }

    private static void saisirReservation() {
        afficherVoituresDisponibles();
        List<Voiture> voituresDisponibles = voitureService.findAllAvailable();
        if (voituresDisponibles == null || voituresDisponibles.isEmpty()) {
            System.out.println("Impossible de créer une réservation sans voiture disponible.");
            return;
        }

        System.out.println("\n--- Création d'une Réservation ---");

        // 1. CHOIX DE LA VOITURE
        Long voitureId = null;
        Voiture voitureChoisie = null;
        while (voitureChoisie == null) {
            System.out.print("Entrez l'ID de la voiture à réserver: ");
            try {
                voitureId = Long.parseLong(scanner.nextLine());
                Optional<Voiture> optionalVoiture = voitureService.findById(voitureId);
                voitureChoisie = optionalVoiture.orElse(null);

                if (voitureChoisie == null || !voitureChoisie.getDisponible()) {
                    System.err.println("Voiture non trouvée, ID invalide ou non disponible.");
                    voitureChoisie = null;
                }
            } catch (NumberFormatException e) {
                System.err.println("Veuillez entrer un nombre valide.");
            }
        }

        // 2. SAISIE DES DATES
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        System.out.println("Format de date et heure requis: JJ/MM/AAAA HH:MM (ex: 16/11/2025 10:00)");

        LocalDateTime dateDebut = saisirDate(formatter, "Début de la réservation (JJ/MM/AAAA HH:MM): ");
        LocalDateTime dateFin = saisirDate(formatter, "Fin de la réservation (JJ/MM/AAAA HH:MM): ");

        if (dateDebut == null || dateFin == null || dateDebut.isAfter(dateFin) || dateDebut.isEqual(dateFin)) {
            System.err.println("Erreur: Dates invalides.");
            return;
        }

        // 3. VÉRIFICATION ET CRÉATION
        try {
            double coutTotal = calculerCoutTotal(voitureChoisie, dateDebut, dateFin);

            if (reservationService.checkAvailability(voitureChoisie, dateDebut, dateFin, null)) {

                System.out.printf("\nCoût Total estimé pour cette période: %.2f\n", coutTotal);
                System.out.print("Confirmer la réservation (oui/non) ? ");
                String confirmation = scanner.nextLine();

                if (confirmation.equalsIgnoreCase("oui")) {

                    // CRÉATION DE L'OBJET AVANT L'APPEL AU SERVICE
                    Reservation reservationAConstruire = new Reservation(
                            utilisateurConnecte,
                            voitureChoisie,
                            dateDebut,
                            dateFin,
                            coutTotal,
                            Reservation.StatutReservation.EN_ATTENTE);

                    // APPEL CORRIGÉ : utiliser la signature creerReservation(Reservation)
                    Optional<Reservation> optionalReservation = reservationService
                            .creerReservation(reservationAConstruire);
                    Reservation nouvelleReservation = optionalReservation.orElse(null);

                    if (nouvelleReservation != null) {
                        System.out.println("✅ Réservation créée avec succès ! ID: " + nouvelleReservation.getId());
                        System.out.println("Statut: " + nouvelleReservation.getStatut());
                    } else {
                        System.err.println("❌ Échec de la création de la réservation (conflit ou erreur interne).");
                    }
                } else {
                    System.out.println("Réservation annulée par l'utilisateur.");
                }
            } else {
                System.err.println("❌ Erreur: La voiture n'est pas disponible pour les dates sélectionnées (conflit).");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur de validation: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de la réservation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}