package Card_Game.Rules;

import Card_Game.Cards.Card;
import Card_Game.Player;
import Card_Game.UtilMaps;

public class Rule {

    private String attr;
    private RuleUtils.CompareAttr compareAttr;
    private String compare;
    private int val;
    private Player player;
    private RuleUtils.PlayerEffect playersToAffect;
    private String players;

    public Rule(String attr, RuleUtils.CompareAttr compareAttr, int val, Player player, RuleUtils.PlayerEffect playersToAffect) {
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

    public RuleUtils.CompareAttr getCompareAttr() {
        return compareAttr;
    }

    public RuleUtils.PlayerEffect getPlayersToAffect() {
        return playersToAffect;
    }

    public void setCompareAttr(RuleUtils.CompareAttr compareAttr) {
        this.compareAttr = compareAttr;
    }

    public void setPlayersToAffect(RuleUtils.PlayerEffect playersToAffect) {
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
}
