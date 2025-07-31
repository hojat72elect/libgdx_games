package com.nopalsoft.zombiekiller.shop;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.MainZombie;
import com.nopalsoft.zombiekiller.Settings;

public class GetGemsSubMenu {

    int facebookLikeReward = 1500;

    // Common
    TextButton buttonLikeFacebook;
    TextButton buttonInviteFacebook;

    // iOS
    TextButton buttonBuy5kCoins, buttonBuy15k, buttonBuy30kCoins, buttonBuy50kCoins;

    Table containerTable;
    MainZombie game;
    I18NBundle languagesBundle;
    String textBuy;

    public GetGemsSubMenu(final MainZombie game, Table containerTable) {
        this.game = game;
        this.containerTable = containerTable;
        languagesBundle = game.idiomas;
        containerTable.clear();

        textBuy = languagesBundle.get("buy");

        buttonLikeFacebook = new TextButton(languagesBundle.get("like_us"), Assets.styleTextButtonBuy);
        if (Settings.didLikeFacebook)
            buttonLikeFacebook = new TextButton(languagesBundle.get("visit_us"), Assets.styleTextButtonPurchased);
        addPressEffect(buttonLikeFacebook);

        buttonInviteFacebook = new TextButton(languagesBundle.get("invite"), Assets.styleTextButtonBuy);
        addPressEffect(buttonInviteFacebook);

        buttonBuy5kCoins = new TextButton(textBuy, Assets.styleTextButtonBuy);
        addPressEffect(buttonBuy5kCoins);


        buttonBuy15k = new TextButton(textBuy, Assets.styleTextButtonBuy);
        addPressEffect(buttonBuy15k);


        buttonBuy30kCoins = new TextButton(textBuy, Assets.styleTextButtonBuy);
        addPressEffect(buttonBuy30kCoins);

        buttonBuy50kCoins = new TextButton(textBuy, Assets.styleTextButtonBuy);
        addPressEffect(buttonBuy50kCoins);


        String faceLikeDescription = languagesBundle.format("facebook_like_description", facebookLikeReward);
        String faceInviteDescription = languagesBundle.format("facebook_invite_description", Settings.NUM_GEMS_INVITE_FACEBOOK);


        containerTable.add(createPlayerTable(facebookLikeReward, Assets.btFacebook, faceLikeDescription, buttonLikeFacebook)).expandX().fill();
        containerTable.row();

        containerTable.add(createPlayerTable(Settings.NUM_GEMS_INVITE_FACEBOOK, Assets.btFacebook, faceInviteDescription, buttonInviteFacebook))
                .expandX().fill();
        containerTable.row();


        TextureRegionDrawable coinDrawable = new TextureRegionDrawable(Assets.itemGem);
        // Coin sale

        // Buy 5 thousand
        containerTable.add(createPlayerTable(5000, coinDrawable, languagesBundle.get("coin_simple_pack"), buttonBuy5kCoins)).expandX().fill();
        containerTable.row();

        // Buy 15 thousand
        containerTable.add(createPlayerTable(15000, coinDrawable, languagesBundle.get("coin_super_pack"), buttonBuy15k)).expandX().fill();
        containerTable.row();

        containerTable.add(createPlayerTable(30000, coinDrawable, languagesBundle.get("coin_mega_pack"), buttonBuy30kCoins)).expandX().fill();
        containerTable.row();

        containerTable.add(createPlayerTable(50000, coinDrawable, languagesBundle.get("coin_super_mega_pack"), buttonBuy50kCoins)).expandX().fill();
        containerTable.row();
    }

    private Table createPlayerTable(int numCoinsToAward, TextureRegionDrawable playerDrawable, String description, TextButton button) {

        Image coinImage = new Image(Assets.itemGem);
        Image playerImage = new Image(playerDrawable);

        Table titleBarTable = new Table();
        titleBarTable.add(new Label(languagesBundle.format("get_num", numCoinsToAward), Assets.labelStyleChico)).left().padLeft(5);
        titleBarTable.add(coinImage).left().expandX().padLeft(5).size(20);

        Table descriptionTable = new Table();
        descriptionTable.add(playerImage).left().pad(10).size(55, 45);
        Label labelDescription = new Label(description, Assets.labelStyleChico);
        labelDescription.setWrap(true);
        labelDescription.setFontScale(.9f);
        descriptionTable.add(labelDescription).expand().fill().padLeft(5);

        Table tableContent = new Table();
        tableContent.pad(0);
        tableContent.defaults().padLeft(20).padRight(20);
        tableContent.setBackground(Assets.storeTableBackground);
        tableContent.add(titleBarTable).expandX().fill().colspan(2).padTop(20);
        tableContent.row().colspan(2);
        tableContent.add(descriptionTable).expandX().fill();
        tableContent.row().colspan(2);

        tableContent.add(button).right().padBottom(20).size(120, 45);

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
