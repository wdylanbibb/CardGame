package cardgame.cardcontainers;

import cardgame.cards.Card;
import cardgame.cards.cardtypes.Monster.Monster;
import cardgame.cards.cardtypes.Spell.Spell;
import cardgame.emissions.Signal;
import cardgame.emissions.SignalManager;

import java.util.ArrayList;
import java.util.List;

public class Field implements CardContainer {
    private int len;
    private Monster[] monsters;
    private Card[] bottomRow;
    private final Signal onDestroy = SignalManager.createSignal("ondestroy", ArrayList.class);

    public Field(int len) {
        this.len = len;
        monsters = new Monster[len];
        bottomRow = new Card[len];
    }

    public boolean play(Card card, int pos, Hand hand) {
        if (card instanceof Monster && monsters[pos] == null) {
            if (!hand.play(card)) return false;
            monsters[pos] = (Monster) card;
            card.play();
            return true;
        }
        if (Spell.class.isAssignableFrom(card.getClass()) && bottomRow[pos] == null) {
            if (!hand.play(card)) return false;
            bottomRow[pos] = card;
            card.play();
            return true;
        }
        return false;
    }

    public List<Card> checkField() {
        List<Card> toDiscard = new ArrayList<>();
        for (int i = 0; i < monsters.length; i++) {
            if (monsters[i] != null) {
                if (monsters[i].isDead()) {
                    toDiscard.add(monsters[i]);
                    monsters[i] = null;
                }
                else if (monsters[i].getDestroyAfter() == 0){
                    monsters[i].die();
                    toDiscard.add(monsters[i]);
                    monsters[i] = null;

                }
            }
        }
        for (int i = 0; i < bottomRow.length; i++) {
            if (bottomRow[i] != null) {
                if (bottomRow[i].getDestroyAfter() == 0){
                    toDiscard.add(bottomRow[i]);
                    onDestroy.emit(new ArrayList<>(List.of(bottomRow[i])));
                    bottomRow[i] = null;
                }
            }
        }
        return toDiscard;
    }

    public Card[] getBottomRow() {
        return bottomRow;
    }

    public Monster[] getMonsters() {
        return monsters;
    }
}
