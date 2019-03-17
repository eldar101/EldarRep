 /*
 ** This class represents an a number of different programs dealing with arrays and recursions
 * @author Eldar Weiss
 **/

import static java.lang.Math.*;

public class Ex14 {
   /*
     * 1)the method what receives an array and calculates the length of the longest sub-array whose sum is divisible by 3
     * 2) O(n^2) before it has a loop inside a loop and both scan the entire array
     * 4) O (n) because it goes over the entire array once.
     */

    /**
     * Get the longest continuous sub-array whose sum can be divisible by 3
     * @param a a given array of number
     * @return length of the longest continuous sub-array whose sum can be divisible by 3
     */
    public static int what(int[] a) {
        int firstZero = -1, firstOne = -1, firstTwo = -1, lastZero = -1, lastOne = -1, lastTwo = -1; //remainders
        int sumLeft = 0, sumRight = 0; //summing the array
        if (a.length == 0) { // if array is empty
            return 0;
        }
        if ((a.length == 1) && (a[ 0 ] % 3 != 0)) //if array is a single number that isn't divisible by 3
        {
            return 0;
        }
        for (int el : a) { ///summing the remainders
            sumRight += el % 3;
        }
        sumRight = negMod(sumRight % 3); //summing the array from it's end
        for (int i = 0; i < a.length + 1; i++) {
            if (((sumLeft % 3) == 0) && (firstZero == -1)) //saving first and last 0,1,2 remainders
            {
                firstZero = i;
            }
            if (sumRight % 3 == 0 && lastZero == -1)
                lastZero = a.length - 1 - i;
            if (sumLeft % 3 == 1 && firstOne == -1)
                firstOne = i;
            if (sumRight % 3 == 1 && lastOne == -1)
                lastOne = a.length - 1 - i;
            if (sumLeft % 3 == 2 && firstTwo == -1)
                firstTwo = i;
            if (sumRight % 3 == 2 && lastTwo == -1)
                lastTwo = a.length - 1 - i;
            sumLeft += negMod(a[ min(i, a.length - 1) ] % 3); //summing and accommodating size of array
            sumRight = negMod(sumRight - a[ max(a.length - 1 - i, 0) ] % 3);
        }
        int zero = lastZero - firstZero + 1; //finding length of sequence
        int one = lastOne - firstOne + 1;
        int two = lastTwo - firstTwo + 1;
        return max(max(zero, one), two); //the longest sequence
    }

    /**
     * Get negative number's modulo 3 by adding 3 to it.
     * @param a an integer
     * @return the integer increased by 3 if negative, otherwise, return the integer
     */
    public static int negMod(int a) {
        if (a < 0) {
            return (a + 3);
        }
        return a;
    }
    /**
     * Get an array of 0's and 1's and replace 1's with their distance from another zero
     * @param a an array of 0's and 1's
     */
    public static void zeroDistance (int [] a) {
        // closest == -1 means no zero was found yet
        int closest = -1;
        for (int i=0 ; i<a.length ; i++) {
            if (a[i] == 0) //if array starts with zero
                closest = 0;
            else {
                if (closest == -1) //if no zeroes were found
                    a[i] = a.length - 1;
                else {
                    closest++; //increase distance
                    a[i] = closest;
                }
            }
        }
        closest = -1;
        for (int i=a.length-1 ; i>=0 ; i--) //restart and scan from right side
            if (a[i] == 0)
                closest = 0;
            else if (closest != -1 && a[i] > ++closest)
                a[i] = closest;
    }
    /**
     * Get two Strings and determine if one is a "transformed" version of the other
     * @param s the original String
     * @param t the other string that needs to be determined as "transformed" or not
     * @return  an overriding method for isTrans
     */
    public static boolean isTrans(String s, String t) {
        return isTrans(s, t, 0, 0);
    }
    /**
     * Get two Strings and determine if one is a "transformed" (one char multiplied several times) version of the other
     * @param s the original String
     * @param t the other string that needs to be determined as "transformed" or not
     * @param i the index of the first String
     * @param j the index of the other String
     * @return true if t is a "transformed" s, false otherwise
     */

    public static boolean isTrans(String s, String t, int i, int j) {
        if (t.length() < s.length()) // t can't be a transformed s if it's shorter
            return false;
        if (s.charAt(0) != t.charAt(0)) // t can't be a transformed s if it starts differently
            return false;
        if (s.charAt(i) == t.charAt(j)) { //if two chars are the same
            if ((i == s.length() - 1) && (j == t.length() - 1)) { //true if end of strings
                return true;
            } else if (i == s.length() - 1) { // if end of s
                return isTrans(s, t, i, ++j);
            } else {
                isTrans(s, t, ++i, ++j); //both continue
            }
        } else {
            return j != t.length() - 1 && t.charAt(j - 1) == t.charAt(j) && (isTrans(s, t, i, ++j));
        }
        return true;
    }

    /**
     * Get two arrays, one of different numbers and one that consists of 0's,1's and 2's and determines if the first
     * array contains a sub-array that fits the second array's pattern (0 symbolizes an two-digit or one-digit number, 1 symbolizes a one-digit number
     * and 2 symbolizes a two-digit number)
     * @param a array of integers
     * @param pattern an array containing a pattern of 0's,1's and 2's
     * @return  an overriding method match
     */
    public static boolean match(int[] a, int[] pattern) {
        return match(a, pattern, 0, 0, 0, 0);
    }


    /**
     * Get two arrays, one of different numbers and one that consists of 0's,1's and 2's and determines if the first
     * array contains a sub-array that fits the second array's pattern (0 symbolizes an two-digit or one-digit number, 1 symbolizes a one-digit number
     * and 2 symbolizes a two-digit number)
     * @param a array of integers
     * @param pattern an array containing a pattern of 0's,1's and 2's
     * @param i array's index
     * @param j pattern's index
     * @param subStr length of sub-array
     * @return  true if sub-array that fits pattern exists, false otherwise.
     */
    public static boolean match(int[] a, int[] pattern, int i, int j, int subStr, int count) {
        if (a.length < pattern.length) //if shorter than pattern it's false
            return false;
        if (((fitsPattern(a[i], pattern[j])) && (j == pattern.length - 1)) || (pattern.length == 0))
            return true; //pattern is empty or matched until end of pattern
        else if (fitsPattern(a[i], pattern[j])) //array fits pattern
            return match(a, pattern, ++i, ++j, subStr, ++count); //move on to next numbers in array and pattern
        else if (subStr < a.length - pattern.length) { //flip to start the sub-array from next index of array
            subStr = i - count + 1;
            i = subStr;
            j = 0;
            count = 0;
            return (match(a, pattern, i, j, subStr, count));
        }
        return false;
    }

    /**
     * Receives two integers, a number and a pattern representing an amount of digits and determines if the number
     * has that amount of digits or not
     * @param num - number received
     * @param pattern a number symbolizing the number's amount of digits
     * @return  true if number has fits the pattern, false otherwise
     */
    public static boolean fitsPattern(int num, int pattern)
    {
        if (pattern == 0)  //can be one digit or two
            return ((numDig(num) == 2) || (numDig(num) == 1));
        if (pattern == 1) //has one digit
            return (numDig(num) == 1);
        return pattern == 2 && (numDig(num) == 2); //has two digits
    }
    /**
     * Get a number and calculate how many digits it has
     * @param num - number received
     * @return  number of digits
     */
    public static int numDig(int num) {
        if (num / 10 == 0)
        { //if has one digit
            return 1;
        } else //add 1 for each digit recursively
            return numDig(num / 10) + 1;
    }

}