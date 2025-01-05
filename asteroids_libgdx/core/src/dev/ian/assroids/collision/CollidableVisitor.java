package dev.ian.assroids.collision;

import dev.ian.assroids.entity.Asteroid;
import dev.ian.assroids.entity.PowerUp;
import dev.ian.assroids.entity.Ship;
import dev.ian.assroids.entity.bullet.Bullet;

/**
 * Created by: Ian Parcon
 * Date created: Sep 08, 2018
 * Time created: 10:06 AM
 */
public interface CollidableVisitor {

    void collide(Asteroid asteroid);

    void collide(Bullet bullet);

    void collide(PowerUp powerUp);

    void collide(Ship ship);

}
