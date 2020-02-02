//This class defines a term in a polynom.
package q1;

import java.text.DecimalFormat;

public class Term implements Comparable  //implementing comparable
{
    private double coefficient; //variables of a "term"
    private double degree;

    //constructor
    public Term(double coefficient, double degree) {
        this.coefficient = coefficient;
        this.degree = degree;
    } //end of constructor

    // get degree
    public double getDegree() {
        return this.degree;
    } //end of getDegree

    // set degree
    public void setDegree(double degree) {
        this.degree = degree;
    }// end of setDegree

    // get coefficient
    public double getCoefficient() {
        return this.coefficient;
    }// end of getCoefficient

    //set coefficient
    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    } // end of setCoefficient

    //override of string for our polynomial purposes
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("###.#");
        if (degree == -1) { //getting rid of numbers in derivative
            return ("");
        } //scenarios to get rid of 1x, -1x and zeroes
        if (degree == 0) { //no x in polynomial
            if (coefficient == 1) {
                return ("1 ");
            } else {
                return (df.format(coefficient) + " ");
            }
        }
        if (degree == 1) { //getting rid of degree when it's just "1"
            if (coefficient == 1) {
                return ("x ");
            }
            if (coefficient == -1) { //same for negative
                return ("-x ");
            } else {
                return (df.format(coefficient) + "x ");
            }
        }
        if (coefficient == 1) { //getting rid of 1 in front of x
            return ("x^" + df.format(degree) + " ");
        }
        if (coefficient == 0) { //getting rid of zeroes
            return ("");
        }
        if (coefficient == -1) {
            return ("-x^" + df.format(degree) + " ");
        }

        return (df.format(coefficient) + "x^" + df.format(degree) + " "); //regular situation
    } //end of toString

    //adding a term
    public void addTerm(Term addend) {
        if (this.getDegree() != addend.getDegree()) { //compare degrees
            System.out.println("Can't add terms that have different degrees!");
        } else {

            this.setCoefficient(this.getCoefficient() + addend.getCoefficient());
        }
    }//end of addTerm

        //subtracting a term
    public void subtractTerm(Term subtrahend) {
        if (this.getDegree() != subtrahend.getDegree()) {
            System.out.println("Can't subtract terms that have different degrees!!");
        } else {
            this.setCoefficient(this.getCoefficient() - subtrahend.getCoefficient());
        }
    }//end of subtractTerm

    //differentiate for derivative
    public void differentiate() {
        this.setCoefficient(this.getCoefficient() * this.getDegree());
        this.setDegree(this.getDegree() - 1);
    } //end of differentiate


    //comparing degrees in compareTo override
    @Override
    public int compareTo(Object obj) {
        if (this.getDegree() > ((Term) obj).getDegree()) {
            return -1;
        }
        if (this.getDegree() < ((Term) obj).getDegree()) {
            return 1;
        } else {
            return 0;
        }
    } //end of compareTo
}