package dev.ian.assroids.factory;

import com.badlogic.gdx.math.MathUtils;

import dev.ian.assroids.entity.Asteroid;
import dev.ian.assroids.entity.Explosion;
import dev.ian.assroids.entity.Ship;
import dev.ian.assroids.entity.bullet.Bullet;

/**
 * Created by: Ian Parcon
 * Date created: Sep 09, 2018
 * Time created: 12:51 PM
 */
public class ExplosionFactory {

    public static Explosion create(Bullet bullet) {
        Explosion explosion = new Explosion("explosion-1", bullet.getX(), bullet.getY());
        explosion.setSize(bullet.getWidth() * 5, bullet.getHeight() * 5);
        explosion.setColor(bullet.getColor());
        return explosion;
    }

    public static Explosion create(Asteroid asteroid) {
        Explosion explosion = new Explosion("Webp", asteroid.getX() + asteroid.getWidth() / 2, asteroid.getY() + asteroid.getHeight() / 2);
        explosion.setSize(asteroid.getWidth() * 3, asteroid.getHeight() * 3);
        return explosion;
    }

    public static Explosion create(Ship ship) {
        Explosion explosion = new Explosion("Webp", ship.getX() + ship.getWidth() / 2, ship.getY() + ship.getHeight() / 2);
        int randomSize = MathUtils.random(1, 3);
        explosion.setSize(ship.getWidth() * randomSize, ship.getHeight() * randomSize);
        return explosion;
    }
}
