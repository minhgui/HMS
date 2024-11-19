package user;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import administrator.*;


public class UserManage implements AdministratorManageFunction {
    private List<String[]> userList;

    public UserManage(List<String[]> userList) {
        this.userList = userList;
    }

    public boolean add(String id) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Username: ");
        String username = sc.nextLine();
        if (username.isEmpty()) return false;
        System.out.print("Enter Password: ");
        String password = sc.nextLine();
        if (password.isEmpty()) return false;
        String isChangeUser = "TRUE";
        String isChangepass = "TRUE";

        userList.add(new String[]{id, username, password,isChangeUser,isChangepass});
        System.out.println("User credentials added successfully.");
        return true;
    }

    public void remove(String id) {
        String[] user = searchUser(id);

        if (user != null) {
            userList.remove(user);
            System.out.println("User credentials removed successfully.");
        } else {
            System.out.println("User not found.");
        }
    }

    public void modify(String id) {
        String[] user = searchUser(id);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        user[1] = modifyField("Username", user[1]);
        user[2] = modifyField("Password", user[2]);

        System.out.println("User credentials updated successfully.");
    }

    private String modifyField(String field, String currentValue) {
        Scanner sc = new Scanner(System.in);
        System.out.printf("Current %s: %s\n", field, currentValue);
        System.out.print("Enter New Value (Leave blank to keep current): ");
        String newValue = sc.nextLine();
        return newValue.isBlank() ? currentValue : newValue;
    }

    private String[] searchUser(String id) {
        for (String[] user : userList) {
            if (user[0].equals(id)) {
                return user;
            }
        }
        return null;
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
