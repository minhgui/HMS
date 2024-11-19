package inventory_data;

/**
 * Represents a medicine in the hospital's inventory.
 * Includes details such as the name of the medicine, initial stock, 
 * low stock level alert, and methods to interact with the inventory system.
 * 
 * @author Alvin Ong Minghui
 * @version 3.0.1
 * @since 2024-11-19
 */
public class Medicine {
    private String name;
    private int initialStock;
    private int lowStockLevelAlert;

    /**
     * Constructs a new Medicine object with the specified name, initial stock, and low stock alert level.
     *
     * @param name               The name of the medicine.
     * @param initialStock       The initial stock quantity of the medicine.
     * @param lowStockLevelAlert The stock level at which a low stock alert is triggered.
     */
    public Medicine(String name, int initialStock, int lowStockLevelAlert) {
        this.name = name;
        this.initialStock = initialStock;
        this.lowStockLevelAlert = lowStockLevelAlert;
    }

    /**
     * Retrieves the name of the medicine.
     *
     * @return The name of the medicine.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the initial stock quantity of the medicine.
     *
     * @return The initial stock quantity of the medicine.
     */
    public int getInitialStock() {
        return initialStock;
    }

    /**
     * Retrieves the low stock alert level for the medicine.
     *
     * @return The low stock alert level.
     */
    public int getLowStockLevelAlert() {
        return lowStockLevelAlert;
    }

    /**
     * Converts the medicine's details into a comma-separated values (CSV) row format.
     * This format includes: name, initial stock, current stock (same as initial stock initially),
     * low stock level alert, replenishment request quantity (default 0), 
     * replenishment status (default NIL), and last update status (default NIL).
     *
     * @return A string representing the medicine's details in CSV row format.
     */
    public String toCSVRow() {
        return name + "," + initialStock + "," + initialStock + "," + lowStockLevelAlert + ",0,NIL,NIL";
    }
}