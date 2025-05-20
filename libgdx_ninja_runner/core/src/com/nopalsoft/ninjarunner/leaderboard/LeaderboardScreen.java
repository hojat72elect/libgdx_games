package com.nopalsoft.ninjarunner.leaderboard;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.game.GameScreen;
import com.nopalsoft.ninjarunner.screens.Screens;

public class LeaderboardScreen extends Screens {

    Table tbMenu;
    Button btLeaderboard, btFacebook, btInviteFriend;
    Button btGoogle;

    ScrollPane scroll;
    Table contenedor;


    public LeaderboardScreen(Game _game) {
        super(_game);

        Label lbShop = new Label("Leaderboards", Assets.labelStyleGrande);

        Table tbTitle = new Table();
        tbTitle.setSize(400, 100);
        tbTitle.setPosition(SCREEN_WIDTH / 2f - tbTitle.getWidth() / 2f, SCREEN_HEIGHT - tbTitle.getHeight());
        tbTitle.setBackground(Assets.backgroundTitleShop);
        tbTitle.padTop(20).padBottom(5);
        // tbTitle.debugAll();

        tbTitle.row().colspan(2);
        tbTitle.add(lbShop).expand();
        tbTitle.row();

        Image imgGem = new Image(Assets.moneda.getKeyFrame(0));
        imgGem.setSize(20, 20);


        initButtons();

        tbMenu = new Table();
        tbMenu.defaults().size(58).padBottom(8);

        tbMenu.row();
        tbMenu.add(btLeaderboard);

        tbMenu.row();
        tbMenu.add(btFacebook);

        tbMenu.row();
        tbMenu.add(btGoogle);

        tbMenu.row();
        tbMenu.add(btInviteFriend);


        Table tbShop = new Table();
        tbShop.setSize(SCREEN_WIDTH, SCREEN_HEIGHT - tbTitle.getHeight());
        tbShop.setBackground(Assets.backgroundShop);
        tbShop.pad(25, 5, 15, 5);

        // Contenedor
        contenedor = new Table();
        contenedor.defaults().expand().fill().padLeft(10).padRight(20).padBottom(10);

        scroll = new ScrollPane(contenedor, new ScrollPaneStyle(null, null, null, null, null));
        scroll.setFadeScrollBars(false);
        scroll.setSize(SCREEN_WIDTH - tbMenu.getWidth(), 420);
        scroll.setPosition(tbMenu.getWidth() + 1, 0);
        scroll.setVariableSizeKnobs(false);

        tbShop.add(tbMenu).expandY().width(122);
        tbShop.add(scroll).expand().fill();

        stage.addActor(tbTitle);
        stage.addActor(tbShop);


        if (game.arrPerson != null)
            updateLeaderboard();


        btLeaderboard.setChecked(true);


    }

    void initButtons() {
        btLeaderboard = new Button(Assets.btShop, Assets.btShopPress, Assets.btShopPress);
        btFacebook = new Button(Assets.btFacebook, Assets.btFacebookPress, Assets.btFacebookPress);
        btGoogle = new Button(Assets.btAchievement, Assets.btAchievementPress, Assets.btLeaderboardPress);
        btInviteFriend = new Button(Assets.btSettings, Assets.btSettingsPress, Assets.btLeaderboardPress);

        btLeaderboard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }

        });

        btFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!game.facebookHandler.facebookIsSignedIn())
                    game.facebookHandler.facebookSignIn();
            }
        });

        btGoogle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.gameServiceHandler.isSignedIn()) {
                    game.gameServiceHandler.getLeaderboard();
                } else {
                    game.gameServiceHandler.signIn();
                }
            }
        });

        btInviteFriend.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        ButtonGroup<Button> radioGroup = new ButtonGroup<Button>();
        radioGroup.add(btLeaderboard, btFacebook, btGoogle, btInviteFriend);

    }

    @Override
    public void draw(float delta) {
        Assets.backgroundNubes.render(0);

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            changeScreenWithFadeOut(GameScreen.class, game);
            return true;
        }
        return super.keyUp(keycode);
    }

    public void updateLeaderboard() {
        contenedor.clear();
        for (Person persona : game.arrPerson) {
            LeaderBoardFrame frame = new LeaderBoardFrame(persona);
            contenedor.add(frame).expandX().fill();
            contenedor.row();
        }
    }

}
