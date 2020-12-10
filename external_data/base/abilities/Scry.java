package cardgame.abilities.abils.base;

import cardgame.*;
import cardgame.abilities.*;
import cardgame.cardcontainers.*;
import cardgame.cards.*;
import com.google.gson.*;

public class Scry extends Ability {

    int num;

    public Scry(JsonObject args, Card card) {
        super(args, card);
        if(args==null){
            card.removeAbility(this);
            return;
        }
        try {
            num = args.get("num").getAsInt();
        } catch (Exception exc) {
            card.removeAbility(this);
        }
    }

    @Override
    public void run(Card target) {
        Player player = card.getPlayer();
        Deck deck = (Deck) GameComponents.getInstance().getPlayerContainer(player, Deck.class);
        System.out.println("Top " + Math.min(deck.size(), num) + " card(s) of the deck:");
        for (Card c : deck.look(Math.min(deck.size(), num))) {
            System.out.println("   " + c.getName());
        }
    }

    @Override
    public String getName(){
        return "scry";
    }
}
