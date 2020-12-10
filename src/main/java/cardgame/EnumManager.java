package cardgame;

public class EnumManager {

    public enum PlayerEffect {
        SELF, NOT_SELF, ALL, NONE
    }

    public enum CompareAttr {
        LESS_THAN, GREATER_THAN, EQUAL_TO, NOT_EQUAL, LESS_OR_EQUAL, GREATER_OR_EQUAL, NONE
    }

    public enum CardEvent {
        PLAY, DEATH, TURNSTART, TURNEND, USE, DESTROY, ATTACK, ATTACKED, ABILITYUSED, ATTRIBUTEUSED, NEWRULE, ENDRULE, NONE
    }

    public enum CardType {
        MONSTER, OUSPELL, SPELL, ALL
    }
}
