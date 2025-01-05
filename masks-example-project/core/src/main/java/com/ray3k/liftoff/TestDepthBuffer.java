package com.ray3k.liftoff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.ray3k.liftoff.Core.*;

public class TestDepthBuffer implements Test {
    private Sprite head;
    private Animation<TextureRegion> headAnimation;
    private Sprite donutSprite;
    private float animationTime;
    private ShapeRenderer shapeRenderer;
    private Array<Point> points;
    
    @Override
    public void prep() {
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        Gdx.gl.glLineWidth(2);
    
        head = new Sprite(skin.getRegion("head-stationary"));
        
        Array<TextureRegion> textures = new Array<>(skin.getRegions("head"));
        textures.addAll(skin.getRegions("head"));
        headAnimation = new Animation<>(.05f, textures, PlayMode.LOOP);
        animationTime = Float.MAX_VALUE;
    
        donutSprite = new Sprite(skin.getRegion("donut"));
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
        background.draw(stage.getBatch(), 0, 0, stage.getWidth(), stage.getHeight());
    
        if (!headAnimation.isAnimationFinished(animationTime)) {
            spriteBatch.draw(headAnimation.getKeyFrame(animationTime), Gdx.input.getX() - 185,
                    Gdx.graphics.getHeight() - Gdx.input.getY() - 70);
        } else {
            head.draw(spriteBatch);
        }
    
        spriteBatch.end();
        
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glColorMask(false, false, false, false);
    
        shapeRenderer.begin();
        shapeRenderer.set(ShapeType.Filled);
        for (Point point : points) {
            shapeRenderer.circle(point.x, point.y, point.radius);
        }
        shapeRenderer.end();
    
        spriteBatch.begin();
        Gdx.gl.glColorMask(true, true, true, true);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        donutSprite.draw(spriteBatch);
        spriteBatch.end();
        
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        stage.draw();
    }
    
    private static class Point {
        public float x;
        public float y;
        public float radius;
    
        public Point(float x, float y) {
            this.x = x;
            this.y = y;
            radius = MathUtils.random(40f, 75f);
        }
    }
}
