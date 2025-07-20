package com.nopalsoft.donttap.game_objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.donttap.Assets;
import com.nopalsoft.donttap.game.WorldGame;

import java.util.LinkedHashMap;

public class Tile extends Actor implements Poolable {
    public static final int STATE_NORMAL = 0;
    public static final int STATE_TAP = 1;
    public int state;

    public static float WIDTH = 120;
    public static float HEIGHT = 180;

    static public final int TYPE_BAD = 0;
    static public final int TYPE_GOOD = 1;

    public int type;
    public boolean canBeTap;// You can't touch it until you touch the one below it

    final static LinkedHashMap<Integer, Vector2> mapPositions = new LinkedHashMap<>();

    static {
        // The ones that have -1 are the ones that are further down where they cannot be seen.
        mapPositions.put(0, new Vector2(0, 720));
        mapPositions.put(1, new Vector2(120, 720));
        mapPositions.put(2, new Vector2(240, 720));
        mapPositions.put(3, new Vector2(360, 720));
        mapPositions.put(4, new Vector2(0, 540));
        mapPositions.put(5, new Vector2(120, 540));
        mapPositions.put(6, new Vector2(240, 540));
        mapPositions.put(7, new Vector2(360, 540));
        mapPositions.put(8, new Vector2(0, 360));
        mapPositions.put(9, new Vector2(120, 360));
        mapPositions.put(10, new Vector2(240, 360));
        mapPositions.put(11, new Vector2(360, 360));
        mapPositions.put(12, new Vector2(0, 180));
        mapPositions.put(13, new Vector2(120, 180));
        mapPositions.put(14, new Vector2(240, 180));
        mapPositions.put(15, new Vector2(360, 180));
        mapPositions.put(16, new Vector2(0, 0));
        mapPositions.put(17, new Vector2(120, 0));
        mapPositions.put(18, new Vector2(240, 0));
        mapPositions.put(19, new Vector2(360, 0));
        mapPositions.put(20, new Vector2(0, -180));
        mapPositions.put(21, new Vector2(120, -180));
        mapPositions.put(22, new Vector2(240, -180));
        mapPositions.put(23, new Vector2(360, -180));
    }

    public int tablePosition;
    TextureRegion keyframe;
    WorldGame worldGame;

    public Tile() {
        setSize(WIDTH, HEIGHT);
        setOrigin(WIDTH / 2f, HEIGHT / 2f);
        addListener(inputListener);
    }

    public void init(WorldGame oWorld, int tablePosition, boolean canStep, boolean isFirstRow) {
        this.tablePosition = tablePosition;
        this.worldGame = oWorld;
        setPosition(mapPositions.get(tablePosition).x, mapPositions.get(tablePosition).y);

        clearActions();
        getColor().a = 1;
        if (!canStep) {
            type = TYPE_BAD;
            keyframe = Assets.tileBlanco;
        } else {
            switch (MathUtils.random(4)) {
                case 0:

                    keyframe = Assets.tileRojo;
                    break;
                case 1:
                    keyframe = Assets.tileAmarillo;
                    break;
                case 2:
                    keyframe = Assets.tileAzul;
                    break;
                case 3:
                    keyframe = Assets.tileMorado;
                    break;
                case 4:
                    keyframe = Assets.tileNaranja;
                    break;
            }
            type = TYPE_GOOD;
            addAction(Actions.forever(Actions.sequence(Actions.alpha(.6f, .5f), Actions.alpha(1, .35f))));
        }

        if (isFirstRow && canStep) {
            canBeTap = true;
            state = Tile.STATE_TAP;
        } else {
            setScale(1f);
            canBeTap = false;
            state = STATE_NORMAL;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, getColor().a);
        batch.draw(keyframe, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

        if (state == STATE_TAP) {
            TextureRegion step;
            if (type == TYPE_GOOD)
                step = Assets.step1;
            else
                step = Assets.wrong;
            batch.draw(step, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }

        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1);
    }

    InputListener inputListener = new InputListener() {
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (worldGame.state == WorldGame.STATE_READY) {
                worldGame.state = WorldGame.STATE_RUNNING;
            }

            if (worldGame.state == WorldGame.STATE_RUNNING) {
                if (state == STATE_NORMAL && canBeTap || type == TYPE_BAD) {
                    state = STATE_TAP;

                    switch (worldGame.mode) {
                        case WorldGame.MODE_CLASSIC:
                            worldGame.addRow();
                            if (type == TYPE_GOOD) {
                                worldGame.score--;
                                Assets.playTapSound();
                            } else {
                                Assets.soundWrong.play();
                            }
                            break;
                        case WorldGame.MODE_TIME:
                            worldGame.addRow();
                            if (type == TYPE_GOOD) {
                                worldGame.score++;
                                Assets.playTapSound();
                            } else {
                                Assets.soundWrong.play();
                            }
                            break;

                        case WorldGame.MODE_ENDLESS:
                            if (type == TYPE_GOOD) {
                                worldGame.score++;
                                Assets.playTapSound();
                            } else {
                                Assets.soundWrong.play();
                            }
                            break;
                    }
                }
            }
            return true;
        }
    };

    public void moveUp() {
        tablePosition -= 4;
        if (tablePosition < 0) {
            return;
        }
        addAction(Actions.moveTo(mapPositions.get(tablePosition).x, mapPositions.get(tablePosition).y, .75f));
    }

    public void moveDown() {
        tablePosition += 4;
        if (tablePosition > 23) {
            return;
        }

        float time = .1f;
        if (worldGame.mode == WorldGame.MODE_ENDLESS)
            time = worldGame.TIME_TO_SPWAN_ROW;
        addAction(Actions.moveTo(mapPositions.get(tablePosition).x, mapPositions.get(tablePosition).y, time));
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }
}
