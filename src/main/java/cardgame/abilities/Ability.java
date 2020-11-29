package cardgame.abilities;

import cardgame.cards.Card;
import com.google.gson.JsonArray;

public abstract class Ability implements Runnable {

    public AbilityRunListener runListener = AbilityRunListener.USE;
    public Card card;

    public Ability(JsonArray array, Card card) {
        this.card = card;
    }

    @Override
    public abstract void run();

    public AbilityRunListener getRunListener() {
        return runListener;
    }

    public void setRunListener(AbilityRunListener runScen) {
        this.runListener = runScen;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}