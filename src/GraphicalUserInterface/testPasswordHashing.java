/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GraphicalUserInterface;

import de.svws_nrw.ext.jbcrypt.BCrypt;

/**
 *
 * @author yonas
 */
public class testPasswordHashing {
  public static void main(String[] args) {
        String originalPassword = "mySecurePassword"; // Replace with your test password
        String hashedPassword = BCrypt.hashpw(originalPassword, BCrypt.gensalt());

        System.out.println("Original Password: " + originalPassword);
        System.out.println("Hashed Password: " + hashedPassword);

        // Simulate a login attempt with the correct password
        boolean isMatch = BCrypt.checkpw(originalPassword, hashedPassword);
        System.out.println("Password match: " + isMatch); // Should print true

        // Simulate a login attempt with an incorrect password
        boolean isMatchWrong = BCrypt.checkpw("wrongPassword", hashedPassword);
        System.out.println("Password match with wrong password: " + isMatchWrong); // Should print false
    }
}