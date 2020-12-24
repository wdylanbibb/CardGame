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
        setTargetType(EnumManager.PlayerEffect.ALL);
    }

    @Override
    public void run(Card target) {
        Player player = card.getPlayer();
        player.addHealth(num);
    }

    @Override
    public String getName() {
        return "gainhealth";
    }
}
