package cardgame.abilities.abils.base;

import cardgame.*;
import cardgame.abilities.*;
import cardgame.cardcontainers.*;
import cardgame.cards.*;
import com.google.gson.*;

public class GainHealth extends Ability {

    int num;

    public GainHealth(JsonObject args, Card card) {
        super(args, card);
        if (args == null) {
            card.removeAbility(this);
            return;
        }
        try {
            num = args.get("num").getAsInt();
        } catch (Exception exc) {
            card.removeAbility(this);
        }
    }

    @Override
    public void run() {
        Player player = card.getPlayer();
    }
}
