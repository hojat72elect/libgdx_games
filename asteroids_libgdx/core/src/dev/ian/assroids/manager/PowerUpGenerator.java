package dev.ian.assroids.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

import dev.ian.assroids.entity.bullet.BulletType;
import dev.ian.assroids.entity.PowerUp;

/**
 * Created by: Ian Parcon
 * Date created: Sep 09, 2018
 * Time created: 11:43 PM
 */
public class PowerUpGenerator {

    private BulletType[] bulletTypes = {BulletType.EAGLE, BulletType.VENOM, BulletType.LAVA, BulletType.LASER};
    private List<PowerUp> powerUps;
    private float stateTime;
    private int time;

    public PowerUpGenerator() {
        powerUps = new ArrayList<PowerUp>();
    }

    public void generatePower(float delta) {
        stateTime += delta;
        if (stateTime >= 1.0f) {
            stateTime = 0;
            time++;
        }
        if (isTimePassed(10)) {
            BulletType bulletType = bulletTypes[MathUtils.random(bulletTypes.length - 1)];
            powerUps.add(new PowerUp(bulletType));
        }
    }

    public void draw(SpriteBatch batch) {
        for (PowerUp powerUp : powerUps) {
            powerUp.update(Gdx.graphics.getDeltaTime());
            powerUp.draw(batch);
        }
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    private boolean isTimePassed(int timePassed) {
        if (time >= timePassed) {
            time = 0;
            return true;
        }
        return false;
    }

}
