import java.util.Random;
import java.util.ArrayList;
public class DeckOfCards
{
    private int currentCard; // index of next Card to be dealt
    private int NUMBER_OF_CARDS = 52; // constant number of Cards
    private Random randomNumbers; // random number generator
    protected  ArrayList<Card> deck = new ArrayList<Card>(NUMBER_OF_CARDS); // array of Card objects

    // constructor fills deck of Cards
    public DeckOfCards()
{
    String[] faces = { "Ace", "Deuce", "Three", "Four", "Five", "Six",
            "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King" };
    String[] suits = { "Hearts", "Diamonds", "Clubs", "Spades" };

    // ArrayList<Card> deck = new ArrayList<>(NUMBER_OF_CARDS); // create array of Card objects
    currentCard = 0; // set currentCard so first Card dealt is deck[ 0 ]
    randomNumbers = new Random(); // create random number generator

    // populate deck with Card objects
    for (int count = 0; count < NUMBER_OF_CARDS; count++)
        deck.add(count,new Card(faces[count % 13], suits[count / 13]));
}


    public void emptyDeckOfCards()
    {
       this.deck.clear();
       this.NUMBER_OF_CARDS = 0;
    }

    // end emptyDeckOfCards


    // shuffle deck of Cards with one-pass algorithm
    public void shuffle()
    {
        // after shuffling, dealing should start at deck[ 0 ] again
        currentCard = 0; // reinitialize currentCard

        // for each Card, pick another random Card and swap them
        for ( int first = 0; first < NUMBER_OF_CARDS; first++ )
        {
            // select a random number between 0 and 51
            int second =  randomNumbers.nextInt( NUMBER_OF_CARDS);
            // swap current Card with randomly selected Card
            Card temp = deck.get(first);
            deck.set(first,deck.get(second));
            deck.set(second,temp);
            // swap current Card with randomly selected Card
        } // end for
    } // end method shuffle

    // deal one Card
    public Card dealCard()
    {

        // determine whether Cards remain to be dealt
        if ( currentCard < NUMBER_OF_CARDS )
            return this.deck.get(currentCard++); // return current Card in arrayList
        else
            return null; // return null to indicate that all Cards were dealt
    }// end method dealCard


    public Card getCard(int i)
    {
        return this.deck.get(i);
    }// end method getCard

    //clear the deck of cards
    public void clearDeck()
    {
     this.deck.clear();
    }  //end method clearDeck

    //Get a card's index
    public Card showTopCard ()
    {
         return this.deck.get(currentCard);
    }  //end method clearDeck

    //add new card
    public void addCard (Card newCard)
    {
        if (this.deck.contains(newCard))
        {
            System.out.println("This card is already in the deck!");
        }
        else
        {
        this.deck.add(newCard);
        }
    }  //end method addCard

    //add new card by Index
    public void addICard (int i, Card newCard)
    {
        if (this.deck.contains(newCard))
        {
            System.out.println("This card is already in the deck!");
        }
        else
        {
            this.deck.add(i, newCard);
        }
    }  //end method addICard

    public void removeCard (Card newCard)
    {
        if (this.deck.contains(newCard))
       this.deck.remove(newCard);

    }  //end method removeCard

    public void removeICard (int i)
    {
        if (this.deck.contains(this.deck.get(i)))
            this.deck.remove(i);

    }  //end method removeICard

    public int deckSize ()
    {
       return this.deck.size();
    }  //end method addCard


} // end class DeckOfCards
