package cardgame.cards.cardtypes.Monster;

import cardgame.cards.Attackable;
import cardgame.cards.Card;
import cardgame.emissions.Signal;
import cardgame.emissions.SignalManager;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Monster extends Card implements Attackable {

    private int atk;
    private int def;
    private boolean alive = true;
    private final Signal cardDeath = SignalManager.createSignal("ondeath", ArrayList.class);
    private final Signal onDestroy = SignalManager.createSignal("ondestroy", ArrayList.class);

    public Monster(String name, int cost, int atk, int def, String description) {
        super(name, cost, description);
        this.atk = atk;
        this.def = def;
    }

    @Override
    public boolean attack(Monster target) {
        target.setDef(target.getDef() - getAtk());
        if (target.getDef() <= 0) {
            target.die();
            return true;
        }
        return false;
    }

    public void die() {
        alive = false;
        cardDeath.emit(new ArrayList<>(List.of(this)));
        onDestroy.emit(new ArrayList<>(List.of(this)));
    }

    public boolean isDead() {
        return !alive;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public void setParams(JsonObject params) {
        alive = true;
    }
}
