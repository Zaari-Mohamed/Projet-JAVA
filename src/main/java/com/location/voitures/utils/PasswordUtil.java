package com.location.voitures.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    private static final int COST = 12; // Coût du hachage BCrypt

    private PasswordUtil() {
    }

    public static String hashPassword(String plainPassword) {
        // Génère le hachage avec un salt aléatoire intégré
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(COST));
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        // Vérifie le mot de passe en clair contre le hash stocké
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}