/**
 * Created by Eldar on 09/11/2016.
 * This program asks the user for a date and an integer between 1 to 10, then prints a date which is the input date
 * with the received integer's amount of days added to it.
 * If the date is invalid or, the if the integer is not between 1-10, the program will print an error message.
 */
import java.util.Scanner;
public class Dates
{

    public static void main(String[] args)
    {
        int daysInMonth, day, month, year, num, temp, newMonth,newDay, newYear; //declaring variables
        Scanner scan = new Scanner (System.in);
        daysInMonth = 31; //the days in each month according to the Gregorian Calendar
        newDay = 0;

        System.out.println("Please enter 3 integers to represent a valid date:");
        day = scan.nextInt(); //user input
        month = scan.nextInt();
        year = scan.nextInt();
        newYear = year; //month and date stay the same unless stated otherwise
        newMonth = month;

        if ((month == 4) || (month == 6) || (month == 9) || (month == 11))//rules of the Gregorian calendar
        {
            daysInMonth = 30;
        }
        else if (month == 2)
        {
            daysInMonth = 28;
            if (((year%4 == 0) && (year%100 != 0)) || (year % 400 == 0) ) //conditions of the days in a month in a leap year
            {
                daysInMonth = 29;
            }
        }
        if ((year < 0) || (month < 1) || (month > 12) || (day < 1) || (day > daysInMonth)) //conditions of an invalid date
        {
            System.out.println("The original date " + day + "/" + month +"/" + year + " is invalid.");
        }
        else
        {
            System.out.println("Please enter an integer which represents the number of days:");
            num = scan.nextInt();
            if (num <= 0) //input number must be positive
            {
                System.out.println("The number of days must be positive.");
            }
            else if (num > 10) //input number must be between 1 to 10
            {
                System.out.println("The number of days must be between 1 to 10.");
            }
            else
            {
                temp = num + day;
                newDay = temp;
                if (temp > daysInMonth) //condition when the new date is past the current month
                {
                    newDay = temp - daysInMonth;
                    newMonth++;
                }
                if (newMonth > 12) //if new date is past December, program skips to next year's January
                {
                    newMonth = 1;
                    newYear++;
                }
                System.out.println("The original date is " + day + "/" + month +"/" + year + ".");
                System.out.println("After " + num + " days the date is " + newDay + "/" + newMonth + "/" + newYear + ".");
            }
        }
    }
}