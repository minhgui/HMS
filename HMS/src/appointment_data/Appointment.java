package appointment_data;


public class Appointment {
private String Patient;
private String Doctor;
private String Date;
private String Time;
private String Status;
private String Diagnosis;
private String Treatments;
private String Prescription;
private String Medicine_status;
private String Quantity;
private String Note;

public Appointment(String Patient, String Doctor, String Date, String Time, String status, String diagnosis, String treatments, String prescription, String medicine_status, String quantity, String note) {
	this.Patient=Patient;
	this.Doctor=Doctor;
	this.Date=Date;
	this.Time=Time;
	this.Status=status;
	this.Diagnosis=diagnosis;
	this.Treatments=treatments;
	this.Prescription=prescription;
	this.Medicine_status=medicine_status;
	this.Quantity = quantity;
	this.Note=note;
	
}

public String getPatient() {
	return Patient;
}
public String getDoctor() {
	return Doctor;
}
public String getDate() {
	return Date;
}
public String getTime() {
	return Time;
}
public String getStatus() {
	return Status;
}
public String getDiagnosis() {
	return Diagnosis;
}
public String getTreatments() {
	return Treatments;
}
public String getPrescription() {
	return Prescription;
}
public String getMedicine_status() {
	return Medicine_status;
}
public String getQuantity() {
	return Quantity;
}
public String getNote() {
	return Note;
}
}