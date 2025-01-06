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
 * screen to display game over.
 */
public class GameOver implements Screen {

    //members
    private int score, bestPoints;
    private String levelTag;
    private float timeTaken;

    //ui
    private Stage stage;
    private TextButton replay, levels;
    private Label title, point, totalTime, best;
    private TweenManager tweenManager;

    //constructor
    public GameOver(int points, String level, float time){

        //initialize member
        score = points;
        levelTag = level;
        timeTaken = time;

        //get high score
        bestPoints = ScoreManager.getScore(levelTag);
    }



    @Override
    public void show() {
        //setup stage
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        title = LabelLoader.headingLabel("Game Over");
        point = LabelLoader.gameScore(score);
        best = LabelLoader.highScore(bestPoints);
        replay = ButtonLoader.replay();
        levels = ButtonLoader.levelss();

        //add ui to stage
        stage.addActor(title);
        stage.addActor(point);
        stage.addActor(levels);
        stage.addActor(replay);
        stage.addActor(best);

        //setup tween manager
        tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new ActorAccessor());

        //setup a sequential event
        Timeline.createSequence().beginSequence()
                .push(Tween.set(point,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(levels,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(replay,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(best,ActorAccessor.ALPHA).target(0))
                .push(Tween.from(title, ActorAccessor.ALPHA, 1f).target(0))
                .push(Tween.to(point,ActorAccessor.ALPHA,1f).target(1))
                .push(Tween.to(best,ActorAccessor.ALPHA,1f).target(1))
                .push(Tween.to(replay,ActorAccessor.ALPHA,.5f).target(1))
                .push(Tween.to(levels,ActorAccessor.ALPHA,.5f).target(1))
                .end().start(tweenManager);

        //setup inputs
        Gdx.input.setInputProcessor(stage);

        //levels
        levels.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //setup a leave screen timeline
                Timeline.createParallel().beginParallel()
                        .push(Tween.to(title,ActorAccessor.Y,1.5f).target(-Gdx.graphics.getHeight()))
                        .push(Tween.to(point,ActorAccessor.Y,1.25f).target(-Gdx.graphics.getHeight()))
                        .push(Tween.to(best,ActorAccessor.Y,1f).target(-Gdx.graphics.getHeight()))
                        .push(Tween.to(levels,ActorAccessor.Y,.75f).target(-Gdx.graphics.getHeight()))
                        .push(Tween.to(replay,ActorAccessor.Y,.75f).target(-Gdx.graphics.getHeight()))
                        .end().setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        ((Game)Gdx.app.getApplicationListener()).setScreen(new Levels());
                    }
                }).start(tweenManager);

                return true;
            }
        });

        //replay
        replay.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //setup a leave screen timeline
                Timeline.createParallel().beginParallel()
                        .push(Tween.to(title,ActorAccessor.Y,1.5f).target(-Gdx.graphics.getHeight()))
                        .push(Tween.to(point,ActorAccessor.Y,1.25f).target(-Gdx.graphics.getHeight()))
                        .push(Tween.to(best,ActorAccessor.Y,1f).target(-Gdx.graphics.getHeight()))
                        .push(Tween.to(levels,ActorAccessor.Y,.75f).target(-Gdx.graphics.getHeight()))
                        .push(Tween.to(replay, ActorAccessor.Y,.75f).target(-Gdx.graphics.getHeight()))
                        .end().setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {

                        if(levelTag == ScoreManager.LEVEL_1){
                            ((Game)Gdx.app.getApplicationListener()).setScreen(new LevelOne());
                        }
                        else if(levelTag == ScoreManager.LEVEL_2){
                            ((Game)Gdx.app.getApplicationListener()).setScreen(new LevelOne());
                        }
                        else if(levelTag == ScoreManager.LEVEL_3){
                            ((Game)Gdx.app.getApplicationListener()).setScreen(new LevelThree());
                        }
                        else if(levelTag == ScoreManager.LEVEL_4){
                            ((Game)Gdx.app.getApplicationListener()).setScreen(new LevelFour());
                        }
                        else if(levelTag == ScoreManager.LEVEL_5){
                            ((Game)Gdx.app.getApplicationListener()).setScreen(new LevelFive());
                        }
                        else if(levelTag  == ScoreManager.LEVEL_6){
                            ((Game)Gdx.app.getApplicationListener()).setScreen(new LevelSix());
                        }else if(levelTag == ScoreManager.H2O){
                            ((Game)Gdx.app.getApplicationListener()).setScreen(new WaterLevel());
                        }else{
                            ((Game)Gdx.app.getApplicationListener()).setScreen(new MethaneLevel());
                        }

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
        stage.dispose();
        ButtonLoader.dispose();
    }
}
