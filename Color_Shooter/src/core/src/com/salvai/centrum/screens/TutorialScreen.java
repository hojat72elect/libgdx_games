package com.salvai.centrum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.salvai.centrum.CentrumGameClass;
import com.salvai.centrum.utils.Constants;

public class TutorialScreen extends ScreenAdapter {


    private Stage stage;
    private Table table;
    private CentrumGameClass game;
    private Label textLabel;
    private float height;
    private float width;

    //buttons
    private Button playButton;

    public TutorialScreen(CentrumGameClass gameClass) {
        game = gameClass;

        height = Gdx.graphics.getHeight() * 0.8f;
        width = Gdx.graphics.getWidth() * 0.6f;

        stage = new Stage();

        setUpMainButtons();

        textLabel = new Label("-PROTECT WHITE-\n\n-TAP TO SHOOT-\n-OTHER COLORS-", game.skin, "default");
        textLabel.setAlignment(Align.center);

        setUpTable();
        //        table.setDebug(true);

        stage.addActor(table);

        Gdx.input.setCatchBackKey(false);
        Gdx.input.setInputProcessor(stage);

        stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(Constants.FADE_TIME)));
    }

    private void setUpTable() {
        table = new Table(game.skin);
        table.setSize(width, height);
        table.setPosition(Gdx.graphics.getWidth() * 0.5f - width * 0.5f, Gdx.graphics.getHeight() * 0.5f - height * 0.5f);


        table.add(textLabel).size(width, height * 0.3f).spaceBottom(height * 0.1f);
        table.row();
        table.add(playButton).size(height * 0.1f, height * 0.1f);
    }

    private void setUpMainButtons() {
        playButton = new Button(game.skin, "play");
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.showTutorial = false;
                        game.setScreen(new GameScreen(game));
                        dispose();
                    }
                })));

            }
        });
    }

    @Override
    public void render(float delta) {
        setupScreen();
        game.batch.begin();
        game.drawBackground(delta);
        game.batch.end();
        stage.act(delta);
        stage.draw();
    }

    private void setupScreen() {
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g, Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }


    @Override
    public void dispose() {
        stage.dispose();
    }

}
