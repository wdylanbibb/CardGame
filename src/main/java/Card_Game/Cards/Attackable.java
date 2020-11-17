package Card_Game.Cards;

import Card_Game.Cards.CardTypes.Monster.Monster;

public interface Attackable {
    public boolean attack(Monster target);
}
