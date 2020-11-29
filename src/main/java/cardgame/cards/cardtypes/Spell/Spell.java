package cardgame.cards.cardtypes.Spell;

import cardgame.cards.Card;

import javax.annotation.Nonnull;

public class Spell extends Card {
    public Spell(@Nonnull String name, int cost, String description) {
        super(name, cost, description);
    }
}
