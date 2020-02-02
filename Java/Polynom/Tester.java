//Eldar Weiss 303169783
//This is just a main program for testing all our functions.

package q1;

import java.util.ArrayList;
import java.util.Scanner;

public class Tester {

    public static void main(String args[]) {

        Scanner scan = new Scanner(System.in); //variables
        ArrayList<Term> terms = new ArrayList<>();
        double coefficient, degree;
        String answer = "y";


        System.out.println("Hello! Please insert the first Polynomial: \n");
        while (answer.equals("y")) {
            System.out.println("Coefficient: ");
            coefficient = scan.nextDouble();
            System.out.println("Degree: ");
            degree = scan.nextDouble();
            Term temp = new Term(coefficient, degree);
            terms.add(temp);
            System.out.println("continue? y/n (Ending now will move on to the second polynomial)");
            answer = scan.next();
            if (answer.equals("y")) {
                continue;
            }
            if (answer.equals("n")) {
                break;
            }
            while (!answer.equals("n") && !answer.equals("y")) {
                System.out.println("Please enter y/n");
                answer = scan.next();
            }

        }
        double[] coefficients1 = new double[terms.size()];
        double[] degrees1 = new double[terms.size()];
        fillArrays(terms, coefficients1, degrees1);
        Polynom poly1 = new Polynom(coefficients1, degrees1);
        terms.clear();
        answer = "y";
        System.out.println("Please insert the second Polynomial: \n");
        while (answer.equals("y")) {
            System.out.println("Coefficient: ");
            coefficient = scan.nextDouble();
            System.out.println("Degree: ");
            degree = scan.nextDouble();
            Term temp = new Term(coefficient, degree);
            terms.add(temp);
            System.out.println("continue? y/n (Ending now will move on to polynomial actions)");
            answer = scan.next();
            if (answer.equals("y")) {
                continue;
            }
            if (answer.equals("n")) {
                break;
            }
            while (!answer.equals("n") && !answer.equals("y")) {
                System.out.println("Please enter y/n");
                answer = scan.next();
            }

        }
        double[] coefficients2 = new double[terms.size()];
        double[] degrees2 = new double[terms.size()];
        fillArrays(terms, coefficients2, degrees2);
        Polynom poly2 = new Polynom(coefficients2, degrees2);
        System.out.println("The first polynomial is: " + poly1.toString());
        System.out.println("The second polynomial is: " + poly2.toString());
        ;
        int choice;
        System.out.println("Pick an option:\n" + "1. Sum polynomials\n2. Subtract polynomials\n3. Differntiate polynomials\n ");
        choice = scan.nextInt();
        if (choice == 1) {
            poly1.plus(poly2);
            System.out.println(poly2.toString());
        }
        if (choice == 2) {
            poly2.minus(poly1);
            System.out.println(poly2.toString());
        }
        if (choice == 3) {
            poly1.differentiate();
            poly2.differentiate();
            System.out.println("First polynomials's derivative: " + poly1.toString());
            System.out.println("Second polynomials's derivative: " + poly2.toString());
        }
    }
    //filling arrays with input from user
    public static void fillArrays(ArrayList<Term> terms, double[] coefficients, double[] degrees) {
        for (int i = 0; i < terms.size(); i++) {
            coefficients[i] = terms.get(i).getCoefficient();
            degrees[i] = terms.get(i).getDegree();
        }
    }

}//end of class
