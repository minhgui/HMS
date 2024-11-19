package pharmacist;

import java.util.Scanner;

import user.*;
import medicine_data.*;
import appointment_data.*;
import hospital.*;

public class Pharmacist extends User {

    protected String staffName;

    public Pharmacist(String hospitalID)
    {
        super(hospitalID);
    }

    private void viewAppointmentOutcomeRecord() {
        AppointmentAction apmenu = new AppointmentAction("C:/Users/mingh/eclipse-workspace/HMS/Appointment_Record/AppointmentRecord.csv");
        apmenu.viewAppointmentRecords(); 
    }

    private void updatePrescriptionStatus() {
        PrescriptionManagement prescription = new PrescriptionManagement();
        prescription.managePatientPrescription();
    }

    private void viewMedicationInventory() {
        PrescriptionManagement prescription = new PrescriptionManagement();
        prescription.viewMedicationInventory();
    }

    private void submitReplenishmentRequest() {
        PrescriptionManagement prescription = new PrescriptionManagement();
        prescription.submitReplenishmentRequest(); 
    }
    
    private void addNewMedicine(){
        PrescriptionManagement prescription = new PrescriptionManagement();
        System.out.println("Adding new medicine to inventory...");
        prescription.addNewMedicine();
    }

    private void deleteMedicine(){
        PrescriptionManagement prescription = new PrescriptionManagement();
        System.out.println("Removing medicine from inventory...");
        prescription.deleteMedicine();
    }

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
