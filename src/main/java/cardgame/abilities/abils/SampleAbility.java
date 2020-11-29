package cardgame.abilities.abils;

import cardgame.*;
import cardgame.abilities.*;
import cardgame.cardcontainers.*;
import cardgame.cards.*;
import com.google.gson.*;

public class SampleAbility extends Ability {

    public SampleAbility(JsonArray array, Card card) {
        super(array, card);
    }

    @Override
    public void run() {

    }
}
