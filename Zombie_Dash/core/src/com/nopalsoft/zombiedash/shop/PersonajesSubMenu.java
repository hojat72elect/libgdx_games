package com.nopalsoft.zombiedash.shop;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.nopalsoft.zombiedash.AnimationSprite;
import com.nopalsoft.zombiedash.Assets;
import com.nopalsoft.zombiedash.MainZombieDash;
import com.nopalsoft.zombiedash.Settings;
import com.nopalsoft.zombiedash.objects.Hero;
import com.nopalsoft.zombiedash.scene2d.AnimatedSpriteActor;

public class PersonajesSubMenu {

    final int PRECIO_HERO_RAMBO = 1000;
    final int PRECIO_HERO_SOLDIER = 1500;
    final int PRECIO_HERO_ELITE = 2000;
    final int PRECIO_HERO_VADER = 2500;

    boolean didBuyRambo, didBuySoldier, didBuyElite, didBuyVader;

    Label lbPrecioRambo, lbPrecioSoldier, lbPrecioElite, lbPrecioVader;
    TextButton btBuySWAT, btBuyRambo, btBuySoldier, btBuyElite, btBuyVader;
    Array<TextButton> arrBotones;

    Table contenedor;
    I18NBundle idiomas;

    String textBuy;
    String textSelect;

    private final static Preferences pref = Gdx.app.getPreferences("com.nopalsoft.zombiedash.shop");

    public PersonajesSubMenu(Table contenedor, MainZombieDash game) {
        idiomas = game.idiomas;
        this.contenedor = contenedor;
        contenedor.clear();
        loadPurchases();

        textBuy = idiomas.get("buy");
        textSelect = idiomas.get("select");

        if (!didBuyRambo)
            lbPrecioRambo = new Label(PRECIO_HERO_RAMBO + "", Assets.labelStyleChico);
        if (!didBuySoldier)
            lbPrecioSoldier = new Label(PRECIO_HERO_SOLDIER + "", Assets.labelStyleChico);

        if (!didBuyElite)
            lbPrecioElite = new Label(PRECIO_HERO_ELITE + "", Assets.labelStyleChico);

        if (!didBuyVader)
            lbPrecioVader = new Label(PRECIO_HERO_VADER + "", Assets.labelStyleChico);

        inicializarBotones();

        // Usar Default
        contenedor.add(agregarPersonajeTabla(idiomas.get("swat"), null, Assets.heroSwatRun, idiomas.get("swat_description"), btBuySWAT)).expandX()
                .fill();
        contenedor.row();

        // SKIN_HERO_RAMBO
        contenedor
                .add(agregarPersonajeTabla(idiomas.get("guerrilla"), lbPrecioRambo, Assets.heroRamboRun, idiomas.get("guerrilla_description"),
                        btBuyRambo)).expandX().fill();
        contenedor.row();

        // SKIN_HERO_SOLDIER
        contenedor.add(agregarPersonajeTabla(idiomas.get("soldier"),//
                lbPrecioSoldier,//
                Assets.heroSoldierRun,//
                idiomas.get("soldier_description"),//
                btBuySoldier)).expandX().fill();
        contenedor.row();

        // SKIN_HERO_SWAT
        contenedor.add(agregarPersonajeTabla(idiomas.get("elite_force"),//
                lbPrecioElite,//
                Assets.heroForceRun,//
                idiomas.get("elite_force_description"),//
                btBuyElite)).expandX().fill();
        contenedor.row();

        // SKIN_HERO_VADER
        contenedor.add(agregarPersonajeTabla(idiomas.get("ghost"),//
                lbPrecioVader,//
                Assets.heroVaderRun,//
                idiomas.get("ghost_description"),//
                btBuyVader)).expandX().fill();
        contenedor.row();

    }

    private Table agregarPersonajeTabla(String titulo, Label lblPrecio, AnimationSprite imagen, String descripcion, TextButton boton) {

        Image moneda = new Image(Assets.itemGem);
        AnimatedSpriteActor imgPersonaje = new AnimatedSpriteActor(imagen);

        if (lblPrecio == null)
            moneda.setVisible(false);

        Table tbBarraTitulo = new Table();
        tbBarraTitulo.add(new Label(titulo, Assets.labelStyleChico)).expandX().left();
        tbBarraTitulo.add(moneda).right().size(20);
        tbBarraTitulo.add(lblPrecio).right().padRight(10);

        Table tbContent = new Table();
        tbContent.setBackground(Assets.storeTableBackground);

        // tbContent.debug();
        tbContent.defaults().padLeft(20).padRight(20);
        tbContent.add(tbBarraTitulo).expandX().fill().colspan(2).padTop(20);
        tbContent.row();
        tbContent.add(imgPersonaje).left().size(70, 70);

        Label lblDescripcion = new Label(descripcion, Assets.labelStyleChico);
        lblDescripcion.setWrap(true);
        lblDescripcion.setFontScale(.9f);
        tbContent.add(lblDescripcion).expand().fill().padLeft(5);

        tbContent.row().colspan(2);
        tbContent.add(boton).expandX().right().padBottom(20).size(120, 45);
        tbContent.row().colspan(2);

        return tbContent;

    }

    private void inicializarBotones() {
        arrBotones = new Array<TextButton>();

        // DEFAULT
        btBuySWAT = new TextButton(textSelect, Assets.styleTextButtonPurchased);
        if (Settings.skinSeleccionada == Hero.TIPO_SWAT)
            btBuySWAT.setVisible(false);

        addEfectoPress(btBuySWAT);
        btBuySWAT.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.skinSeleccionada = Hero.TIPO_SWAT;
                setSelected(btBuySWAT);
            }
        });

        // SKIN_HERO_RAMBO
        if (didBuyRambo)
            btBuyRambo = new TextButton(textSelect, Assets.styleTextButtonPurchased);
        else
            btBuyRambo = new TextButton(textBuy, Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == Hero.TIPO_RAMBO)
            btBuyRambo.setVisible(false);

        addEfectoPress(btBuyRambo);
        btBuyRambo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyRambo) {
                    Settings.skinSeleccionada = Hero.TIPO_RAMBO;
                    setSelected(btBuyRambo);
                } else if (Settings.gemsTotal >= PRECIO_HERO_RAMBO) {
                    Settings.gemsTotal -= PRECIO_HERO_RAMBO;
                    setButtonStylePurchased(btBuyRambo);
                    lbPrecioRambo.remove();
                    didBuyRambo = true;
                }
                savePurchases();
            }
        });

        // SKIN_HERO_SOLDIER
        if (didBuySoldier)
            btBuySoldier = new TextButton(textSelect, Assets.styleTextButtonPurchased);
        else
            btBuySoldier = new TextButton(textBuy, Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == Hero.TIPO_SOLDIER)
            btBuySoldier.setVisible(false);

        addEfectoPress(btBuySoldier);
        btBuySoldier.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuySoldier) {
                    Settings.skinSeleccionada = Hero.TIPO_SOLDIER;
                    setSelected(btBuySoldier);
                } else if (Settings.gemsTotal >= PRECIO_HERO_SOLDIER) {
                    Settings.gemsTotal -= PRECIO_HERO_SOLDIER;
                    setButtonStylePurchased(btBuySoldier);
                    lbPrecioSoldier.remove();
                    didBuySoldier = true;
                }
                savePurchases();
            }
        });

        // SKIN_HERO_SWAT
        if (didBuyElite)
            btBuyElite = new TextButton(textSelect, Assets.styleTextButtonPurchased);
        else
            btBuyElite = new TextButton(textBuy, Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == Hero.TIPO_FORCE)
            btBuyElite.setVisible(false);

        addEfectoPress(btBuyElite);
        btBuyElite.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyElite) {
                    Settings.skinSeleccionada = Hero.TIPO_FORCE;
                    setSelected(btBuyElite);
                } else if (Settings.gemsTotal >= PRECIO_HERO_ELITE) {
                    Settings.gemsTotal -= PRECIO_HERO_ELITE;
                    setButtonStylePurchased(btBuyElite);
                    lbPrecioElite.remove();
                    didBuyElite = true;
                }
                savePurchases();
            }
        });

        // SKIN_HERO_VADER
        if (didBuyVader)
            btBuyVader = new TextButton(textSelect, Assets.styleTextButtonPurchased);
        else
            btBuyVader = new TextButton(textBuy, Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == Hero.TIPO_VADER)
            btBuyVader.setVisible(false);

        addEfectoPress(btBuyVader);
        btBuyVader.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyVader) {
                    Settings.skinSeleccionada = Hero.TIPO_VADER;
                    setSelected(btBuyVader);
                } else if (Settings.gemsTotal >= PRECIO_HERO_VADER) {
                    Settings.gemsTotal -= PRECIO_HERO_VADER;
                    setButtonStylePurchased(btBuyVader);
                    lbPrecioVader.remove();
                    didBuyVader = true;
                }
                savePurchases();
            }
        });

        arrBotones.add(btBuySWAT);
        arrBotones.add(btBuyRambo);
        arrBotones.add(btBuySoldier);
        arrBotones.add(btBuyElite);
        arrBotones.add(btBuyVader);

    }

    private void loadPurchases() {
        didBuyRambo = pref.getBoolean("didBuyRambo", false);
        didBuySoldier = pref.getBoolean("didBuySoldier", false);
        didBuyElite = pref.getBoolean("didBuyElite", false);
        didBuyVader = pref.getBoolean("didBuyVader", false);

    }

    private void savePurchases() {
        pref.putBoolean("didBuyRambo", didBuyRambo);
        pref.putBoolean("didBuySoldier", didBuySoldier);
        pref.putBoolean("didBuyElite", didBuyElite);
        pref.putBoolean("didBuyVader", didBuyVader);
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
