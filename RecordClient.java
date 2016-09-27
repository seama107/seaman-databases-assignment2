import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.io.InputStreamReader;
import java.io.BufferedReader;




public class RecordClient {
  private SqlConnectionStream sqlCon;


  public RecordClient() {
    sqlCon = new SqlConnectionStream();

  }

  public void runApplication() {

    if(!(sqlCon.hasDriver()))
    {
      return;
    }
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    System.out.println("Welcome to Chapman's student database user interface.");
    int input = 9;
    try{

      while(input != 0){
        switch(input){
          case 1: {
            sqlCon.displayAll();
            break;
          }
          case 2: {
            //Add student code
            String firstName, lastName, major, advisor = "default";
            double gpa = 0.0;
            while(true){
              System.out.print("Enter the new student's first name: ");
              firstName = br.readLine();
              if(firstName.length() > 25){
                System.out.println("Must be 25 characters or less.");
              }
              else{
                break;
              }
            }
            while(true){
              System.out.print("Enter the new student's last name: ");
              lastName = br.readLine();
              if(lastName.length() > 25){
                System.out.println("Must be 25 characters or less.");
              }
              else{
                break;
              }
            }

            while(true){
              System.out.print("Enter the new student's GPA: ");
              try{
                if( (gpa = Double.parseDouble(br.readLine())) >= 0.0 && input <= 4.0 ) {
                  break;
                }
                else {
                  System.out.println("Enter a decimal from 0.0 to 4.0.");
                }
              }
              catch(Exception e){
                System.out.println("Enter an decimal.");
              }
            }
            while(true){
              System.out.print("Enter the new student's major: ");
              major = br.readLine();
              if(major.length() > 10){
                System.out.println("Must be 10 characters or less.");
              }
              else{
                break;
              }
            }
            while(true){
              System.out.print("Enter the new student's faculty advisor: ");
              advisor = br.readLine();
              if(advisor.length() > 25){
                System.out.println("Must be 25 characters or less.");
              }
              else{
                break;
              }
            }
            sqlCon.addStudent(firstName, lastName, gpa, major, advisor);
            break;
          }
          case 3: {
            //update advisor code
            System.out.println("Enter the ID # of the student to be updated: ");
            boolean hasID = false;
            int id = -1;
            try{
              id = Integer.parseInt(br.readLine());
              hasID = true;
            }
            catch(Exception e){
              System.out.println("Enter a valid id.");
            }
            if(hasID){
              System.out.println("Enter the new Faculty Advisor's name: ");
              sqlCon.updateField("FacultyAdvisor", br.readLine(), id);
            }
            break;
          }
          case 4: {
            //update major code
            System.out.println("Enter the ID # of the student to be updated: ");
            boolean hasID = false;
            int id = -1;
            try{
              id = Integer.parseInt(br.readLine());
              hasID = true;
            }
            catch(Exception e){
              System.out.println("Enter a valid id.");
            }
            if(hasID){
              System.out.println("Enter the new major: ");
              sqlCon.updateField("Major", br.readLine(), id);
            }
            break;
          }
          case 5: {
            //delete student code
            System.out.println("Enter the ID # of the student to be deleted: ");
            boolean hasID = false;
            int id = -1;
            try{
              id = Integer.parseInt(br.readLine());
              hasID = true;
            }
            catch(Exception e){
              System.out.println("Enter a valid id.");
            }
            if(hasID){
              sqlCon.deleteStudent(id);
            }
            break;
          }
          case 6: {
            //major query code
            System.out.print("Enter the major you want to search for: ");
            String majorQuery = br.readLine();
            sqlCon.selectStudent("Major", majorQuery);
            break;
          }
          case 7: {
            //gpa query code
            System.out.print("Enter the GPA you want to search for: ");
            String gpaQuery = br.readLine();
            sqlCon.selectStudent("GPA", gpaQuery);
            break;
          }
          case 8: {
            //advisor query code
            System.out.print("Enter the advisor you want to search for: ");
            String advisorQuery = br.readLine();
            sqlCon.selectStudent("FacultyAdvisor", advisorQuery);
            break;
          }
          case 9: {
            displayHelpMenu();
            break;
          }
          default: {
            System.out.println("Command not recognized.");
            break;
          }
        }

        //input loop
        while(true){
          System.out.println("");
          System.out.print("(9 for help) Command: ");
          try{
            if( (input = Integer.parseInt(br.readLine())) >= 0 && input < 10 ) {
              break;
            }
            else {
              System.out.println("Enter an integer 0-9.");
            }
          }
          catch(Exception e){
            System.out.println("Enter an integer.");
          }
        }

      }
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  public void displayHelpMenu() {
    System.out.println("Menu - enter a number for each option:");
    System.out.println("(1) Display all students");
    System.out.println("(2) Add a student");
    System.out.println("(3) Update a student's advisor");
    System.out.println("(4) Update a student's major");
    System.out.println("(5) Delete a student");
    System.out.println("(6) Display students with specific major");
    System.out.println("(7) Display students with specific GPA");
    System.out.println("(8) Display students with specific advisor");
    System.out.println("(9) Display this help menu again");
    System.out.println("(0) Quit");
  }
}
