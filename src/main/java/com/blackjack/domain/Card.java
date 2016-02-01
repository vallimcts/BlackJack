package blackjack.domain;

import java.util.ArrayList;

/**
 * This is the domain class that represents a card in a card game.
 */
public class Card {

    public static ArrayList<String> cards = new ArrayList<String>() {
        {

            add("2");
            add("3");
            add("4");
            add("5");
            add("6");
            add("7");
            add("8");
            add("9");
            add("10");
            add("J");
            add("Q");
            add("K");
            add("A");
        }
    };

    public static ArrayList<String> suits = new ArrayList<String>() {
        {
            add("Hearts");
            add("Diamonds");
            add("Clubs");
            add("Spades");
        }
    };
}