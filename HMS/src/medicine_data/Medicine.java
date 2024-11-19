package medicine_data;


public class Medicine {
    private String name;
    private int initialStock;
    private int lowStockLevelAlert;

    public Medicine(String name, int initialStock, int lowStockLevelAlert) {
        this.name = name;
        this.initialStock = initialStock;
        this.lowStockLevelAlert = lowStockLevelAlert;
    }

    public String getName() {
        return name;
    }

    public int getInitialStock() {
        return initialStock;
    }

    public int getLowStockLevelAlert() {
        return lowStockLevelAlert;
    }

    public String toCSVRow() {
        return name + "," + initialStock + "," + initialStock + "," + lowStockLevelAlert + ",0,NIL,NIL";
    }
}