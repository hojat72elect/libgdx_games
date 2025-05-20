package com.nopalsoft.superjumper.scene2d;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AnimatedSpriteActor extends Actor {

	Animation<TextureRegion> animation;

	float stateTime;

	public AnimatedSpriteActor(Animation<TextureRegion> animation) {
		this.animation = animation;
		stateTime = 0;
	}

	@Override
	public void act(float delta) {
		stateTime += delta;
		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		TextureRegion spriteframe = animation.getKeyFrame(stateTime, true);
		batch.draw(spriteframe, getX(), getY(), getWidth(), getHeight());
	}
}
