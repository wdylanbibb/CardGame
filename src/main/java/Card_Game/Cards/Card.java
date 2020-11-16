package Card_Game.Cards;

import Card_Game.Player;

import javax.annotation.Nonnull;

public class Card implements Playable{

    @Nonnull
    private String name;
    private int cost;
    private Player player;

    public Card( @Nonnull String name, int cost) {
        this.name = name;
        this.cost = cost;
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
}
