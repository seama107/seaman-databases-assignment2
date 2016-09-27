# Databases Assignment 2

**Author:** Michael Seaman

**Due date:** 2016/09/27

## Usage


To run:

```
export CLASSPATH=/PATH_TO_JDBC_DRIVER/mysql-connector-java-5.1.39:$CLASSPATH

javac *.java
java SchoolDatabaseApplication

```


Using this java class is contingent on having the jdbc driver for interfacing with the
sql server. Before running the application, it is necessary to create a `CLASSPATH`
environment variable that stores the path to the driver. If not found, the application
will not start.



## Specification

Overview:
For this assignment you need to create an application that connects to a MySQL Database
and performs basic database transactions.
Develop the following Application/Database.
1. Create a Database named StudentDB with the following schema
  a. Relation/Table
  i. Student (
  StudentId PK INT,
  FirstName varchar(25),
  LastName varchar(25),
  GPA Numeric,
  Major varchar(10),
  FacultyAdvisor varchar(25)
  )
2. The application should be able to:
  a. Display All Students and all their attributes.
  b. Add/Update Students
  i. All attributes are required when creating new student.
  ii. Only the Major and Advisor attributes can be updated.
  c. Delete Students
  d. Search/Display students by Major, GPA and Advisor.
Grading:
Your program will be evaluated for correctness and elegance. Make sure your code
includes methods and good naming conventions. Use all the good practice methods you
have learned throughout your academic career.


## Honor Pledge

I pledge that all the work in this repository is my own!


Signed,

Michael Seaman
