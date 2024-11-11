/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GraphicalUserInterface;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.text.AES256TextEncryptor;


/**
 *
 * @author yonas
 */
public class DatabaseConf {
    private static final String Encryption_KEY = "EncryptionKey12@"; 
    private StandardPBEStringEncryptor Encryptor;
    private AES256TextEncryptor AESEncryptor;

    // Constructor
    public DatabaseConf() {
        Encryptor = new StandardPBEStringEncryptor();
        Encryptor.setPassword(Encryption_KEY); // this is the Password used for PBE encryption
        Encryptor.setAlgorithm("PBEWithMD5AndDES"); // PBE algorithm (password-based encryption)

        
        
        AESEncryptor = new AES256TextEncryptor();
        AESEncryptor.setPassword("BANKPASSWORD"); //this is the AES password 
    }


    public String encrpt(String plaintext) { //encryption method for PBE
        return Encryptor.encrypt(plaintext);
    }

   
    public String decrpt(String encryptedData) { //decryption method for AES
        try {
            return Encryptor.decrypt(encryptedData); // Decrypt using the same Encryptor
        } catch (EncryptionOperationNotPossibleException e) {
            System.err.println("Decryption failed for PBE: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    
    public String AESencrpt(String plaintext) { //encryption  methd for  AES
        return AESEncryptor.encrypt(plaintext);
    }

    // Decrypt using AES256 (AES256TextEncryptor)
    public String AESdecrpt(String encryptedText) { //decryption methd for AES
        try {
            return AESEncryptor.decrypt(encryptedText); 
        } catch (Exception e) {
            System.err.println("Decryption failed for AES: " + e.getMessage());
            e.printStackTrace();
            return null;  // Or handle it more appropriately
        }
    }

    
    public static void main(String[] args) { //testing if both PBE and 
        DatabaseConf config = new DatabaseConf(); // AES encryption and decryption works
        String username = "Project";
        String password = "EncryptionKey12@";
        String firstName = "Liam";
        

        String encryptedFirstName = config.encrpt(firstName); //PBE test
        System.out.println("Encrypted First Name (PBE): " + encryptedFirstName);
        System.out.println("Decrypted First Name (PBE): " + config.decrpt(encryptedFirstName));
        
      
        String encryptedUsername = config.AESencrpt(username); //AES test
        System.out.println("Encrypted Username (AES): " + encryptedUsername);
        System.out.println("Decrypted Username (AES): " + config.AESdecrpt(encryptedUsername));
        
        //result of the test: successful
    }
}