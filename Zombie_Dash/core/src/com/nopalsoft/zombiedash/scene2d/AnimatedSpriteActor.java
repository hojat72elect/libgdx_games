package com.nopalsoft.zombiedash.scene2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nopalsoft.zombiedash.AnimationSprite;

public class AnimatedSpriteActor extends Actor {

	AnimationSprite animation;

	float stateTime;

	public AnimatedSpriteActor(AnimationSprite animation) {
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
		Sprite spriteframe = animation.getKeyFrame(stateTime, true);
		spriteframe.setPosition(getX(), getY());
		spriteframe.setSize(getWidth(), getHeight());
		spriteframe.draw(batch);
	}

}
