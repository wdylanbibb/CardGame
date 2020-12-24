package cardgame.abilities.abils.base;

import java.util.*;
import cardgame.*;
import cardgame.abilities.*;
import cardgame.attributes.*;
import cardgame.cardcontainers.*;
import cardgame.cards.*;
import cardgame.cards.cardtypes.Spell.*;
import cardgame.cards.cardtypes.Monster.*;
import cardgame.rules.*;
import com.google.gson.*;

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
    public void run(Card target) {
        for (Player player : GameComponents.getInstance().getAllPlayers()) {
            if (player != card.getPlayer()) {
                player.removeHealth(num);
            }
        }
    }

    @Override
    public String getName() {
        return "dealdamage";
    }
}
