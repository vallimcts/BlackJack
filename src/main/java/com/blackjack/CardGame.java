package blackjack;


import blackjack.domain.Card;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Class that handles the playing of a card game from a simple command line interface,
 * and echoes back a step-by-step description of the game to the console.
 */
@SuppressWarnings("unchecked")
public class CardGame {

    public static int playerCount = 0;

    public static ArrayList<String> drawnCards = new ArrayList<String>();

    private static Random random = new Random();

    private static LinkedHashMap<String, Integer> playersAndValue = new LinkedHashMap<String, Integer>();

    /**
     * BlackJack for the number of players in the input,
     * The game finishes if:
     * All players "stick" in a round.
     * Any player hits 21 exactly.
     * There is only one player is left in the game because all others have "gone bust".
     *
     * @param args
     */
    public static void main(String[] args) {

        if (args == null || args.length == 0) {
            System.out.println("No Player Count Provided, Cannot proceed with the game");
            System.exit(0);
        } else {
            playerCount = Integer.parseInt(args[0]);
            if (playerCount < 2 || playerCount > 6) {
                System.out.println("Invalid Player Count Provided, Cannot proceed with the game, Number of players should be from 2 to 6");
                System.exit(0);
            }
        }

        System.out.println("Welcome to Black Jack of " + playerCount + " players");

        for (int i = 1; i <= playerCount; i++) {
            playersAndValue.put("Player " + i, 0); // Create Players and Values
        }

        boolean isGameOver = drawInitialCardsForPlayers(); // get two cards for players

        if (!isGameOver) {
            System.out.println("Nobody had a Black Jack, So continue drawing more cards");
            drawMoreCardsAndCheckForWinner(); // drawing more cards
        }

    }

    /**
     * Draw more cards and check for winner
     *
     * @return
     */
    public static boolean drawMoreCardsAndCheckForWinner() {

        int possibleNumberOfCards = 104 - (playerCount * 2);

        while (drawnCards.size() <= possibleNumberOfCards) {

            int stayed = 0;
            int busted = 0;
            int blackJack = 0;

            for (Map.Entry<String, Integer> entry : playersAndValue.entrySet()) {
                String player = entry.getKey();
                int total = entry.getValue();

                if (total < 17) {
                    // drawing card for player since less than 17
                    String suitCard = getCardFromDeck(player);
                    int card = getCardFromSuiteCard(suitCard);
                    total = entry.getValue() + card;
                    playersAndValue.put(entry.getKey(), total);
                }

                if (total > 21) { // player busted
                    busted++;
                    System.out.println(player + " Busted");
                } else if (total == 21) { // player got a blackjack
                    blackJack++;
                    System.out.println("Game Over! " + player + " got 21");
                    break;
                } else if (total == 17 || total > 17) { //player sticking
                    stayed++;
                    System.out.println(player + " Sticking");
                }

            }

            for (Map.Entry<String, Integer> entry : playersAndValue.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }

            if (blackJack > 0) {
                return true;
            } else if (busted == playerCount - 1) { // one winner since all others are busted
                for (Map.Entry<String, Integer> entry : playersAndValue.entrySet()) {
                    String player = entry.getKey();
                    int total = entry.getValue();
                    if (total < 21) {
                        System.out.println("Game Over! " + player + " Won, Since other Players are busted");
                        return true;
                    }
                }
                return true;
            } else if (busted == playerCount) { // all players are busted
                System.out.println("Game Over ! All Players Busted");
                return true;
            } else if (stayed == playerCount || stayed + busted == playerCount) {

                // All players are Sticked orAll Players Sticked And Busted
                if (stayed == playerCount) {
                    System.out.println("All Players Sticked");
                } else {
                    System.out.println("All Players Sticked And Busted");
                }
                int highest = 0;
                ArrayList<String> winners = new ArrayList();

                for (Map.Entry<String, Integer> entry : playersAndValue.entrySet()) {
                    int total = entry.getValue();
                    if (total < 21) { // identifying highest count amount staying
                        if (highest < total) {
                            highest = total;
                        }
                    }
                }

                for (Map.Entry<String, Integer> entry : playersAndValue.entrySet()) {
                    if (entry.getValue() == highest) { // identifying winner
                        winners.add(entry.getKey());
                    }
                }

                for (String winner : winners) {
                    System.out.println("Game Over ! " + winner + " Won");
                }
                return true;
            } else {
                System.out.println("Drawing more Cards from deck");
            }

        }

        return false;
    }

    /**
     * Get Initial 2 cards for all the players
     *
     * @return
     */
    public static boolean drawInitialCardsForPlayers() {

        boolean blackJack = false;

        System.out.println("Drawing 2 cards for each player");

        for (int i = 1; i <= 2; i++) {
            //get 2 cards for each player
            for (Map.Entry<String, Integer> entry : playersAndValue.entrySet()) {
                String key = entry.getKey();
                String suitCard = getCardFromDeck(key);
                int card = getCardFromSuiteCard(suitCard);
                playersAndValue.put(entry.getKey(), entry.getValue() + card);
            }
        }

        for (Map.Entry<String, Integer> entry : playersAndValue.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        for (Map.Entry<String, Integer> entry : playersAndValue.entrySet()) {
            if (entry.getValue() == 21) {
                // player had a blackjack
                blackJack = true;
                System.out.println("Game Over! " + entry.getKey() + " got a Black Jack");
            }
        }
        return blackJack;

    }

    /**
     * Get Card from Deck
     *
     * @param key
     * @return
     */
    public static String getCardFromDeck(String key) {

        String suitCard;

        while (true) {
            suitCard = getRandomCardSuite(key);
            if (isValidCard(suitCard)) { // check if the card exists twice already, if so get card again
                drawnCards.add(suitCard);
                break;
            }
        }
        return suitCard;
    }

    /**
     * Get the value of the card
     *
     * @param suitCard
     * @return
     */
    private static int getCardFromSuiteCard(String suitCard) {
        String card;
        if (suitCard.contains("10") || suitCard.contains("11")) {
            card = suitCard.substring(0, 2); // 11Diamonds will return 11
        } else {
            card = suitCard.substring(0, 1); // 2Spades will return 2
        }

        return Integer.parseInt(card);
    }

    public static String getRandomCardSuite(String key) {
        int suitIndex = random.nextInt(Card.suits.size());
        String suit = Card.suits.get(suitIndex); //Hearts or Diamonds or Clubs etc
        int cardIndex = random.nextInt(Card.cards.size());
        String card = Card.cards.get(cardIndex); //  2 or 7 or A or J etc
        System.out.println(key + " gets card " + card + " " + suit);
        int cardValue;
        try {
            cardValue = Integer.parseInt(card); // 2 or 7 or Exception if A,J,Q,K
        } catch (NumberFormatException e) {
            if (card.equals("A")) {
                cardValue = 11; // if A
            } else {
                cardValue = 10; // J, Q or K
            }
        }
        return cardValue + suit; // 10 Hearts or 2 Diamonds etc
    }

    /**
     * check if the card exists more than once
     *
     * @param suitCard
     * @return
     */
    public static boolean isValidCard(String suitCard) {

        int drawnCount = 0;
        for (String it : drawnCards) { // check the count of the same card in drawn cards
            if (it == suitCard) {
                drawnCount++;
            }
        }

        if (drawnCount < 2) {
            return true; // card already drawn twice
        } else {
            return false; // card not drawn twice
        }
    }


}