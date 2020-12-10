package cardgame.abilities;

import cardgame.EnumManager;
import cardgame.cards.Card;
import com.google.gson.JsonObject;

import javax.annotation.Nullable;

public abstract class Ability implements Runnable {

    public EnumManager.CardEvent cardEvent = EnumManager.CardEvent.USE;
    private EnumManager.PlayerEffect targetType = EnumManager.PlayerEffect.NONE;
    private EnumManager.CardType cardType = EnumManager.CardType.ALL;
    public Card card;

    public Ability(JsonObject args, Card card) {
        this.card = card;
    }

    @Override
    public abstract void run(@Nullable Card target);

    public EnumManager.CardEvent getCardEvent() {
        return cardEvent;
    }

    public void setCardEvent(EnumManager.CardEvent cardEvent) {
        this.cardEvent = cardEvent;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }


    public EnumManager.PlayerEffect getTargetType() {
        return targetType;
    }

    public void setTargetType(EnumManager.PlayerEffect targetType) {
        this.targetType = targetType;
    }

    public EnumManager.CardType getCardType() {
        return cardType;
    }

    public void setCardType(EnumManager.CardType cardType) {
        this.cardType = cardType;
    }

    public abstract String getName();
}