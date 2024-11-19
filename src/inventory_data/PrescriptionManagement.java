package inventory_data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Manages the hospital's prescription and medicine inventory system.
 * This includes adding and deleting medicines, viewing inventory,
 * handling replenishment requests, and managing patient prescriptions.
 * 
 * @author Alvin Ong Minghui
 * @version 3.0.1
 * @since 2024-11-19
 */

public class PrescriptionManagement {
	
    /**
     * File path for the medicine inventory CSV file.
     */

    private static final String MEDICINE_CSV = "./Medicine_List/Medicine_List.csv";
    
    /**
     * File path for the appointment records CSV file.
     */
    
    private static final String APPOINTMENT_CSV = "./Appointment_Record/AppointmentRecord.csv";

    /**
     * Adds a new medicine to the inventory. 
     * Prompts the user for the medicine name, initial stock, and low stock alert level.
     * Validates whether the medicine already exists.
     */    
    
    public void addNewMedicine() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter Medicine Name: ");
            String name = scanner.nextLine().trim();

            if (isMedicineValid(name)) {
                System.out.println("Medicine already exists in the list.");
                return;
            }

            int initialStock = getValidIntegerInput(scanner, "Enter Initial Stock: ");
            int lowStockLevelAlert = getValidIntegerInput(scanner, "Enter Low Stock Level Alert: ");

            Medicine newMedicine = new Medicine(name, initialStock, lowStockLevelAlert);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEDICINE_CSV, true))) {
                bw.write(newMedicine.toCSVRow());
                bw.newLine();
            }

            System.out.println("New medicine '" + name + "' has been added successfully!");
        } catch (IOException e) {
            System.out.println("Error updating the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Deletes a medicine from the inventory.
     * Prompts the user for the medicine name and validates whether it exists in the inventory.
     * If the medicine is found, it is removed from the inventory CSV file.
     */
    
    public void deleteMedicine() {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter Medicine Name to delete: ");
        String medicineName = scanner.nextLine().trim();
    
        File inputFile = new File(MEDICINE_CSV);
        File tempFile = new File(inputFile.getParent(), "Temp_" + inputFile.getName());
    
        boolean medicineFound = false;
    
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
    
            String line;
            boolean isFirstLine = true;
    
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    bw.write(line);
                    bw.newLine();
                    isFirstLine = false;
                    continue;
                }
    
                String[] values = line.split(",");
                if (values.length >= 1 && values[0].equalsIgnoreCase(medicineName)) {
                    medicineFound = true;
                    System.out.println("Medicine '" + medicineName + "' found and deleted.");
                } else {
                    bw.write(line);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error processing the file: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    
        if (medicineFound) {
            if (inputFile.delete()) {
                if (tempFile.renameTo(inputFile)) {
                    System.out.println("Medicine list updated successfully.");
                } else {
                    System.out.println("Error renaming the temp file.");
                }
            } else {
                System.out.println("Error deleting the original file.");
            }
        } else {
            System.out.println("Medicine '" + medicineName + "' not found in the list.");
            tempFile.delete();
        }
    }
    
    /**
     * Displays the inventory of medicines.
     * Reads from the `Medicine_List.csv` file and prints each medicine's details,
     * including its name, stock levels, replenishment status, and last update.
     */
    
    public void viewMedicationInventory() {
        System.out.println("Viewing medication inventory...\n");
    
        try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_CSV))) {
            String line;
            boolean isFirstLine = true;
    
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
    
                String[] values = line.split(",");
                if (values.length == 7) { 
                    System.out.println("Medicine Name: " + values[0]);
                    System.out.println("Initial Stock: " + values[1]);
                    System.out.println("Current Stock: " + values[2]);
                    System.out.println("Low Stock Level Alert: " + values[3]);
                    System.out.println("Request Replenishment: " + values[4]);
                    System.out.println("Replenishment Approved: " + values[5]);
                    System.out.println("Last Update: " + values[6]);
                    System.out.println("---------------------------------------------------");
                } else {
                    System.out.println("Invalid row format: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + MEDICINE_CSV);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Submits a replenishment request for a specific medicine.
     * Prompts the user for the medicine name and the replenishment quantity.
     * Updates the replenishment request status in the CSV file.
     */
    
    public void submitReplenishmentRequest() {
        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;

        try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_CSV))) {
            StringBuilder updatedFileContent = new StringBuilder();
            String line;
            boolean isFirstLine = true;

            System.out.println("Submit Replenishment Request:");
            System.out.print("Enter Medicine Name: ");
            String inputMedicine = scanner.nextLine().trim();

            int inputQuantity = 0;
            while (!validInput) {
                System.out.print("Enter Quantity: ");
                String quantityStr = scanner.nextLine().trim();
                try {
                    inputQuantity = Integer.parseInt(quantityStr);
                    if (inputQuantity <= 0) {
                        System.out.println("Quantity must be a positive number.");
                    } else {
                        validInput = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Quantity must be a numeric value.");
                }
            }

            boolean medicineFound = false;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (isFirstLine) {
                    updatedFileContent.append(line).append("\n");
                    isFirstLine = false;
                    continue;
                }

                if (values[0].equalsIgnoreCase(inputMedicine)) {
                    medicineFound = true;

                    values[4] = String.valueOf(inputQuantity); 
                    values[5] = "Pending"; 

                    updatedFileContent.append(String.join(",", values)).append("\n");
                } else {
                    updatedFileContent.append(line).append("\n");
                }
            }

            if (!medicineFound) {
                System.out.println("Error: Medicine not found in the inventory.");
                return;
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEDICINE_CSV))) {
                bw.write(updatedFileContent.toString());
            }

            System.out.println("Replenishment request submitted successfully!");

        } catch (IOException e) {
            System.out.println("Error accessing the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Approves or rejects pending replenishment requests.
     * Prompts the user for the medicine name and updates the status in the CSV file.
     */
    
    public void approveReplenishmentRequest() {
        Scanner scanner = new Scanner(System.in);
    
        try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_CSV))) {
            StringBuilder updatedFileContent = new StringBuilder();
            String line;
            boolean isFirstLine = true;
    
            System.out.println("Medicines with 'Pending' Replenishment Requests:\n");
            System.out.printf("%-15s %-15s %-15s %-20s %-20s %-20s %-20s%n",
                    "Medicine Name", "Initial Stock", "Current Stock",
                    "Low Stock Level Alert", "Request Replenishment",
                    "Replenishment Approved", "Last Update");
    
            boolean hasPendingRequests = false;
    
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (isFirstLine) {
                    updatedFileContent.append(line).append("\n");
                    isFirstLine = false;
                    continue;
                }
    
                if (values.length == 7 && values[5].equalsIgnoreCase("Pending")) {
                    hasPendingRequests = true;
                    System.out.printf("%-15s %-15s %-15s %-20s %-20s %-20s %-20s%n",
                            values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
                }
                updatedFileContent.append(line).append("\n");
            }
    
            if (!hasPendingRequests) {
                System.out.println("\nNo pending replenishment requests found.");
                return;
            }
    
            String inputMedicine;
            do {
                System.out.print("\nEnter the Medicine Name to process: ");
                inputMedicine = scanner.nextLine().trim();
    
                if (!isPendingValid(inputMedicine, updatedFileContent.toString())) {
                    System.out.println("Invalid Medicine Name or not in Pending status.");
                    System.out.print("Do you want to retry or quit? (Type 'Retry' to try again or 'Quit' to exit): ");
                    String choice = scanner.nextLine().trim();
                    if (choice.equalsIgnoreCase("Quit")) {
                        System.out.println("Exiting replenishment approval process.");
                        return;
                    }
                } else {
                    break;
                }
            } while (true);
    
            boolean validInput = false;
            String approvalStatus = "";
            while (!validInput) {
                System.out.print("Approve or Reject Replenishment Request (Type 'Approve' or 'Reject'): ");
                approvalStatus = scanner.nextLine().trim();
                if (approvalStatus.equalsIgnoreCase("Approve") || approvalStatus.equalsIgnoreCase("Reject")) {
                    validInput = true;
                } else {
                    System.out.println("Invalid input. Please type 'Approve' or 'Reject'.");
                }
            }
    
            StringBuilder finalFileContent = new StringBuilder();
            String[] rows = updatedFileContent.toString().split("\n");
            for (String row : rows) {
                String[] values = row.split(",");
                if (values.length == 7 && values[0].equalsIgnoreCase(inputMedicine)) {
                    if (approvalStatus.equalsIgnoreCase("Approve")) {
                        values[6] = "Approved on " + LocalDateTime.now();
                        values[1] = String.valueOf(Integer.parseInt(values[2]) + Integer.parseInt(values[4]));
                        values[2] = String.valueOf(Integer.parseInt(values[2]) + Integer.parseInt(values[4]));
                        values[4] = "0";
                        values[5] = "NIL";
                    } else if (approvalStatus.equalsIgnoreCase("Reject")) {
                        values[6] = "Rejected on " + LocalDateTime.now();
                        values[4] = "0";
                        values[5] = "NIL";
                    }
                }
                finalFileContent.append(String.join(",", values)).append("\n");
            }
    
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEDICINE_CSV))) {
                bw.write(finalFileContent.toString());
            }
    
            System.out.println("Replenishment request for '" + inputMedicine + "' has been " + approvalStatus + ".");
        } catch (IOException e) {
            System.out.println("Error accessing the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Dispenses or remain pending prescriptition requests.
     * Prompts the user for the medicine name and quantity and updates the status in the CSV file.
     */
    
    public void managePatientPrescription() {
        Scanner scanner = new Scanner(System.in);

        String patientName = null;
        boolean isValidPatient = false;
        while (!isValidPatient) {
            System.out.print("Enter the name of the patient: ");
            patientName = scanner.nextLine();

            if (isPatientValid(patientName)) {
                isValidPatient = true;
            } else {
                System.out.println("Invalid patient name. Please try again.");
            }
        }

        List<String[]> allAppointments = new ArrayList<>();
        boolean hasPendingAppointments = false;

        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENT_CSV))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    allAppointments.add(line.split(","));
                    continue;
                }

                String[] values = line.split(",");
                allAppointments.add(values);

                if (values.length == 11 && values[0].equalsIgnoreCase(patientName) && values[9].equalsIgnoreCase("Pending")) {
                    hasPendingAppointments = true;

                    System.out.println("\nPending Appointment:");
                    System.out.println("    Date of Appointment: " + values[2]);
                    System.out.println("    Time of Appointment: " + values[3]);
                    System.out.println("    Appointment Status: " + values[4]);
                    System.out.println("    Patient Diagnosis: " + values[5]);
                    System.out.println("    Patient Treatment: " + values[6]);
                    System.out.println("    Prescribed Medication: " + values[7]);
                    System.out.println("    Medication Quantity: " + values[8]);
                    System.out.println("    Prescription Status: " + values[9]);
                    System.out.println("    Remarks: " + values[10]);

                    boolean validMedication = false;
                    while (!validMedication) {
                        System.out.print("Enter Medication Name: ");
                        String medicationName = scanner.nextLine();

                        if (!medicationName.equalsIgnoreCase(values[7])) {
                            System.out.println("Incorrect Prescribed Medication. Would you like to retry or exit? (retry/exit): ");
                            String choice = scanner.nextLine();
                            if (choice.equalsIgnoreCase("exit")) {
                                return;
                            }
                        } else {
                            validMedication = true;

                            boolean validQuantity = false;
                            int medicationQuantity = -1;
                            while (!validQuantity) {
                                System.out.print("Enter Medication Quantity: ");
                                try {
                                    medicationQuantity = Integer.parseInt(scanner.nextLine());
                                    validQuantity = true;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid quantity. Please enter a numeric value.");
                                }
                            }

                            System.out.print("Enter Prescription Status (Dispensed/Pending): ");
                            String prescriptionStatus = scanner.nextLine();

                            values[8] = String.valueOf(medicationQuantity);
                            values[9] = prescriptionStatus;

                            if (prescriptionStatus.equalsIgnoreCase("Dispensed")) {
                                boolean stockUpdated = updateMedicineStock(medicationName, medicationQuantity);
                                if (!stockUpdated) {
                                    System.out.println("Out of Stock, Submit Replenishment Request. Exiting.");
                                    return;
                                }
                            } else if (prescriptionStatus.equalsIgnoreCase("Pending")) {
                                System.out.println("Prescription status set to Pending. Exiting.");
                                return;
                            } else {
                                System.out.println("Invalid prescription status. Exiting.");
                                return;
                            }
                        }
                    }
                }
            }

            if (!hasPendingAppointments) {
                System.out.println("No pending appointments found for the specified patient.");
                return;
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENT_CSV))) {
                for (String[] record : allAppointments) {
                    bw.write(String.join(",", record));
                    bw.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error writing to the appointment file: " + e.getMessage());
            }

        } catch (IOException e) {
            System.out.println("Error reading the appointment file: " + e.getMessage());
        }
    }

    /**
     * Validates whether a medicine exists in the inventory.
     * 
     * @param medicineName The name of the medicine to validate.
     * @return True if the medicine exists, false otherwise.
     */
    
    private boolean isMedicineValid(String medicineName) {
        try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_CSV))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 1 && values[0].equalsIgnoreCase(medicineName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error validating medicine: " + e.getMessage());
        }
        return false;
    }

    /**
     * Validates whether a submission request exists in the inventory.
     * 
     * @param medicineName The name of the medicine to validate.
     * @return True if the medicineName and pending exists, false otherwise.
     */
    
    private boolean isPendingValid(String medicineName, String fileContent) {
        String[] rows = fileContent.split("\n");
        for (String row : rows) {
            String[] values = row.split(",");
            if (values.length == 7 && values[0].equalsIgnoreCase(medicineName) && values[5].equalsIgnoreCase("Pending")) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Validates whether a patient exists in the appointment records.
     * 
     * @param patientName The name of the patient to validate.
     * @return True if the patientName exists, false otherwise.
     */
    
    private boolean isPatientValid(String patientName) {
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENT_CSV))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 1 && values[0].equalsIgnoreCase(patientName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the appointment file: " + e.getMessage());
        }
        return false;
    }

    /**
     * Validates integer.
     * 
     * @param scanner The input of the integer to validate.
     * @return Integer if the scanner is INT, print error otherwise.
     */
    
    private int getValidIntegerInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    /**
     * Updates the stock of a specified medicine in the inventory.
     * This method checks whether the specified medicine exists and 
     * whether there is sufficient stock to dispense the requested quantity.
     * If sufficient stock is available, it deducts the quantity and updates the inventory.
     *
     * @param medicationName The name of the medicine to update.
     * @param medicationQuantity The quantity of the medicine to deduct from the stock.
     * @return True if the stock was successfully updated, false otherwise.
     */
    
    private boolean updateMedicineStock(String medicationName, int medicationQuantity) {
        List<String[]> medicineRecords = new ArrayList<>();
        boolean medicineFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_CSV))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 3 && values[0].equalsIgnoreCase(medicationName)) {
                    medicineFound = true;
                    int currentStock = Integer.parseInt(values[2]);

                    if (currentStock >= medicationQuantity) {
                        currentStock -= medicationQuantity;
                        values[2] = String.valueOf(currentStock);
                        System.out.println("Medication updated successfully. New stock: " + currentStock);
                    } else {
                        System.out.println("Out of Stock.");
                        return false;
                    }
                }
                medicineRecords.add(values);
            }
        } catch (IOException e) {
            System.out.println("Error reading the medicine file: " + e.getMessage());
            return false;
        }

        if (!medicineFound) {
            System.out.println("Medication not found in the inventory.");
            return false;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEDICINE_CSV))) {
            bw.write("Medicine Name,Initial Stock,Current Stock,Low Stock Level Alert,Request Replenishment,Replenishment Approved,Last Updated");
            bw.newLine();
            for (String[] record : medicineRecords) {
                bw.write(String.join(",", record));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to the medicine file: " + e.getMessage());
            return false;
        }

        return true;
    }

}
