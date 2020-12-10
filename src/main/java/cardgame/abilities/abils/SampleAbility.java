package cardgame.abilities.abils;

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


public class SampleAbility extends Ability {

    public SampleAbility(JsonObject args, Card card) {
        super(args, card);
    }

    @Override
    public void run(Card target) {

    }

    @Override
    public String getName() {
        return "Sample";
    }
}
