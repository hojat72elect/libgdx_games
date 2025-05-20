package com.nopalsoft.slamthebird.shop;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.MainSlamBird;
import com.nopalsoft.slamthebird.Settings;

public class NoAdsSubMenu {

	int priceNoAds = 35000;

	TextButton btNoAds;
	Label lblNoAds;

	Table contenedor;
	MainSlamBird game;

	public NoAdsSubMenu(final MainSlamBird game, Table contenedor) {
		this.game = game;
		this.contenedor = contenedor;
		contenedor.clear();

		if (!Settings.didBuyNoAds)
			lblNoAds = new Label(priceNoAds + "", Assets.styleLabelChico);

		btNoAds = new TextButton("Buy", Assets.styleTextButtonBuy);
		if (Settings.didBuyNoAds)
			btNoAds.setVisible(false);
		addEfectoPress(btNoAds);
		btNoAds.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (Settings.monedasActuales >= priceNoAds) {
					Settings.monedasActuales -= priceNoAds;
					Settings.didBuyNoAds = true;
					lblNoAds.setVisible(false);
					btNoAds.setVisible(false);
					game.reqHandler.removeAds();
				}
			}
		});

		// Upgrade BoostTime
		contenedor.add(new Image(Assets.separadorHorizontal)).expandX().fill().height(5);
		contenedor.row();
		contenedor
				.add(agregarPersonajeTabla("No more ads", lblNoAds, Assets.btNoAds,
						"Buy it and no more ads will apper in the app", btNoAds)).expandX().fill();
		contenedor.row();

	}

	/**
	 * 
	 * @param titulo
	 * @param lblPrecio
	 * @param imagen
	 * @param descripcion
	 * @param boton
	 * @return
	 */
	private Table agregarPersonajeTabla(String titulo, Label lblPrecio, TextureRegionDrawable imagen,
			String descripcion, TextButton boton) {

		Image moneda = new Image(Assets.moneda);
		Image imgPersonaje = new Image(imagen);

		if (lblPrecio == null)
			moneda.setVisible(false);

		Table tbBarraTitulo = new Table();
		tbBarraTitulo.add(new Label(titulo, Assets.styleLabelChico)).expandX().left().padLeft(5);
		tbBarraTitulo.add(moneda).right();
		tbBarraTitulo.add(lblPrecio).right().padRight(10);

		Table tbDescrip = new Table();
		tbDescrip.add(imgPersonaje).left().pad(10).size(55, 45);
		Label lblDescripcion = new Label(descripcion, Assets.styleLabelChico);
		lblDescripcion.setWrap(true);
		tbDescrip.add(lblDescripcion).expand().fill().padLeft(5);

		Table tbContent = new Table();
		tbContent.add(tbBarraTitulo).expandX().fill().colspan(2).padTop(8);
		tbContent.row().colspan(2);
		tbContent.add(tbDescrip).expandX().fill();
		tbContent.row().colspan(2);

		tbContent.add(boton).right().padRight(10).size(120, 45);

		tbContent.row().colspan(2);
		tbContent.add(new Image(Assets.separadorHorizontal)).expandX().fill().height(5).padTop(15);

		return tbContent;

	}

	protected void addEfectoPress(final Actor actor) {
		actor.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				actor.setPosition(actor.getX(), actor.getY() - 3);
				event.stop();
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				actor.setPosition(actor.getX(), actor.getY() + 3);
			}
		});
	}
}
