package dev.ian.assroids.entity;

import com.badlogic.gdx.math.MathUtils;

import dev.ian.assroids.asset.Asset;
import dev.ian.assroids.collision.Collidable;
import dev.ian.assroids.collision.CollidableVisitor;
import dev.ian.assroids.entity.bullet.Bullet;

/**
 * Created by: Ian Parcon
 * Date created: Sep 03, 2018
 * Time created: 11:40 AM
 */
public class Asteroid extends GameObject implements Collidable, CollidableVisitor {

    private int health;
    protected float rotation;
    protected float speed;
    private String[] asteroidTextures = {"asteroid_a", "asteroid_b", "asteroid_c"};


    public Asteroid() {
        this.sprite = Asset.instance().createSprite(asteroidTextures[MathUtils.random(asteroidTextures.length - 1)]);
        rotation = MathUtils.random(-0.5f, 0.5f);
        speed = MathUtils.random(50, 200);
        x = MathUtils.random(-100, 0);
        y = MathUtils.random(-100, 0);
        dx = MathUtils.random(-40, 20);
        dy = MathUtils.random(-40, 20);
        health = 100;
        int randSize = MathUtils.random(50, 100);
        setSize(randSize, randSize);
        originCenter();

    }

    @Override
    public void update(float delta) {
        float angle = MathUtils.atan2(dy, dx);
        dx = MathUtils.cos(angle) * speed;
        dy = MathUtils.sin(angle) * speed;

        x += dx * delta;
        y += dy * delta;
        sprite.setPosition(x, y);
        sprite.rotate(rotation);

        super.update(delta);
    }

    @Override
    public void accept(CollidableVisitor visitor) {
        visitor.collide(this);
    }

    @Override
    public void collide(Asteroid asteroid) {

    }

    @Override
    public void collide(Bullet bullet) {
        if (super.isCollide(bullet)) {
            health = health - bullet.getDamage();
        }
    }

    @Override
    public void collide(PowerUp powerUp) {

    }

    @Override
    public void collide(Ship ship) {

    }

    public boolean isAlive() {
        return health >= 0;
    }
}
