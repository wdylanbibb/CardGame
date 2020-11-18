package Card_Game;


import Card_Game.Abilities.AbilRunScen;
import Card_Game.Abilities.Ability;
import Card_Game.CardContainers.Field;
import Card_Game.Cards.Card;
import Card_Game.Cards.CardTypes.Monster.Monster;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Game {

    Player[] players;
    Field[] board;
    Scanner scanner;

    public Game(Scanner scanner) throws IOException {
        JsonAccessor.fillAbils();
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

    public void run() throws IOException {

        int currPlayer = 0;
        ArrayList<String> words = new ArrayList<>();
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
                if (!words.isEmpty()){
                    switch (words.get(0)) {
                        case "play":
                            try {
                                int num = Integer.parseInt(words.get(words.size() - 1)) - 1;
                                try {
                                    Card cardPlayed = Parser.parseCardFromHand(player, words.subList(1, words.size() - 1));
                                    if (GameComponents.getInstance().canPlay(cardPlayed)) {
                                        playCard(player, cardPlayed, num);
                                        cardPlayed.getAbilitiesFromScene(AbilRunScen.PLAY).forEach(Ability::run);
                                    }
                                } catch (IndexOutOfBoundsException e) {
                                    System.out.println("Card not found");
                                }
                            } catch (NumberFormatException exc) {
                                try {
                                    Parser.parseCardFromHand(player, words.subList(1, words.size()));
                                    System.out.println("Please input the name of a card and a number space to play it in on the field.");
                                } catch (IndexOutOfBoundsException e) {
                                    System.out.println("Card not found");
                                }
                            }
                            break;
                        case "draw":
                            Card cardDrawn = player.draw();
                            System.out.print(cardDrawn != null ? "You drew " + cardDrawn.getName() + "\n" : "");
                            break;
                        case "end":
                        case "quit":
                            endTurn = true;
                            break;
                        case "info":
                            if(words.size() > 1){
                                printCard(Parser.parseCardFromFieldOrHand(player, words.subList(1, words.size())));
                            }else {
                                System.out.print("Player " + (currPlayer + 1) + "'s Turn\n");
                                System.out.println(player.getMana() + " Mana Left");
                                System.out.print("Your hand:\n");
                                printHand(player);
                            }
                        case "attack":
                            if(words.size() > 1){
                                if(words.get(1).equalsIgnoreCase("help")){
                                    System.out.println("'attack <friendly lane> <enemy lane>");
                                }else{
                                    if(words.size() == 3){
                                        if(StringUtils.isNumeric(words.get(1)) && StringUtils.isNumeric(words.get(2))){
                                            try{
                                                Monster monster = player.getField().getMonsters()[Integer.parseInt(words.get(1))-1];
                                                Monster target = players[currPlayer + 1 >= players.length ? 0 : currPlayer + 1].getField().getMonsters()[5-Integer.parseInt(words.get(2))];
                                                if(monster != null && target != null){
                                                    boolean killed = monster.attack(target);
                                                    System.out.println(monster.getName() + " attacked " + target.getName() + " for " + monster.getAtk() + " damage!");
                                                    if(killed) {
                                                        System.out.println(target.getName() + " has died!");
                                                        target.getAbilitiesFromScene(AbilRunScen.DEATH).forEach(Ability::run);
                                                    }
                                                    players[currPlayer + 1 >= players.length ? 0 : currPlayer + 1].checkForDead();
                                                    printBoard(player, players[currPlayer + 1 >= players.length ? 0 : currPlayer + 1]);
                                                }else{
                                                    System.out.println("No Monster in Lane. Command: 'attack <friendly lane> <enemy lane>");
                                                }

                                            }catch (IndexOutOfBoundsException e){
                                                System.out.println("Lane out of bounds. Command: 'attack <friendly lane> <enemy lane>");
                                            }
                                        }else{
                                            System.out.println("Incorrect syntax. Command: 'attack <friendly lane> <enemy lane>");
                                        }
                                    }else{
                                        System.out.println("Incorrect format (see 'attack help' for details)");
                                    }
                                }
                            }
                            break;

                        default:
                            Class<? extends Ability> cls = JsonAccessor.getAbil(words.get(0));
                            if (cls != null) {
                                try {
                                    Card card = Parser.parseCardFromField(player, words.subList(1, words.size()));
                                    if (!((Field) GameComponents.getInstance().getPlayerContainer(player, Field.class)).use(card, null, cls)) System.out.println("This card does not have a " + Character.toUpperCase(cls.getSimpleName().charAt(0)) + cls.getSimpleName().substring(1).toLowerCase() + " ability.");
                                } catch (IndexOutOfBoundsException e) {
                                    System.out.println("Card not found on field");
                                }
                            }
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
        }while(!words.get(0).equalsIgnoreCase("quit"));

    }

    private void printHand(Player player) throws IOException {
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

    private void printCard(Card card) {
        System.out.println(card.getName() + ":\n  " + card.getDescription());
    }
}
