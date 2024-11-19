package patient_data;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import administrator.*;

public class PatientManage implements AdministratorManageFunction {
    private List<String[]> patientList;

    public PatientManage(List<String[]> patientList) {
        this.patientList = patientList;
    }

    public boolean add(String newID) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Add Patient");
        System.out.print("ID: "+newID);

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

        patientList.add(new String[]{newID, name, dob, gender, blood, contact,phone});
        System.out.println("Patient added successfully.");
        return true;
    }

    public void remove(String removeID) {

        String[] patient = searchPatient(removeID);
        if (patient != null) {
            patientList.remove(patient);
            System.out.println("Patient removed successfully.");
        } else {
            System.out.println("Patient not found.");
        }
    }

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

    private String modifyField(String field, String currentValue) {
        Scanner sc = new Scanner(System.in);
        System.out.printf("Current %s: %s\n", field, currentValue);
        System.out.print("Enter New Value (Leave blank to keep current): ");
        String newValue = sc.nextLine();
        return newValue.isBlank() ? currentValue : newValue;
    }

    private String[] searchPatient(String id) {
        for (String[] patient : patientList) {
            if (patient[0].equals(id)) {
                System.out.println(patient);
                return patient;
            }
        }
        return null;
    }

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

    private String[] getHeader(List<String[]> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    private List<String[]> getData(List<String[]> list) {
        if (list == null || list.size() <= 1) {
            return new ArrayList<>();
        }
        return list.subList(1, list.size());
    }


}
