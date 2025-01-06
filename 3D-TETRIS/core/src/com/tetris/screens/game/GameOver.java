package com.tetris.screens.game;

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
import com.tetris.screens.Menu;

/**
 * Created by mkabi_000 on 8/1/2015.
 */
public class GameOver implements Screen {
    Game game;
    int points;
    Label l1,l2,l3;
    AssetLoader assets;
    PerspectiveCamera cam;
    TextButton mainmenu,replay;
    Label.LabelStyle style, style2, style3;
    Button music, sound;
    ModelBatch modelBatch;
    Stage stage;
    ButtonLoader buttons;

    public GameOver(int score, Game game, AssetLoader assets, ButtonLoader buttonLoader, Button music, Button sound){
        this.game = game;
        this.assets = assets;
        this.music = music;
        this.sound = sound;
        points = score;
        buttons = buttonLoader;

    }

    @Override
    public void show() {
        //Setup
        modelBatch = new ModelBatch();
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
        stage = new Stage();
        stage.addActor(music);
        stage.addActor(sound);
        style = new Label.LabelStyle(assets.font, Color.WHITE);
        style2 = new Label.LabelStyle(assets.font,Color.GREEN);
        style3 = new Label.LabelStyle(assets.font,Color.PURPLE);

        //Labels
        l1 = new Label("Game Over!",style);
        l1.setPosition(Gdx.graphics.getWidth() / 4, (Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 6));
        stage.addActor(l1);
        l2 = new Label("Score: "+Integer.toString(points),style2);
        l2.setPosition(Gdx.graphics.getWidth() / 4, (Gdx.graphics.getHeight() / 2) + Gdx.graphics.getHeight() / 6);
        stage.addActor(l2);
        if(points > assets.getHighScore()){
            assets.setHighScore(points);
            l3 = new Label("New High Score!",style3);
            l3.setPosition(Gdx.graphics.getWidth()/8, (Gdx.graphics.getHeight() / 3)+Gdx.graphics.getHeight()/6);
        }
        else{
            l3 = new Label("High Score: "+assets.getHighScore(),style3);
            l3.setPosition(Gdx.graphics.getWidth()/4, (Gdx.graphics.getHeight() / 3)+Gdx.graphics.getHeight()/6);
        }
        stage.addActor(l3);

        //Buttons
        mainmenu = new TextButton("Replay",assets.buttonStyle);
        mainmenu.setWidth(Gdx.graphics.getWidth() / 2);
        mainmenu.setHeight(Gdx.graphics.getHeight() / 3);
        mainmenu.setPosition(Gdx.graphics.getWidth()/6-music.getHeight(), Gdx.graphics.getHeight() / 4 - mainmenu.getHeight());

        mainmenu.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!sound.isChecked()) {
                    assets.interact.play();
                }
                game.setScreen(new Difficulty(game,assets,buttons,music,sound,cam, modelBatch));
                return true;
            }
        });
        stage.addActor(mainmenu);

        replay = new TextButton("Menu", assets.buttonStyle);
        replay.setWidth(Gdx.graphics.getWidth() / 2);
        replay.setHeight(Gdx.graphics.getHeight() / 3);
        replay.setPosition(Gdx.graphics.getWidth() - replay.getWidth()-music.getWidth(), Gdx.graphics.getHeight() / 4 - mainmenu.getHeight());

        replay.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!sound.isChecked()) {
                    assets.interact.play();
                }
                game.setScreen( new Menu(game,assets,buttons,music,sound));
                return true;
            }
        });
        stage.addActor(replay);

        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        //Basic Setup
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(30 / 255f, 144 / 255f, 255 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL20.GL_BACK);

        //Rendering
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
