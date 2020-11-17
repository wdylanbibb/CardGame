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

    public ArrayList<String> parser(){
        Scanner scanner = new Scanner(System.in);
        String statementIn = scanner.nextLine();
        ArrayList<String> wordList = new ArrayList<>();
        Scanner tokenizer = new Scanner(statementIn);
        while(tokenizer.hasNext()){
            wordList.add(tokenizer.next());
        }
        return wordList;
    }

    public void run(){

        int currPlayer = 0;
        String in = "";
        do{
            boolean endTurn = false;
            boolean beginningTurn = true;
            players[currPlayer].setMana(Player.MAX_MANA);
            while (!endTurn){
                endTurn = false;
                if (beginningTurn) printBoard(players[currPlayer], players[currPlayer + 1 >= players.length ? 0 : currPlayer + 1]);
                System.out.print(beginningTurn ? "Player " + (currPlayer + 1) + "'s Turn\n" : "");
                System.out.println(players[currPlayer].getMana() + " Mana Left");
                System.out.print(beginningTurn ? "Your hand:\n" : "");
                if (beginningTurn) printHand(players[currPlayer]);
                beginningTurn = false;

                ArrayList<String> words = parser();
                if (words.get(0) != null){
                    switch (words.get(0)) {
                        case "play":
                            try {
                                Card cardPlayed = parseCardFromHand(players[currPlayer], words.get(1));
                                playCard(players[currPlayer], cardPlayed);
//                                printBoard(players[currPlayer], players[currPlayer + 1 >= players.length ? 0 : currPlayer + 1]);
                            } catch (IndexOutOfBoundsException e) {
                                System.out.println("Card not found");
                            }
                            break;
                        case "draw":
                            Card cardDrawn = players[currPlayer].draw();
                            System.out.print(cardDrawn != null ? "You drew " + cardDrawn.getName() + "\n" : "");
                            break;
                        case "end":
                            endTurn = true;
                            break;
                        case "info":
                            System.out.print("Player " + (currPlayer + 1) + "'s Turn\n");
                            System.out.println(players[currPlayer].getMana() + " Mana Left");
                            System.out.print("Your hand:\n");
                            printHand(players[currPlayer]);
                        default:
                            break;
                    }
                }
            }
            currPlayer = currPlayer + 1 >= players.length ? 0 : currPlayer + 1;
        }while(true);

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
            System.out.println("Played " + card.getName());
            printBoard(player, Arrays.stream(players).filter(p -> p != player).collect(Collectors.toList()).get(0));
        }else{
            System.out.println("Did not play; Not enough mana. (Costs " + card.getCost() + " out of " + player.getMana() + " mana)");
        }
    }

    private void printBoard(Player current, Player opposite) {
        Field oppField = (Field) GameComponents.getInstance().getPlayerContainer(opposite, Field.class);
        Field currField = (Field) GameComponents.getInstance().getPlayerContainer(current, Field.class);
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
