package Card_Game.Cards;

import Card_Game.Abilities.AbilityRunListener;
import Card_Game.Abilities.Ability;
import Card_Game.GameComponents;
import Card_Game.Player;
import Card_Game.Rules.Rule;
import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Card implements Playable{

    @Nonnull
    private String name;
    private int cost;
    private Player player;
    private String description;
    private String image;

    private List<Ability> abils;

    private List<Rule> ruleList = new ArrayList<>();


    public Card(@Nonnull String name, int cost, String description) {
        this.name = name;
        this.cost = cost;
        this.description = description;
    }

    @Override
    public boolean use(Playable target, Class<? extends Ability> cls) {
        Ability ability = getAbility(cls);
        if (ability != null) {
            ability.run();
            return true;
        }
        return false;
    }

    @Override
    public void play() {
        ruleList.forEach(rule -> GameComponents.getInstance().addRule(rule));
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

    public List<Ability> getAbilitiesFromListener(AbilityRunListener scene){
        return abils.stream().filter(ability -> ability.getRunListener()==scene).collect(Collectors.toList());
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
        ruleList.forEach(rule -> GameComponents.getInstance().removeRule(rule));
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
}
