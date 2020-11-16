package Card_Game;

import Card_Game.CardContainers.Deck;
import Card_Game.CardContainers.Discard;
import Card_Game.CardContainers.Field;
import Card_Game.CardContainers.Hand;
import Card_Game.Cards.Card;

import java.io.IOException;
import java.util.stream.IntStream;

public class Player {

    public static final int STARTING_HAND = 5;
    public static int MAX_MANA = 3;
    private int mana;

    private final Deck deck;
    private final Hand hand;
    private final Discard discard;
    private final Field field;

    public Player(String deckName) throws IOException {
        hand = new Hand();
        deck = new Deck();
        DeckFiller.fillDeck(deckName, deck, this);
        deck.shuffle();
        discard = new Discard();
        field = new Field(5);
        mana = MAX_MANA;
        IntStream.rangeClosed(1, STARTING_HAND).forEach(i -> initDraw());
    }

    private void initDraw() {
        hand.add(deck.draw());
    }

    public void draw() {
        hand.add(deck.draw());
        mana--;
    }

    public boolean play(Card card) {
        if(card.getCost() > mana){
            return false;
        }else{
            hand.play(card);
            mana -= card.getCost();
            return true;
        }
    }

    public void endTurn() {
        mana = 0;
        field.checkField();
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
}
