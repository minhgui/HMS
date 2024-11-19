
package user;

import java.io.IOException;
import java.util.Scanner;
import administrator.*;
import patient.*;
import pharmacist.*;
import doctor.*;
import main.*;

/**
 * Represents a User in the hospital management system.
 * A User can be assigned various roles based on their credentials.
 * @author Jan Chen Jie
 * @version 1.0
 * @since 19/11/2024
 */
public abstract class User {

    /**
     * The unique ID of the User in the hospital system.
     */
    protected String hospitalID;

    /**
     * The name of the User.
     */
    protected String name;

    /**
     * Creates a new User with the given hospital ID.
     * @param hospitalID The unique ID assigned to the User.
     */
    public User(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    /**
     * Displays the menu for the User.
     * This method must be implemented by subclasses.
     */
    public abstract void displayMenu();

    /**
     * Gets the hospital ID of the User.
     * @return The hospital ID of the User.
     */
    public String getHospitalID() {
        return hospitalID;
    }

    /**
     * Gets the role prefix based on the given role name.
     * @param role The role name (e.g., "Doctor", "Administrator").
     * @return The role prefix or null if the role is invalid.
     */
    protected static String getRoleIndex(String role) {
        switch (role.toLowerCase()) {
            case "doctor":
                return "D0";
            case "pharmacist":
                return "P0";
            case "administrator":
                return "A0";
            case "patient":
                return "P1";
            default:
                return null; 
        }
    }

    /**
     * Gets the role name using the hospital ID prefix.
     * @param hospitalID The hospital ID to extract the role from.
     * @return The role name (e.g., "Pharmacist", "Patient").
     */
    public static String getRoleUsingID(String hospitalID) {
        String role = hospitalID.substring(0, 2);
        switch (role) {
            case "P0":
                return "Pharmacist"; 
            case "P1":
                return "Patient"; 
            case "D0":
                return "Doctor";
            case "A0":
                return "Administrator";
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }

    /**
     * Assigns a specific User role based on the hospital ID.
     * @param hospitalID The hospital ID to determine the role.
     * @return A User object corresponding to the assigned role.
     */
    protected static User assignRole(String hospitalID) {
        String role = hospitalID.substring(0, 2);
        switch (role) {
            case "P0":
                return new Pharmacist(hospitalID); 
            case "P1":
                return new Patient(hospitalID); 
            case "D0":
                return new Doctor(hospitalID);
            case "A0":
                return new Administrator(hospitalID, false);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }

    /**
     * Handles the login process for a User, validating and updating credentials if required.
     * @param currentUser The UserCredentials object containing the User's details.
     * @return A User object after successful login.
     */
    public static User login(UserCredentials currentUser) {
        Scanner sc = new Scanner(System.in);

        if (currentUser.isMustChangePass()) {
            System.out.println("You must change your password:");
            String newPassword;

            do {
                System.out.print("Enter new password: ");
                newPassword = sc.nextLine();
            } while (!validPass(newPassword, currentUser));

            System.out.println("Password changed successfully.");
        }

        if (currentUser.isMustChangeUser()) {
            System.out.println("You must change your Username:");
            String newUsername;

            do {
                System.out.print("Enter new username: ");
                newUsername = sc.nextLine();
            } while (!validUser(newUsername, currentUser));

            System.out.println("Username changed successfully.");
        }

        return assignRole(currentUser.getHospitalID());
    }

    /**
     * Validates the new password and updates it if valid.
     * @param newPassword The new password to validate.
     * @param currentUser The UserCredentials object of the current User.
     * @return True if the password is valid, otherwise false.
     */
    protected static boolean validPass(String newPassword, UserCredentials currentUser) {
        if (newPassword.length() >= 8) {
            currentUser.setPassword(newPassword);
            return true;
        } else {
            System.out.println("Password must be at least 8 characters long.");
            return false;
        }
    }

    /**
     * Validates the new username and updates it if valid.
     * @param newUsername The new username to validate.
     * @param currentUser The UserCredentials object of the current User.
     * @return True if the username is valid, otherwise false.
     */
    protected static boolean validUser(String newUsername, UserCredentials currentUser) {
        if (newUsername.length() >= 8) {
            currentUser.setUsername(newUsername);
            return true;
        } else {
            System.out.println("Username must be at least 8 characters long.");
            return false;
        }
    }
}
