package Card_Game.CardContainers;

import Card_Game.Abilities.Ability;
import Card_Game.Cards.Card;
import Card_Game.Cards.CardTypes.Monster.Monster;
import Card_Game.Cards.CardTypes.Spell.Spell;
import Card_Game.UtilMaps;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
public class Field implements CardContainer {
    private int len;
    private Monster[] monsters;
    private Card[] bottomRow;

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

    public boolean use(Card used, Card target, Class<? extends Ability> cls) {
        final boolean[] found = {false};
        IntStream.rangeClosed(0, len - 1).forEach(i -> {
            if (monsters[i] == used || bottomRow[i] == used) {
                found[0] = true;
            }
        });
        if (!found[0]) {
            return false;
        }
        return used.use(target, cls);
    }

    public List<Card> checkField() {
        List<Card> toDiscard = new ArrayList<>();
        for (int i = 0; i < monsters.length; i++) {
            if (monsters[i] != null) {
                if (monsters[i].isDead()) {
                    monsters[i] = null;
                    toDiscard.add(monsters[i]);
                }
            }
        }
        for (int i = 0; i < bottomRow.length; i++) {
            if (bottomRow[i] != null) {
                if (bottomRow[i].getClass().equals(UtilMaps.getInstance().getCardTypeByStr("ouspell"))) {
                    bottomRow[i] = null;
                    toDiscard.add(bottomRow[i]);
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
