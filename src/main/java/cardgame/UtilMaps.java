package cardgame;

import cardgame.abilities.Ability;
import cardgame.attributes.Attribute;
import cardgame.cards.Card;
import cardgame.cards.cardtypes.Monster.Monster;
import cardgame.cards.cardtypes.Spell.OneUseSpell;
import cardgame.cards.cardtypes.Spell.Spell;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UtilMaps {

    private static UtilMaps INSTANCE;
    private static Map<String, EnumManager.CardEvent> eventMap;
    private static Map<String, Pair<EnumManager.CardType, Class<? extends Card>>> cardTypes;
    private static Map<String, EnumManager.CompareAttr> comparisonsMap;
    private static Map<String, EnumManager.PlayerEffect> playerEffects;
    private static Map<String, Class<? extends Ability>> abilList;
    private static Map<String, Class<? extends Attribute>> attrList;
    private static Map<String, String> deckJsons;
    private static Map<String, String> cardJsons;

    public UtilMaps(){
        eventMap = new HashMap<>();
        eventMap.put("onplay", EnumManager.CardEvent.PLAY);
        eventMap.put("ondeath", EnumManager.CardEvent.DEATH);
        eventMap.put("turnstart", EnumManager.CardEvent.TURNSTART);
        eventMap.put("turnend", EnumManager.CardEvent.TURNEND);
        eventMap.put("use", EnumManager.CardEvent.USE);
        eventMap.put("ondestroy", EnumManager.CardEvent.DESTROY);
        eventMap.put("onattack", EnumManager.CardEvent.ATTACK);
        eventMap.put("onattacked", EnumManager.CardEvent.ATTACKED);
        eventMap.put("abilityused", EnumManager.CardEvent.ABILITYUSED);
        eventMap.put("attributeused", EnumManager.CardEvent.ATTRIBUTEUSED);
        eventMap.put("newrule", EnumManager.CardEvent.NEWRULE);
        eventMap.put("endrule", EnumManager.CardEvent.ENDRULE);

        cardTypes = new HashMap<>();
        cardTypes.put("monster", Pair.of(EnumManager.CardType.MONSTER, Monster.class));
        cardTypes.put("spell", Pair.of(EnumManager.CardType.SPELL, Spell.class));
        cardTypes.put("ouspell", Pair.of(EnumManager.CardType.OUSPELL, OneUseSpell.class));
        cardTypes.put("default", Pair.of(EnumManager.CardType.ALL, Card.class));

        comparisonsMap = new HashMap<>();
        comparisonsMap.put(">", EnumManager.CompareAttr.GREATER_THAN);
        comparisonsMap.put("<", EnumManager.CompareAttr.LESS_THAN);
        comparisonsMap.put(">=", EnumManager.CompareAttr.GREATER_OR_EQUAL);
        comparisonsMap.put("<=", EnumManager.CompareAttr.LESS_OR_EQUAL);
        comparisonsMap.put("=", EnumManager.CompareAttr.EQUAL_TO);
        comparisonsMap.put("==", EnumManager.CompareAttr.EQUAL_TO);
        comparisonsMap.put("!=", EnumManager.CompareAttr.NOT_EQUAL);

        playerEffects = new HashMap<>();
        playerEffects.put("self", EnumManager.PlayerEffect.SELF);
        playerEffects.put("all", EnumManager.PlayerEffect.ALL);
        playerEffects.put("other", EnumManager.PlayerEffect.NOT_SELF);

        attrList = new HashMap<>();

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

    public EnumManager.CardEvent getCardEvent(String event){
        return eventMap.getOrDefault(event, EnumManager.CardEvent.USE);
    }

    public List<String> getCardEvents(){
        return new ArrayList<>(eventMap.keySet());
    }

    public Class<? extends Card> getCardClassByStr(String type){
        return cardTypes.getOrDefault(type, Pair.of(EnumManager.CardType.ALL, Card.class)).getRight();
    }

    public EnumManager.CardType getCardTypeByStr(String type){
        return cardTypes.getOrDefault(type, Pair.of(EnumManager.CardType.ALL, Card.class)).getLeft();
    }

    public EnumManager.CompareAttr getComparisonByString(String compare){
        return comparisonsMap.getOrDefault(compare, EnumManager.CompareAttr.NONE);
    }

    public EnumManager.PlayerEffect getPlayerByString(String player){
        return playerEffects.getOrDefault(player, EnumManager.PlayerEffect.NONE);
    }

    public void fillAttrList(Map<String, Class<? extends Attribute>> list) {
        attrList = list;
    }

    public Class<? extends Attribute> getAttrByString(String attr){
        return attrList.getOrDefault(attr, null);
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

    public Map<String, EnumManager.CardType> getCardTypes() {
        Map<String, EnumManager.CardType> cardType = new HashMap<>();
        cardTypes.forEach((s, cardTypeClassPair) -> cardType.put(s, cardTypeClassPair.getLeft()));
        return cardType;
    }

    public Class<? extends Card> getCardClass(EnumManager.CardType type){
        return cardTypes.values().stream().filter(cardTypeClassPair -> cardTypeClassPair.getLeft().equals(type)).collect(Collectors.toList()).get(0).getRight();
    }
}

