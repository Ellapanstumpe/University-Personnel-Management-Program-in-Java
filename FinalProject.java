/*
 Final Project
 Ella Stumpe
 
 */
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class FinalProject {

    private static ArrayList<Person> people = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Throwable {
        while (true) {
            // Print menu
            System.out.println("\t\t\t\t\t\t\tWelcome to my Personal Management Program");
            System.out.println("1- Enter the information a faculty");
            System.out.println("2- Enter the information of a student");
            System.out.println("3- Print tuition invoice for a student");
            System.out.println("4- Print faculty information");
            System.out.println("5- Enter the information of a staff member");
            System.out.println("6- Print the information of a staff member");
            System.out.println("7- Delete a person");
            System.out.println("8- Exit Program\n");

            System.out.println("Choose one of the options:");

            System.out.print("Enter your selection: ");

            try {
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        enterFacultyInfo();
                        break;
                    case 2:
                        enterStudentInfo();
                        break;
                    case 3:
                        printTuitionInvoice();
                        break;
                    case 4:
                        printFacultyInfo();
                        break;
                    case 5:
                        enterStaffInfo();
                        break;
                    case 6:
                        printStaffInfo();
                        break;
                    case 7:
                        deletePerson();
                        break;
                    case 8:
                        exitProgram();
                        break;
                    default:
                        System.out.println("Invalid selection. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid entry - please try again.");
                sc.nextLine(); // Clear the buffer
            }
        }
    }

    private static void enterFacultyInfo() {
        System.out.println("Enter faculty info:");
        System.out.print("Name of the faculty: ");
        String facultyName = sc.next();
        System.out.print("ID: ");
        String facultyId = sc.next();
        validateAndGenerateId(facultyId);
        System.out.print("Rank: ");
        String facultyRank = sc.next().toLowerCase();
        while (!facultyRank.equals("professor") && !facultyRank.equals("adjunct")) {
            System.out.println(" Input invalid, Please try again");
            System.out.print("Rank: ");
            facultyRank = sc.next().toLowerCase();
        }
        System.out.print("Department: ");
        String facultyDepartment = sc.next();
        validateDepartment(facultyDepartment);
        Faculty faculty = new Faculty(facultyName, facultyId, facultyDepartment, facultyRank);
        people.add(faculty);
        System.out.println("Faculty added!\n");
        sc.nextLine();
    }

    private static void enterStudentInfo() {
        System.out.println("Enter the student info:");
        System.out.println("Name of student: ");
        sc.nextLine();
        String fullName = sc.nextLine();
        System.out.println("ID: ");
        String id = sc.next().toLowerCase();
        validateAndGenerateId(id);
        System.out.print("Gpa: ");
        double gpa = sc.nextDouble();
        System.out.print("Credit hours: ");
        int creditHours = sc.nextInt();
        Student student = new Student(fullName, id, gpa, creditHours);
        System.out.println("Student added!");
        people.add(student);
    }

    private static void printTuitionInvoice() {
        System.out.print("Enter the student's ID: ");
        String tuitionId = sc.next().toLowerCase();
        boolean found = false;
        for (Person person : people) {
            if (person instanceof Student && person.id.equals(tuitionId)) {
                Student tuitionStudent = (Student) person;
                tuitionStudent.print();
                found = true;
                break;
            }
        }
      if (!found) {
            System.out.println("No student matched!");
        }
    }

    private static void printFacultyInfo() {
        System.out.print("Enter the Faculty's id: ");
        String facultyIdInput = sc.next();
        boolean found = false;
        for (Person person : people) {
            if (person instanceof Faculty && person.id.equals(facultyIdInput)) {
                ((Faculty) person).print();
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("No faculty matched!");
        }
    }

    private static void enterStaffInfo() {
        System.out.println("The name of the staff member: ");
        String staffName = sc.next();
        System.out.println("Enter the id: ");
        String staffId = sc.next();
        validateAndGenerateId(staffId);
        System.out.print("Enter the Department: ");
        String staffDepartment = sc.next();
        validateDepartment(staffDepartment);
        System.out.print("Status, Enter P for Part Time, or Enter F for Full Time: ");
        String staffStatus = validateStatus();
        Staff newStaff = new Staff(staffName, staffId, staffDepartment, staffStatus);
        people.add(newStaff);
    }

    private static void printStaffInfo() {
        System.out.print("Enter the Staff's id: ");
        String idToSearch = sc.next();
        boolean staffFound = false;
        for (Person person : people) {
            if (person instanceof Staff && person.id.equals(idToSearch)) {
                ((Staff) person).print();
                staffFound = true;
                break;
            }
        }
        if (!staffFound) {
            System.out.println("No Staff member matched!");
        }
    }

    private static void deletePerson() {
        System.out.print("Enter the id of the person to delete:");
        String idToDelete = sc.next();
        deletePersonById(idToDelete);
    }

    private static void exitProgram() throws Throwable {
        System.out.print("Would you like to create the report? (Y/N): ");
        String createReport = sc.next().toUpperCase();
        if (createReport.equals("Y")) {
            System.out.print("Would like to sort your students by descending gpa or name(1 for gpa, 2 for name): ");
            int reportOption = sc.nextInt();
            sc.nextLine();
            generateReport(people, reportOption);
        }
        System.out.println("Report created and saved on your hard drive!\nGoodbye!");
        System.out.println("_____________________________________________________________");
        System.exit(0);
    }

  
    	private static void generateReport(ArrayList<Person> people, int sortOption) throws Exception {
            Collections.sort(people, new Comparator<Person>() {
                @Override
                public int compare(Person p1, Person p2) {
                    if (p1 instanceof Student && p2 instanceof Student) {
                        Student s1 = (Student) p1;
                        Student s2 = (Student) p2;
                        if (sortOption == 1) {
                            int result = Double.compare(s2.getGpa(), s1.getGpa());
                            if (result == 0) {
                                return s1.fullName.compareToIgnoreCase(s2.fullName);
                            }
                            return result;
                        } else if (sortOption == 2) {
                            return s1.fullName.compareToIgnoreCase(s2.fullName);
                        }
                    } else {
                        return p1.fullName.compareToIgnoreCase(p2.fullName);
                    }
                    return 0;
                }
            });

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String date = dateFormat.format(new Date());

            FileWriter fileWriter = new FileWriter("report.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.println("Report created on " + date);

            printWriter.println("Faculty Members");
            int facultyCount = 0;
            for (Person person : people) {
                if (person instanceof Faculty) {
                    facultyCount++;
                    Faculty faculty = (Faculty) person;
                    printWriter.println(facultyCount + ". " + faculty.fullName + " ID: " + faculty.id +
                            " " + faculty.getDepartment() + " Department, " + faculty.getRank());
                }
            }

            printWriter.println("Staff Members");
            int staffCount = 0;
            for (Person person : people) {
                if (person instanceof Staff) {
                    staffCount++;
                    Staff staff = (Staff) person;
                    printWriter.println(staffCount + ". " + staff.fullName + " ID: " + staff.id +
                            " " + staff.getDepartment() + ", " + staff.getStatus() + " Time");
                }
            }

            printWriter.println("Students (Sorted by ");
            if (sortOption == 1) {
                printWriter.println("gpa in descending order)");
            } else if (sortOption == 2) {
                printWriter.println("name)");
            }

            int studentCount = 0;
            for (Person person : people) {
                if (person instanceof Student) {
                    studentCount++;
                    Student student = (Student) person;
                    printWriter.println(studentCount + ". " + student.fullName + " ID: " + student.id +
                            " Gpa: " + student.getGpa() + " Credit hours: " + student.getCreditHours());
                }
            }

            printWriter.close();
        
    }

    private static void validateAndGenerateId(String id) {
        HashSet<String> usedIds = new HashSet<>();
        do {
            if (!id.matches("[a-z]{2}\\d{4}") || usedIds.contains(id)) {
                System.out.println("Invalid ID format. Must be letterletterDigitDigitDigitDigit");
                System.out.println("ID: ");
             
            }
        } while (!id.matches("[a-z]{2}\\d{4}") || usedIds.contains(id));
        id = sc.nextLine().toLowerCase();
        usedIds.add(id);
    }

    private static void validateDepartment(String department) {
        Set<String> validDepartments = Set.of("Engineering", "Mathematics", "English");
        while (!validDepartments.contains(department)) {
            System.out.println("Sorry, the entered department (" + department + ") is invalid");
            System.out.print("Enter a valid department (engineering, mathematics, english): ");
            department = sc.nextLine();
        }
    }

    private static String validateStatus() {
        String status = sc.next().toUpperCase();
        if (status.equals("P") || status.equals("F")) {
            System.out.println("Staff member added");
        } else {
            System.out.println("Invalid input!");
        }
        return status;
    }

    private static void deletePersonById(String idToDelete) {
        Iterator<Person> iterator = people.iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            if (person.id.equals(idToDelete)) {
                iterator.remove(); // Safely remove the element during iteration
                System.out.println("Person with ID " + idToDelete + " deleted.");
                return; // Assuming there's only one person with the given ID
            }
        }
        System.out.println("Sorry no such person " + idToDelete + " exists.");
    }

    static abstract class Person {
        protected String fullName;
        protected String id;

        public Person(String fullName, String id) {
            this.fullName = fullName;
            this.id = id;
        }

        // Declare abstract method to be implemented by subclasses
        public abstract void print();
    }

    static abstract class Employee extends Person {
        // Declare common attributes for Faculty and Staff

        private String department;

        public Employee(String fullName, String id, String department) {
            super(fullName, id);
            this.department = department;
        }

        public String getDepartment() {
            return department;
        }

        // Implement the abstract method from the abstract class
        @Override
        public abstract void print();
    }

    static class Student extends Person implements Comparable<Student> {
        private double gpa;
        private int creditHours;

        // Student constructor with parameters
        public Student(String fullName, String id, double gpa, int creditHours) {
            super(fullName, id);
            this.gpa = gpa;
            this.creditHours = creditHours;
        }

        // Getters and setters methods

        public double getGpa() {
            return gpa;
        }

        public void setGpa(double gpa) {
            this.gpa = gpa;
        }

        public int getCreditHours() {
            return creditHours;
        }

        public void setCreditHours(int creditHours) {
            this.creditHours = creditHours;
        }

        // Override the print method from abstract Person to print the tuition invoice
        public void print() {
            double total = (creditHours * 236.45) + 52;
            double discount = 0.0;

            if (gpa >= 3.85) {
                discount = total * 0.25;
                total -= discount;
            }

            System.out.println("--------------------------------------------------------------------------------------------------");
            System.out.println(fullName);
            System.out.println("Credit Hours: " + creditHours + " ($236.45/credit hour)");
            System.out.println("Fees: $52\n\n");
            System.out.println("Total Payment: $" + total + "\t\t\t\t\t\t($" + discount + " discount applied)");
            System.out.println("--------------------------------------------------------------------------------------------------\n\n");
        }

		@Override
		public int compareTo(FinalProject.Student o) {
			// TODO Auto-generated method stub
			return 0;
		}   
    }

    static class Staff extends Employee {
        private String status;

        // Constructor with parameters for Staff
        public Staff(String fullName, String id, String department, String status) {
            super(fullName, id, department);
            this.status = status;
        }

        // Getter and setter for status with validation
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        // Implement the abstract method from the abstract class
        @Override
        public void print() {
        	String staffStatus = getStatus();
            // Implement print method for Staff
            System.out.println("------------------------------------------");
            System.out.println(super.fullName + "\t" + super.id+"\n");
            System.out.print(super.department);
            if(staffStatus.equals("F")) {
            	System.out.println("\t\tFull Time\n");
            	} else if(staffStatus.equals("P")) 
            	{
            		System.out.println("\t\tPart Time\n");
            		sc.next();
            		}
            
        System.out.println("------------------------------------------");
        }
    }

    static class Faculty extends Employee {

        private String rank;

        public Faculty(String fullName, String id, String department, String rank) {
            super(fullName, id, department);
            this.rank = rank;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        @Override
        // Method that prints the faculty information
        public void print() {
            System.out.println("----------------------------------------------------------------------");
            System.out.println(fullName+ "\t\t"+ id);
            System.out.println(super.department + " Department, " + rank);
            System.out.println("----------------------------------------------------------------------");
        }
    }
}
