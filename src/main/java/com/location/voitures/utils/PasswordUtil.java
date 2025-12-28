package com.location.voitures.utils;
import org.mindrot.jbcrypt.BCrypt;
public class PasswordUtil {
    private static final int COST = 12; 
    private PasswordUtil() {
    }
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(COST));
    }
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
