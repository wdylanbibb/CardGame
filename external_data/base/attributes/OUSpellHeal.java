package base.attributes;

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

public class OUSpellHeal extends Attribute {

    int num;

    public OUSpellHeal(JsonObject args, Card card) {
        super(args, card);
        if (args == null) {
            card.removeAttr(this);
            return;
        }
        try {
            num = args.get("num").getAsInt();
        } catch (Exception exc) {
            card.removeAttr(this);
        }
    }

    @Override
    public void run(Card target) {
        System.out.println("This should heal it " + num + "health");
    }

    @Override
    public String getName(){
        return "ouspellheal";
    }
}
