package com.location.voitures.dao;
import com.location.voitures.entities.Utilisateur;
import java.util.Optional;
public class UtilisateurDAO extends GenericDAOImpl<Utilisateur, Long> {
    public UtilisateurDAO() {
        super(Utilisateur.class);
    }
    public Optional<Utilisateur> findByEmail(String email) {
        try {
            return findOneByAttribute("email", email);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
