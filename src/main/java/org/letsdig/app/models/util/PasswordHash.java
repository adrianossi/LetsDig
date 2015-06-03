package org.letsdig.app.models.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by adrian on 6/2/15.
 *
 * Based on approach outlined at http://viralpatel.net/blogs/java-md5-hashing-salting-password/
 */
public class PasswordHash {

    private static final String salt = "1a2b";

    public static String getHash(String password) {

        // Confirm password data is present
//        if (password == null) {
  //          return null;
    //    }

        // Create a one-way hash of password

        String hash = null;
        String saltedPassword = applySalt(password);

        if(null == password) return null;

        try {
            // Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            // Update input string in MessageDigest
            digest.update(saltedPassword.getBytes(), 0, saltedPassword.length());

            // Converts message digest value in base 16 (hex)
            hash = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash;
    }

    private static String applySalt(String password) {
        return password + salt;
    }

    public static boolean isValidPassword(String password, String hash) {

        // Convert passed password to hash
        String hashedPassword = getHash(password);

        // Verify if hashes are equal and return result
        return hashedPassword.equals(hash);
    }

}
