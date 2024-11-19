
/**
Represents a patient menu, a patient can use this class to deal medical record and Appointment
@author: Tang Ruixuan
@version: 1.0
@since: 19/11/2024

*/



package patient;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.io.*;  

import user.*;
import appointment_data.*;
import medical_record_data.*;
import hospital.*;


public class Patient extends User {
    
    /** the name of this patient*/
    protected String staffName;
    /**the path of file needed
    */
    private String Path="./Appointment_Record/AppointmentRecord.csv";
    private String MedicalRecordPath="./Medical_Records/MedicalRecords.csv";
    private String PatientInfoPath="./Patient_List/Patient_List.csv";
    /**
    get patient name*/
    public Patient(String hospitalID)
    {
        super(hospitalID);        
    }

    /** Present a patient's upcoming Appointments
    @param staffName This is patient's name
    @return this patietn's upcoming appointment
    */
    public List<Appointment> PresentAppointment(String staffName) {
    	AppointmentAction appointmentaction=new AppointmentAction();
    	List<Appointment> app=appointmentaction.getPatientbyName(staffName);
    	app.removeIf(appointment -> appointment.getStatus().equals("Canceled"));
    	LocalDate today = LocalDate.now();
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
    	app.removeIf(appointment -> {
    	    try {
    	    	String dateStr = appointment.getDate().trim();
    	    	LocalDate csvDate = LocalDate.parse(dateStr, formatter);
    	        
    	        return csvDate.isBefore(today);
    	    } catch (DateTimeParseException e) {
    	        e.printStackTrace();
    	        return false; 
    	    }
    	});

    	
        int i2=0;
        for (int i = 0; i < app.size(); i++) {
        	i2++;
        	System.out.print(i2);
            System.out.print(" Patient: "+app.get(i).getPatient());
            System.out.print(" Doctor: "+app.get(i).getDoctor());
            System.out.print(" Date: "+app.get(i).getDate());
            System.out.print(" Time: "+app.get(i).getTime());
            System.out.println(" Status: "+app.get(i).getStatus());
        }
        if(app.size()==0) {
        	System.out.print("No upcoming appointments");
            return app;
        }
        return app;
    }
    /**get the total number of doctors
    @return number of doctors
    */
    public static int getDocNum() {
    	int i=0;
    	String line;
        String csvSeparator = ","; 

        try (BufferedReader br = new BufferedReader(new FileReader("./Staff_List/Staff_List.csv"))) {
            
            while ((line = br.readLine()) != null) {
          
                String[] values = line.split(csvSeparator);


                if (values.length > 0 && values[2].equals("Doctor")) {
                	i++;
                	
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
    	return i;
    }
    /**get the list of all doctors
    @return list of all doctors
    */
    public static ArrayList<String> getDocList(){
    	ArrayList<String> NameList = new ArrayList<>();
    	String line;
        String csvSeparator = ","; 

        try (BufferedReader br = new BufferedReader(new FileReader("./Staff_List/Staff_List.csv"))) {

            while ((line = br.readLine()) != null) {

                String[] values = line.split(csvSeparator);

            
                if (values.length > 0 && values[2].equals("Doctor")) {
                	NameList.add(values[1]);
                	
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
    	return NameList;
    	
    }
    /**
    present next 7 days from today, then present avaliablity of every 1 hour
    */
    
    public void CheckAvaliableDates() {
    	List<Appointment> app=new ArrayList<Appointment>();
    	while(true) {
    	app.clear();   	
    	int numofDoc=getDocNum();
    	ArrayList<String> NameList = getDocList();
    	String line;
        String csvSeparator = ",";  
        String apptime;
    	LocalDate today = LocalDate.now();
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

        for (int i = 0; i <= 7; i++) {
            LocalDate nextDay = today.plusDays(i);
            System.out.println(i+ ": " + nextDay);
        }
        System.out.println("8: Return to Patient Menu");
    	System.out.println("Check which date?");
    	Scanner sc = new Scanner(System.in);
        int choice=sc.nextInt();
        LocalDate selectedDate;
        if (choice >= 0 && choice <= 7) {
            selectedDate = today.plusDays(choice);
        } else if(choice==8) {return;}
        else{
            System.out.println("Invalid date");
            continue;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            line = br.readLine();

     
            while ((line = br.readLine()) != null) {
   
                line = line.trim();

                String[] values = line.split(csvSeparator);

                if (values.length >= 3) {
                    String dateStr = values[2].trim();  

                    try {
                       
                        LocalDate csvDate = LocalDate.parse(dateStr, formatter);

                        
                        if (csvDate.isEqual(selectedDate)) {
                            
                            Appointment appointment=new Appointment(values[0],values[1],values[2],values[3],values[4],values[5],values[6],values[7],values[8],values[9],values[10]);
                        	app.add(appointment);
                        }
                    } catch (DateTimeParseException e) {
                        System.err.println("Error parsing date in row: " + line);
                    }
                } else {
                   
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
        if(app.size()!=0) {
        
        for(int i=0;i<=7;i++) {
        	int time=1000;
        	int AvaliableDoc=numofDoc;
        	ArrayList<String> avaliableList=new ArrayList<>(NameList);
        	time+=i*100;
        	
        	for (int i2 = 0; i2 < app.size(); i2++) {
            	
        		if(Integer.valueOf(app.get(i2).getTime())==time&&app.get(i2).getStatus().equals("Confirmed")) {
        			AvaliableDoc--;
        			avaliableList.remove(app.get(i2).getDoctor());
        			
        		}
        		}
        	if(AvaliableDoc==0) {
        		System.out.println("Time:"+time+" "+" no doctor Avaliable ");
        	}else {
        		System.out.println("Time:"+time+" "+ AvaliableDoc+" doctors Avaliable: "+avaliableList);
        	}
       
        }
        
        }else {
        	int AvaliableDoc=numofDoc;
        	ArrayList<String> avaliableList=new ArrayList<>(NameList);
        	 for(int i=0;i<=7;i++) {
        		 int time=1000;
        		 time+=i*100;
        		 System.out.println("Time:"+time+" "+ AvaliableDoc+" doctors Avaliable: "+avaliableList);
        	 }
        }
    	continue;}
    	}
    /**book the appointment from today to next 7 days 
    check the avaliablity of each timeslot and each Doctor
    update the csv file if slot is avaliable
    */
    public void BookAppointment(String Name) {
    	AppointmentAction appointmentAction=new AppointmentAction();
    	List<Appointment> app1=new ArrayList<Appointment>();
    	List<Appointment> app=new ArrayList<Appointment>();
    	outerLoop:
    	while(true) {
        	app.clear(); 
        	app1.clear();
        	int numofDoc=getDocNum();
        	ArrayList<String> NameList = getDocList();
        	String line;
            String csvSeparator = ",";  
        	LocalDate today = LocalDate.now();
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

            for (int i = 0; i <= 7; i++) {
                LocalDate nextDay = today.plusDays(i);
                System.out.println(i+ ": " + nextDay);
            }
            System.out.println("8: Return to Patient Menu");
        	System.out.println("Book which date?");
        	Scanner sc = new Scanner(System.in);
            int choice=sc.nextInt();
            LocalDate selectedDate;
            if (choice >= 0 && choice <= 7) {
                selectedDate = today.plusDays(choice);
            } else if(choice==8) {return;}
            else{
                System.out.println("Invalid date");
                continue;
            }
          
            app1=appointmentAction.ReadPatientAppointment();
            
            for(int i=0;i<app1.size();i++) {
            	String dateStr = app1.get(i).getDate().trim();
            	try {
                    LocalDate csvDate = LocalDate.parse(dateStr, formatter);

                    if (csvDate.isEqual(selectedDate)) {
           
        
                    	app.add(app1.get(i));
                    }
                } catch (DateTimeParseException e) {
                    System.err.println("Error parsing date in row: " );
                }
            	
            }
            System.out.println(app.size());
            if(app.size()!=0) {
            
            for(int i=0;i<=7;i++) {
            	int time=1000;
            	int AvaliableDoc=numofDoc;
            	ArrayList<String> avaliableList=new ArrayList<>(NameList);
            	time+=i*100;
            	
            	for (int i2 = 0; i2 < app.size(); i2++) {
                	
            		if(Integer.valueOf(app.get(i2).getTime())==time&&(app.get(i2).getStatus().equals("Booked")||app.get(i2).getStatus().equals("Confirmed"))) {
            			AvaliableDoc--;
            			avaliableList.remove(app.get(i2).getDoctor());
            			
            		}
            		}
            	if(AvaliableDoc==0) {
            		System.out.println("Time:"+time+" no doctors Avaliable: ");
            		
            	}else {
            		System.out.println("Time:"+time+" "+ AvaliableDoc+" doctors Avaliable: "+avaliableList);
            	
            	}
           
            }
            
            }else {
            	int AvaliableDoc=numofDoc;
            	ArrayList<String> avaliableList=new ArrayList<>(NameList);
            	 for(int i=0;i<=7;i++) {
            		 int time=1000;
            		 time+=i*100;
            		 System.out.println("Time:"+time+" "+ AvaliableDoc+" doctors Avaliable: "+avaliableList);
            	 }
            }
            System.out.println("Choose a timeslot, enter 8 to choose another date ");
            String apptime=sc.next();
            if(apptime.equals("8"))
            	{app.clear(); 
            	app1.clear();
            	continue;
            	}
            if(!apptime.matches("1000|1100|1200|1300|1400|1500|1600|1700")) {
            	System.out.println("Invalid timeslot, choose another one ");
            	continue outerLoop;
            }
            sc.nextLine();
        	System.out.println("Choose a doctor: ");
    		String bookdoc=sc.nextLine();
    		for (int i2 = 0; i2 < app.size(); i2++) {
            	
        		if(app.get(i2).getTime().equals(apptime)&&app.get(i2).getDoctor().equals(bookdoc)&&(app.get(i2).getStatus().equals("Booked")||app.get(i2).getStatus().equals("Confirmed")) ){
        			
        				System.out.println("This doctor has been booked , choose another one ");
        				continue outerLoop;
        			
        			
        		}
        		
        		}
    		if(!NameList.contains(bookdoc)) {
    			System.out.println("This doctor is not in staff list");
    			continue;
    		}
    		
    		
    		String[] data = {Name,bookdoc ,formatter.format(selectedDate).toString(), apptime,"Booked","NIL","NIL","NIL","NIL","NIL","NIL"};


            appointmentAction.addAppointment(data);
            
        	return;}
    }
    /** cancel a booked appointment, this method will present the booked and Confirmed appointments and user select one to cancel,
    after cancel, the status will become "canceled"
    */
    
public void CancelAppointment(String Name) {
	int choice;
	Scanner sc=new Scanner(System.in);
	while(true) {
	List<Appointment> myapp=PresentAppointment(staffName);
	System.out.println("Select your appoint to be canceled, or press 0 return to main menu ");
	choice=sc.nextInt();
	if(choice==0)
		return;
	choice--;
	if((choice<0)||(choice>=myapp.size()))
	{
		System.out.println("Invalid option");
		continue;
	}
	if(myapp.get(choice).getStatus().equals("Canceled"))
		{System.out.println("It's already canceled!");
		continue;
		}
	
	AppointmentAction appointmentaction=new AppointmentAction();
	boolean result=appointmentaction.cancelAppointment(myapp.get(choice));
	if(result)
        System.out.println("Appointment canceled");
	else
		System.out.println("Something wrong, try again");
    return;
	}	
}
/**reschedule the appointment, user will select a booked/confirmed appointment, then choose one of the dates in 7 days from today, then reschedule this appointment into any empty slots 
*/

public void RescheduleAppointment(String Name) {
	int choice;
	String line;
	int numofDoc=getDocNum();
	ArrayList<String> NameList = getDocList();
	String csvSeparator = ",";  
	LocalDate selectedDate = null;
	Scanner sc=new Scanner(System.in);
	while(true) {
	System.out.println("Select your appoint to be rescheduled, or press 0 return to main menu ");
	List<Appointment> myapp=PresentAppointment(staffName);
	choice=sc.nextInt();
	
	if(choice==0)
		return;
	choice--;
	if((choice<0)||(choice>=myapp.size()))
	{
		System.out.println("Invalid option");
		continue;
	}
	if(myapp.get(choice).getStatus().equals("Canceled"))
		{System.out.println("It's already canceled!");
		}
			
	
	LocalDate today = LocalDate.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
	
	
		
	
	for (int i = 0; i <= 7; i++) {
        LocalDate nextDay = today.plusDays(i);
        System.out.println(i+ ": " + nextDay);
    }
    System.out.println("8: Return to Patient Menu");
	System.out.println("Book which date?");
    int choice2=sc.nextInt();
    
    if (choice2 >= 0 && choice2 <= 7) {
        selectedDate = today.plusDays(choice2);
    } else if(choice2==8) {return;}
    else{
        System.out.println("Invalid date");
        continue;
    }
    
    List<Appointment> app=new ArrayList<Appointment>();
    try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
  
        line = br.readLine();

        while ((line = br.readLine()) != null) {
        
            line = line.trim();


            String[] values = line.split(csvSeparator);

            if (values.length >= 3) {
                String dateStr = values[2].trim();  
           

                try {
                   
                    LocalDate csvDate = LocalDate.parse(dateStr, formatter);

                  
                    if (csvDate.isEqual(selectedDate)) {
                        
                        Appointment appointment=new Appointment(values[0],values[1],values[2],values[3],values[4],values[5],values[6],values[7],values[8],values[9],values[10]);
                    	app.add(appointment);
                    }
                } catch (DateTimeParseException e) {
                    System.err.println("Error parsing date in row: " + line);
                }
            } else {
               
            }
        }

    } catch (IOException e) {
        System.err.println("Error reading the CSV file: " + e.getMessage());
    }
    
    if(app.size()!=0) {
        
        for(int i=0;i<=7;i++) {
        	int time=1000;
        	int AvaliableDoc=numofDoc;
        	ArrayList<String> avaliableList=new ArrayList<>(NameList);
        	time+=i*100;
        	
        	for (int i2 = 0; i2 < app.size(); i2++) {
            	
        		if(Integer.valueOf(app.get(i2).getTime())==time&&(app.get(i2).getStatus().equals("Booked")||app.get(i2).getStatus().equals("Confirmed"))) {
        			AvaliableDoc--;
        			avaliableList.remove(app.get(i2).getDoctor());
        			
        		}
        		}
        	if(AvaliableDoc==0) {
        		System.out.println("Time:"+time+" no doctors Avaliable: ");
        		
        	}else {
        		System.out.println("Time:"+time+" "+ AvaliableDoc+" doctors Avaliable: "+avaliableList);
        	
        	}
       
        }
        
        }else {
        	int AvaliableDoc=numofDoc;
        	ArrayList<String> avaliableList=new ArrayList<>(NameList);
        	 for(int i=0;i<=7;i++) {
        		 int time=1000;
        		 time+=i*100;
        		 System.out.println("Time:"+time+" "+ AvaliableDoc+" doctors Avaliable: "+avaliableList);
        	 }
        }
        System.out.println("Choose a timeslot, enter 8 to choose another date ");
        String apptime=sc.next();
        if(apptime.equals("8"))
        	continue;
        sc.nextLine();
    	System.out.println("Choose a doctor: ");
		String bookdoc=sc.nextLine();
		for (int i2 = 0; i2 < app.size(); i2++) {
        	
    		if(app.get(i2).getTime().equals(apptime)&&app.get(i2).getDoctor().equals(bookdoc)&&(app.get(i2).getStatus().equals("Confirmed")||app.get(i2).getStatus().equals("Booked"))) {
    			
    				System.out.println("This doctor has been booked , choose another one ");
    			
    			
    		}
    		
    		}
		if(!NameList.contains(bookdoc)) {
			System.out.println("This doctor is not in staff list");
			continue;
		}
		List<String> lines = new ArrayList<>();
		 try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
	           
	            while ((line = br.readLine()) != null) {
	                String[] values = line.split(csvSeparator);             
	                if (values.length > 0 && values[0].equals(myapp.get(choice).getPatient())&& values[1].equals(myapp.get(choice).getDoctor())&& values[2].equals(myapp.get(choice).getDate())&& values[3].equals(myapp.get(choice).getTime())&& values[4].equals(myapp.get(choice).getStatus()))   {
	                	values[1]=bookdoc;
	                	values[2]=formatter.format(selectedDate).toString();
	                	values[3]=apptime;
	                	values[4]="Booked";
	                	
	                	
	                }
	                lines.add(String.join(",", values));
	            }

	        } catch (IOException e) {
	            System.err.println("Error reading the CSV file: " + e.getMessage());
	        }
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Path))) {
	            for (String updatedLine  : lines) {
	                writer.write(updatedLine );
	                writer.newLine();
	            }
	        }catch (IOException e) {
	            System.err.println("Error writing the CSV file: " + e.getMessage());
	        }
	        System.out.println("Appointment Rescheduled");
	        return;
	}
        
        
}
/**
show outcome of completed appointments of the user

*/
public void ShowOutcomes(String Name) {
	AppointmentAction appointmentaction=new AppointmentAction();
	List<Appointment> app=appointmentaction.getPatientbyName(Name);
	app.removeIf(appointment -> appointment.getDiagnosis().equals("NIL"));
	 int i2=0;
     for (int i = 0; i < app.size(); i++) {
     	i2++;
     	System.out.print(i2);
         System.out.print(" Patient: "+app.get(i).getPatient());
         System.out.print(" Doctor: "+app.get(i).getDoctor());
         System.out.print(" Date: "+app.get(i).getDate());
         System.out.print(" Time: "+app.get(i).getTime());
         System.out.print(" Status: "+app.get(i).getStatus());
         System.out.print(" Outcome: "+app.get(i).getDiagnosis());
         System.out.print(" Outcome: "+app.get(i).getTreatments());
         System.out.print(" Outcome: "+app.get(i).getPrescription());
         System.out.print(" Outcome: "+app.get(i).getMedicine_status());
         System.out.print(" Outcome: "+app.get(i).getQuantity());
         System.out.println(" Outcome: "+app.get(i).getNote());
     }
	
}
/**
present all medical record of the user
read from MedicalRecords.csv
*/
public void ViewMedicalRecord() {
	List<MedicalRecord> RecordList=MedicalRecordAction.readCSV(MedicalRecordPath, staffName);
    if(RecordList==null){
        System.out.println("No medical records yet, please see a doctor first");
        return;
    }
	System.out.println("ID:"+RecordList.get(0).getID());
	System.out.println("Name:"+RecordList.get(0).getPatient());
	System.out.println("Date of birth:"+RecordList.get(0).getDOB());
	System.out.println("Gender:"+RecordList.get(0).getGender());
	System.out.println("BloodType:"+RecordList.get(0).getBloodType());
	System.out.println("Email:"+RecordList.get(0).getEmail());
	System.out.println("Number:"+RecordList.get(0).getNumber());
	System.out.println("Appointment records: ");
	for(int i=0;i<RecordList.size();i++) {
		System.out.print("Date:"+RecordList.get(i).getAppointmentDate());
		System.out.print("Time:"+RecordList.get(i).getAppointmentTime());
		System.out.print("Doctor:"+RecordList.get(i).getDoctor());
		System.out.print("Diagonsis:"+RecordList.get(i).getDiagnosis());
		System.out.print("Treatment:"+RecordList.get(i).getTreatments());
		System.out.print("Medicine:"+RecordList.get(i).getPrescription());
		System.out.print("Amount:"+RecordList.get(i).getMedicine_status());
		System.out.println("Notes:"+RecordList.get(i).getNote());
	}

	
}
/**
Change the contact and number of user.
change will happen on MedicalRecords.csv and Patient_List.CSV
*/
public void ChangeContact() {
    Scanner scanner = new Scanner(System.in);
    int choice;
    String csvSeparator = ","; 
    String line;
	System.out.println("Press 1 to change email; 2 to change phone number:");
	choice=scanner.nextInt();
	if(choice==1) {
		System.out.println("Enter new email");
		scanner.nextLine();
	String newemail=scanner.nextLine();
	List<String> lines = new ArrayList<>();
	 try (BufferedReader br = new BufferedReader(new FileReader(MedicalRecordPath))) {
          
           while ((line = br.readLine()) != null) {
               String[] values = line.split(csvSeparator);             
               if (values.length > 0&&values[1].equals(staffName))   {
               	values[5]=newemail;
               	
               	
               }
               lines.add(String.join(",", values));
           }

       } catch (IOException e) {
           System.err.println("Error reading the CSV file: " + e.getMessage());
       }
       try (BufferedWriter writer = new BufferedWriter(new FileWriter(MedicalRecordPath))) {
           for (String updatedLine  : lines) {
               writer.write(updatedLine );
               writer.newLine();
           }
       }catch (IOException e) {
           System.err.println("Error writing the CSV file: " + e.getMessage());
       }
       lines.clear();
       try (BufferedReader br = new BufferedReader(new FileReader(PatientInfoPath))) {
           
           while ((line = br.readLine()) != null) {
               String[] values = line.split(csvSeparator);             
               if (values.length > 0&&values[1].equals(staffName))   {
               	values[5]=newemail;
               	
               	
               }
               lines.add(String.join(",", values));
           }

       } catch (IOException e) {
           System.err.println("Error reading the CSV file: " + e.getMessage());
       }
       try (BufferedWriter writer = new BufferedWriter(new FileWriter(PatientInfoPath))) {
           for (String updatedLine  : lines) {
               writer.write(updatedLine );
               writer.newLine();
           }
       }catch (IOException e) {
           System.err.println("Error writing the CSV file: " + e.getMessage());
       }
	
		System.out.println("Email updated");

	
	
	}else if(choice==2) {
		System.out.println("Enter new phone number");
		scanner.nextLine();
		String newnumber=scanner.nextLine();
		
	List<String> lines = new ArrayList<>();
	 try (BufferedReader br = new BufferedReader(new FileReader(MedicalRecordPath))) {
          
           while ((line = br.readLine()) != null) {
               String[] values = line.split(csvSeparator);             
               if (values.length > 0&&values[1].equals(staffName))   {
               	values[6]=newnumber;
               	
               	
               }
               lines.add(String.join(",", values));
           }

       } catch (IOException e) {
           System.err.println("Error reading the CSV file: " + e.getMessage());
       }
       try (BufferedWriter writer = new BufferedWriter(new FileWriter(MedicalRecordPath))) {
           for (String updatedLine  : lines) {
               writer.write(updatedLine );
               writer.newLine();
           }
       }catch (IOException e) {
           System.err.println("Error writing the CSV file: " + e.getMessage());
       }
       lines.clear();
       try (BufferedReader br = new BufferedReader(new FileReader(PatientInfoPath))) {
           
           while ((line = br.readLine()) != null) {
               String[] values = line.split(csvSeparator);             
               if (values.length > 0&&values[1].equals(staffName))   {
               	values[6]=newnumber;
               	
               	
               }
               lines.add(String.join(",", values));
           }

       } catch (IOException e) {
           System.err.println("Error reading the CSV file: " + e.getMessage());
       }
       try (BufferedWriter writer = new BufferedWriter(new FileWriter(PatientInfoPath))) {
           for (String updatedLine  : lines) {
               writer.write(updatedLine );
               writer.newLine();
           }
       }catch (IOException e) {
           System.err.println("Error writing the CSV file: " + e.getMessage());
       }
	
		System.out.println("Phone number updated");
		
		
		
		
	}
}
    /**
    display the menu of patient, patient will choose one of the option from this list to manage his medical record and appointments.
    */
    public void displayMenu() {
        //Administrator admin = new Administrator(hospitalID);

        boolean isLoggedIn = true;
        
        while(isLoggedIn) {
        Hospital hospital = new Hospital();
        staffName = hospital.getName(hospitalID, this);
        System.out.println("Welcome Patient, "+ staffName);
        System.out.println("---- Patient Menu ----");
        System.out.println("1. View Medical Record");
        System.out.println("2. Update Personal Information");
        System.out.println("3. View Available Appointment Slots");
        System.out.println("4. Schedule an Appointment");
        System.out.println("5. Reschedule an Appointment");
        System.out.println("6. Cancel an Appointment");
        System.out.println("7. View Scheduled Appointments");
        System.out.println("8. View Past Appointment Outcome Records");
        System.out.println("9. Logout");
        
        Scanner sc = new Scanner(System.in);
        int i=sc.nextInt();

        sc.nextLine();
        switch(i) {
        case 1:
        	{ViewMedicalRecord();
        	break;}
        case 2:
        	{ChangeContact();
        	break;}
        case 3:
        	{CheckAvaliableDates();
        	break;}
        case 4:
        	{BookAppointment(staffName);
        	break;}
        case 5:
        	{RescheduleAppointment(staffName);
        	break;}
        case 6:
        	{CancelAppointment(staffName);
        	break;}
        case 7:
        {this.PresentAppointment(staffName);
    	break;}
        case 8:
        	{this.ShowOutcomes(staffName);
        	break;}
        case 9:
        	{isLoggedIn = false;
        	return;}
        default:
        	break;
        }
        }
    }

}
