package cardgame.cardcontainers;

import cardgame.cards.Card;

import java.util.ArrayList;
import java.util.List;

public class Discard extends ArrayList<Card> implements CardContainer {

    public List<Card> reshuffle() {
        List<Card> deck = this;
        removeAll(deck);
        return deck;
    }

    @Override
    public boolean add(Card card) {
        boolean ret = super.add(card);
        card.destroy();
        return ret;
    }
}
