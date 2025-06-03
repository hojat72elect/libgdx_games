package com.nopalsoft.ninjarunner.shop;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.MainGame;

public class CoinsSubMenu {

    Table contenedor;

    // I18NBundle idiomas;

    public CoinsSubMenu(Table contenedor, MainGame game) {
        // idiomas = game.idiomas;
        this.contenedor = contenedor;
        contenedor.clear();

        contenedor.add(new Label("Coins", Assets.labelStyleLarge)).expand().row();
        contenedor.add(new Label("Coins1", Assets.labelStyleLarge)).row();
        contenedor.add(new Label("Coins2", Assets.labelStyleLarge)).row();
        contenedor.add(new Label("Coins3", Assets.labelStyleLarge)).row();
        contenedor.add(new Label("Coins4", Assets.labelStyleLarge)).row();
        contenedor.add(new Label("Coins5", Assets.labelStyleLarge)).row();
        contenedor.add(new Label("Coins6", Assets.labelStyleLarge)).row();
        contenedor.add(new Label("Coins7", Assets.labelStyleLarge)).row();
        contenedor.add(new Label("Coins8", Assets.labelStyleLarge)).row();
        contenedor.add(new Label("Coins9", Assets.labelStyleLarge)).row();
        contenedor.add(new Label("Coins10", Assets.labelStyleLarge)).row();
        contenedor.add(new Label("Coins11", Assets.labelStyleLarge)).row();
        contenedor.add(new Label("Coins12", Assets.labelStyleLarge)).row();
        contenedor.add(new Label("Coins13", Assets.labelStyleLarge)).row();
        contenedor.add(new Label("Coins14", Assets.labelStyleLarge)).row();
        contenedor.add(new Label("Coins15", Assets.labelStyleLarge)).row();
    }
}
