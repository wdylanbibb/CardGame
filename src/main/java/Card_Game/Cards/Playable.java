package Card_Game.Cards;

import Card_Game.Abilities.Ability;
import Card_Game.Player;

public interface Playable {
    default void play() {
    }

    boolean use(Playable target, Class<? extends Ability> cls);

    default void destroy() {
    }
}