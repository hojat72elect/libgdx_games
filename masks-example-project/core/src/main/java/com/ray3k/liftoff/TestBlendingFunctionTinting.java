package com.ray3k.liftoff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static com.ray3k.liftoff.Core.*;

public class TestBlendingFunctionTinting implements Test {
    private Sprite head;
    private Animation<TextureRegion> headAnimation;
    private Sprite donutSprite;
    private float animationTime;
    private Sprite mask;
    private Array<Point> points;
    private FrameBuffer frameBuffer;
    private ScreenViewport frameBufferViewport;
    
    @Override
    public void prep() {
        head = new Sprite(skin.getRegion("head-stationary"));
        
        Array<TextureRegion> textures = new Array<>(skin.getRegions("head"));
        textures.addAll(skin.getRegions("head"));
        headAnimation = new Animation<>(.05f, textures, PlayMode.LOOP);
        animationTime = Float.MAX_VALUE;
    
        donutSprite = new Sprite(skin.getRegion("donut"));
        
        mask = new Sprite(skin.getRegion("bite-black"));
        points = new Array<>();
        frameBuffer = new FrameBuffer(Format.RGBA4444, donutSprite.getRegionWidth(), donutSprite.getRegionHeight(), false);
        frameBufferViewport = new ScreenViewport();
        frameBufferViewport.update(frameBuffer.getWidth(), frameBuffer.getHeight(), true);
    }
    
    @Override
    public void act(float delta) {
        float x = Gdx.input.getX();
        float y = stage.getHeight() - Gdx.input.getY();
        
        head.setPosition(x - 185, y - 70);
        
        donutSprite.setPosition(stage.getWidth() - donutSprite.getRegionWidth(), stage.getHeight() / 2 - donutSprite.getRegionHeight() / 2f);
        frameBufferViewport.getCamera().position.set(donutSprite.getX() + donutSprite.getWidth() / 2, donutSprite.getY() + donutSprite.getHeight() / 2, 0);
    
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
        frameBuffer.begin();
        frameBufferViewport.apply();
        spriteBatch.setProjectionMatrix(frameBufferViewport.getCamera().combined);
        spriteBatch.begin();
        ScreenUtils.clear(Color.CLEAR);
        
        donutSprite.draw(spriteBatch);
        spriteBatch.end();
        
        spriteBatch.begin();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        spriteBatch.setBlendFunctionSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ZERO, GL20.GL_DST_ALPHA);
        for (Point point : points) {
            mask.setCenter(point.x, point.y);
            mask.draw(spriteBatch);
        }
        spriteBatch.end();
        frameBuffer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        ScreenUtils.clear(Color.BLACK);
        stage.getViewport().apply();
        spriteBatch.setProjectionMatrix(stage.getCamera().combined);
        spriteBatch.begin();
        background.draw(stage.getBatch(), 0, 0, stage.getWidth(), stage.getHeight());
    
        if (!headAnimation.isAnimationFinished(animationTime)) {
            spriteBatch.draw(headAnimation.getKeyFrame(animationTime), Gdx.input.getX() - 185,
                    Gdx.graphics.getHeight() - Gdx.input.getY() - 70);
        } else {
            head.draw(spriteBatch);
        }
    
        Texture texture = frameBuffer.getColorBufferTexture();
        TextureRegion textureRegion = new TextureRegion(texture);
        textureRegion.flip(false, true);
        
        spriteBatch.draw(textureRegion, stage.getWidth() - textureRegion.getRegionWidth(), stage.getHeight() / 2 - textureRegion.getRegionHeight() / 2f);
        spriteBatch.end();

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
