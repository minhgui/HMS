
package hospital;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import utility.*;
import administrator.*;
import user.*;
import doctor.*;
import pharmacist.*;
import patient.*;

/**
 * Represents a hospital system that manages staff, patients, and medicine records.
 * Provides functionality to load, retrieve, and write data for various user roles.
 * @author Jan Chen Jie
 * @version 1.0
 * @since 19/11/2024
 */
public class Hospital {

    /**
     * The hospital ID associated with this hospital instance.
     */
    protected String hospitalID;

    /**
     * File paths for staff, patient, and medicine data.
     */
    private static final String STAFF_FILE_PATH = "./Staff_List/Staff_List.csv";
    private static final String PATIENT_FILE_PATH = "./Patient_List/Patient_List.csv";
    private static final String MEDICINE_FILE_PATH = "./Medicine_List/Medicine_List.csv";

    /**
     * Lists to store staff, patient, and medicine data.
     */
    private List<String[]> staffList;
    private List<String[]> patientList;
    private List<String[]> medicineList;

    /**
     * The name of the hospital or user (used for lookups).
     */
    private String name;

    /**
     * Default constructor for the Hospital class.
     */
    public Hospital() {
    }

    /**
     * Loads all staff data from the CSV file.
     * @return A list of staff records.
     */
    private List<String[]> loadStaff() {
        List<String[]> allStaff = new ArrayList<>();
        try {
            allStaff = Utility.readCSV(STAFF_FILE_PATH, 0);
        } catch (IOException e) {
            System.out.println("Error in loadStaff: " + e.getMessage());
        }
        return allStaff;
    }

    /**
     * Loads all patient data from the CSV file.
     * @return A list of patient records.
     */
    private List<String[]> loadPatients() {
        List<String[]> allPatients = new ArrayList<>();
        try {
            allPatients = Utility.readCSV(PATIENT_FILE_PATH, 0);
        } catch (IOException e) {
            System.out.println("Error in loadPatients: " + e.getMessage());
        }
        return allPatients;
    }

    /**
     * Loads a specific patient's data by their ID.
     * @param patientID The ID of the patient to load.
     * @return A list containing the patient's record.
     */
    private List<String[]> loadPatientByID(String patientID) {
        List<String[]> patient = new ArrayList<>();
        try {
            List<String[]> allPatients = Utility.readCSV(PATIENT_FILE_PATH, 0);
            for (String[] patientRow : allPatients) {
                if (patientRow[0].equals(patientID)) {
                    patient.add(patientRow);
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading patient " + patientID + ": " + e.getMessage());
        }
        return patient;
    }

    /**
     * Loads all medicine data from the CSV file.
     * @return A list of medicine records.
     */
    private List<String[]> loadMedicine() {
        List<String[]> allMedicine = new ArrayList<>();
        try {
            allMedicine = Utility.readCSV(MEDICINE_FILE_PATH, 0);
        } catch (IOException e) {
            System.out.println("Error in loading Medicine: " + e.getMessage());
        }
        return allMedicine;
    }

    /**
     * Retrieves the list of staff records for an Administrator.
     * @param user The user requesting the data.
     * @return A list of staff records.
     * @throws SecurityException If the user is not an Administrator.
     */
    public List<String[]> getStaff(User user) {
        if (user instanceof Administrator) {
            return loadStaff();
        } else {
            throw new SecurityException("Access denied");
        }
    }

    /**
     * Retrieves the list of patient records for a Doctor or Administrator.
     * @param user The user requesting the data.
     * @return A list of patient records.
     * @throws SecurityException If the user is not a Doctor or Administrator.
     */
    public List<String[]> getPatient(User user) {
        if (user instanceof Doctor || user instanceof Administrator) {
            return loadPatients();
        } else {
            throw new SecurityException("Access denied");
        }
    }

    /**
     * Retrieves a specific patient's record by their ID.
     * @param patientID The ID of the patient.
     * @param user The user requesting the data.
     * @return A list containing the patient's record.
     * @throws SecurityException If the user does not have the required access.
     */
    protected List<String[]> getPatientByID(String patientID, User user) {
        if (user instanceof Patient || user instanceof Doctor || user instanceof Administrator) {
            return loadPatientByID(patientID);
        } else {
            throw new SecurityException("Access denied");
        }
    }

    /**
     * Retrieves the list of medicine records for a Pharmacist or Administrator.
     * @param user The user requesting the data.
     * @return A list of medicine records.
     * @throws SecurityException If the user is not a Pharmacist or Administrator.
     */
    protected List<String[]> getMedicine(User user) {
        if (user instanceof Pharmacist || user instanceof Administrator) {
            return loadMedicine();
        } else {
            throw new SecurityException("Access denied");
        }
    }

    /**
     * Writes patient data to the CSV file.
     * @param data The patient data to write.
     */
    private void writePatientCSV(List<String[]> data) {
        try {
            Utility.writeCSV(PATIENT_FILE_PATH, data);
        } catch (IOException e) {
            System.out.println("Error in Writing Patient: " + e.getMessage());
        }
    }

    /**
     * Writes staff data to the CSV file.
     * @param data The staff data to write.
     */
    private void writeStaffCSV(List<String[]> data) {
        try {
            Utility.writeCSV(STAFF_FILE_PATH, data);
        } catch (IOException e) {
            System.out.println("Error in Writing Staff: " + e.getMessage());
        }
    }

    /**
     * Writes patient data to the CSV file if the user is an Administrator.
     * @param data The patient data to write.
     * @param user The user performing the operation.
     */
    public void writePatient(List<String[]> data, User user) {
        if (user instanceof Administrator) writePatientCSV(data);
    }

    /**
     * Writes staff data to the CSV file if the user is an Administrator.
     * @param data The staff data to write.
     * @param user The user performing the operation.
     */
    public void writeStaff(List<String[]> data, User user) {
        if (user instanceof Administrator) writeStaffCSV(data);
    }

    /**
     * Retrieves the name associated with a hospital ID.
     * @param hospitalID The hospital ID to search for.
     * @param user The user performing the operation.
     * @return The name associated with the hospital ID, or null if not found.
     */
    public String getName(String hospitalID, User user) {
        return searchName(hospitalID, user);
    }

    /**
     * Searches for a name associated with a hospital ID in the staff or patient list.
     * @param hospitalID The hospital ID to search for.
     * @param user The user performing the operation.
     * @return The name associated with the hospital ID, or null if not found.
     */
    private String searchName(String hospitalID, User user) {
        if (user instanceof Administrator || user instanceof Doctor || user instanceof Pharmacist) {
            staffList = loadStaff();
            for (String[] staff : staffList) {
                if (staff[0].equals(hospitalID)) return staff[1];
            }
        } else if (user instanceof Patient) {
            patientList = loadPatients();
            for (String[] patient : patientList) {
                if (patient[0].equals(hospitalID)) return patient[1];
            }
        }
        return null;
    }
}
