package medical_record_data;


public class MedicalRecord {
private String ID;
private String Patient;
private String DOB;
private String Gender;
private String BloodType;
private String Email;
private String Number;
private String AppointmentDate;
private String Doctor;
private String DoctorID;
private String AppointmentTime;
private String Diagnosis;
private String Treatments;
private String Prescription;
private String Medicine_status;
private String Note;

public MedicalRecord(String ID, String Patient, String DOB, String Gender, String BloodType, String Email,
        String Number,String AppointmentDate, String Doctor, String DoctorID, String AppointmentTime,
        String Diagnosis, String Treatments, String Prescription, String Medicine_status,
        String Note) {
this.ID = ID;
this.Patient = Patient;
this.DOB = DOB;
this.Gender = Gender;
this.BloodType = BloodType;
this.Email = Email;
this.Number=Number;
this.AppointmentDate = AppointmentDate;
this.Doctor = Doctor;
this.DoctorID = DoctorID;
this.AppointmentTime = AppointmentTime;
this.Diagnosis = Diagnosis;
this.Treatments = Treatments;
this.Prescription = Prescription;
this.Medicine_status = Medicine_status;
this.Note = Note;
}
public String getID() {
    return ID;
}

public void setID(String ID) {
    this.ID = ID;
}

public String getPatient() {
    return Patient;
}

public void setPatient(String Patient) {
    this.Patient = Patient;
}

public String getDOB() {
    return DOB;
}

public void setDOB(String DOB) {
    this.DOB = DOB;
}

public String getGender() {
    return Gender;
}

public void setGender(String Gender) {
    this.Gender = Gender;
}

public String getBloodType() {
    return BloodType;
}

public void setBloodType(String BloodType) {
    this.BloodType = BloodType;
}

public String getEmail() {
    return Email;
}

public void setEmail(String Email) {
    this.Email = Email;
}
public String getNumber() {
    return Number;
}

public void setNumver(String Number) {
    this.Number = Number;
}
public String getAppointmentDate() {
    return AppointmentDate;
}

public void setAppointmentDate(String AppointmentDate) {
    this.AppointmentDate = AppointmentDate;
}

public String getDoctor() {
    return Doctor;
}

public void setDoctor(String Doctor) {
    this.Doctor = Doctor;
}

public String getDoctorID() {
    return DoctorID;
}

public void setDoctorID(String DoctorID) {
    this.DoctorID = DoctorID;
}

public String getAppointmentTime() {
    return AppointmentTime;
}

public void setAppointmentTime(String AppointmentTime) {
    this.AppointmentTime = AppointmentTime;
}

public String getDiagnosis() {
    return Diagnosis;
}

public void setDiagnosis(String Diagnosis) {
    this.Diagnosis = Diagnosis;
}

public String getTreatments() {
    return Treatments;
}

public void setTreatments(String Treatments) {
    this.Treatments = Treatments;
}

public String getPrescription() {
    return Prescription;
}

public void setPrescription(String Prescription) {
    this.Prescription = Prescription;
}

public String getMedicine_status() {
    return Medicine_status;
}

public void setMedicine_status(String Medicine_status) {
    this.Medicine_status = Medicine_status;
}

public String getNote() {
    return Note;
}

public void setNote(String Note) {
    this.Note = Note;
}



}
