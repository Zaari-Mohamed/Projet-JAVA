package com.location.voitures.services;

import com.location.voitures.dao.PaiementDAO;
import com.location.voitures.dao.ReservationDAO;
import com.location.voitures.entities.Paiement;
import com.location.voitures.entities.Reservation;
import com.location.voitures.entities.Reservation.StatutReservation;
import java.time.LocalDateTime;
import java.util.Optional;

public class PaymentService {

    private final PaiementDAO paiementDAO;
    private final ReservationDAO reservationDAO;

    public PaymentService(PaiementDAO paiementDAO, ReservationDAO reservationDAO) {
        this.paiementDAO = paiementDAO;
        this.reservationDAO = reservationDAO;
    }

    public boolean traiterPaiement(Paiement paiement, Reservation reservation) {

        // Règle métier : Vérification du montant
        if (paiement.getMontant() < reservation.getCoutTotal()) {
            System.err.println("Paiement échoué: Montant insuffisant.");
            return false;
        }

        // Enregistre la date de paiement et la liaison à la réservation
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setReservation(reservation);
        paiementDAO.save(paiement);

        // Règle métier : Met à jour le statut de la réservation à CONFIRMEE
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservationDAO.update(reservation);

        return true;
    }

    public void rembourserPaiement(Paiement paiement) {
        // Logique de remboursement (simulation de transaction externe)
        System.out.println(
                "Remboursement de " + paiement.getMontant() + " de la méthode " + paiement.getMethode() + " traité.");
    }

    public Optional<Paiement> getPaiementByReservation(Reservation reservation) {
        // CORRECTION des deux erreurs :
        // 1. Utilisation de l'instance 'this.paiementDAO' (non statique).
        // 2. Utilisation de 'findOneByReservation' pour retourner Optional<Paiement>.
        return this.paiementDAO.findOneByReservation(reservation);
    }
}