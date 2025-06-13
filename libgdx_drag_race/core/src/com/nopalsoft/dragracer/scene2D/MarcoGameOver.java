package com.nopalsoft.dragracer.scene2D;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.dragracer.Assets;
import com.nopalsoft.dragracer.Settings;
import com.nopalsoft.dragracer.screens.Screens;

public class MarcoGameOver extends Group {

    Screens screen;

    public MarcoGameOver(final Screens screen, int distance, int coins) {
        this.screen = screen;
        setSize(420, 350);
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 900);
        addAction(Actions.moveTo(getX(), 390, 1, Interpolation.bounceOut));
        Image background = new Image(Assets.fondoPuntuaciones);
        background.setSize(getWidth(), getHeight());
        addActor(background);

        Label lblScore = new Label("Distance\n" + distance + "m",
                Assets.labelStyleGrande);
        lblScore.setAlignment(Align.center);
        lblScore.setFontScale(1.3f);
        lblScore.setPosition(getWidth() / 2f - lblScore.getWidth() / 2f, 210);
        addActor(lblScore);

        Table bestScoreTable = new Table();
        bestScoreTable.setSize(getWidth(), 110);
        bestScoreTable.setY(80);
        bestScoreTable.padLeft(15).padRight(15);
        // bestScoreTable.debug();

        Label lblBestScore = new Label("Best distance", Assets.labelStyleGrande);
        lblBestScore.setAlignment(Align.left);
        lblBestScore.setFontScale(.75f);

        Label lblNumBestScore = new Label(Settings.bestScore + "m",
                Assets.labelStyleGrande);
        lblNumBestScore.setAlignment(Align.right);
        lblNumBestScore.setFontScale(.75f);

        Label lblCoins = new Label("Coins", Assets.labelStyleGrande);
        lblCoins.setAlignment(Align.left);
        lblCoins.setFontScale(.75f);

        Label lblNumBestCoins = new Label(coins + "", Assets.labelStyleGrande);
        lblNumBestCoins.setAlignment(Align.right);
        lblNumBestCoins.setFontScale(.75f);

        bestScoreTable.add(lblBestScore).left();
        bestScoreTable.add(lblNumBestScore).right().expand();

        bestScoreTable.row();
        bestScoreTable.add(lblCoins).left();
        bestScoreTable.add(lblNumBestCoins).right().expand();

        // Facebook Twitter
        Button btShareFacebook, btShareTwitter;

        btShareTwitter = new Button(new TextureRegionDrawable(Assets.btTwitter));
        btShareTwitter.setSize(50, 50);
        btShareTwitter.setPosition(155, 20);
        screen.addPressEffect(btShareTwitter);


        btShareFacebook = new Button(new TextureRegionDrawable(
                Assets.btFacebook));
        btShareFacebook.setSize(50, 50);
        btShareFacebook.setPosition(225, 20);
        screen.addPressEffect(btShareFacebook);

        addActor(bestScoreTable);
        addActor(btShareTwitter);
        addActor(btShareFacebook);
    }
}
