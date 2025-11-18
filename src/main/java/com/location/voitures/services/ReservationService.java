package com.location.voitures.services;

import com.location.voitures.dao.ReservationDAO;
import com.location.voitures.dao.VoitureDAO;
import com.location.voitures.entities.Reservation;
import com.location.voitures.entities.Utilisateur; // Ajouté pour checkAvailability
import com.location.voitures.entities.Voiture;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class ReservationService {

    private final ReservationDAO reservationDAO;

    @SuppressWarnings("unused")
    private final VoitureDAO voitureDAO;

    @SuppressWarnings("unused")
    private final PaymentService paymentService;

    // Le constructeur doit être ajusté si vous initialisez les services sans DI
    // (Injection de dépendances)
    // Pour les besoins de MainApp, si vous n'utilisez pas de framework (Spring,
    // Guice), vous devrez adapter le constructeur
    public ReservationService() {
        this.reservationDAO = new ReservationDAO(); // Instancier le DAO ici
        this.voitureDAO = new VoitureDAO();
        this.paymentService = new PaymentService(new com.location.voitures.dao.PaiementDAO(), this.reservationDAO);
    }

    // Si vous utilisez l'injection de dépendances, conservez ce constructeur (mais
    // il est incompatible avec MainApp sans framework)
    /*
     * public ReservationService(ReservationDAO reservationDAO, VoitureDAO
     * voitureDAO, PaymentService paymentService) {
     * this.reservationDAO = reservationDAO;
     * this.voitureDAO = voitureDAO;
     * this.paymentService = paymentService;
     * }
     */

    public Optional<Reservation> creerReservation(Reservation reservation) {

        // Règle métier : Validation des dates
        if (reservation.getDateDebut().isBefore(LocalDateTime.now().minusMinutes(1)) ||
                reservation.getDateFin().isBefore(reservation.getDateDebut())) {
            throw new IllegalArgumentException("Dates de réservation invalides ou dans le passé.");
        }

        // Règle métier : Vérification de la disponibilité
        if (!isVoitureDisponible(reservation.getVoiture(), reservation.getDateDebut(), reservation.getDateFin())) {
            return Optional.empty();
        }

        // Règle métier : Calcul du coût total (Retiré car calculé dans MainApp.java)
        // double coutCalcule = calculerCout(reservation);
        // reservation.setCoutTotal(coutCalcule);
        // NOTE: Le coût est supposé être déjà calculé dans l'objet Reservation passé en
        // argument.

        // Statut initial
        reservation.setStatut(Reservation.StatutReservation.EN_ATTENTE);
        reservationDAO.save(reservation);
        return Optional.of(reservation);
    }

    // Méthode publique exposée pour MainApp (appelée checkAvailability dans
    // MainApp)
    public boolean checkAvailability(Voiture voiture, LocalDateTime debut, LocalDateTime fin, Utilisateur user) {
        // user n'est pas utilisé ici mais est inclus pour correspondre à une signature
        // de service courante.
        return isVoitureDisponible(voiture, debut, fin);
    }

    // Logique interne de disponibilité
    public boolean isVoitureDisponible(Voiture voiture, LocalDateTime debut, LocalDateTime fin) {
        // Appelle le DAO pour chercher les chevauchements actifs
        List<Reservation> chevauchements = reservationDAO.findOverlappingReservations(voiture, debut, fin);
        return chevauchements.isEmpty();
    }

    @SuppressWarnings("unused")
    private double calculerCout(Reservation reservation) {
        // Cette méthode n'est plus appelée par creerReservation, mais est conservée
        // pour la clarté.
        long heures = ChronoUnit.HOURS.between(reservation.getDateDebut(), reservation.getDateFin());
        long jours = heures / 24;
        if (heures % 24 > 0)
            jours++;

        double tauxJournalier = reservation.getVoiture().getTauxJournalier();

        return jours * tauxJournalier;
    }

    // ... (autres méthodes : annulerReservation, findReservationsUtilisateur,
    // completeReservation)
}