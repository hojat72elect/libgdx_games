package dev.ian.assroids.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dev.ian.assroids.collision.Collidable;
import dev.ian.assroids.collision.CollidableVisitor;
import dev.ian.assroids.entity.bullet.Bullet;
import dev.ian.assroids.entity.bullet.BulletType;
import dev.ian.assroids.factory.BulletFactory;

/**
 * Created by: Ian Parcon
 * Date created: Sep 02, 2018
 * Time created: 8:51 PM
 */
public class Ship extends GameObject implements Collidable, CollidableVisitor {

    private BulletType bulletType;
    private List<Bullet> bullets;
    private float shipExplosionDelay;
    private float fireDelay;
    private float gunCoil;
    private float angle;
    private int damage;
    private int kill;
    private boolean isFiring;

    public Ship(Sprite sprite, float x, float y) {
        super(sprite, x, y);
        bullets = new ArrayList<Bullet>();
    }

    public void update(float delta) {
        shipExplosionDelay += delta;
        fireDelay += delta;

        angle = MathUtils.atan2(dy, dx);
        dx += MathUtils.cos(angle) * delta;
        dy += MathUtils.sin(angle) * delta;

        x += dx * delta;
        y += dy * delta;

        super.update(delta);
    }

    @Override
    public void draw(SpriteBatch batch) {
        Iterator<Bullet> iter = bullets.iterator();
        while (iter.hasNext()) {
            Bullet bullet = iter.next();
            bullet.update(Gdx.graphics.getDeltaTime());
            bullet.draw(batch);
            if (bullet.isOutOfBounds()) iter.remove();
        }
        sprite.setPosition(x, y);
        sprite.setRotation(angle * MathUtils.radDeg);
        super.draw(batch);
    }

    public void fire() {
        isFiring = false;
        if (fireDelay >= gunCoil) {
            Bullet bullet = BulletFactory.create(bulletType, angle, x + getWidth() / 2, y + getHeight() / 2);
            damage = bullet.getDamage();
            gunCoil = bullet.getBulletCoil();
            bullets.add(bullet);
            fireDelay = 0;
            bullet.playSound();
            isFiring = true;
        }
    }

    public boolean isFiring() {
        return isFiring;
    }

    @Override
    public boolean isCollide(GameObject gameObject) {
        if (super.isCollide(gameObject) && shipExplosionDelay >= 0.5f) {
            shipExplosionDelay = 0;
            return true;
        }
        return false;
    }

    public void changeBullet(PowerUp powerUp) {
        this.bulletType = powerUp.getBulletType();
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public float getAngle() {
        return angle * MathUtils.radDeg;
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    @Override
    public void accept(CollidableVisitor visitor) {
        visitor.collide(this);
    }

    @Override
    public void collide(Asteroid asteroid) {
    }

    public int getKill() {
        return kill;
    }

    @Override
    public void collide(Bullet bullet) {

    }

    @Override
    public void collide(PowerUp powerUp) {

    }

    @Override
    public void collide(Ship ship) {

    }

    public int getDamage() {
        return damage;
    }

    public void increaseKill() {
        ++kill;
    }
}
