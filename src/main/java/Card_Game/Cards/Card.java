package Card_Game.Cards;

import Card_Game.Player;

import javax.annotation.Nonnull;
import java.awt.*;

public class Card implements Playable{

    @Nonnull
    private String name;
    private int cost;
    private Player player;
    private String description;
//    private Image image;
    private String imageName;

    public Card(@Nonnull String name, int cost, String description, Image image) {
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
}
