package com.location.voitures.dao;

import com.location.voitures.entities.Utilisateur;
import java.util.Optional;

public class UtilisateurDAO extends GenericDAOImpl<Utilisateur, Long> {

    public UtilisateurDAO() {
        super(Utilisateur.class);
    }

    // Recherche un utilisateur par son adresse email (utilisé pour la connexion)
    public Optional<Utilisateur> findByEmail(String email) {
        try {
            return findOneByAttribute("email", email);
        } catch (Exception e) {
            // S'il n'y a pas de résultat, retourne Optional.empty()
            return Optional.empty();
        }
    }
}
