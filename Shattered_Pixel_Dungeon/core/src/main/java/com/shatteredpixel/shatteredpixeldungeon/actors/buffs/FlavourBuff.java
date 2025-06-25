

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

//buff whose only internal logic is to wait and detach after a time.
public class FlavourBuff extends Buff {

    @Override
    public boolean act() {
        detach();
        return true;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }

    //flavour buffs can all just rely on cooldown()
    protected String dispTurns() {
        return dispTurns(visualcooldown());
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString((int) visualcooldown());
    }
}
