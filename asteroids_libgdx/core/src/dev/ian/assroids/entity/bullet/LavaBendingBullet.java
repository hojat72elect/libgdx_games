package dev.ian.assroids.entity.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import dev.ian.assroids.asset.Asset;
import dev.ian.assroids.factory.BulletFactory;

/**
 * Created by: Ian Parcon
 * Date created: Sep 09, 2018
 * Time created: 1:34 PM
 */
public class LavaBendingBullet extends Bullet {

    private Bullet secondBullet;
    private Bullet thirdBullet;

    public LavaBendingBullet(float angle, float x, float y) {
        super(angle, x, y);
        name = "Lava Bending bullet";
        damage = 50;
        width = 25;
        height = 25;
        color = Color.RED;
        bulletCoil = .2f;
        gunShotSound = Asset.instance().get(Asset.MACHINE_GUN_SHOT);

        secondBullet = BulletFactory.create(BulletType.NORMAL, angle, x, y + 15);
        thirdBullet = BulletFactory.create(BulletType.NORMAL, angle, x, y - 15);
        thirdBullet.setColor(Color.BLUE);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        sprite.setOriginCenter();
        sprite.setRotation(angle * MathUtils.radDeg);
        sprite.draw(batch);
        secondBullet.update(Gdx.graphics.getDeltaTime());
        thirdBullet.update(Gdx.graphics.getDeltaTime());
        secondBullet.draw(batch);
        thirdBullet.draw(batch);
    }
}
