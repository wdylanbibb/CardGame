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

        List<String> deckNames = JsonAccessor.pickDecks();
        players = new Player[deckNames.size()];
        for (int i = 0; i < deckNames.size(); i++) {
            players[i] = new Player(deckNames.get(i));
        }
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
                if (!words.isEmpty()){
                    switch (words.get(0)) {
                        case "play":
                                try {
                                    int num = Integer.parseInt(words.get(words.size() - 1)) - 1;
                                    try {
                                        Card cardPlayed = parseCardFromHand(players[currPlayer], words.subList(1, words.size() - 1));
                                        playCard(players[currPlayer], cardPlayed, num);
                                    } catch (IndexOutOfBoundsException e) {
                                        System.out.println("Card not found");
                                    }
                                } catch (NumberFormatException exc) {
                                    try {
                                        parseCardFromHand(players[currPlayer], words.subList(1, words.size()));
                                        System.out.println("Please input the name of a card and a number space to play it in on the field.");
                                    } catch (IndexOutOfBoundsException e) {
                                        System.out.println("Card not found");
                                    }
                                }
//                                printBoard(players[currPlayer], players[currPlayer + 1 >= players.length ? 0 : currPlayer + 1]);
                            break;
                        case "draw":
                            Card cardDrawn = players[currPlayer].draw();
                            System.out.print(cardDrawn != null ? "You drew " + cardDrawn.getName() + "\n" : "");
                            break;
                        case "end":
                            endTurn = true;
                            break;
                        case "info":
                            if(words.size() > 1){
                                printCard(parseCardFromFieldOrHand(players[currPlayer], words.subList(1, words.size())));
                            }else {
                                System.out.print("Player " + (currPlayer + 1) + "'s Turn\n");
                                System.out.println(players[currPlayer].getMana() + " Mana Left");
                                System.out.print("Your hand:\n");
                                printHand(players[currPlayer]);
                            }
                        default:
                            break;
                    }
                }
            }
            currPlayer = currPlayer + 1 >= players.length ? 0 : currPlayer + 1;
        }while(true);

    }

    private Card parseCardFromHand(Player player, List<String> words) {
        String word = StringUtils.join(words, " ");
        return parseCard(player.getHand(), word);
    }

    private Card parseCardFromFieldOrHand(Player player, List<String> words) {
        List<Card> collection = new ArrayList<>(player.getHand());
        collection.addAll(GameComponents.getInstance().getAllFieldCards());
        String word = StringUtils.join(words, " ");
        return parseCard(collection, word);
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

    private void playCard(Player player, Card card, int num) {
        if (num <= Player.FIELD_LEN) {
            if(player.play(card, num)){
                System.out.println("Played " + card.getName());
                printBoard(player, Arrays.stream(players).filter(p -> p != player).collect(Collectors.toList()).get(0));
            }else{
                System.out.println("Did not play; Not enough mana. (Costs " + card.getCost() + " out of " + player.getMana() + " mana)");
            }
        } else {
            System.out.println("Please use a number from 1 to " + Player.FIELD_LEN + " to designate a space on the board.");
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
        System.out.print(StringUtils.repeat("   -----", Player.FIELD_LEN));
        System.out.println();
        for (Card card : oppBackList) {
            if (card != null) System.out.print("   |" + card.getName().substring(0, 3) + "|"); else System.out.print("   |   |");
        }
        System.out.println();
        for (Card card : oppBack) {
            System.out.print("   |   |");
        }
        System.out.println();
        System.out.print(StringUtils.repeat("   -----", Player.FIELD_LEN));
        System.out.println();
        System.out.println();
        System.out.print(StringUtils.repeat("   -----", Player.FIELD_LEN));
        System.out.println();
        for (Card card : oppFrontList) {
            if (card != null) System.out.print("   |" + card.getName().substring(0, 3) + "|"); else System.out.print("   |   |");
        }
        System.out.println();
        for (Card card : oppBack) {
            System.out.print("   |   |");
        }
        System.out.println();
        System.out.print(StringUtils.repeat("   -----", Player.FIELD_LEN));
        System.out.println();
        System.out.println();
        System.out.print(StringUtils.repeat("   -----", Player.FIELD_LEN));
        System.out.println();
        for (int i = 0; i < currFront.length; i++) {
            Card card = currFront[i];
            if (card != null) System.out.print("   |" + card.getName().substring(0, 3) + "|"); else System.out.print("   | " + (i + 1) +" |");
        }
        System.out.println();
        for (Card card : oppBack) {
            System.out.print("   |   |");
        }
        System.out.println();
        System.out.print(StringUtils.repeat("   -----", Player.FIELD_LEN));
        System.out.println();
        System.out.println();
        System.out.print(StringUtils.repeat("   -----", Player.FIELD_LEN));
        System.out.println();
        for (int i = 0; i < currBack.length; i++) {
            Card card = currBack[i];
            if (card != null) System.out.print("   |" + card.getName().substring(0, 3) + "|"); else System.out.print("   | " + (i + 1) +" |");
        }
        System.out.println();
        for (Card card : oppBack) {
            System.out.print("   |   |");
        }
        System.out.println();
        System.out.print(StringUtils.repeat("   -----", Player.FIELD_LEN));
        System.out.println();
        System.out.println();
    }

    private void printCard(Card card) {
        System.out.println(card.getName() + ":\n  " + card.getDescription());
    }
}
