package Card_Game;


import Card_Game.CardContainers.Field;
import Card_Game.Cards.Card;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Game {

    Player[] players;
    Field[] board;
    Scanner scanner;

    public Game(Scanner scanner) throws IOException {
        this.scanner = scanner;

        players = new Player[]{new Player("deck1"), new Player("deck1")};
        GameComponents.newInstance(players);
        board = new Field[]{players[0].getField(), players[1].getField()};
    }

    public static void main(String[] args) throws IOException {
        Game game = new Game(new Scanner(System.in));
        game.run();
    }

    public void run(){
        // Players set, hands set, board set
        int playerTurn = 0;
        String statementIn = "";
        while(!statementIn.equalsIgnoreCase("quit")) {
            while (players[playerTurn].getMana() > 0) {
                System.out.println("Player " + (playerTurn + 1) + "'s Turn" + "\nYour hand:");
                printHand(players[playerTurn]);
                int finalPlayerTurn = playerTurn;
                printBoard(players[playerTurn], Arrays.stream(players).filter(player -> players[finalPlayerTurn] != player).collect(Collectors.toList()).get(0));
                statementIn = scanner.nextLine();
                String word1 = null;
                String word2 = null;

                Scanner tokenizer = new Scanner(statementIn);
                if (tokenizer.hasNext()) {
                    word1 = tokenizer.next();
                    if (tokenizer.hasNext()) {
                        word2 = tokenizer.next();
                    }
                }
                if (word1 != null) {
                    switch (word1) {
                        case "play":
                            try {
                                Card cardPlayed = parseCardFromHand(players[playerTurn], word2);
                                playCard(players[playerTurn], cardPlayed);
                            }catch (IndexOutOfBoundsException e){
                                System.out.println("Card not found");
                            }
                            break;
                        case "draw":
                            players[playerTurn].draw();
                            break;
                        case "end":
                            players[playerTurn].endTurn();
                            break;
                        case "info":
                            try {
                                printCard(parseCardFromFieldOrHand(players[playerTurn], word2));
                            } catch (NullPointerException | IndexOutOfBoundsException e) {
                                System.out.println("Card not found");
                            }
                            break;
                        default:
                            // bad
                    }
                } else {
                    // bad
                }
            }
            players[playerTurn].setMana(Player.MAX_MANA);
            playerTurn = playerTurn + 1 >= players.length ? 0 : playerTurn + 1;
        }
    }

    private Card parseCardFromHand(Player player, String words) {
        return parseCard(player.getHand(), words);
    }

    private Card parseCardFromFieldOrHand(Player player, String words) {
        List<Card> collection = new ArrayList<>(player.getHand());
        collection.addAll(GameComponents.getInstance().getAllFieldCards());
        return parseCard(collection, words);
    }

    private Card parseCard(List<Card> collection, String words) {
        return collection
                .stream()
                .filter(card -> card.getName().equalsIgnoreCase(words))
                .collect(Collectors.toList())
                .get(0);
    }

    private void printHand(Player player) {
        for (Card card : player.getHand()) {
            System.out.println("  " + card.getName());
        }
    }

    private void playCard(Player player, Card card) {
        if(player.play(card)){
            System.out.println("Played card");
            printBoard(player, Arrays.stream(players).filter(p -> p != player).collect(Collectors.toList()).get(0));
        }else{
            System.out.println("Did not play");
        }
    }

    private void printBoard(Player current, Player opposite) {
        Field oppField = GameComponents.getInstance().getPlayerField(opposite);
        Field currField = GameComponents.getInstance().getPlayerField(current);
        Card[] oppBack = oppField.getBottomRow();
        Card[] oppFront = oppField.getMonsters();
        Card[] currFront = currField.getMonsters();
        Card[] currBack = currField.getBottomRow();
        List<Card> oppBackList = new ArrayList<>(Arrays.asList(oppBack));
        Collections.reverse(oppBackList);
        List<Card> oppFrontList = new ArrayList<>(Arrays.asList(oppFront));
        Collections.reverse(oppFrontList);
        System.out.print(StringUtils.repeat("   -----", oppFront.length));
        System.out.println();
        for (Card card : oppBackList) {
            if (card != null) System.out.print("   |" + card.getName().substring(0, 3) + "|"); else System.out.print("   |   |");
        }
        System.out.println();
        for (Card card : oppBack) {
            System.out.print("   |   |");
        }
        System.out.println();
        System.out.print(StringUtils.repeat("   -----", oppFront.length));
        System.out.println();
        System.out.println();
        System.out.print(StringUtils.repeat("   -----", oppFront.length));
        System.out.println();
        for (Card card : oppFrontList) {
            if (card != null) System.out.print("   |" + card.getName().substring(0, 3) + "|"); else System.out.print("   |   |");
        }
        System.out.println();
        for (Card card : oppBack) {
            System.out.print("   |   |");
        }
        System.out.println();
        System.out.print(StringUtils.repeat("   -----", oppFront.length));
        System.out.println();
        System.out.println();
        System.out.print(StringUtils.repeat("   -----", oppFront.length));
        System.out.println();
        for (Card card : currFront) {
            if (card != null) System.out.print("   |" + card.getName().substring(0, 3) + "|"); else System.out.print("   |   |");
        }
        System.out.println();
        for (Card card : oppBack) {
            System.out.print("   |   |");
        }
        System.out.println();
        System.out.print(StringUtils.repeat("   -----", oppFront.length));
        System.out.println();
        System.out.println();
        System.out.print(StringUtils.repeat("   -----", oppFront.length));
        System.out.println();
        for (Card card : currBack) {
            if (card != null) System.out.print("   |" + card.getName().substring(0, 3) + "|"); else System.out.print("   |   |");
        }
        System.out.println();
        for (Card card : oppBack) {
            System.out.print("   |   |");
        }
        System.out.println();
        System.out.print(StringUtils.repeat("   -----", oppFront.length));
        System.out.println();
        System.out.println();
    }

    private void printCard(Card card) {
        System.out.println(card.getName() + ":\n" + card.getDescription());
    }
}
