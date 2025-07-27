package com.nopalsoft.ponyrace.screens;

import com.badlogic.gdx.graphics.Color;
import com.nopalsoft.ponyrace.MainPonyRace;
import com.nopalsoft.ponyrace.game.GameScreenTileds;

public class LoadingScreen extends Screens {
    Class<?> clase;

    int cargaActual;
    int nivelTiled;

    public LoadingScreen(MainPonyRace game, Class<?> clase, int nivelTiled) {
        super(game);
        create(game, clase, nivelTiled);
    }

    public LoadingScreen(MainPonyRace game, Class<?> clase) {
        super(game);
        create(game, clase, -100);
    }

    public void create(MainPonyRace game, Class<?> clase, int nivelTiled) {
        this.clase = clase;
        this.game = game;
        cargaActual = 0;
        this.nivelTiled = nivelTiled;

        if (clase == MainMenuScreen.class) {
            oAssets.loadMenus();
        } else if (clase == LeaderboardChooseScreen.class) {
            oAssets.loadMenus();
        } else if (clase == WorldMapTiledScreen.class) {
            oAssets.loadMenus();
        } else if (clase == ShopScreen.class) {
            oAssets.loadMenus();
        } else if (clase == GameScreenTileds.class) {
            oAssets.loadGameScreenTiled(nivelTiled);
        }
    }

    @Override
    public void update(float delta) {
        if (oAssets.update()) {

            if (clase == MainMenuScreen.class) {
                oAssets.cargarMenus();
                game.setScreen(new MainMenuScreen(game));
            } else if (clase == LeaderboardChooseScreen.class) {
                oAssets.cargarMenus();
                game.setScreen(new LeaderboardChooseScreen(game));
            } else if (clase == WorldMapTiledScreen.class) {
                oAssets.cargarMenus();
                game.setScreen(new WorldMapTiledScreen(game));
            } else if (clase == ShopScreen.class) {
                oAssets.cargarMenus();
                game.setScreen(new ShopScreen(game));
            } else if (clase == GameScreenTileds.class) {
                oAssets.cargarGameScreenTiled();
                game.setScreen(new GameScreenTileds(game, nivelTiled));
            }
        } else {

            cargaActual = (int) (game.oAssets.getProgress() * 100);
        }
    }

    @Override
    public void draw(float delta) {

        guiCam.update();
        batcher.setProjectionMatrix(guiCam.combined);

        batcher.begin();
        oAssets.fontChco.setColor(Color.WHITE);
        glyphLayout.setText(oAssets.fontChco, "%");
        oAssets.fontChco.draw(batcher, cargaActual + "%", (SCREEN_WIDTH / 2F)
                - (glyphLayout.width / 2), SCREEN_HEIGHT / 2F - glyphLayout.height / 2);
        batcher.end();
    }

    @Override
    public void show() {
    }
}
