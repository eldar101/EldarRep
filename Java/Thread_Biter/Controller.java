//This class represents the controller for the threads
//Eldar Weiss 303169783
package q1;
//swing libraries

import javax.swing.*;
import java.awt.*;

public class Controller extends Thread {

    private BiteThread thread;
    private JTextField[][] cellFields;
    private Boolean[][] boolArray;
    private JButton goButton;
    private int num; // number of threads to wait for
    private int count; // number of threads done
    private int transitions; //number of transitions
    private int n; // picture size
    private int transitionIndex; //index of current transition
    private boolean activated; //checking if panel is activated

    //constructor
    public Controller(JTextField[][] threadBoard, int num, int t, JButton go) {

        this.activated = false;
        this.cellFields = threadBoard;
        this.n = cellFields[0].length;
        this.boolArray = new Boolean[n][n];
        this.num = num;
        this.transitions = t;
        this.transitionIndex = 0;
        this.goButton = go;

    } //end of Controller

    // This method is used to start or end threads
    public boolean isActivated() {
        return activated;
    } //end of isActivated

    //this method is when we are done with the thread
    public synchronized void done(BiteThread thread) {

        count++;
        if (count >= num) {
            this.notifyAll();
        }
        // we try to avoid waking thread before the next transition
        while (activated && transitionIndex == thread.getTransitionIndex()) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    } //end of done

    private synchronized void notifyThreads() {
        notifyAll();
    }

    public synchronized void biteImage(Boolean[][] temp) {

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (temp[i][j] != null && temp[i][j] == true)
                    cellFields[i][j].setBackground(Color.WHITE);
                temp[i][j] = false; // Prepare for the next transition
            }
        }

    }

    //This method is used for waiting on a thread to finish
    public synchronized void threadWait() {

        while (count < num) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted while thread is waiting");
            }
        }
    }

    //run implementation
    public void run() {

        activated = true;

        if (num > n) {
            num = n;
        }
        int rowsInThread = n / num; //dividing rows per thread

        for (int i = 0; i < num; i++) {
            int firstRow = i * rowsInThread;
            // last thread may need to handle more rows than other threads
            if (i == num - 1) {
                rowsInThread = n - i * rowsInThread;
            }
            // create and start threads
            thread = new BiteThread(boolArray, cellFields, this, firstRow, rowsInThread, transitionIndex); //initializing
            thread.start(); //starting the thread

        }

        for (int i = 0; i < transitions; i++) {
            threadWait();
            // Sleep
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            biteImage(boolArray);
            count = 0;
            transitionIndex++;
            // notify thread to continue
            notifyThreads();
        }

        // We terminate signal
        count = 0;
        activated = false;
        notifyThreads();
        threadWait();
        // All threads have stopped now
        goButton.setEnabled(true);

    } //end of run

}
