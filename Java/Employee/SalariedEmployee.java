package q2;

//This class defines a type of employee with a weekly salary
public class SalariedEmployee extends Employee {
    private double weeklySalary;

    // constructor
    public SalariedEmployee(String firstName, String lastName, BirthDate birthDate, String idNumber, double weeklySalary) {
        super(firstName, lastName, idNumber, birthDate);
        if (weeklySalary < 0.0) {
            throw new IllegalArgumentException("Weekly salary must be at least 0.0");
        }
        this.weeklySalary = weeklySalary;
    }

    // set salary
    public void setWeeklySalary(double weeklySalary) {
        if (weeklySalary < 0.0)
            throw new IllegalArgumentException("Weekly salary must be at least 0.0");
        this.weeklySalary = weeklySalary;
    }

    // return salary
    public double getWeeklySalary() {
        return weeklySalary;
    }

    // calculate earnings; override abstract method earnings in Employee
    @Override
    public double earnings() {
        return getWeeklySalary();
    }

    // return String representation of SalariedEmployee object
    @Override
    public String toString() {
        return String.format("salaried employee: %s%n%s: $%,.2f",
                super.toString(), "Weekly salary", getWeeklySalary());
    }

    @Override
    public void addToSalary(double amount) {
        this.setWeeklySalary(this.getWeeklySalary() + amount);
    }
}// end class SalariedEmployee
