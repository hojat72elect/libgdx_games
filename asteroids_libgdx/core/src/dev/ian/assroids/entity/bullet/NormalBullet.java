package dev.ian.assroids.entity.bullet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import dev.ian.assroids.asset.Asset;


/**
 * Created by: Ian Parcon
 * Date created: Sep 09, 2018
 * Time created: 2:51 PM
 */
public class NormalBullet extends Bullet {

    public NormalBullet(float angle, float x, float y) {
        super(angle, x, y);
        damage = 40;
        width = 25;
        height = 25;
        color = Color.YELLOW;
        gunShotSound = Asset.instance().get(Asset.NORMAL_GUN_SHOT);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        sprite.setOriginCenter();
        sprite.setRotation(angle * MathUtils.radDeg);
        sprite.draw(batch);
    }
}
