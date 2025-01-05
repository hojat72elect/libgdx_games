package dev.ian.assroids.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import dev.ian.assroids.asset.Asset;

/**
 * Created by: Ian Parcon
 * Date created: Sep 08, 2018
 * Time created: 10:28 AM
 */
public class Explosion extends GameObject {

    private Animation<TextureRegion> explosionAnim;
    private float explosionWidth;
    private float explosionHeight;
    private float stateTime;
    private boolean animFinished;

    public Explosion(String name, float x, float y) {
        super(x, y);
        color = Color.YELLOW;
        Array<TextureAtlas.AtlasRegion> region = Asset.instance().createExplosion(name);
        explosionAnim = new Animation<TextureRegion>(0.01f, region, Animation.PlayMode.NORMAL);
        sprite = new Sprite();
    }

    @Override
    public void setSize(float width, float height) {
        this.explosionHeight = height;
        this.explosionWidth = width;
    }

    @Override
    public void update(float delta) {
        stateTime += Gdx.graphics.getDeltaTime();
        animFinished = explosionAnim.isAnimationFinished(stateTime);
    }

    public void draw(SpriteBatch batch) {
        sprite.setRegion(explosionAnim.getKeyFrame(stateTime));
        sprite.setSize(explosionWidth, explosionHeight);
        sprite.setOriginCenter();
        sprite.setColor(color);
        sprite.setPosition(x - explosionWidth / 2, y - explosionHeight / 2);
        super.draw(batch);
    }

    public boolean isAnimationFinished() {
        return animFinished;
    }
}
