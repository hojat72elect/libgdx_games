package com.nGame.utils.scene2d;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.utils.Array;

/**
 * Created with IntelliJ IDEA.
 *
 * @author oli
 *         Date: 23.11.13 - 16:23
 */
public class AnimatedDrawable extends BaseDrawable {
    public Animation animation;
    public float stateTime = 0; //Can be used to retrieve time where animation is paused
    private boolean flipH = false; //true if original image is flipped
    private boolean paused = false;

    public AnimatedDrawable(Animation ani) {
        this.animation = ani;
        setMinWidth(ani.getKeyFrame(0).getRegionWidth());
        setMinHeight(ani.getKeyFrame(0).getRegionHeight());
    }

    /**
     * Pause animation.
     * @param delta
     */
    public void pause() {
    	paused = true;
    }

    /**
     * To use when animation was paused.
     */
    public void continuePlay() {
    	paused = false;
    }

    public boolean isPaused() {
    	return paused;
    }

    public void update(float delta) {
    	if (!paused) {
    		stateTime += delta;
    	}
    }

    public void reset() {
    	if (isFlippedHorizontally()) flipHorizontally();
        stateTime = 0;
    }

    public void flipHorizontally() {
    	for (TextureRegion keyFrame : animation.getKeyFrames()){
    		keyFrame.flip(true, false);
    	}
    	if (isFlippedHorizontally()) {
	    	flipH = false;
    	}else{
    		flipH = true;
    	}
    }

    public boolean isFlippedHorizontally() {
    	return flipH;
    }

    public Animation getCopyOfAnimation(){
    	Array<TextureRegion> copyOfRegions = new Array<TextureRegion>();
    	for(TextureRegion keyFrame : animation.getKeyFrames()) {
    	    TextureRegion region = new TextureRegion(keyFrame);
    	    copyOfRegions.add(region);
    	}
    	return new Animation(animation.getFrameDuration(), copyOfRegions, animation.getPlayMode());
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
    	batch.draw(animation.getKeyFrame(stateTime), x, y, width, height);
    }

    /**
     * Get displayed/current frame index
     * @return
     */
    public int getCurrentKeyFrameIndex() {
    	return animation.getKeyFrameIndex(stateTime);
    }

    public void setPlayMode(Animation.PlayMode playMode) {
    	animation.setPlayMode(playMode);
    }
}
