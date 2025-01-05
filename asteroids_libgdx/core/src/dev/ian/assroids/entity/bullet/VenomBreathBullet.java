package dev.ian.assroids.entity.bullet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import dev.ian.assroids.asset.Asset;

/**
 * Created by: Ian Parcon
 * Date created: Sep 09, 2018
 * Time created: 1:34 PM
 */
public class VenomBreathBullet extends Bullet {

    public VenomBreathBullet(float angle, float x, float y) {
        super(angle, x, y);
        name = "Venom breath bullet";
        damage = 20;
        width = 32;
        height = 32;
        color = Color.GREEN;
        gunShotSound = Asset.instance().get(Asset.PLASMA_GUN_SHOT);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        sprite.rotate(20);
        sprite.draw(batch);
    }
}
