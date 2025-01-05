package dev.ian.assroids.factory;

import dev.ian.assroids.entity.bullet.BulletType;
import dev.ian.assroids.entity.bullet.Bullet;
import dev.ian.assroids.entity.bullet.EagleEyeSightBullet;
import dev.ian.assroids.entity.bullet.LaserBullet;
import dev.ian.assroids.entity.bullet.LavaBendingBullet;
import dev.ian.assroids.entity.bullet.NormalBullet;
import dev.ian.assroids.entity.bullet.VenomBreathBullet;

/**
 * Created by: Ian Parcon
 * Date created: Sep 09, 2018
 * Time created: 1:15 PM
 */
public class BulletFactory {

    public static Bullet create(BulletType type, float angle, float x, float y) {
        switch (type) {
            case LAVA:
                return new LavaBendingBullet(angle, x, y);
            case EAGLE:
                return new EagleEyeSightBullet(angle, x, y);
            case VENOM:
                return new VenomBreathBullet(angle, x, y);
            case LASER:
                return new LaserBullet(angle, x, y);
            default:
                return new NormalBullet(angle, x, y);
        }
    }
}
