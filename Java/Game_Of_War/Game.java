package Mmn11_q1;

import java.util.ArrayList;
import javax.swing.JOptionPane;
/*
Eldar Weiss
This class is a simulated version of the card game "War"
*/

public class Game {
    public static void main(String[] args) {
        play();
    }

    public static void play() {
        DeckOfCards war = new DeckOfCards(); //new deck of cards for the game to begin
        war.shuffle(); //shuffling the deck

        //Setting up the players and their decks
        String player1, player2; //player names
        ArrayList<Card> p1 = new ArrayList();
        ArrayList<Card> p2 = new ArrayList();
        Card tempCard = new Card(null, null);
        //This index indicates the card inside the card deck
        int index = 0;
        //boolean for us to check if both players have the same card
        boolean warTime = false;

        //we share the deck between 2 players
        for (int i = 0; i < war.getGameDeck().size(); i += 2) {
            p1.add(war.getGameDeck().get(i));
            p2.add(war.getGameDeck().get(i + 1));
        }

        //Game start up menu to set up players
        JOptionPane.showMessageDialog(null, "Welcome to a game of \"War\"!");
        player1 = JOptionPane.showInputDialog(null, "Please enter player 1's name:");
        player2 = JOptionPane.showInputDialog(null, "Please enter player 2's name:");
        JOptionPane.showMessageDialog(null, player1 + " is player 1 \n" + player2 + " is player 2");

        int turn = 1; //turn counter
        //The game lasts until one deck is empty
        while (!p1.isEmpty() && !p1.isEmpty()) {

            //condition of winning the turn
            if (index < p1.size() && index < p1.size()) {
                JOptionPane.showMessageDialog(null, "Round " + turn++ + "\n" + player1 + " has " + p1.get(index) + '\n' + player2 + " has " + p2.get(index));
                //compare values
                if (tempCard.cardValue(p1.get(index)) > tempCard.cardValue(p2.get(index))) {

                    JOptionPane.showMessageDialog(null, player1 + " Wins This Round!");
                    turnEnd(p1, p2, index); //move cards to the winning deck
                    JOptionPane.showMessageDialog(null, player1 + " : " + p1.size() + " " + player2 + " : " + p2.size()); //show current decks
                    index = 0;
                    if (warTime && p2.size() >= 3) { //Start war status when conditions are met
                        warEnd(p1, p2, index);
                        //set flag to false so it won't repeat
                        warTime = false;
                    }
                    //same thing but in case player2 wins the round
                } else if (tempCard.cardValue(p1.get(index)) < tempCard.cardValue(p2.get(index))) {
                    JOptionPane.showMessageDialog(null, player2 + " Wins This Round!");
                    turnEnd(p2, p1, index);
                    JOptionPane.showMessageDialog(null, player1 + " : " + p1.size() + " " + player2 + " : " + p2.size());
                    index = 0;
                    if (warTime && p1.size() >= 3) {
                        warEnd(p2, p1, index);
                        warTime = false;
                    }
                }
                //equal cards
                else {
                    int j = 0;
                    warTime = true;
                    //play 'war' - deal 3 cards
                    while (j < 3) {
                        //player1 lost - less than 3 cards to play
                        if (p1.size() < 3) {
                            JOptionPane.showMessageDialog(null, player2 + ", You Are The WINNER of The Game!" + '\n' + player1 + " is out of cards");
                            return;
                        }
                        //player2 lost - less than 3 cards to play
                        if (p2.size() < 3) {
                            JOptionPane.showMessageDialog(null, player1 + ", You Are The WINNER of The Game!" + '\n' + player2 + " is out of cards");
                            return;
                        }
                        //play with next card in the deck: index + j
                        j++;
                        JOptionPane.showMessageDialog(null, "The cards are of equal value! time for war!" + '\n' + player1 + " has " + p1.get(index + j) + '\n' + player2 + " has " + p2.get(index + j));

                    }
                    //change index in order to add the last 3 cards of losing player to the other (at the start of the loop)
                    index += j;
                }
            } else index = 0;

        }
//winning announcement
        if (p1.isEmpty())
            JOptionPane.showMessageDialog(null, "<html><span style='color:blue'>" + player2 + ", You Are The winner!</span> </html>");
        else
            JOptionPane.showMessageDialog(null, "<html><span style='color:blue'>" + player1 + ", You Are The winner!</span> </html>");
    } //end of main

    //this method moves the cards to the winner's deck
    public static void turnEnd(ArrayList p1, ArrayList p2, int index) {
        p1.add(p2.get(index));
        p1.add(p1.get(index));
        //remove card from player2 and remove card from player1 (it was already added to his deck)
        p2.remove(index);
        p1.remove(index);
    } //end method turnEnd

    //this method moves the cards to the winner's deck after a state of war
    public static void warEnd(ArrayList p1, ArrayList p2, int index) {
        p1.add(p2.get(index));
        p2.remove(index);
        p1.add(p2.get(index));
        p2.remove(index);
    }//end method warEmd
}//end of class Game
