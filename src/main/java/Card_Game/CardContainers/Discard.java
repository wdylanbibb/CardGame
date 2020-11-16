package Card_Game.CardContainers;

import Card_Game.Cards.Card;

import java.util.ArrayList;
import java.util.List;

public class Discard extends ArrayList<Card> implements CardContainer {

    public List<Card> reshuffle() {
        List<Card> deck = this;
        removeAll(deck);
        return deck;
    }
}
