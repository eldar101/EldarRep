import com.sun.xml.internal.bind.v2.model.core.MaybeElement;

/**
 *This class represents a Date object
 *@author Eldar Weiss
 *
 */
public class Date
{
    private int _day; //default init elements of a date
    private int _month;
    private int _year;
    private int maxDayInMonth;

    private final int TWO = 2; //constants to use like months and multipliers
    private final int FOUR = 4;
    private final int SEVEN = 7;
    private final int TWENTYSIX = 26;
    private final int TEN = 10;
    private final int FOURHUNDRED = 400;
    private final int HUNDRED = 100;
    private final int TWOTHOUSAND = 2000;
    private final int DAYS_IN_YEAR = 365;
    private final int JANUARY = 1;
    private final int FEBRUARY = 2;
    private final int MARCH = 3;
    private final int MAY = 5;
    private final int JULY = 7;
    private final int AUGUST = 8;
    private final int OCTOBER = 10;
    private final int DECEMBER = 12;

    //constructors
    /**
     * copy constructor
     * @param other the day value to be set - Date
     */

    public Date(Date other)
    {
        if (checkDate(other.getDay(), other.getMonth(), other.getYear())) //use date if valid
        {
            _day = other._day;
            _month = other._month;
            _year = other._year;
        }
    }

 /*creates a new Date object if the date is valid, otherwise creates the date 1/1/2000
 * @param day - the day in the month(1-31)
 * @param month - the month in the year(1-12)
 * @param year - the year ( 4 digits)
 */

    public Date(int day, int month, int year)
    {
        if (this.checkDate(day, month, year) == true) //default date if received date is invalid
        {
            _day = day;//date inserted
            _month = month;
            _year = year;
        }
        else
        {
            _day = 1;
            _month = 1;
            _year = TWOTHOUSAND;
        }

    }

    /**
     * check if this date is after other date
     @return true if this date is after other date
     */

    public boolean after(Date other)
    {
        return other.before(this);
    }

    /**
     * check if this date is before other date
     @return true if this date is before other date
     */

    public boolean before(Date other)
    {
        return ((calculateDate(_day, _month, _year)) < (calculateDate(other.getDay(), other.getMonth(), other.getYear()))); //decides which date is before by having less days since beginning of time
    }

    /**
     * calculate the day of the week that this date occurs on 0-Saturday 1-Sunday 2-Monday etc.
     @return the day of the week that this date occurs on
     */

    public int dayInWeek()
    {
        int tempMonth = _month;
        int last2DigitsOfYear = _year % 100; //last two digits of a year
        int first2DigitsOfYear = _year/100; //first two digits of a year
        if (tempMonth<MARCH)
        {
            tempMonth +=12; //january and feb are considered 13 and 14
            last2DigitsOfYear--;
        }
        return (_day + (TWENTYSIX * (tempMonth + 1)) / TEN + last2DigitsOfYear + last2DigitsOfYear / FOUR + first2DigitsOfYear / FOUR - TWO * first2DigitsOfYear) % SEVEN; //formula to calculate day of the week
    }

    /**
     * calculates the difference in days between two dates
     * @param other the date to calculate the difference between.
     * @return the number of days between the dates
     */

    public int difference(Date other)
    {
        if (before(other))
            return ((calculateDate(other.getDay(), other.getMonth(), other.getYear())) - (calculateDate(_day, _month, _year))); //calculates difference so it's a positive number
        return ((calculateDate(_day, _month, _year)) - (calculateDate(other.getDay(), other.getMonth(), other.getYear())));
    }

    /**
     * check if 2 dates are the same
     @param other - the date to compare this date to
     @return true if the dates are the same
     */

    public boolean equals(Date other)
    {
        int a=calculateDate( other._day, other._month, other._year);
        int b=calculateDate( _day, _month, _year );
        if (a==b)
            return true;
        else
            return false;
    }

    /**
     * gets the Day
     * @return the day
     */

    public int getDay()

    {
        return _day; //returns value
    }

    /**
     * gets the month
     * @return the month
     */

    public int getMonth()
    {
        return _month; //returns value
    }

    /**
     * gets the year
     * @return the year
     */

    public int getYear()
    {
        return _year; //returns value
    }

    /**
     * sets the day (only if date remains valid)
     @param dayToSet the day value to be set
     */

    public void setDay(int dayToSet)
    {
        int day=dayToSet;
        if (checkDate(day, _month, _year))
            _day=day;
    }

    /**
     * sets the month (only if date remains valid)
     @param monthToSet - the month value to be set
     */

    public void setMonth(int monthToSet)
    {
        if (checkDate(_day,monthToSet,_year)) //only sets if the new date is valid
            _month = monthToSet;

    }

    /**
     * sets the year (only if date remains valid)
     @param yearToSet - the month value to be set
     */

    public void setYear(int yearToSet)
    {
        if (checkDate(_day,_month,yearToSet) &&yearToSet>999 ) //only sets if the new date is valid
            _year = yearToSet;

    }

    /**
     * returns a String that represents this date
     * @return String that represents this date in the following format: day/month/year for example: 2/3/1998
     */

    public String toString()
    {
        return this._day + "/" + this._month + "/" + this._year; //prints string
    }

    /**
     Receives a date and converts it to an integer of days
     */
    private int calculateDate(int day, int month, int year)
    {
        if (month < MARCH) //conditions of formula
        {
            year--;
            month += 12;
        }
        return DAYS_IN_YEAR * year + year / FOUR - year / HUNDRED + year / FOURHUNDRED + ((month + 1) * 306) / TEN + (day - 62); //calculation of number of days

    }

    /**
     * receives a date and check if it is valid
     */

    //private method checks if a date is valid by checking if a date is a valid date in a month , a year is a valid year (including a leap year and
    private boolean checkDate(int day, int month, int year)
    {
        if ((( month==JANUARY)||(month==MARCH)||(month== MAY)||(month==JULY)||(month== AUGUST)||(month==OCTOBER)||(month==DECEMBER)))
            maxDayInMonth=31;
        else if (this.leap(year)==true && (month==FEBRUARY))
            maxDayInMonth=29;
        else if ( month==FEBRUARY && this.leap(year)==false)
            maxDayInMonth=28;
        else
            maxDayInMonth=30;
        if (this.yearCheck(year)==true && (day>=1) && (day<=maxDayInMonth) && (month>=JANUARY) && (month<=DECEMBER))
            return true;
        else
            return false;
    }

    //a private boolean to check if a year is considered a leap year or not
    private boolean leap ( int year )
    {
        return ( year%4 == 0 && year%100 != 0 ) || ( year%400==0 ) ? true : false;
    }

    //a private boolean check to see if the year is a valid 4 digit number
    private boolean yearCheck(int year)
    {
        String yearStr = Integer.toString(year);
        int yearDigits = yearStr.length();
        if  ((((yearDigits==FOUR)) && (Math.signum(year)>=0)) && (year % 1 == 0))
            return true;
        else
            return false;
    }
}