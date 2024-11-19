
package main;

import java.io.IOException;
import java.util.Scanner;
import user.*;

/**
 * The entry point for the Hospital Management System.
 * Handles user authentication and directs authenticated users to their respective menus.
 * @author Jan Chen Jie
 * @version 1.0
 * @since 19/11/2024
 */
public class Main {

    /**
     * The main method to start the Hospital Management System.
     * Prompts the user for credentials and authenticates them.
     * @param args Command-line arguments.
     * @throws IOException If an I/O error occurs during the operation.
     */
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to the Hospital Management System");

            System.out.println("Username:");
            String username = sc.nextLine();

            System.out.println("Password:");
            String password = sc.nextLine();

            // Authenticate user credentials
            User user = UserCredentials.authenticate(username, password);
            if (user != null) {
                // Display the menu for the authenticated user
                user.displayMenu();
            }
        }
    }
}
