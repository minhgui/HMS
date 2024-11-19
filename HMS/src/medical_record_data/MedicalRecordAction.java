package medical_record_data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordAction {
	  public static boolean isValidPath(String filePath) {
	        File file = new File(filePath);
	        return file.exists() && file.isFile() && file.canRead();
	    }
	public static List<MedicalRecord> readCSV(String filePath,String Name) {
        if (!isValidPath(filePath)) {
            System.out.println("INVALID PATH:" + filePath);
        }

    	List<MedicalRecord> RecordList=new ArrayList<MedicalRecord>();
    	String line;
        String csvSeparator = ",";  // Define the separator used in the CSV file (usually a comma)

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
           
            //line = br.readLine();
            /*if (line != null) {
                System.out.println("Headers: " + line);  // Print headers if present
            }*/

            // Read and display the CSV data line by line
            while ((line = br.readLine()) != null) {
                // Split the line by the separator to get individual values
                String[] values = line.split(csvSeparator);

                // Check if the first column matches the target value
                if (values.length > 0 && values[1].equals(Name)) {
                	MedicalRecord medicalrecord=new MedicalRecord(values[0],values[1],values[2],values[3],values[4],values[5],values[6],values[7],values[8],values[9],values[10],values[11],values[12],values[13],values[14],values[15]);
                	RecordList.add(medicalrecord);
                	
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
        return RecordList;
    }
	
}
