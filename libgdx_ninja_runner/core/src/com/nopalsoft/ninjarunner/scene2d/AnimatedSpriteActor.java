package com.nopalsoft.ninjarunner.scene2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nopalsoft.ninjarunner.AnimationSprite;


public class AnimatedSpriteActor extends Actor {
	AnimationSprite anim;
	float stateTime;

	public AnimatedSpriteActor(AnimationSprite animacion) {
		anim = animacion;
		stateTime = 0;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		stateTime += delta;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Sprite sprite = anim.getKeyFrame(stateTime, true);
		sprite.setSize(getWidth(), getHeight());
		sprite.setPosition(getX(), getY());
		sprite.draw(batch);
	}
}
