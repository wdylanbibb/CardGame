package Card_Game.Cards.CardTypes.Monster;

import Card_Game.Cards.Attackable;
import Card_Game.Cards.Card;
import com.google.gson.JsonObject;

public class Monster extends Card implements Attackable {

    private int atk;
    private int def;
    private boolean alive = true;

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

    private void die() {
        alive = false;
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
