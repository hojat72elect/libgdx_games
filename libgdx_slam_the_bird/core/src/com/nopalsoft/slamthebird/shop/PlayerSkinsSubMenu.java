package com.nopalsoft.slamthebird.shop;

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
import com.nopalsoft.slamthebird.Settings;
import com.nopalsoft.slamthebird.SlamTheBirdGame;

public class PlayerSkinsSubMenu {
    public static final int SKIN_DEFAULT = 0;
    public static final int SKIN_RED_ANDROID = 1;
    public static final int SKIN_BLUE_ANDROID = 2;

    final int PRICE_RED_ANDROID = 1500;
    final int PRICE_BLUE_ANDROID = 2000;

    boolean isRedAndroidPurchased;
    boolean isBlueAndroidPurchased;

    TextButton buttonDefault, buttonRedAndroid, buttonBlueAndroid;
    Array<TextButton> arrayButtons;

    Table tableContainer;
    SlamTheBirdGame game;

    Label labelPriceRedAndroid, labelPriceBlueAndroid;

    private final static Preferences preferences = Gdx.app
            .getPreferences("com.nopalsoft.slamthebird.personajes");

    public PlayerSkinsSubMenu(SlamTheBirdGame game, Table tableContainer) {
        this.game = game;
        this.tableContainer = tableContainer;
        tableContainer.clear();

        isRedAndroidPurchased = preferences.getBoolean("didBuyRojo", false);
        isBlueAndroidPurchased = preferences.getBoolean("didBuyAzul", false);

        labelPriceRedAndroid = new Label(PRICE_RED_ANDROID + "",
                Assets.styleLabelChico);
        labelPriceBlueAndroid = new Label(PRICE_BLUE_ANDROID + "",
                Assets.styleLabelChico);

        initializeButtons();

        tableContainer.add(new Image(Assets.separadorHorizontal)).expandX().fill()
                .height(5);
        tableContainer.row();

        // default skin
        tableContainer
                .add(addPlayerTable("Green robot", null,
                        Assets.personajeShopDefault,
                        "Just a simple robot for slaming birds", buttonDefault))
                .expandX().fill();
        tableContainer.row();

        // red skin
        tableContainer
                .add(addPlayerTable(
                        "Red robot",
                        labelPriceRedAndroid,
                        Assets.personajeShopRojo,
                        "Do you like red color. Play with this amazing red robot and slam those birds",
                        buttonRedAndroid)).expandX().fill();
        tableContainer.row();

        // blue skin
        tableContainer
                .add(addPlayerTable(
                        "Blue robot",
                        labelPriceBlueAndroid,
                        Assets.personajeShopAzul,
                        "Do you like blue color. Play with this amazing blue robot and slam those birds",
                        buttonBlueAndroid)).expandX().fill();
        tableContainer.row();

        if (isBlueAndroidPurchased)
            labelPriceBlueAndroid.remove();
        if (isRedAndroidPurchased)
            labelPriceRedAndroid.remove();
    }

    private Table addPlayerTable(String title, Label labelPrice,
                                 AtlasRegion imageAtlasRegion, String description, TextButton button) {

        Image coinImage = new Image(Assets.moneda);
        Image playerImage = new Image(imageAtlasRegion);

        if (labelPrice == null)
            coinImage.setVisible(false);

        Table tableTitleBar = new Table();
        tableTitleBar.add(new Label(title, Assets.styleLabelChico)).expandX()
                .left();
        tableTitleBar.add(coinImage).right();
        tableTitleBar.add(labelPrice).right().padRight(10);

        Table tableContent = new Table();
        tableContent.add(tableTitleBar).expandX().fill().colspan(2).padTop(8);
        tableContent.row();
        tableContent.add(playerImage).left().pad(10).size(60);

        Label labelDescription = new Label(description, Assets.styleLabelChico);
        labelDescription.setWrap(true);
        tableContent.add(labelDescription).expand().fill().padLeft(5);

        tableContent.row().colspan(2);
        tableContent.add(button).expandX().right().padRight(10).size(120, 45);
        tableContent.row().colspan(2);
        tableContent.add(new Image(Assets.separadorHorizontal)).expandX().fill()
                .height(5).padTop(15);

        return tableContent;
    }

    private void initializeButtons() {
        arrayButtons = new Array<>();

        // default skin
        buttonDefault = new TextButton("Select", Assets.styleTextButtonPurchased);
        if (Settings.skinSeleccionada == SKIN_DEFAULT)
            buttonDefault.setVisible(false);

        addPressEffect(buttonDefault);
        buttonDefault.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.skinSeleccionada = SKIN_DEFAULT;
                setSelected(buttonDefault);
                Assets.cargarPersonaje();
            }
        });

        // red Android skin
        if (isRedAndroidPurchased)
            buttonRedAndroid = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            buttonRedAndroid = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == SKIN_RED_ANDROID)
            buttonRedAndroid.setVisible(false);

        addPressEffect(buttonRedAndroid);
        buttonRedAndroid.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isRedAndroidPurchased) {
                    Settings.skinSeleccionada = SKIN_RED_ANDROID;
                    setSelected(buttonRedAndroid);
                    Assets.cargarPersonaje();
                } else if (Settings.monedasActuales >= PRICE_RED_ANDROID) {
                    Settings.monedasActuales -= PRICE_RED_ANDROID;
                    setButtonStylePurchased(buttonRedAndroid);
                    isRedAndroidPurchased = true;
                    labelPriceRedAndroid.remove();
                    save();
                }
            }
        });

        // blue Android skin
        if (isBlueAndroidPurchased)
            buttonBlueAndroid = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            buttonBlueAndroid = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == SKIN_BLUE_ANDROID)
            buttonBlueAndroid.setVisible(false);

        addPressEffect(buttonBlueAndroid);
        buttonBlueAndroid.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isBlueAndroidPurchased) {
                    Settings.skinSeleccionada = SKIN_BLUE_ANDROID;
                    setSelected(buttonBlueAndroid);
                    Assets.cargarPersonaje();
                } else if (Settings.monedasActuales >= PRICE_BLUE_ANDROID) {
                    Settings.monedasActuales -= PRICE_BLUE_ANDROID;
                    setButtonStylePurchased(buttonBlueAndroid);
                    isBlueAndroidPurchased = true;
                    labelPriceBlueAndroid.remove();
                    save();
                }
            }
        });

        arrayButtons.add(buttonDefault);
        arrayButtons.add(buttonRedAndroid);
        arrayButtons.add(buttonBlueAndroid);
    }

    private void save() {
        preferences.putBoolean("didBuyAzul", isBlueAndroidPurchased);
        preferences.putBoolean("didBuyRojo", isRedAndroidPurchased);
        preferences.flush();
    }

    private void setButtonStylePurchased(TextButton button) {
        button.setStyle(Assets.styleTextButtonPurchased);
        button.setText("Select");
    }

    private void setSelected(TextButton selectedButton) {
        // I make all visible and at the end the selected button invisible
        for (TextButton button : arrayButtons) {
            button.setVisible(true);
        }
        selectedButton.setVisible(false);
    }

    protected void addPressEffect(final Actor actor) {
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
