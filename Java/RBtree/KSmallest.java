/* Eldar Weiss 303169783
This program receives an array and a k integer and prints out the K-smallest elements.
The program receives an array from the user, a number k that is smaller than the array size and three stops in the array size
The program inserts the array's elements into an Red Black tree in such a way that only k elements are inserted.
When the program gets to insert the k+1th element, it removes the node with the maximal value and inserts the element instead.
Finally, the program will print said array's K-smallest elements by printing the tree;*/
/*-----------------------------------------------------------------------------------------------------------------------------------*/

import java.util.Scanner;
public class KSmallest {

    public static void main(String[] args)

    { /* variables */
        int k, n, n1, n2, n3;
        Utils u = new Utils();
        RedBlackTree rbt;   /* Creating object of RedBlack Tree */
        Scanner scan = new Scanner(System.in);
        System.out.println("Print K-smallest numbers in an array: ");


            /*  User input  */
        System.out.println("\nRed Black Tree Operations\n");

        System.out.println("Built-in test arrays for n=200, n=400 or n=800. ");
        System.out.println("Choose k = -1 to stop the program ");
        System.out.println("Please enter k: ");
        k = scan.nextInt();
        while (k != -1) {      /*  This generates arrays of various sizes,  and fills them with random numbers that range from 0 to 1023  */
            System.out.println("Please enter n: ");
            n = scan.nextInt();
            n1 = (n / 4);  /*  Requested index stops  */
            n2 = (n / 2);
            n3 = (int) (n * 0.75);
            rbt = new RedBlackTree();

            System.out.println("\t The algorithm is now tested on an array of random integers:\n");
            if (n == 200)   /*  Array is sized 200  */ {
                int[] A = new int[n];
                u.fillRandArr(A, 1023); /* fill with random  */
                u.printArr(A); /*  Print array for user to see */
                rbt.printKMin(A, k, n1, n2, n3);
            }/* print K-Smallest */ else if (n == 400) {/* Same for an array of 400 elements */
                int[] B = new int[n];
                u.fillRandArr(B, 1023);
                u.printArr(B);
                rbt.printKMin(B, k, n1, n2, n3);
            } else if (n == 800) {/* Same for an array of 800  */
                int[] C = new int[n];
                u.fillRandArr(C, 1023);
                u.printArr(C);
                rbt.printKMin(C, k, n1, n2, n3);
            }
            System.out.println("Number of Nodes = " + rbt.size());
            System.out.println("Please enter k: ");
            k = scan.nextInt();
        }
        if (k==-1)
            System.out.println("Goodbye");

    }  }

