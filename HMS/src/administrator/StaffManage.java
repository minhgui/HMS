package staff_data;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import administrator.*;
import user.*;


public class StaffManage implements AdministratorManageFunction {

    private static List<String[]> staffList;

    public StaffManage(List<String[]> staffList) {
        this.staffList = staffList;
    }

    public boolean add(String id) {
   
        String role = User.getRoleUsingID(id);
        Scanner sc = new Scanner(System.in);
        System.out.println("Adding staff member with ID: " + id);
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        if (name.isEmpty()) return false;
        System.out.print("Enter Gender (Male/Female): ");
        String gender = sc.nextLine();
        if (gender.isEmpty()) return false;
        System.out.print("Enter Age: ");
        String age = sc.nextLine();
        if(age.isEmpty()) return false;

        staffList.add(new String[]{id, name, role, gender, age});
        System.out.println("Staff member added successfully.");
        return true;
    }

    public void remove(String id) {
        String[] staff = searchStaff(id);

        if (staff != null) {
            staffList.remove(staff);
            System.out.println("Staff member removed successfully.");
        } else {
            System.out.println("Staff member not found.");
        }
    }

    public void modify(String id) {
        String[] staff = searchStaff(id);

        if (staff == null) {
            System.out.println("Staff member not found.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        staff[1] = modifyField("Name", staff[1]);
        staff[3] = modifyField("Gender", staff[3]);
        staff[4] = modifyField("Age", staff[4]);

        System.out.println("Staff information updated successfully.");
    }

    private String modifyField(String field, String currentValue) {
        Scanner sc = new Scanner(System.in);
        System.out.printf("Current %s: %s\n", field, currentValue);
        System.out.print("Enter New Value (Leave blank to keep current): ");
        String newValue = sc.nextLine();
        return newValue.isBlank() ? currentValue : newValue;
    }

    private String[] searchStaff(String id) {
        for (String[] staff : staffList) {
            if (staff[0].equals(id)) {
                return staff;
            }
        }
        return null;
    }

    public List<String[]> filter(List<String[]> staffList)
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("Choose filter criteria:");
        System.out.println("1. Role");
        System.out.println("2. Gender");
        System.out.println("3. Age");

        String[] header = getHeader(staffList);
        List<String[]> data = getData(staffList);
        List<String[]> filteredList = new ArrayList<>();
        filteredList.add(header);

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice)
        {
            case 1: System.out.print("Enter Role (Doctor, Pharmarcist, Administrator): ");
                    String role = sc.nextLine();
                    for (String[] staff : data) {
                        if (staff[2].equalsIgnoreCase(role)) filteredList.add(staff);
                    }
                    break;

            case 2: System.out.print("Enter Gender (Male/Female): ");
                    String gender = sc.nextLine();
                    for (String[] staff : data) {
                        if (staff[3].equalsIgnoreCase(gender)) filteredList.add(staff);
                    }
                    break;

            case 3:
                    System.out.print("Enter Minimum Age: ");
                    int minAge = sc.nextInt();
                    System.out.print("Enter Maximum Age: ");
                    int maxAge = sc.nextInt();
                    sc.nextLine();
                    
                    for (String[] staff : data) {
                            
                        int age = Integer.parseInt(staff[4]);
                        if (age >= minAge && age <= maxAge) filteredList.add(staff);
                        
                    }
                    break;
            default: 
                    System.out.println("Invalid choice.");
        }

        return(filteredList);

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
