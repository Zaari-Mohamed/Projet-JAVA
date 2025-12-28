package com.location.voitures.services;
import com.location.voitures.dao.ReservationDAO;
import com.location.voitures.dao.VoitureDAO;
import com.location.voitures.entities.Reservation;
import com.location.voitures.entities.Utilisateur;
import com.location.voitures.entities.Voiture;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
public class ReservationService {
    private final ReservationDAO reservationDAO;
    private final VoitureDAO voitureDAO;
    public ReservationService() {
        this.reservationDAO = new ReservationDAO();
        this.voitureDAO = new VoitureDAO();
    }
    public Optional<Reservation> creerReservation(Reservation reservation) {
        if (reservation.getDateDebut().isBefore(LocalDateTime.now().minusMinutes(1)) ||
                reservation.getDateFin().isBefore(reservation.getDateDebut())) {
            throw new IllegalArgumentException("Dates de réservation invalides ou dans le passé.");
        }
        if (!isVoitureDisponible(reservation.getVoiture(), reservation.getDateDebut(), reservation.getDateFin())) {
            return Optional.empty();
        }
        reservation.setStatut(Reservation.StatutReservation.EN_ATTENTE);
        reservationDAO.save(reservation);
        return Optional.of(reservation);
    }
    public boolean checkAvailability(Voiture voiture, LocalDateTime debut, LocalDateTime fin, Utilisateur user) {
        return isVoitureDisponible(voiture, debut, fin);
    }
    public boolean isVoitureDisponible(Voiture voiture, LocalDateTime debut, LocalDateTime fin) {
        List<Reservation> chevauchements = reservationDAO.findOverlappingReservations(voiture, debut, fin);
        return chevauchements.isEmpty();
    }
    @SuppressWarnings("unused")
    private double calculerCout(Reservation reservation) {
        long heures = ChronoUnit.HOURS.between(reservation.getDateDebut(), reservation.getDateFin());
        long jours = heures / 24;
        if (heures % 24 > 0)
            jours++;
        double tauxJournalier = reservation.getVoiture().getTauxJournalier();
        return jours * tauxJournalier;
    }
}
