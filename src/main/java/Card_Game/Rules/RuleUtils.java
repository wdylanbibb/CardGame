package Card_Game.Rules;

import Card_Game.Cards.Card;
import Card_Game.Cards.CardTypes.Monster.Monster;
import Card_Game.GameComponents;
import Card_Game.Player;

public class RuleUtils {

    public enum CompareAttr {
        LESS_THAN, GREATER_THAN, EQUAL_TO, NOT_EQUAL, LESS_OR_EQUAL, GREATER_OR_EQUAL, NONE
    }

    public enum PlayerEffect {
        SELF, NOT_SELF, ALL, NONE
    }

    static Integer getCompareVal(String attr, Card card, Player player, PlayerEffect playersToAffect) {
        Integer valToCompare = null;
        switch (attr) {
            case "atk":
                if (card instanceof Monster) {
                    valToCompare = ((Monster) card).getAtk();
                } else {
                    return null;
                }
                break;
            case "def":
                if (card instanceof Monster) {
                    valToCompare = ((Monster) card).getDef();
                } else {
                    return null;
                }
                break;
            case "cost":
                valToCompare = card.getCost();
                break;
            case "cards":
                switch (playersToAffect) {
                    case SELF:
                        valToCompare = GameComponents.getInstance().getSelfFieldCards(player).size();
                        if (card.getPlayer() != player) {
                            return null;
                        }
                        break;
                    case NOT_SELF:
                        valToCompare = GameComponents.getInstance().getAllFieldCards().size() - GameComponents.getInstance().getSelfFieldCards(player).size();
                        if (card.getPlayer() == player) {
                            return null;
                        }
                        break;
                    case ALL:
                        valToCompare = GameComponents.getInstance().getAllFieldCards().size();
                        break;
                    case NONE:
                        return null;
                }
                break;
            default:
                return null;
        }
        return valToCompare;
    }

    static boolean compare(Integer valToCompare, CompareAttr compareAttr, int val) {
        switch (compareAttr) {
            case LESS_THAN:
                return valToCompare < val;
            case GREATER_THAN:
                return valToCompare > val;
            case EQUAL_TO:
                return valToCompare == val;
            case NOT_EQUAL:
                return valToCompare != val;
            case LESS_OR_EQUAL:
                return valToCompare <= val;
            case GREATER_OR_EQUAL:
                return valToCompare >= val;
            case NONE:
                return true;
        }
        return true;
    }
}
