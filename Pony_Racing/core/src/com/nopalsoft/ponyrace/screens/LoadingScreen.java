package com.nopalsoft.ponyrace.screens;

import com.badlogic.gdx.graphics.Color;
import com.nopalsoft.ponyrace.PonyRacingGame;
import com.nopalsoft.ponyrace.game.GameScreen;

public class LoadingScreen extends BaseScreen {
    Class<?> clase;

    int cargaActual;
    int nivelTiled;

    public LoadingScreen(PonyRacingGame game, Class<?> clase, int nivelTiled) {
        super(game);
        create(game, clase, nivelTiled);
    }

    public LoadingScreen(PonyRacingGame game, Class<?> clase) {
        super(game);
        create(game, clase, -100);
    }

    public void create(PonyRacingGame game, Class<?> clase, int nivelTiled) {
        this.clase = clase;
        this.game = game;
        cargaActual = 0;
        this.nivelTiled = nivelTiled;

        if (clase == MainMenuScreen.class) {
            assetsHandler.loadMenus();
        } else if (clase == LeaderboardChooseScreen.class) {
            assetsHandler.loadMenus();
        } else if (clase == WorldMapTiledScreen.class) {
            assetsHandler.loadMenus();
        } else if (clase == ShopScreen.class) {
            assetsHandler.loadMenus();
        } else if (clase == GameScreen.class) {
            assetsHandler.loadGameScreenTiled(nivelTiled);
        }
    }

    @Override
    public void update(float delta) {
        if (assetsHandler.update()) {

            if (clase == MainMenuScreen.class) {
                assetsHandler.cargarMenus();
                game.setScreen(new MainMenuScreen(game));
            } else if (clase == LeaderboardChooseScreen.class) {
                assetsHandler.cargarMenus();
                game.setScreen(new LeaderboardChooseScreen(game));
            } else if (clase == WorldMapTiledScreen.class) {
                assetsHandler.cargarMenus();
                game.setScreen(new WorldMapTiledScreen(game));
            } else if (clase == ShopScreen.class) {
                assetsHandler.cargarMenus();
                game.setScreen(new ShopScreen(game));
            } else if (clase == GameScreen.class) {
                assetsHandler.cargarGameScreenTiled();
                game.setScreen(new GameScreen(game, nivelTiled));
            }
        } else {

            cargaActual = (int) (game.assetsHandler.getProgress() * 100);
        }
    }

    @Override
    public void draw(float delta) {

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        assetsHandler.fontChco.setColor(Color.WHITE);
        glyphLayout.setText(assetsHandler.fontChco, "%");
        assetsHandler.fontChco.draw(batch, cargaActual + "%", (SCREEN_WIDTH / 2F)
                - (glyphLayout.width / 2), SCREEN_HEIGHT / 2F - glyphLayout.height / 2);
        batch.end();
    }

    @Override
    public void show() {
    }
}
