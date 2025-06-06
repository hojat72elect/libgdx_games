package com.nopalsoft.ninjarunner.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.nopalsoft.ninjarunner.AnimationSprite;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.NinjaRunnerGame;
import com.nopalsoft.ninjarunner.Settings;
import com.nopalsoft.ninjarunner.game_objects.Mascot;
import com.nopalsoft.ninjarunner.scene2d.AnimatedSpriteActor;

public class MascotsSubMenu {

    final int BOMB_PRICE = 5000;

    boolean didBuyBomb;

    Label labelBirdPrice, labelBombPrice;

    TextButton buttonBird, buttonBomb;
    Array<TextButton> arrayButtons;

    public final int MAX_LEVEL = 6;
    final int PRICE_LEVEL_1 = 350;
    final int PRICE_LEVEL_2 = 1000;
    final int PRICE_LEVEL_3 = 3000;
    final int PRICE_LEVEL_4 = 4500;
    final int PRICE_LEVEL_5 = 5000;
    final int PRICE_LEVEL_6 = 7500;

    Button buttonUpgradeBird, buttonUpgradeBomb;

    Image[] arrayBird;
    Image[] arrayBomb;

    Table container;
    I18NBundle languages;

    String textBuy;
    String textSelect;

    private final static Preferences preferences = Gdx.app.getPreferences("com.tiar.shantirunner.shop");

    public MascotsSubMenu(Table container, NinjaRunnerGame game) {
        languages = game.languages;
        this.container = container;
        container.clear();

        loadPurchases();

        textBuy = languages.get("buy");
        textSelect = languages.get("select");

        arrayBird = new Image[MAX_LEVEL];
        arrayBomb = new Image[MAX_LEVEL];

        if (Settings.MASCOT_LEVEL_BIRD < MAX_LEVEL) {
            labelBirdPrice = new Label(calculatePrice(Settings.MASCOT_LEVEL_BIRD) + "", Assets.labelStyleSmall);
        }

        if (!didBuyBomb) {
            labelBombPrice = new Label(BOMB_PRICE + "", Assets.labelStyleSmall);
        } else if (Settings.MASCOT_LEVEL_BOMB < MAX_LEVEL) {
            labelBombPrice = new Label(calculatePrice(Settings.MASCOT_LEVEL_BOMB) + "", Assets.labelStyleSmall);
        }

        initializeButtons();

        container.defaults().expand().fill().padLeft(10).padRight(20).padBottom(10);

        container.add(
                addMascot("Chicken", labelBirdPrice, Assets.mascotBirdFlyAnimation, 60, 54, languages.get("pinkChikenDescription"), buttonBird, arrayBird,
                        buttonUpgradeBird)).row();
        container.add(
                        addMascot("Bomb", labelBombPrice, Assets.mascotBombFlyAnimation, 53, 64, languages.get("bombDescription"), buttonBomb, arrayBomb, buttonUpgradeBomb))
                .row();

        setArrays();
    }

    public Table addMascot(String title, Label labelPrice, AnimationSprite image, float imageWidth, float imageHeight, String description,
                           TextButton buttonBuy, Image[] arrayLevel, Button buttonUpgrade) {
        Image coinImage = new Image(Assets.coinAnimation.getKeyFrame(0));
        AnimatedSpriteActor imagePlayer = new AnimatedSpriteActor(image);

        if (labelPrice == null)
            coinImage.setVisible(false);

        Table tableTitleBar = new Table();
        tableTitleBar.add(new Label(title, Assets.labelStyleSmall)).expandX().left();
        tableTitleBar.add(coinImage).right().size(20);
        tableTitleBar.add(labelPrice).right().padRight(10);

        Table tbContent = new Table();
        tbContent.setBackground(Assets.backgroundItemShop);
        tbContent.pad(5);

        tbContent.add(tableTitleBar).expandX().fill().colspan(2);
        tbContent.row();

        tbContent.add(imagePlayer).size(imageWidth, imageHeight);
        Label labelDescription = new Label(description, Assets.labelStyleSmall);
        labelDescription.setWrap(true);
        tbContent.add(labelDescription).expand().fill();

        Table auxTab = new Table();
        auxTab.setBackground(Assets.backgroundUpgradeBar);
        auxTab.pad(5);
        auxTab.defaults().padLeft(5);
        for (int i = 0; i < MAX_LEVEL; i++) {
            arrayLevel[i] = new Image();
            auxTab.add(arrayLevel[i]).size(15);
        }

        tbContent.row();
        tbContent.add(auxTab);
        tbContent.add(buttonUpgrade).left().size(40);

        tbContent.row().colspan(2);
        tbContent.add(buttonBuy).expandX().right().size(120, 45);
        tbContent.row().colspan(2);

        return tbContent;
    }

    private void initializeButtons() {
        arrayButtons = new Array<>();

        {// DEFAULT
            {// BUY
                buttonBird = new TextButton(textSelect, Assets.styleTextButtonPurchased);
                if (Settings.selectedMascot == Mascot.MascotType.PINK_BIRD)
                    buttonBird.setVisible(false);
                buttonBird.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Settings.selectedMascot = Mascot.MascotType.PINK_BIRD;
                        setSelected(buttonBird);
                    }
                });
            }
            {// UPGRADE
                buttonUpgradeBird = new Button(Assets.styleButtonUpgrade);
                if (Settings.MASCOT_LEVEL_BIRD == MAX_LEVEL)
                    buttonUpgradeBird.setVisible(false);
                buttonUpgradeBird.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (Settings.totalCoins >= calculatePrice(Settings.MASCOT_LEVEL_BIRD)) {
                            Settings.totalCoins -= calculatePrice(Settings.MASCOT_LEVEL_BIRD);
                            Settings.MASCOT_LEVEL_BIRD++;
                            updateLabelPriceAndButton(Settings.MASCOT_LEVEL_BIRD, labelBirdPrice, buttonUpgradeBird);
                            setArrays();
                        }
                    }
                });
            }
        }

        {// MASCOT
            {// BUY
                if (didBuyBomb)
                    buttonBomb = new TextButton(textSelect, Assets.styleTextButtonPurchased);
                else
                    buttonBomb = new TextButton(textBuy, Assets.styleTextButtonBuy);

                if (Settings.selectedMascot == Mascot.MascotType.BOMB)
                    buttonBomb.setVisible(false);

                buttonBomb.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (didBuyBomb) {
                            Settings.selectedMascot = Mascot.MascotType.BOMB;
                            setSelected(buttonBomb);
                        } else if (Settings.totalCoins >= BOMB_PRICE) {
                            Settings.totalCoins -= BOMB_PRICE;
                            setButtonStylePurchased(buttonBomb);
                            didBuyBomb = true;
                            buttonUpgradeBomb.setVisible(true);
                            updateLabelPriceAndButton(Settings.MASCOT_LEVEL_BOMB, labelBombPrice, buttonUpgradeBomb);
                        }
                        savePurchases();
                    }
                });
            }
            {// UPGRADE
                buttonUpgradeBomb = new Button(Assets.styleButtonUpgrade);
                if (Settings.MASCOT_LEVEL_BOMB == MAX_LEVEL || !didBuyBomb)
                    buttonUpgradeBomb.setVisible(false);
                buttonUpgradeBomb.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (Settings.totalCoins >= calculatePrice(Settings.MASCOT_LEVEL_BOMB)) {
                            Settings.totalCoins -= calculatePrice(Settings.MASCOT_LEVEL_BOMB);
                            Settings.MASCOT_LEVEL_BOMB++;
                            updateLabelPriceAndButton(Settings.MASCOT_LEVEL_BOMB, labelBombPrice, buttonUpgradeBomb);
                            setArrays();
                        }
                    }
                });
            }
        }

        arrayButtons.add(buttonBird);
        arrayButtons.add(buttonBomb);
    }

    private void loadPurchases() {
        didBuyBomb = preferences.getBoolean("didBuyBomb", false);
    }

    private void savePurchases() {
        preferences.putBoolean("didBuyBomb", didBuyBomb);
        preferences.flush();
    }

    private void setButtonStylePurchased(TextButton button) {
        button.setStyle(Assets.styleTextButtonPurchased);
        button.setText(textSelect);
    }

    private void setSelected(TextButton button) {
        // I make all visible and at the end the selected button invisible
        for (TextButton arrayButton : arrayButtons) {
            arrayButton.setVisible(true);
        }
        button.setVisible(false);
    }

    private int calculatePrice(int level) {
        return switch (level) {
            case 0 -> PRICE_LEVEL_1;
            case 1 -> PRICE_LEVEL_2;
            case 2 -> PRICE_LEVEL_3;
            case 3 -> PRICE_LEVEL_4;
            case 4 -> PRICE_LEVEL_5;
            default -> PRICE_LEVEL_6;
        };
    }

    private void updateLabelPriceAndButton(int level, Label label, Button button) {
        if (level < MAX_LEVEL) {
            label.setText(calculatePrice(level) + "");
        } else {
            label.setVisible(false);
            button.setVisible(false);
        }
    }

    private void setArrays() {
        for (int i = 0; i < Settings.MASCOT_LEVEL_BIRD; i++) {
            arrayBird[i].setDrawable(new TextureRegionDrawable(Assets.buttonShare));
        }

        for (int i = 0; i < Settings.MASCOT_LEVEL_BOMB; i++) {
            arrayBomb[i].setDrawable(new TextureRegionDrawable(Assets.buttonShare));
        }
    }
}
