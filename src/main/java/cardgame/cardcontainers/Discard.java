package cardgame.cardcontainers;

import cardgame.cards.Card;

import java.util.ArrayList;
import java.util.Collection;
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

    @Override
    public boolean addAll(Collection<? extends Card> c) {
        boolean ret = super.addAll(c);
        c.forEach(Card::destroy);
        return ret;
    }

    public List<Card> look(int num) {
        List<Card> ret = new ArrayList<>();
        if (size() >= num) {
            for (int i = 0; i < num; i++) {
                ret.add(get(size() - i - 1));
            }
        } else if (size() > 0) {
            ret.addAll(this);
        } else {
            ret.add(null);
        }
        return ret;
    }

    public Card look() {
        if (!isEmpty()) {
            return get(size() - 1);
        } else {
            return null;
        }
    }
}
