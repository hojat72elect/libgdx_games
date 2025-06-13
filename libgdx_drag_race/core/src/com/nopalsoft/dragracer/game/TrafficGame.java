package com.nopalsoft.dragracer.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.nopalsoft.dragracer.Assets;
import com.nopalsoft.dragracer.objects.EnemyCar;
import com.nopalsoft.dragracer.objects.InfiniteScrollBackground;
import com.nopalsoft.dragracer.objects.Coin;
import com.nopalsoft.dragracer.objects.PlayerCar;
import com.nopalsoft.dragracer.screens.Screens;

import java.util.Iterator;

public class TrafficGame extends Table {
    public static final int STATE_RUNNING = 0;
    public static final int STATE_GAMEOVER = 1;
    public int state;

    final float WIDTH = Screens.WORLD_WIDTH;
    final float HEIGHT = Screens.WORLD_HEIGHT;

    public final static int NUM_COINS_FOR_SUPER_SPEED = 10;
    public int numCoinsForSuperSpeed;
    boolean canSuperSpeed;

    final float TIME_TO_SPAWN_CAR = 2;
    float timeToSpawnCar;

    final float TIME_TO_SPAWN_COIN = 1f;
    float timeToSpawnCoin;

    final float DURATION_SUPER_SPEED = 5;
    float durationSuperSpeed = 0;
    boolean isSuperSpeed;
    float currentSpeed = 5;

    float score;
    int coins;

    private final InfiniteScrollBackground backgroundRoad;
    public PlayerCar oCar;
    private final Array<EnemyCar> arrayEnemyCars;
    private final Array<Coin> arrayCoins;

    public final float lane2 = 390;
    public final float lane1 = 240;
    public final float lane0 = 90;

    public TrafficGame() {
        setBounds(0, 0, WIDTH, HEIGHT);
        setClip(true);
        backgroundRoad = new InfiniteScrollBackground(getWidth(), getHeight());
        addActor(backgroundRoad);

        oCar = new PlayerCar(this);
        addActor(oCar);
        arrayEnemyCars = new Array<>();
        arrayCoins = new Array<>();

        state = STATE_RUNNING;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        durationSuperSpeed += delta;
        if (durationSuperSpeed >= DURATION_SUPER_SPEED) {
            stopSuperSpeed();
        }

        if (numCoinsForSuperSpeed >= NUM_COINS_FOR_SUPER_SPEED) {
            canSuperSpeed = true;
        }

        updateCar();
        updateEnemyCar(delta);
        updateCoins(delta);
        score += delta * currentSpeed;

        if (oCar.state == PlayerCar.STATE_DEAD) {
            state = STATE_GAMEOVER;
        }
    }

    private void updateCar() {}

    private void updateEnemyCar(float delta) {
        // First I create a car if necessary

        timeToSpawnCar += delta;
        if (timeToSpawnCar >= TIME_TO_SPAWN_CAR) {
            timeToSpawnCar -= TIME_TO_SPAWN_CAR;
            spawnCar();
        }

        Iterator<EnemyCar> iterator = arrayEnemyCars.iterator();
        while (iterator.hasNext()) {
            EnemyCar enemyCar = iterator.next();
            if (enemyCar.getBounds().y + enemyCar.getHeight() <= 0) {
                iterator.remove();
                removeActor(enemyCar);
                continue;
            }

            if (isSuperSpeed)
                enemyCar.setSpeed();
        }

        // Then I check the collisions with the player
        iterator = arrayEnemyCars.iterator();
        while (iterator.hasNext()) {
            EnemyCar enemyCar = iterator.next();
            if (enemyCar.getBounds().overlaps(oCar.getBounds())) {
                iterator.remove();

                if (enemyCar.getX() > oCar.getX()) {
                    enemyCar.crash(true, enemyCar.getY() > oCar.getY());
                    if (!isSuperSpeed)
                        oCar.crash(false, true);
                } else {
                    enemyCar.crash(false, enemyCar.getY() > oCar.getY());
                    if (!isSuperSpeed)
                        oCar.crash(true, true);
                }
                Assets.soundCrash.stop();
                Assets.playSound(Assets.soundCrash);
            }
        }
    }

    private void updateCoins(float delta) {

        timeToSpawnCoin += delta;

        if (timeToSpawnCoin >= TIME_TO_SPAWN_COIN) {
            timeToSpawnCoin -= TIME_TO_SPAWN_COIN;
            spawnCoin();
        }

        Iterator<Coin> iterator = arrayCoins.iterator();
        while (iterator.hasNext()) {
            Coin obj = iterator.next();
            if (obj.getBounds().y + obj.getHeight() <= 0) {
                iterator.remove();
                removeActor(obj);
                continue;
            }
            // I see if they are touching my car
            if (oCar.getBounds().overlaps(obj.getBounds())) {
                iterator.remove();
                removeActor(obj);
                coins++;
                numCoinsForSuperSpeed++;
                continue;
            }

            // I see if it's touching an enemy
            for (EnemyCar enemyCar : arrayEnemyCars) {
                if (obj.getBounds().overlaps(enemyCar.getBounds())) {
                    iterator.remove();
                    removeActor(obj);
                    break;
                }
            }

            if (isSuperSpeed)
                obj.setSpeed();
        }
    }

    public void setSuperSpeed() {
        canSuperSpeed = false;
        durationSuperSpeed = 0;
        isSuperSpeed = true;
        currentSpeed = 30;
        numCoinsForSuperSpeed = 0;
        backgroundRoad.setSpeed();
    }

    public void stopSuperSpeed() {
        isSuperSpeed = false;
        currentSpeed = 5;
        backgroundRoad.stopSpeed();
    }

    private void spawnCar() {
        int lane = MathUtils.random(0, 2);
        float x = 0;
        if (lane == 0)
            x = lane0;
        if (lane == 1)
            x = lane1;
        if (lane == 2)
            x = lane2;
        EnemyCar enemyCar = new EnemyCar(x, getHeight());
        arrayEnemyCars.add(enemyCar);
        addActor(enemyCar);
    }

    private void spawnCoin() {
        int lane = MathUtils.random(0, 2);
        float x = 0;
        if (lane == 0)
            x = lane0;
        if (lane == 1)
            x = lane1;
        if (lane == 2)
            x = lane2;
        Coin obj = new Coin(x, getHeight());
        arrayCoins.add(obj);
        addActor(obj);
    }
}
