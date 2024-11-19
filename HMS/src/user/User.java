package user;
import java.io.IOException;
import java.util.Scanner;
import administrator.*;
import patient.*;
import pharmacist.*;
import doctor.*;
import main.*;

public abstract class User {

    //ASSIGNING ROLES BASED ON LOGIN DETAILS
    
    protected String hospitalID;
    protected String name;

    public User(String hospitalID) {

        this.hospitalID = hospitalID;
    }

    //ABSTRACT//
    public abstract void displayMenu();


    //GETTER//
    public String getHospitalID() {
        return hospitalID;
    }

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

    public static String getRoleUsingID(String hospitalID)
    {
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

    

    //SETTER//
    

    //CONSTRUCTOR//
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
                return new Administrator(hospitalID,false);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }




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

        if (currentUser.isMustChangeUser())
        {
            System.out.println("You must change your Username:");
            String newUsername;

            do {
                System.out.print("Enter new username: ");
                newUsername = sc.nextLine();

            } while (!validUser(newUsername, currentUser));
            System.out.println("Password changed successfully.");

        }

        return assignRole(currentUser.getHospitalID());
    }


    protected static boolean validPass(String newPassword, UserCredentials currentUser) {
        if (newPassword.length() >= 8) {
            currentUser.setPassword(newPassword);
            currentUser.setMustChangePass(false);
            return true;
        } else {
            System.out.println("Password must be at least 8 characters long.");
            return false;
        }
    }

    protected static boolean validUser(String newUsername, UserCredentials currentUser)
    {
        if (newUsername.length() >= 8) {
            currentUser.setUsername(newUsername);
            return true;
        }
        else {
            System.out.println("Username must be at least 8 characters long.");
            return false;
        }

    }



}
