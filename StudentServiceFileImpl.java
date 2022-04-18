package com.hcl.myproject.crud.entity;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

//import java.util.Comparator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

public class StudentServiceFileImpl {

	public static void main(String[] args) throws IOException, ParseException {
		
		header();
		Connection dbConnection = getConnection();
		// Sort during insertion
		/*
		 * Set<Student1> studentSet = new TreeSet<>(new Comparator<Student1>() {
		 * 
		 * @Override public int compare(Student1 o1, Student1 o2) { // TODO
		 * Auto-generated method stub return o1.getName().compareTo(o2.getName()); } });
		 */

		/*
		 * Set<Student1> studentSet = new TreeSet<>((o1, o2) -> { return
		 * o1.getName().compareTo(o2.getName()); });
		 */
		// Type inference
		Set<Student1> studentSet = new TreeSet<>((o1, o2) -> o1.getName().compareTo(o2.getName()));
		// Set<Student1> studentSet = new TreeSet<>();

		// Extracts the information from the text file and inputs it into the set
		File file = extractFileInformation(studentSet);

		Scanner myObj = new Scanner(System.in);
		String actionKey = "";
		double Average = 0;

		showCommands();

		while (!(actionKey.equals("q") || actionKey.equals("quit"))) {
			System.out.print("\nEnter the next command: ");

			actionKey = myObj.next();

			switch (actionKey) {
			case "insert":
			case "i":
				insertStudent(studentSet, myObj, dbConnection);
				break;
			case "delete":
			case "d":
				deleteStudent(studentSet, myObj, dbConnection);
				break;
			case "update":
			case "u":
				updateStudent(studentSet, myObj, dbConnection);
				break;
			case "find":
			case "search":
				searchStudent(studentSet, myObj, dbConnection);
				break;
			case "display":
				printAllStudents(studentSet);
				break;
			case "q":
			case "quit":
				break;
			case "a":
			case "average":
				Average = AverageAge(studentSet);
				System.out.println("the average age of a student is " + Average);
				break;
			default:
				System.out.println("Invalid Command, try again!");
				break;
			}
		}

		// puts set into text file for next use
		inputDataBack(studentSet, file);

		footer();
	}

	 public static Connection getConnection() {
	        // Registering drivers
	    	//DriverManager.registerDriver();
		 

	        try {Connection dbconnection= DriverManager.getConnection("jdbc:mysql://localhost:3306/Studentdb","root","Aleesha@01");{
	        	Statement st= dbconnection.createStatement();
	        st.execute("create table student(id numeric(10), name varchar(100),age varchar(100),contact numeric(10),primary key(id))");
	        } }catch (SQLException e) {
	        	// TODO Auto-generated catch block
	        	e.printStackTrace();
	        }
			return null;
			
	 }
	
	private static void inputDataBack(Set<Student1> studentSet, File file) throws IOException {
		try (FileWriter fw = new FileWriter(file);) {

			for (Student1 curr : studentSet) {
				String insertString = curr.getID() + "," + curr.getName() + "," + curr.getAge() + ","
						+ curr.getContact() + "\n";
				fw.write(insertString);
			}

		}
	}

	private static File extractFileInformation(Set<Student1> studentSet) throws ParseException {
		File file = new File("StoredInfo.txt");
		File myObj = new File("StoredInfo.txt");
		try (Scanner myReader = new Scanner(myObj);) {

			while (myReader.hasNextLine()) {
				String data[] = myReader.nextLine().split(",");
				int id = Integer.parseInt(data[0]);
				String name = data[1];
				int age = Integer.parseInt(data[2]);
				int contact = Integer.parseInt(data[3]);
				studentSet.add(new Student1(id, name, age, contact));
			}
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return file;
	}

	// Searchs for studdent in given treeset
	/*private static void searchStudent(Set<Student1> studentSet, Scanner myObj) {
		// TODO Auto-generated method stub
		System.out.print("What is the student ID: ");
		int id = myObj.nextInt();

		/*for (Student1 curr : studentSet) {
			if (curr.getID() == id) {
				System.out.println("_______________________________________________________________\n");
				System.out.println("Student information: \nName: " + curr.getName() + "\nAge: " + curr.getAge()
						+ "\nID :" + curr.getID() + "\ncontact Added: " + curr.getcontact());
				System.out.println("_______________________________________________________________\n");
				return;
			}
		}*/
		
		/*studentSet.stream().forEach(curr -> {
			if (curr.getID() == id) {
				System.out.println("_______________________________________________________________\n");
				System.out.println("Student information: \nName: " + curr.getName() + "\nAge: " + curr.getAge()
						+ "\nID :" + curr.getID() + "\ncontact Added: " + curr.getContact());
				System.out.println("_______________________________________________________________\n");
				return;
			}
		});
		idNotInStore(id);

	}*/
private static void searchStudent(Set<Student1> studentSet, Scanner myObj, Connection sqlConnection) {
		// TODO Auto-generated method stub
    	System.out.print( "What is the student ID: " );
    	int id = myObj.nextInt();
    	/*final AtomicBoolean found = new AtomicBoolean(false);
		studentSet.stream().forEach(curr -> {
			if (curr.getID() == id) {
				System.out.println("_______________________________________________________________\n");
				System.out.println("Student information: \nName: " + curr.getName() + "\nAge: " + curr.getAge()
						+ "\nID: " + curr.getID() + "\nDate Added: " + curr.getDate());
				System.out.println("_______________________________________________________________\n");
				found.set(true);
			}
			
		});*/
		
		boolean found = false;
		try {
			String selectAllSql = "select * from student where id =" + id;
			
			Statement st = sqlConnection.createStatement();
			ResultSet rs = st.executeQuery(selectAllSql);
			
			if(rs.next())
			{
				found = true;
				 String foundID= rs.getString(1);
		    	 String name = rs.getString(2);
		    	 String age = rs.getString(3);
		    	 int contact = rs.getInt(4);
				System.out.println("_______________________________________________________________\n");
				System.out.println("Student information: \nName: " + name + "\nAge: " + age
						+ "\nID: " + foundID + "\ncontact: " + contact);
				System.out.println("_______________________________________________________________\n");
			}
			
		} catch (Exception e) {
			System.out.println("Unexpected issue while searching in the database");
		}
		
		if(!found) {
			idNotInStore(id);
		}
    }
	
	// Message anytime an operation is tried with a ID that does not exist
	private static void idNotInStore(int id) {
		System.out.println("____________________________________________\n");
		System.out.println("ID: " + id + " does not exist");
		System.out.println("____________________________________________\n");
	}

	// Update student data by ID
	/*private static void updateStudent(Set<Student1> studentSet, Scanner myObj) {
		// TODO Auto-generated method stub
		try {
			System.out.print("Would you like to update age or name: ");
			String updateType = myObj.next();

			while (!(updateType.equals("age") || updateType.equals("name"))) {
				System.out.print("Invalid update, choose 'age' or 'name'!: ");
				updateType = myObj.next();
			}

			System.out.print("What is the student ID: ");
			int id = myObj.nextInt();
			final String updatetype1 =updateType;
			studentSet.stream().forEach(curr -> {
				if (curr.getID() == id) {
					if (updatetype1.equals("age")) {
						System.out.print("What is the new age: ");
						curr.setAge(myObj.nextInt());
					} else if (updatetype1.equals("name")) {
						System.out.print("What is the new name: ");
						curr.setName(myObj.next());
					}

					success("update", curr.getName());

					return;
				}
		
			
			idNotInStore(id);
		});
			
			
		}catch (Exception e) {
			System.out.println("Invalid number!");
		}
	}*/
	private static void updateStudent(Set<Student1> studentSet, Scanner myObj, Connection sqlConnection) {
		// TODO Auto-generated method stub
    	try {
	    	System.out.print( "Would you like to update age or name: " );
	    	String updateType = myObj.next();
	    	
	    	while(!(updateType.equals("age") ||updateType.equals("name")))
	    	{
	    		System.out.print( "Invalid update, choose 'age' or 'name'!: " );
	    		updateType = myObj.next();
	    	}
	    	
	    	System.out.print( "What is the student ID: " );
	    	int id = myObj.nextInt();
	    	
	    	/*for(Student curr: studentSet) {
	    		if(curr.getID() == id) {
	    			if(updateType.equals("age")) {
	    				System.out.print( "What is the new age: " );
	    				curr.setAge( myObj.nextInt());
	    			}
	    			else if(updateType.equals("name")) {
	    				System.out.print( "What is the new name: " );
	    				curr.setName( myObj.next());
	    			}
	    			
	    			success("updated", curr.getName());
	    			
	    			return;
	    		}
	    	}*/
    		String updateSql = "select name from student where id = "+ id;
    		Statement st = sqlConnection.createStatement();
    		ResultSet rs = st.executeQuery(updateSql);
    		String name = "";
    		if(rs.next())
    		{
    			name = rs.getString(1);
    		}
    		
			if(updateType.equals("age")) {
				System.out.print( "What is the new age: " );
				String newAge = myObj.next();
				updateSql = "UPDATE student SET age = " + newAge + " WHERE id = " + id;
			}
			else if(updateType.equals("name")) {
				System.out.print( "What is the new name: " );
				String newName = myObj.next();
				updateSql = "UPDATE student SET name = '" + newName + "' WHERE id = " + id;
			}
    		
    		int updated = st.executeUpdate(updateSql);
    		
    		if(updated == 1)
    		{
    			success("Updated", name);
    		}
	    	
	    	
	   		//idNotInStore(id);
    	} catch (Exception e) {
    		System.out.println("Unexpected issue with inputs!");
    	}
	}

	// Deletes student data by ID
	private static void deleteStudent(Set<Student1> studentSet, Scanner myObj, Connection sqlConnection) {
		// TODO Auto-generated method stub
		try {
			System.out.println("Input student's ID to delete:");
			int id = myObj.nextInt();
			
/*
			studentSet.stream().forEach(curr -> {

				if (curr.getID() == id) {
					studentSet.remove(curr);
					success("removed", curr.getName());

					return;
				}
				
			});
		
		} */
			String delSql = "select name from student where id = "+ id;
    		Statement st = sqlConnection.createStatement();
    		ResultSet rs = st.executeQuery(delSql);
    		String name = "";
    		if(rs.next())
    		{
    			name = rs.getString(1);
    		}
    		
    		delSql = "delete from student where  id = " + id;
    		int deleted = st.executeUpdate(delSql);
    		
    		if(deleted == 1)
    		{
    			success("removed", name);
    		}
		}
		catch (Exception e) {
			
			System.out.println("Invalid ID!");
		}
	}
	
	//Print average of age
		
		private static double AverageAge(Set <Student1> s) {
	        return s.stream()
	                    .mapToDouble(d -> d.getAge())
	                    .average()
	                    .orElse(0.0);
	    }
	

				
	
				
	


	// Prints all students currently stored
	private static void printAllStudents(Set<Student1> studentSet) {
		// TODO Auto-generated method stub
		final AtomicInteger counter = new AtomicInteger();
		studentSet.stream().forEach(curr -> {
			 {
				System.out.println("");
				
				System.out.println("Student information: \nName: " + curr.getName() + "\nAge: " + curr.getAge()
						+ "\nID :" + curr.getID() + "\ncontact Added: " + curr.getContact());
				counter.getAndIncrement();
				
				return;
			}
			 
		});
		
		
	}
	
	

	// Inserts new student into the treeSet
	/*private static void insertStudent(Set<Student1> studentSet, Scanner myObj) {
		try {
			System.out.print("Input new student's name: ");
			String name = myObj.next();

			System.out.print("Input " + name + "'s id: ");
			int id = myObj.nextInt();

			System.out.print("Input " + name + "'s age: ");
			int age = myObj.nextInt();

			System.out.print("Input " + name + "'s contact: ");
			int contact = myObj.nextInt();
			studentSet.add(new Student1(id, name, age, contact));

			success("added", name);

		} catch (Exception e) {
			System.out.println("Invalid inputs!");
			return;
		}
	}
*/
	private static void insertStudent(Set<Student1> studentSet, Scanner myObj, Connection sqlConnection) {
    	try {
    		System.out.print( "Input new student's name: " );
        	String name = myObj.next();
        	
    		System.out.print( "Input "+ name+"'s id: " );
        	int id = myObj.nextInt();
        	
    		System.out.print( "Input "+ name +"'s age: " );
    		int age = myObj.nextInt();
    		
    		System.out.print("Input " + name + "'s contact: ");
			int contact = myObj.nextInt();
			studentSet.add(new Student1(id, name, age, contact));

    		success("added", name);

    		
    		//SQL PART
    		String insertSql = "insert into student values(" + id + ",'" + name + "'," + age + ", " + contact + "now() )";
    		
    		Statement st = sqlConnection.createStatement();
    		st.executeUpdate(insertSql);
    		
    		
    	} catch (Exception e) {
    		System.out.println(e);
    	}
    	

    	
    }
	// Success Message
	private static void success(String type, String name) {
		System.out.println("\n____________________________________________\n");
		System.out.println("Successfully " + type + ": " + name);
		System.out.println("____________________________________________\n");
	}

	// footer exit message
	private static void footer() {
		System.out.println("______________________________________________");
		System.out.println("|                                             |");
		System.out.println("| Thank you for using the Student Set Manager!|");
		System.out.println("|_____________________________________________|\n");
	}

	// shows commands
	private static void showCommands() {
		System.out.println("Available Commands:\n");
		System.out.println("'insert' or 'i'\nDescription: inserts new student\n");

		System.out.println("'delete' or 'd'\nDescription: deletes student on ID\n");

		System.out.println("'update' or 'u'\nDescription: upcontacts existing student on ID\n");

		System.out.println("'search' or 'find'\nDescription: finds existing student on ID\n");

		System.out.println("'display' \nDescription: Shows all existing students\n");

		System.out.println("'q' or 'quit' \nDescription: Saves and exits the application\n");
		
		System.out.println("'a' or 'average' \nDescription: average age\n");

	}

	// header for first opening application
	private static void header() {
		System.out.println("_____________________________________");
		System.out.println("|                                    |");
		System.out.println("| Welcome to the Student Set Manager!|");
		System.out.println("|____________________________________|\n");
	}
}