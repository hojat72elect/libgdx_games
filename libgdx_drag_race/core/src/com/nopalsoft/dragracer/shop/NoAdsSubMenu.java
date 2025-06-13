package com.nopalsoft.dragracer.shop;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.dragracer.Assets;
import com.nopalsoft.dragracer.MainStreet;
import com.nopalsoft.dragracer.Settings;

public class NoAdsSubMenu {

    int priceNoAds = 20000;

    TextButton buttonNoAds;
    Label labelNoAds;

    Table containerTable;
    MainStreet game;

    public NoAdsSubMenu(final MainStreet game, Table containerTable) {
        this.game = game;
        this.containerTable = containerTable;
        containerTable.clear();

        if (!Settings.didBuyNoAds)
            labelNoAds = new Label(priceNoAds + "", Assets.labelStyleSmall);

        buttonNoAds = new TextButton("Buy", Assets.styleTextButtonBuy);
        if (Settings.didBuyNoAds)
            buttonNoAds.setVisible(false);
        addPressEffect(buttonNoAds);
        buttonNoAds.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.coinsTotal >= priceNoAds) {
                    Settings.coinsTotal -= priceNoAds;
                    Settings.didBuyNoAds = true;
                    labelNoAds.setVisible(false);
                    buttonNoAds.setVisible(false);
                }
            }
        });

        // Upgrade BoostTime
        containerTable.add(new Image(Assets.horizontalSeparatorDrawable)).expandX().fill()
                .height(5);
        containerTable.row();
        containerTable
                .add(addPlayerTable(labelNoAds,
                        Assets.buttonNoAds,
                        buttonNoAds))
                .expandX().fill();
        containerTable.row();
    }

    private Table addPlayerTable(Label labelPrice,
                                 TextureRegionDrawable imageDrawable, TextButton button) {

        Image coinImage = new Image(Assets.coinFront);
        Image playerImage = new Image(imageDrawable);

        if (labelPrice == null)
            coinImage.setVisible(false);

        Table titleBarTable = new Table();
        titleBarTable.add(new Label("No more ads", Assets.labelStyleSmall)).expandX()
                .left().padLeft(5);
        titleBarTable.add(coinImage).right();
        titleBarTable.add(labelPrice).right().padRight(10);

        Table descriptionTable = new Table();
        descriptionTable.add(playerImage).left().pad(10).size(55, 45);
        Label descriptionLabel = new Label("Buy it and no more ads will appear in the app", Assets.labelStyleSmall);
        descriptionLabel.setWrap(true);
        descriptionLabel.setFontScale(.85f);
        descriptionTable.add(descriptionLabel).expand().fill().padLeft(5);

        Table tbContent = new Table();
        tbContent.add(titleBarTable).expandX().fill().colspan(2).padTop(8);
        tbContent.row().colspan(2);
        tbContent.add(descriptionTable).expandX().fill();
        tbContent.row().colspan(2);

        tbContent.add(button).right().padRight(10).size(120, 45);

        tbContent.row().colspan(2);
        tbContent.add(new Image(Assets.horizontalSeparatorDrawable)).expandX().fill()
                .height(5).padTop(15);

        return tbContent;
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
