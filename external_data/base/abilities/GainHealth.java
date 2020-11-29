package base.abilities;

import cardgame.Player;
import cardgame.abilities.Ability;
import cardgame.cards.Card;
import com.google.gson.JsonArray;

public class GainHealth extends Ability {

    int num;

    public GainHealth(JsonArray array, Card card) {
        super(array, card);
        try {
            num = array.get(0).getAsInt();
        } catch (Exception exc) {
            card.removeAbility(this);
        }
    }

    @Override
    public void run() {
        Player player = card.getPlayer();
    }
}
