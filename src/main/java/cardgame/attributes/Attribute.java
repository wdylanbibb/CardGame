package cardgame.attributes;

// Cards can have attributes that cause certain things to happen when events on the board are fulfilled
// e.x. card gains defense when a spell is played by the other player

import cardgame.EnumManager;
import cardgame.abilities.Runnable;
import cardgame.cards.Card;
import com.google.gson.JsonObject;

public abstract class Attribute implements Runnable {

    public Card card;
    public EnumManager.PlayerEffect target;
    public EnumManager.CardEvent event;
    public EnumManager.CardType type;

    public Attribute(JsonObject args, Card card){
         this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public EnumManager.PlayerEffect getTarget() {
        return target;
    }

    @Override
    public abstract void run(Card target);

    public void setTarget(EnumManager.PlayerEffect target) {
        this.target = target;
    }

    public EnumManager.CardEvent getEvent() {
        return event;
    }

    public void setEvent(EnumManager.CardEvent event) {
        this.event = event;
    }

    public EnumManager.CardType getType() {
        return type;
    }

    public void setType(EnumManager.CardType type) {
        this.type = type;
    }

    public abstract String getName();
}
