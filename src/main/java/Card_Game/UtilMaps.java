package Card_Game;

import Card_Game.Abilities.AbilityRunListener;
import Card_Game.Abilities.Ability;
import Card_Game.Cards.Card;
import Card_Game.Cards.CardTypes.Monster.Monster;
import Card_Game.Cards.CardTypes.Spell.OneUseSpell;
import Card_Game.Cards.CardTypes.Spell.Spell;
import Card_Game.Rules.RuleUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilMaps {

    private static UtilMaps INSTANCE;
    private static Map<String, AbilityRunListener> abilMap;
    private static Map<String, Class<? extends Card>> cardTypes;
    private static Map<String, RuleUtils.CompareAttr> comparisonsMap;
    private static Map<String, RuleUtils.PlayerEffect> playerEffects;
    private static Map<String, Class<? extends Ability>> abilList;
    private static Map<String, String> deckJsons;
    private static Map<String, String> cardJsons;

    public UtilMaps(){
        abilMap = new HashMap<>();
        abilMap.put("onplay", AbilityRunListener.PLAY);
        abilMap.put("ondeath", AbilityRunListener.DEATH);
        abilMap.put("turnstart", AbilityRunListener.TURNSTART);
        abilMap.put("turnend", AbilityRunListener.TURNEND);
        abilMap.put("use", AbilityRunListener.USE);

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

        abilList = new HashMap<>();

        deckJsons = new HashMap<>();

        deckJsons = new HashMap<>();
    }

    public static UtilMaps getInstance() {
        if(INSTANCE==null){
            INSTANCE = new UtilMaps();
        }
        return INSTANCE;
    }

    public AbilityRunListener getAbilRunScen(String scene){
        return abilMap.getOrDefault(scene, AbilityRunListener.USE);
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

    public void fillAbilList(Map<String, Class<? extends Ability>> list) {
        abilList = list;
    }

    public Class<? extends Ability> getAbilityByString(String abil) {
        return abilList.getOrDefault(abil, null);
    }

    public void fillDeckList(Map<String, String> decks) {
        deckJsons = decks;
    }

    public String getDeckByName(String name) {
        return deckJsons.getOrDefault(name, null);
    }

    public List<String> getDeckPaths() {
        return new ArrayList<>(deckJsons.values());
    }

    public void fillCardList(Map<String, String> cards) {
        cardJsons = cards;
    }

    public String getCardByName(String name) {
        return cardJsons.getOrDefault(name, null);
    }

    public List<String> getCardPaths() {
        return new ArrayList<>(cardJsons.values());
    }
}
