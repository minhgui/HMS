
package administrator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents the management of patients in the hospital system.
 * Provides functionality to add, remove, modify, and search patient records.
 * @author Jan Chen Jie
 * @version 1.0
 * @since 19/11/2024
 */
public class PatientManage implements AdministratorManageFunction {

    /**
     * The list of patient records.
     */
    private List<String[]> patientList;

    /**
     * Creates a new PatientManage instance with the given patient list.
     * @param patientList The list of patient records.
     */
    public PatientManage(List<String[]> patientList) {
        this.patientList = patientList;
    }

    /**
     * Adds a new patient record to the list.
     * @param newID The unique ID for the new patient.
     * @return True if the patient was added successfully, otherwise false.
     */
    public boolean add(String newID) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Add Patient");
        System.out.print("ID: " + newID);

        System.out.print("\nName: ");
        String name = sc.nextLine();
        if (name.isEmpty()) return false;
        System.out.print("Date of Birth (MM/DD/YYYY): ");
        String dob = sc.nextLine();
        if (dob.isEmpty()) return false;
        System.out.print("Gender (Male/Female): ");
        String gender = sc.nextLine();
        if (gender.isEmpty()) return false;
        System.out.print("Blood Type: ");
        String blood = sc.nextLine();
        if (blood.isEmpty()) return false;
        System.out.print("Contact Information: ");
        String contact = sc.nextLine();
        if (contact.isEmpty()) return false;
        System.out.print("Phone: ");
        String phone = sc.nextLine();
        if (phone.isEmpty()) return false;

        patientList.add(new String[]{newID, name, dob, gender, blood, contact, phone});
        System.out.println("Patient added successfully.");
        return true;
    }

    /**
     * Removes a patient record from the list based on the given ID.
     * @param removeID The ID of the patient to be removed.
     */
    public void remove(String removeID) {
        String[] patient = searchPatient(removeID);
        if (patient != null) {
            patientList.remove(patient);
            System.out.println("Patient removed successfully.");
        } else {
            System.out.println("Patient not found.");
        }
    }

    /**
     * Modifies the information of a patient with the given ID.
     * @param modifyID The ID of the patient to be modified.
     */
    public void modify(String modifyID) {
        String[] patient = searchPatient(modifyID);
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        patient[1] = modifyField("Name", patient[1]);
        patient[2] = modifyField("Date of Birth", patient[2]);
        patient[3] = modifyField("Gender", patient[3]);
        patient[4] = modifyField("Blood Type", patient[4]);
        patient[5] = modifyField("Contact Information", patient[5]);
        patient[6] = modifyField("Phone", patient[6]);

        System.out.println("Patient information updated successfully.");
    }

    /**
     * Prompts the user to modify a specific field of a patient record.
     * @param field The name of the field to modify.
     * @param currentValue The current value of the field.
     * @return The new value of the field or the current value if left blank.
     */
    private String modifyField(String field, String currentValue) {
        Scanner sc = new Scanner(System.in);
        System.out.printf("Current %s: %s\n", field, currentValue);
        System.out.print("Enter New Value (Leave blank to keep current): ");
        String newValue = sc.nextLine();
        return newValue.isBlank() ? currentValue : newValue;
    }

    /**
     * Searches for a patient in the list based on the given ID.
     * @param id The ID of the patient to search for.
     * @return The patient record if found, otherwise null.
     */
    private String[] searchPatient(String id) {
        for (String[] patient : patientList) {
            if (patient[0].equals(id)) {
                System.out.println(patient);
                return patient;
            }
        }
        return null;
    }

    /**
     * Searches for a patient in the list and returns matching records.
     * @param id The ID of the patient to search for.
     * @return A list containing the header and matching patient record.
     */
    public List<String[]> search(String id) {
        List<String[]> result = new ArrayList<>();
        result.add(getHeader(patientList));
        for (String[] patient : patientList) {
            if (patient[0].equals(id)) {
                result.add(patient);
                break;
            }
        }
        if (result.isEmpty()) {
            System.out.println("Patient not found.");
        } else {
            System.out.println("Patient found.");
        }
        return result;
    }

    /**
     * Retrieves the header row from the patient list.
     * @param list The list of patient records.
     * @return The header row if present, otherwise null.
     */
    private String[] getHeader(List<String[]> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Retrieves the data rows from the patient list, excluding the header.
     * @param list The list of patient records.
     * @return A list containing only the data rows.
     */
    private List<String[]> getData(List<String[]> list) {
        if (list == null || list.size() <= 1) {
            return new ArrayList<>();
        }
        return list.subList(1, list.size());
    }
}
