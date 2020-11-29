package cardgame.abilities.abils.base;

import cardgame.*;
import cardgame.abilities.*;
import cardgame.cardcontainers.*;
import cardgame.cards.*;
import com.google.gson.*;

public class Scry extends Ability {

    private int num;

    public Scry(JsonArray array, Card card) {
        super(array, card);
        try {
            num = array.get(0).getAsInt();
        } catch (Exception exc) {
            card.removeAbility(this);
        }
    }

    @Override
    public void run() {
        Player player = card.getPlayer();
        Deck deck = (Deck) GameComponents.getInstance().getPlayerContainer(player, Deck.class);
        System.out.println("Top " + Math.min(deck.size(), num) + " card(s) of the deck:");
        for (Card c : deck.look(Math.min(deck.size(), num))) {
            System.out.println("   " + c.getName());
        }
    }
}
