package Card_Game.CardContainers;

import Card_Game.Cards.Card;

import java.util.ArrayList;

public class Hand extends ArrayList<Card> implements CardContainer {
    public boolean play(Card card) {
        if (contains(card)) {
            remove(card);
            card.play();
        }
        return false;
    }
}
