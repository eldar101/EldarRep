/**
 * This program receives an integer representing milliseconds and Prints that amount converted to days,hours and seconds.
 * Eldar Weiss
 * 03.11.2016
 */
import java.util.Scanner;
public class Milliseconds
{
    public static void main (String [] args)
    {
    final int THOUSAND = 1000; //declaring variables for conversion to a date
    final int SIXTY = 60;
    final int HOURSINDAY = 24;
    long days, hours, minutes, seconds, temp;

    Scanner scan = new Scanner (System.in);
    System.out.println("This program reads an integer which " + "represents Milliseconds" + " and converts it to days, " + "hours,  minutes and seconds. ");
    System.out.println ("Please enter the number of Milliseconds");
    long ms = scan.nextLong();
    temp = ms / THOUSAND; //holds the total seconds.
    seconds = temp % SIXTY;//holds the final seconds value.
    temp /= SIXTY;
    minutes = temp%SIXTY; //holds the final minutes value
    temp /= SIXTY;
    hours = temp %HOURSINDAY; //holds the final hours value
    days = temp/HOURSINDAY;//holds the final days value
    System.out.println(days + " days " + hours + ":" +minutes + ":" + seconds + " hours");
    }  //end of method main
} //end of class Milliseonds