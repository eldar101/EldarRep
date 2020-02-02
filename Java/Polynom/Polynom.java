//This class a polynomial and actions around it

package q1;

import java.util.ArrayList;
import java.util.Collections;

public class Polynom {
    private static final int ADD = 1; //final variables
    private static final int SUBTRACT = 0;

    ArrayList<Term> terms;

    public Polynom(double[] coefficient, double[] degree) {

        if (coefficient.length != degree.length) {
            throw new UnsupportedOperationException("Can't create Polynomial from unmatched length arrays!");
        } else {
            this.terms = new ArrayList<>();
            for (int i = 0; i < coefficient.length; i++) {
                Term temp = new Term(coefficient[i], degree[i]);
                this.terms.add(temp);
            }
            Collections.sort(this.terms); //sort using collections
        }
    } //end of Polynom

    //get length
    public int getPolynomLength() {
        return this.terms.size();
    } //end of getPolynomLength

    //differentiate the polynom term by term
    public void differentiate() {
        for (Term term : this.terms) {
            term.differentiate();
        }
    } //end of differentiate

    //inserting a term into the polynomial
    public void insertTerm(Term addend, int actionDesc) {
        int i = 0;
        Term temp;
        temp = this.terms.get(i);
        while (addend.compareTo(temp) < 1 && i < this.terms.size()) {
            i++;
            temp = this.terms.get(i);
        }
        if (addend.compareTo(temp) == 0) {
            if (actionDesc > 0) {
                temp.addTerm(addend);
            } else {
                temp.subtractTerm(addend);
            }
        } else {
            this.terms.add(i, addend);
        }
        Collections.sort(this.terms); //sort after adding
    } //end of insertTerm

    //summing up two polynomials term by term
    public void plus(Polynom addend) {
        if (this.getPolynomLength() >= addend.getPolynomLength()) {
            this.addOrSubtract(addend, ADD);
        } else {
            addend.addOrSubtract(this, ADD);
        }
    }//end of Plus

    //subtracting two polynomials
    public void minus(Polynom subtrahend) {
        if (this.getPolynomLength() >= subtrahend.getPolynomLength()) {
            this.addOrSubtract(subtrahend, SUBTRACT);
        } else {
            subtrahend.addOrSubtract(this, SUBTRACT);
        }
    }//end of Minus

    //we track two array lists of terms and add/subtract one to another
    public void addOrSubtract(Polynom addend, int action) {
        int i = 0, j = 0, counter = 0;
        if (action == SUBTRACT) { //we turn one polynom to a negative and then add it
            for (int k = 0; k < this.getPolynomLength(); k++)
                this.terms.get(k).setCoefficient(0 - this.terms.get(k).getCoefficient());
        }
        while (i < this.getPolynomLength() && j < addend.getPolynomLength()) { //tracking and adding
            if (this.terms.get(i).getDegree() == (addend.terms.get(j).getDegree())) {
                this.terms.get(i).addTerm(addend.terms.get(j));
                i++; //incremending
                j++;
                counter = 1;
            } else if (this.terms.get(i).getDegree() > addend.terms.get(j).getDegree())
                i++; //tracking forward
            else
                j++;
        }
        if (counter == 0) {
            this.insertTerm(addend.terms.get(j), ADD); //otherwise just add the term if it's not found
        }
    }//end of addOrSubtract

    //override of toString to combine terms
    @Override
    public String toString() {

        String polynomAsString = new String();
        int i = 0;
        Term temp;
        temp = this.terms.get(i);

        polynomAsString = temp.toString();
        i++;
        while (i < this.terms.size()) {
            temp = this.terms.get(i);
            if (temp.getCoefficient() > 0) {
                polynomAsString += "+ ";
            }
            polynomAsString += temp.toString();
            i++;
        }
        return polynomAsString;
    }
}//end of toString