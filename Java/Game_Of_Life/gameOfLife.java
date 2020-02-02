package Mmn11_q2;// Eldar Weiss 303169783
// This class creates the game itself using the board

import javax.swing.*; //declaring imported API
import java.awt.*;
import java.util.Random;


public class gameOfLife {

    public static void main(String[] args) {
        play(); //calling the game
    }

    public static void play() {
        int reply, size = 10;
        int[][] alive = new int[size][size];

        Random rand = new Random();
        for (int i = 0; i < size; i++) //filling up a 2d matrix full of random 0's and 1's
        {
            for (int j = 0; j < size; j++) {
                int randLife = rand.nextInt(2); //random integer that is either 0 or 1 to simulate a game of life
                alive[i][j] = randLife; //filling the matrix full of alive/dead cells
            }
        }

        board game = new board(size, alive); //game object


        JOptionPane.showMessageDialog(null, "Welcome to the game of life!"); //game starts the matrix and panel
        setupGUI(game, size);


        reply = JOptionPane.showConfirmDialog(null, "Would you like to move on to the next cycle of life?");
        while (reply == 0)  //Menu offers you to continue you the game as long as you say "yes"
        {
            int[][] nextGen = new int[size][size]; //new board for the next life in the game
            repaint(game, size, alive, nextGen); //starting the next generation
            game.checkDeath(nextGen); //checking if the game has ended. we can see this when the matrix is full of zeroes

            reply = JOptionPane.showConfirmDialog(null, "Would you like to move on to the next cycle of life?");
        }
        if (reply == 1) {
            JOptionPane.showMessageDialog(null, "Ok, goodbye!");
            System.exit(0);
        }

    }

    //This method sets up the up the actual panel and frame
    public static void setupGUI(board board, int size) //setting up the GUI
    {
        JFrame frame = new JFrame();
        frame.setTitle("Game Of Life");
        frame.getContentPane().add(board);
        frame.setSize(53 * size, 55 * size);
        frame.setLocationRelativeTo(null);
        frame.setBackground(Color.GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }// end method setupGUI

    //this method updates the new board for the next generation of life
    public static void repaint(board game, int size, int[][] alive, int[][] newGeneration)
    {
        // Loop through every cell
        for (int l = 1; l < size - 1; l++) {
            for (int m = 1; m < size - 1; m++) {
                // finding no Of Neighbours that are alive
                int aliveNeighbours = 0;
                for (int i = -1; i <= 1; i++)
                    for (int j = -1; j <= 1; j++)
                        aliveNeighbours += alive[l + i][m + j];

                // The cell needs to be subtracted from
                // its neighbours as it was counted before
                aliveNeighbours -= alive[l][m];

                // Implementing the Rules of Life

                // Cell is lonely and dies
                if ((alive[l][m] == 1) && (aliveNeighbours < 2))
                    newGeneration[l][m] = 0;

                    // Cell dies due to over population
                else if ((alive[l][m] == 1) && (aliveNeighbours > 3))
                    newGeneration[l][m] = 0;

                    // A new cell is born
                else if ((alive[l][m] == 0) && (aliveNeighbours == 3))
                    newGeneration[l][m] = 1;

                    // Remains the same
                else
                    newGeneration[l][m] = alive[l][m];
            }
        }
        game.setAlive(newGeneration); //we created a new matrix with the new lives, now we set it
        setupGUI(game, size); //re drawing the panel
    }
}
