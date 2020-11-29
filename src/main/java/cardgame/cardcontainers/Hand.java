package cardgame.cardcontainers;

import cardgame.cards.Card;

import java.util.ArrayList;

public class Hand extends ArrayList<Card> implements CardContainer {
    public boolean play(Card card) {
        if (contains(card)) {
            remove(card);
            card.play();
            return true;
        }
        return false;
    }
}
