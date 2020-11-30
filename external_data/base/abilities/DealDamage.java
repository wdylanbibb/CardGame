package base.abilities;

import cardgame.GameComponents;
import cardgame.Player;
import cardgame.abilities.Ability;
import cardgame.cards.Card;
import com.google.gson.JsonObject;

public class DealDamage extends Ability {

    int num;

    public DealDamage(JsonObject args, Card card) {
        super(args, card);
        try {
            num = args.get("num").getAsInt();
        } catch (NullPointerException exc) {
            card.removeAbility(this);
        }
    }

    @Override
    public void run() {
        for (Player player : GameComponents.getInstance().getPlayers()) {
            if (player != card.getPlayer()) {
                player.removeHealth(num);
            }
        }
    }
}
