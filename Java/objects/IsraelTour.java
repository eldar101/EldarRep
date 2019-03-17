/**
 *** This class represents an IsraelTour object,
 * which is composed of an array of Trips.
 *@author Eldar Weiss
 */
public class IsraelTour
{

    // instance variables
    private final int MAX_TRIPS = 100;
    private Trip[] _data;
    private int _noOfTrips;

    /*
     * default constructor.
     */
    public IsraelTour()
    {
        _data = new Trip[MAX_TRIPS];
        _noOfTrips = 0;
    }

    /**
     * Get the number of trips in the tour
     * @return The number of trips that are in the tour
     */
    public int getNoOfTrips() {
        return _noOfTrips;
    }

    /**
     * Add a new trip to the tour
     * if the tour is full or the trip is empty, it will not be added
     * @param trip the new trip to add
     * @return True if the new trip was added, false otherwise
     */
    public boolean addTrip(Trip trip)
    {
        if ((trip != null) && (_noOfTrips <= MAX_TRIPS - 1))
        {
            _data[_noOfTrips] = new Trip(trip);
            _noOfTrips++;
            return true;
        }
            return false;
    }

    /**
     * Removes a trip from the list
     * if the list is empty or the trip is empty, it will not be removed
     * @param trip the trip to remove
     * @return True if the  trip was removed, false otherwise
     */
    public boolean removeTrip(Trip trip)
    {

        if(trip!=null)
        {
            for(int k=0;k<_noOfTrips;k++)
            {
                /*
                 * if the trip was found,removes the trip by running over it
                 * with the last object of the array.
                 */
                if(_data[k].equals(trip))
                {
                    _data[k] = _data[_noOfTrips-1];
                    _data[_noOfTrips-1] = null;
                    _noOfTrips--;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines how many travellers were in the tour
     * @return number of travellers in tour
     */
    public int howManyTravellers()
    {
        int index;
        int noOfTravellers = 0;
        for (index = 0; index < _noOfTrips; index++) {
            noOfTravellers += _data[index].getNoOfTravellers(); //add amount of travellers by trips
        }

        return noOfTravellers;
    }

    /**
     * Count how many trips depart on a certain date
     * @param date date of departure
     * @return Number of trips that depart on a given date
     */
    public int howManyTripsDeparture(Date date)
    {
        int index, numOfDepartures = 0;
        for (index = 0; index < _noOfTrips; index++)
        {
            if (date.equals(_data[index].getDepartureDate()))
                numOfDepartures++; //adds counter when next trip has same departure date
        }

        return numOfDepartures;
    }

     /**
     * Find the number of cars needed for all trips departing at a given date
     * @param date date of departure
     * @return Number of cars needed for departure date
     */
    public int howManyCars(Date date)
    {
        int index, numOfCars = 0;
        for (index = 0; index < _noOfTrips; index++)
        {
            if (date.equals(_data[index].getDepartureDate()))
                numOfCars += _data[index].howManyCars(); //adds total amount of cars
        }

        return numOfCars;
    }

    /**
     * Search the trips list for the trip with the longest duration and return a copy of the trip.
     * @return A copy of the longest trip.
     */
    public Trip longestTrip()
    {
        int index, longestTripIndex;
        int prev = 0;
        if (_noOfTrips == 0)
            return null; //no longest trip if the list is empty
        else {
            longestTripIndex = 0;
            for (index = 0; index < _noOfTrips; index++) {
                if (_data[index].tripDuration() > _data[prev].tripDuration()) //checks trip duration to find the longest
                {
                    longestTripIndex = index;
                    prev++;
                }
            }
            return new Trip(_data[longestTripIndex]);
        }
    }

    /**
     * find most popular guide, the guide that guides the most trips in the list.
     * If more than one guide guides the most trips - return the first of them (the guide)
     * @return The name of the most popular guide.
     */
    public String mostPopularGuide()
    {
        int maxCounter = 0;
        int counter;
        if(_noOfTrips == 0)
            return "";
        String popularGuide = _data[0].getGuideName(); //most popular at first
        for (int i = 0; i < _noOfTrips; i++)
        {
            counter = 0;
            String guideName = _data[i].getGuideName();
            for (int j = 0; j < _noOfTrips; j++) {
                if (guideName.equals( _data[j].getGuideName())) //if appears again add to counter
                    counter++;
            }

            if (maxCounter < counter) { //check who has the most appearances as guide
                maxCounter = counter;
                popularGuide = _data[i].getGuideName();
            }
        }
        if (popularGuide.equals(""))
            return null;
        return popularGuide;
    }
    /**
     * Finds the date of the earliest trip on the list of trips
     * @return The a copy of the trip's departure date.
     */
    public Date earliestTrip()
    {
        if(_noOfTrips == 0)
            return null;
        Date earliestTrip = _data[0].getDepartureDate(); //first trip
        for (int i = 1; i < _noOfTrips; i++)
        {
            if (_data[i].getDepartureDate().before(earliestTrip)) //compares departure dates
                earliestTrip = _data[i].getDepartureDate();
        }
        return new Date (earliestTrip);
    }
     /**
     * Finds the most expensive trip on the list
      * @return A copy of the most expensive trip   .
     */
     public Trip mostExpensiveTrip()
     {
         int i;
         if(_noOfTrips == 0)
             return null;
         Trip mostExpensive = new Trip(_data[0]);
         for (i = 0; i<_noOfTrips; i++)
         {
             if (_data[i].calculatePrice() >(mostExpensive.calculatePrice())) //compare and find price
                 mostExpensive = new Trip(_data[i]);
         }
         return (mostExpensive);
     }
}
