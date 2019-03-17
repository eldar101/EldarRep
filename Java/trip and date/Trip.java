/**
 *This class represents a Trip object
 *@author Eldar Weiss
 *
 */
public class Trip
{
    // instance variables
    private String _guideName = "NoName"; //a default init for _guideName
    private int _noOfCountries; //up to 10
    private Date _departureDate;
    private Date _returningDate;
    private int _noOfTravellers; //up to 50

    private final int PRICE_PER_DAY = 250;
    private final int PRICE_PER_COUNTRY = 100;
    private final int DEFAULT_NO_OF_COUNTRIES = 1;
    private final int DEFAULT_NO_OF_TRAVELLERS = 50;
    private final int MAX_IN_CAR = 10;
    private final int DAYS_IN_WEEK = 7;
    private final int JANUARY = 1;
    private final int FEBRUARY = 2;
    private final int APRIL = 4;
    private final int SEPTEMBER = 9;
    private final int NOVEMBER = 11;
    private final int JULY = 7;
    private final int AUGUST = 8;
    private final int FOURHUNDRED = 400;
    private final int HUNDRED = 100;
    private final int TWOTHOUSAND = 2000;


    /**
     * creates a new Date object
     *@param name of the guide of the trip
     *@param depDate - the departure date
     *@param retDate - the return date
     *@param noOfCountries - the number of countries to be visited in the trip(1-10)
     *@param noOfTravellers - the number of travellers(1-50)
     */

    public Trip(String name, Date depDate, Date retDate, int noOfCountries, int noOfTravellers)
    {
        setGuideName(name); //set name and then date
        if ((depDate.after(retDate) || retDate.before(depDate)))
        {
            _departureDate = new Date(1,JANUARY,TWOTHOUSAND);
            _returningDate = new Date(1,JANUARY,TWOTHOUSAND);
        }
        else
        {
            _departureDate = new Date(depDate);
            _returningDate = new Date(retDate);
        }
        _noOfCountries = noOfCountries;
        _noOfTravellers = noOfTravellers;
    }
    /**
     * creates a new Date object
     *@param name - of the guide of the trip
     *@param depDay - the day of the departure date(1-31)
     *@param depMonth - the month of the departure date(1-12)
     *@param depYear - the year of the departure date (4 digits)
     *@param retDay - the day of the return date(1-31)
     *@param retMonth - the month of the return date(1-12)
     *@param retYear - the year of the return date(4 digits)
     *@param noOfCountries - the number of countries to be visited in the trip(1-10)
     *@param noOfTravellers - the number of travellers(1-50)
     */
    public Trip(String name, int depDay, int depMonth, int depYear, int retDay, int retMonth, int retYear, int noOfCountries, int noOfTravellers)
    {
        setGuideName(name);
        Date depTemp = new Date(depDay, depMonth, depYear);
        Date retTemp = new Date(retDay, retMonth, retYear);
        if ((depTemp.after(retTemp) || (retTemp.before(depTemp))))
        {
            _departureDate = new Date(1, JANUARY, TWOTHOUSAND);
            _returningDate = new Date(1, JANUARY, TWOTHOUSAND);
        }
        else
        {
            _departureDate = new Date(depDay, depMonth, depYear);
            _returningDate = new Date(retDay, retMonth, retYear);
        }
        _noOfCountries = noOfCountries;
        _noOfTravellers = noOfTravellers;
    }

    /**
     * copy constructor
     * @param otherTrip - trip to be copied
     */
    public Trip(Trip otherTrip)
    {
        _guideName = otherTrip._guideName; //set date
        _departureDate = new Date(otherTrip._departureDate);
        _returningDate = new Date(otherTrip._returningDate);
        _noOfCountries = otherTrip._noOfCountries;
        _noOfTravellers = otherTrip._noOfTravellers;
    }
    /**
     * calculates total price of the trip according to days of the trip and number of countries visited
     * @return the total price of the trip
     */
    public int calculatePrice()
    {
        //calculate 20% from the price and add to the price
        int totalPrice = (PRICE_PER_DAY*tripDuration()) + (PRICE_PER_COUNTRY*_noOfCountries);
        int depMonth = _departureDate.getMonth();
        if (depMonth >= JULY && depMonth <= AUGUST) { //conditions of price raise in summer
            totalPrice *= 1.2;
        }
        totalPrice += HUNDRED*howManyWeekends(); //add weekends
        return (totalPrice);
    }
    /**
     * check if 2 trips are the same
     * @param other the trip to compare this trip to
     * @return true if the trips are the same
     */
    public boolean equals(Trip other)  //equality of two trips
    {
        if ((this._guideName.equals(other._guideName)) &&(this._noOfCountries == other._noOfCountries ) && (this._noOfTravellers == other._noOfTravellers  ) && (sameDepartureDate(other)) &&  (sameReturningDate(other)))
            return true;
        else
            return false;
    }

    /**
     * calculates the date of first weekend of the trip
     * @return the date of the first weekend of the trip or null if there is no weekend on the trip
     */

    public Date firstWeekend() //first weekend check
    {
        Date weekend = new Date(_departureDate);
        int daysInMonth = 31;
        int curMonth = _departureDate.getMonth();
        int curYear = _departureDate.getYear();
        int daysUntilWeekend = DAYS_IN_WEEK-_departureDate.dayInWeek();
        if ((curMonth == APRIL) || (curMonth == JULY) || (curMonth == SEPTEMBER) || (curMonth == NOVEMBER))
        {
            daysInMonth = 28; //sets number of days in certain months
        }
        else if (curMonth == FEBRUARY)
        {
            daysInMonth = 28;
            if (((curYear % 4 == 0) && (curYear % HUNDRED != 0)) || (curYear % FOURHUNDRED == 0)) //conditions of the days in a month in a leap year
            {
                daysInMonth = 29; //amount of days in a leap year
            }
        }
        if (daysUntilWeekend ==0) {
            weekend.setDay(_departureDate.getDay());
            weekend.setMonth(curMonth);
            weekend.setYear(curYear);
        }
        if (daysUntilWeekend >0 && daysUntilWeekend< DAYS_IN_WEEK) //situation where depDate is not the weekend and a weekend will come in a few days
        {
            if (_departureDate.getDay() + daysUntilWeekend >= daysInMonth) {  //if it skips to next month
                if (curMonth == 12)
                { //if it skips to next year
                    curYear++;
                    curMonth = JANUARY;
                }
                else
                {
                    curMonth++; //skip to next month
                }
                weekend.setDay(daysInMonth-_departureDate.getDay() -daysUntilWeekend);
                weekend.setMonth(curMonth);
                weekend.setYear(curYear);
            }
            else {
                weekend.setDay(_departureDate.getDay() + daysUntilWeekend);
                weekend.setMonth(curMonth);
                weekend.setYear(curYear);
            }
        }
        if (weekend.after(_returningDate))
        {//if the trip is already over there isn't a first weekend
            return null;
        }
        return weekend;
    }

    /**
     * gets the trip departure date
     * @return the departure date
     */

    public Date getDepartureDate()

    {
        return new Date(_departureDate);
    }

    /**
     * gets the guide name
     * @return the guide date
     */

    public String getGuideName()
    {
        return _guideName;
    }

    /**
     * gets the number of countries to be visit in the trip
     * @return the number of countries
     */

    public int getNoOfCountries()
    {
        return _noOfCountries;
    }

    /**
     * gets the number of travellers in the trip
     * @return the number of travellers
     */

    public int getNoOfTravellers() {
        return _noOfTravellers;
    }

    /**
     * gets the trip return date
     * @return the return date
     */

    public Date getReturningDate()
    {
        return new Date(_returningDate);
    }

    /**
     * calculates the minimum number of buses needed for the trip
     * @return the number of buses needed for the trip
     */

    public int howManyCars()
    {
        int cars = _noOfTravellers / MAX_IN_CAR; //calculates how many cars it takes
        if (_noOfTravellers % MAX_IN_CAR != 0) {
            cars++;
        }
        return cars;
    }

    /**
     * calculates how many weekends were in the trip
     * @return the number of weekends occurring during the trip
     */

    public int howManyWeekends()
    {
        int dayNum = _departureDate.dayInWeek();
        if (dayNum == 0)
            dayNum += DAYS_IN_WEEK; //In other word, add one weekend
        dayNum--;
        return ((tripDuration() + dayNum)/ DAYS_IN_WEEK);
    }

    /**
     * check if trip is loaded
     * @return true if the number of countries to visit is greater than the trip duration - else return false
     */

    public boolean isLoaded()
    {
        return (_noOfCountries > tripDuration());
    }

    /**
     * check if two trips overlap with their dates
     * @param otherTrip - the trip to check if overlaps with this trip
     * @return true if the two trip have overlapping dates otherwise false
     */

    public boolean overlap(Trip otherTrip)
    {
        //overlap is if start day of a first trip less than a finish of the second and start of the second is less than a finish of the first .
        if (this.sameDepartureDate(otherTrip) || this.sameReturningDate(otherTrip))
            return true;
        if(this._departureDate.equals(otherTrip._returningDate))
            return true;
        if(otherTrip._departureDate.equals(this._returningDate))
            return true;
        if(otherTrip._departureDate.before(this._returningDate)&& otherTrip._departureDate.after(this._departureDate))
            return true;
        if(otherTrip._returningDate.before(this._returningDate)&& otherTrip._returningDate.after(this._departureDate))
            return true;
        return false;
    }
    /**
     * check if two trips have the same departure date
     * @param otherTrip - the trip to compare to
     * @return true if the two trips have the same departure date otherwise false
     */

    public boolean sameDepartureDate(Trip otherTrip) {
        return otherTrip != null && _departureDate.equals(otherTrip.getDepartureDate());
    }

    /**
     * check if two trips have the same return date
     * @param otherTrip - the trip to compare to
     * @return check if two trips have the same return date
     */

    public boolean sameReturningDate(Trip otherTrip) {
        return otherTrip != null && _returningDate.equals(otherTrip.getReturningDate());
    }

    /**
     * sets the trip departure day the date will change only if the new departure date is before the return date or equal to it.
     * @param newDepDate the value to be set
     */


    public void setDepartureDate(Date newDepDate)
    //    //using before and equals method of a date class
    {
        if (newDepDate.before(this._returningDate) || newDepDate.equals(this._returningDate))
        {
            this._departureDate = new Date (newDepDate);
        }
    }

    /**
     * sets the guide name .
     * @param otherName the value to be set
     */

    public void setGuideName(String otherName) {
        _guideName = otherName;
    }

    /**
     * sets the number of countries (only if valid) .
     * @param otherNoOfCountries - the value to be set
     */

    public void setNoOfCountries(int otherNoOfCountries)
    {if ((otherNoOfCountries >= DEFAULT_NO_OF_COUNTRIES) && (otherNoOfCountries <= 10))
        this._noOfCountries = otherNoOfCountries;
    }

    /**
     * sets the number of travellers (only if valid)  .
     * @param otherNoOfTravellers - the value to be set
     */

    public void setNoOfTravellers(int otherNoOfTravellers)
    {if ((otherNoOfTravellers >= 1) && (otherNoOfTravellers <=DEFAULT_NO_OF_TRAVELLERS)) {
        this._noOfTravellers = otherNoOfTravellers;
    }
    }

    /**
     * sets the trip return date the date will change only if the new return date is after the departure date or equal to it. .
     * @param newRetDate - the value to be set
     */

    public void setReturningDate(Date newRetDate)
    //using after and equals method of a date class
    {
        if ((newRetDate.after(this._departureDate) || (newRetDate.equals(this._departureDate)))==true)
        {
            _returningDate = new Date(newRetDate);
        }
    }

    /**
     * return a string representation of this trip.
     * @return representation of the trip in the following format: Trip:guide name|departure date-return date|number of countries|number of travellers for example: Trip:Yossi Chen|2/3/1998|10/3/1998|5|25
     */

    public String toString() {
        return ("TRIP:" + _guideName + "|" + _departureDate + "|" + _returningDate + "|" + _noOfCountries + "|" + _noOfTravellers);
    }

    /**
     * calculates the number of days of the trip.
     * @return the number of days of the trip
     */

    public int tripDuration()
    {
        return _returningDate.difference(_departureDate)+1;
    }

}