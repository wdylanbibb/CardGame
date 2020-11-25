package Card_Game;


import Card_Game.Abilities.AbilityRunListener;
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
                                    .getAbilitiesFromListener(AbilityRunListener.TURNSTART)
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
                        default:
                            Class<? extends Ability> cls = UtilMaps.getInstance().getAbilityByString(words.get(0));
                            if (cls != null) {
                                try {
                                    Card card = Parser.parseCardFromField(player, words.subList(1, words.size()));
                                    if (!((Field) GameComponents.getInstance().getPlayerContainer(player, Field.class)).use(card, null, cls)) System.out.println("This card does not have a " + Character.toUpperCase(cls.getSimpleName().charAt(0)) + cls.getSimpleName().substring(1).toLowerCase() + " ability.");
                                } catch (IndexOutOfBoundsException e) {
                                    System.out.println("Card not found on field");
                                }
                            }
                    }
                }
            }
            player.endTurn();
            GameComponents
                        .getInstance()
                        .getSelfFieldCards(player)
                        .forEach(card -> card
                                .getAbilitiesFromListener(AbilityRunListener.TURNEND)
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
        List<Card> currFrontList = new ArrayList<>(Arrays.asList(currFront));
        Collections.reverse(currFrontList);
        List<Card> currBackList = new ArrayList<>(Arrays.asList(currBack));
        Collections.reverse(currBackList);

        //Print opposite back field
        ArrayList<ArrayList<String>> oppBackImages = new ArrayList<>();
        for(int i = 0; i < oppBackList.size(); i++){
            oppBackImages.add(getCardImage(oppBackList.get(i), " "));
        }
        printField(oppBackImages);

        //Print opposite front field
        ArrayList<ArrayList<String>> oppFrontImages = new ArrayList<>();
        for(int i = 0; i < oppFrontList.size(); i++){
            oppFrontImages.add(getCardImage(oppFrontList.get(i), " "));
        }
        printField(oppFrontImages);

        //Print current Front field
        ArrayList<ArrayList<String>> currFrontImages = new ArrayList<>();
        for(int i = 0; i < currFrontList.size(); i++){
            currFrontImages.add(getCardImage(currFrontList.get(i), Integer.toString(i)));
        }
        printField(currFrontImages);

        //Print current back field
        ArrayList<ArrayList<String>> currBackImages = new ArrayList<>();
        for(int i = 0; i < currBackList.size(); i++){
            currBackImages.add(getCardImage(currBackList.get(i), Integer.toString(i)));
        }
        printField(currBackImages);


    }

    private void printField(ArrayList<ArrayList<String>> fieldImages){
        ArrayList<String> line1 = new ArrayList<>();
        ArrayList<String> line2 = new ArrayList<>();
        ArrayList<String> line3 = new ArrayList<>();
        ArrayList<String> line4 = new ArrayList<>();
        for(ArrayList<String> image : fieldImages) {
            line1.add(image.get(0));
            line2.add(image.get(1));
            line3.add(image.get(2));
            line4.add(image.get(3));
        }

        System.out.println(StringUtils.join(line1, "   "));
        System.out.println(StringUtils.join(line2, "   "));
        System.out.println(StringUtils.join(line3, "   "));
        System.out.println(StringUtils.join(line4, "   "));

    }

    private ArrayList<String> getCardImage(Card card, String boardSpot){
        ArrayList<String> cardList = new ArrayList<>();
        cardList.add("-----");
        cardList.add(card != null ? "|" + card.getName().substring(0, 3) + "|" : "| " + boardSpot +" |");
        cardList.add("|   |");
        cardList.add("-----");
        return cardList;
    }

    public void printCardDesc(Card card) {
        System.out.println(card.getName() + ":\n  " + card.getDescription());
    }
}
