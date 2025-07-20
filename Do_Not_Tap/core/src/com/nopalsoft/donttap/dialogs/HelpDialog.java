package com.nopalsoft.donttap.dialogs;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.donttap.Assets;
import com.nopalsoft.donttap.screens.Screens;

public class HelpDialog extends Group {
    static public float fadeDuration = 0.25f;
    Screens screen;

    public HelpDialog(Screens screen) {
        this.screen = screen;

        setSize(430, 460);
        setOrigin(getWidth() / 2f, getHeight() / 2f);
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 200);

        Image background = new Image(Assets.fondoPuntuaciones);
        background.setSize(getWidth(), getHeight());
        addActor(background);
        getColor().a = 0;

        Table tableHelp = new Table();
        tableHelp.setFillParent(true);

        ScrollPaneStyle scrollStyle = new ScrollPaneStyle(null, null, null,
                null, Assets.fondoPuntuaciones);
        final ScrollPane scroll = new ScrollPane(tableHelp, scrollStyle);
        scroll.setSize(getWidth(), getHeight() - 50);
        scroll.setPosition(0, 50);

        Label labelClassic = new Label("Classic:", Assets.labelStyleBlack);
        labelClassic.setWrap(true);

        Label labelTime = new Label("Time trial:", Assets.labelStyleBlack);
        labelTime.setWrap(true);

        Label labelEndless = new Label("Endless:", Assets.labelStyleBlack);
        labelEndless.setWrap(true);

        Label lbHelpClassic = new Label("Tap 100 tiles as fast as you can",
                Assets.labelStyleChico);
        lbHelpClassic.setWrap(true);

        Label labelHelpTime = new Label("Tap as fast as you can for 1 minute",
                Assets.labelStyleChico);
        labelHelpTime.setWrap(true);

        Label labelHelpEndless = new Label("How many tiles can you tap?",
                Assets.labelStyleChico);
        labelHelpEndless.setWrap(true);

        tableHelp.add(labelClassic).left().padLeft(5).width(135);
        tableHelp.add(lbHelpClassic).left().expandX().fill();

        tableHelp.row().padTop(15);
        tableHelp.add(labelTime).left().padLeft(5).width(135);
        tableHelp.add(labelHelpTime).left().expandX().fill();

        tableHelp.row().padTop(15);
        tableHelp.add(labelEndless).left().padLeft(5).width(135);
        tableHelp.add(labelHelpEndless).left().expandX().fill();

        final TextButton buttonOk = new TextButton("OK",
                Assets.textButtonStyleChico);
        buttonOk.setPosition(getWidth() / 2f - buttonOk.getWidth() / 2f, 5);
        screen.addPressEffect(buttonOk);
        buttonOk.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        addAction(Actions.sequence(Actions.alpha(1f, fadeDuration),
                Actions.run(new Runnable() {

                    @Override
                    public void run() {
                        addActor(scroll);
                        addActor(buttonOk);
                    }
                })));
    }

    Image dim;

    public void show(Stage stage) {

        dim = new Image(Assets.pixelNegro);
        dim.setFillParent(true);
        dim.getColor().a = 0;
        dim.addAction(Actions.alpha(.7f, fadeDuration - .5f));

        stage.addActor(dim);

        stage.addActor(this);
    }

    void hide() {

        dim.addAction(Actions.sequence(Actions.alpha(0, fadeDuration - .5f),
                Actions.removeActor()));
        addAction(Actions.sequence(Actions.alpha(0, fadeDuration),
                Actions.removeActor()));
    }
}
