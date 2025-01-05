package dev.ian.assroids.entity.bullet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import dev.ian.assroids.asset.Asset;

/**
 * Created by: Ian Parcon
 * Date created: Sep 09, 2018
 * Time created: 3:06 PM
 */
public class LaserBullet extends Bullet {

    public LaserBullet(float angle, float x, float y) {
        super(angle, x, y);
        name = "Laser Bullet";
        color = Color.YELLOW;
        damage = 10;
        width = 15;
        height = 15;
        speed = 1000;
        bulletCoil = 0.05f;
        gunShotSound = Asset.instance().get(Asset.LASER_SOUND);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        sprite.setOriginCenter();
        sprite.setRotation(angle * MathUtils.radDeg);
        sprite.draw(batch);
    }
}
