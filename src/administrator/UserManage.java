
package administrator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents the management of user credentials in the hospital system.
 * Provides functionality to add, remove, and modify user credentials.
 * @author Jan Chen Jie
 * @version 1.0
 * @since 19/11/2024
 */
public class UserManage implements AdministratorManageFunction {

    /**
     * The list of user credentials.
     */
    private List<String[]> userList;

    /**
     * Creates a new UserManage instance with the given user list.
     * @param userList The list of user credentials.
     */
    public UserManage(List<String[]> userList) {
        this.userList = userList;
    }

    /**
     * Adds a new user credential to the list.
     * @param id The unique ID of the user.
     * @return True if the user was added successfully, otherwise false.
     */
    public boolean add(String id) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Username: ");
        String username = sc.nextLine();
        if (username.isEmpty()) return false;
        System.out.print("Enter Password: ");
        String password = sc.nextLine();
        if (password.isEmpty()) return false;
        String isChangeUser = "TRUE";
        String isChangePass = "TRUE";

        userList.add(new String[]{id, username, password, isChangeUser, isChangePass});
        System.out.println("User credentials added successfully.");
        return true;
    }

    /**
     * Removes a user credential from the list based on the given ID.
     * @param id The ID of the user to remove.
     */
    public void remove(String id) {
        String[] user = searchUser(id);
        if (user != null) {
            userList.remove(user);
            System.out.println("User credentials removed successfully.");
        } else {
            System.out.println("User not found.");
        }
    }

    /**
     * Modifies the username and/or password of a user based on the given ID.
     * @param id The ID of the user to modify.
     */
    public void modify(String id) {
        String[] user = searchUser(id);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        user[1] = modifyField("Username", user[1]);
        user[2] = modifyField("Password", user[2]);

        System.out.println("User credentials updated successfully.");
    }

    /**
     * Prompts the user to modify a specific field of a user's credentials.
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
     * Searches for a user in the list based on the given ID.
     * @param id The ID of the user to search for.
     * @return The user record if found, otherwise null.
     */
    private String[] searchUser(String id) {
        for (String[] user : userList) {
            if (user[0].equals(id)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Retrieves the header row from the user list.
     * @param list The list of user credentials.
     * @return The header row if present, otherwise null.
     */
    private String[] getHeader(List<String[]> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Retrieves the data rows from the user list, excluding the header.
     * @param list The list of user credentials.
     * @return A list containing only the data rows.
     */
    private List<String[]> getData(List<String[]> list) {
        if (list == null || list.size() <= 1) {
            return new ArrayList<>();
        }
        return list.subList(1, list.size());
    }
}
