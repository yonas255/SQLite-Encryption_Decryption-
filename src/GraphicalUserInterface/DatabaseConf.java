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

  private static final String Encryption_KEY= "EncryptionKey12@";
  private StandardPBEStringEncryptor Encryptor;
  private AES256TextEncryptor AESEncryptor;
    
    public DatabaseConf(){
      Encryptor=new StandardPBEStringEncryptor();
      Encryptor.setPassword(Encryption_KEY);
      Encryptor.setAlgorithm("PBEWithMD5AndDES");
      
      AESEncryptor = new AES256TextEncryptor();
      AESEncryptor.setPassword(Encryption_KEY);
  }
    public String encrpt(String textplain){
        return Encryptor.encrypt(textplain);
    }
    
    public String decrpt(String encryptedData) {
    try {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(Encryption_KEY); // Must match the key used during encryption
        encryptor.setAlgorithm("PBEWithMD5AndDES"); // Ensure this matches
        return encryptor.decrypt(encryptedData);
    } catch (EncryptionOperationNotPossibleException e) {
        System.err.println("Decryption failed: " + e.getMessage());
        return null;
    }
}
    
    public String AESencrpt(String textplain){
        return AESEncryptor.encrypt(textplain);
    }
    
    public String AESdecrpt(String Encryptedtext){
        return AESEncryptor.decrypt(Encryptedtext);
    }
    
    public static void main (String[] args){
        DatabaseConf config = new DatabaseConf();
        String Username= "Project";
        String password= "EncryptionKey12@";
        
       String firstName ="Liam";
       String encryptedFirstName = config.encrpt(firstName);
        
        System.out.println("Encrypted First Name: " + encryptedFirstName);
        System.out.println("Decrypted First Name: " + config.decrpt(encryptedFirstName));
    }
}
