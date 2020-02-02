package Mmn11_q1;
/*
Eldar Weiss 303169783
This class defines a card deck object
 */

import java.util.Random;
import java.util.ArrayList;

public class DeckOfCards {
    private int currentCard; // index of next Card to be dealt
    private final int NUMBER_OF_CARDS = 52; // constant number of Cards
    private Random randomNumbers; // random number generator
    protected ArrayList<Card> deck; // array of Card objects

    // constructor fills deck of Cards
    public DeckOfCards() {
        String[] faces = {"Ace", "Deuce", "Three", "Four", "Five", "Six",
                "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};

        deck = new ArrayList<>(); // create array of Card objects
        currentCard = 0; // set currentCard so first Card dealt is deck[ 0 ]
        randomNumbers = new Random(); // create random number generator

        // populate deck with Card objects
        for (int count = 0; count < NUMBER_OF_CARDS; count++)
            deck.add(count, new Card(faces[count % 13], suits[count / 13]));
    }


    // shuffle deck of Cards with one-pass algorithm
    public void shuffle() {
        // after shuffling, dealing should start at deck[ 0 ] again
        currentCard = 0; // reinitialize currentCard

        // for each Card, pick another random Card and swap them
        for (int first = 0; first < deck.size(); first++) {
            // select a random number between 0 and 51
            int second = randomNumbers.nextInt(NUMBER_OF_CARDS);
            // swap current Card with randomly selected Card
            Card temp = deck.get(first);
            deck.set(first, deck.get(second));
            deck.set(second, temp);
            // swap current Card with randomly selected Card
        } // end for
    } // end method shuffle


    //return the deck of the cards in a current game
    public ArrayList<Card> getGameDeck() {
        return deck;
    } //end method getGameDeck


} // end class DeckOfCards
