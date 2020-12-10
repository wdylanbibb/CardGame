package cardgame.cards;

import cardgame.abilities.Ability;
import cardgame.attributes.Attribute;

public interface Playable {
    default void play() {
    }

    boolean use(Card target, Ability ability);
    boolean use(Card target, Attribute attribute);

    default void destroy() {
    }
}