
package q2;

//This class defines an employee who receives a commission

public class BasePlusCommissionEmployee extends CommissionEmployee {
    private double baseSalary; // base salary per week

    // constructor
    public BasePlusCommissionEmployee(String firstName, String lastName, BirthDate birthDate,
                                      String idNumber, double grossSales,
                                      double commissionRate, double baseSalary) {
        super(firstName, lastName, birthDate, idNumber,
                grossSales, commissionRate); //super
        if (baseSalary < 0.0) // check baseSalary
            throw new IllegalArgumentException("Base salary must be at least 0.0");
        this.baseSalary = baseSalary;
    }

    // set base salary
    public void setBaseSalary(double baseSalary) {
        if (baseSalary < 0.0) // check baseSalary
            throw new IllegalArgumentException("Base salary must be at least 0.0");
        this.baseSalary = baseSalary;
    }

    // return base salary
    public double getBaseSalary() {
        return baseSalary;
    }

    // calculate earnings; override method earnings in CommissionEmployee
    @Override
    public double earnings() {
        return getBaseSalary() + super.earnings();
    }

    // return String representation of BasePlusCommissionEmployee object
    @Override
    public String toString() {
        return String.format("%s %s; %s: $%,.2f", "base-salaried", super.toString(), "base salary", getBaseSalary());
    }

    @Override
    public void addToSalary(double amount) {
        this.setBaseSalary(this.getBaseSalary() + amount);
    }
}// end class BasePlusCommissionEmployee
