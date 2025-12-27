package com.location.voitures.dao;

import com.location.voitures.entities.Voiture;
import java.util.List;
import java.util.Optional;

// Data Access Object pour l'entité Voiture
public class VoitureDAO extends GenericDAOImpl<Voiture, Long> {

    public VoitureDAO() {
        super(Voiture.class);
    }
    
    // Récupère toutes les voitures d'une marque spécifique
    public List<Voiture> findByMarque(String marque) {
        // Utilise la méthode générique findAllByAttribute
        return findAllByAttribute("marque", marque);
    }

    // Récupère toutes les voitures qui sont actuellement marquées comme disponibles
    public List<Voiture> findAvailableCars() {
        // Utilise la méthode générique findAllByAttribute
        return findAllByAttribute("disponible", true);
    }
    
    // Récupère une voiture par son immatriculation
    public Optional<Voiture> findByImmatriculation(String immatriculation) {
        return findOneByAttribute("immatriculation", immatriculation);
    }
    
    // Les autres méthodes CRUD sont héritées de GenericDAOImpl<Voiture, Long>
}