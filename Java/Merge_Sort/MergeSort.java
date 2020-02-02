//This class shows a parallel merge-sort for an input array
//Eldar Weiss 303169783

package q2;
import javax.swing.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class MergeSort implements Runnable {

    //array queue
    private static Queue<int[]> arraysQueue;

    //user input
    public static void main(String[] args) throws InterruptedException {
        int n = getNumFromUser("Please enter the array size (n): ");
        int m = getNumFromUser("Please enter the number of threads (m): ");

        long start = System.currentTimeMillis(); //for measuring time
        int[] arrayToSort = initializeArrayToSort(n); //input array initialization
        System.out.print("The original array: ");
        printArray(arrayToSort);
        int[] sortedArray = sortArray(arrayToSort, m); //the sorted array initialization
        System.out.println();
        System.out.print("The sorted array: ");
        printArray(sortedArray);
        long end = System.currentTimeMillis(); //end time
        System.out.println("\nThe time it took is: " + (end - start) + " milliseconds"); //printing out how long it took
    }

    /*This method distributes through threads*/
    private static int[] sortArray(int[] arrayToSort, int m) throws InterruptedException {
        arraysQueue = initializeArraysQueue(arrayToSort); //create array queue
        Thread[] threadArray = threadStart(m); //thread array start
        threadWait(threadArray); //wait
        return arraysQueue.poll(); // only element in queue is the array
    } //end of sortArray

    /*This method runs threads*/
    private static void threadWait(Thread[] threadArray) throws InterruptedException {
        for (int i = 0; i < threadArray.length; i++)
            threadArray[i].join();
    } //end of threadWait

    /*This method creates threads*/
    private static Thread[] threadStart(int m) {
        Thread[] threadArray = new Thread[m]; //initalization
        for (int i = 0; i < m; i++) { //run creation
            Thread t = new Thread(new MergeSort()); //merge in each thread
            threadArray[i] = t; //put thread in array
            t.start();
        }
        return threadArray;
    } //end of threadStart

    /*This method creates a new array*/
    private static Queue<int[]> initializeArraysQueue(int[] arrayToSort) {
        Queue<int[]> toReturn = new LinkedList<>();
        for (int i = 0; i < arrayToSort.length; i++)
            toReturn.add(new int[]{arrayToSort[i]});
        return toReturn;
    } //end of initializeArraysQueue

    /*This method prints parameter array's values*/
    private static void printArray(int[] array) {
        System.out.print("[");
        for (int i = 0; i < array.length - 1; i++) //run and print
            System.out.print(array[i] + ", ");
        System.out.print(array[array.length - 1] + "]");
    } //end of printArray

    /*This method aligns random values in array*/
    private static int[] initializeArrayToSort(int n) {
        int[] toReturn = new int[n]; //new array
        Random rand = new Random(); //randomize
        for (int i = 0; i < n; i++)
            toReturn[i] = rand.nextInt(100) + 1; // insert the elements in range 1..100
        return toReturn;
    } //end initializeArrayToSort

    //This method is for making sure  the input from the  user is an integer
    private static int getNumFromUser(String inputString) {
        boolean inputRepeat = true;
        int input = -1;
        while (inputRepeat) {
            try {
                input = Integer.parseInt(JOptionPane.showInputDialog(inputString)); //if not integer
                if ((input > 0))
                    inputRepeat = false;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please Enter an integer");
            }

        }
        return input;
    } //end of getNumFromUser


    //This method is necessary for running the threads  and sorting
    public void run() {
        boolean thereIsOnlyOneSortedArray = false;
        while (!thereIsOnlyOneSortedArray) {
            int[] arr1; //two arrays initialized
            int[] arr2;
            synchronized (arraysQueue) {
                arr1 = arraysQueue.poll(); //synchronize threads
                arr2 = arraysQueue.poll();
            }
            if (arr1 == null) {
                thereIsOnlyOneSortedArray = true;
                continue;
            } else if (arr2 == null) { //add only first array if the second is empty
                thereIsOnlyOneSortedArray = true;
                synchronized (arraysQueue) {
                    arraysQueue.add(arr1);
                }
                continue;
            }
            int[] mergedArray = MergeSort(arr1, arr2); //merge the arrays
            synchronized (arraysQueue) { //run the threads synchronized
                arraysQueue.add(mergedArray);
            }
        }
    } //end of run

    /*merge the arrays*/
    private int[] MergeSort(int[] arr1, int[] arr2) {
        int[] sortedArray = new int[arr1.length + arr2.length]; //new final array
        int i1 = 0, i2 = 0;

        for (int j = 0; j < sortedArray.length; j++) { //classic merge-sort
            if (i1 < arr1.length) {
                if (i2 < arr2.length) {
                    if (arr1[i1] < arr2[i2]) {
                        sortedArray[j] = arr1[i1];
                        i1++;
                    } else {
                        sortedArray[j] = arr2[i2];
                        i2++;
                    }
                } else { // the second array ends, but the first array still has elements
                    sortedArray[j] = arr1[i1];
                    i1++;
                }
            } else {
                // first array ends, but the second array still has elements
                sortedArray[j] = arr2[i2];
                i2++;
            }
        }
        return sortedArray;
    } // end of MergeSort


}// end of class