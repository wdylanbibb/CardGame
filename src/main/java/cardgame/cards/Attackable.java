package cardgame.cards;

import cardgame.cards.cardtypes.Monster.Monster;

public interface Attackable {
    public boolean attack(Monster target);
}
