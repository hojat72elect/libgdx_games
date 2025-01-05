package com.ray3k.liftoff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.ray3k.liftoff.Core.*;

public class TestSpriteBatch2 implements Test {
    private Sprite head;
    private Animation<TextureRegion> headAnimation;
    private Sprite donutSprite;
    private float animationTime;
    private Sprite mask;
    private Array<Point> points;
    
    @Override
    public void prep() {
        head = new Sprite(skin.getRegion("head-stationary"));
        
        Array<TextureRegion> textures = new Array<>(skin.getRegions("head"));
        textures.addAll(skin.getRegions("head"));
        headAnimation = new Animation<>(.05f, textures, PlayMode.LOOP);
        animationTime = Float.MAX_VALUE;
    
        donutSprite = new Sprite(skin.getRegion("donut"));
        
        mask = new Sprite(skin.getRegion("bite"));
        points = new Array<>();
    }
    
    @Override
    public void act(float delta) {
        float x = Gdx.input.getX();
        float y = stage.getHeight() - Gdx.input.getY();
        
        head.setPosition(x - 185, y - 70);
        
        donutSprite.setPosition(stage.getWidth() - donutSprite.getRegionWidth(), stage.getHeight() / 2 - donutSprite.getRegionHeight() / 2f);
    
        animationTime += delta;
        if (Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
            if (donutSprite.getBoundingRectangle().contains(x, y)) {
                animationTime = 0;
                chomp.play();
                points.add(new Point(x, y));
            }
        }
    
        stage.act(delta);
        background.update(delta);
    }
    
    @Override
    public void draw() {
        ScreenUtils.clear(Color.BLACK);
        
        spriteBatch.begin();
        
        //I left the redundant gl calls in to make what's happening at each stage more clear.
        //Draw the background and head like normal.
        spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glBlendEquation(GL20.GL_FUNC_ADD);
        Gdx.gl.glColorMask(true,true, true, true);
        background.draw(stage.getBatch(), 0, 0, stage.getWidth(), stage.getHeight());
    
        if (!headAnimation.isAnimationFinished(animationTime)) {
            spriteBatch.draw(headAnimation.getKeyFrame(animationTime), Gdx.input.getX() - 185,
                    Gdx.graphics.getHeight() - Gdx.input.getY() - 70);
        } else {
            head.draw(spriteBatch);
        }
        spriteBatch.flush();
        
        //Subtract the reverse alpha from the donut. This will serve as the donut mask.
        spriteBatch.setBlendFunction(GL20.GL_ZERO, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glBlendEquation(GL20.GL_FUNC_REVERSE_SUBTRACT);
        Gdx.gl.glColorMask(false, false, false, true);
        donutSprite.draw(spriteBatch);
        spriteBatch.flush();
        
        //Add back the alpha from the mask at every point generated. The donut will not be drawn here.
        spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_DST_ALPHA);
        Gdx.gl.glBlendEquation(GL20.GL_FUNC_ADD);
        Gdx.gl.glColorMask(false, false, false, true);
        for (Point point : points) {
            mask.setCenter(point.x, point.y);
            mask.draw(spriteBatch);
        }
        spriteBatch.flush();

        //Draw the donut only where alpha is less than 1.
        spriteBatch.setBlendFunction(GL20.GL_ONE_MINUS_DST_ALPHA, GL20.GL_DST_ALPHA);
        Gdx.gl.glBlendEquation(GL20.GL_FUNC_ADD);
        Gdx.gl.glColorMask(true, true, true, true);
        donutSprite.draw(spriteBatch);
        spriteBatch.end();
        
        //Reset the blending to draw the UI above everything.
        spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glBlendEquation(GL20.GL_FUNC_ADD);
        Gdx.gl.glColorMask(true,true, true, true);
        stage.draw();
    }
    
    private static class Point {
        public float x;
        public float y;
        
        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
