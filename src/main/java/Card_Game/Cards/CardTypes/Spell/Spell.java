package Card_Game.Cards.CardTypes.Spell;

import Card_Game.Cards.Card;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;

public class Spell extends Card {
    public Spell(@Nonnull String name, int cost, String description) {
        super(name, cost, description);
    }

    @Override
    public void setParams(JsonObject params) {

    }
}
