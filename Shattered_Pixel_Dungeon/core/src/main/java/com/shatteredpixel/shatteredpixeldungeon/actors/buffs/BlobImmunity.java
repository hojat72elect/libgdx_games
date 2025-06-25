

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blizzard;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Inferno;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Regrowth;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SmokeScreen;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StormCloud;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Web;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.MagicalFireRoom;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class BlobImmunity extends FlavourBuff {

    {
        type = buffType.POSITIVE;
    }

    public static final float DURATION = 20f;

    @Override
    public int icon() {
        return BuffIndicator.IMMUNITY;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    {
        //all harmful blobs
        immunities.add(Blizzard.class);
        immunities.add(ConfusionGas.class);
        immunities.add(CorrosiveGas.class);
        immunities.add(Electricity.class);
        immunities.add(Fire.class);
        immunities.add(MagicalFireRoom.EternalFire.class);
        immunities.add(Freezing.class);
        immunities.add(Inferno.class);
        immunities.add(ParalyticGas.class);
        immunities.add(Regrowth.class);
        immunities.add(SmokeScreen.class);
        immunities.add(StenchGas.class);
        immunities.add(StormCloud.class);
        immunities.add(ToxicGas.class);
        immunities.add(Web.class);

        immunities.add(Tengu.FireAbility.FireBlob.class);
    }
}
