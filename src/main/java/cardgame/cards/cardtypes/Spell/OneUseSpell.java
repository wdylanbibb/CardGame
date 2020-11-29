package cardgame.cards.cardtypes.Spell;

import cardgame.abilities.AbilityRunListener;
import cardgame.abilities.Ability;

import javax.annotation.Nonnull;

public class OneUseSpell extends Spell {
    public OneUseSpell(@Nonnull String name, int cost, String description) {
        super(name, cost, description);
    }

    @Override
    public boolean addAbility(Ability ability) {
        boolean ret = super.addAbility(ability);
        ability.setRunListener(AbilityRunListener.PLAY);
        return ret;
    }
}
