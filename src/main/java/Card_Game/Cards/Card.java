package Card_Game.Cards;

import Card_Game.Abilities.Ability;
import Card_Game.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Card implements Playable{

    @Nonnull
    private String name;
    private int cost;
    private Player player;
    private String description;
//    private Image image;
    private String imageName;

    private List<Ability> abils = new ArrayList<>();


    public Card(@Nonnull String name, int cost, String description/*, Image image*/) {
        this.name = name;
        this.cost = cost;
        this.description = description;
//        this.image = image;
    }

    @Override
    public void use(Playable target) {

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

//    public Image getImage() {
//        return image;
//    }

//    public void setImage(Image image) {
//        this.image = image;
//    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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
}
