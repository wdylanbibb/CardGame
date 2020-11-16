package Card_Game.Cards;

public class Monster extends Card {

    private int atk;
    private int def;
    private boolean alive = true;

    public Monster(String name, int cost, int atk, int def, String description) {
        super(name, cost, description);
        this.atk = atk;
        this.def = def;
    }

    @Override
    public void use(Playable target) {
        if (target instanceof Monster) attack((Monster) target);
    }

    private void attack(Monster target) {
        if (atk > target.def) {
            target.die();
        }
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
}
