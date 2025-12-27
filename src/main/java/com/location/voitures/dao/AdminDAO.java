package com.location.voitures.dao;

import com.location.voitures.entities.Admin;
import java.util.Optional;

public class AdminDAO extends GenericDAOImpl<Admin, Long> {

    public AdminDAO() {
        super(Admin.class);
    }

    // Recherche un admin par son adresse email (utilisé pour la connexion)
    public Optional<Admin> findByEmail(String email) {
        try {
            return findOneByAttribute("email", email);
        } catch (Exception e) {
            // S'il n'y a pas de résultat, retourne Optional.empty()
            return Optional.empty();
        }
    }
}
