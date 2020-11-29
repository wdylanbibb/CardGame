package cardgame.cards;

import cardgame.abilities.Ability;

public interface Playable {
    default void play() {
    }

    boolean use(Playable target, Class<? extends Ability> cls);

    default void destroy() {
    }
}