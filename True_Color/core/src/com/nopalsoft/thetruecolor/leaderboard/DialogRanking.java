package com.nopalsoft.thetruecolor.leaderboard;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.thetruecolor.Assets;
import com.nopalsoft.thetruecolor.MainTheTrueColor;
import com.nopalsoft.thetruecolor.scene2d.BaseDialogAmazon;
import com.nopalsoft.thetruecolor.scene2d.BaseDialogFacebook;
import com.nopalsoft.thetruecolor.scene2d.BaseDialogGoogle;
import com.nopalsoft.thetruecolor.screens.MainMenuScreen;
import com.nopalsoft.thetruecolor.screens.Screens;


public class DialogRanking extends Group {
    public static final float WIDTH = 400;
    public static final float HEIGHT = 385;

    MainMenuScreen menuScreen;
    MainTheTrueColor game;

    Label rankingTitle;

    Button buttonFacebook;
    Button buttonGoogle;

    BaseDialogFacebook ventanaFacebook;
    BaseDialogGoogle ventanaGoogle;
    BaseDialogAmazon ventanaAmazon;

    Table contenedor;

    public DialogRanking(MainMenuScreen screen) {
        menuScreen = screen;
        game = screen.game;
        setBounds(Screens.SCREEN_WIDTH / 2f - WIDTH / 2f, 210, WIDTH, HEIGHT);
        setBackground(Assets.rankingDialogDrawable);

        rankingTitle = new Label(Assets.languagesBundle.get("ranking"), new Label.LabelStyle(Assets.fontSmall, Color.WHITE));
        rankingTitle.setPosition(15, 328);

        ventanaFacebook = new BaseDialogFacebook(screen);
        ventanaGoogle = new BaseDialogGoogle(screen);
        ventanaAmazon = new BaseDialogAmazon(screen);

        buttonFacebook = new Button(Assets.buttonFacebookDrawable);

        menuScreen.addPressEffect(buttonFacebook);
        buttonFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ventanaFacebook.show(getStage());
            }
        });

        TextureRegionDrawable btLoginKeyFrame = Assets.buttonGoogleDrawable;


        buttonGoogle = new Button(btLoginKeyFrame);

        menuScreen.addPressEffect(buttonGoogle);
        buttonGoogle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                    ventanaGoogle.show(getStage());
            }
        });

        Table tbSocial = new Table();
        tbSocial.setSize(130, 50);
        tbSocial.setPosition(255, 328);
        tbSocial.defaults().expandX().size(50).right();
        tbSocial.add(buttonFacebook);

        if (Gdx.app.getType() != ApplicationType.WebGL && Gdx.app.getType() != ApplicationType.iOS) {
            tbSocial.add(buttonGoogle);
        }

        addActor(rankingTitle);
        addActor(tbSocial);

        contenedor = new Table();

        ScrollPane scroll = new ScrollPane(contenedor);
        scroll.setSize(WIDTH, 320);
        scroll.setPosition(0, 0);

        addActor(scroll);

        contenedor.top();
    }

    private void setBackground(NinePatchDrawable dialogRanking) {
        Image img = new Image(dialogRanking);
        img.setSize(getWidth(), getHeight());
        addActor(img);
    }

    public void addPerson(Person person) {
        contenedor.add(person);
        contenedor.row();
    }

    public void clearLeaderboard() {
        contenedor.clear();
    }
}
