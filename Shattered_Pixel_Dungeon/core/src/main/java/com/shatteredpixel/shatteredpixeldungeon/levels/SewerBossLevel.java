

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.Builder;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.FigureEightBuilder;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.SewerPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret.RatKingRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.sewerboss.GooBossRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.sewerboss.SewerBossEntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.sewerboss.SewerBossExitRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SewerBossLevel extends SewerLevel {

    {
        color1 = 0x48763c;
        color2 = 0x59994a;
    }

    @Override
    public void playLevelMusic() {
        if (locked) {
            Music.INSTANCE.play(Assets.Music.SEWERS_BOSS, true);
            return;
        }

        boolean gooAlive = false;
        for (Mob m : mobs) {
            if (m instanceof Goo) {
                gooAlive = true;
                break;
            }
        }

        if (gooAlive) {
            Music.INSTANCE.end();
        } else {
            Music.INSTANCE.playTracks(SewerLevel.SEWER_TRACK_LIST, SewerLevel.SEWER_TRACK_CHANCES, false);
        }
    }

    @Override
    protected ArrayList<Room> initRooms() {
        ArrayList<Room> initRooms = new ArrayList<>();

        initRooms.add(roomEntrance = new SewerBossEntranceRoom());
        initRooms.add(roomExit = new SewerBossExitRoom());

        int standards = standardRooms(true);
        for (int i = 0; i < standards; i++) {
            StandardRoom s = StandardRoom.createRoom();
            //force to normal size
            s.setSizeCat(0, 0);
            initRooms.add(s);
        }

        GooBossRoom gooRoom = GooBossRoom.randomGooRoom();
        initRooms.add(gooRoom);
        ((FigureEightBuilder) builder).setLandmarkRoom(gooRoom);
        initRooms.add(new RatKingRoom());
        return initRooms;
    }

    @Override
    protected int standardRooms(boolean forceMax) {
        if (forceMax) return 3;
        //2 to 3, average 2.5
        return 2 + Random.chances(new float[]{1, 1});
    }

    protected Builder builder() {
        return new FigureEightBuilder()
                .setLoopShape(2, Random.Float(0.3f, 0.8f), 0f)
                .setPathLength(1f, new float[]{1})
                .setTunnelLength(new float[]{1, 2}, new float[]{1});
    }

    @Override
    protected Painter painter() {
        return new SewerPainter()
                .setWater(0.50f, 5)
                .setGrass(0.20f, 4)
                .setTraps(nTraps(), trapClasses(), trapChances());
    }

    protected int nTraps() {
        return 0;
    }

    @Override
    protected void createMobs() {
    }

    public Actor addRespawner() {
        return null;
    }

    @Override
    protected void createItems() {
        Random.pushGenerator(Random.Long());
        ArrayList<Item> bonesItems = Bones.get();
        if (bonesItems != null) {
            int pos;
            do {
                pos = pointToCell(roomEntrance.random());
            } while (pos == entrance() || solid[pos]);
            for (Item i : bonesItems) {
                drop(i, pos).setHauntedIfCursed().type = Heap.Type.REMAINS;
            }
        }
        Random.popGenerator();
    }

    @Override
    public int randomRespawnCell(Char ch) {
        ArrayList<Integer> candidates = new ArrayList<>();
        for (Point p : roomEntrance.getPoints()) {
            int cell = pointToCell(p);
            if (passable[cell]
                    && roomEntrance.inside(p)
                    && Actor.findChar(cell) == null
                    && (!Char.hasProp(ch, Char.Property.LARGE) || openSpace[cell])) {
                candidates.add(cell);
            }
        }

        if (candidates.isEmpty()) {
            return -1;
        } else {
            return Random.element(candidates);
        }
    }


    public void seal() {
        if (!locked) {

            super.seal();

            Statistics.qualifiedForBossChallengeBadge = true;

            set(entrance(), Terrain.WATER);
            GameScene.updateMap(entrance());
            GameScene.ripple(entrance());

            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    Music.INSTANCE.play(Assets.Music.SEWERS_BOSS, true);
                }
            });
        }
    }

    public void unseal() {
        if (locked) {

            super.unseal();

            set(entrance(), Terrain.ENTRANCE);
            GameScene.updateMap(entrance());

            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    Music.INSTANCE.fadeOut(5f, new Callback() {
                        @Override
                        public void call() {
                            Music.INSTANCE.end();
                        }
                    });
                }
            });
        }
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        if (map[exit() - 1] != Terrain.WALL_DECO) visuals.add(new PrisonLevel.Torch(exit() - 1));
        if (map[exit() + 1] != Terrain.WALL_DECO) visuals.add(new PrisonLevel.Torch(exit() + 1));
        return visuals;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        roomExit = roomEntrance;
    }
}
