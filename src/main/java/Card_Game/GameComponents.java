package Card_Game;

import Card_Game.CardContainers.*;
import Card_Game.Cards.Card;

import java.util.*;

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

    public CardContainer getPlayerContainer(Player player, Class<? extends CardContainer> container){ return players.get(player).get(container); }

    public List<Card> getAllFieldCards() {
        List<CardContainer> fields = new ArrayList<>();
        players.values().forEach(map -> fields.add(map.get(Field.class)));
        List<Card> cards = new ArrayList<>();
        fields.forEach(field -> {
            cards.addAll(Arrays.asList(((Field) field).getBottomRow()));
            cards.addAll(Arrays.asList(((Field) field).getMonsters()));
        });
        cards.removeIf(Objects::isNull);
        return cards;
    }
}
