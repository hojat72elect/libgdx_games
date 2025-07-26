package com.salvai.snake.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.salvai.snake.SnakeIt;
import com.salvai.snake.input.CatchBackKeyProcessor;
import com.salvai.snake.utils.Constants;
import com.salvai.snake.utils.Text;


public class LevelChooseScreen extends ScreenAdapter {

    public final SnakeIt game;
    private final int COLUMNS = 4;
    private final int TABS = 3;
    float width;
    float height;
    private Table tableTabs;
    private final Array<Table> levelTables;
    private final Array<Label> levelLabels;
    private final ButtonGroup<TextButton> tabs;
    private ScrollPane scrollPane;


    public LevelChooseScreen(SnakeIt gameClass) {
        game = gameClass;

        levelLabels = new Array<Label>();
        levelTables = new Array<Table>();

        tabs = new ButtonGroup<TextButton>();

        width = game.worldWidth;
        height = game.worldHeight;

        game.stage.clear();
        game.setUpTopBar(Constants.SCREEN.LEVELCHOOSE);

        setUpTabs();

        setUpLevelTables();


        setUpScrollPane();

        setUpInputMultiplexer();


        game.stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(Constants.FADE_TIME)));
    }

    private void setUpScrollPane() {
        scrollPane = new ScrollPane(levelTables.get(game.selectedLevelTab), game.skin);
        scrollPane.setSize(width, height * 0.7f);
        scrollPane.setPosition(0, height * 0.1f);
        game.stage.addActor(scrollPane);
    }


    private void setUpInputMultiplexer() {
        Gdx.input.setCatchBackKey(true);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(game.stage);
        multiplexer.addProcessor(game.topBarStage);
        multiplexer.addProcessor(new CatchBackKeyProcessor(game, this)); // Your screen
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void setUpTabs() {
        tableTabs = new Table(game.skin);
        tableTabs.setSize(width, height * 0.1f);
        tableTabs.setPosition(0, height * 0.8f);
        tableTabs.defaults().uniformX().spaceBottom(height * 0.1f).size(width * 0.33f, height * 0.05f);
        tableTabs.row();
        for (int i = 0; i < TABS; i++) {
            final int tabIndex = i;
            TextButton tab = new TextButton(Text.LEVEL_TABS[i], game.skin);

            if (game.selectedLevelTab == i)
                tab.setChecked(true);

            tab.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {


                    Action changeTable = new Action() {
                        public boolean act(float delta) {
                            scrollPane.setActor(levelTables.get(tabIndex));
//                            updateTabColor();
                            return true;
                        }
                    };

                    scrollPane.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), changeTable, Actions.fadeIn(Constants.FADE_TIME)));

                    game.setSelectedLevelTab(tabIndex);
                }
            });
            tabs.add(tab);

            tableTabs.add(tab);
        }
        game.stage.addActor(tableTabs);
    }


    private void setUpLevelTables() {
        int levelCounter = 0;
        for (int i = 0; i < TABS; i++) {
            Table levelTable = new Table(game.skin);
            levelTable.defaults().space(0, width * 0.05f, 0, width * 0.05f);
            levelTable.row();
            for (int j = 0; j < Constants.MAX_LEVEL / TABS; j++) {
                setUpLevelPreviews(levelTable, levelCounter);
                if ((j + 1) % COLUMNS == 0) {
                    levelTable.row();
                    setUpLevelLabels(levelTable, levelCounter);
                }
                levelCounter++;
            }
            levelTables.add(levelTable);
        }
    }

    private void setUpLevelPreviews(Table levelTable, final int level) {
        final Image levelPreview = new Image(game.assetsManager.manager.get(Constants.LEVEL_PREVIEW + (level + 1) + ".png", Texture.class));
        levelPreview.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.level = level;
                game.stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new GameScreen(game));
                        dispose();
                    }
                })));
            }
        });
        levelTable.add(levelPreview).size(width * 0.2f, height * 0.2f);
    }

    private void setUpLevelLabels(Table levelTable, int end) {
        for (int i = end - (COLUMNS - 1); i <= end; i++) {
            final int level = i;
            final Label levelLabel = new Label("" + game.highScores[level], game.skin, "level");
            levelLabel.setAlignment(Align.center);
            levelTable.add(levelLabel).size(width * 0.2f, height * 0.04f).spaceBottom(width * 0.05f);
            levelLabels.add(levelLabel);
        }
        levelTable.row();
    }


    @Override
    public void render(float delta) {
        setupScreen();
        game.draw(delta);
    }

    private void setupScreen() {
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g, Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        game.stage.getViewport().update(width, height, true);
    }
}
