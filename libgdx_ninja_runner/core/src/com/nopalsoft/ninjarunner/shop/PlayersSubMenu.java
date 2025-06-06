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
import com.nopalsoft.ninjarunner.NinjaRunnerGame;
import com.nopalsoft.ninjarunner.Settings;
import com.nopalsoft.ninjarunner.game_objects.Player;
import com.nopalsoft.ninjarunner.scene2d.AnimatedSpriteActor;

public class PlayersSubMenu {

    final int PRICE_NINJA = 1000;

    boolean didBuyNinja;

    Label labelNinjaPrice;

    TextButton buttonSelectShanti, buttonBuyNinja;
    Array<TextButton> arrayButtons;

    Table container;
    I18NBundle languages;

    String textBuy;
    String textSelect;

    private final static Preferences preferences = Gdx.app.getPreferences("com.tiar.shantirunner.shop");

    public PlayersSubMenu(Table container, NinjaRunnerGame game) {
        languages = game.languages;
        this.container = container;
        container.clear();
        preferences.clear();
        preferences.flush();

        loadPurchases();

        textBuy = languages.get("buy");
        textSelect = languages.get("select");

        if (!didBuyNinja)
            labelNinjaPrice = new Label(PRICE_NINJA + "", Assets.labelStyleSmall);

        initializeButtons();

        container.defaults().expand().fill().padLeft(10).padRight(20).padBottom(10);

        container.add(addPlayer("Runner girl", null, Assets.girlRunAnimation, languages.get("bombDescription"), buttonSelectShanti)).row();
        container.add(addPlayer("Runner Ninja", labelNinjaPrice, Assets.ninjaRunAnimation, languages.get("bombDescription"), buttonBuyNinja)).row();
    }

    public Table addPlayer(String title, Label labelPrice, AnimationSprite image, String description, TextButton button) {

        Image coin = new Image(Assets.coinAnimation.getKeyFrame(0));
        AnimatedSpriteActor imagePlayer = new AnimatedSpriteActor(image);

        if (labelPrice == null)
            coin.setVisible(false);

        Table tableTitleBar = new Table();
        tableTitleBar.add(new Label(title, Assets.labelStyleSmall)).expandX().left();
        tableTitleBar.add(coin).right().size(20);
        tableTitleBar.add(labelPrice).right().padRight(10);

        Table tableContent = new Table();
        tableContent.setBackground(Assets.backgroundItemShop);
        tableContent.pad(5); // Ninepatch adds padding by default.

        tableContent.defaults().padLeft(20).padRight(20);
        tableContent.add(tableTitleBar).expandX().fill().colspan(2);
        tableContent.row();

        tableContent.add(imagePlayer).size(120, 99);
        Label labelDescription = new Label(description, Assets.labelStyleSmall);
        labelDescription.setWrap(true);
        tableContent.add(labelDescription).expand().fill();

        tableContent.row().colspan(2);
        tableContent.add(button).expandX().right().size(120, 45);
        tableContent.row().colspan(2);

        tableContent.debugAll();
        return tableContent;
    }

    private void initializeButtons() {
        arrayButtons = new Array<>();

        // DEFAULT
        buttonSelectShanti = new TextButton(textSelect, Assets.styleTextButtonPurchased);
        if (Settings.selectedSkin == Player.TYPE_GIRL)
            buttonSelectShanti.setVisible(false);

        buttonSelectShanti.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.selectedSkin = Player.TYPE_GIRL;
                setSelected(buttonSelectShanti);
            }
        });

        // SKIN_NINJA
        if (didBuyNinja)
            buttonBuyNinja = new TextButton(textSelect, Assets.styleTextButtonPurchased);
        else
            buttonBuyNinja = new TextButton(textBuy, Assets.styleTextButtonBuy);

        if (Settings.selectedSkin == Player.TYPE_NINJA)
            buttonBuyNinja.setVisible(false);

        buttonBuyNinja.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyNinja) {
                    Settings.selectedSkin = Player.TYPE_NINJA;
                    setSelected(buttonBuyNinja);
                } else if (Settings.totalCoins >= PRICE_NINJA) {
                    Settings.totalCoins -= PRICE_NINJA;
                    setButtonStylePurchased(buttonBuyNinja);
                    labelNinjaPrice.remove();
                    didBuyNinja = true;
                }
                savePurchases();
            }
        });

        arrayButtons.add(buttonSelectShanti);
        arrayButtons.add(buttonBuyNinja);
    }

    private void loadPurchases() {
        didBuyNinja = preferences.getBoolean("didBuyNinja", false);
    }

    private void savePurchases() {
        preferences.putBoolean("didBuyNinja", didBuyNinja);
        preferences.flush();
    }

    private void setButtonStylePurchased(TextButton button) {
        button.setStyle(Assets.styleTextButtonPurchased);
        button.setText(textSelect);
    }

    private void setSelected(TextButton selectedButton) {
        // I make all other buttons visible and at the end, the selected button invisible.
        for (TextButton button : arrayButtons) {
            button.setVisible(true);
        }
        selectedButton.setVisible(false);
    }
}
