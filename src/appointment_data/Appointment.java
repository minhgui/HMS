
package appointment_data;

/**
 * Represents an appointment in the hospital system.
 * Provides details about the patient, doctor, date, time, status, and other appointment-related information.
 * 
 * @author WU CHENGRUI
 * @author TANG RUIXUAN
 * @version 1.0
 */
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

    /**
     * Constructs an Appointment object with all relevant details.
     * 
     * @param Patient          The name of the patient.
     * @param Doctor           The name of the doctor.
     * @param Date             The date of the appointment.
     * @param Time             The time of the appointment.
     * @param status           The status of the appointment (e.g., Booked, Confirmed, Completed).
     * @param diagnosis        The diagnosis provided during the appointment.
     * @param treatments       The treatments recommended or performed during the appointment.
     * @param prescription     The prescription details provided to the patient.
     * @param medicine_status  The status of the prescribed medicine.
     * @param quantity         The quantity of the prescribed medicine.
     * @param note             Additional notes related to the appointment.
     */
    public Appointment(String Patient, String Doctor, String Date, String Time, String status, 
                       String diagnosis, String treatments, String prescription, 
                       String medicine_status, String quantity, String note) {
        this.Patient = Patient;
        this.Doctor = Doctor;
        this.Date = Date;
        this.Time = Time;
        this.Status = status;
        this.Diagnosis = diagnosis;
        this.Treatments = treatments;
        this.Prescription = prescription;
        this.Medicine_status = medicine_status;
        this.Quantity = quantity;
        this.Note = note;
    }

    /**
     * @return The name of the patient.
     */
    public String getPatient() {
        return Patient;
    }

    /**
     * @return The name of the doctor.
     */
    public String getDoctor() {
        return Doctor;
    }

    /**
     * @return The date of the appointment.
     */
    public String getDate() {
        return Date;
    }

    /**
     * @return The time of the appointment.
     */
    public String getTime() {
        return Time;
    }

    /**
     * @return The status of the appointment (e.g., Booked, Confirmed, Completed).
     */
    public String getStatus() {
        return Status;
    }

    /**
     * @return The diagnosis provided during the appointment.
     */
    public String getDiagnosis() {
        return Diagnosis;
    }

    /**
     * @return The treatments recommended or performed during the appointment.
     */
    public String getTreatments() {
        return Treatments;
    }

    /**
     * @return The prescription details provided to the patient.
     */
    public String getPrescription() {
        return Prescription;
    }

    /**
     * @return The status of the prescribed medicine.
     */
    public String getMedicine_status() {
        return Medicine_status;
    }

    /**
     * @return The quantity of the prescribed medicine.
     */
    public String getQuantity() {
        return Quantity;
    }

    /**
     * @return Additional notes related to the appointment.
     */
    public String getNote() {
        return Note;
    }
}
