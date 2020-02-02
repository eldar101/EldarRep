//This is a main class for all the question's requirements, including testing all features
//Eldar Weiss 303169783
package q1;
//libraries

import java.util.Random;
import java.util.Scanner;

//start of main
public class Main {

    public static void main(String[] args) {
        final int size = 10; // amount of values per group
        Set<Integer> group1 = new Set<>(); //groups
        Set<Integer> group2 = new Set<>();
        Set<Integer> group3 = new Set<>();
        Set<Integer> group4 = new Set<>();
        Set<Person> personSet = new Set<>();

        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            group1.insert(rand.nextInt(100)); //we fill 3 sets with  random numbers from 0 to 100
            group2.insert(rand.nextInt(100));
            group3.insert(rand.nextInt(100));
        }
        System.out.println("Welcome to a demonstration of our sets!\n");
        System.out.println("The first set:");
        System.out.println(group1);
        System.out.println("The second set:");
        System.out.println(group2);
        System.out.println("The third set:");
        System.out.println(group3);
        group1.union(group2); //union of the first two sets
        System.out.println("Union of the first and second sets:");
        System.out.println(group1);
        group1.intersect(group3); //intersection with the third set
        System.out.println("Intersection of third set with a union of the first two sets:");
        System.out.println(group1);
        int firstNum, secondNum, thirdNum;
        firstNum = userInput("Enter the first number of a fourth set:");
        secondNum = userInput("Enter the second number of fourth set:");


        group4.insert(firstNum); //insertion
        group4.insert(secondNum);

        if (group1.isSubset(group4)) { //all conditions
            System.out.println("The fourth set is a subset of the first set");
        } else {
            System.out.println("The fourth set is not a subset of the first set");
        }
        if (group2.isSubset(group4)) {
            System.out.println("The fourth set is a subset of the second set");
        } else {
            System.out.println("The fourth set is not a subset of the second set");
        }
        if (group3.isSubset(group4)) {
            System.out.println("The fourth set is a subset of the third set");
        } else {
            System.out.println("The fourth set is not a subset of the third set");
        }

        thirdNum = userInput("Enter a third number:");

        if (group1.isMember(thirdNum)) { //we check if the third number is a member of the first set or not
            System.out.println(thirdNum + " is member of the first set"); //print if true
        }
        group2.insert(thirdNum); //we add the third number
        System.out.println("The second set after the adding the third number:");
        System.out.println(group2);
        group3.delete(thirdNum);
        System.out.println("The third set after the deleting the third number:");
        System.out.println(group3);


        //third part of q1
        Person p1 = new Person(1989, "Eldar", "Weiss", "303169783"); //5 people
        Person p2 = new Person(2010, "Gal", "Buchriz", "294567219");
        Person p3 = new Person(2000, "Kobi", "Buchrizberg", "127415982");
        Person p4 = new Person(1965, "Joel", "Joelson", "12465421");
        Person p5 = new Person(1950, "Boaz", "Marquee", "0362307262");

        personSet.insert(p1);
        personSet.insert(p2);
        personSet.insert(p3);
        personSet.insert(p4);
        personSet.insert(p5);


        System.out.println("The smallest person (lexicographically)  is:");
        System.out.println(Separate.minSet(personSet));


    }

    //basic input method for receiving the numbers of the fourth group
    private static int userInput(String s) {
        Scanner scan = new Scanner(System.in); //scanner input

        while (true) { //waiting for input
            System.out.println(s);
            if (scan.hasNextInt()) {
                return scan.nextInt();
            } else { //in case of error
                System.out.println("Error! Please try again!");
                scan.next();
            }
        }

    }
} //end of Main
		
