package Card_Game;


import Card_Game.Abilities.AbilRunScen;
import Card_Game.Abilities.Ability;
import Card_Game.CardContainers.Field;
import Card_Game.Cards.Card;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Game {

    Player[] players;
    Field[] board;
    Scanner scanner;

    public Game(Scanner scanner) throws IOException {
        JsonAccessor.fillMaps();
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

    public void run() {

        int currPlayer = 0;
        ArrayList<String> words;
        boolean endGame = false;
        do{

            boolean endTurn = false;
            boolean beginningTurn = true;
            Player player = players[currPlayer];
            player.setMana(Player.MAX_MANA);
            while (!endTurn){
                System.out.print(beginningTurn ? "Player " + (currPlayer + 1) + "'s Turn\n" : "");
                System.out.println(player.getMana() + " Mana Left");


                if (beginningTurn) {
                    printBoard(player, players[currPlayer + 1 >= players.length ? 0 : currPlayer + 1]);
                    ArrayList<Card> cardsDrew = (ArrayList<Card>) player.multiNoManaDraw(player.init_draw);
                    System.out.println("You drew:\n    " + StringUtils.join(cardsDrew.stream().map(Card::getName).collect(Collectors.toList()), ", "));
                    System.out.print("Your hand:\n");
                    printHand(player);
                    GameComponents
                            .getInstance()
                            .getSelfFieldCards(player)
                            .forEach(card -> card
                                    .getAbilitiesFromScene(AbilRunScen.TURNSTART)
                                    .forEach(Ability::run));
                }


                beginningTurn = false;

                words = Parser.parser();
                CommandParser commandParser = new CommandParser(words, player, this, currPlayer + 1, players);
                if (!words.isEmpty()) {
                    switch (commandParser.parseCmd()) {
                        case CommandParser.CONTINUE:
                            continue;
                        case CommandParser.END_TURN:
                            endTurn = true;
                            break;
                        case CommandParser.END_GAME:
                            endTurn = true;
                            endGame = true;
                            break;
                    }
                }
            }
            player.endTurn();
            GameComponents
                        .getInstance()
                        .getSelfFieldCards(player)
                        .forEach(card -> card
                                .getAbilitiesFromScene(AbilRunScen.TURNEND)
                                .forEach(Ability::run));
            currPlayer = currPlayer + 1 >= players.length ? 0 : currPlayer + 1;
        }while(!endGame);

    }

    public void printHand(Player player) {
        for (Card card : player.getHand()) {
            System.out.println("  " + card.getName());
        }
    }

    public void playCard(Player player, Card card, int num) {
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

    public void printBoard(Player current, Player opposite) {
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
        for (Card ignored : oppBack) {
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
        for (Card ignored : oppBack) {
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
        for (Card ignored : oppBack) {
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
        for (Card ignored : oppBack) {
            System.out.print("   |   |");
        }
        System.out.println();
        System.out.print(StringUtils.repeat("   -----", Player.FIELD_LEN));
        System.out.println();
        System.out.println();
    }

    public void printCard(Card card) {
        System.out.println(card.getName() + ":\n  " + card.getDescription());
    }
}
