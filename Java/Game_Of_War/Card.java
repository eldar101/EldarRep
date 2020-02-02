package Mmn11_q1;

/*
Eldar Weiss 303169783
This class defines a card object
 */
public class Card {
    public String face; // face of card ("Ace", "Deuce", ...)
    public String suit; // suit of card ("Hearts", "Diamonds", ...)

    // two-argument constructor initializes card's face and suit
    public Card(String cardFace, String cardSuit) {
        this.face = cardFace; // initialize face of card
        this.suit = cardSuit; // initialize suit of card
    } // end two-argument Card constructor

    // return String representation of Card

    public int cardValue(Card card) //translate string into card's numerical value
    {
        if (card.face.equals("Deuce"))
            return 2;
        if (card.face.equals("Three"))
            return 3;
        if (card.face.equals("Four"))
            return 4;
        if (card.face.equals("Five"))
            return 5;
        if (card.face.equals("Six"))
            return 6;
        if (card.face.equals("Seven"))
            return 7;
        if (card.face.equals("Eight"))
            return 8;
        if (card.face.equals("Nine"))
            return 9;
        if (card.face.equals("Ten"))
            return 10;
        if (card.face.equals("Jack"))
            return 11;
        if (card.face.equals("Queen"))
            return 12;
        if (card.face.equals("King"))
            return 13;
        if (card.face.equals("Ace"))
            return 14;
        return -2;
    } //end method cardValue

    public String toString() {
        return face + " of " + suit;
    } // end method toString
} // end class Card