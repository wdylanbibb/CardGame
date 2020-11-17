package Card_Game.Abilities;

import Card_Game.CardContainers.Deck;
import Card_Game.Cards.Card;
import Card_Game.GameComponents;
import Card_Game.Player;

public class Scry implements Ability {

    private int num;

    public Scry(int num) {
        this.num = num;
    }

    @Override
    public void run(Card card) {
        Player player = card.getPlayer();
        System.out.println("Top " + num + " card of the deck:");
        for (Card c : ((Deck) GameComponents.getInstance().getPlayerContainer(player, Deck.class)).look(num)) {
            System.out.println("   " + c.getName());
        }
    }
}