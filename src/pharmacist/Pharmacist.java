package pharmacist;

import java.util.Scanner;

import user.*;
import appointment_data.*;
import hospital.*;
import inventory_data.*;

/**
 * Represents a pharmacist employed in the hospital. 
 * A pharmacist manages the prescription for hospital patients.
 * 
 * @author Alvin Ong Minghui
 * @version 3.0.1
 * @since 2024-11-19
 */
public class Pharmacist extends User {

    /**
     * The name of the pharmacist.
     */
    protected String staffName;

    /**
     * The hospital ID for this pharmacist.
     * 
     * @param hospitalID The unique identifier for the pharmacist's hospital.
     */
    public Pharmacist(String hospitalID) {
        super(hospitalID);
    }

    /**
     * Displays the appointment outcome records for the pharmacist.
     * Calls the `viewAppointmentRecords` function from the `AppointmentAction` class.
     */
    private void viewAppointmentOutcomeRecord() {
        AppointmentAction apmenu = new AppointmentAction();
        apmenu.viewAppointmentRecords(); 
    }

    /**
     * Updates the prescription status for a patient.
     * Calls the `managePatientPrescription` function from the `PrescriptionManagement` class.
     */
    private void updatePrescriptionStatus() {
        PrescriptionManagement prescription = new PrescriptionManagement();
        prescription.managePatientPrescription();
    }

    /**
     * Views the medication inventory.
     * Calls the `viewMedicationInventory` function from the `PrescriptionManagement` class.
     */
    private void viewMedicationInventory() {
        PrescriptionManagement prescription = new PrescriptionManagement();
        prescription.viewMedicationInventory();
    }

    /**
     * Submits a replenishment request for a medicine in the inventory.
     * Calls the `submitReplenishmentRequest` function from the `PrescriptionManagement` class.
     */
    private void submitReplenishmentRequest() {
        PrescriptionManagement prescription = new PrescriptionManagement();
        prescription.submitReplenishmentRequest(); 
    }

    /**
     * Adds a new medicine to the inventory.
     * Calls the `addNewMedicine` function from the `PrescriptionManagement` class.
     */
    private void addNewMedicine() {
        PrescriptionManagement prescription = new PrescriptionManagement();
        System.out.println("Adding new medicine to inventory...");
        prescription.addNewMedicine();
    }

    /**
     * Deletes an existing medicine from the inventory.
     * Calls the `deleteMedicine` function from the `PrescriptionManagement` class.
     */
    private void deleteMedicine() {
        PrescriptionManagement prescription = new PrescriptionManagement();
        System.out.println("Removing medicine from inventory...");
        prescription.deleteMedicine();
    }

    /**
     * Displays the menu for the pharmacist and handles the user's choices.
     * 
     * Options include:
     * <ul>
     * <li>Viewing appointment outcome records</li>
     * <li>Updating prescription status</li>
     * <li>Viewing medication inventory</li>
     * <li>Submitting replenishment requests</li>
     * <li>Adding new medicines</li>
     * <li>Deleting medicines</li>
     * <li>Logging out</li>
     * </ul>
     * The pharmacist selects an option by entering a number between 1 and 7.
     */
    public void displayMenu() {

        Hospital hospital = new Hospital();
        staffName = hospital.getName(hospitalID, this);
        System.out.println("Welcome Pharmacist, " + staffName);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("---- Pharmacist Menu ----");
            System.out.println("1. View Appointment Outcome Record");
            System.out.println("2. Update Prescription Status");
            System.out.println("3. View Medication Inventory");
            System.out.println("4. Submit Replenishment Request");
            System.out.println("5. Add New Medicine to Inventory");
            System.out.println("6. Remove Medicine From Inventory");
            System.out.println("7. Logout");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    viewAppointmentOutcomeRecord();
                    break;
                case 2:
                    updatePrescriptionStatus();
                    break;
                case 3:
                    viewMedicationInventory();
                    break;
                case 4:
                    submitReplenishmentRequest();
                    break;
                case 5:
                    addNewMedicine();
                    break;
                case 6:
                    deleteMedicine();
                    break;
                case 7:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
