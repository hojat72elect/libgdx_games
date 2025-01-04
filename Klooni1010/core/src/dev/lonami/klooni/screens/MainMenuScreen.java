package dev.lonami.klooni.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dev.lonami.klooni.Klooni;
import dev.lonami.klooni.actors.SoftButton;

// Main menu screen, presenting some options (play, customize…)
public class MainMenuScreen extends InputListener implements Screen {

    private static final float minDelta = 1 / 30f;
    private final Klooni game;
    private final Stage stage;

    public MainMenuScreen(Klooni game) {
        this.game = game;

        stage = new Stage();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Play button
        final SoftButton playButton = new SoftButton(
                0, GameScreen.hasSavedData() ? "play_saved_texture" : "play_texture");
        playButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuScreen.this.game.transitionTo(
                        new GameScreen(MainMenuScreen.this.game, GameScreen.GAME_MODE_SCORE));
            }
        });
        table.add(playButton).colspan(3).fill().space(16);

        table.row();

        final SoftButton starButton = new SoftButton(1, "star_texture");
        starButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.net.openURI("https://github.com/LonamiWebs/Klooni1010/stargazers");
            }
        });
        table.add(starButton).space(16);

        // Time mode
        final SoftButton statsButton = new SoftButton(2, "stopwatch_texture");
        statsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuScreen.this.game.transitionTo(
                        new GameScreen(MainMenuScreen.this.game, GameScreen.GAME_MODE_TIME));
            }
        });
        table.add(statsButton).space(16);

        // Palette button (buy colors)
        final SoftButton paletteButton = new SoftButton(3, "palette_texture");
        paletteButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                // Don't dispose because then it needs to take us to the previous screen
                MainMenuScreen.this.game.transitionTo(new CustomizeScreen(MainMenuScreen.this.game, MainMenuScreen.this.game.getScreen()), false);
            }
        });
        table.add(paletteButton).space(16);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Klooni.theme.glClearBackground();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), minDelta));
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
    //region Unused methods

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}