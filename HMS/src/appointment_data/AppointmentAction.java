package appointment_data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class AppointmentAction implements AppointmentInterface {
	public AppointmentAction(String Path) {
		this.Path=Path;
	}
	private static String Path;
	public static List<Appointment> ReadPatientAppointment(String Name){
		
		List<Appointment> app=new ArrayList<Appointment>();
    	String line;
        String csvSeparator = ",";  // Define the separator used in the CSV file (usually a comma)

        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
           
            //line = br.readLine();
            /*if (line != null) {
                System.out.println("Headers: " + line);  // Print headers if present
            }*/

            // Read and display the CSV data line by line
            while ((line = br.readLine()) != null) {
                // Split the line by the separator to get individual values
                String[] values = line.split(csvSeparator);

                // Check if the first column matches the target value
                if (values.length > 0 && values[0].equals(Name)) {
                	Appointment appointment=new Appointment(values[0],values[1],values[2],values[3],values[4],values[5],values[6],values[7],values[8],values[9],values[10]);
                	app.add(appointment);
                	
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
        return app;
	}
    public List<Appointment> ReadPatientAppointment(){
		
		List<Appointment> app=new ArrayList<Appointment>();
    	String line;
        String csvSeparator = ",";  // Define the separator used in the CSV file (usually a comma)

        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
           
            //line = br.readLine();
            /*if (line != null) {
                System.out.println("Headers: " + line);  // Print headers if present
            }*/

            // Read and display the CSV data line by line
            while ((line = br.readLine()) != null) {
                // Split the line by the separator to get individual values
                String[] values = line.split(csvSeparator);

                // Check if the first column matches the target value
               
                	Appointment appointment=new Appointment(values[0],values[1],values[2],values[3],values[4],values[5],values[6],values[7],values[8],values[9],values[10]);
                	app.add(appointment);
                	
                
            }

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
        return app;
	}
	public void addAppointment(String[] data) {
		try (FileWriter writer = new FileWriter(Path,true)) { // 'true' enables append mode
            // Convert data array to CSV row format
            String row = String.join(",", data);
            writer.append(row).append("\n");
            System.out.println("Appointment booked");
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        
	}
	public boolean cancelAppointment(Appointment app) {
		String line;
	    String csvSeparator = ",";  
	    List<String> lines = new ArrayList<>();

	        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
	           
	            while ((line = br.readLine()) != null) {
	                String[] values = line.split(csvSeparator);             
	                if (values.length > 0 && values[0].equals(app.getPatient())&& values[1].equals(app.getDoctor())&& values[2].equals(app.getDate())&& values[3].equals(app.getTime())&& values[4].equals(app.getStatus()))   {
	                	values[4]="Canceled";
	                	
	                	
	                }
	                lines.add(String.join(",", values));
	            }

	        } catch (IOException e) {
	            System.err.println("Error reading the CSV file: " + e.getMessage());
	            return false;
	        }
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Path))) {
	            for (String updatedLine  : lines) {
	                writer.write(updatedLine );
	                writer.newLine();
	            }
	        }catch (IOException e) {
	            System.err.println("Error writing the CSV file: " + e.getMessage());
	            return false;
	        }
	        return true;
	}
	
	public List<Appointment> getValidAppointments(String doctorName) {
        List<Appointment> validAppointments = new ArrayList<>();
        String line;
        String csvSeparator = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSeparator);

                // Validate and process only relevant appointments
                if (values.length > 4 &&
                    (values[4].equalsIgnoreCase("Booked") || values[4].equalsIgnoreCase("Confirmed")) &&
                    !values[0].equalsIgnoreCase("NIL") && 
                    values[1].equalsIgnoreCase(doctorName)) {
                    
                    Appointment appointment = new Appointment(
                        values[0], // Patient
                        values[1], // Doctor
                        values[2], // Date
                        values[3], // Time
                        values[4], // Status
                        values.length > 5 ? values[5] : "", // Diagnosis
                        values.length > 6 ? values[6] : "", // Treatment
                        values.length > 7 ? values[7] : "", // Prescription
                        values.length > 8 ? values[8] : "", // Medicine Status
                        values.length > 9 ? values[9] : "", // Medicine Status
                        values.length > 10 ? values[10] : ""  // Notes
                    );
                    validAppointments.add(appointment);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments file: " + e.getMessage());
        }

        return validAppointments;
    }


	public List<String> getAvailableSlots(String doctorName) {
        List<String> unavailableSlots = new ArrayList<>();
        List<String> availableSlots = new ArrayList<>();
        String line;
        String csvSeparator = ",";
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSeparator);

                if (values.length > 4 && values[1].equals(doctorName) &&
                        (values[4].equals("Booked") || values[4].equals("Confirmed"))) {
                    LocalDate date = LocalDate.parse(values[2], inputFormatter);
                    String formattedDate = date.format(outputFormatter);
                    unavailableSlots.add(formattedDate + "," + values[3]); // Combine date and time as key
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }

        // Generate available slots for 2024/11/2 to 2024/11/8
        for (int i = 2; i <= 8; i++) {
            String date = i + "/11/2024"; // Format: d/MM/yyyy
            for (int time = 1000; time <= 1700; time += 100) {
                String slotKey = date + "," + time;
                if (!unavailableSlots.contains(slotKey)) {
                    availableSlots.add("Date: " + date + ", Time: " + time);
                }
            }
        }

        return availableSlots;
    }

    // Fetch unavailable slots for the doctor
    public List<String[]> getDoctorUnavailableSlots(String doctorName) {
        List<String[]> doctorBookedSlots = new ArrayList<>();
        String line;
        String csvSeparator = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSeparator);
                if (values.length > 4 && values[0].equals("NIL") && values[1].equals(doctorName) && values[4].equals("Confirmed")) {
                    doctorBookedSlots.add(values);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }

        return doctorBookedSlots;
    }

    // Cancel a doctor's unavailable slot
    public boolean cancelDoctorSlot(String[] slotToCancel) {
        List<String> updatedLines = new ArrayList<>();
        String line;
        String csvSeparator = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSeparator);
                if (values[0].equals(slotToCancel[0]) && values[1].equals(slotToCancel[1]) &&
                        values[2].equals(slotToCancel[2]) && values[3].equals(slotToCancel[3])) {
                    values[4] = "Canceled"; // Mark the slot as canceled
                }
                updatedLines.add(String.join(",", values));
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Path))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating appointments: " + e.getMessage());
            return false;
        }

        return true;
    }

    // Add an unavailable slot for the doctor with "NIL" for columns 5-9
    public boolean addDoctorSlot(String doctorName, String date, String time) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Path, true))) {
            writer.write("NIL," + doctorName + "," + date + "," + time + ",Confirmed,NIL,NIL,NIL,NIL,NIL\n");
            return true;
        } catch (IOException e) {
            System.err.println("Error adding slot: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateAppointmentStatus(Appointment appointment, String newStatus) {
        List<String> updatedLines = new ArrayList<>();
        String line;
        String csvSeparator = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSeparator);

                // Match the appointment to update
                if (values[0].equals(appointment.getPatient()) &&
                    values[1].equals(appointment.getDoctor()) &&
                    values[2].equals(appointment.getDate()) &&
                    values[3].equals(appointment.getTime()) &&
                    values[4].equals(appointment.getStatus())) {
                    values[4] = newStatus; // Update status
                }

                updatedLines.add(String.join(",", values));
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
            return false;
        }

        // Write updated CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Path))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating appointments: " + e.getMessage());
            return false;
        }

        return true;
    }
    
    private boolean updatedFlag = false; // Class-level flag to indicate if an update occurred

    public void updateAppointmentRecord(String doctorName) {
        List<String[]> confirmedAppointments = new ArrayList<>();
        String line;

        // Collect confirmed appointments for the specific doctor
        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > 4 && values[4].equals("Confirmed") && values[1].equals(doctorName) && !values[0].equals("NIL")) {
                    confirmedAppointments.add(values);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
            return;
        }

        if (confirmedAppointments.isEmpty()) {
            System.out.println("No confirmed appointments to update.");
            updatedFlag = false; // Explicitly set to false
            return;
        }

        // Display confirmed appointments
        System.out.println("Confirmed Appointments:");
        for (int i = 0; i < confirmedAppointments.size(); i++) {
            String[] app = confirmedAppointments.get(i);
            System.out.println((i + 1) + ": Patient: " + app[0] + ", Date: " + app[2] + ", Time: " + app[3]);
        }

        // Select an appointment to update
        Scanner sc = new Scanner(System.in);
        System.out.println("Select an appointment to update (1, 2, 3, ...):");
        int choice = sc.nextInt() - 1;
        sc.nextLine(); // Consume newline

        if (choice < 0 || choice >= confirmedAppointments.size()) {
            System.out.println("Invalid choice.");
            updatedFlag = false; // Explicitly set to false
            return;
        }

        String[] selectedAppointment = confirmedAppointments.get(choice);

        // Begin updating the selected appointment
        List<String> updatedLines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                // Match the selected appointment
                if (values.length > 4 && values[0].equals(selectedAppointment[0]) &&
                        values[1].equals(selectedAppointment[1]) &&
                        values[2].equals(selectedAppointment[2]) &&
                        values[3].equals(selectedAppointment[3])) {

                    // Ensure all required indices exist
                    while (values.length < 11) { // Adjusted for the added quantity column
                        String[] extendedValues = new String[11];
                        System.arraycopy(values, 0, extendedValues, 0, values.length);
                        values = extendedValues;
                    }

                    // Update each field one by one
                    System.out.println("Enter new Diagnosis:");
                    values[5] = sc.nextLine();

                    System.out.println("Enter new Type of Service:");
                    values[6] = sc.nextLine();

                    System.out.println("Enter new Prescribed Medications:");
                    values[7] = sc.nextLine();

                    System.out.println("Enter new Quantity of Medications:");
                    values[8] = sc.nextLine();

                    System.out.println("Enter new Medication Status (e.g., Pending, Dispensed):");
                    values[9] = sc.nextLine();

                    System.out.println("Enter new Notes:");
                    values[10] = sc.nextLine();

                    // Mark as completed
                    values[4] = "Completed";
                    updatedFlag = true; // Set the flag to true if an update occurred

                    // Print updated record for confirmation
                    System.out.println("Updated Record:");
                    System.out.println("Diagnosis: " + values[5]);
                    System.out.println("Type of Service: " + values[6]);
                    System.out.println("Prescribed Medications: " + values[7]);
                    System.out.println("Quantity of Medications: " + values[8]);
                    System.out.println("Medication Status: " + values[9]);
                    System.out.println("Notes: " + values[10]);
                    System.out.println("Status: " + values[4]);
                }

                // Add the (possibly updated) line back to the list of updated lines
                updatedLines.add(String.join(",", values));
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
            return;
        }

        // Write the updated lines back to the CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Path))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating appointments: " + e.getMessage());
            return;
        }

        if (updatedFlag) {
            System.out.println("Appointment record updated successfully and marked as Completed.");
        } else {
            System.out.println("No updates were made.");
        }
    }

    // Add a getter for the updatedFlag
    public boolean isUpdatedFlag() {
        return updatedFlag;
    }

    public void viewAppointmentRecords() {
        System.out.println("Viewing appointment records...\n");

        // A map to group records by patient names
        Map<String, List<String[]>> groupedRecords = new TreeMap<>(); // TreeMap to ensure sorted order by patient name

        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            String line;
            boolean isFirstLine = true;

            // Read each line and group by patient name
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header row if it exists
                    continue;
                }

                String[] values = line.split(",");
                if (values.length == 11) {
                    String patientName = values[0]; // Assuming the first column is the patient's name
                    groupedRecords.putIfAbsent(patientName, new ArrayList<>());
                    groupedRecords.get(patientName).add(values);
                } else {
                    System.out.println("Invalid row format: " + line);
                }
            }

            // Print grouped records
            for (String patientName : groupedRecords.keySet()) {
                System.out.println("\nName of Patient: " + patientName);
                System.out.println("Appointments: ");

                // Print the records for the patient in the desired format
                for (String[] record : groupedRecords.get(patientName)) {
                    System.out.println("    Name of Doctor: " + record[1]);
                    System.out.println("    Date of Appointment: " + record[2]);
                    System.out.println("    Time of Appointment: " + record[3]);
                    System.out.println("    Appointment Status: " + record[4]);
                    System.out.println("    Patient Diagnosis: " + record[5]);
                    System.out.println("    Patient Treatment: " + record[6]);
                    System.out.println("    Prescribed Medication: " + record[7]);
                    System.out.println("    Medication Quantity: " + record[8]);
                    System.out.println("    Prescription Status: " + record[9]);
                    System.out.println("    Remarks: " + record[10]);
                    System.out.println(); // Blank line for better separation
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
}