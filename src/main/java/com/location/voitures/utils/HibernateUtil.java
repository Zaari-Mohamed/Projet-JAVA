package com.location.voitures.utils;
import com.location.voitures.entities.Admin;
import com.location.voitures.entities.Reservation;
import com.location.voitures.entities.Utilisateur;
import com.location.voitures.entities.Voiture;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
public class HibernateUtil {
    private static SessionFactory sessionFactory;
    private HibernateUtil() {
    }
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
                configuration.addAnnotatedClass(Utilisateur.class);
                configuration.addAnnotatedClass(Admin.class);
                configuration.addAnnotatedClass(Voiture.class);
                configuration.addAnnotatedClass(Reservation.class);
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
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
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
