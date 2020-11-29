package cardgame.cardcontainers;

import cardgame.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck extends ArrayList<Card> implements CardContainer {
    public void shuffle() {
        Collections.shuffle(this);
    }

    public Card draw() {
        return remove(0);
    }

    public List<Card> look(int num) {
        return subList(0, num);
    }
}
