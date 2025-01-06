package com.tetris.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tetris.game.helpers.AssetLoader;
import com.tetris.game.helpers.ButtonLoader;
import com.tetris.screens.game.Difficulty;
import com.tetris.screens.game.MainGame;
import com.tetris.screens.tutorial.FirstTut;

/**
 * Created by mkabi_000 on 8/1/2015.
 */
public class Menu implements Screen {
    //Members
    AssetLoader assets;
    Label title;
    Label.LabelStyle titleStyle;
    Stage stage;
    PerspectiveCamera cam;
    TextButton play, tutorial;
    Game game;
    Button music, sound;
    ModelBatch modelBatch;
    ButtonLoader buttonLoader;

    //Constructor
    public Menu(Game game,AssetLoader assets, ButtonLoader buttons, Button music, Button sound){
        this.game = game;
        this.assets = assets;
        this.music = music;
        this.sound = sound;
        buttonLoader = buttons;
    }

    @Override
    public void show() {
        //Basic Setup
        modelBatch = new ModelBatch();
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        //Adding components to stage
        stage = new Stage();
        stage.addActor(music);
        stage.addActor(sound);
        buttonLoader.generateMenuButtons();
        play = buttonLoader.menuPlay;
        tutorial = buttonLoader.menuTut;

        //Adding Listener to buttons
        play.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!sound.isChecked()) {
                    assets.interact.play();
                }
                game.setScreen(new Difficulty(game,assets,buttonLoader,music,sound,cam, modelBatch));
                return true;
            }
        });

        tutorial.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!sound.isChecked()) {
                    assets.interact.play();
                }
               game.setScreen(new FirstTut(game,assets,buttonLoader,cam,modelBatch,music,sound));
                return true;
            }
        });

        stage.addActor(play);
        stage.addActor(tutorial);

        //Add Title
        titleStyle = new Label.LabelStyle(assets.largeFont, Color.WHITE);
        title = new Label("3D TETRIS",titleStyle);
        title.setPosition(Gdx.graphics.getWidth() / 2-title.getWidth()/2, (Gdx.graphics.getHeight() / 2) + (Gdx.graphics.getHeight() / 3));
        stage.addActor(title);

        //Set the stage as the input
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(30 / 255f, 144 / 255f, 255 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL20.GL_BACK);
        modelBatch.begin(cam);
        modelBatch.end();
        stage.act();
        stage.draw();
        if(!music.isChecked()){
            assets.playSong();
        }
        else{
            assets.stopSong();
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
        stage.dispose();
        assets.dispose();
    }
}
