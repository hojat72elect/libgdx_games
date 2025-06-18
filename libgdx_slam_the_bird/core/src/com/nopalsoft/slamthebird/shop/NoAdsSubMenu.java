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
import com.nopalsoft.slamthebird.Settings;
import com.nopalsoft.slamthebird.SlamTheBirdGame;

public class NoAdsSubMenu {

    int priceNoAds = 35000;

    TextButton buttonNoAds;
    Label labelNoAds;

    Table tableContainer;
    SlamTheBirdGame game;

    public NoAdsSubMenu(final SlamTheBirdGame game, Table tableContainer) {
        this.game = game;
        this.tableContainer = tableContainer;
        tableContainer.clear();

        if (!Settings.didBuyNoAds)
            labelNoAds = new Label(priceNoAds + "", Assets.smallLabelStyle);

        buttonNoAds = new TextButton("Buy", Assets.styleTextButtonBuy);
        if (Settings.didBuyNoAds)
            buttonNoAds.setVisible(false);
        addPressEffect(buttonNoAds);
        buttonNoAds.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.currentCoins >= priceNoAds) {
                    Settings.currentCoins -= priceNoAds;
                    Settings.didBuyNoAds = true;
                    labelNoAds.setVisible(false);
                    buttonNoAds.setVisible(false);
                }
            }
        });

        tableContainer.add(new Image(Assets.horizontalSeparator)).expandX().fill().height(5);
        tableContainer.row();
        tableContainer
                .add(createPlayerTable(labelNoAds, Assets.buttonNoAds,
                        buttonNoAds)).expandX().fill();
        tableContainer.row();
    }

    private Table createPlayerTable(Label labelPrice, TextureRegionDrawable imageDrawable,
                                    TextButton button) {

        Image coinImage = new Image(Assets.coinsRegion);
        Image playerImage = new Image(imageDrawable);

        if (labelPrice == null)
            coinImage.setVisible(false);

        Table tableTitleBar = new Table();
        tableTitleBar.add(new Label("No more ads", Assets.smallLabelStyle)).expandX().left().padLeft(5);
        tableTitleBar.add(coinImage).right();
        tableTitleBar.add(labelPrice).right().padRight(10);

        Table tableDescription = new Table();
        tableDescription.add(playerImage).left().pad(10).size(55, 45);
        Label labelDescription = new Label("Buy it and no more ads will apper in the app", Assets.smallLabelStyle);
        labelDescription.setWrap(true);
        tableDescription.add(labelDescription).expand().fill().padLeft(5);

        Table tableContent = new Table();
        tableContent.add(tableTitleBar).expandX().fill().colspan(2).padTop(8);
        tableContent.row().colspan(2);
        tableContent.add(tableDescription).expandX().fill();
        tableContent.row().colspan(2);

        tableContent.add(button).right().padRight(10).size(120, 45);

        tableContent.row().colspan(2);
        tableContent.add(new Image(Assets.horizontalSeparator)).expandX().fill().height(5).padTop(15);

        return tableContent;
    }

    protected void addPressEffect(final Actor actor) {
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
