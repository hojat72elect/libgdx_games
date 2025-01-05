package com.nGame.utils.scene2d;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created with IntelliJ IDEA.
 *
 * @author oli
 *         Date: 23.11.13 - 16:09
 */
public class AnimatedImage extends Image {
    public AnimatedDrawable drawable;

    public AnimatedImage(AnimatedDrawable drawable) {
        super(drawable);
        this.drawable = drawable;
    }

    @Override
    public void act(float delta) {
        drawable.update(delta);
        super.act(delta);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void reset() {
        drawable.reset();
    }

    public void flipHorizontally() {
    	drawable.flipHorizontally();
    }

    public boolean isFlippedHorizontally(){
    	return drawable.isFlippedHorizontally();
    }

    public Animation getCopyOfAnimation() {
    	return drawable.getCopyOfAnimation();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
    	batch.draw(drawable.animation.getKeyFrame(((AnimatedDrawable) drawable).stateTime), getX(), getY(), getOriginX(), getOriginY(),
    			getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    	this.validate();
    }

    public void setPlayMode(Animation.PlayMode playMode) {
    	drawable.animation.setPlayMode(playMode);
    }
}
