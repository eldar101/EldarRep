public class Card
{
    public String face; // face of card ("Ace", "Deuce", ...)
    public String suit; // suit of card ("Hearts", "Diamonds", ...)

    // two-argument constructor initializes card's face and suit
    public Card( String cardFace, String cardSuit )
    {
        this.face = cardFace; // initialize face of card
        this.suit = cardSuit; // initialize suit of card
    } // end two-argument Card constructor

    // return String representation of Card

    public int cardValue()
    {
        if (this.face.equals("Ace"))
            return 1;
        if (this.face.equals("Deuce"))
            return 2;
        if (this.face.equals("Three"))
            return 3;
        if (this.face.equals("Four"))
            return 4;
        if (this.face.equals("Five"))
            return 5;
        if (this.face.equals("Six"))
            return 6;
        if (this.face.equals("Seven"))
            return 7;
        if (this.face.equals("Eight"))
            return 8;
        if (this.face.equals("Nine"))
            return 9;
        if (this.face.equals("Ten"))
            return 10;
        if (this.face.equals("Jack"))
            return 11;
        if (this.face.equals("Queen"))
            return 12;
        if (this.face.equals("King"))
            return 13;
    return -2;
   }
    public String toString()
    {
        return face + " of " + suit;
    } // end method toString
} // end class Card