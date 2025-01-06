package com.bms.chemtris.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bms.chemtris.asset.TextureLoader;
import com.bms.chemtris.render.Background;
import com.bms.chemtris.tween.SpriteAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

/**
 * Screen to display splash image.
 */
public class Splash implements Screen {
    //batch to draw images
    private SpriteBatch batch;
    //splash sprite
    private Sprite splash;
    //tween animation manager
    private TweenManager tween;

    @Override
    public void show() {
        //initialize sprite batch
        batch = new SpriteBatch();
        //initialize splash sprite
        splash = new Sprite(TextureLoader.getSplash());
        //set splash size
        splash.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


        //setup animation
        tween = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        //FadeIN
        Tween.set(splash,SpriteAccessor.ALPHA).target(0).start(tween);
        Tween.to(splash,SpriteAccessor.ALPHA,1.5f).target(1).start(tween);

        //FadeOut
        Tween.to(splash,SpriteAccessor.ALPHA,2f).target(0).delay(1.25f).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        }).start(tween);


    }

    @Override
    public void render(float delta) {
        //render background
        Background.render();
        //update tween
        tween.update(delta);
        //Render the sprite
        batch.begin();
        splash.draw(batch);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        //update size
        splash.setSize(width,height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        //dispose assets
        batch.dispose();
        TextureLoader.disposeSplash();
    }
}
