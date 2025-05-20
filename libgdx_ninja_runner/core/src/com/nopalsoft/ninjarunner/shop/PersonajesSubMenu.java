package com.nopalsoft.ninjarunner.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.nopalsoft.ninjarunner.AnimationSprite;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.MainGame;
import com.nopalsoft.ninjarunner.Settings;
import com.nopalsoft.ninjarunner.objetos.Personaje;
import com.nopalsoft.ninjarunner.scene2d.AnimatedSpriteActor;

import java.util.Iterator;

public class PersonajesSubMenu {

    final int PRECIO_NINJA = 1000;

    boolean didBuyNinja;

    Label lbPrecioNinja;

    TextButton btSelectShanti, btBuyNinja;
    Array<TextButton> arrBotones;

    Table contenedor;
    I18NBundle idiomas;

    String textBuy;
    String textSelect;

    private final static Preferences pref = Gdx.app.getPreferences("com.tiar.shantirunner.shop");

    public PersonajesSubMenu(Table contenedor, MainGame game) {
        idiomas = game.idiomas;
        this.contenedor = contenedor;
        contenedor.clear();
        // TODO QUITAR ESTO PARA BORRAR PREF
        pref.clear();
        pref.flush();

        loadPurchases();

        textBuy = idiomas.get("buy");
        textSelect = idiomas.get("select");

        if (!didBuyNinja)
            lbPrecioNinja = new Label(PRECIO_NINJA + "", Assets.labelStyleChico);

        inicializarBotones();

        contenedor.defaults().expand().fill().padLeft(10).padRight(20).padBottom(10);

        contenedor.add(agregarPersonaje("Runner girl", null, Assets.personajeRun, idiomas.get("bombDescription"), btSelectShanti)).row();
        contenedor.add(agregarPersonaje("Runner Ninja", lbPrecioNinja, Assets.ninjaRun, idiomas.get("bombDescription"), btBuyNinja)).row();
    }

    public Table agregarPersonaje(String titulo, Label lblPrecio, AnimationSprite imagen, String descripcion, TextButton boton) {

        Image moneda = new Image(Assets.moneda.getKeyFrame(0));
        AnimatedSpriteActor imgPersonaje = new AnimatedSpriteActor(imagen);

        if (lblPrecio == null)
            moneda.setVisible(false);

        Table tbBarraTitulo = new Table();
        tbBarraTitulo.add(new Label(titulo, Assets.labelStyleChico)).expandX().left();
        tbBarraTitulo.add(moneda).right().size(20);
        tbBarraTitulo.add(lblPrecio).right().padRight(10);

        Table tbContent = new Table();
        tbContent.setBackground(Assets.backgroundItemShop);
        tbContent.pad(5);//El ninepatch le mete un padding por default

        tbContent.defaults().padLeft(20).padRight(20);
        tbContent.add(tbBarraTitulo).expandX().fill().colspan(2);
        tbContent.row();

        tbContent.add(imgPersonaje).size(120, 99);
        Label lblDescripcion = new Label(descripcion, Assets.labelStyleChico);
        lblDescripcion.setWrap(true);
        tbContent.add(lblDescripcion).expand().fill();

        tbContent.row().colspan(2);
        tbContent.add(boton).expandX().right().size(120, 45);
        tbContent.row().colspan(2);

        tbContent.debugAll();
        return tbContent;

    }

    private void inicializarBotones() {
        arrBotones = new Array<TextButton>();

        // DEFAULT
        btSelectShanti = new TextButton(textSelect, Assets.styleTextButtonPurchased);
        if (Settings.skinSeleccionada == Personaje.TIPO_GIRL)
            btSelectShanti.setVisible(false);

        btSelectShanti.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.skinSeleccionada = Personaje.TIPO_GIRL;
                setSelected(btSelectShanti);
            }
        });

        // SKIN_NINJA
        if (didBuyNinja)
            btBuyNinja = new TextButton(textSelect, Assets.styleTextButtonPurchased);
        else
            btBuyNinja = new TextButton(textBuy, Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == Personaje.TIPO_NINJA)
            btBuyNinja.setVisible(false);

        btBuyNinja.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyNinja) {
                    Settings.skinSeleccionada = Personaje.TIPO_NINJA;
                    setSelected(btBuyNinja);
                } else if (Settings.monedasTotal >= PRECIO_NINJA) {
                    Settings.monedasTotal -= PRECIO_NINJA;
                    setButtonStylePurchased(btBuyNinja);
                    lbPrecioNinja.remove();
                    didBuyNinja = true;
                }
                savePurchases();
            }
        });

        arrBotones.add(btSelectShanti);
        arrBotones.add(btBuyNinja);

    }

    private void loadPurchases() {
        didBuyNinja = pref.getBoolean("didBuyNinja", false);
    }

    private void savePurchases() {
        pref.putBoolean("didBuyNinja", didBuyNinja);
        pref.flush();
        Settings.save();

    }

    private void setButtonStylePurchased(TextButton boton) {
        boton.setStyle(Assets.styleTextButtonPurchased);
        boton.setText(textSelect);

    }

    private void setSelected(TextButton boton) {
        // Pongo todos visibles y al final el boton seleccionado en invisible
        Iterator<TextButton> i = arrBotones.iterator();
        while (i.hasNext()) {
            i.next().setVisible(true);
        }
        boton.setVisible(false);
    }
}
