//This method is for biting the panels

package q1;

import javax.swing.*;
import java.awt.*;

public class BiteThread extends Thread {

    private Controller controller;
    private JTextField[][] cellFields;
    private Boolean[][] boolArray; // array for cells needed to be changed
    private int startRow; // beginning row of the thread
    private int numOfRows; // number of rows thread needs to handle
    private int transitionIndex;


    //Constructor
    public BiteThread(Boolean[][] temp, JTextField[][] cellFields, Controller controller, int firstRow,
                      int numOfRows, int transitionIndex) {

        this.boolArray = temp;
        this.cellFields = cellFields;
        this.controller = controller;
        this.startRow = firstRow;
        this.numOfRows = numOfRows;
        this.transitionIndex = transitionIndex;
    } //end of BiteThread

    //This method retrieves the index for row transition
    public int getTransitionIndex() {
        return transitionIndex;
    } //end of getTransitionIndex

    public void run() {

        while (controller.isActivated()) {
            neighborCheck(startRow);
            controller.done(this);
            // set for every thread the transition index and increase every time thread is finished
            transitionIndex++;
        }

        //The thread is shutting down
        controller.done(this);
    }

    //this method verifies if a cell has at least one white neighbor
    public void neighborCheck(int startRow) {

        for (int row = startRow; row < numOfRows + startRow; row++) {
            for (int col = 0; col < cellFields[0].length; col++) {
                // The cell is already white
                if (cellFields[row][col].getBackground() == Color.WHITE) {
                    continue;
                }

                // check left neighbor
                if (col > 0 && cellFields[row][col - 1].getBackground() == Color.WHITE) {
                    boolArray[row][col] = true;

                }
                // check right neighbor
                else if (col + 1 < cellFields[0].length && cellFields[row][col + 1].getBackground() == Color.WHITE) {
                    boolArray[row][col] = true;

                }
                // check up neighbor
                else if (row + 1 < cellFields[0].length && cellFields[row + 1][col].getBackground() == Color.WHITE) {
                    boolArray[row][col] = true;

                }
                // check down neighbor
                else if (row > 0 && cellFields[row - 1][col].getBackground() == Color.WHITE) {
                    boolArray[row][col] = true;

                }

            }
        }

    } //end of neighborCheck

} //end of BiteThread
