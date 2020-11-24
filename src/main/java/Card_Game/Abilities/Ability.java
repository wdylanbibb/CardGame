package Card_Game.Abilities;

import Card_Game.Cards.Card;
import com.google.gson.JsonArray;

public abstract class Ability implements Runnable {

    public AbilRunScene runScen = AbilRunScene.USE;
    public Card card;

    public Ability(JsonArray array, Card card) {
        this.card = card;
    }

    @Override
    public abstract void run();

    public AbilRunScene getRunScene() {
        return runScen;
    }

    public void setRunScene(AbilRunScene runScen) {
        this.runScen = runScen;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}