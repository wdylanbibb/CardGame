package Card_Game.Abilities.Abils;

import Card_Game.Abilities.*;
import Card_Game.*;
import Card_Game.CardContainers.*;
import Card_Game.Cards.*;
import Card_Game.Rules.*;
import com.google.gson.*;
import java.util.*;

public class SampleAbility extends Ability {

    public SampleAbility(JsonArray array, Card card) {
        super(array, card);
    }

    @Override
    public void run() {

    }
}
