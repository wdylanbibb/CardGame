package cardgame;


import cardgame.cardcontainers.Field;
import cardgame.cards.Card;
import cardgame.emissions.Signal;
import cardgame.emissions.SignalManager;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Game {

    Player[] players;
    Field[] board;
    Scanner scanner;

    Signal turnStart = SignalManager.createSignal("turnstart", ArrayList.class);
    Signal turnEnd = SignalManager.createSignal("turnend", ArrayList.class);
    Signal targetChosen = SignalManager.createSignal("targetchosen", Integer.class, Card.class); //Emits once a target is chose, takes a request code

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

        SignalManager.connect("choosetarget", (signal, args) -> {
            int requestCode = (int) args[0];
            Card target = chooseTarget((Card) args[1], (EnumManager.PlayerEffect) args[2], (EnumManager.CardType) args[3]);
            targetChosen.emit(requestCode, target);
        }, null, true);
    }

    public static void registerSignals(){
        SignalManager.createSignal("turnstart", ArrayList.class);
        SignalManager.createSignal("turnend", ArrayList.class);
        SignalManager.createSignal("use", ArrayList.class, String.class);
        SignalManager.createSignal("onplay", ArrayList.class);
        SignalManager.createSignal("ondestroy", ArrayList.class);
        SignalManager.createSignal("ondeath", ArrayList.class);
        SignalManager.createSignal("choosetarget", Integer.class, Card.class, EnumManager.PlayerEffect.class, EnumManager.CardType.class);
        SignalManager.createSignal("targetchosen", Integer.class, Card.class);
        SignalManager.createSignal("onattack", ArrayList.class);
        SignalManager.createSignal("onattacked", ArrayList.class);
        SignalManager.createSignal("abilityused", ArrayList.class, String.class);
        SignalManager.createSignal("attributeused", ArrayList.class, String.class);
        SignalManager.createSignal("newrule", ArrayList.class);
        SignalManager.createSignal("endrule", ArrayList.class);
    }

    public static void main(String[] args) throws IOException {
        registerSignals();

        Game game = new Game(new Scanner(System.in));

        game.run();
    }

    public void run() {
        int turns = 0;
        int currPlayer = 0;
        ArrayList<String> words;
        boolean endGame = false;
        do{
            turns++;
            boolean endTurn = false;
            boolean beginningTurn = true;
            Player player = players[currPlayer];
            player.setMana(Player.MAX_MANA);
            while (!endTurn){
                System.out.print(beginningTurn ? "Player " + (currPlayer + 1) + "'s Turn\n" : "");
                System.out.println(player.getMana() + " Mana Left");


                if (beginningTurn) {
                    printBoard(player, players[currPlayer + 1 >= players.length ? 0 : currPlayer + 1]);
                    ArrayList<Card> cardsDrew = (ArrayList<Card>) player.multiNoManaDraw(player.getInitDraw());
                    System.out.println("You drew:\n    " + StringUtils.join(cardsDrew.stream().map(Card::getName).collect(Collectors.toList()), ", "));
                    System.out.print("Your hand:\n");
                    printHand(player);
                    turnStart.emit(GameComponents.getInstance().getSelfFieldCards(player));
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
            GameComponents.getInstance().getAllFieldCards().forEach(card -> card.setDestroyAfter(card.getDestroyAfter() > 0 ? card.getDestroyAfter()-1 : card.getDestroyAfter()));
            player.endTurn();
            turnEnd.emit(GameComponents.getInstance().getSelfFieldCards(player));
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
            currFrontImages.add(getCardImage(currFrontList.get(4-i), Integer.toString(i)));
        }
        printField(currFrontImages);

        //Print current back field
        ArrayList<ArrayList<String>> currBackImages = new ArrayList<>();
        for(int i = 0; i < currBackList.size(); i++){
            currBackImages.add(getCardImage(currBackList.get(4-i), Integer.toString(i)));
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
        cardList.add(card != null ? "|" + card.getName().substring(0, 3) + "|" : "| " + (StringUtils.isNumeric(boardSpot) ? Integer.parseInt(boardSpot)+1 : boardSpot)  +" |");
        cardList.add("|   |");
        cardList.add("-----");
        return cardList;
    }

    public void printCardDesc(Card card) {
        System.out.println(card.getName() + ":\n  " + card.getDescription());
    }

    public Card chooseTarget(Card card, EnumManager.PlayerEffect effect, EnumManager.CardType cardType){
        System.out.println("Choose a target of type " + UtilMaps.getInstance().getCardClass(cardType).getSimpleName());
        Card returnCard = null;
        do {
            try{
                switch (effect) {
                    case SELF -> {
                        returnCard = Parser.parseCardFromField(card.getPlayer(), Parser.parser());
                        break;
                    }
                    case NOT_SELF -> {
                        ArrayList<Card> cardsToParse = new ArrayList<>();
                        GameComponents.getInstance().getAllPlayers().stream().filter(player -> player != card.getPlayer()).forEach(player -> cardsToParse.addAll(GameComponents.getInstance().getSelfFieldCards(player)));
                        returnCard = Parser.parseCardFromList(cardsToParse, Parser.parser());
                        break;
                    }
                    case ALL -> {
                        returnCard = Parser.parseCardFromAnyField(Parser.parser());
                        break;
                    }
                    case NONE -> {
                        return null;
                    }
                }
                Class<? extends Card> cls = UtilMaps.getInstance().getCardClass(cardType);
                if(!cls.isAssignableFrom(card.getClass())){
                    returnCard = null;
                    System.out.println("Wrong card type. Choose a target of type " + UtilMaps.getInstance().getCardClass(cardType).getSimpleName());
                }
            }catch(IndexOutOfBoundsException exc){
                returnCard = null;
            }
        }while(returnCard==null);
        return returnCard;
    }
}
