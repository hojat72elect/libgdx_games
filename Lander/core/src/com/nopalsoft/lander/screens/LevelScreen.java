package com.nopalsoft.lander.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.lander.Assets;
import com.nopalsoft.lander.MainLander;
import com.nopalsoft.lander.Settings;
import com.nopalsoft.lander.game.GameScreen;
import com.nopalsoft.lander.scene2d.PagedScrollPane;

public class LevelScreen extends Screens {

    public LevelScreen(MainLander game) {
        super(game);

        PagedScrollPane scroll = new PagedScrollPane();
        scroll.setFlingTime(.1f);
        scroll.setPageSpacing(60);// Espacio entre cada pagina

        Table contenedor = new Table();
        contenedor.setFillParent(true);

        int level = 0;
        for (int l = 0; l < 10; l++) {// Paginas

            Table levels = new Table().padBottom(100);// Modificar para el margen
            levels.defaults().pad(20, 20, 20, 20);
            for (int y = 0; y < 4; y++) {// Filas
                levels.row();
                for (int x = 0; x < 3; x++) {// columnas
                    levels.add(getLevelButton(level)).size(100);
                    level++;
                }
            }
            scroll.addPage(levels);
        }

        contenedor.add(scroll).expand().fill().pad(32).top();

        stage.addActor(contenedor);
    }

    public Button getLevelButton(final int level) {

        final TextButton button = new TextButton("" + (level + 1), Assets.styleTextButtonLevels);

        int stars = Settings.arrEstrellasMundo[level];

        if (Settings.arrIsWorldLocked[level]) {
            button.row();
            button.add(new Image(Assets.candado));
            button.setDisabled(true);
        }

        Table starTable = new Table();
        starTable.defaults().pad(5);
        if (stars >= 0) {
            for (int star = 0; star < 3; star++) {
                if (stars > star) {
                    starTable.add(new Image(Assets.star)).width(20).height(20);
                } else {
                    starTable.add(new Image(Assets.starOff)).width(20).height(20);
                }
            }
        }

        button.row();
        button.add(starTable).height(30);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (button.isDisabled()) {
                    Gdx.app.log("Locked", "El mundo " + (level + 1) + "esta bloqueado");
                } else
                    game.setScreen(new GameScreen(game, level));
            }
        });

        return button;

    }

    @Override
    public void draw(float delta) {
        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);

        batcher.begin();
        batcher.disableBlending();
        batcher.draw(Assets.fondo, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batcher.end();

    }

    @Override
    public void update(float delta) {
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            game.setScreen(new MainMenuScreen(game));
            return true;
        }
        return false;
    }

}
