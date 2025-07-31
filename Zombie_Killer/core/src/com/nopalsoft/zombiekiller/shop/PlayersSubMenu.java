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

        createButtons();

        containerTable.add(createPlayerTable(languagesBundle.get("swat"), null, Assets.heroSwatWalk, languagesBundle.get("swat_description"), buttonBuySWAT)).expandX().fill();
        containerTable.row();

        containerTable.add(createPlayerTable(languagesBundle.get("guerrilla"), labelPriceRambo, Assets.heroRamboWalk, languagesBundle.get("guerrilla_description"), buttonBuyRambo)).expandX().fill();
        containerTable.row();

        containerTable.add(createPlayerTable(languagesBundle.get("soldier"), labelPriceSoldier, Assets.heroSoldierWalk, languagesBundle.get("soldier_description"), buttonBuySoldier)).expandX().fill();
        containerTable.row();

        containerTable.add(createPlayerTable(languagesBundle.get("elite_force"), labelPriceElite, Assets.heroForceWalk, languagesBundle.get("elite_force_description"), buttonBuyElite)).expandX().fill();
        containerTable.row();

        containerTable.add(createPlayerTable(languagesBundle.get("ghost"), labelPriceVader, Assets.heroVaderWalk, languagesBundle.get("ghost_description"), buttonBuyVader)).expandX().fill();
        containerTable.row();
    }

    private Table createPlayerTable(String title, Label priceLabel, AnimationSprite playerAnimation, String description, TextButton button) {

        Image coinImage = new Image(Assets.itemGem);
        AnimatedSpriteActor playerSpriteActor = new AnimatedSpriteActor(playerAnimation);

        if (priceLabel == null)
            coinImage.setVisible(false);

        Table titleBarTable = new Table();
        titleBarTable.add(new Label(title, Assets.labelStyleChico)).expandX().left();
        titleBarTable.add(coinImage).right().size(20);
        titleBarTable.add(priceLabel).right().padRight(10);

        Table tbContent = new Table();
        tbContent.pad(0);
        tbContent.setBackground(Assets.storeTableBackground);

        tbContent.defaults().padLeft(20).padRight(20);
        tbContent.add(titleBarTable).expandX().fill().colspan(2).padTop(20);
        tbContent.row();
        tbContent.add(playerSpriteActor).left().size(70, 70);

        Label descriptionLabel = new Label(description, Assets.labelStyleChico);
        descriptionLabel.setWrap(true);
        descriptionLabel.setFontScale(.9f);
        tbContent.add(descriptionLabel).expand().fill().padLeft(5);

        tbContent.row().colspan(2);
        tbContent.add(button).expandX().right().padBottom(20).size(120, 45);
        tbContent.row().colspan(2);

        return tbContent;
    }

    private void createButtons() {
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
        // I make all visible and at the end the selected button invisible.
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
