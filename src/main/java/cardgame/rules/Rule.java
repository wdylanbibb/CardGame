package cardgame.rules;

import cardgame.EnumManager;
import cardgame.cards.Card;
import cardgame.Player;
import cardgame.UtilMaps;

public class Rule {

    private String attr;
    private EnumManager.CompareAttr compareAttr;
    private String compare;
    private int val;
    private Player player;
    private EnumManager.PlayerEffect playersToAffect;
    private String players;

    public Rule(String attr, EnumManager.CompareAttr compareAttr, int val, Player player, EnumManager.PlayerEffect playersToAffect) {
        this.attr = attr;
        this.compareAttr = compareAttr;
        this.val = val;
        this.player = player;
        this.playersToAffect = playersToAffect;
    }

    public boolean check(Card card) {
        Integer valToCompare = RuleUtils.getCompareVal(attr, card, player, playersToAffect);
        if (valToCompare == null) return true;
        return RuleUtils.compare(valToCompare, compareAttr, val);
    }

    public EnumManager.CompareAttr getCompareAttr() {
        return compareAttr;
    }

    public EnumManager.PlayerEffect getPlayersToAffect() {
        return playersToAffect;
    }

    public void setCompareAttr(EnumManager.CompareAttr compareAttr) {
        this.compareAttr = compareAttr;
    }

    public void setPlayersToAffect(EnumManager.PlayerEffect playersToAffect) {
        this.playersToAffect = playersToAffect;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setEnums() {
        compareAttr = UtilMaps.getInstance().getComparisonByString(compare);
        playersToAffect = UtilMaps.getInstance().getPlayerByString(players);
    }

    public int getVal() {
        return val;
    }

    public String getAttr() {
        return attr;
    }
}
