package Card_Game.Cards;

import Card_Game.Abilities.Ability;

public interface Playable {
    default void play() {}
    boolean use(Playable target, Class<? extends Ability> cls);
}
