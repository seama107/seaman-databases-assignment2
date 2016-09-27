import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class SqlConnectionStream {
  private String driverLocation;
  private String connectionURL;
  private String mySqlUsername;
  private String mySqlPassword;
  private boolean hasDriver;


  public SqlConnectionStream(String driverLoc) {
    connectionURL = "jdbc:mysql://us-cdbr-azure-west-b.cleardb.com:3306/acsm_676dbbe84ea3d8c";
    mySqlUsername = "be234364e375d8";
    mySqlPassword = "e60754d2";
    driverLocation = driverLoc;
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

  public SqlConnectionStream() {
    this("com.mysql.jdbc.Driver");
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
