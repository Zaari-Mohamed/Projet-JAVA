package com.location.voitures.services;

import com.location.voitures.dao.VoitureDAO;
import com.location.voitures.entities.Voiture;

import java.util.List;
import java.util.Optional;

public class VoitureService {

    private final VoitureDAO voitureDAO;

    public VoitureService() {
        this.voitureDAO = new VoitureDAO();
    }

    public VoitureService(VoitureDAO voitureDAO) {
        this.voitureDAO = voitureDAO;
    }

    /**
     * Récupère toutes les voitures disponibles.
     * @return Liste des voitures disponibles, ou une liste vide si aucune n'est disponible.
     */
    public List<Voiture> findAllAvailable() {
        return voitureDAO.findAvailableCars();
    }

    /**
     * Récupère une voiture par son ID.
     * @param id L'ID de la voiture
     * @return Optional contenant la voiture si trouvée, sinon Optional vide
     */
    public Optional<Voiture> findById(Long id) {
        return voitureDAO.findById(id);
    }

    /**
     * Récupère toutes les voitures.
     * @return Liste de toutes les voitures
     */
    public List<Voiture> findAll() {
        return voitureDAO.findAll();
    }

    /**
     * Sauvegarde une nouvelle voiture.
     * @param voiture La voiture à sauvegarder
     * @return La voiture sauvegardée avec son ID généré
     */
    public Voiture saveVoiture(Voiture voiture) {
        voitureDAO.save(voiture);
        return voiture;
    }

    /**
     * Met à jour une voiture existante.
     * @param voiture La voiture à mettre à jour
     */
    public void updateVoiture(Voiture voiture) {
        voitureDAO.update(voiture);
    }

    /**
     * Supprime une voiture par son ID.
     * @param id L'ID de la voiture à supprimer
     */
    public void deleteVoiture(Long id) {
        Optional<Voiture> voitureOpt = voitureDAO.findById(id);
        voitureOpt.ifPresent(voiture -> voitureDAO.delete(voiture));
    }

    /**
     * Rend une voiture disponible.
     * @param id L'ID de la voiture
     */
    public void setVoitureAvailable(Long id) {
        Optional<Voiture> voitureOpt = voitureDAO.findById(id);
        voitureOpt.ifPresent(voiture -> {
            voiture.setDisponible(true);
            voitureDAO.update(voiture);
        });
    }

    /**
     * Rend une voiture indisponible.
     * @param id L'ID de la voiture
     */
    public void setVoitureUnavailable(Long id) {
        Optional<Voiture> voitureOpt = voitureDAO.findById(id);
        voitureOpt.ifPresent(voiture -> {
            voiture.setDisponible(false);
            voitureDAO.update(voiture);
        });
    }
}
