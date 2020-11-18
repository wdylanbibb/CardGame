package Card_Game;

import Card_Game.Abilities.AbilRunScen;
import Card_Game.Cards.Card;
import Card_Game.Cards.CardTypes.Monster.Monster;
import Card_Game.Cards.CardTypes.Spell.OneUseSpell;
import Card_Game.Cards.CardTypes.Spell.Spell;
import Card_Game.Rules.RuleUtils;

import java.util.HashMap;
import java.util.Map;

public class UtilMaps {

    private static UtilMaps INSTANCE;
    private static Map<String, AbilRunScen> abilMap;
    private static Map<String, Class<? extends Card>> cardTypes;
    private static Map<String, RuleUtils.CompareAttr> comparisonsMap;
    private static Map<String, RuleUtils.PlayerEffect> playerEffects;

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

        comparisonsMap = new HashMap<>();
        comparisonsMap.put(">", RuleUtils.CompareAttr.GREATER_THAN);
        comparisonsMap.put("<", RuleUtils.CompareAttr.LESS_THAN);
        comparisonsMap.put(">=", RuleUtils.CompareAttr.GREATER_OR_EQUAL);
        comparisonsMap.put("<=", RuleUtils.CompareAttr.LESS_OR_EQUAL);
        comparisonsMap.put("=", RuleUtils.CompareAttr.EQUAL_TO);
        comparisonsMap.put("==", RuleUtils.CompareAttr.EQUAL_TO);
        comparisonsMap.put("!=", RuleUtils.CompareAttr.NOT_EQUAL);

        playerEffects = new HashMap<>();
        playerEffects.put("self", RuleUtils.PlayerEffect.SELF);
        playerEffects.put("all", RuleUtils.PlayerEffect.ALL);
        playerEffects.put("other", RuleUtils.PlayerEffect.NOT_SELF);
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

    public RuleUtils.CompareAttr getComparisonByString(String compare){
        return comparisonsMap.getOrDefault(compare, RuleUtils.CompareAttr.NONE);
    }

    public RuleUtils.PlayerEffect getPlayerByString(String player){
        return playerEffects.getOrDefault(player, RuleUtils.PlayerEffect.NONE);
    }
}
