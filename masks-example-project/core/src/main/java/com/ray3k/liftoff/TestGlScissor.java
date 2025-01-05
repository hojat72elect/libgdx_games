package com.ray3k.liftoff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.ray3k.liftoff.Core.*;

public class TestGlScissor implements Test {
    private Sprite head;
    private Animation<TextureRegion> headAnimation;
    private Sprite donutSprite;
    private float animationTime;
    private float chewPosition;
    
    @Override
    public void prep() {
        head = new Sprite(skin.getRegion("head-stationary"));
        
        Array<TextureRegion> textures = new Array<>(skin.getRegions("head"));
        textures.addAll(skin.getRegions("head"));
        headAnimation = new Animation<>(.05f, textures, PlayMode.LOOP);
        animationTime = Float.MAX_VALUE;
        
        donutSprite = new Sprite(skin.getRegion("donut"));
    }
    
    @Override
    public void act(float delta) {
        float x = Gdx.input.getX();
        float y = stage.getHeight() - Gdx.input.getY();
        
        head.setPosition(x - 185, y - 70);
        
        donutSprite.setPosition(stage.getWidth() - donutSprite.getRegionWidth(), stage.getHeight() / 2 - donutSprite.getRegionHeight() / 2f);
        
        animationTime += delta;
        if (Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
            if (donutSprite.getBoundingRectangle().contains(x, y) && x > chewPosition) {
                animationTime = 0;
                chomp.play();
                chewPosition = x;
            }
        }
        
        stage.act(delta);
    }
    
    @Override
    public void draw() {
        ScreenUtils.clear(Color.BLACK);

        spriteBatch.begin();
        background.update(Gdx.graphics.getDeltaTime());
        background.draw(stage.getBatch(), 0, 0, stage.getWidth(), stage.getHeight());
        
        if (!headAnimation.isAnimationFinished(animationTime)) {
            spriteBatch.draw(headAnimation.getKeyFrame(animationTime), Gdx.input.getX() - 185,
                    Gdx.graphics.getHeight() - Gdx.input.getY() - 70);
        } else {
            head.draw(spriteBatch);
        }
    
        spriteBatch.flush();
        
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor(MathUtils.round(chewPosition), 0, Math.round(stage.getWidth() - chewPosition), MathUtils.round(stage.getHeight()));
        
        donutSprite.draw(spriteBatch);
        
        spriteBatch.end();
        
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
        spriteBatch.flush();
        
        stage.draw();
    }
}
