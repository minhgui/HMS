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

public class Hospital {

    protected String hospitalID;

    private static final String STAFF_FILE_PATH = "C:/Users/mingh/eclipse-workspace/HMS/Staff_List/Staff_List.csv";
    private static final String PATIENT_FILE_PATH = "C:/Users/mingh/eclipse-workspace/HMS/Patient_List/Patient_List.csv";
    private static final String MEDICINE_FILE_PATH = "C:/Users/mingh/eclipse-workspace/HMS/Medicine_List/Medicine_List.csv";

    private List<String[]> staffList;
    private List<String[]> patientList;
    private List<String[]> medicineList;

    private String name;


    public Hospital() {
    }

    

    private List<String[]> loadStaff() {
        List<String[]> allStaff = new ArrayList<>();
        try {
            allStaff = Utility.readCSV(STAFF_FILE_PATH, 0);
        } catch (IOException e) {
            System.out.println("Error in loadStaff: " + e.getMessage());
        }
        return allStaff;
    }

    private List<String[]> loadPatients() {
        List<String[]> allPatients = new ArrayList<>();
        try {
            allPatients = Utility.readCSV(PATIENT_FILE_PATH, 0);
        } catch (IOException e) {
            System.out.println("Error in loadPatients: " + e.getMessage());
        }
        return allPatients;
    }

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

    private List<String[]> loadMedicine() {
        List<String[]> allMedicine = new ArrayList<>();
        try {
            allMedicine = Utility.readCSV(MEDICINE_FILE_PATH, 0);
        } catch (IOException e) {
            System.out.println("Error in loading Medicine: " + e.getMessage());
        }
        return allMedicine;
    }

    public List<String[]> getStaff(User user) {
        if (user instanceof Administrator) {
            return loadStaff();
        } else {
            throw new SecurityException("Access denied");
        }
    }

    public List<String[]> getPatient(User user) {
        if (user instanceof Doctor || user instanceof Administrator) {
            return loadPatients();
        } else {
            throw new SecurityException("Access denied");
        }
    }

    protected List<String[]> getPatientByID(String patientID, User user) {
        if (user instanceof Patient || user instanceof Doctor || user instanceof Administrator) {
            return loadPatientByID(patientID);
        } else {
            throw new SecurityException("Access denied");
        }
    }

    protected List<String[]> getMedicine(User user) {
        if (user instanceof Pharmacist || user instanceof Administrator) {
            return loadMedicine();
        } else {
            throw new SecurityException("Access denied");
        }
    }

    private void writePatientCSV(List<String[]>data)
    {
        try{
            Utility.writeCSV(PATIENT_FILE_PATH, data);
        }
        catch(IOException e)
        {
            System.out.println("Error in Writing Patient: " + e.getMessage());
        }

    }

    private void writeStaffCSV(List<String[]>data)
    {
        try{
            Utility.writeCSV(STAFF_FILE_PATH, data);
        }
        catch(IOException e)
        {
            System.out.println("Error in Writing Staff: " + e.getMessage());
        }

    }

    public void writePatient(List<String[]>data, User user)
    {
        if (user instanceof Administrator) writePatientCSV(data);
    }

    public void writeStaff(List<String[]>data, User user)
    {
        if (user instanceof Administrator) writeStaffCSV(data);
    }

    public String getName(String hospitalID, User user)
    {
        String name = searchName(hospitalID, user);
        return name;

    }

    private String searchName(String hospitalID, User user)
    {
        if (user instanceof Administrator || user instanceof Doctor || user instanceof Pharmacist)
        {
            staffList = loadStaff();
            for (String[] staff : staffList)
            {
                if (staff[0].equals(hospitalID)) return staff[1];
            }
        }

        else if (user instanceof Patient)
        {
            patientList = loadPatients();
            for (String[] patient : patientList)
            {
                if (patient[0].equals(hospitalID)) return patient[1];
            }

        }

        return null;

    }



}
