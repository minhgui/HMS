
package utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides utility functions for file operations such as reading and writing CSV files.
 * Handles file validation, reading, and updating line-by-line.
 * @author Jan Chen Jie
 * @version 1.0
 * @since 19/11/2024
 */
public class Utility {

    /**
     * Checks if the given file path is valid, readable, and refers to a file.
     * @param filePath The file path to validate.
     * @return True if the file path is valid, otherwise false.
     */
    public static boolean isValidPath(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile() && file.canRead();
    }

    /**
     * Reads data from a CSV file and returns it as a list of string arrays.
     * @param filePath The path of the CSV file to read.
     * @param removeHeaderFlag A flag to indicate whether to skip the header row (1 to remove, 0 to keep).
     * @return A list of string arrays representing the CSV rows.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public static List<String[]> readCSV(String filePath, int removeHeaderFlag) throws IOException {
        if (!isValidPath(filePath)) {
            System.out.println("INVALID PATH: " + filePath);
        }

        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            if (removeHeaderFlag == 1) line = br.readLine(); // Remove header line only
            while ((line = br.readLine()) != null) {
                String[] rows = line.split(",");
                data.add(rows);
            }
        }
        return data;
    }

    /**
     * Updates a CSV file line by line, replacing any "null" values with empty strings.
     * @param filePath The path of the CSV file to update.
     * @throws IOException If an I/O error occurs while updating the file.
     */
    public static void updateLineByLineCSV(String filePath) throws IOException {
        if (!isValidPath(filePath)) {
            System.out.println("INVALID PATH");
            return;
        }

        List<String[]> updateData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] rows = line.split(",");
                for (int i = 0; i < rows.length; i++) {
                    if (rows[i].equalsIgnoreCase("null")) {
                        rows[i] = "";
                    }
                }
                updateData.add(rows);
            }
        }

        // Optionally write the updated data back to the file
        writeCSV(filePath, updateData);
    }

    /**
     * Writes data to a CSV file, overwriting its current content.
     * @param filePath The path of the CSV file to write to.
     * @param data A list of string arrays representing the rows to write.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public static void writeCSV(String filePath, List<String[]> data) throws IOException {
        if (!isValidPath(filePath)) {
            System.out.println("INVALID PATH");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : data) {
                String line = String.join(",", row); // Convert array to comma-separated string
                bw.write(line);                      // Write the string to the file
                bw.newLine();                        // Add a newline character
            }
        }
    }
}
