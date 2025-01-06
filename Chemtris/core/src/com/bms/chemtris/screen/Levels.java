package com.bms.chemtris.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
 * Class to display levels.
 */
public class Levels implements Screen {
    //stage for UI
    private Stage stage;
    //level buttons
    private TextButton level1, level2, level3, level4, level5, level6, more;
    //level title and scores
    private Label heading, l1, l2, l3 , l4, l5, l6, l7;
    //tween animation
    private TweenManager tweenManager;

    @Override
    public void show() {
        //Setup Stage
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        //make screen UI
        heading = LabelLoader.headingLabel("Levels");
        level1 = ButtonLoader.makeLevelButton("1",1);
        level2 = ButtonLoader.makeLevelButton("2",2);
        level3 = ButtonLoader.makeLevelButton("3",3);
        level4 = ButtonLoader.makeLevelButton("4",4);
        level5 = ButtonLoader.makeLevelButton("5",5);
        level6 = ButtonLoader.makeLevelButton("6",6);
        more = ButtonLoader.makeLevelButton("More",2);
        more.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/15,
                more.getHeight());
        l1 = LabelLoader.levelScore(1, ScoreManager.LEVEL_1);
        l2 = LabelLoader.levelScore(2, ScoreManager.LEVEL_2);
        l3 = LabelLoader.levelScore(3, ScoreManager.LEVEL_3);
        l4 = LabelLoader.levelScore(4, ScoreManager.LEVEL_4);
        l5 = LabelLoader.levelScore(5, ScoreManager.LEVEL_5);
        l6 = LabelLoader.levelScore(6, ScoreManager.LEVEL_6);
        l7 = LabelLoader.extraLevel();

        //setup tween manager
        tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new ActorAccessor());

        //add actors to stage
        stage.addActor(heading);
        stage.addActor(level1);
        stage.addActor(level2);
        stage.addActor(level3);
        stage.addActor(level4);
        stage.addActor(level5);
        stage.addActor(level6);
        stage.addActor(l1);
        stage.addActor(l2);
        stage.addActor(l3);
        stage.addActor(l4);
        stage.addActor(l5);
        stage.addActor(l6);
        stage.addActor(more);
        stage.addActor(l7);

        l7.addAction(Actions.fadeOut(0f));

        //start screen animation
        startAnimation();

        //level 1
        level1.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leaveAnimation(new LevelOne());
                return false;
            }
        });

        //level 2
        level2.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leaveAnimation(new LevelTwo());
                return false;
            }
        });

        //level 3
        level3.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leaveAnimation(new LevelThree());
                return false;
            }
        });

        //level 4
        level4.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leaveAnimation(new LevelFour());
                return false;
            }
        });

        //level 5
        level5.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leaveAnimation(new LevelFive());
                return false;
            }
        });

        //level 6
        level6.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leaveAnimation(new LevelSix());
                return false;
            }
        });

        more.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                l7.addAction(Actions.fadeIn(1f));
                if(ScoreManager.scored()){
                    leaveAnimation(new ElementLevels());
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        //render background
        Background.render();
        //update tween
        tweenManager.update(delta);
        //stage rendering
        stage.act(delta);
        stage.draw();

    }

    //starting animation
    private void startAnimation(){
        //setup a sequential event
        Timeline.createSequence().beginSequence()
                .push(Tween.set(level1,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(level2,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(level3,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(level4,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(level5,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(level6,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(more,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(l1,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(l2,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(l3,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(l4,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(l5,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(l6,ActorAccessor.ALPHA).target(0))
                .push(Tween.from(heading, ActorAccessor.ALPHA, 1f).target(0))
                .push(Tween.to(level1,ActorAccessor.ALPHA,.5f).target(1))
                .push(Tween.to(l1,ActorAccessor.ALPHA,.2f).target(1))
                .push(Tween.to(level2,ActorAccessor.ALPHA,.5f).target(1))
                .push(Tween.to(l2,ActorAccessor.ALPHA,.2f).target(1))
                .push(Tween.to(level3,ActorAccessor.ALPHA,.5f).target(1))
                .push(Tween.to(l3,ActorAccessor.ALPHA,.2f).target(1))
                .push(Tween.to(level4,ActorAccessor.ALPHA,.5f).target(1))
                .push(Tween.to(l4,ActorAccessor.ALPHA,.2f).target(1))
                .push(Tween.to(level5,ActorAccessor.ALPHA,.5f).target(1))
                .push(Tween.to(l5,ActorAccessor.ALPHA,.2f).target(1))
                .push(Tween.to(level6,ActorAccessor.ALPHA,.5f).target(1))
                .push(Tween.to(l6,ActorAccessor.ALPHA,.2f).target(1))
                .push(Tween.to(more,ActorAccessor.ALPHA,.5f).target(1))
                .end().start(tweenManager);
    }

    //method to animate screen leaving
    private void leaveAnimation(final Screen setScreen){

        //setup a parallel event
        Timeline.createParallel().beginParallel()
                .push(Tween.to(heading, ActorAccessor.Y, 2f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(level1, ActorAccessor.Y, 1.5f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(level2, ActorAccessor.Y, 1.5f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(level3, ActorAccessor.Y, 1.5f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(level4, ActorAccessor.Y, 1f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(level5, ActorAccessor.Y, 1f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(level6, ActorAccessor.Y, 1f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(l1, ActorAccessor.Y, 1.25f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(l2, ActorAccessor.Y, 1.25f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(l3, ActorAccessor.Y, 1.25f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(l4, ActorAccessor.Y, .75f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(l5, ActorAccessor.Y, .75f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(l6, ActorAccessor.Y, .75f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(more, ActorAccessor.Y, .5f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(l7, ActorAccessor.Y, .5f).target(-Gdx.graphics.getHeight())).end().setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(setScreen);
            }
        }).start(tweenManager);

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
