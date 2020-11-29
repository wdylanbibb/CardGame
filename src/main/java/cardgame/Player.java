package cardgame;

import cardgame.cardcontainers.Deck;
import cardgame.cardcontainers.Discard;
import cardgame.cardcontainers.Field;
import cardgame.cardcontainers.Hand;
import cardgame.cards.Card;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player {

    public static final int STARTING_HAND = 5;
    public static final int FIELD_LEN = 5;
    public static int MAX_MANA = 3;
    private int max_health = 20;
    private int init_draw = 1;
    private int mana;
    private int health;

    private final Deck deck;
    private final Hand hand;
    private final Discard discard;
    private final Field field;

    public Player(String deckName) throws IOException {
        hand = new Hand();
        deck = new Deck();
        JsonAccessor.fillDeck(deckName, deck, this);
        deck.shuffle();
        discard = new Discard();
        field = new Field(FIELD_LEN);
        mana = MAX_MANA;
        health = max_health;
        multiNoManaDraw(STARTING_HAND);
    }

    public List<Card> multiNoManaDraw(int num) {
        ArrayList<Card> returnList = new ArrayList<>();
        for(int i=0;i<num;i++) {
            Card card = deck.draw();
            hand.add(card);
            returnList.add(card);
        }
        return returnList;
    }

    public Card draw() {
        if(deck.size() > 0) {
            hand.add(deck.draw());
            mana--;
            return hand.get(hand.size() - 1);
        }else{
            return null;
        }
    }

    public boolean play(Card card, int num) {
        if(card.getCost() > mana){
            return false;
        }else{
            if (!field.play(card, num, hand)) return false;
            mana -= card.getCost();
            return true;
        }
    }

    public void endTurn() {
        checkForDead();
        if (deck.isEmpty()) {
            deck.addAll(discard.reshuffle());
            deck.shuffle();
        }
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void setMaxMana(int maxMana) {
        MAX_MANA = maxMana;
    }

    public Deck getDeck() {
        return deck;
    }

    public Discard getDiscard() {
        return discard;
    }

    public Field getField() {
        return field;
    }

    public Hand getHand() {
        return hand;
    }

    public void checkForDead() {
        discard.addAll(field.checkField());
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return max_health;
    }

    public void setMaxHealth(int max_health) {
        this.max_health = max_health;
    }

    public int getInitDraw() {
        return init_draw;
    }

    public void setInitDraw(int init_draw) {
        this.init_draw = init_draw;
    }

    public void addHealth(int health){
        this.health+=health;
    }

    public void removeHealth(int health){
        this.health-=health;
    }

    public void addMaxHealth(int max_health){
        this.max_health+=max_health;
    }

    public void removeMaxHealth(int max_health){
        this.max_health-=max_health;
    }
}
