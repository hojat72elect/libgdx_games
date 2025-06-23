package com.nopalsoft.zombiedash;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class AnimationSprite {

	/** Defines possible playback modes for an {@link Animation}. */
	public enum PlayMode {
		NORMAL, REVERSED, LOOP, LOOP_REVERSED, LOOP_PINGPONG, LOOP_RANDOM,
	}

	final Sprite[] spriteFrames;
	public final float frameDuration;
	public final float animationDuration;

	private PlayMode playMode = PlayMode.NORMAL;

	/**
	 * Constructor, storing the frame duration and key frames.
	 * 
	 * @param frameDuration
	 *            the time between frames in seconds.
	 * @param keyFrames
	 *            the {@link TextureRegion}s representing the frames.
	 */
	public AnimationSprite(float frameDuration,
			Array<? extends Sprite> keyFrames) {
		this.frameDuration = frameDuration;
		this.animationDuration = keyFrames.size * frameDuration;
		this.spriteFrames = new Sprite[keyFrames.size];
		for (int i = 0, n = keyFrames.size; i < n; i++) {
			this.spriteFrames[i] = keyFrames.get(i);
		}

		this.playMode = PlayMode.NORMAL;
	}

	/**
	 * Constructor, storing the frame duration, key frames and play type.
	 * 
	 * @param frameDuration
	 *            the time between frames in seconds.
	 * @param keyFrames
	 *            the {@link TextureRegion}s representing the frames.
	 * @param playMode
	 *            the animation playback mode.
	 */
	public AnimationSprite(float frameDuration,
			Array<? extends Sprite> keyFrames, PlayMode playMode) {

		this.frameDuration = frameDuration;
		this.animationDuration = keyFrames.size * frameDuration;
		this.spriteFrames = new Sprite[keyFrames.size];
		for (int i = 0, n = keyFrames.size; i < n; i++) {
			this.spriteFrames[i] = keyFrames.get(i);
		}

		this.playMode = playMode;
	}

	/**
	 * Constructor, storing the frame duration and key frames.
	 * 
	 * @param frameDuration
	 *            the time between frames in seconds.
	 * @param keyFrames
	 *            the {@link TextureRegion}s representing the frames.
	 */
	public AnimationSprite(float frameDuration, Sprite... keyFrames) {
		this.frameDuration = frameDuration;
		this.animationDuration = keyFrames.length * frameDuration;
		this.spriteFrames = keyFrames;
		this.playMode = PlayMode.NORMAL;
	}

	/**
	 * Returns a {@link TextureRegion} based on the so called state time. This is the amount of seconds an object has spent in the state this Animation instance represents, e.g. running, jumping and
	 * so on. The mode specifies whether the animation is looping or not.
	 * 
	 * @param stateTime
	 *            the time spent in the state represented by this animation.
	 * @param looping
	 *            whether the animation is looping or not.
	 * @return the TextureRegion representing the frame of animation for the given state time.
	 */
	public Sprite getKeyFrame(float stateTime, boolean looping) {
		// we set the play mode by overriding the previous mode based on looping
		// parameter value
		PlayMode oldPlayMode = playMode;
		if (looping
				&& (playMode == PlayMode.NORMAL || playMode == PlayMode.REVERSED)) {
			if (playMode == PlayMode.NORMAL)
				playMode = PlayMode.LOOP;
			else
				playMode = PlayMode.LOOP_REVERSED;
		}
		else if (!looping
				&& !(playMode == PlayMode.NORMAL || playMode == PlayMode.REVERSED)) {
			if (playMode == PlayMode.LOOP_REVERSED)
				playMode = PlayMode.REVERSED;
			else
				playMode = PlayMode.LOOP;
		}

		Sprite frame = getKeyFrame(stateTime);
		playMode = oldPlayMode;
		return frame;
	}

	/**
	 * Returns a {@link TextureRegion} based on the so called state time. This is the amount of seconds an object has spent in the state this Animation instance represents, e.g. running, jumping and
	 * so on using the mode specified by {@link #setPlayMode(PlayMode)} method.
	 * 
	 * @param stateTime
	 * @return the TextureRegion representing the frame of animation for the given state time.
	 */
	public Sprite getKeyFrame(float stateTime) {
		int frameNumber = getKeyFrameIndex(stateTime);
		return spriteFrames[frameNumber];
	}

	/**
	 * Returns the current frame number.
	 * 
	 * @param stateTime
	 * @return current frame number
	 */
	public int getKeyFrameIndex(float stateTime) {
		if (spriteFrames.length == 1)
			return 0;

		int frameNumber = (int) (stateTime / frameDuration);
		switch (playMode) {
		case NORMAL:
			frameNumber = Math.min(spriteFrames.length - 1, frameNumber);
			break;
		case LOOP:
			frameNumber = frameNumber % spriteFrames.length;
			break;
		case LOOP_PINGPONG:
			frameNumber = frameNumber % ((spriteFrames.length * 2) - 2);
			if (frameNumber >= spriteFrames.length)
				frameNumber = spriteFrames.length - 2
						- (frameNumber - spriteFrames.length);
			break;
		case LOOP_RANDOM:
			frameNumber = MathUtils.random(spriteFrames.length - 1);
			break;
		case REVERSED:
			frameNumber = Math.max(spriteFrames.length - frameNumber - 1, 0);
			break;
		case LOOP_REVERSED:
			frameNumber = frameNumber % spriteFrames.length;
			frameNumber = spriteFrames.length - frameNumber - 1;
			break;
		}

		return frameNumber;
	}

	/**
	 * Returns the keyFrames[] array where all the TextureRegions of the animation are stored.
	 * 
	 * @return keyFrames[] field
	 */
	public TextureRegion[] getKeyFrames() {
		return spriteFrames;
	}

	/** Returns the animation play mode. */
	public PlayMode getPlayMode() {
		return playMode;
	}

	/**
	 * Sets the animation play mode.
	 * 
	 * @param playMode
	 *            The animation {@link PlayMode} to use.
	 */
	public void setPlayMode(PlayMode playMode) {
		this.playMode = playMode;
	}

	/**
	 * Whether the animation would be finished if played without looping (PlayMode#NORMAL), given the state time.
	 * 
	 * @param stateTime
	 * @return whether the animation is finished.
	 */
	public boolean isAnimationFinished(float stateTime) {
		int frameNumber = (int) (stateTime / frameDuration);
		return spriteFrames.length - 1 < frameNumber;
	}

}
