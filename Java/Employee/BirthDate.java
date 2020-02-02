
package q2;
//this class defines a birth date


public class BirthDate {
    private int year;
    private int month;
    private int day;

    //constructor
    public BirthDate(BirthDate birthDate) {
        this.day = birthDate.getDay();
        this.month = birthDate.getMonth();
        this.year = birthDate.getYear();
    }

    //constructor
    public BirthDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    //get year
    public int getYear() {
        return this.year;
    }

    //get month
    public int getMonth() {
        return this.month;
    }

    //get day
    public int getDay() {
        return this.day;
    }


    @Override
    public String toString() {
        return (day + "/" + month + "/" + year);
    }
}
