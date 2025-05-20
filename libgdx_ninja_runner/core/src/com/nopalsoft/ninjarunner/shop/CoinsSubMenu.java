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

		contenedor.add(new Label("Coins", Assets.labelStyleGrande)).expand().row();
		contenedor.add(new Label("Coins1", Assets.labelStyleGrande)).row();
		contenedor.add(new Label("Coins2", Assets.labelStyleGrande)).row();
		contenedor.add(new Label("Coins3", Assets.labelStyleGrande)).row();
		contenedor.add(new Label("Coins4", Assets.labelStyleGrande)).row();
		contenedor.add(new Label("Coins5", Assets.labelStyleGrande)).row();
		contenedor.add(new Label("Coins6", Assets.labelStyleGrande)).row();
		contenedor.add(new Label("Coins7", Assets.labelStyleGrande)).row();
		contenedor.add(new Label("Coins8", Assets.labelStyleGrande)).row();
		contenedor.add(new Label("Coins9", Assets.labelStyleGrande)).row();
		contenedor.add(new Label("Coins10", Assets.labelStyleGrande)).row();
		contenedor.add(new Label("Coins11", Assets.labelStyleGrande)).row();
		contenedor.add(new Label("Coins12", Assets.labelStyleGrande)).row();
		contenedor.add(new Label("Coins13", Assets.labelStyleGrande)).row();
		contenedor.add(new Label("Coins14", Assets.labelStyleGrande)).row();
		contenedor.add(new Label("Coins15", Assets.labelStyleGrande)).row();

	}

}
