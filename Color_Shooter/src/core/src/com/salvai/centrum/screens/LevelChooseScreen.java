package com.salvai.centrum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.salvai.centrum.CentrumGameClass;
import com.salvai.centrum.enums.GameType;
import com.salvai.centrum.input.CatchBackKeyProcessor;
import com.salvai.centrum.utils.Constants;

public class LevelChooseScreen extends ScreenAdapter {

    private final CentrumGameClass game;
    private final int COLUMNS = 5;
    private final int COLORS = 4;
    private final float SIZE = 0.15f; //size of buttons % to screen height
    public Stage stage;
    float width;
    float height;
    private Table table;

    public LevelChooseScreen(CentrumGameClass gameClass) {
        game = gameClass;

        height = Gdx.graphics.getHeight() * 0.8f;
        width = Gdx.graphics.getWidth() * 0.9f;

        stage = new Stage();

        table = new Table(game.skin);
        table.setSize(width, height);
        table.setPosition(Gdx.graphics.getWidth() * 0.5f - width * 0.5f, Gdx.graphics.getHeight() * 0.5f - height * 0.5f);

        setUpTable();


        //.setDebug(true);

        ScrollPane scrollPane = new ScrollPane(table, game.skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setSize(width, height);
        scrollPane.setPosition(Gdx.graphics.getWidth() * 0.5f - width * 0.5f, Gdx.graphics.getHeight() * 0.5f - height * 0.5f);
        stage.addActor(scrollPane);


        Gdx.input.setCatchBackKey(true);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new CatchBackKeyProcessor(game, this)); // Your screen
        Gdx.input.setInputProcessor(multiplexer);


        stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(Constants.FADE_TIME)));
    }

    private void setUpTable() {

        Button endlessButton = new Button(game.skin, "infinity");
        endlessButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.gameType = GameType.ENDLESS;
                        if (game.showTutorial)
                            game.setScreen(new TutorialScreen(game));
                        else
                            game.setScreen(new GameScreen(game));
                        dispose();
                    }
                })));

            }
        });

        table.add(endlessButton).colspan(COLUMNS).pad(width * 0.01f).size(width * SIZE*3).spaceBottom(width * 0.05f);
        table.row();
        for (int i = 0; i < game.levels.size; i++) {
            setUpLevelButtons(i, game.levelStars[i]);
            if ((i + 1) % COLUMNS == 0)
                setupStarsRow(i, game.levelStars);
        }
    }

    private void setUpLevelButtons(final int level, int levelStars) {
        if (levelStars >= 0) {
            TextButton levelButton = new TextButton("" + (level + 1), game.skin, "level-" + ((level * 1) % COLORS));
            levelButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            game.level = level;
                            if (game.showTutorial)
                                game.setScreen(new TutorialScreen(game));
                            else
                                game.setScreen(new GameScreen(game));
                            dispose();
                        }
                    })));

                }
            });
            table.add(levelButton).size(width * SIZE).pad(width * 0.01f);
        } else { //locked
            Button levelButton = new Button(game.skin, "locked-" + ((level * 1) % COLORS));
            table.add(levelButton).size(width * SIZE).pad(width * 0.01f);
        }
    }

    private void setupStarsRow(final int i, int[] levelStars) {
        //new row
        if ((i + 1) % COLUMNS == 0) {
            table.row().padTop(-height * 0.04f).height(height * 0.04f).padBottom(0); //TODO fix stars
            //draw stars
            for (int j = i - COLUMNS + 1; j <= i; j++) {
                if (levelStars[j] >= 0) {
                    Button starsButton;
                    switch (levelStars[j]) {
                        case 3:
                            starsButton = new Button(game.skin, "three-star");
                            break;
                        case 2:
                            starsButton = new Button(game.skin, "two-star");
                            break;
                        case 1:
                            starsButton = new Button(game.skin, "one-star");
                            break;
                        default:
                            starsButton = new Button(game.skin, "no-star");
                            break;
                    }
                    starsButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {

                            stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    game.level = i;
                                    if (game.showTutorial)
                                        game.setScreen(new TutorialScreen(game));
                                    else
                                        game.setScreen(new GameScreen(game));
                                    dispose();
                                }
                            })));

                        }
                    });
                    table.add(starsButton);
                }
            }
            table.row();
        }
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
