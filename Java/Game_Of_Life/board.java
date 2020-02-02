package Mmn11_q2;
// Eldar Weiss 303169783
// This file includes all necessary functions for a graphic based Game Of Life that is initially simulated on a 2d array

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.*;

public class board extends JPanel {
    private int size; //private variables
    private int[][] alive;
    private board board;


    public board(int size, int[][] alive) //constructor
    {
        this.size = size;
        this.alive = alive;
    } //end constructor board

    //This method sets the new matrix for the next generation of life when called
    public void setAlive(int[][] alive) //setting up the 2d matrix
    {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.alive[i][j] = alive[i][j];
            }
        }
    } //end of method setAlive

    public void checkDeath(int[][] alive) //checking if the matrix is all zeroes, meaning the game has ended
    {
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (alive[i][j] != 0)
                    count++; //we count only if there's life
            }
        }
        if (count == 0) //no life, therefore the game has ended after some turns
        {
            JOptionPane.showMessageDialog(null, "The game has ended!");
            System.exit(0);
        }
    } //end of method checkDeath

    //This creates the graphics based on the values in the 2d array
    public void paint(Graphics g) {
        for (int i = 10, row = 0; i <= 10 + 45 * size; i += 50, row++) {
            for (int j = 10, column = 0; j <= 10 + 45 * size; j += 50, column++) {
                if (this.alive[row][column] == 0) {
                    g.setColor(Color.WHITE); //fill in white when cells are '0", meaning dead
                }
                if (this.alive[row][column] == 1) {
                    g.setColor(Color.BLACK);  //fill in white when cells are '1", meaning alive
                }
                g.fillRect(i, j, 45, 45);
            }
        }
    } //end of method paint


}