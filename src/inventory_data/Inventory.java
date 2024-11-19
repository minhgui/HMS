
/* it manages MedicalEquipment
@author: Alvin Ong
@version: 1.0
@since: 19/11/2024
*/

package inventory_data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Inventory {

    private static final String CSV_FILE_PATH = "./Data/Medicine_List.csv";
    private static final String CSV_FILE_PATH2 = "./Data/Medical_Equipment_List.csv";
    /*this function adds new medical equipment into the csv file
    */
    public void addNewMedicalEquipment() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Adding a new medical equipment to the inventory...");

        System.out.print("Enter Equipment Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Initial Stock: ");
        int initialStock = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter Low Stock Level Alert: ");
        int lowStockLevelAlert = Integer.parseInt(scanner.nextLine());

        
        MedicalEquipment newEquipment = new MedicalEquipment(name, initialStock, lowStockLevelAlert);

        scanner.close();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH2, true))) {
            bw.write(newEquipment.toCsvFormat());
            bw.newLine();
            System.out.println("New medical equipment added successfully.");
        }

        catch (IOException e) {
        System.out.println("Error adding new medical equipment to file: " + e.getMessage());
        }
    }
    /* this method delete selected medical equipment from csv file
    */
    public void deleteMedicalEquipment() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the Equipment Name to remove from inventory: ");
        String equipmentNameToDelete = scanner.nextLine();

        List<String> updatedData = new ArrayList<>();
        boolean equipmentFound = false;

        scanner.close();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH2))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    updatedData.add(line);
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                String equipmentName = values[0];

                if (!equipmentName.equalsIgnoreCase(equipmentNameToDelete)) {
                    updatedData.add(line);
                } else {
                    equipmentFound = true;
                    System.out.println("Equipment '" + equipmentNameToDelete + "' has been deleted from the inventory.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            return;
        }

        if (!equipmentFound) {
            System.out.println("Equipment '" + equipmentNameToDelete + "' not found in the inventory.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH2))) {
            for (String dataLine : updatedData) {
                bw.write(dataLine);
                bw.newLine();
            }
        }

        catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }
    }
    /*this method view the mediccation, by read from csv file and present it to the user
    */
    public void viewMedicationInventory() {
        System.out.println("Viewing medication inventory...\n");
        System.out.printf("%-15s %-15s %-15s %-20s %-20s %-20s %-25s%n",
                "Medicine Name", "Initial Stock", "Current Stock",
                "Low Stock Level Alert", "Request Replenishment", "Replenishment Approval Status", "Last Update");
    
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;
    
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; 
                    continue;
                }
    
                String[] values = line.split(",");
                if (values.length >= 7) { 
                    System.out.printf("%-15s %-15s %-15s %-20s %-20s %-20s %-25s%n",
                            values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
                }
            }
        } 
        
        catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
    /*this method view the equipment, by read from csv file and present it to the user
    */
    public void viewEquipmentInventory() {
        System.out.println("Viewing equipment inventory...\n");
        System.out.printf("%-15s %-15s %-15s %-20s %-20s %-20s %-25s%n",
                "Equipment Name", "Initial Stock", "Current Stock",
                "Low Stock Level Alert", "Request Replenishment", "Replenishment Approval Status", "Last Update");

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH2))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; 
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 7) { 
                    System.out.printf("%-15s %-15s %-15s %-20s %-20s %-20s %-25s%n",
                            values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
                }
            }
        }

        catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

}
