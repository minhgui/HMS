
package administrator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import user.*;
import appointment_data.*;
import hospital.*;
import inventory_data.*;

/**
 * Represents an Administrator of the hospital system. 
 * An Administrator can manage staff, patients, appointments, and inventory.
 * @author Jan Chen Jie
 * @version 1.0
 * @since 19/11/2024
 */
public class Administrator extends User {

    /** 
     * The name of the staff member logged in as an Administrator. 
     */
    protected String staffName;

    /**
     * The list of staff records.
     */
    private List<String[]> staffList;

    /**
     * The list of user records.
     */
    private List<String[]> userList;

    /**
     * The list of patient records.
     */
    private List<String[]> patientList;

    /**
     * Manages staff records.
     */
    private StaffManage staffManage;

    /**
     * Manages patient records.
     */
    private PatientManage patientManage;

    /**
     * Manages user records.
     */
    private UserManage userManage;

    /**
     * Indicates if the Administrator is a secret user.
     */
    private boolean isSecretUser;

    /**
     * The hospital associated with this Administrator.
     */
    private Hospital hospital;

    /**
     * Manages prescriptions and inventory replenishment.
     */
    private PrescriptionManagement prescriptionManagement;

    /**
     * Creates a new Administrator instance with the given hospital ID and secret user flag.
     * @param hospitalID The ID of the hospital.
     * @param isSecretUser Whether the Administrator is a secret user.
     */
    public Administrator(String hospitalID, boolean isSecretUser) {
        super(hospitalID);
        this.isSecretUser = isSecretUser;
        this.hospital = new Hospital();
    }

    /**
     * The main method to start the Administrator interface.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Administrator admin = new Administrator("A001", false);
        admin.displayMenu();
    }

    /**
     * Displays the Administrator menu and handles user interactions.
     */
    public void displayMenu() {
        if (!isValidStaff()) return;

        boolean isLoggedIn = true;
        Scanner sc = new Scanner(System.in);

        while (isLoggedIn) {
            System.out.println("---- Administrator Menu ----");
            System.out.println("1. View and Manage Hospital Staff");
            System.out.println("2. View and Manage Patient Staff");
            System.out.println("3. View Appointments Details");
            System.out.println("4. View and Manage Medication Inventory");
            System.out.println("5. Approve Replenishment Requests");
            System.out.println("6. Logout");

            int choice = sc.nextInt();

            switch (choice) {
                case 1: manageStaffMenu(); break;
                case 2: managePatientMenu(); break;
                case 3: 
                    AppointmentAction appointmentAction = new AppointmentAction();
                    appointmentAction.viewAppointmentRecords();
                    break;
                case 4: manageMedicationMenu(); break;
                case 5: 
                    prescriptionManagement = new PrescriptionManagement();
                    prescriptionManagement.approveReplenishmentRequest();
                    break;
                case 6: 
                    System.out.println("You have logged out."); 
                    isLoggedIn = false; 
                    return;
                default:
                    break;
            }
        }
    }

    /**
     * Manages the medication inventory menu.
     */
    private void manageMedicationMenu() {
        prescriptionManagement = new PrescriptionManagement();
        Scanner sc = new Scanner(System.in);
        System.out.println("1. View Medication List");
        System.out.println("2. Add Medication");
        System.out.println("3. Delete Medication");

        int choice = sc.nextInt();

        switch (choice) {
            case 1: prescriptionManagement.viewMedicationInventory(); break;
            case 2: prescriptionManagement.addNewMedicine(); break;
            case 3: prescriptionManagement.deleteMedicine(); break;
            default: break;
        }
    }

    /**
     * Sorts a given list in ascending or descending order by ID.
     * @param list The list to sort.
     */
    private void sorting(List<String[]> list) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Sorting");
        System.out.println("A-Ascending & D-Descending:");
        String sortBy = sc.nextLine();

        boolean ascending = sortBy.equalsIgnoreCase("A");
        List<String[]> sortedList = sortList(list, ascending);
        display(sortedList);
    }

    /**
     * Sorts the given list based on ascending or descending order.
     * @param list The list to sort.
     * @param ascending Whether to sort in ascending order.
     * @return The sorted list.
     */
    private List<String[]> sortList(List<String[]> list, boolean ascending) {
        if (list.isEmpty()) return list;

        String[] header = list.get(0);
        List<String[]> data = list.subList(1, list.size());

        if (ascending) 
            Collections.sort(data, (ID1, ID2) -> ID1[0].compareTo(ID2[0]));
        else 
            Collections.sort(data, (ID1, ID2) -> ID2[0].compareTo(ID1[0]));

        List<String[]> sortedList = new ArrayList<>();
        sortedList.add(header);
        sortedList.addAll(data);

        return sortedList;
    }

    /**
     * Manages the patient records menu, allowing various patient-related actions.
     */
    private void managePatientMenu() {
        boolean exit = false;
        Scanner sc = new Scanner(System.in);
        String role = "Patient";

        while (!exit) {
            System.out.println("---- Manage Hospital Patient ----");
            System.out.println("1. View Patient List");
            System.out.println("2. Sort Patient");
            System.out.println("3. Search Patient");
            System.out.println("4. Add Patient");
            System.out.println("5. Remove Patient");
            System.out.println("6. Modify Patient Information");
            System.out.println("7. Exit");

            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1: 
                    display(patientList);
                    display(userList); 
                    break;
                case 2: 
                    sorting(patientList); 
                    break;
                case 3: 
                    System.out.println("Enter Patient ID to search:");
                    String searchID = sc.nextLine();
                    List<String[]> foundPatient = patientManage.search(searchID);
                    display(foundPatient);
                    break;
                case 4: 
                    String newID = assignID(role);
                    if (newID != null && patientManage.add(newID) && userManage.add(newID)) {
                        sortList(userList, true);
                        sortList(patientList, true);
                        UserCredentials.writeUser(userList, this);
                        hospital.writePatient(patientList, this);
                    }
                    break;
                case 5: 
                    System.out.print("Enter Patient ID to Remove: ");
                    String removeID = sc.nextLine();
                    patientManage.remove(removeID);
                    userManage.remove(removeID);
                    sortList(userList, true);
                    sortList(patientList, true);
                    UserCredentials.writeUser(userList, this);
                    hospital.writePatient(patientList, this);
                    break;
                case 6: 
                    System.out.print("Enter Patient ID to Modify: ");
                    String modifyID = sc.nextLine();
                    patientManage.modify(modifyID);
                    userManage.modify(modifyID);
                    sortList(userList, true);
                    sortList(patientList, true);
                    UserCredentials.writeUser(userList, this);
                    hospital.writePatient(patientList, this);
                    break;
                case 7:
                    exit = true; 
                    break;
                default:
                    System.out.println("Invalid choice. Try again."); 
                    break;
            }
        }
    }

    /**
     * Manages the staff records menu, allowing various staff-related actions.
     */
    private void manageStaffMenu() {
        boolean exit = false;
        Scanner sc = new Scanner(System.in);

        while (!exit) {
            System.out.println("---- Manage Hospital Staff ----");
            System.out.println("1. View Staff List");
            System.out.println("2. Sort Staff");
            System.out.println("3. Filter Staff List");
            System.out.println("4. Add Staff");
            System.out.println("5. Remove Staff");
            System.out.println("6. Modify Staff Information");
            System.out.println("7. Exit");

            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1: 
                    display(staffList);
                    display(userList); 
                    break;
                case 2: 
                    sorting(staffList); 
                    break;
                case 3: 
                    List<String[]> filteredStaff = staffManage.filter(staffList);
                    display(filteredStaff); 
                    break;
                case 4: 
                    System.out.print("Enter Role: ");
                    String role = sc.nextLine();
                    String newID = assignID(role);
                    if (newID != null && staffManage.add(newID) && userManage.add(newID)) {
                        sortList(userList, true);
                        sortList(staffList, true);
                        UserCredentials.writeUser(userList, this);
                        hospital.writeStaff(staffList, this);
                    }
                    break;
                case 5: 
                    System.out.print("Enter Staff ID to Remove: ");
                    String removeID = sc.nextLine();
                    staffManage.remove(removeID);
                    userManage.remove(removeID);
                    sortList(userList, true);
                    sortList(staffList, true);
                    UserCredentials.writeUser(userList, this);
                    hospital.writeStaff(staffList, this);
                    break;
                case 6: 
                    System.out.print("Enter Staff ID to Modify: ");
                    String modifyID = sc.nextLine();
                    staffManage.modify(modifyID);
                    userManage.modify(modifyID);
                    sortList(userList, true);
                    sortList(staffList, true);
                    UserCredentials.writeUser(userList, this);
                    hospital.writeStaff(staffList, this);
                    break;
                case 7:
                    exit = true; 
                    break;
                default:
                    System.out.println("Invalid choice. Try again."); 
                    break;
            }
        }
    }

    /**
     * Displays the given list of records in a formatted table.
     * @param displayList The list of records to display.
     */
    private static void display(List<String[]> displayList) {
        if (displayList == null || displayList.isEmpty()) {
            System.out.println("No data to display.");
            return;
        }

        String[] headers = displayList.get(0);
        int[] columnWidths = new int[headers.length];
        
        for (int i = 0; i < headers.length; i++) {
            columnWidths[i] = headers[i].length();
        }

        for (String[] row : displayList) {
            for (int i = 0; i < row.length; i++) {
                columnWidths[i] = Math.max(columnWidths[i], row[i].length());
            }
        }

        int totalWidth = 0;
        for (int width : columnWidths) {
            totalWidth += width + 3; 
        }

        System.out.println("=".repeat(totalWidth));
        for (int i = 0; i < headers.length; i++) {
            System.out.printf("%-" + (columnWidths[i] + 3) + "s", headers[i]);
        }
        System.out.println();
        System.out.println("=".repeat(totalWidth));

        for (int i = 1; i < displayList.size(); i++) {
            String[] row = displayList.get(i);
            for (int j = 0; j < row.length; j++) {
                System.out.printf("%-" + (columnWidths[j] + 3) + "s", row[j]);
            }
            System.out.println();
        }
        System.out.println("=".repeat(totalWidth));
    }

    /**
     * Validates the staff credentials for the current Administrator.
     * @return True if the staff credentials are valid, otherwise false.
     */
    private boolean isValidStaff() {
        loadData();
        if (isSecretUser) {
            System.out.println("\nWelcome Administrator, SECRET");
            return true;
        }

        String displayName = hospital.getName(hospitalID, this);
        if (displayName != null) {
            System.out.println("\nWelcome Administrator, " + displayName);
            return true;
        }

        System.out.println("Access denied: Invalid staff credentials.");
        return false;
    }

    /**
     * Loads data from the hospital system.
     */
    private void loadData() {
        staffList = hospital.getStaff(this);
        patientList = hospital.getPatient(this);
        userList = UserCredentials.getUser(this);
        staffManage = new StaffManage(staffList);
        userManage = new UserManage(userList);
        patientManage = new PatientManage(patientList);
    }

    /**
     * Assigns a new ID for a given role based on the existing records.
     * @param role The role for which the ID is to be assigned.
     * @return The new assigned ID.
     */
    private String assignID(String role) {
        String index = User.getRoleIndex(role);
        if (index == null) {
            return null;
        }

        String highestID = "";

        if (role.equalsIgnoreCase("patient")) 
            highestID = getHighestID(index, patientList);
        else 
            highestID = getHighestID(index, staffList);

        return highestID;
    }

    /**
     * Retrieves the highest ID for a given index from the list.
     * @param index The prefix index for the IDs.
     * @param itemList The list to search for the highest ID.
     * @return The next highest ID.
     */
    private String getHighestID(String index, List<String[]> itemList) {
        int highestID = 1;
        String format = index.equals("P1") ? "%s%03d" : "%s%02d";
        for (String[] itemRow : itemList) {
            String ID = itemRow[0];
            if (ID.startsWith(index)) {
                int idNumber = Integer.parseInt(ID.substring(index.length()));
                if (idNumber != highestID) 
                    return String.format(format, index, highestID);

                highestID++;
            }
        }
        return String.format(format, index, highestID);
    }
}
