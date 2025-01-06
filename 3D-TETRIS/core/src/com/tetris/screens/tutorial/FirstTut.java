package com.tetris.screens.tutorial;

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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tetris.game.helpers.AssetLoader;
import com.tetris.game.helpers.ButtonLoader;
import com.tetris.screens.Menu;

/**
 * Created by mkabi_000 on 8/1/2015.
 */
public class FirstTut implements Screen {
    //Members
    Button music, sound;
    TextButton back, next;
    PerspectiveCamera cam;
    ModelBatch modelBatch;
    Game game;
    AssetLoader assets;
    ButtonLoader buttonLoader;
    Image tutorialImage;
    Stage stage;

    //Constructor
    public FirstTut(Game game, AssetLoader assets, ButtonLoader buttons, PerspectiveCamera cam, ModelBatch batch,
                    Button music, Button sound){
        this.game = game;
        this.assets = assets;
        this.cam = cam;
        this.modelBatch = batch;
        this.music = music;
        this.sound = sound;
        this.buttonLoader = buttons;
    }
    @Override
    public void show() {
        //Set stage
        stage = new Stage();

        //Set the image
        tutorialImage = new Image(assets.tut1);
        tutorialImage.setWidth(Gdx.graphics.getWidth());
        tutorialImage.setHeight(Gdx.graphics.getHeight());

        //Add all components
        stage.addActor(tutorialImage);
        stage.addActor(music);
        stage.addActor(sound);
        back = buttonLoader.generateBackButton("Back");
        next = buttonLoader.generateForwardButton("Next");
        stage.addActor(back);
        stage.addActor(next);

        //Add Listeners
        back.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!sound.isChecked()) {
                    assets.interact.play();
                }
                game.setScreen(new Menu(game,assets,buttonLoader,music,sound));
                return true;
            }
        });

        next.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!sound.isChecked()) {
                    assets.interact.play();
                }
                game.setScreen(new SecondTut(game, assets, buttonLoader, cam, modelBatch,music,sound));
                return true;
            }
        });

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
