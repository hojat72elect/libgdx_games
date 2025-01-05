package com.ray3k.liftoff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.badlogic.gdx.graphics.Pixmap.Blending.None;
import static com.ray3k.liftoff.Core.background;
import static com.ray3k.liftoff.Core.chomp;
import static com.ray3k.liftoff.Core.skin;
import static com.ray3k.liftoff.Core.spriteBatch;
import static com.ray3k.liftoff.Core.stage;

public class TestPixmaps implements Test {
    
    private Sprite head;
    private Animation<TextureRegion> headAnimation;
    private Sprite donutSprite;
    private float animationTime;
    private Pixmap donutPixmap;
    private Pixmap result, mask, combinedMasks;
    
    @Override
    public void prep() {
        head = new Sprite(skin.getRegion("head-stationary"));
        
        Array<TextureRegion> textures = new Array<>(skin.getRegions("head"));
        textures.addAll(skin.getRegions("head"));
        headAnimation = new Animation<>(.05f, textures, PlayMode.LOOP);
        animationTime = Float.MAX_VALUE;
        
        FileHandle fileHandle = Gdx.files.internal("donut.png");
        
        donutSprite = new Sprite(new Texture(fileHandle));
        
        donutPixmap = new Pixmap(fileHandle);
        result = new Pixmap(fileHandle);
        mask = new Pixmap(Gdx.files.internal("bite-black.png"));
        combinedMasks = new Pixmap(donutPixmap.getWidth(), donutPixmap.getHeight(), Format.RGBA4444);
    }
    
    @Override
    public void act(float delta) {
        int x = Gdx.input.getX();
        int y = MathUtils.round(stage.getHeight()) - Gdx.input.getY();
        
        head.setPosition(x - 185, y - 70);
        
        donutSprite.setPosition(stage.getWidth() - donutSprite.getRegionWidth(),
                stage.getHeight() / 2 - donutSprite.getRegionHeight() / 2f);
        
        animationTime += delta;
        if (Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
            if (donutSprite.getBoundingRectangle().contains(x, y)) {
                animationTime = 0;
                chomp.play();
                addMask();
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
        
        donutSprite.draw(spriteBatch);
        
        spriteBatch.end();
        stage.draw();
    }
    
    private void addMask() {
        int pointX = Gdx.input.getX() - MathUtils.round(donutSprite.getX());
        int pointY = Gdx.input.getY() - MathUtils.round(donutSprite.getY());
        
        int x = pointX - mask.getWidth() / 2;
        int y = pointY - mask.getHeight() / 2;
        combinedMasks.drawPixmap(mask, x, y);
        
        updateDonut(x, y, x + mask.getWidth(), y + mask.getHeight());
    }
    
    private void updateDonut(int startX, int startY, int endX, int endY) {
        result.setBlending(None);
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                /* Be really careful not to use the same pixmap calling drawPixel(...) inside
                 * the method's parameters as it causes extreme performance issues */
                result.drawPixel(x, y, donutPixmap.getPixel(x, y) & ~combinedMasks.getPixel(x, y));
            }
        }
        donutSprite.setTexture(new Texture(result));
    }
}