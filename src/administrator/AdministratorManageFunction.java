package administrator;

/**
 * Represents the interface for managing Administrator-related functions.
 * Provides methods to add, remove, and modify records associated with Administrators.
 * @author Jan Chen Jie
 * @version 1.0
 * @since 19/11/2024
 */
public interface AdministratorManageFunction {

    /**
     * Adds a new record associated with the given ID.
     * @param id The ID to be added.
     * @return True if the addition is successful, otherwise false.
     */
    boolean add(String id);

    /**
     * Removes a record associated with the given ID.
     * @param id The ID of the record to be removed.
     */
    void remove(String id);

    /**
     * Modifies a record associated with the given ID.
     * @param id The ID of the record to be modified.
     */
    void modify(String id);
}