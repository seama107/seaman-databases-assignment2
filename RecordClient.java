import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.io.InputStreamReader;
import java.io.BufferedReader;




public class RecordClient {
  private String driverLocation;
  private String connectionURL;
  private String mySqlUsername;
  private String mySqlPassword;
  private boolean hasDriver;


  public RecordClient() {
    connectionURL = "jdbc:mysql://us-cdbr-azure-west-b.cleardb.com:3306/acsm_676dbbe84ea3d8c";
    mySqlUsername = "be234364e375d8";
    mySqlPassword = "e60754d2";
    driverLocation = "com.mysql.jdbc.Driver";
    hasDriver = false;
    try{
      Class.forName(driverLocation);
      hasDriver = true;
    }
    catch(Exception e){
      System.out.println("Driver could not be located.");
      System.out.println("Make sure the CLASSPATH environment variable holds the driver jar.");
      System.out.println("Example: 'export CLASSPATH=/Users/michaelseaman/Downloads/testConnector/mysql-connector-java-5.1.39:$CLASSPATH'");
      //e.printStackTrace();
    }

  }

  public void runApplication() {

    if(!(hasDriver()))
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
            displayAll();
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
            addStudent(firstName, lastName, gpa, major, advisor);
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
              updateField("FacultyAdvisor", br.readLine(), id);
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
              updateField("Major", br.readLine(), id);
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
              deleteStudent(id);
            }
            break;
          }
          case 6: {
            //major query code
            System.out.print("Enter the major you want to search for: ");
            String majorQuery = br.readLine();
            selectStudent("Major", majorQuery);
            break;
          }
          case 7: {
            //gpa query code
            System.out.print("Enter the GPA you want to search for: ");
            String gpaQuery = br.readLine();
            selectStudent("GPA", gpaQuery);
            break;
          }
          case 8: {
            //advisor query code
            System.out.print("Enter the advisor you want to search for: ");
            String advisorQuery = br.readLine();
            selectStudent("FacultyAdvisor", advisorQuery);
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

  public void closeConnections(Connection mySql, AutoCloseable auxilary) {
    //To be called after all Sql querys and updates.
    //Closes the primary connection and whatever secondary
    //'auxilary' quietly
    try{
      mySql.close();
      auxilary.close();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    finally {
      try {
        mySql.close();
      }
      catch(Exception e) {}

      try {
        auxilary.close();
      }
      catch(Exception e) {}
    }
  }

  public void displayAll() {
    Connection mySql = null;
    PreparedStatement pst = null;
    ResultSet queryReturn = null;

    try{
      mySql = DriverManager.getConnection(connectionURL, mySqlUsername, mySqlPassword);
      pst = mySql.prepareStatement("SELECT * FROM StudentDB");
      queryReturn = pst.executeQuery();

      prettyPrintStudents(queryReturn);

    }
    catch(Exception e){
      e.printStackTrace();
    }
    closeConnections(mySql, queryReturn);

  }

  public void prettyPrintStudents(ResultSet rs) throws Exception {
    //Prints all lines of ResultSet
    //fancy formattiong
    System.out.println(String.format("%90s", " ").replaceAll(" ", "═" ));
    System.out.println(String.format("%-5s%-20s%-20s%-5s%-10s%-20s", "ID", "FirstName", "LastName", "GPA", "Major","FacultyAdvisor"));
    System.out.println(String.format("%90s", " ").replaceAll(" ", "═" ));
    String[] rowData = new String[6];
    while(rs.next()) {
      rowData[0] = rs.getString("studentId");
      rowData[1] = rs.getString("FirstName");
      rowData[2] = rs.getString("LastName");
      rowData[3] = rs.getString("GPA");
      rowData[4] = rs.getString("Major");
      rowData[5] = rs.getString("FacultyAdvisor");
      System.out.println(String.format("%-5s%-20s%-20s%-5s%-10s%-20s", rowData[0], rowData[1], rowData[2], rowData[3], rowData[4], rowData[5]));
    }
    System.out.println(String.format("%90s", " ").replaceAll(" ", "═" ));
  }

  public void addStudent(String firstName, String lastName, double gpa, String major, String facultyAdvisor){
    Connection mySql = null;
    Statement statement = null;

    try{
      mySql = DriverManager.getConnection(connectionURL, mySqlUsername, mySqlPassword);
      statement = mySql.createStatement();
      String insertCode = String.format("INSERT INTO StudentDB (FirstName, LastName, GPA, Major, FacultyAdvisor) VALUES('%s', '%s', %f, '%s', '%s');", firstName, lastName, gpa, major, facultyAdvisor);
      int affectedRows = statement.executeUpdate(insertCode);

      if(affectedRows == 0) {
        System.out.println("Could not add student.");
      }
      else {
        System.out.println("Added student.");
      }


    }
    catch(Exception e){
      e.printStackTrace();
    }
    closeConnections(mySql, statement);
  }

  public void updateField(String field, Object value, int id){
    //Only valid for VARCHAR fields in the StudentDB database
    //Should only be "Major" or "FacultyAdvisor"
    Connection mySql = null;
    Statement statement = null;

    try{
      mySql = DriverManager.getConnection(connectionURL, mySqlUsername, mySqlPassword);
      statement = mySql.createStatement();

      String updateCode = String.format("UPDATE StudentDB SET %s='" + value + "' WHERE StudentId=%d;", field, id);
      int affectedRows = statement.executeUpdate(updateCode);

      if(affectedRows == 0) {
        System.out.println(String.format("Could not update %s.", field));
      }
      else {
        System.out.println(String.format("Updated %s.", field));
      }
    }
    catch(Exception e){
      e.printStackTrace();
    }
    closeConnections(mySql, statement);
  }

  public void deleteStudent(int id) {

    Connection mySql = null;
    Statement statement = null;

    try{
      mySql = DriverManager.getConnection(connectionURL, mySqlUsername, mySqlPassword);
      statement = mySql.createStatement();

      String updateCode = String.format("DELETE FROM StudentDB WHERE StudentId=%d;", id);
      int affectedRows = statement.executeUpdate(updateCode);

      if(affectedRows == 0) {
        System.out.println(String.format("Could not delete student # %d.", id));
      }
      else {
        System.out.println("Deleted student.");
      }
    }

    catch(Exception e){
      e.printStackTrace();
    }
    closeConnections(mySql, statement);
  }

  public void selectStudent(String field, Object value){
    Connection mySql = null;
    PreparedStatement pst = null;
    ResultSet queryReturn = null;
    try{
      mySql = DriverManager.getConnection(connectionURL, mySqlUsername, mySqlPassword);
      pst = mySql.prepareStatement(String.format("SELECT * FROM StudentDB WHERE %s='" + value + "';", field));
      queryReturn = pst.executeQuery();
      System.out.println(String.format("Students where %s is " + value, field));
      prettyPrintStudents(queryReturn);
    }
    catch(Exception e){
      e.printStackTrace();
    }
    closeConnections(mySql, queryReturn);
  }


  public boolean hasDriver(){
    return hasDriver;
  }

}
