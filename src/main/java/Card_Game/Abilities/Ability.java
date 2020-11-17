package Card_Game.Abilities;

import Card_Game.Cards.Card;
import com.google.gson.JsonArray;

public class Ability implements Runnable {

    AbilRunScen runScen = AbilRunScen.USE;
    Card card;

    public Ability(JsonArray array, Card card) {
        this.card = card;
    }

    @Override
    public void run() {

    }

    public AbilRunScen getRunScen() {
        return runScen;
    }

    public void setRunScen(AbilRunScen runScen) {
        this.runScen = runScen;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}