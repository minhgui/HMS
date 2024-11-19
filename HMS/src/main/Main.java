package main;
import java.io.IOException;
import java.util.Scanner;
import user.*;


public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        while(true)
        {

            System.out.println("Welcome to the Hospital Management System");

            System.out.println("Username:");
            String username = sc.nextLine();

            System.out.println("Password:");
            String password = sc.nextLine();

            
                User user = UserCredentials.authenticate(username, password);
                if (user != null) {
                    user.displayMenu();
                    break;
                }
            
        }

    }



}
