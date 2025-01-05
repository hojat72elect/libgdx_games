package com.nGame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created with IntelliJ IDEA.
 *
 * @author oli
 *         Date: 06.11.13 - 14:16
 */
public class AnimatedSprite extends Sprite {

    private Animation animation;
    private Sprite lastFrame = new Sprite();
    private float frameTime = 0;

    private boolean running = true;
    private boolean resize = true;

    private boolean updateOnDraw = true;
    private boolean centered = true;
    private boolean facecRight;
    private float width;
    private float height;

    public AnimatedSprite(Animation animation, float width, float height) {
        super(animation.getKeyFrame(0));
        this.width = width;
        this.height = height;
        if (resize) {
            setSize(1f / width * getRegionWidth(), 1f / height * getRegionHeight());
            setOrigin(1f / width * getOriginX(), 1f / height * getOriginY());
        }
        this.animation = animation;
    }

    void update(float delta) {
        if (running) {
            lastFrame.set(this);
            frameTime += delta;
            setRegion(animation.getKeyFrame(frameTime));
            if (resize) {
                setSize(1f / width * getRegionWidth(), 1f / height * getRegionHeight());
                setOrigin(1f / width * getOriginX(), 1f / height * getOriginY());
            }
        }
    }


    @Override
    public void draw(Batch spriteBatch) {
        if (updateOnDraw) {
            update(Gdx.graphics.getDeltaTime());
        }

        if (centered) {
            float x = lastFrame.getX();
            float y = lastFrame.getY();
            float ox = lastFrame.getOriginX();
            float oy = lastFrame.getOriginY();
            float w = lastFrame.getWidth();
            float h = lastFrame.getHeight();
            float diffX = w - (1f / width * getRegionWidth());
            float diffY = h - (1f / height * getRegionHeight());

            //setOrigin(ox-diffX/2,oy-diffY/2);
            setOrigin(ox - diffX / 2, getOriginY());
            setBounds(x + diffX / 2, y + diffY / 2, w - diffX, h - diffY);
            if (!facecRight) {
                flip(true, false);
            }
            super.draw(spriteBatch);

            setOrigin(ox, oy);
            setBounds(x, y, w, h);
            return;
        }
        super.draw(spriteBatch);
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
        frameTime = 0;
    }

    public void setAnimation(Animation ani) {
        animation = ani;
        frameTime = 0;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setFacesRight(boolean facecRight) {
        this.facecRight = facecRight;
    }

    public boolean isAnimationFinished() {
        return animation.isAnimationFinished(frameTime);
    }

    public float getFrameTime() {
        return frameTime;
    }

    public void setFrameTime(float frameTime) {
        this.frameTime = frameTime;
    }
}
