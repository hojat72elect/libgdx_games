package com.nopalsoft.zombiekiller.shop;

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
import com.nopalsoft.zombiekiller.AnimationSprite;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.MainZombie;
import com.nopalsoft.zombiekiller.Settings;
import com.nopalsoft.zombiekiller.game_objects.Hero;
import com.nopalsoft.zombiekiller.scene2d.AnimatedSpriteActor;

public class PlayersSubMenu {

    private final static Preferences preferences = Gdx.app.getPreferences("com.nopalsoft.zombiekiller.shop");

    final int PRICE_HERO_RAMBO = 1000;
    final int PRICE_HERO_SOLDIER = 1500;
    final int PRICE_HERO_ELITE = 2000;
    final int PRICE_HERO_VADER = 2500;

    boolean didBuyRambo, didBuySoldier, didBuyElite, didBuyVader;

    Label labelPriceRambo, labelPriceSoldier, labelPriceElite, labelPriceVader;
    TextButton buttonBuySWAT, buttonBuyRambo, buttonBuySoldier, buttonBuyElite, buttonBuyVader;
    Array<TextButton> buttons;
    Table containerTable;
    I18NBundle languagesBundle;
    String textBuy;
    String textSelect;

    public PlayersSubMenu(Table containerTable, MainZombie game) {
        languagesBundle = game.idiomas;
        this.containerTable = containerTable;
        containerTable.clear();
        loadPurchases();

        textBuy = languagesBundle.get("buy");
        textSelect = languagesBundle.get("select");

        if (!didBuyRambo)
            labelPriceRambo = new Label(PRICE_HERO_RAMBO + "", Assets.labelStyleChico);

        if (!didBuySoldier)
            labelPriceSoldier = new Label(PRICE_HERO_SOLDIER + "", Assets.labelStyleChico);

        if (!didBuyElite)
            labelPriceElite = new Label(PRICE_HERO_ELITE + "", Assets.labelStyleChico);

        if (!didBuyVader)
            labelPriceVader = new Label(PRICE_HERO_VADER + "", Assets.labelStyleChico);

        inicializarBotones();

        containerTable.add(agregarPersonajeTabla(languagesBundle.get("swat"), null, Assets.heroSwatWalk, languagesBundle.get("swat_description"), buttonBuySWAT)).expandX().fill();
        containerTable.row();

        containerTable.add(agregarPersonajeTabla(languagesBundle.get("guerrilla"), labelPriceRambo, Assets.heroRamboWalk, languagesBundle.get("guerrilla_description"), buttonBuyRambo)).expandX().fill();
        containerTable.row();

        containerTable.add(agregarPersonajeTabla(languagesBundle.get("soldier"), labelPriceSoldier, Assets.heroSoldierWalk, languagesBundle.get("soldier_description"), buttonBuySoldier)).expandX().fill();
        containerTable.row();

        containerTable.add(agregarPersonajeTabla(languagesBundle.get("elite_force"), labelPriceElite, Assets.heroForceWalk, languagesBundle.get("elite_force_description"), buttonBuyElite)).expandX().fill();
        containerTable.row();

        containerTable.add(agregarPersonajeTabla(languagesBundle.get("ghost"), labelPriceVader, Assets.heroVaderWalk, languagesBundle.get("ghost_description"), buttonBuyVader)).expandX().fill();
        containerTable.row();
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
        tbContent.pad(0);
        tbContent.setBackground(Assets.storeTableBackground);

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
        buttons = new Array<>();

        buttonBuySWAT = new TextButton(textSelect, Assets.styleTextButtonPurchased);
        if (Settings.skinSeleccionada == Hero.TYPE_SWAT)
            buttonBuySWAT.setVisible(false);

        addEfectoPress(buttonBuySWAT);
        buttonBuySWAT.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.skinSeleccionada = Hero.TYPE_SWAT;
                setSelected(buttonBuySWAT);
            }
        });

        if (didBuyRambo)
            buttonBuyRambo = new TextButton(textSelect, Assets.styleTextButtonPurchased);
        else
            buttonBuyRambo = new TextButton(textBuy, Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == Hero.TYPE_RAMBO)
            buttonBuyRambo.setVisible(false);

        addEfectoPress(buttonBuyRambo);
        buttonBuyRambo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyRambo) {
                    Settings.skinSeleccionada = Hero.TYPE_RAMBO;
                    setSelected(buttonBuyRambo);
                } else if (Settings.gemsTotal >= PRICE_HERO_RAMBO) {
                    Settings.gemsTotal -= PRICE_HERO_RAMBO;
                    setButtonStylePurchased(buttonBuyRambo);
                    labelPriceRambo.remove();
                    didBuyRambo = true;
                }
                savePurchases();
            }
        });

        if (didBuySoldier)
            buttonBuySoldier = new TextButton(textSelect, Assets.styleTextButtonPurchased);
        else
            buttonBuySoldier = new TextButton(textBuy, Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == Hero.TYPE_SOLDIER)
            buttonBuySoldier.setVisible(false);

        addEfectoPress(buttonBuySoldier);
        buttonBuySoldier.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuySoldier) {
                    Settings.skinSeleccionada = Hero.TYPE_SOLDIER;
                    setSelected(buttonBuySoldier);
                } else if (Settings.gemsTotal >= PRICE_HERO_SOLDIER) {
                    Settings.gemsTotal -= PRICE_HERO_SOLDIER;
                    setButtonStylePurchased(buttonBuySoldier);
                    labelPriceSoldier.remove();
                    didBuySoldier = true;
                }
                savePurchases();
            }
        });

        if (didBuyElite)
            buttonBuyElite = new TextButton(textSelect, Assets.styleTextButtonPurchased);
        else
            buttonBuyElite = new TextButton(textBuy, Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == Hero.TYPE_FORCE)
            buttonBuyElite.setVisible(false);

        addEfectoPress(buttonBuyElite);
        buttonBuyElite.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyElite) {
                    Settings.skinSeleccionada = Hero.TYPE_FORCE;
                    setSelected(buttonBuyElite);
                } else if (Settings.gemsTotal >= PRICE_HERO_ELITE) {
                    Settings.gemsTotal -= PRICE_HERO_ELITE;
                    setButtonStylePurchased(buttonBuyElite);
                    labelPriceElite.remove();
                    didBuyElite = true;
                }
                savePurchases();
            }
        });

        if (didBuyVader)
            buttonBuyVader = new TextButton(textSelect, Assets.styleTextButtonPurchased);
        else
            buttonBuyVader = new TextButton(textBuy, Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == Hero.TYPE_VADER)
            buttonBuyVader.setVisible(false);

        addEfectoPress(buttonBuyVader);
        buttonBuyVader.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyVader) {
                    Settings.skinSeleccionada = Hero.TYPE_VADER;
                    setSelected(buttonBuyVader);
                } else if (Settings.gemsTotal >= PRICE_HERO_VADER) {
                    Settings.gemsTotal -= PRICE_HERO_VADER;
                    setButtonStylePurchased(buttonBuyVader);
                    labelPriceVader.remove();
                    didBuyVader = true;
                }
                savePurchases();
            }
        });

        buttons.add(buttonBuySWAT);
        buttons.add(buttonBuyRambo);
        buttons.add(buttonBuySoldier);
        buttons.add(buttonBuyElite);
        buttons.add(buttonBuyVader);
    }

    private void loadPurchases() {
        didBuyRambo = preferences.getBoolean("didBuyRambo", false);
        didBuySoldier = preferences.getBoolean("didBuySoldier", false);
        didBuyElite = preferences.getBoolean("didBuyElite", false);
        didBuyVader = preferences.getBoolean("didBuyVader", false);
    }

    private void savePurchases() {
        preferences.putBoolean("didBuyRambo", didBuyRambo);
        preferences.putBoolean("didBuySoldier", didBuySoldier);
        preferences.putBoolean("didBuyElite", didBuyElite);
        preferences.putBoolean("didBuyVader", didBuyVader);
        preferences.flush();
        Settings.save();
    }

    private void setButtonStylePurchased(TextButton boton) {
        boton.setStyle(Assets.styleTextButtonPurchased);
        boton.setText(textSelect);
    }

    private void setSelected(TextButton button) {
        // Pongo todos visibles y al final el boton seleccionado en invisible
        for (TextButton arrBotone : buttons) {
            arrBotone.setVisible(true);
        }
        button.setVisible(false);
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
