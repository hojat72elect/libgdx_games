package com.bms.chemtris.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.bms.chemtris.asset.ButtonLoader;
import com.bms.chemtris.asset.LabelLoader;
import com.bms.chemtris.render.Background;
import com.bms.chemtris.tween.ActorAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

/**
 * Class to display main menu.
 */
public class MainMenu implements Screen {
    //stage for ui
    private Stage stage;
    //play button
    private Button buttonPlay;
    //Chemtris title
    private Label heading;
    //tween animation
    private TweenManager tweenManager;

    @Override
    public void show() {
        //setup stage
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        //make ui
        buttonPlay = ButtonLoader.makePlay();
        heading = LabelLoader.headingLabel("Chemtris");

        //add ui
        stage.addActor(buttonPlay);
        stage.addActor(heading);

        //setup tween manager
        tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new ActorAccessor());

        //setup a sequential event
        Timeline.createSequence().beginSequence()
                .push(Tween.set(buttonPlay,ActorAccessor.ALPHA).target(0))
                .push(Tween.from(heading, ActorAccessor.ALPHA, 1f).target(0))
                .push(Tween.to(buttonPlay, ActorAccessor.ALPHA,1f).target(1))
                .end().start(tweenManager);

        //setup play button listener
        buttonPlay.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //setup a leave screen timeline
                Timeline.createParallel().beginParallel()
                        .push(Tween.to(buttonPlay,ActorAccessor.Y,1f).target(-Gdx.graphics.getHeight()))
                        .push(Tween.to(heading,ActorAccessor.Y,1.5f).target(-Gdx.graphics.getHeight()))
                        .end().setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        ((Game)Gdx.app.getApplicationListener()).setScreen(new Levels());
                    }
                }).start(tweenManager);

                return true;
            }
        });

    }

    @Override
    public void render(float delta) {
        Background.render();
        //Tween updating
        tweenManager.update(delta);
        //stage rendering
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        ButtonLoader.dispose();
        stage.dispose();
    }
}
