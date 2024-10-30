
package GraphicalUserInterface;

import java.io.BufferedReader;
import java.sql.*;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import javax.swing.JOptionPane;
import com.ibatis.common.jdbc.ScriptRunner;
import de.svws_nrw.ext.jbcrypt.BCrypt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.table.DefaultTableModel;
import static GraphicalUserInterface.login.getID;
import static GraphicalUserInterface.Registration.jTextField10;
import static GraphicalUserInterface.login.getpassword;


public class SQlite {
    
    public static String url = "jdbc:sqlite:C:/DB/DB.db";
    public static void main(String[] args) {
        
        java.awt.EventQueue.invokeLater(() ->{
            new NewJFrame().setVisible(true);
        });
  
    }
    
        public static void CreateDB () {
        //start of button
        String fileName = "DB.db"; //gets the DB name from the textbox
        String Dir = "C:/DB/" ;//gets the DB location from the textbox
        File directory = new File(Dir);
        url = "jdbc:sqlite:" + Dir + fileName; //creates a URL for the database
        System.out.print(url);

        try { //loads driver into memory, just one way of doing it
            
            Class.forName("org.sqlite.JDBC").newInstance(); 
          
        } catch (Exception ex) {
             
            JOptionPane.showMessageDialog(null, ex.getMessage()); //println for catching error, popup would be better
            
            JOptionPane.showMessageDialog(null, "You can exit the program and reopen it, then click on populate the DB.");
      
        }//end try catch
        
        if (!directory.exists()) {
            
            directory.mkdir(); //creates the folder were the DB will be saved.
            JOptionPane.showMessageDialog(null, "The Databse has been created successfully!");
            login.main(null);
                
            //Makes a connection called conn to the url created earlier
            try (Connection conn = DriverManager.getConnection(url)) {

                if (conn != null) { //if the connection is not null 
                    
                    NewJFrame.dbbutton.setText("Database can populated only once!"); //change the text on the create DB button

                }//end if

                else {

                    JOptionPane.showMessageDialog(null, "something went wrong, please try again!");
                    NewJFrame.main(null);

                }

            } 
            
            catch (SQLException e) {

                JOptionPane.showMessageDialog(null, e.getMessage()); //println for catching error, popup would be better
    
            }//end try catch
            
            ScriptRunner ();
            HashingPreviousPasswords();
        
        } else {

                JOptionPane.showMessageDialog(null, " It Seems the DB been created already... please start the Program.");
                NewJFrame.main(null);

            }
    }
    
    public static void ScriptRunner () {
        
        try {
            
            DriverManager.registerDriver(new org.sqlite.JDBC());
            Connection conn = DriverManager.getConnection(url);
            ScriptRunner runner = new ScriptRunner(conn, false, false);
            Reader reader =  new BufferedReader(new FileReader("src/GraphicalUserInterface/SQLTemplate.sql"));
            runner.runScript(reader);
        
        }
        
        catch (Exception e) {
            System.out.println(e);
        }

    }
    
    public static void HashingPreviousPasswords(){
        try (Connection conn = DriverManager.getConnection(url)) {// using url establish a connection with the database 
          String selectQuery = "SELECT UserID, password FROM UserDetails";// get users IDs and passwords
          String updateQuery = "UPDATE UserDetails SET password = ? WHERE UserID = ?";// update them

        
        try (PreparedStatement selectStatment= conn.prepareStatement(selectQuery);//prepare sql statement for selecting.
             PreparedStatement updateStatment= conn.prepareStatement(updateQuery);  //prepare sql statement for updating
             ResultSet rs = selectStatment.executeQuery()){// excute them to recive the current passwords storing in rs(resultset).
                
             while(rs.next()){//loops through each record to acess the user id and password
                 // get the user ids and passwords from current record/ resultset
                 String userid = rs.getString("UserID");
                 String TextPasswords=rs.getString("Password");
                 
                 //ckeck if the passwors hashed or start with hashing characters if not it will hashe them
                 if(!TextPasswords.startsWith("$2a$")){
                    String hashedpassword=BCrypt.hashpw(TextPasswords, BCrypt.gensalt());
                 
                    // update the old text passwords to the new hashed passwords to the correct user id
                    updateStatment.setString(1,hashedpassword);
                    updateStatment.setString(2,userid);
                    updateStatment.executeUpdate();// run updates on the database to replace the hashed passwords
                 
                 }
             }
             JOptionPane.showMessageDialog(null, "all existing passwors are hashed successfully! ");
             
             }catch (SQLException e){// throw an sql related exception
                     JOptionPane.showMessageDialog(null,"error with hashing the passwords");
                     e.printStackTrace();
                     
               }
                
           }catch (SQLException e){// throw an connection related exception
               JOptionPane.showMessageDialog(null,"Connection gone wrong" + e.getMessage());
           
           }
    }
      
     public static void login() {
        
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/DB/DB.db")){  

            String UserID = getID.getText(); 
            String UserPassWord = getpassword.getText();
            
            PreparedStatement ID = conn.prepareStatement("select * from UserDetails where UserID=?");  
            PreparedStatement PassWord = conn.prepareStatement("select * from UserDetails where Password=?");  
            ID.setString(1, UserID); 
            PassWord.setString(1, UserPassWord); 
            //Excuting Query  
            ResultSet rsForID = ID.executeQuery(); 
            ResultSet rsForPassWord = PassWord.executeQuery(); 

            if (rsForID.next() && rsForPassWord.next()) {
                
                //CustomerFirstFrame.
                Menu.main(null);
                
            } 
            
            else {  
                
                JOptionPane.showMessageDialog(null, "Plases Insert Correct information!"); 
                login.main(null);
           
            } 
        } 
        
        catch (Exception e) { 
            
            JOptionPane.showMessageDialog(null,(e.getMessage())); //error from try
            
            JOptionPane.showMessageDialog(null, "It Seems You Haven`t populate The Data. Please go back and Populate The Database");
                
                
                    
                    System.exit(0);
                    
                }
                
                
        }//end try catch

     private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean isValidEmail() {
        
        String email = Registration.jTextField5.getText();
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public static void check() {
        if (!isValidEmail()) {
            JOptionPane.showMessageDialog(null, "Error: Email address must contain '@' and '.' characters");
        }
    }
    
     
    public static void AddNewCustomer() {
    String AddNewCustomer_SQL = "INSERT INTO UserDetails (UserID, FirstName, SecondName, DOB, PassportNO, Email, Password, PhoneNo, Country, PostCode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String ADD_CREDITCARD_SQL = "INSERT INTO UserBankCardDetails "
        + "(UserBankCardNumber, CardHolderName, UserCardExpiryDate,UserCardVerificationValue, UserID) VALUES (?, ?, ?, ?, ?)";
    
    
    String UserID = Registration.jTextField10.getText();
    String FirstName = Registration.jTextField1.getText();
    String SecondName = Registration.jTextField2.getText();
    String DOB = Registration.getyear.getText() + "-" + Registration.getmounth.getText() + "-" +
               Registration.getday.getText();
    String passportNumber = Registration.jTextField4.getText();
    String Email = Registration.jTextField5.getText();
    String password = Registration.jTextField9.getText();
    String hashedpassword=BCrypt.hashpw(password, BCrypt.gensalt());
    String phoneNo = Registration.jTextField7.getText();
    String Country = Registration.jTextField8.getText();
    String PostCode = Registration.jTextField6.getText();
    String UserBankCardNumber = Registration.jTextField3.getText();
    String CardHolderName = Registration.jTextField11.getText();
    String UserCardExpiryDate= Registration.jTextField12.getText() + "-" + Registration.jTextField13.getText() + "-" +
                Registration.jTextField14.getText();
     String UserCardVerificationValue = Registration.jTextField15.getText();

    try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/DB/DB.db")) {
        // Check if the UserDetails table exists
        String CHECK_TABLE_SQL = "SELECT name FROM sqlite_master WHERE type='table' AND name='UserDetails';";
        PreparedStatement checkTable = connection.prepareStatement(CHECK_TABLE_SQL);
        ResultSet tableResult = checkTable.executeQuery();

        if (!tableResult.next()) {
            JOptionPane.showMessageDialog(null, "The UserDetails table doesn't exist in the database.");
            return;
        }

            // Check if the User exists
            String CHECK_USER_SQL = "SELECT * FROM UserDetails WHERE UserID = ?";
            PreparedStatement checUser = connection.prepareStatement(CHECK_USER_SQL);
            checUser.setString(1, UserID);
            ResultSet userResult = checUser.executeQuery();
            
            
            String CHECK_CreditCard_SQL = "SELECT * FROM UserBankCardDetails WHERE UserBankCardNumber = ?";
            
            PreparedStatement checCard = connection.prepareStatement(CHECK_CreditCard_SQL);
            checCard.setString(1, UserBankCardNumber);
            ResultSet CardResult = checCard.executeQuery();
            
            if (CardResult.next()) {
                
                JOptionPane.showMessageDialog(null, "The Card with number " + UserBankCardNumber + " Already exist in the DB.");
                return;
                
            }
            
            else if (userResult.next()) {
                
                JOptionPane.showMessageDialog(null, "The user with id " + UserID + " already exist in the database.");
                return;
                
            }
           
            
            else {
            if (Registration.jTextField10.getText().isEmpty() || Registration.jTextField1.getText().isEmpty() ||
                Registration.jTextField2.getText().isEmpty() || Registration.getyear.getText().isEmpty() ||
                Registration.jTextField4.getText().isEmpty() || Registration.jTextField5.getText().isEmpty() ||
                Registration.jTextField6.getText().isEmpty() || Registration.jTextField7.getText().isEmpty() ||
                Registration.jTextField8.getText().isEmpty() || Registration.jTextField9.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            } else {
                PreparedStatement preparedStatement = connection.prepareStatement(AddNewCustomer_SQL);
                preparedStatement.setString(1, UserID);
                preparedStatement.setString(2, FirstName);
                preparedStatement.setString(3, SecondName);
                preparedStatement.setString(4, DOB);
                preparedStatement.setString(5, passportNumber);
                preparedStatement.setString(6, Email);
                preparedStatement.setString(7, hashedpassword);
                preparedStatement.setString(8, phoneNo);
                preparedStatement.setString(9, Country);
                preparedStatement.setString(10, PostCode);
                
                
                
                PreparedStatement preparedStatement1 = connection.prepareStatement(ADD_CREDITCARD_SQL); 

                preparedStatement1.setString(1, UserBankCardNumber);
                preparedStatement1.setString(2, CardHolderName);
                preparedStatement1.setString(3, UserCardExpiryDate);
                preparedStatement1.setString(4, UserCardVerificationValue);
                preparedStatement1.setString(5, UserID);
                    
                    preparedStatement1.executeUpdate();
                    preparedStatement.executeUpdate();

                 
                JOptionPane.showMessageDialog(null, "New user with ID " + UserID + " has been added to the database.");
               jTextField10.setText(null);
                Registration.getyear.setText(null);
                Registration.getmounth.setText(null);
                Registration.getday.setText(null);
                Registration.jTextField1.setText(null);
                Registration.jTextField2.setText(null);
                Registration.jTextField4.setText(null);
                Registration.jTextField5.setText(null);
                Registration.jTextField9.setText(null);
                Registration.jTextField7.setText(null);
                Registration.jTextField8.setText(null);
                Registration.jTextField6.setText(null);
                Registration.jTextField3.setText(null);
                Registration.jTextField11.setText(null);
                Registration.jTextField12.setText(null);
                Registration.jTextField13.setText(null);
                Registration.jTextField14.setText(null);
                Registration.jTextField15.setText(null);
            }
        }
    } catch (SQLException e) {
        System.out.println(e);
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}
    
public static void updateUserDetails() {
    try (Connection conn = DriverManager.getConnection(url)) {
        Statement statement = conn.createStatement();
        
        String password = EditUserAccount.jTextField5.getText();
        String hashedpassword=BCrypt.hashpw(password, BCrypt.gensalt());
     String updateQuery = "UPDATE UserDetails SET " +
                "FirstName='" + EditUserAccount.jTextField2.getText() + "', " +
                "SecondName='" + EditUserAccount.jTextField3.getText() + "', " +
                "DOB='" + EditUserAccount.jTextField4.getText() + "-" +  EditUserAccount.jTextField11.getText() + "-" +
                EditUserAccount.jTextField12.getText() + "', " +
                "PassportNO='" + EditUserAccount.jTextField10.getText() + "', " +
                "Email='" + EditUserAccount.jTextField5.getText() + "', " +
                "Password='" + hashedpassword+ "', " +
                "PhoneNO='" + EditUserAccount.jTextField7.getText() + "', " +
                "Country='" + EditUserAccount.jTextField8.getText() + "', " + 
                "PostCode='" + EditUserAccount.jTextField9.getText() + "' " +
                "WHERE UserID='" + EditUserAccount.jTextField1.getText() + "'";
     
        

        statement.executeUpdate(updateQuery);

        
        JOptionPane.showMessageDialog(null, "Record updated for the User ID " + EditUserAccount.jTextField1.getText());
        statement.close();
        conn.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}

 
public static void searchUserDetails() {
    String UserID = EditUserAccount.jTextField1.getText();  
    String query = "SELECT * FROM UserDetails WHERE UserID = ?";
    
    try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/DB/DB.db");
         PreparedStatement statement = connection.prepareStatement(query)) {
        
        statement.setString(1, UserID);
        ResultSet resultSet = statement.executeQuery();
        
        if (resultSet.next()) {
            String Userid = resultSet.getString(1);
            String FirstName = resultSet.getString(2);
            String SecondName = resultSet.getString(3);
            String DOB = resultSet.getString(4); // assign DOB to date variable
            String PassportNumber = resultSet.getString(5);
            String Email = resultSet.getString(6);
            String Password = resultSet.getString(7);
            String PhoneNO = resultSet.getString(8);
            String Country = resultSet.getString(9);
            String PostCode = resultSet.getString(10);
            
            // Split DOB into year, month, and day values
            String[] stringarray = DOB.split("-");
            String year = stringarray[0];
            String month = stringarray[1];
            String day = stringarray[2];
            
            // Set records in text fields
            EditUserAccount.jTextField1.setText(Userid);
            EditUserAccount.jTextField2.setText(FirstName);
            EditUserAccount.jTextField3.setText(SecondName);
            //EditUserAccount.jTextField4.setText(year + "-" + month + "-" + day); // set date value
            
            EditUserAccount.jTextField4.setText(stringarray[0]);
            EditUserAccount.jTextField11.setText(stringarray[1]);
            EditUserAccount.jTextField12.setText(stringarray[2]);
            EditUserAccount.jTextField10.setText(PassportNumber);
            EditUserAccount.jTextField5.setText(Email);
            EditUserAccount.jTextField6.setText(Password);
            EditUserAccount.jTextField7.setText(PhoneNO);
            EditUserAccount.jTextField8.setText(Country);
            EditUserAccount.jTextField9.setText(PostCode);
            
        } else {
            JOptionPane.showMessageDialog(null, "No results found for User ID " + UserID);
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}
 

 
 
public static void deleteUserDetails() {
    String userID = EditUserAccount.jTextField1.getText();
    String query = "DELETE FROM UserDetails  WHERE UserID=?";

    try (Connection conn = DriverManager.getConnection(url);
         PreparedStatement statement = conn.prepareStatement(query);
         PreparedStatement st1 = conn.prepareStatement("SELECT * FROM UserBankCardDetails WHERE UserID=?")) {

        st1.setString(1, userID);
        ResultSet rs1 = st1.executeQuery();

        if (!rs1.next()) {
            
            JOptionPane.showMessageDialog(null, "No User found with number " + userID + ".");
        } else {
          if(EditUserAccount.jTextField1.getText().isEmpty() == true || EditUserAccount.jTextField2.getText().isEmpty() == true
                            || EditUserAccount.jTextField3.getText().isEmpty() == true || EditUserAccount.jTextField4.getText().isEmpty() == true
                            || EditUserAccount.jTextField11.getText().isEmpty() == true || EditUserAccount.jTextField12.getText().isEmpty() == true
                            || EditUserAccount.jTextField10.getText().isEmpty() == true || EditUserAccount.jTextField5.getText().isEmpty() == true
                            || EditUserAccount.jTextField6.getText().isEmpty() == true ||  EditUserAccount.jTextField7.getText().isEmpty() == true
                            || EditUserAccount.jTextField8.getText().isEmpty() == true ||   EditUserAccount.jTextField9.getText().isEmpty()==true) {

                            JOptionPane.showMessageDialog(null, "Fields can not be empty!"); //let user know

                
            } else {
                statement.setString(1, userID);
                int numRowsAffected = statement.executeUpdate();
                if (numRowsAffected == 0) {
                    JOptionPane.showMessageDialog(null, "No rows deleted!");
                } else {
                    JOptionPane.showMessageDialog(null, "The user with UserID " + userID + " has been deleted Thanks for using our Service!");
                   System.exit(0);
                }
                
                
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}
      
 
public static void addNewFlightBooking() {
    String addNewFlightBookingSQL = "INSERT INTO BookingReffrence (BookingReffrenceNumber, UserID, FlightNumber) VALUES (?, ?, ?)";

    String bookingReferenceNumber = FlightPurchase.jTextField7.getText();
    String userID = FlightPurchase.jTextField9.getText();
    String flightNumber = FlightPurchase.jTextField6.getText();

    try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/DB/DB.db")) {
        // Check if the FlightDetails table exists
        String checkBookingReffrenceSQL = "SELECT * FROM BookingReffrence WHERE BookingReffrenceNumber = ?";
        PreparedStatement checkBookingReffrence = connection.prepareStatement(checkBookingReffrenceSQL);
        checkBookingReffrence.setString(1, bookingReferenceNumber);
        ResultSet checkBookingReffrenceResult = checkBookingReffrence.executeQuery();
        
        // Check if the FlightNumber already exists
        String checkFlightSQL = "SELECT * FROM FlightDetails WHERE FlightNumber = ?";
        PreparedStatement checkFlight = connection.prepareStatement(checkFlightSQL);
        checkFlight.setString(1, flightNumber);
        ResultSet flightResult = checkFlight.executeQuery();

        if (checkBookingReffrenceResult.next()) {
            FlightPurchase.main(null);
            JOptionPane.showMessageDialog(null, "The Booking Reffrence Number already exist in the database Please choose new Booking ID.");
            return;
        }
        
        else if (!flightResult.next()) {
            FlightPurchase.main(null);
            JOptionPane.showMessageDialog(null, "Flight with ID " + flightNumber + " doesn't exist in the database. Please choose another Flight Number.");
            return;
        }

        // Check if BookingReferenceNumber, UserID, and FlightNumber are not empty
        else if (bookingReferenceNumber.isEmpty() || userID.isEmpty() || flightNumber.isEmpty()) {
            FlightPurchase.main(null);
            JOptionPane.showMessageDialog(null, "All fields are required. Please fill in all fields before adding a new flight booking.");
            return;
        }
        
        else {
            
            // Insert new BookingReferenceNumber into BookingReference table
            PreparedStatement preparedStatement = connection.prepareStatement(addNewFlightBookingSQL);
            preparedStatement.setString(1, bookingReferenceNumber);
            preparedStatement.setString(2, userID);
            preparedStatement.setString(3, flightNumber);


            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(null, "New flight booking with ID " + bookingReferenceNumber + " has been added...Please proceed the Payment.");

            GraphicalUserInterface.BankDetail.main(null); // It's not clear why this line is here, but I kept it as it is.
            
            
            
        }

    } catch (SQLException e){
        System.out.println(e);
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}


 
        
 
  public static void PurchaseBtn() {
           if( BankDetail.jTextField1.getText().isEmpty() == true ||   BankDetail.jTextField2.getText().isEmpty() == true
                            ||   BankDetail.jTextField3.getText().isEmpty() == true ||   BankDetail.jTextField4.getText().isEmpty() == true
                            ||   BankDetail.jTextField5.getText().isEmpty() == true)  {

                          
                            JOptionPane.showMessageDialog(null,"PLEASE FILL EMPTY BOX..");
                           GraphicalUserInterface.BankDetail.main(null);
            }
          
          else{
               JOptionPane.showMessageDialog(null, "You`ve purchase The flight Ticket Successfully thanks for trusting us!");
               GraphicalUserInterface.Menu.main(null);
          }
        }
  
  
  
   public static void searchBankDetail() {
    String UsersID = BankDetail.jTextField5.getText();
    String query = "SELECT * FROM UserBankCardDetails WHERE  UserID = ?";
    
    try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/DB/DB.db");
         PreparedStatement statement = connection.prepareStatement(query)) {
        
        statement.setString(1,  UsersID);
        ResultSet resultSet = statement.executeQuery();
        
        if (resultSet.next()) {
            
            String UserBankCardNumber  = resultSet.getString(1);
            String  CardHolderName = resultSet.getString(2);
            String  UserCardExpiryDate = resultSet.getString(3);
            String  UserCardVerificationValue = resultSet.getString(4);
            String  UserID = resultSet.getString(5);
            
            
            
            
            // Set records in text fields
            BankDetail.jTextField1.setText(UserBankCardNumber);
            BankDetail.jTextField2.setText(CardHolderName);
            BankDetail.jTextField3.setText(UserCardExpiryDate);
            BankDetail.jTextField4.setText(UserCardVerificationValue);
            BankDetail.jTextField5.setText(UserID);
          
            
        } 
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}

  public static void CancelBookingReffrence() {
    String BookingReffrenceNumber = BookingReffrence.jTextField1.getText();
    String query = "DELETE FROM BookingReffrence WHERE BookingReffrenceNumber = ?";

    try (Connection conn = DriverManager.getConnection(url);
         PreparedStatement statement = conn.prepareStatement(query)) {
        statement.setString(1, BookingReffrenceNumber);

        int rowsDeleted = statement.executeUpdate();

        if (rowsDeleted == 0) {
            JOptionPane.showMessageDialog(null, "No Flight Booking found with number " + BookingReffrenceNumber + ".");
        } else {
            JOptionPane.showMessageDialog(null, "The Flight Booking with number " + BookingReffrenceNumber + " has been deleted!");
             BookingReffrence.jTextField1.setText(null);
             BookingReffrence.jTextField2.setText(null);
             BookingReffrence.jTextField3.setText(null);
             BookingReffrence.jTextField1.setEnabled(true);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
        JOptionPane.showMessageDialog(null, "It Seems You Haven`t populate The Data. Please go back and Populate The Database.");

       
            System.exit(0);
        
    }
}
 public static void UpdateBookingReffrence() {
   String FlightNumber = BookingReffrence.jTextField3.getText();

   try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/DB/DB.db")) {
         // Check if the FlightNumber already exists
        String CHECK_FLIGHT_SQL = "SELECT * FROM FlightDetails WHERE FlightNumber = ?";
        PreparedStatement checkFlight = connection.prepareStatement(CHECK_FLIGHT_SQL);
        checkFlight.setString(1, FlightNumber);
        ResultSet flightResult = checkFlight.executeQuery();

       

    if(flightResult.next()) {
            
        String updateQuery = "UPDATE BookingReffrence SET " +
                "FlightNumber='" + BookingReffrence.jTextField3.getText() + "' " +
                "WHERE BookingReffrenceNumber='" + BookingReffrence.jTextField1.getText() + "'";
        
        
        Statement statement = connection.createStatement();
        statement.executeUpdate(updateQuery);
        JOptionPane.showMessageDialog(null, "You`ve Updated Your Flight Number");
             BookingReffrence.jTextField1.setText(null);
             BookingReffrence.jTextField2.setText(null);
             BookingReffrence.jTextField3.setText(null);
             BookingReffrence.jTextField1.setEnabled(true);
             
             
             
        } else {
       
        JOptionPane.showMessageDialog(null, "Flight with id " + FlightNumber+ " doesn`t exists in the database Please Choose another Flight Number.");
             BookingReffrence.jTextField1.setText(null);
             BookingReffrence.jTextField2.setText(null);
             BookingReffrence.jTextField3.setText(null);
            return;
           
        }
 
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
        JOptionPane.showMessageDialog(null, "It Seems You Haven`t populate The Data. Please go back and Populate The Database.");

       
            System.exit(0);
    }
 }
 
 public static void searchBookingReffrence() {
    String BookingReffrencee = BookingReffrence.jTextField1.getText();
    String query = "SELECT * FROM  BookingReffrence WHERE  BookingReffrenceNumber = ?";
    
    try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/DB/DB.db");
         PreparedStatement statement = connection.prepareStatement(query)) {
        
        statement.setString(1, BookingReffrencee);
        ResultSet resultSet = statement.executeQuery();
        
        if (resultSet.next()) {
            String BookingReffrenceNumber = resultSet.getString(1);
            String UserID = resultSet.getString(2);
            String FlightNumber = resultSet.getString(3);
            
            
            
            
            
            // Set records in text fields
            BookingReffrence.jTextField1.setText(BookingReffrenceNumber);
            BookingReffrence.jTextField2.setText(UserID);
            BookingReffrence.jTextField3.setText(FlightNumber);
            
            BookingReffrence.jTextField1.setEnabled(false);
            
        } else {
            JOptionPane.showMessageDialog(null, "No results found for BookingReffrenceNumber" + BookingReffrencee);
            BookingReffrence.jTextField1.setText(null);
             BookingReffrence.jTextField2.setText(null);
             BookingReffrence.jTextField3.setText(null);
           
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}
 
 public static void ViewFlight() {
        
        DefaultTableModel tm = (DefaultTableModel) ViewFlight.jTable1.getModel();
        
        try (Connection conn = DriverManager.getConnection(url)) {
            
            //Statement – Used to execute string-based SQL queries
            Statement stmt = conn.createStatement();
            ResultSet FlightDetails = stmt.executeQuery("SELECT * FROM FlightDetails "); //get everything from table, result set
            tm.setRowCount(0);
            while(FlightDetails.next()){ //FlightDetails is result set, use while to iterate through it
                
                tm.addRow(new Object[]{FlightDetails.getString("FlightNumber"), 
                                       FlightDetails.getString("FlightDestination"), 
                                       FlightDetails.getString("FlightFrom"), 
                                       FlightDetails.getString("FlightDepartureDate"), 
                                       FlightDetails.getString("FlightReturnDate"),
                                       FlightDetails.getString("Passenger"), 
                                       FlightDetails.getString("SeatNumber")});
                
            }//end while
            
        } catch (SQLException e) {
            
            JOptionPane.showMessageDialog(null,(e.getMessage())); //if there is an error
            
        }//end try catch
    }
 
  public static void searchFlightDetails() {
    String flightNumber = FlightPurchase.jTextField6.getText();  
    String query = "SELECT * FROM FlightDetails WHERE FlightNumber = ?";
    
    try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/DB/DB.db");
         PreparedStatement statement = connection.prepareStatement(query)) {
        
        statement.setString(1, flightNumber);
        ResultSet resultSet = statement.executeQuery();
        
        if (resultSet.next()) {
            String FlightNumber = resultSet.getString(1);
            String destination = resultSet.getString(2);
            String from = resultSet.getString(3);
            String departureDate = resultSet.getString(4);
            String returnDate = resultSet.getString(5);
            String passenger = resultSet.getString(6);
            String seatNumber = resultSet.getString(7);
            
            FlightPurchase.jTextField6.setEnabled(false);
            

            
            // Set records in text fields
            FlightPurchase.jTextField6.setText(FlightNumber);
            FlightPurchase.jTextField1.setText(destination);
            FlightPurchase.jTextField3.setText(from);
            FlightPurchase.jTextField2.setText(departureDate);
            FlightPurchase.jTextField8.setText(returnDate);
            FlightPurchase.jTextField4.setText(passenger);
            FlightPurchase.jTextField5.setText(seatNumber);
            
        } else {
            JOptionPane.showMessageDialog(null, "No results found for Flight Number " + flightNumber);
            FlightPurchase.jTextField6.setEnabled(true);
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}
}


         


                
