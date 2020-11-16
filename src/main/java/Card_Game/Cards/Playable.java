package Card_Game.Cards;

public interface Playable {
    default void play() {}
    void use(Playable target);
}
