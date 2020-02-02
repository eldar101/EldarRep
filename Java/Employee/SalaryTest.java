package q2;
import java.util.Calendar;

//This is a test to print out employees and details

public class SalaryTest {
    public static void main(String[] args) {
        // employee objects with details
        BirthDate mosheBirthDate = new BirthDate(1, 1, 1989);
        SalariedEmployee salariedEmployee1 = new SalariedEmployee("Moshe", "Biton", mosheBirthDate, "123-11-4444", 820.00);

        BirthDate danaBirthDate = new BirthDate(12, 2, 1998);
        SalariedEmployee salariedEmployee2 = new SalariedEmployee("Dana", "Cohen", danaBirthDate, "555-23-5555", 100.00);

        BirthDate natashaBirthDate = new BirthDate(28, 3, 1981);
        HourlyEmployee hourlyEmployee = new HourlyEmployee("Natasha", "Flotzski", natashaBirthDate, "111-22-2222", 29.90, 40);

        BirthDate zviBirthDate = new BirthDate(2, 4, 1956);
        HourlyEmployee hourlyEmployee2 = new HourlyEmployee("Zvi", "Buchritz", zviBirthDate, "666-66-5676", 19.80, 30);

        BirthDate saritBirthDate = new BirthDate(15, 5, 1982);
        CommissionEmployee commissionEmployee = new CommissionEmployee("Sarit", "Weiss", saritBirthDate, "666-33-3333", 12000, .07);

        BirthDate tamiBirthDate = new BirthDate(7, 6, 1989);
        BasePlusCommissionEmployee basePlusCommissionEmployee = new BasePlusCommissionEmployee("Tami", "Zanzonet", tamiBirthDate, "999-44-4444", 6000, .05, 400);

        BirthDate shirBirthDate = new BirthDate(22, 12, 1988);
        BasePlusCommissionEmployee basePlusCommissionEmployee2 = new BasePlusCommissionEmployee("Shir", "Toledo", shirBirthDate, "777-77-1111", 7650, .07, 280);

        BirthDate efratBirthDate = new BirthDate(18, 12, 1970);
        PieceWorker pieceWorker1 = new PieceWorker("Efrat", "Bar", efratBirthDate, "123-88-8888", 19.84, 578);

        BirthDate tigranBirthDate = new BirthDate(5, 7, 1992);
        PieceWorker pieceWorker2 = new PieceWorker("Tigran", "Namer", tigranBirthDate, "111-99-1234", 22.45, 668);

        Employee[] employees = new Employee[9];
        employees[0] = salariedEmployee1;
        employees[1] = hourlyEmployee;
        employees[2] = commissionEmployee;
        employees[3] = basePlusCommissionEmployee;
        employees[4] = salariedEmployee2;
        employees[5] = hourlyEmployee2;
        employees[6] = basePlusCommissionEmployee2;
        employees[7] = pieceWorker1;
        employees[8] = pieceWorker2;

        System.out.printf("Employees:%n%n");

        int curMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;

        double bonus = 200;

        // generically process each element in array employees
        for (Employee currentEmployee : employees) {
            if (currentEmployee.getBirthDate().getMonth() == curMonth) {
                System.out.println("This month is " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName() + "has a birth on: "
                        + currentEmployee.getBirthDate().toString());
                System.out.println("The employee ie entitled to a 200 NIS bonus!");
                System.out.printf("Currently salary is $%,.2f%n", currentEmployee.earnings());
                currentEmployee.addToSalary(bonus);
                System.out.printf("Salary including bonus is  $%,.2f%n", currentEmployee.earnings());
            }
            System.out.println(currentEmployee); // invokes toString

            // determine whether element is a BasePlusCommissionEmployee
            if (currentEmployee instanceof BasePlusCommissionEmployee) {
                BasePlusCommissionEmployee employee = (BasePlusCommissionEmployee) currentEmployee;
                employee.setBaseSalary(1.10 * employee.getBaseSalary());
                System.out.printf("new base salary with 10%% increase is: $%,.2f%n", employee.getBaseSalary());
            }
            System.out.printf("earned $%,.2f%n%n", currentEmployee.earnings());
        }
    }
}// end class SalaryTest

