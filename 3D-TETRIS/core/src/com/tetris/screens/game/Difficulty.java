package com.tetris.screens.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tetris.game.helpers.AssetLoader;
import com.tetris.game.helpers.ButtonLoader;

/**
 * Created by mkabi_000 on 8/9/2015.
 */
public class Difficulty implements Screen {
    Button music, sound;
    ButtonLoader buttonLoader;
    TextButton easy,hard;
    PerspectiveCamera cam;
    ModelBatch modelBatch;
    Game game;
    AssetLoader assets;
    Stage stage;

    public Difficulty(Game game,AssetLoader assets,ButtonLoader buttons,Button music, Button sound,PerspectiveCamera cam, ModelBatch batch){
        this.game = game;
        this.assets = assets;
        buttonLoader = buttons;
        this.modelBatch = batch;
        this.cam = cam;
        this.music = music;
        this.sound = sound;
    }
    @Override
    public void show() {
        //Set stage and add pre-made buttons
        stage = new Stage();
        stage.addActor(music);
        stage.addActor(sound);

        //Setup the difficulty buttons
        easy = new TextButton("EASY",assets.buttonStyle);
        easy.setWidth(Gdx.graphics.getWidth() / 2);
        easy.setHeight(Gdx.graphics.getHeight() / 2);
        easy.setPosition((Gdx.graphics.getWidth() / 2 - easy.getWidth() / 2 + music.getWidth() / 4), (Gdx.graphics.getHeight() / 2 - easy.getHeight() / 4));

        hard = new TextButton("HARD",assets.buttonStyle);
        hard.setWidth(Gdx.graphics.getWidth() / 2);
        hard.setHeight(Gdx.graphics.getHeight() / 2);
        hard.setPosition((Gdx.graphics.getWidth() / 2 - easy.getWidth() / 2 + music.getWidth() / 4),(Gdx.graphics.getHeight() / 5 - hard.getHeight() / 2));

        //Add buttons to stage and add listeners
        stage.addActor(easy);
        stage.addActor(hard);
        easy.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!sound.isChecked()) {
                    assets.interact.play();
                }
                game.setScreen(new MainGameEasy(game, assets, buttonLoader, music, sound));
                return true;
            }
        });
        hard.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!sound.isChecked()) {
                    assets.interact.play();
                }
                game.setScreen(new MainGame(game, assets, buttonLoader, music, sound));
                return true;
            }
        });

        //Set input processor
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
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        modelBatch.dispose();
        assets.dispose();
    }
}
