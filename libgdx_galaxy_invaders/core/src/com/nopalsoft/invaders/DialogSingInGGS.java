package com.nopalsoft.invaders;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class DialogSingInGGS {
    Stage stage;
    final MainInvaders game;

    Dialog dialogSignIn, dialogRate;

    public DialogSingInGGS(MainInvaders game, Stage stage) {
        this.stage = stage;
        this.game = game;
    }

    public void showDialogSignIn() {
        dialogSignIn = new Dialog(Assets.idiomas.get("sign_in"), Assets.styleDialogPause);
        Label lblContenido = new Label(Assets.idiomas.get("sign_in_with_google_to_share_your_scores_and_achievements_with_your_friends"), Assets.styleLabelDialog);
        lblContenido.setWrap(true);

        dialogSignIn.getContentTable().add(lblContenido).width(300).height(120);

        TextButtonStyle stilo = new TextButtonStyle(Assets.btSignInUp, Assets.btSignInDown, null, Assets.font15);
        TextButton btSignIn = new TextButton(Assets.idiomas.get("sign_in"), stilo);
        btSignIn.getLabel().setWrap(true);
        TextButton btNotNow = new TextButton(Assets.idiomas.get("not_now"), Assets.styleTextButton);

        btNotNow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialogSignIn.hide();
            }
        });

        btSignIn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Handle login
                dialogSignIn.hide();
            }
        });

        dialogSignIn.getButtonTable().add(btSignIn).minWidth(140).fill();
        dialogSignIn.getButtonTable().add(btNotNow).minWidth(140).fill();
        dialogSignIn.show(stage);
    }

    public void showDialogRate() {
        dialogRate = new Dialog(Assets.idiomas.get("please_rate_the_app"), Assets.styleDialogPause);
        Label lblContenido = new Label(Assets.idiomas.get("thank_you_for_playing_if_you_like_this_game_please"), Assets.styleLabelDialog);
        lblContenido.setWrap(true);

        dialogRate.getContentTable().add(lblContenido).width(300).height(150);

        TextButton rate = new TextButton(Assets.idiomas.get("rate"), Assets.styleTextButton);
        TextButton btNotNow = new TextButton(Assets.idiomas.get("not_now"), Assets.styleTextButton);
        rate.setHeight(10);

        btNotNow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialogRate.hide();
            }
        });

        rate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO: Open appstore
                dialogRate.hide();
            }
        });

        dialogRate.getButtonTable().add(rate).minWidth(140).minHeight(40).fill();
        dialogRate.getButtonTable().add(btNotNow).minWidth(140).minHeight(40).fill();
        dialogRate.show(stage);
    }

    public boolean isDialogShown() {
        return stage.getActors().contains(dialogRate, true) || stage.getActors().contains(dialogSignIn, true);
    }

    public void dismissAll() {
        if (stage.getActors().contains(dialogRate, true))
            dialogRate.hide();

        if (stage.getActors().contains(dialogSignIn, true))
            dialogSignIn.hide();

    }
}
