package cardgame.cards;

import
        cardgame.*;
import cardgame.abilities.Ability;
import cardgame.attributes.Attribute;
import cardgame.emissions.EmissionListener;
import cardgame.emissions.Signal;
import cardgame.emissions.SignalManager;
import cardgame.rules.Rule;
import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Card implements Playable{

    @Nonnull
    private String name;
    private int cost;
    private Player player;
    private String description;
    private String image;
    private boolean onField;
    private int destroyAfter;
    private int turnPlayed;

    private static final int DESTROY_AFTER_DEFAULT = -1;
    private static final int TARGET_REQUEST_CODE = 0;

    private List<Ability> abils;

    private List<Attribute> attrList;

    private List<Rule> ruleList = new ArrayList<>();

    private static final Signal chooseTarget = SignalManager.createSignal("choosetarget", Integer.class, Card.class, EnumManager.PlayerEffect.class, EnumManager.CardType.class);
    private static final Signal abilUsed = SignalManager.createSignal("abilityused", ArrayList.class, String.class);
    private static final Signal attrUsed = SignalManager.createSignal("attributeused", ArrayList.class, String.class);
    private static final Signal newRule = SignalManager.createSignal("newrule", ArrayList.class);
    private static final Signal endRule = SignalManager.createSignal("endrule", ArrayList.class);

    public Card(@Nonnull String name, int cost, String description) {
        this.name = name;
        this.cost = cost;
        this.description = description;
    }

    @Override
    public boolean use(Card target, Ability ability) {
        if (ability != null) {
            ability.run(target);
            abilUsed.emit(new ArrayList<>(List.of(this)), ability.getName());
            return true;
        }
        return false;
    }

    @Override
    public boolean use(Card target, Attribute attribute) {
        if (attribute != null) {
            attribute.run(target);
            attrUsed.emit(new ArrayList<>(List.of(this)), attribute.getName());
            return true;
        }
        return false;
    }

    @Override
    public void play() {
        onField = true;
        ruleList.forEach(rule -> {
            GameComponents.getInstance().addRule(rule);
            newRule.emit(new ArrayList<>(List.of(this)));
        });
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public List<Ability> getAbils() {
        return abils;
    }

    public void setAbils(List<Ability> abils) {
        this.abils = abils;
    }

    public boolean addAbility(Ability ability) {
        if (abils.stream().noneMatch(abil -> abil.getClass() == ability.getClass())) return abils.add(ability);
        return false;
    }

    public boolean removeAbility(Ability ability) {
        return abils.remove(ability);
    }

    public boolean removeAbility(Class<? extends Ability> cls) {
        return abils.removeAll(abils.stream().filter(abil -> abil.getClass() == cls).collect(Collectors.toList()));
    }

    public boolean hasAbility(Class<? extends Ability> cls) {
        return abils.stream().anyMatch(abil -> abil.getClass() == cls);
    }

    public Ability getAbility(Class<? extends Ability> cls) {
        if (hasAbility(cls)) {
            return abils.stream().filter(ability -> ability.getClass() == cls).collect(Collectors.toList()).get(0);
        }
        return null;
    }

    public List<Attribute> getAttr() {
        return attrList;
    }

    public void setAttr(List<Attribute> attr) {
        this.attrList = attr;
    }

    public boolean addAttr(Attribute attribute) {
        if (attrList.stream().noneMatch(att -> att.getClass() == attribute.getClass())) return attrList.add(attribute);
        return false;
    }

    public boolean removeAttr(Attribute attribute) {
        return attrList.remove(attribute);
    }

    public boolean removeAttr(Class<? extends Attribute> cls) {
        return attrList.removeAll(attrList.stream().filter(attribute -> attribute.getClass() == cls).collect(Collectors.toList()));
    }

    public boolean hasAttr(Class<? extends Attribute> cls) {
        return attrList.stream().anyMatch(attribute -> attribute.getClass() == cls);
    }

    public Attribute getAttribute(Class<? extends Attribute> cls) {
        if (hasAttr(cls)) {
            return attrList.stream().filter(attribute -> attribute.getClass() == cls).collect(Collectors.toList()).get(0);
        }
        return null;
    }

    public List<Attribute> getAttributeFromTarget(EnumManager.PlayerEffect target){
        return attrList.stream().filter(attribute -> attribute.getTarget()==target).collect(Collectors.toList());
    }

    public List<Attribute> getAttributeFromEvent(EnumManager.CardEvent event){
        return attrList.stream().filter(attribute -> attribute.getEvent()==event).collect(Collectors.toList());
    }


    public void setParams(JsonObject params) {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public void destroy() {
        onField = false;
        ruleList.forEach(rule -> {
            GameComponents.getInstance().removeRule(rule);
            endRule.emit(new ArrayList<>(List.of(this)));
        });
    }

    public List<Rule> getRules() {
        return ruleList;
    }

    public void setRules(List<Rule> ruleList) {
        this.ruleList = ruleList;
    }

    public void addRule(Rule rule) {
        ruleList.add(rule);
    }

    public void removeRule(Rule rule) {
        ruleList.remove(rule);
    }

    public void verifyImage() throws IOException {
        if (image == null || !Base64.isBase64(image)) {
            image = Base64.encodeBase64String(FileUtils.readFileToByteArray(new File("external_data/default.png")));
        }

//        System.out.println(name + "\n\n" + image + "\n\n\n\n");
    }

    public void setEmissionListeners(){
        EmissionListener listener = (signal, args) -> {
            if(args[0] instanceof List){
                if(onField) {
                    if(((List<?>) args[0]).contains(this)) {
                        List<Ability> abilsByEvent = abils.stream().filter(abil -> abil.getCardEvent() == UtilMaps.getInstance().getCardEvent(signal.name)).collect(Collectors.toList());
                        abilsByEvent
                                = args.length > 1 && args[1] instanceof String
                                ? abilsByEvent.stream().filter(ability -> ability.getClass() == UtilMaps.getInstance().getAbilityByString((String) args[1])).collect(Collectors.toList())
                                : abilsByEvent;
                        Map<Boolean, List<Ability>> splitAbils = abilsByEvent.stream().collect(Collectors.partitioningBy(ability -> ability.getTargetType() != EnumManager.PlayerEffect.NONE));
                        splitAbils.get(true).forEach(ability -> {
                            EmissionListener targetListener = (signal1, args1) -> {
                                if (TARGET_REQUEST_CODE == (int) args1[0]) use((Card) args1[1], ability);
                            };
                            SignalManager.connect("targetchosen", targetListener, null, true);
                            chooseTarget.emit(TARGET_REQUEST_CODE, this, ability.getTargetType(), ability.getCardType());
                        });
                        splitAbils.get(false).forEach(ability -> use(null, ability));
                    }
                    if(!(((List<?>) args[0]).size() == 1 && ((List<?>) args[0]).contains(this))) {
                        List<Attribute> attrsByEvent = attrList.stream().filter(attr -> attr.getEvent() == UtilMaps.getInstance().getCardEvent(signal.name)).collect(Collectors.toList());
                        attrsByEvent
                                = args.length > 1 && args[1] instanceof String
                                ? attrsByEvent.stream().filter(attr -> attr.getClass() == UtilMaps.getInstance().getAttrByString((String) args[1])).collect(Collectors.toList())
                                : attrsByEvent;
                        if(((List<?>) args[0]).size() == 1){
                            attrsByEvent.forEach(attribute -> {
                                if(UtilMaps.getInstance().getCardClass(attribute.getType()) == ((List<?>) args[0]).get(0).getClass()){
                                    use((Card) ((List<?>) args[0]).get(0), attribute);
                                }
                            });
                        }
                    }
                }
            }
        };

        UtilMaps.getInstance().getCardEvents().forEach(event -> SignalManager.connect(event, listener, list -> onField));
    }

    public int getDestroyAfter() {
        return destroyAfter;
    }

    public void setDestroyAfter(int destroyAfter) {
        this.destroyAfter = destroyAfter;
    }

    public int getTurnPlayed() {
        return turnPlayed;
    }

    public void setTurnPlayed(int turnPlayed) {
        this.turnPlayed = turnPlayed;
    }
}