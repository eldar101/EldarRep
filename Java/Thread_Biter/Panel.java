//This class implements the desired GUI for the task with panels
package q1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Panel extends JFrame implements ActionListener {

    private JButton goButton, clearButton;
    private JTextField cellField[][]; //array for the cells
    private int n; // Input size
    private int m; //  threads
    private int t; // transitions

    //This is the panel itself
    public Panel() {

        //
        this.setTitle("Pixel Biter");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);

        // panel of the picture
        n = userInput("Please enter the size of the desired matrix: ");
        JPanel cellPanel = new JPanel(new GridLayout(n, n));
        cellField = new JTextField[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cellField[i][j] = new JTextField(); //creating full panel
                cellField[i][j].setEditable(false);
                cellField[i][j].setBackground(Color.WHITE);
                cellField[i][j].setBorder(new LineBorder(Color.GRAY, 1));
                cellField[i][j].addMouseListener(new Listener());

                cellPanel.add(cellField[i][j]);
            }
        }
        this.add(cellPanel);

        // bottom panel
        JPanel bottomPanel = new JPanel();
        goButton = new JButton("Go");
        clearButton = new JButton("Clear");
        bottomPanel.add(goButton);
        bottomPanel.add(clearButton);
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.add(bottomPanel, BorderLayout.SOUTH);

        // add action listeners to buttons
        goButton.addActionListener(this);
        clearButton.addActionListener(this);

    } //end of Panel

    //This method validates the button press
    @Override
    public void actionPerformed(ActionEvent action) {

        if (action.getActionCommand().equals("Go")) {
            handleGoButton();

        } else if (action.getActionCommand().equals("Clear")) {
            clearPanel();
        }

    } //end of actionPerformed

    //Go button management
    public void handleGoButton() {

        if (checkIfToBite()) {

            m = userInput("Please enter the number of threads: ");
            t = userInput("Please enter the number of transitions: ");

            Controller controller = new Controller(cellField, m, t, goButton);
            // We avoid creating threads while running
            goButton.setEnabled(false);
            controller.start();
        } else {

            JOptionPane.showMessageDialog(null, "Nothing to Bite");
        }
    } //end of handleGoButton

    public boolean checkIfToBite() {
        // in case the board is white when  the button is pressed
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (cellField[i][j].getBackground() == Color.BLACK)
                    return true;
            }
        }
        return false;
    } //end of checkIfToBite

    //This method is in charge clearing the board by making all cells white
    public void clearPanel() {
        // We change all board to white
        for (int row = 0; row < n; ++row) {
            for (int col = 0; col < n; ++col) {
                cellField[row][col].setBackground(Color.WHITE);
            }
        }

    }//end of clearPanel

    // This method receives and validates user input
    private int userInput(String user_message) {

        String input = "";
        // verify positive number
        while (isPositive(input) == false) {
            input = JOptionPane.showInputDialog(this, user_message);
        }
        return Integer.valueOf(input);
    } //end of userInput

    //This method validates that the input is positive
    private boolean isPositive(String value) {
        // check if number if positive
        try {
            int parsed_int = Integer.parseInt(value);
            return parsed_int > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }//end of isPositive

    //This method just activates the panel's visibility
    public void showPanel() {
        this.setVisible(true);

    } //end of showPanel

    //This method is for changing color of the panel
    private class Listener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            JTextField current_cell = ((JTextField) e.getSource()); //receive current color
            if (current_cell.getBackground() == Color.WHITE) //change color
                current_cell.setBackground(Color.BLACK);
            else if (current_cell.getBackground() == Color.BLACK)
                current_cell.setBackground(Color.WHITE);

        }
    } // end of Listener

} //end of class Panel
