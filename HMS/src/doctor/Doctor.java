package doctor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import medical_record_data.*;
import appointment_data.*;
import user.*;

public class Doctor extends User {

	protected String staffName;
	private String appointmentsPath = "C:/Users/mingh/eclipse-workspace/HMS/Appointment_Record/AppointmentRecord.csv";
	private String medicalRecordsPath = "C:/Users/mingh/eclipse-workspace/HMS/Medical_Records/MedicalRecords.csv";
	private String patientListPath = "C:/Users/mingh/eclipse-workspace/HMS/Patient_List/Patient_List.csv";

	public Doctor(String hospitalID) {
		super(hospitalID);
	}

	public void viewPatientMedicalRecords() {
		List<String> patientsUnderCare = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(medicalRecordsPath))) {
			String line;
			String csvSeparator = ",";
			while ((line = br.readLine()) != null) {
				String[] values = line.split(csvSeparator);

				if (values.length > 8 && values[8].equals(hospitalID) && !patientsUnderCare.contains(values[1])) {
					patientsUnderCare.add(values[1]);
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading medical records: " + e.getMessage());
			return;
		}

		if (patientsUnderCare.isEmpty()) {
			System.out.println("No patients under your care.");
			return;
		}

		System.out.println("Select a patient to view medical records:");
		for (int i = 0; i < patientsUnderCare.size(); i++) {
			System.out.println((i + 1) + ": " + patientsUnderCare.get(i));
		}

		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt() - 1;

		if (choice >= 0 && choice < patientsUnderCare.size()) {
			String selectedPatient = patientsUnderCare.get(choice);

			List<MedicalRecord> medicalRecords = MedicalRecordAction.readCSV(medicalRecordsPath, selectedPatient);

			if (medicalRecords.isEmpty()) {
				System.out.println("No medical records found for " + selectedPatient);
				return;
			}

			System.out.println("Medical Records for " + selectedPatient + ":");

			int i = 1;
			for (MedicalRecord record : medicalRecords) {

				System.out.println(i++);
				System.out.println("Patient ID: " + record.getID());
				System.out.println("Name: " + record.getPatient());
				System.out.println("Date of Birth: " + record.getDOB());
				System.out.println("Gender: " + record.getGender());
				System.out.println("Blood Type: " + record.getBloodType());
				System.out.println("Email: " + record.getEmail());
				System.out.println("Number: " + record.getNumber());
				System.out.println("Appointment Date: " + record.getAppointmentDate());
				System.out.println("Doctor: " + record.getDoctor());
				System.out.println("Doctor ID: " + record.getDoctorID());
				System.out.println("Time: " + record.getAppointmentTime());
				System.out.println("Diagnosis: " + record.getDiagnosis());
				System.out.println("Treatment: " + record.getTreatments());
				System.out.println("Prescription: " + record.getPrescription());
				System.out.println("Amount: " + record.getMedicine_status());
				System.out.println("Notes: " + record.getNote());
				System.out.println("------------------------------------");
				System.out.println("------------------------------------");
			}
		} else {
			System.out.println("Invalid choice.");
		}
	}

	public void updatePatientMedicalRecords() {
		List<String> patientsUnderCare = new ArrayList<>();
		String line;
		String csvSeparator = ",";

		try (BufferedReader br = new BufferedReader(new FileReader(medicalRecordsPath))) {
			while ((line = br.readLine()) != null) {
				String[] values = line.split(csvSeparator);

				if (values.length > 8 && values[8].equals(hospitalID) && !patientsUnderCare.contains(values[1])) {
					patientsUnderCare.add(values[1]);
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading medical records: " + e.getMessage());
			return;
		}

		if (patientsUnderCare.isEmpty()) {
			System.out.println("No patients under your care.");
			return;
		}

		System.out.println("Select a patient to update medical records:");
		for (int i = 0; i < patientsUnderCare.size(); i++) {
			System.out.println((i + 1) + ": " + patientsUnderCare.get(i));
		}

		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt() - 1;
		sc.nextLine();

		if (choice >= 0 && choice < patientsUnderCare.size()) {
			String selectedPatient = patientsUnderCare.get(choice);

			List<String[]> recordsToEdit = new ArrayList<>();
			List<String> allLines = new ArrayList<>();

			try (BufferedReader br = new BufferedReader(new FileReader(medicalRecordsPath))) {
				while ((line = br.readLine()) != null) {
					allLines.add(line);
					String[] values = line.split(csvSeparator);

					if (values.length > 8 && values[1].equals(selectedPatient) && values[8].equals(hospitalID)) {
						recordsToEdit.add(values);
					}
				}
			} catch (IOException e) {
				System.err.println("Error reading medical records: " + e.getMessage());
				return;
			}

			if (recordsToEdit.isEmpty()) {
				System.out.println("No medical records available for editing.");
				return;
			}

			System.out.println("Select a record to edit (1, 2, ...):");
			for (int i = 0; i < recordsToEdit.size(); i++) {
				System.out.println((i + 1) + ": Appointment Date: " + recordsToEdit.get(i)[7] + ", Time: "
						+ recordsToEdit.get(i)[10]);
			}

			int recordChoice = sc.nextInt() - 1;
			sc.nextLine();

			if (recordChoice >= 0 && recordChoice < recordsToEdit.size()) {
				String[] selectedRecord = recordsToEdit.get(recordChoice);

				while (true) {
					System.out.println("What do you want to update?");
					System.out.println("1: Diagnosis");
					System.out.println("2: Treatment");
					System.out.println("3: Prescription");
					System.out.println("4: Amount");
					System.out.println("5: Notes");
					System.out.println("6: Save and Exit");

					int updateChoice = sc.nextInt();
					sc.nextLine();

					switch (updateChoice) {
					case 1:
						System.out.println("Enter new diagnosis:");
						selectedRecord[11] = sc.nextLine();
						break;
					case 2:
						System.out.println("Enter new treatment:");
						selectedRecord[12] = sc.nextLine();
						break;
					case 3:
						System.out.println("Enter new prescription:");
						selectedRecord[13] = sc.nextLine();
						break;
					case 4:
						System.out.println("Enter new Amount:");
						selectedRecord[14] = sc.nextLine();
						break;
					case 5:
						System.out.println("Enter new notes:");
						selectedRecord[15] = sc.nextLine();
						break;
					case 6:
						System.out.println("Saving updates and exiting...");
						break;
					default:
						System.out.println("Invalid choice. Try again.");
						continue;
					}

					if (updateChoice == 6)
						break;
				}

				try (BufferedWriter writer = new BufferedWriter(new FileWriter(medicalRecordsPath))) {
					for (String originalLine : allLines) {
						String[] currentRecord = originalLine.split(csvSeparator);

						if (currentRecord.length > 8 && currentRecord[0].equals(selectedRecord[0])
								&& currentRecord[7].equals(selectedRecord[7])
								&& currentRecord[10].equals(selectedRecord[10])
								&& currentRecord[8].equals(hospitalID)) {
							writer.write(String.join(csvSeparator, selectedRecord));
						} else {
							writer.write(originalLine);
						}
						writer.newLine();
					}
				} catch (IOException e) {
					System.err.println("Error saving medical records: " + e.getMessage());
				}
				System.out.println("Medical record updated successfully.");
			} else {
				System.out.println("Invalid record selection.");
			}
		} else {
			System.out.println("Invalid patient selection.");
		}
	}

	public void viewPersonalSchedule() {
		AppointmentAction appointmentAction = new AppointmentAction(appointmentsPath);
		List<Appointment> validAppointments = appointmentAction.getValidAppointments(staffName);

		if (validAppointments.isEmpty()) {
			System.out.println("No upcoming appointments.");
		} else {
			System.out.println("Your Upcoming Appointments (Status: Booked or Confirmed):");
			for (Appointment app : validAppointments) {
				System.out.println("Patient: " + app.getPatient() + ", Date: " + app.getDate() + ", Time: "
						+ app.getTime() + ", Status: " + app.getStatus());
			}
		}

		List<String> availableSlots = appointmentAction.getAvailableSlots(staffName);

		System.out.println("\nAvailable Slots:");
		for (String slot : availableSlots) {
			System.out.println(slot);
		}
	}

	public void setAvailability() {
		AppointmentAction appointmentAction = new AppointmentAction(appointmentsPath);
		Scanner sc = new Scanner(System.in);

		System.out.println("1. View Available Slots and Update");
		System.out.println("2. View Unavailable Slots and Update");
		int choice = sc.nextInt();
		sc.nextLine();

		if (choice == 1) {

			List<String> availableSlots = appointmentAction.getAvailableSlots(staffName);

			System.out.println("Available Slots:");
			if (availableSlots.isEmpty()) {
				System.out.println("No available slots.");
			} else {
				for (String slot : availableSlots) {
					System.out.println(slot);
				}
			}

			System.out.println("Enter date (d/MM/yyyy):");
			String date = sc.nextLine();

			System.out.println("Enter time (e.g., 1000 for 10:00 AM):");
			String time = sc.nextLine();

			if (appointmentAction.addDoctorSlot(staffName, date, time)) {
				System.out.println("Slot updated successfully.");
			} else {
				System.out.println("Failed to update the slot.");
			}
		} else if (choice == 2) {

			List<String[]> doctorBookedSlots = appointmentAction.getDoctorUnavailableSlots(staffName);

			System.out.println("Unavailable Slots:");
			if (doctorBookedSlots.isEmpty()) {
				System.out.println("No unavailable slots.");
			} else {
				for (int i = 0; i < doctorBookedSlots.size(); i++) {
					String[] slot = doctorBookedSlots.get(i);
					System.out.println((i + 1) + ": Date: " + slot[2] + ", Time: " + slot[3]);
				}

				System.out.println((doctorBookedSlots.size() + 1) + ": Return to the Doctor Menu");
				System.out.println("Select a slot to update:");
				int slotChoice = sc.nextInt() - 1;

				if (slotChoice == doctorBookedSlots.size()) {
					System.out.println("Returning to Doctor Menu...");
					return;
				}

				if (slotChoice >= 0 && slotChoice < doctorBookedSlots.size()) {
					if (appointmentAction.cancelDoctorSlot(doctorBookedSlots.get(slotChoice))) {
						System.out.println("Slot updated successfully.");
					} else {
						System.out.println("Failed to update the slot.");
					}
				} else {
					System.out.println("Invalid slot selection.");
				}
			}
		} else {
			System.out.println("Invalid choice.");
		}
	}

	public void acceptOrDeclineRequests() {
		AppointmentAction appointmentAction = new AppointmentAction(appointmentsPath);

		List<Appointment> validAppointments = appointmentAction.getValidAppointments(staffName);

		List<Appointment> bookedAppointments = new ArrayList<>();
		for (Appointment app : validAppointments) {
			if (app.getStatus().equalsIgnoreCase("Booked")) {
				bookedAppointments.add(app);
			}
		}

		if (bookedAppointments.isEmpty()) {
			System.out.println("No booked appointments to respond to.");
			return;
		}

		System.out.println("Booked Appointments:");
		for (int i = 0; i < bookedAppointments.size(); i++) {
			Appointment app = bookedAppointments.get(i);
			System.out.println((i + 1) + ": Patient: " + app.getPatient() + ", Date: " + app.getDate() + ", Time: "
					+ app.getTime() + ", Status: " + app.getStatus());
		}

		Scanner sc = new Scanner(System.in);
		System.out.println("Select an appointment to respond to (1, 2, 3, ...):");
		int choice = sc.nextInt() - 1;

		if (choice >= 0 && choice < bookedAppointments.size()) {
			Appointment selectedAppointment = bookedAppointments.get(choice);
			System.out.println("1: Confirm, 2: Cancel");
			int decision = sc.nextInt();

			String newStatus = (decision == 1) ? "Confirmed" : "Canceled";

			boolean success = appointmentAction.updateAppointmentStatus(selectedAppointment, newStatus);

			if (success) {
				System.out.println("Appointment status updated to: " + newStatus);
			} else {
				System.out.println("Failed to update appointment status. Please try again.");
			}
		} else {
			System.out.println("Invalid choice. Returning to menu.");
		}
	}

	public void viewUpcomingAppointment() {
		AppointmentAction appointmentAction = new AppointmentAction(appointmentsPath);

		List<Appointment> validAppointments = appointmentAction.getValidAppointments(staffName);

		List<Appointment> bookedAppointments = new ArrayList<>();
		for (Appointment app : validAppointments) {
			if (app.getStatus().equalsIgnoreCase("Confirmed")) {
				bookedAppointments.add(app);
			}
		}

		if (bookedAppointments.isEmpty()) {
			System.out.println("No upcoming appointments.");
			return;
		}

		System.out.println("Confirmed Appointments:");
		for (int i = 0; i < bookedAppointments.size(); i++) {
			Appointment app = bookedAppointments.get(i);
			System.out.println((i + 1) + ": Patient: " + app.getPatient() + ", Date: " + app.getDate() + ", Time: "
					+ app.getTime() + ", Status: " + app.getStatus());
		}

	}

	public void updateAppointmentRecord() {
		AppointmentAction appointmentAction = new AppointmentAction(appointmentsPath);

		appointmentAction.updateAppointmentRecord(staffName);

		if (appointmentAction.isUpdatedFlag()) {
			String[] updatedAppointment = fetchUpdatedAppointment();

			if (updatedAppointment != null) {
				recordToMedicalRecords(updatedAppointment);
			} else {
				System.out.println("No appointment was updated.");
			}
		} else {
			System.out.println("No appointment was updated in the records.");
		}
	}

	private String[] fetchUpdatedAppointment() {
		String[] latestUpdatedAppointment = null;
		String line;
		String csvSeparator = ",";

		try (BufferedReader br = new BufferedReader(new FileReader(appointmentsPath))) {
			while ((line = br.readLine()) != null) {
				String[] values = line.split(csvSeparator);

				if (values.length > 4 && values[1].equals(staffName) && values[4].equals("Completed")) {
					latestUpdatedAppointment = values;
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading appointments: " + e.getMessage());
		}

		return latestUpdatedAppointment;
	}

	public void recordToMedicalRecords(String[] updatedAppointment) {

		String[] patientInfo = null;
		try (BufferedReader br = new BufferedReader(new FileReader(patientListPath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				if (values.length > 1 && values[1].equals(updatedAppointment[0])) { // Match patient name
					patientInfo = values;
					break;
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading patient list: " + e.getMessage());
			return;
		}

		if (patientInfo == null) {
			System.err.println("Patient not found in Patient_List.csv.");
			return;
		}

		String newRecord = String.join(",", patientInfo[0], patientInfo[1], patientInfo[2], patientInfo[3],
				patientInfo[4], patientInfo[5], patientInfo[6], updatedAppointment[2], hospitalID, staffName,
				updatedAppointment[3], updatedAppointment[5], updatedAppointment[6], updatedAppointment[7],
				updatedAppointment[8], updatedAppointment[10]);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(medicalRecordsPath, true))) {
			writer.write(newRecord);
			writer.newLine();
		} catch (IOException e) {
			System.err.println("Error writing to medical records: " + e.getMessage());
		}

		System.out.println("Medical record updated successfully.");
	}

	public void displayMenu() {
		//Hospital hospital = new Hospital();
		//staffName = hospital.getName(hospitalID, this);
		//System.out.println("Welcome Doctor, " + staffName);

		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("---- Doctor Menu ----");
			System.out.println("1. View Patient Medical Records");
			System.out.println("2. Update Patient Medical Records");
			System.out.println("3. View Personal Schedule");
			System.out.println("4. Set Availability for Appointments");
			System.out.println("5. Accept or Decline Appointment Requests");
			System.out.println("6. View Upcoming Appointments");
			System.out.println("7. Record Appointment Outcome");
			System.out.println("8. Logout");

			int choice = sc.nextInt();
			switch (choice) {
			case 1:
				viewPatientMedicalRecords();
				break;
			case 2:
				updatePatientMedicalRecords();
				break;
			case 3:
				viewPersonalSchedule();
				break;
			case 4:
				setAvailability();
				break;
			case 5:
				acceptOrDeclineRequests();
				break;
			case 6:
				viewUpcomingAppointment();
				break;
			case 7:
				updateAppointmentRecord();
				break;
			case 8:
				System.out.println("Logging out...");
				return;
			default:
				System.out.println("Invalid choice. Try again.");
			}
		}
	}
}
