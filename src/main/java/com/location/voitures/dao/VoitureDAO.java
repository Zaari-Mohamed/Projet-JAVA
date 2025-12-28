package com.location.voitures.dao;
import com.location.voitures.entities.Voiture;
import java.util.List;
import java.util.Optional;
public class VoitureDAO extends GenericDAOImpl<Voiture, Long> {
    public VoitureDAO() {
        super(Voiture.class);
    }
    public List<Voiture> findByMarque(String marque) {
        return findAllByAttribute("marque", marque);
    }
    public List<Voiture> findAvailableCars() {
        return findAllByAttribute("disponible", true);
    }
    public Optional<Voiture> findByImmatriculation(String immatriculation) {
        return findOneByAttribute("immatriculation", immatriculation);
    }
}
