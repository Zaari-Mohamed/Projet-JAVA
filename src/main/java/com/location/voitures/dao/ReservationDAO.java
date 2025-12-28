package com.location.voitures.dao;
import com.location.voitures.entities.Reservation;
import com.location.voitures.entities.Voiture;
import com.location.voitures.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.time.LocalDateTime;
import java.util.List;
public class ReservationDAO extends GenericDAOImpl<Reservation, Long> {
    public ReservationDAO() {
        super(Reservation.class);
    }
    @Override
    public List<Reservation> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Reservation r " +
                         "LEFT JOIN FETCH r.voiture " +
                         "LEFT JOIN FETCH r.utilisateur " +
                         "ORDER BY r.id ASC";
            Query<Reservation> query = session.createQuery(hql, Reservation.class);
            return query.list();
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de toutes les réservations: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
    public List<Reservation> findOverlappingReservations(Voiture voiture, LocalDateTime debut, LocalDateTime fin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Reservation r " +
                         "WHERE r.voiture = :voiture " +
                         "AND r.statut IN ('EN_ATTENTE', 'CONFIRMEE') " + 
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
    public List<Reservation> findByUtilisateurId(Long utilisateurId) {
        return findAllByAttribute("utilisateur.id", utilisateurId);
    }
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
}
