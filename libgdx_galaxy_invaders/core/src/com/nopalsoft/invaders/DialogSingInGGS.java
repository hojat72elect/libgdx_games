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
    final GalaxyInvadersGame game;

    Dialog dialogSignIn, dialogRate;

    public DialogSingInGGS(GalaxyInvadersGame game, Stage stage) {
        this.stage = stage;
        this.game = game;
    }

    public void showDialogSignIn() {
        dialogSignIn = new Dialog(Assets.languagesBundle.get("sign_in"), Assets.styleDialogPause);
        Label labelContents = new Label(Assets.languagesBundle.get("sign_in_with_google_to_share_your_scores_and_achievements_with_your_friends"), Assets.styleLabelDialog);
        labelContents.setWrap(true);

        dialogSignIn.getContentTable().add(labelContents).width(300).height(120);

        TextButtonStyle style = new TextButtonStyle(Assets.buttonSignInUp, Assets.buttonSignInDown, null, Assets.font15);
        TextButton buttonSignIn = new TextButton(Assets.languagesBundle.get("sign_in"), style);
        buttonSignIn.getLabel().setWrap(true);
        TextButton buttonNotNow = new TextButton(Assets.languagesBundle.get("not_now"), Assets.styleTextButton);

        buttonNotNow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialogSignIn.hide();
            }
        });

        buttonSignIn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                dialogSignIn.hide();
            }
        });

        dialogSignIn.getButtonTable().add(buttonSignIn).minWidth(140).fill();
        dialogSignIn.getButtonTable().add(buttonNotNow).minWidth(140).fill();
        dialogSignIn.show(stage);
    }

    public void showDialogRate() {
        dialogRate = new Dialog(Assets.languagesBundle.get("please_rate_the_app"), Assets.styleDialogPause);
        Label labelContent = new Label(Assets.languagesBundle.get("thank_you_for_playing_if_you_like_this_game_please"), Assets.styleLabelDialog);
        labelContent.setWrap(true);

        dialogRate.getContentTable().add(labelContent).width(300).height(150);

        TextButton rate = new TextButton(Assets.languagesBundle.get("rate"), Assets.styleTextButton);
        TextButton buttonNotNow = new TextButton(Assets.languagesBundle.get("not_now"), Assets.styleTextButton);
        rate.setHeight(10);

        buttonNotNow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialogRate.hide();
            }
        });

        rate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialogRate.hide();
            }
        });

        dialogRate.getButtonTable().add(rate).minWidth(140).minHeight(40).fill();
        dialogRate.getButtonTable().add(buttonNotNow).minWidth(140).minHeight(40).fill();
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
