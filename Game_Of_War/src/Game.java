import java.util.Scanner;
import javax.swing.JOptionPane;


public class Game {
    String p1, p2;
    int p1DeckSize = 0, p2DeckSize = 0;
    DeckOfCards deck, deckP1, deckP2;

    public Game() {

        Scanner input = new Scanner(System.in);
        deck = new DeckOfCards(); // crate a new deck
        deck.shuffle(); // Shuffle the deck
        deckP1 = new DeckOfCards();
        deckP1.clearDeck();
        deckP2 = new DeckOfCards();
        deckP2.clearDeck();
        for (int i = 0; i < 26; i++) {
            deckP1.addICard(i,deck.getCard(i));
        }
        for (int i = 0; i < 26; i++) {
            deckP2.addICard(i,deck.getCard(i+26));
        }
        deck.clearDeck();
        JOptionPane.showMessageDialog(null, "Welcome to a game of \"War\"!");
        this.p1 = JOptionPane.showInputDialog(null, "Please enter player 1's name:");
        this.p2 = JOptionPane.showInputDialog(null, "Please enter player 2's name:");
        JOptionPane.showMessageDialog(null, this.p1 + " is player 1 \n" + this.p2 + " is player 2");

        gamePlay();
    }

    public void gamePlay() {
        int turn = 1, i = 0;
        //int indexP1 = 0, indexP2 = 0;
        while (deckP1.deckSize() != 0 && deckP2.deckSize() != 0) {
            JOptionPane.showMessageDialog(null, "Turn number " + turn++);
            JOptionPane.showMessageDialog(null, p1 + " has " + deckP1.getCard(i).toString() + "\n" + p2 + " has " + deckP2.getCard(i).toString());
            if (deckP1.getCard(i).cardValue() > deckP2.getCard(i).cardValue()) {
                JOptionPane.showMessageDialog(null, p1 + " wins this turn!");
                turnWin(deckP1, deckP2, i);
                JOptionPane.showMessageDialog(null, p1 + " : " +deckP1.deckSize() +" " + p2+ " : " +deckP2.deckSize()  );
            } else if (deckP1.getCard(i).cardValue() < deckP2.getCard(i).cardValue()) {
                JOptionPane.showMessageDialog(null, p2 + " wins this turn!");
                turnWin(deckP2, deckP1, i);
                JOptionPane.showMessageDialog(null, p1 + " : " +deckP1.deckSize() +" " + p2+ " : " +deckP2.deckSize()  );
            } else {
                JOptionPane.showMessageDialog(null, "The cards are equal! time for war!");
                JOptionPane.showMessageDialog(null, p1 + " : " +deckP1.deckSize() +" " + p2+ " : " +deckP2.deckSize()  );
                JOptionPane.showMessageDialog(null, p1 + "'s third card is " + deckP1.getCard(i+3) + "\n" + p2  + "'s third card is " + deckP2.getCard(i+3));
                warStage(deckP1, deckP2, i);
                i++;
                JOptionPane.showMessageDialog(null, p1 + " : " +deckP1.deckSize() +" " + p2+ " : " +deckP2.deckSize()  );

            }
            if (deckP1.deckSize() == 0)
                JOptionPane.showMessageDialog(null, p1 + " Lost the game!" + p2 + " is the winner!");
            else if (deckP2.deckSize() == 0)
                JOptionPane.showMessageDialog(null, p2 + " Lost the game!" + p1 + " is the winner!");
        }
    }


    public void turnWin(DeckOfCards d1, DeckOfCards d2, int i) {
        Card temp1 = new Card(d1.getCard(i).face, d1.getCard(i).suit);
        Card temp2 = new Card(d2.getCard(i).face, d2.getCard(i).suit);
        d1.removeICard(i);
        d2.removeICard(i);
        d1.addCard(temp1);
        d1.addCard(temp2);
    }  //end method turnWin

    public void warStage(DeckOfCards d1, DeckOfCards d2, int i) {
        Card temp1 = new Card(d1.getCard(i + 3).face, d1.getCard(i + 3).suit);
        Card temp2 = new Card(d2.getCard(i + 3).face, d2.getCard(i + 3).suit);
        if (temp1.cardValue() > temp2.cardValue())
        {
            for (int j = 0; j < 3; j++){
                turnWin(d1, d2,i+1);}

        } else if (temp1.cardValue() < temp2.cardValue()) {
            for (int j = 0; j < 3; j++){
                turnWin(d2, d1, i+1);}
        }
        else
        {
            warStage(d1,d2,i+3);
        }
    }  //end method warStage
}





