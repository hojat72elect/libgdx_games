

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Identification;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes.Landmark;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class WaterOfAwareness extends WellWater {

    @Override
    protected boolean affectHero(Hero hero) {

        Sample.INSTANCE.play(Assets.Sounds.DRINK);
        emitter.parent.add(new Identification(hero.sprite.center()));

        hero.belongings.observe();

        for (int i = 0; i < Dungeon.level.length(); i++) {

            int terr = Dungeon.level.map[i];
            if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

                Dungeon.level.discover(i);

                if (Dungeon.level.heroFOV[i]) {
                    GameScene.discoverTile(i, terr);
                }
            }
        }

        Buff.affect(hero, Awareness.class, Awareness.DURATION);
        Dungeon.observe();

        Dungeon.hero.interrupt();

        GLog.p(Messages.get(this, "procced"));

        return true;
    }

    @Override
    protected Item affectItem(Item item, int pos) {
        if (item.isIdentified()) {
            return null;
        } else {
            ScrollOfIdentify.IDItem(item);

            Sample.INSTANCE.play(Assets.Sounds.DRINK);
            emitter.parent.add(new Identification(DungeonTilemap.tileCenterToWorld(pos)));

            return item;
        }
    }

    @Override
    public Landmark landmark() {
        return Landmark.WELL_OF_AWARENESS;
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.pour(Speck.factory(Speck.QUESTION), 0.3f);
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }
}
