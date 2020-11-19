package base.abilities;

import Card_Game.Abilities.Ability;
import Card_Game.CardContainers.Deck;
import Card_Game.Cards.Card;
import Card_Game.GameComponents;
import Card_Game.Player;
import com.google.gson.JsonArray;

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
        System.out.println("Top " + Math.min(deck.size(), num)+ " card(s) of the deck:");
        for (Card c : deck.look(Math.min(deck.size(), num))) {
            System.out.println("   " + c.getName());
        }
    }
}