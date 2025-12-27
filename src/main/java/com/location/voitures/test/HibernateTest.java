package com.location.voitures.test;

import com.location.voitures.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Classe de test pour vérifier la configuration Hibernate
 */
public class HibernateTest {
    
    public static void main(String[] args) {
        System.out.println("=== Test de connexion Hibernate ===");
        
        try {
            // Test de création de la SessionFactory
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            System.out.println("✓ SessionFactory créée avec succès");
            
            // Test d'ouverture d'une session
            Session session = sessionFactory.openSession();
            System.out.println("✓ Session ouverte avec succès");
            
            // Test de requête simple
            session.beginTransaction();
            Long count = (Long) session.createQuery("SELECT COUNT(*) FROM Utilisateur").uniqueResult();
            session.getTransaction().commit();
            System.out.println("✓ Requête exécutée avec succès - Nombre d'utilisateurs: " + count);
            
            session.close();
            System.out.println("✓ Session fermée avec succès");
            
            System.out.println("\n=== Test réussi ! Hibernate est correctement configuré ===");
            
        } catch (Exception e) {
            System.err.println("✗ Erreur lors du test Hibernate:");
            e.printStackTrace();
            System.err.println("\nVérifiez:");
            System.err.println("1. MySQL est démarré");
            System.err.println("2. La base de données 'location_voitures' existe");
            System.err.println("3. Les identifiants dans hibernate.cfg.xml sont corrects");
        } finally {
            HibernateUtil.shutdown();
        }
    }
}