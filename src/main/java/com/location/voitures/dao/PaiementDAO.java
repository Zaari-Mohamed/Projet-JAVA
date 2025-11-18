package com.location.voitures.dao;

import com.location.voitures.entities.Paiement;
import com.location.voitures.entities.Reservation;
import java.util.List;
import java.util.Optional; // Importation nécessaire pour findOneByReservation

// Data Access Object pour l'entité Paiement
// Hérite de GenericDAOImpl qui fournit les méthodes CRUD de base
public class PaiementDAO extends GenericDAOImpl<Paiement, Long> {

    public PaiementDAO() {
        super(Paiement.class);
    }

    /**
     * Recherche UN SEUL Paiement par Réservation.
     * C'est la méthode utilisée par PaymentService.getPaiementByReservation().
     * * @param reservation L'objet Reservation à rechercher.
     * 
     * @return Un Optional contenant le Paiement trouvé, ou Optional.empty().
     */
    public Optional<Paiement> findOneByReservation(Reservation reservation) {
        // Appelle la méthode générique pour trouver UN SEUL résultat
        // Le nom de l'attribut est 'reservation', correspondant au champ de l'entité
        // Paiement
        return findOneByAttribute("reservation", reservation);
    }

    /**
     * Récupère TOUS les paiements associés à une réservation spécifique.
     * C'est utile pour vérifier l'historique de paiement d'une réservation.
     * * @param reservation L'objet Reservation à rechercher.
     * 
     * @return Une liste de Paiement.
     */
    public List<Paiement> findByReservation(Reservation reservation) {
        return findAllByAttribute("reservation", reservation);
    }

    /**
     * Récupère les paiements effectués par une méthode spécifique.
     * * @param methode La méthode de paiement (utilisant l'enum MethodePaiement de
     * Paiement).
     * 
     * @return Une liste de Paiement.
     */
    public List<Paiement> findByMethodePaiement(Paiement.MethodePaiement methode) {
        // Supposant que l'entité Paiement a un champ 'methodePaiement'
        return findAllByAttribute("methodePaiement", methode);
    }
}