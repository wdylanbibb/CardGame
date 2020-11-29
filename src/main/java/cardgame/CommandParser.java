package cardgame;

import cardgame.abilities.AbilityRunListener;
import cardgame.abilities.Ability;
import cardgame.cardcontainers.Field;
import cardgame.cards.Card;
import cardgame.cards.cardtypes.Monster.Monster;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

class CommandParser {

    public static final int CONTINUE = 0;
    public static final int END_TURN = 1;
    public static final int END_GAME = 2;

    private List<String> words;
    private Player player;
    private final Game game;
    private int playerNum;
    private final Player[] players;

    CommandParser(List<String> words, Player player, Game game, int playerNum, Player[] players) {
        this.words = words;
        this.player = player;
        this.game = game;
        this.playerNum = playerNum;
        this.players = players;
    }

    int parseCmd() {
        switch (words.get(0).toLowerCase()) {
            case "play":
                return play();
            case "draw":
                return draw();
            case "end":
                return end();
            case "quit":
                return quit();
            case "info":
                return info();
            case "attack":
                return attack();
            default:
                return abil();
        }
    }

    private int play() {
        try {
            int num = Integer.parseInt(words.get(words.size() - 1)) - 1;
            try {
                Card cardPlayed = Parser.parseCardFromHand(player, words.subList(1, words.size() - 1));
                if (GameComponents.getInstance().canPlay(cardPlayed)) {
                    game.playCard(player, cardPlayed, num);
                    cardPlayed.getAbilitiesFromListener(AbilityRunListener.PLAY).forEach(Ability::run);
                } else {
                    System.out.println("Card cannot be played right now because of a rule set by another card.");
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
        return CONTINUE;
    }

    private int draw() {
        Card cardDrawn = player.draw();
        System.out.print(cardDrawn != null ? "You drew " + cardDrawn.getName() + "\n" : "");
        return CONTINUE;
    }

    private int quit() {
        return END_GAME;
    }

    private int end() {
        return END_TURN;
    }

    private int info() {
        if(words.size() > 1){
            game.printCardDesc(Parser.parseCardFromFieldOrHand(player, words.subList(1, words.size())));
        }else {
            System.out.print("Player " + (playerNum) + "'s Turn\n");
            System.out.println(player.getMana() + " Mana Left");
            System.out.print("Your hand:\n");
            game.printHand(player);
        }
        return CONTINUE;
    }

    private int attack() {
        if(words.size() > 1){
            if(words.get(1).equalsIgnoreCase("help")){
                System.out.println("'attack <friendly lane> <enemy lane>");
            }else{
                if(words.size() == 3){
                    if(StringUtils.isNumeric(words.get(1)) && StringUtils.isNumeric(words.get(2))){
                        try{
                            Monster monster = player.getField().getMonsters()[Integer.parseInt(words.get(1))-1];
                            Monster target = players[playerNum >= players.length ? 0 : playerNum].getField().getMonsters()[5-Integer.parseInt(words.get(2))];
                            if(monster != null && target != null){
                                boolean killed = monster.attack(target);
                                System.out.println(monster.getName() + " attacked " + target.getName() + " for " + monster.getAtk() + " damage!");
                                if(killed) {
                                    System.out.println(target.getName() + " has died!");
                                    target.getAbilitiesFromListener(AbilityRunListener.DEATH).forEach(Ability::run);
                                }
                                players[playerNum >= players.length ? 0 : playerNum].checkForDead();
                                game.printBoard(player, players[playerNum >= players.length ? 0 : playerNum]);
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
        return CONTINUE;
    }

    private int abil() {
        Class<? extends Ability> cls = UtilMaps.getInstance().getAbilityByString(words.get(0));
        if (cls != null) {
            try {
                Card card = Parser.parseCardFromField(player, words.subList(1, words.size()));
                if (!((Field) GameComponents.getInstance().getPlayerContainer(player, Field.class)).use(card, null, cls)) System.out.println("This card does not have a " + Character.toUpperCase(cls.getSimpleName().charAt(0)) + cls.getSimpleName().substring(1).toLowerCase() + " ability.");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Card not found on field");
            }
        }
        return CONTINUE;
    }
}
