package appointment_data;

import java.util.List;

public interface AppointmentInterface {
public  List<Appointment> ReadPatientAppointment();
public  void addAppointment(String[] data);
//public List<Appointment> CancelAppointment();
}
