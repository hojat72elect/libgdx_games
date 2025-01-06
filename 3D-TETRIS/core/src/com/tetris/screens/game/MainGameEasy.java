package com.tetris.screens.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.tetris.game.gameobjects.PiecesEasy;
import com.tetris.game.gameobjects.Platform;
import com.tetris.game.helpers.AssetLoader;
import com.tetris.game.helpers.ButtonLoader;
import com.tetris.game.helpers.Helper;
import com.tetris.game.helpers.Keyboard;

/**
 * Created by mkabi_000 on 8/10/2015.
 */
public class MainGameEasy implements Screen {
    //Members
    Game game;
    Environment environment;
    PerspectiveCamera cam;
    CameraInputController camController;
    ModelBatch modelBatch;
    Array<PiecesEasy> pieces;
    boolean[][][]fixed;
    boolean gameOver = false;
    Button music,sound,play,clock;
    TextButton up,down,left,right,a,rx,ry,rz,cameraB, quit;
    Label score;
    Label.LabelStyle style;
    Stage stage;
    AssetLoader assets;
    Platform platform;
    Array<ModelInstance>allInstances;
    InputMultiplexer inputs;
    float timeLasped = 0f;
    float interval;
    int index = 0;
    Helper helper;
    Keyboard keyboard;
    ButtonLoader buttons;

    //Constructor
    public MainGameEasy(Game game, AssetLoader assets, ButtonLoader buttons, Button music,Button sound){
        this.game = game;
        this.assets = assets;
        this.buttons = buttons;
        this.music =  music;
        this.sound = sound;
        interval = 1f;
    }

    @Override
    public void show() {
        //Setup camera and environment
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        modelBatch = new ModelBatch();
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.translate(8f, 0, 10f);
        cam.update();
        camController = new CameraInputController(cam);
        camController.target = new Vector3(11f, 0f, 12f);

        //Setup stage and collision detection array
        stage = new Stage();
        fixed = new boolean[50][50][50];

        //Setup Platform
        platform = new Platform();
        platform.makeSmallPlatform();
        allInstances = new Array<ModelInstance>();
        Array<ModelInstance>platformInstance = new Array<ModelInstance>(platform.create());
        for(int i =0; i < platformInstance.size; i++){
            Vector3 temp = new Vector3();
            platformInstance.get(i).transform.getTranslation(temp);
            int x = Math.round(temp.x);
            int y = Math.round(temp.y);
            int z = Math.round(temp.z);
            fixed[x][y][z]= true;
            allInstances.add(platformInstance.get(i));
        }

        //Setup Buttons
        buttons.generateGameButtons();
        play = buttons.play;
        clock = buttons.clock;
        a = buttons.a;
        up =buttons.up;
        down = buttons.down;
        left = buttons.left;
        right = buttons.right;
        rx = buttons.rx;
        ry = buttons.ry;
        rz = buttons.rz;
        cameraB = buttons.cameraB;
        quit = buttons.quit;

        //Add buttons
        stage.addActor(music);
        stage.addActor(sound);
        stage.addActor(right);
        stage.addActor(up);
        stage.addActor(a);
        stage.addActor(down);
        stage.addActor(left);
        stage.addActor(rx);
        stage.addActor(ry);
        stage.addActor(rz);
        stage.addActor(play);
        stage.addActor(clock);
        stage.addActor(cameraB);

        //Make the first piece and setup helpers
        pieces = new Array<PiecesEasy>();
        helper = new Helper();
        pieces.add(helper.spawnPieceEasy());
        keyboard = new Keyboard();

        //Setup Input
        inputs = new InputMultiplexer();
        inputs.addProcessor(stage);
        inputs.addProcessor(camController);
        Gdx.input.setInputProcessor(inputs);

        //Setup Controls
        up.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!play.isChecked()) {
                    if (!sound.isChecked()) {
                        assets.interact.play();
                    }
                    pieces.get(index).up(fixed);
                }
                return true;
            }
        });
        down.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!play.isChecked()) {
                    if (!sound.isChecked()) {
                        assets.interact.play();
                    }
                    pieces.get(index).down(fixed);
                }
                return true;
            }
        });
        right.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!play.isChecked()) {
                    if (!sound.isChecked()) {
                        assets.interact.play();
                    }
                    pieces.get(index).right(fixed);
                }
                return true;
            }
        });
        left.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!play.isChecked()) {
                    if (!sound.isChecked()) {
                        assets.interact.play();
                    }
                    pieces.get(index).left(fixed);
                }
                return true;
            }
        });
        a.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!play.isChecked()) {
                    if (!sound.isChecked()) {
                        assets.interact.play();
                    }
                    pieces.get(index).descend(fixed);
                }
                return true;
            }
        });
        rx.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!play.isChecked()) {
                    if (!sound.isChecked()) {
                        assets.spin.play(Gdx.graphics.getWidth() * 2);
                    }
                    pieces.get(index).rotateX(fixed, !clock.isChecked());
                }
                return true;
            }
        });
        ry.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!play.isChecked()) {
                    if (!sound.isChecked()) {
                        assets.spin.play(Gdx.graphics.getWidth() * 2);
                    }
                    pieces.get(index).rotateY(fixed, !clock.isChecked());
                    Gdx.app.log("Pressed", "ry");
                }
                return true;
            }
        });
        rz.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!play.isChecked()) {
                    if (!sound.isChecked()) {
                        assets.spin.play(Gdx.graphics.getWidth() * 2);
                    }
                    pieces.get(index).rotateZ(fixed, !clock.isChecked());
                }
                return true;
            }
        });

        cameraB.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!play.isChecked()) {
                    if (!sound.isChecked()) {
                        assets.spin.play(Gdx.graphics.getWidth() * 2);
                    }

                }
                cam.view.idt();
                cam.position.set(10f, 10f, 10f);
                cam.lookAt(0, 0, 0);
                cam.near = 1f;
                cam.far = 300f;
                cam.translate(8f, 0, 10f);
                cam.view.idt();
                cam.up.set(0f, 1f, 0f);
                cam.update();
                return true;
            }
        });

        quit.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (play.isChecked()) {
                    if (!sound.isChecked()) {
                        assets.interact.play();
                    }

                }
                game.setScreen(new GameOver(helper.getScore(), game, assets, buttons, music, sound));
                return true;
            }
        });

        //Setup the score text
        style = new Label.LabelStyle(assets.largeFont, Color.WHITE);
        score = new Label(Integer.toString(helper.getScore()),style);
        score.setWidth(Gdx.graphics.getWidth() / 8);
        score.setHeight(Gdx.graphics.getHeight() / 8);
        score.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - score.getHeight());
        stage.addActor(score);


    }

    @Override
    public void render(float delta) {
        //Basic Setup and Back face culling
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(30 / 255f, 144 / 255f, 255 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL20.GL_BACK);

        //Rendering of models
        cam.update();
        modelBatch.begin(cam);
        modelBatch.render(allInstances, environment);
        modelBatch.render(pieces.get(index).create(), environment);
        pieces.get(index).createShadow(fixed);
        pieces.get(index).renderShadow(modelBatch, environment);
        helper.render(modelBatch,environment);
        modelBatch.end();
        stage.act();
        stage.draw();

        //Check Music
        if(music.isChecked()){
            assets.stopSong();
        }
        else{
            assets.playSong();
        }

        //Check if the game is over
        if(!gameOver){
            //Check if game is paused
            if(!play.isChecked()){

                //Initial game setup
                quit.remove();
                float elasped = Gdx.graphics.getDeltaTime();
                timeLasped += elasped;
                keyboard.interactEasy(sound,clock,play,assets,pieces.get(index),fixed);

                //Check animation timebase
                if(timeLasped >= interval){

                    //Check if piece can go down
                    boolean check = pieces.get(index).descend(fixed);
                    if(!check){

                        //If it can't then add it to collection and store its position values
                        Array<ModelInstance> instances = new Array<ModelInstance>(pieces.get(index).create());
                        for(int i = 0; i < instances.size; i++){
                            helper.addInstance(instances.get(i));
                            Vector3 temp = new Vector3();
                            instances.get(i).transform.getTranslation(temp);
                            int x = Math.round(temp.x);
                            int y = Math.round(temp.y);
                            int z =  Math.round(temp.z);
                            fixed[x][y][z] = true;
                        }

                        //Check if game is over
                        gameOver = helper.gameOver();
                        if(!gameOver) {

                            // Row clearing and scoring
                            boolean scoredPoints = helper.clearBlocksEasy(fixed);
                            while(scoredPoints) {
                                if (!sound.isChecked()) {
                                    assets.score.play();
                                }
                                scoredPoints = helper.clearBlocksEasy(fixed);
                            }
                            score.setText(Integer.toString(helper.getScore()));

                            //Setting high score
                            if (helper.getScore() > assets.getHighScore()) {
                                assets.setHighScore(helper.getScore());
                            }

                            //Reducing descend time after certain points
                            if(helper.getScore() >= 1){
                                interval = .8f;
                            }
                            else if(helper.getScore() >= 6){
                               interval = .7f;
                            }
                            else if(helper.getScore() >= 8){
                                interval = .6f;
                            }
                            else if(helper.getScore() >= 14){
                                interval = .5f;
                            }
                            pieces.add(helper.spawnPieceEasy());
                            index = pieces.size - 1;
                        }
                    }
                    //Reset animation timebase
                    timeLasped = 0f;
                }
            }
            else{
                //Add quit option when game is paused
                stage.addActor(quit);
            }
        }
        else{
            //If game is over
            game.setScreen(new GameOver(helper.getScore(), game, assets, buttons, music, sound));
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
        modelBatch.dispose();
        platform.dispose();
        stage.dispose();
        assets.dispose();
        pieces.get(index).dispose();
    }
}