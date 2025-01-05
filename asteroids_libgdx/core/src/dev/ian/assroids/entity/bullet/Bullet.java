package dev.ian.assroids.entity.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import dev.ian.assroids.asset.Asset;
import dev.ian.assroids.collision.Collidable;
import dev.ian.assroids.collision.CollidableVisitor;
import dev.ian.assroids.entity.Asteroid;
import dev.ian.assroids.entity.GameObject;
import dev.ian.assroids.entity.PowerUp;
import dev.ian.assroids.entity.Ship;

/**
 * Created by: Ian Parcon
 * Date created: Sep 03, 2018
 * Time created: 12:04 PM
 */
public class Bullet extends GameObject implements Collidable, CollidableVisitor {

    protected String name;
    private Animation<TextureRegion> fireAnimation;
    protected float angle;
    protected float speed;
    protected float stateTime;
    protected Color color;
    protected float width = 16;
    protected float height = 16;
    protected float bulletCoil;
    protected int damage;

    protected Sound gunShotSound;

    public Bullet(float angle, float x, float y) {
        super(x, y);
        this.bulletCoil = 0.2f;
        this.speed = 400;
        this.angle = angle;
        Array<TextureAtlas.AtlasRegion> region = Asset.instance().createRegion("bullet");
        fireAnimation = new Animation<TextureRegion>(0.06f, region, Animation.PlayMode.LOOP);
        sprite = new Sprite();
    }

    @Override
    public void update(float delta) {
        stateTime += delta;
        dx = MathUtils.cos(angle) * speed;
        dy = MathUtils.sin(angle) * speed;
        x += dx * delta;
        y += dy * delta;
    }

    public boolean isOutOfBounds() {
        return y > Gdx.graphics.getHeight() ||
                y + getHeight() < 0 ||
                x + getWidth() < 0 ||
                x > Gdx.graphics.getWidth();
    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.setRegion(fireAnimation.getKeyFrame(stateTime));
        sprite.setSize(width, height);
        sprite.setColor(color);
        sprite.setPosition(x - sprite.getWidth() / 2, y - sprite.getWidth() / 2);
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

    public float getBulletCoil() {
        return bulletCoil;
    }

    public Color getColor() {
        return color;
    }

    public void playSound() {
        gunShotSound.setVolume(gunShotSound.play(), 0.3f);
    }
}
