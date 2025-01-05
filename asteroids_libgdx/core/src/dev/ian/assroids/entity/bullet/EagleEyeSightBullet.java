package dev.ian.assroids.entity.bullet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import dev.ian.assroids.asset.Asset;

/**
 * Created by: Ian Parcon
 * Date created: Sep 09, 2018
 * Time created: 1:35 PM
 */
public class EagleEyeSightBullet extends Bullet {

    public EagleEyeSightBullet(float angle, float x, float y) {
        super(angle, x, y);
        name = "Eagle Eye Sight";
        damage = 30;
        width = 25;
        height = 25;
        color = Color.PINK;
        gunShotSound = Asset.instance().get(Asset.EAGLE_GUN_SHOT);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        sprite.rotate(angle * MathUtils.radDeg);
        sprite.setOriginCenter();
        sprite.draw(batch);
    }
}
