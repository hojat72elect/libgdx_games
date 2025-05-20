package com.nopalsoft.slamthebird.shop;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.MainSlamBird;
import com.nopalsoft.slamthebird.Settings;

public class PersonajesSubMenu {
	public static final int SKIN_DEFAULT = 0;
	public static final int SKIN_ANDROID_ROJO = 1;
	public static final int SKIN_ANDROID_AZUL = 2;

	final int PRECIO_ANDROID_ROJO = 1500;
	final int PRECIO_ANDROID_AZUL = 2000;

	boolean didBuyRojo;
	boolean didBuyAzul;

	TextButton btBuyDefault, btBuyAndroidRojo, btBuyAndroidAzul;
	Array<TextButton> arrBotones;

	Table contenedor;
	MainSlamBird game;

	Label lblPrecioRojo, lblPrecioBlue;

	private final static Preferences pref = Gdx.app
			.getPreferences("com.nopalsoft.slamthebird.personajes");

	public PersonajesSubMenu(MainSlamBird game, Table contenedor) {
		this.game = game;
		this.contenedor = contenedor;
		contenedor.clear();

		didBuyRojo = pref.getBoolean("didBuyRojo", false);
		didBuyAzul = pref.getBoolean("didBuyAzul", false);

		lblPrecioRojo = new Label(PRECIO_ANDROID_ROJO + "",
				Assets.styleLabelChico);
		lblPrecioBlue = new Label(PRECIO_ANDROID_AZUL + "",
				Assets.styleLabelChico);

		inicializarBotones();

		contenedor.add(new Image(Assets.separadorHorizontal)).expandX().fill()
				.height(5);
		contenedor.row();

		// Usar Default
		contenedor
				.add(agregarPersonajeTabla("Green robot", null,
						Assets.personajeShopDefault,
						"Just a simple robot for slaming birds", btBuyDefault))
				.expandX().fill();
		contenedor.row();

		// Usar personaje rojo
		contenedor
				.add(agregarPersonajeTabla(
						"Red robot",
						lblPrecioRojo,
						Assets.personajeShopRojo,
						"Do you like red color. Play with this amazing red robot and slam those birds",
						btBuyAndroidRojo)).expandX().fill();
		contenedor.row();

		// SKIN_ANDROID_AZUL
		contenedor
				.add(agregarPersonajeTabla(
						"Blue robot",
						lblPrecioBlue,
						Assets.personajeShopAzul,
						"Do you like blue color. Play with this amazing blue robot and slam those birds",
						btBuyAndroidAzul)).expandX().fill();
		contenedor.row();

		if (didBuyAzul)
			lblPrecioBlue.remove();
		if (didBuyRojo)
			lblPrecioRojo.remove();

	}

	private Table agregarPersonajeTabla(String titulo, Label lblPrecio,
			AtlasRegion imagen, String descripcion, TextButton boton) {

		Image moneda = new Image(Assets.moneda);
		Image imgPersonaje = new Image(imagen);

		if (lblPrecio == null)
			moneda.setVisible(false);

		Table tbBarraTitulo = new Table();
		tbBarraTitulo.add(new Label(titulo, Assets.styleLabelChico)).expandX()
				.left();
		tbBarraTitulo.add(moneda).right();
		tbBarraTitulo.add(lblPrecio).right().padRight(10);

		Table tbContent = new Table();
		// tbContent.debug();
		tbContent.add(tbBarraTitulo).expandX().fill().colspan(2).padTop(8);
		tbContent.row();
		tbContent.add(imgPersonaje).left().pad(10).size(60);

		Label lblDescripcion = new Label(descripcion, Assets.styleLabelChico);
		lblDescripcion.setWrap(true);
		tbContent.add(lblDescripcion).expand().fill().padLeft(5);

		tbContent.row().colspan(2);
		tbContent.add(boton).expandX().right().padRight(10).size(120, 45);
		tbContent.row().colspan(2);
		tbContent.add(new Image(Assets.separadorHorizontal)).expandX().fill()
				.height(5).padTop(15);

		return tbContent;

	}

	private void inicializarBotones() {
		arrBotones = new Array<TextButton>();

		// SKIN_DEFAULT
		btBuyDefault = new TextButton("Select", Assets.styleTextButtonPurchased);
		if (Settings.skinSeleccionada == SKIN_DEFAULT)
			btBuyDefault.setVisible(false);

		addEfectoPress(btBuyDefault);
		btBuyDefault.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Settings.skinSeleccionada = SKIN_DEFAULT;
				setSelected(btBuyDefault);
				Assets.cargarPersonaje();

			}
		});

		// SKIN_ANDROID_ROJO
		if (didBuyRojo)
			btBuyAndroidRojo = new TextButton("Select",
					Assets.styleTextButtonPurchased);
		else
			btBuyAndroidRojo = new TextButton("Buy", Assets.styleTextButtonBuy);

		if (Settings.skinSeleccionada == SKIN_ANDROID_ROJO)
			btBuyAndroidRojo.setVisible(false);

		addEfectoPress(btBuyAndroidRojo);
		btBuyAndroidRojo.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (didBuyRojo) {
					Settings.skinSeleccionada = SKIN_ANDROID_ROJO;
					setSelected(btBuyAndroidRojo);
					Assets.cargarPersonaje();
				}
				else if (Settings.monedasActuales >= PRECIO_ANDROID_ROJO) {
					Settings.monedasActuales -= PRECIO_ANDROID_ROJO;
					setButtonStylePurchased(btBuyAndroidRojo);
					didBuyRojo = true;
					lblPrecioRojo.remove();
					save();
				}
			}
		});

		// SKIN_ANDROID_AZUL
		if (didBuyAzul)
			btBuyAndroidAzul = new TextButton("Select",
					Assets.styleTextButtonPurchased);
		else
			btBuyAndroidAzul = new TextButton("Buy", Assets.styleTextButtonBuy);

		if (Settings.skinSeleccionada == SKIN_ANDROID_AZUL)
			btBuyAndroidAzul.setVisible(false);

		addEfectoPress(btBuyAndroidAzul);
		btBuyAndroidAzul.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (didBuyAzul) {
					Settings.skinSeleccionada = SKIN_ANDROID_AZUL;
					setSelected(btBuyAndroidAzul);
					Assets.cargarPersonaje();
				}
				else if (Settings.monedasActuales >= PRECIO_ANDROID_AZUL) {
					Settings.monedasActuales -= PRECIO_ANDROID_AZUL;
					setButtonStylePurchased(btBuyAndroidAzul);
					didBuyAzul = true;
					lblPrecioBlue.remove();
					save();
				}
			}
		});

		arrBotones.add(btBuyDefault);
		arrBotones.add(btBuyAndroidRojo);
		arrBotones.add(btBuyAndroidAzul);

	}

	private void save() {
		pref.putBoolean("didBuyAzul", didBuyAzul);
		pref.putBoolean("didBuyRojo", didBuyRojo);
		pref.flush();
	}

	private void setButtonStylePurchased(TextButton boton) {
		boton.setStyle(Assets.styleTextButtonPurchased);
		boton.setText("Select");

	}

	private void setSelected(TextButton boton) {
		// Pongo todos visibles y al final el boton seleccionado en invisible
		Iterator<TextButton> i = arrBotones.iterator();
		while (i.hasNext()) {
			i.next().setVisible(true);
		}
		boton.setVisible(false);
	}

	protected void addEfectoPress(final Actor actor) {
		actor.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				actor.setPosition(actor.getX(), actor.getY() - 3);
				event.stop();
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				actor.setPosition(actor.getX(), actor.getY() + 3);
			}
		});
	}
}
