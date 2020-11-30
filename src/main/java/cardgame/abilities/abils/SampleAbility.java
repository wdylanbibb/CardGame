package cardgame.abilities.abils;

import cardgame.*;
import cardgame.abilities.*;
import cardgame.cardcontainers.*;
import cardgame.cards.*;
import com.google.gson.*;

public class SampleAbility extends Ability {

    public SampleAbility(JsonObject args, Card card) {
        super(args, card);
    }

    @Override
    public void run() {

    }
}
