package com.location.voitures.utils;

import com.location.voitures.entities.Admin;
import com.location.voitures.entities.Paiement;
import com.location.voitures.entities.Reservation;
import com.location.voitures.entities.Utilisateur;
import com.location.voitures.entities.Voiture;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

// Classe utilitaire pour initialiser et fournir la SessionFactory d'Hibernate
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    // Constructeur privé pour empêcher l'instanciation
    private HibernateUtil() {
    }

    // Crée la SessionFactory si elle n'existe pas déjà
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // 1. Charge la configuration à partir du fichier hibernate.cfg.xml
                Configuration configuration = new Configuration().configure("hibernate.cfg.xml");

                // 2. CORRECTION CRITIQUE : Ajout explicite des classes annotées
                // Cela force Hibernate à reconnaître les entités (résout HHH000183)
                configuration.addAnnotatedClass(Utilisateur.class);
                configuration.addAnnotatedClass(Admin.class);
                configuration.addAnnotatedClass(Voiture.class);
                configuration.addAnnotatedClass(Reservation.class);
                configuration.addAnnotatedClass(Paiement.class);

                // 3. Construction du ServiceRegistry
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                // 4. Construit la SessionFactory
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);

                System.out.println("Hibernate SessionFactory créée avec succès.");

            } catch (Exception e) {
                System.err.println("Erreur lors de l'initialisation de la SessionFactory: " + e.getMessage());
                e.printStackTrace();
                throw new ExceptionInInitializerError(e);
            }
        }
        return sessionFactory;
    }

    // Ferme la SessionFactory et libère les ressources
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}