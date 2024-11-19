
package user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import utility.*;
import administrator.*;

/**
 * Manages user credentials for login and data manipulation.
 * Handles operations like authentication, password updates, and username updates.
 * @author Jan Chen Jie
 * @version 1.0
 * @since 19/11/2024
 */
public class UserCredentials {

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The hospital ID associated with the user.
     */
    private String hospitalID;

    /**
     * Indicates if the user must change their password.
     */
    private boolean mustChangePassword;

    /**
     * Indicates if the user must change their username.
     */
    private boolean mustChangeUser;

    /**
     * File paths for user and secret user data.
     */
    private static final String FILE_PATH1 = "./User_List/User_List.csv";
    private static final String SECRET_PATH = "./User_Secret_List/User_Secret_List.csv";

    /**
     * Creates a new UserCredentials object with the given hospital ID.
     * @param hospitalID The hospital ID of the user.
     */
    public UserCredentials(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    // GETTERS //

    /**
     * Checks if the user must change their password.
     * @return True if the password must be changed, otherwise false.
     */
    public boolean isMustChangePass() {
        return mustChangePassword;
    }

    /**
     * Checks if the user must change their username.
     * @return True if the username must be changed, otherwise false.
     */
    public boolean isMustChangeUser() {
        return mustChangeUser;
    }

    /**
     * Gets the hospital ID of the user.
     * @return The hospital ID.
     */
    public String getHospitalID() {
        return hospitalID;
    }

    /**
     * Retrieves the list of user credentials.
     * @param user The user requesting the data.
     * @return A list of user credentials.
     * @throws SecurityException If the user does not have the required access.
     */
    public static List<String[]> getUser(User user) {
        if (user instanceof Administrator) return loadUser();
        else throw new SecurityException("Access denied");
    }

    // SETTERS //

    /**
     * Sets the user's password and updates the CSV file.
     * @param newPassword The new password to set.
     */
    public void setPassword(String newPassword) {
        updatePasswordCSV(newPassword);
    }

    /**
     * Sets the user's username and updates the CSV file.
     * @param newUsername The new username to set.
     */
    public void setUsername(String newUsername) {
        updateUsernameCSV(newUsername);
    }

    /**
     * Sets whether the user must change their password.
     * @param mustChangePassword True if the password must be changed, otherwise false.
     */
    public void setMustChangePass(boolean mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }

    /**
     * Sets whether the user must change their username.
     * @param mustChangeUser True if the username must be changed, otherwise false.
     */
    public void setMustChangeUser(boolean mustChangeUser) {
        this.mustChangeUser = mustChangeUser;
    }

    // PRIVATE METHODS //

    /**
     * Updates the username in the CSV file.
     * @param newUsername The new username to set.
     */
    private void updateUsernameCSV(String newUsername) {
        List<String[]> allUsers = loadUser();
        for (String[] userRow : allUsers) {
            if (userRow[0].equals(this.hospitalID)) {
                userRow[1] = newUsername;
                userRow[4] = "FALSE";
                writeUser(allUsers, User.assignRole(this.hospitalID));
            }
        }
    }

    /**
     * Updates the password in the CSV file.
     * @param newPassword The new password to set.
     */
    private void updatePasswordCSV(String newPassword) {
        List<String[]> allUsers = loadUser();
        for (String[] userRow : allUsers) {
            if (userRow[0].equals(this.hospitalID)) {
                userRow[2] = newPassword;
                userRow[3] = "FALSE";
                writeUser(allUsers, User.assignRole(this.hospitalID));
            }
        }
    }

    // PUBLIC METHODS //

    /**
     * Authenticates a user with the given username and password.
     * @param inputUsername The username to authenticate.
     * @param inputPassword The password to authenticate.
     * @return The authenticated user object, or null if authentication fails.
     */
    public static User authenticate(String inputUsername, String inputPassword) {
        List<String[]> secretUsers = loadSecretUsers();

        for (String[] userRow : secretUsers) {
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

            if (username.equals(inputUsername) && password.equals(inputPassword)) {
                UserCredentials currentUser = new UserCredentials(hospitalID);
                currentUser.mustChangePassword = mustChangePassword;
                currentUser.mustChangeUser = mustChangeUser;
                return User.login(currentUser);
            }
        }
        System.out.println("Authentication failed: Invalid User");
        return null;
    }

    // PRIVATE UTILITY METHODS //

    /**
     * Loads the list of user credentials from the CSV file.
     * @return A list of user credentials.
     */
    private static List<String[]> loadUser() {
        List<String[]> allUsers = new ArrayList<>();
        try {
            allUsers = Utility.readCSV(FILE_PATH1, 0);
        } catch (IOException e) {
            System.out.println("Error in loadUser: " + e.getMessage());
        }
        return allUsers;
    }

    /**
     * Loads the list of secret user credentials from the CSV file.
     * @return A list of secret user credentials.
     */
    private static List<String[]> loadSecretUsers() {
        List<String[]> secretUser = new ArrayList<>();
        try {
            secretUser = Utility.readCSV(SECRET_PATH, 1);
        } catch (IOException e) {
            System.out.println("Error in loadSecretUsers: " + e.getMessage());
        }
        return secretUser;
    }

    /**
     * Writes the list of user credentials to the CSV file.
     * @param data The list of user credentials to write.
     */
    private static void writeCSV(List<String[]> data) {
        try {
            Utility.writeCSV(FILE_PATH1, data);
        } catch (IOException e) {
            System.out.println("Error in Writing User: " + e.getMessage());
        }
    }

    /**
     * Writes user credentials to the CSV file, ensuring valid user access.
     * @param data The list of user credentials to write.
     * @param user The user performing the operation.
     */
    public static void writeUser(List<String[]> data, User user) {
        writeCSV(data);
    }
}
