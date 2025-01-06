package com.bms.chemtris.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.bms.chemtris.asset.ButtonLoader;
import com.bms.chemtris.asset.LabelLoader;
import com.bms.chemtris.asset.TextureLoader;
import com.bms.chemtris.elements.Element;
import com.bms.chemtris.game.ScoreManager;
import com.bms.chemtris.helpers.Controller;
import com.bms.chemtris.helpers.WaterController;
import com.bms.chemtris.helpers.WaterSpawner;
import com.bms.chemtris.helpers.WaterStorage;
import com.bms.chemtris.models.Platform;
import com.bms.chemtris.render.Background;
import com.bms.chemtris.render.ParticleRender;
import com.bms.chemtris.tween.ActorAccessor;

import java.util.Random;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

/**
 * screen to display methane level (ch4).
 */
public class MethaneLevel implements Screen {
    //game state members
    private enum GameState{
        CHECK,SPAWN,CONTROL,LOSE,PAUSE,INIT, TUTORIAL, QUIT
    }

    //game objects
    private Platform platform;
    private Element current;
    private WaterStorage storage;
    private WaterController controller;
    private WaterSpawner spawner;
    private int score = 0;
    private GameState gameState;
    private GameState preState;
    private float done = 0f;
    private boolean leaving = false;
    private PerspectiveCamera camera;
    private CameraInputController camController;
    private boolean [][][] matrix;

    //UI members
    private Stage stage, tutorialStage;
    private TextButton up, down, left, right, cam , descend, question, quit, yes, no, rx, ry, rz;
    private Button pause;
    private Label scoreLabel, textLabel;
    private Image tutorialImage;
    private TweenManager tweenManager;
    private InputMultiplexer inputs;
    private Random rand;
    private int hint_itr = -1;
    private String[] praises = {"Awesome", "Good Job", "Nice", "Yesss", "Great", "Wohoo"};
    private ParticleRender particler;

    @Override
    public void show() {
        //setup random class
        rand = new Random();

        //setup camera
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(12f, 12f, 12f);
        camera.lookAt(0, 0, 0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();
        camController = new CameraInputController(camera);
        camController.target.set(3f,5f,3f);
        camController.rotateAngle = 90f;

        //setup collision matrix
        matrix = new boolean[8][20][8];

        //setup platform
        platform = new Platform(8f,camera,matrix);

        storage = new WaterStorage();
        controller = new WaterController(storage,30f,platform.box);
        spawner = new WaterSpawner(matrix,camera);

        //make stages
        stage = new Stage();
        particler = new ParticleRender(ParticleRender.yellow,stage);
        tutorialStage = new Stage();

        //make UI
        up  = ButtonLoader.makeUp();
        down = ButtonLoader.makeDown();
        right = ButtonLoader.makeRight();
        left = ButtonLoader.makeLeft();
        descend = ButtonLoader.makeDesc();
        cam = ButtonLoader.cam();
        pause = ButtonLoader.pause();
        question = ButtonLoader.question();
        quit = ButtonLoader.quit();
        yes = ButtonLoader.yes();
        no = ButtonLoader.no();
        rx = ButtonLoader.rx();
        ry = ButtonLoader.ry();
        rz = ButtonLoader.rz();
        tutorialImage = new Image(TextureLoader.getCh4());
        tutorialImage.setHeight(Gdx.graphics.getHeight());
        tutorialImage.setWidth(Gdx.graphics.getWidth());
        scoreLabel = LabelLoader.scoreL();
        textLabel = LabelLoader.txtL();
        textLabel.setText("CH4");

        //add members to the main stage
        stage.addActor(left);
        stage.addActor(right);
        stage.addActor(up);
        stage.addActor(down);
        stage.addActor(descend);
        stage.addActor(rx);
        stage.addActor(ry);
        stage.addActor(rz);
        stage.addActor(cam);
        stage.addActor(pause);
        stage.addActor(scoreLabel);
        stage.addActor(question);
        stage.addActor(textLabel);
        stage.addActor(quit);
        stage.addActor(yes);
        stage.addActor(no);

        //add members to the tutorial stage
        tutorialStage.addActor(tutorialImage);

        //fade out non main buttons
        yes.addAction(Actions.fadeOut(0f));
        no.addAction(Actions.fadeOut(0f));

        //setup multiplexer
        inputs = new InputMultiplexer();
        inputs.addProcessor(stage);
        inputs.addProcessor(camController);
        Gdx.input.setInputProcessor(inputs);

        //right button
        right.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(gameState == GameState.CONTROL){
                    current.moveRight();
                    return true;
                }
                return false;
            }
        });

        left.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(gameState == GameState.CONTROL){
                    current.moveLeft();
                    return true;
                }
                return false;
            }
        });

        up.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(gameState == GameState.CONTROL){
                    current.moveUp();
                    return true;
                }
                return false;
            }
        });

        down.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(gameState == GameState.CONTROL){
                    current.moveDown();
                    return true;
                }
                return false;
            }
        });

        rx.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(gameState == GameState.CONTROL){
                    current.rotateX();
                    return true;
                }
                return false;
            }
        });

        ry.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(gameState == GameState.CONTROL){
                    current.rotateY();
                    return true;
                }
                return false;
            }
        });

        rz.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(gameState == GameState.CONTROL){
                    current.rotateZ();
                    return true;
                }
                return false;
            }
        });

        descend.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(gameState == GameState.CONTROL){
                    current.descend();
                    return true;
                }
                return false;
            }
        });

        cam.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                camera.view.idt();
                camera.position.set(12f, 12f, 12f);
                camera.lookAt(0, 0, 0);
                camera.near = 1f;
                camera.far = 300f;
                camera.up.set(0f, 1f, 0f);
                camera.update();
                return true;
            }
        });

        //game pause button
        pause.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if(gameState == GameState.PAUSE){
                    gameState = preState;
                }
                else{
                    preState = gameState;
                    textLabel.setText("Game Paused");
                    textLabel.addAction(Actions.fadeIn(1.5f));
                    gameState = GameState.PAUSE;
                }

                return true;
            }
        });

        //game quit button
        quit.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if(gameState != GameState.QUIT){
                    preState = gameState;
                    textLabel.setText("Quit?");
                    textLabel.addAction(Actions.fadeIn(2f));
                    yes.addAction(Actions.fadeIn(1f));
                    no.addAction(Actions.fadeIn(1f));
                    gameState = GameState.QUIT;
                }

                return true;
            }
        });

        //quit confirm yes button
        yes.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gameState = GameState.LOSE;
                no.addAction(Actions.fadeOut(1f));
                yes.addAction(Actions.fadeOut(1f));
                return true;
            }
        });

        //quit confirm no button
        no.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gameState = preState;
                textLabel.addAction(Actions.fadeOut(1f));
                no.addAction(Actions.fadeOut(1f));
                yes.addAction(Actions.fadeOut(1f));
                return true;
            }
        });

        //hint button
        question.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                preState = gameState;
                gameState = GameState.TUTORIAL;

                Tween.to(tutorialImage, ActorAccessor.ALPHA,1f).target(1).start(tweenManager);
                Tween.to(tutorialImage, ActorAccessor.ALPHA,1f).target(0).delay(5f).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        gameState = preState;
                    }
                }).start(tweenManager);

                return true;
            }
        });

        //setup game state to initialization
        gameState = GameState.INIT;


        //setup twin engine
        tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new ActorAccessor());

        //fade out tutorial image
        Tween.set(tutorialImage,ActorAccessor.ALPHA).target(0).start(tweenManager);


        //start initializing animation
        animateIn();
    }

    @Override
    public void render(float delta) {
        Background.render();
        tweenManager.update(delta);

        //render main stage if not tutorial
        if(gameState != GameState.TUTORIAL){
            stage.act(delta);
            stage.draw();
        }//otherwise render tutorial screen
        else{
            tutorialStage.act(delta);
            tutorialStage.draw();
        }

        //render platform and pieces if not initializing or tutorial
        if(gameState != GameState.INIT){
            if(gameState != GameState.TUTORIAL){
                done += delta;
                storage.render();
                platform.render();
            }
        }

        //check game state
        switch (gameState) {
            //Game Over
            case LOSE:
                //set label
                textLabel.setText("Game Over");
                textLabel.addAction(Actions.fadeIn(1f));

                //leave screen
                if(!leaving){
                    leaveScreen();
                    leaving = true;
                }
                break;

            case CHECK:
                int points = storage.get_score();
                if(score < points){
                    int index = rand.nextInt(5);
                    textLabel.setText(praises[index]);
                    textLabel.addAction(Actions.fadeIn(1f));
                    scoreLabel.setText(Integer.toString(points));
                    scoreLabel.addAction(Actions.fadeOut(0f));
                    scoreLabel.addAction(Actions.fadeIn(1f));
                    particler.renderParticle();
                }

                score = points;
                if(score > ScoreManager.getScore(ScoreManager.CH4)){
                    ScoreManager.setScore(ScoreManager.CH4,score);
                    textLabel.setText("High Score");
                    textLabel.addAction(Actions.fadeIn(1f));
                }

                if(particler.isFinished) {
                    gameState = GameState.SPAWN;
                }else{
                    particler.renderParticle();
                }
                break;

            case SPAWN:
                current = spawner.spawn2();
                gameState = GameState.CONTROL;
                break;

            case CONTROL:
                if(hint_itr == -1){
                    textLabel.addAction(Actions.fadeOut(2f));
                }
                int c_stat = controller.run(delta,current);
                switch (c_stat) {
                    case Controller.DONE:
                        try {
                            if (spawner.current == WaterSpawner.C) {
                                storage.add_carbon(current);
                            } else {
                                storage.add_methane_h(current);
                            }
                        }catch (Exception e){
                            gameState = GameState.LOSE;
                        }

                        boolean over = storage.over;

                        if(over){
                            gameState = GameState.LOSE;
                        }
                        else{
                            gameState  = GameState.CHECK;
                        }
                        break;
                }
                try {
                    current.renderParts();
                    current.renderShadow();
                    current.traceFrame();
                    current.renderShadow();
                }catch (Exception e){
                    gameState = GameState.LOSE;
                }
                break;
        }

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
        TextureLoader.disposeCh4();
        stage.dispose();
        tutorialStage.dispose();
    }

    public void animateIn(){
        //create sequential animation
        Timeline.createSequence().beginSequence().
                push(Tween.set(up,ActorAccessor.ALPHA).target(0)).
                push(Tween.set(down,ActorAccessor.ALPHA).target(0)).
                push(Tween.set(left,ActorAccessor.ALPHA).target(0)).
                push(Tween.set(right,ActorAccessor.ALPHA).target(0)).
                push(Tween.set(descend,ActorAccessor.ALPHA).target(0)).
                push(Tween.set(pause,ActorAccessor.ALPHA).target(0)).
                push(Tween.set(cam,ActorAccessor.ALPHA).target(0)).
                push(Tween.set(rx,ActorAccessor.ALPHA).target(0)).
                push(Tween.set(ry,ActorAccessor.ALPHA).target(0)).
                push(Tween.set(rz,ActorAccessor.ALPHA).target(0)).
                push(Tween.set(quit,ActorAccessor.ALPHA).target(0)).
                push(Tween.set(scoreLabel,ActorAccessor.ALPHA).target(0)).
                push(Tween.set(question,ActorAccessor.ALPHA).target(0)).
                push(Tween.from(textLabel, ActorAccessor.ALPHA, 1f).target(0)).
                push(Tween.to(up,ActorAccessor.ALPHA,.5f).target(1)).
                push(Tween.to(down,ActorAccessor.ALPHA,.5f).target(1)).
                push(Tween.to(right,ActorAccessor.ALPHA,.5f).target(1)).
                push(Tween.to(left,ActorAccessor.ALPHA,.5f).target(1)).
                push(Tween.to(descend,ActorAccessor.ALPHA,.5f).target(1)).
                push(Tween.to(textLabel,ActorAccessor.ALPHA,.5f).target(0)).
                push(Tween.to(rx,ActorAccessor.ALPHA,.5f).target(1)).
                push(Tween.to(ry,ActorAccessor.ALPHA,.5f).target(1)).
                push(Tween.to(rz,ActorAccessor.ALPHA,.5f).target(1)).
                push(Tween.to(pause,ActorAccessor.ALPHA,.5f).target(1)).
                push(Tween.to(cam,ActorAccessor.ALPHA,.5f).target(1)).
                push(Tween.to(quit,ActorAccessor.ALPHA,.5f).target(1)).
                push(Tween.to(question,ActorAccessor.ALPHA,.5f).target(1)).
                push(Tween.to(scoreLabel,ActorAccessor.ALPHA,.5f).target(1)).end().setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                gameState = GameState.CHECK;
            }
        }).start(tweenManager);
    }

    //method to leave the screen
    private void leaveScreen(){
        Timeline.createParallel().beginParallel()
                .push(Tween.to(right, ActorAccessor.Y, 1.15f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(left, ActorAccessor.Y, 1.15f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(up, ActorAccessor.Y, 1.25f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(down, ActorAccessor.Y, 1f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(descend, ActorAccessor.Y, 1.15f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(rx, ActorAccessor.Y, 1.15f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(ry, ActorAccessor.Y, 1.15f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(rz, ActorAccessor.Y, 1.15f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(pause, ActorAccessor.Y, 1.5f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(cam, ActorAccessor.Y, 1.25f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(quit, ActorAccessor.Y, 1.15f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(question, ActorAccessor.Y, 1.5f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(scoreLabel, ActorAccessor.Y, 1.5f).target(-Gdx.graphics.getHeight()))
                .push(Tween.to(textLabel, ActorAccessor.Y, 2.5f).target(-Gdx.graphics.getHeight())).end().setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameOver(score, ScoreManager.CH4,done));
            }
        }).start(tweenManager);
    }
}
