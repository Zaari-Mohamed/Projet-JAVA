package com.location.voitures.dao;

import com.location.voitures.entities.Voiture;
import java.util.List;

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
    
    // Les autres méthodes CRUD sont héritées de GenericDAOImpl<Voiture, Long>
}