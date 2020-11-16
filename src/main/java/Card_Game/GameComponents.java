package Card_Game;

import Card_Game.CardContainers.*;

import java.util.HashMap;
import java.util.Map;

public class GameComponents {

    private static GameComponents INSTANCE = null;

    private Map<Player, Map<Class<? extends CardContainer>, CardContainer>> players;

    public GameComponents(Player... players) {
        this.players = new HashMap<>();
        for (Player player : players) {
            Map<Class<? extends CardContainer>, CardContainer> containers = new HashMap<>();
            containers.put(Field.class, player.getField());
            containers.put(Hand.class, player.getHand());
            containers.put(Discard.class, player.getDiscard());
            containers.put(Deck.class, player.getDeck());
            this.players.put(player, containers);
        }
    }

    public static GameComponents newInstance(Player... players) {
        INSTANCE = new GameComponents(players);
        return INSTANCE;
    }

    public static GameComponents getInstance() {
        return INSTANCE;
    }

    public Map<Class<? extends CardContainer>, CardContainer> getPlayerContainers(Player player) {
        return players.getOrDefault(player, null);
    }

    public CardContainer getPlayerDeck(Player player) {
        return players.get(player).get(Deck.class);
    }

    public CardContainer getPlayerDiscard(Player player) {
        return players.get(player).get(Discard.class);
    }

    public CardContainer getPlayerField(Player player) {
        return players.get(player).get(Field.class);
    }

    public CardContainer getPlayerHand(Player player) {
        return players.get(player).get(Hand.class);
    }
}
