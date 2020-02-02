
package q2;

//This class defines an employee:
public abstract class Employee {
    private final String firstName;
    private final String lastName;
    private final String idNumber;
    private final BirthDate birthDate;

    // constructor
    public Employee(String firstName, String lastName, String idNumber, BirthDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.idNumber = idNumber;
        this.birthDate = new BirthDate(birthDate);
    }

    //Get first name
    public String getFirstName() {
        return firstName;
    }

    // Get last name
    public String getLastName() {
        return lastName;
    }

    //Get ID number
    public String getIdNumber() {
        return idNumber;
    }

    //Get Birth Date
    public BirthDate getBirthDate() {
        return birthDate;
    }

    // return String representation of Employee object
    @Override
    public String toString() {
        return String.format("%s %s%nBirth date: %s%nID number: %s",
                getFirstName(), getLastName(), this.birthDate.toString(), getIdNumber());
    }

    // abstract method must be overridden by concrete subclasses
    public abstract double earnings();

    // abstract method must be overridden by concrete subclasses - used to add birth day bonus to employees.
    public abstract void addToSalary(double amount);
} // end abstract class Employee
