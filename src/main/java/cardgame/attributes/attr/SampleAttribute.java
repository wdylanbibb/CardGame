package cardgame.attributes.attr;

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

public class SampleAttribute extends Attribute {

    public SampleAttribute(JsonObject args, Card card) {
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
