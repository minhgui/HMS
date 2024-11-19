package medical_equipment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MedicalEquipment {
    private String name;
    private int initialStock;
    private int currentStock;
    private int lowStockLevelAlert;
    private int requestReplenishment;
    private String replenishmentApproved;
    private String lastUpdate;

    // Constructor
    public MedicalEquipment(String name, int initialStock, int lowStockLevelAlert) {
        this.name = name;
        this.initialStock = initialStock;
        this.currentStock = initialStock; // Set current stock to initial stock
        this.lowStockLevelAlert = lowStockLevelAlert;
        this.requestReplenishment = 0;
        this.replenishmentApproved = "-";
        updateLastUpdate("Added");
    }

    // Method to update lastUpdate with current time and a reason
    public void updateLastUpdate(String reason) {
        this.lastUpdate = reason + " on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Method to convert medicine to CSV format
    public String toCsvFormat() {
        return String.format("%s,%d,%d,%d,%d,%s,%s",
                             name, initialStock, currentStock, lowStockLevelAlert,
                             requestReplenishment, replenishmentApproved, lastUpdate);
    }
}