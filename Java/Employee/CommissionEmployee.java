
package q2;
//this class defines a commission for employee
public class CommissionEmployee extends Employee{
    private double grossSales; // gross weekly sales
    private double commissionRate; // commission percentage
    // constructor
    public CommissionEmployee(String firstName, String lastName, BirthDate birthDate,
    String idNumber, double grossSales, double commissionRate){
        super(firstName, lastName, idNumber, birthDate);
        if (commissionRate <= 0.0 || commissionRate >= 1.0) // check
        throw new IllegalArgumentException("Commission rate must be between 0.0 and 1.0");
        if (grossSales < 0.0) // check
        throw new IllegalArgumentException("Gross sales must be at least 0.0");
        this.grossSales = grossSales; //set
        this.commissionRate = commissionRate;
    }
    // set gross sales amount
    public void setGrossSales(double grossSales)    {
        if (grossSales < 0.0) // check
        throw new IllegalArgumentException("Gross sales must be at least 0.0");
        this.grossSales = grossSales;
    }
    // return gross sales amount
    public double getGrossSales(){
        return grossSales;
    }

    // set commission rate
    public void setCommissionRate(double commissionRate){
        if (commissionRate <= 0.0 || commissionRate >= 1.0) // check
        throw new IllegalArgumentException(
        "Commission rate must be between 0.0 and 1.0");
        this.commissionRate = commissionRate;
    }
    // return commission rate
    public double getCommissionRate(){
        return commissionRate;
    }
    // calculate earnings; override abstract method earnings in Employee
    @Override
    public double earnings(){
        return getCommissionRate() * getGrossSales();
    }
    // return String representation of CommissionEmployee object
    @Override
    public String toString(){
        return String.format("%s: %s%n%s: $%,.2f; %s: %.2f",
        "Commission employee", super.toString(),
        "Gross sales", getGrossSales(),
        "Commission rate", getCommissionRate());
    }
    
    @Override
    public void addToSalary(double amount){
        this.setGrossSales(this.getGrossSales() + amount/this.getCommissionRate());
    }
}//End of class employee

