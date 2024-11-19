package user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utility.*;
import administrator.*;

public class UserCredentials {

    // ANYTHING RELATED TO DATA MANUPALATION IN LOGIN DETAILS
    private String username;
    private String password;
    private String hospitalID;
    private boolean mustChangePassword;
    private boolean mustChangeUser;

    private static final String FILE_PATH1 = "C:/Users/mingh/eclipse-workspace/HMS/User_List/User_List.csv";
    private static final String SECRET_PATH = "C:/Users/mingh/eclipse-workspace/HMS/User_Secret_List/User_Secret_List.csv";


    public UserCredentials(String hospitalID) {
        this.hospitalID = hospitalID;

    }


    //GETTER

    public boolean isMustChangePass()
    {
        return mustChangePassword;
    }

    public boolean isMustChangeUser()
    {
        return mustChangeUser;
    }

    public String getHospitalID()
    {
        return hospitalID;
    }

    public static List<String[]> getUser(User user)
    {
        if (user instanceof Administrator) return loadUser();
        
        else throw new SecurityException("Access denied");
    }



    //SETTER
    public void setPassword(String newPassword) {
        updatePasswordCSV(newPassword);
    }

    public void setUsername(String newUsername)
    {
        updateUsernameCSV(newUsername);
    }

    public void setMustChangePass(boolean mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }

    public void setMustChangeUser(boolean mustChangePassword) {
        this.mustChangeUser = mustChangeUser;
    }



    private void updateUsernameCSV(String newUsername)
    {
        List<String[]> allUsers = loadUser();
        for (String[] userRow : allUsers) {
            if (userRow[0].equals(this.hospitalID)) {
                userRow[1] = newUsername;
                userRow[4] = "FALSE";
                writeUser(allUsers, User.assignRole(this.hospitalID));
            } 
        }

    }

    private void updatePasswordCSV(String newPassword)
    {
        List<String[]> allUsers = loadUser();
        for (String[] userRow : allUsers) {
            if (userRow[0].equals(this.hospitalID)) {
                userRow[2] = newPassword;
                userRow[3] = "FALSE";
                writeUser(allUsers,User.assignRole(this.hospitalID));
            } 
        }

    }


    public static User authenticate(String inputUsername, String inputPassword) {

        List<String[]> SecretUsers = loadSecretUsers();

        for (String[] userRow : SecretUsers)
        {
            String SECRET_USER = userRow[0];
            String SECRET_PASS = userRow[1];

            if (SECRET_USER.equals(inputUsername) && SECRET_PASS.equals(inputPassword)) {
                return new Administrator(SECRET_USER, true);        
            }
           
        }

        List<String[]> allUsers = loadUser();

        for (String[] userRow : allUsers) {
            

            String hospitalID = userRow[0];
            String username = userRow[1];
            String password = userRow[2];
            boolean mustChangePassword = Boolean.parseBoolean(userRow[3]);
            boolean mustChangeUser = Boolean.parseBoolean(userRow[4]);
            

            if (username.equals(inputUsername) && password.equals(inputPassword))
            {
                UserCredentials currentUser = new UserCredentials(hospitalID);
                currentUser.mustChangePassword = mustChangePassword;
                currentUser.mustChangeUser = mustChangeUser;
                return User.login(currentUser);
            }

        }
        System.out.println("Authentication failed: Invalid User");
        return null;
    }

    private static List<String[]> loadUser() 
    {
        List<String[]> allUsers = new ArrayList<>();
        try{
            allUsers = Utility.readCSV(FILE_PATH1, 0);
        } 
        catch (IOException e)
        {
            System.out.println("Error in loadUser: " + e.getMessage());

        }
        return allUsers;

    }

    private static List<String[]> loadSecretUsers()
    {
        List<String[]> secretUser = new ArrayList<>();
        try{
            secretUser = Utility.readCSV(SECRET_PATH, 1);
        }
        catch (IOException e)
        {
            System.out.println("Error in loadUser: " + e.getMessage());
        }
        return secretUser;
    }

    private static void writeCSV(List<String[]>data)
    {
        try{
            Utility.writeCSV(FILE_PATH1, data);
        }
        catch(IOException e)
        {
            System.out.println("Error in Writing User: " + e.getMessage());
        }
    }


    public static void writeUser(List<String[]>data, User user)
    {
        if (user instanceof Administrator) writeCSV(data);
    }


}
