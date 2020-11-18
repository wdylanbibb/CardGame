package Card_Game;

import Card_Game.Abilities.AbilRunScen;
import Card_Game.Cards.Card;
import Card_Game.Cards.CardTypes.Monster.Monster;
import Card_Game.Cards.CardTypes.Spell.OneUseSpell;
import Card_Game.Cards.CardTypes.Spell.Spell;

import java.util.HashMap;
import java.util.Map;

public class UtilMaps {

    private static UtilMaps INSTANCE;
    public static Map<String, AbilRunScen> abilMap;
    public static Map<String, Class<? extends Card>> cardTypes;

    public UtilMaps(){
        abilMap = new HashMap<>();
        abilMap.put("onplay", AbilRunScen.PLAY);
        abilMap.put("ondeath", AbilRunScen.DEATH);
        abilMap.put("turnstart", AbilRunScen.TURNSTART);
        abilMap.put("turnend", AbilRunScen.TURNEND);
        abilMap.put("use", AbilRunScen.USE);

        cardTypes = new HashMap<>();
        cardTypes.put("monster", Monster.class);
        cardTypes.put("spell", Spell.class);
        cardTypes.put("ouspell", OneUseSpell.class);
    }

    public static UtilMaps getInstance() {
        if(INSTANCE==null){
            INSTANCE = new UtilMaps();
        }
        return INSTANCE;
    }

    public AbilRunScen getAbilRunScen(String scene){
        return abilMap.getOrDefault(scene, AbilRunScen.USE);
    }

    public Class<? extends Card> getCardTypeByStr(String type){
        return cardTypes.getOrDefault(type, null);
    }
}