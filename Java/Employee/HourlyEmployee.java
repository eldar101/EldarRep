
package q2;

// This class defines an employee working by an hourly rate
public class HourlyEmployee extends Employee {

    private double wage; //  variables
    private double hours;

    // constructor
    public HourlyEmployee(String firstName, String lastName, BirthDate birthDate, String idNumber, double wage, double hours) {
        super(firstName, lastName, idNumber, birthDate);
        if (wage < 0.0) // check wage
            throw new IllegalArgumentException("Hourly wage must be at least 0.0");
        if ((hours < 0.0) || (hours > 168.0)) // check hours
            throw new IllegalArgumentException("Hours worked must be between 0.0 and 168.0");
        this.wage = wage;
        this.hours = hours;
    }

    // set wage
    public void setWage(double wage) {
        if (wage < 0.0)
            throw new IllegalArgumentException("Hourly wage must be at least 0.0");
        this.wage = wage;
    }

    // return wage
    public double getWage() {
        return wage;
    }

    // return hours worked
    public double getHours() {
        return hours;
    }

    // calculate earnings; override abstract method earnings in Employee
    @Override
    public double earnings() {
        if (getHours() <= 40) // checking if  overtime
            return getWage() * getHours();
        else
            return 40 * getWage() + (getHours() - 40) * getWage() * 1.5;
    }

    // return String representation of HourlyEmployee object
    @Override
    public String toString() {
        return String.format("Hourly employee: %s%n%s: $%,.2f; %s: %,.2f",
                super.toString(), "Hourly wage", getWage(), "hours worked", getHours());
    }

    @Override
    public void addToSalary(double amount) {
        this.setWage((this.earnings() + amount) / this.getHours());

    }
} // end class HourlyEmployee