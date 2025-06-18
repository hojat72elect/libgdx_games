package com.nopalsoft.slamthebird.shop;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.Settings;
import com.nopalsoft.slamthebird.SlamTheBirdGame;

public class GetCoinsSubMenu {

    int facebookLikeCoinBonus = 1500;

    TextButton buttonLikeFacebook;
    TextButton buttonBuy5MCoins, buttonBuy15MCoins, buttonBuy30MCoins,
            buttonBuy50MCoins;

    Table tableContainer;
    SlamTheBirdGame game;

    public GetCoinsSubMenu(final SlamTheBirdGame game, Table tableContainer) {
        this.game = game;
        this.tableContainer = tableContainer;
        tableContainer.clear();

        buttonLikeFacebook = new TextButton("Like us", Assets.styleTextButtonBuy);
        if (Settings.didLikeFacebook)
            buttonLikeFacebook = new TextButton("Visit Us",
                    Assets.styleTextButtonSelected);
        addPressEffect(buttonLikeFacebook);
        buttonLikeFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!Settings.didLikeFacebook) {

                    Settings.didLikeFacebook = true;
                    game.stage.addAction(Actions.sequence(Actions.delay(1),
                            Actions.run(() -> {
                                Settings.currentCoins += facebookLikeCoinBonus;
                                buttonLikeFacebook.setText("Visit us");
                                buttonLikeFacebook
                                        .setStyle(Assets.styleTextButtonSelected);
                            })));
                }
            }
        });

        buttonBuy5MCoins = new TextButton("Buy", Assets.styleTextButtonBuy);
        addPressEffect(buttonBuy5MCoins);

        buttonBuy15MCoins = new TextButton("Buy", Assets.styleTextButtonBuy);
        addPressEffect(buttonBuy15MCoins);

        buttonBuy30MCoins = new TextButton("Buy", Assets.styleTextButtonBuy);
        addPressEffect(buttonBuy30MCoins);

        buttonBuy50MCoins = new TextButton("Buy", Assets.styleTextButtonBuy);
        addPressEffect(buttonBuy50MCoins);

        tableContainer.add(new Image(Assets.horizontalSeparator)).expandX().fill()
                .height(5);
        tableContainer.row();
        tableContainer
                .add(createPlayerTable(facebookLikeCoinBonus,
                        Assets.buttonFacebook, "Like us on facebook and get "
                                + facebookLikeCoinBonus + " coins",
                        buttonLikeFacebook)).expandX().fill();
        tableContainer.row();

        TextureRegionDrawable coinDrawable = new TextureRegionDrawable(Assets.coinsRegion);

        tableContainer
                .add(createPlayerTable(
                        5000,
                        coinDrawable,
                        "Coin simple pack. A quick way to buy simple upgrades",
                        buttonBuy5MCoins)).expandX().fill();
        tableContainer.row();


        tableContainer
                .add(createPlayerTable(
                        15000,
                        coinDrawable,
                        "Coin super pack. Get some cash for upgrades and characters",
                        buttonBuy15MCoins)).expandX().fill();
        tableContainer.row();

        tableContainer
                .add(createPlayerTable(
                        30000,
                        coinDrawable,
                        "Coin mega pack. You can buy a lot of characters and upgrades",
                        buttonBuy30MCoins)).expandX().fill();
        tableContainer.row();

        tableContainer
                .add(createPlayerTable(
                        50000,
                        coinDrawable,
                        "Coin super mega pack. Get this pack and you will be slamming in cash",
                        buttonBuy50MCoins)).expandX().fill();
        tableContainer.row();
    }

    private Table createPlayerTable(int numCoinsToGet,
                                    TextureRegionDrawable imageDrawable, String description, TextButton button) {

        Image coinImage = new Image(Assets.coinsRegion);
        Image playerImage = new Image(imageDrawable);

        Table titleBarTable = new Table();
        titleBarTable
                .add(new Label("Get " + numCoinsToGet, Assets.smallLabelStyle))
                .left().padLeft(5);
        titleBarTable.add(coinImage).left().expandX().padLeft(5);

        Table descriptionTable = new Table();
        descriptionTable.add(playerImage).left().pad(10).size(55, 45);
        Label descriptionLabel = new Label(description, Assets.smallLabelStyle);
        descriptionLabel.setWrap(true);
        descriptionTable.add(descriptionLabel).expand().fill().padLeft(5);

        Table contentTable = new Table();
        contentTable.add(titleBarTable).expandX().fill().colspan(2).padTop(8);
        contentTable.row().colspan(2);
        contentTable.add(descriptionTable).expandX().fill();
        contentTable.row().colspan(2);

        contentTable.add(button).right().padRight(10).size(120, 45);

        contentTable.row().colspan(2);
        contentTable.add(new Image(Assets.horizontalSeparator)).expandX().fill()
                .height(5).padTop(15);

        return contentTable;
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
