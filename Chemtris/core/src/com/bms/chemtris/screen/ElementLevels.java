package com.bms.chemtris.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.bms.chemtris.asset.ButtonLoader;
import com.bms.chemtris.asset.LabelLoader;
import com.bms.chemtris.game.ScoreManager;
import com.bms.chemtris.render.Background;
import com.bms.chemtris.tween.ActorAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

/**
 * Screen to display element levels.
 */
public class ElementLevels implements Screen {
    //stage for UI
    private Stage stage;
    //level buttons
    private TextButton level1,level2;
    //level title and scores
    private Label heading, l1,l2;
    //tween animation
    private TweenManager tweenManager;

    @Override
    public void show() {
        //Setup Stage
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        //make screen UI
        heading = LabelLoader.headingLabel("Levels");
        level1 = ButtonLoader.makeLevelButton("H2O",1);
        level2 = ButtonLoader.makeLevelButton("CH4",2);

        l1 = LabelLoader.levelScore(1, ScoreManager.H2O);
        l2 = LabelLoader.levelScore(2,ScoreManager.CH4);

        //setup tween manager
        tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new ActorAccessor());

        //add actors to stage
        stage.addActor(heading);
        stage.addActor(level1);
        stage.addActor(l1);
        stage.addActor(level2);
        stage.addActor(l2);

        //start screen animation
        startAnimation();

        //level 1
        level1.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leaveAnimation(new WaterLevel());
                return true;
            }
        });

        level2.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leaveAnimation(new MethaneLevel());
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Background.render();
        //update tween
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

    //starting animation
    private void startAnimation(){
        //setup a sequential event
        Timeline.createSequence().beginSequence()
                .push(Tween.set(level1,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(l1,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(level2,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(l2,ActorAccessor.ALPHA).target(0))
                .push(Tween.from(heading, ActorAccessor.ALPHA, 1f).target(0))
                .push(Tween.to(level1,ActorAccessor.ALPHA,.5f).target(1))
                .push(Tween.to(l1,ActorAccessor.ALPHA,.2f).target(1))
                .push(Tween.to(level2,ActorAccessor.ALPHA,.5f).target(1))
                .push(Tween.to(l2,ActorAccessor.ALPHA,.2f).target(1))
                .end().start(tweenManager);
    }

    //method to animate screen leaving
    private void leaveAnimation(final Screen setScreen){

        //setup a parallel event
        Timeline.createParallel().beginParallel()
                .push(Tween.to(heading, ActorAccessor.Y, 2f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(level1, ActorAccessor.Y, 1.5f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(l1, ActorAccessor.Y, 1.25f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(level2, ActorAccessor.Y, 1.5f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(l2, ActorAccessor.Y, 1.25f).target(-Gdx.graphics.getHeight()))
                .end().setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(setScreen);
            }
        }).start(tweenManager);

    }
}
