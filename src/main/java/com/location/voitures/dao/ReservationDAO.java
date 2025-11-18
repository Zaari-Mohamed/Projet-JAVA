package com.location.voitures.dao;

import com.location.voitures.entities.Reservation;
import com.location.voitures.entities.Voiture;
import com.location.voitures.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;

// Data Access Object pour l'entité Reservation
public class ReservationDAO extends GenericDAOImpl<Reservation, Long> {

    public ReservationDAO() {
        super(Reservation.class);
    }

    // Méthode cruciale: Recherche les réservations actives qui chevauchent la période donnée pour une voiture spécifique.
    public List<Reservation> findOverlappingReservations(Voiture voiture, LocalDateTime debut, LocalDateTime fin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            // HQL pour détecter le chevauchement (Règle métier)
            // Un chevauchement existe si : (le début de la nouvelle < la fin de l'ancienne) ET (la fin de la nouvelle > le début de l'ancienne)
            String hql = "FROM Reservation r " +
                         "WHERE r.voiture = :voiture " +
                         "AND r.statut IN ('EN_ATTENTE', 'CONFIRMEE') " + // Ne considère que les réservations actives
                         "AND :debut < r.dateFin AND :fin > r.dateDebut";
            
            Query<Reservation> query = session.createQuery(hql, Reservation.class);
            query.setParameter("voiture", voiture);
            query.setParameter("debut", debut);
            query.setParameter("fin", fin);
            
            return query.list();
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche de chevauchements de réservation.");
            e.printStackTrace();
            return List.of();
        }
    }
    
    // Récupère toutes les réservations d'un utilisateur donné
    public List<Reservation> findByUtilisateurId(Long utilisateurId) {
        // Utilise la méthode générique pour filtrer par l'ID de l'objet Utilisateur lié
        return findAllByAttribute("utilisateur.id", utilisateurId);
    }
    
    // Récupère toutes les réservations confirmées pour une voiture spécifique
    public List<Reservation> findConfirmedReservationsByVoiture(Voiture voiture) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Reservation r " +
                         "WHERE r.voiture = :voiture " +
                         "AND r.statut = 'CONFIRMEE' " + 
                         "ORDER BY r.dateDebut ASC";
            
            Query<Reservation> query = session.createQuery(hql, Reservation.class);
            query.setParameter("voiture", voiture);
            
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // Les autres méthodes CRUD sont héritées de GenericDAOImpl<Reservation, Long>
}