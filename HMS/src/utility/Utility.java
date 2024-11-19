package utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utility {
    // ONLY DOES READING AND WRITTING OF FILE


    public static boolean isValidPath(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile() && file.canRead();
    }

    public static List<String[]> readCSV(String filePath, int removeHeaderFlag) throws IOException {
        if (!isValidPath(filePath)) {
            System.out.println("INVALID PATH:" + filePath);
        }

        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            if (removeHeaderFlag == 1) line = br.readLine(); // remove headerline only
            while ((line = br.readLine()) != null) {
                String[] rows = line.split(",");
                data.add(rows);
            }
        }
        return data;
    }

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
    }

    public static void writeCSV(String filePath,List<String[]> data) throws IOException{
        String header;

        if (!isValidPath(filePath)) {
            System.out.println("INVALID PATH");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath)))
        {
            for (String[] row : data) {
                String line = String.join(",", row); 
                bw.write(line);                      
                bw.newLine();                     
            }
        }

    }

}
