package com.nGame.utils.ng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;
import com.nGame.utils.Utils;
import com.nGame.utils.ng.input.InputResponceEvent;
import com.nGame.utils.ng.input.XboxController;
import com.nGame.utils.scene2d.MenuEntry;
import com.nGame.utils.scene2d.MenuTable;
import com.nGame.utils.scene2d.WindowNG;


/**
 * Created by n0iz on 31.01.2015.
 */
public abstract class MenuScreenNG extends ScreenNG {

    private Table rootTable;
    private MenuTable tableMenu;
    private Table bottomTable;
   // protected ControllerAdapter defaultAdapter;
    private MenuEntry entry;






    int textAlign;

    int menuAlign;

/*        ButtonImage loginGooglePlay;
        ButtonImage googleAchievements;
        ButtonImage googleLeaderboard;

        Label loginGooglePlayLabel;
        Label googleAchievementsLabel;
        Label googleLeaderboardLabel;*/

    /**
     * Creates Default Menu
     * Use Align.left, Align.right, etc. for menuAlign and textAlign
     *
     * @param menuAlign alignment for the menu block
     * @param textAlign alignment for menuentry text
     */
    public MenuScreenNG(int menuAlign, int textAlign){
        super();
        TAG = "Menu";
        this.menuAlign = menuAlign;
        this.textAlign = textAlign;



        Label title = new com.badlogic.gdx.scenes.scene2d.ui.Label("Woodway", Assets.I.titleLabelStyle);


        rootTable = Utils.Ui.createTable(true);
        rootTable.debug();



        rootTable.add(title).center().align(Align.top).expandX();
        rootTable.row();
        tableMenu = new MenuTable(0,textAlign);
        tableMenu.debug();

        rootTable.add(tableMenu).expandX().align(menuAlign);



        stage.addActor(rootTable);





        /*        bottomTable = new Table();
        //bottomTable.debug();
       // bottomTable.columnDefaults(0).pad(15);

        loginGooglePlay = new ButtonImage(new TextureRegionDrawable(Assets.I.atlasGoogle.findRegion("controller"))) {
            @Override
            public void onAction() {
                if(!SameBreaker.I.actionResolver.getSignedInGPGS()){
                    SameBreaker.I.actionResolver.loginGPGS();
                }
            }
        };

        googleAchievements = new ButtonImage(new TextureRegionDrawable(Assets.I.atlasGoogle.findRegion("achievements"))) {
            @Override
            public void onAction() {
                if (SameBreaker.I.actionResolver.getSignedInGPGS()){
                   // stage.addAction(Actions.fadeOut(0));
                    SameBreaker.I.actionResolver.getAchievementsGPGS();
                }
                else SameBreaker.I.actionResolver.loginGPGS();
            }
        };

       googleLeaderboard = new ButtonImage(new TextureRegionDrawable(Assets.I.atlasGoogle.findRegion("leaderboards"))) {
            @Override
            public void onAction() {
                if (SameBreaker.I.actionResolver.getSignedInGPGS()){
                   // stage.addAction(Actions.fadeOut(0));
                    SameBreaker.I.actionResolver.getLeaderboardGPGS();
                }
                else SameBreaker.I.actionResolver.loginGPGS();
            }
        };


        //loginGooglePlay.setScale(.25f);
        //googleAchievements.setScale(.25f);

        bottomTable.setFillParent(false);

        bottomTable.add(loginGooglePlay).uniform();
        bottomTable.add(googleAchievements).uniform();
        bottomTable.add(googleLeaderboard).uniform();
        bottomTable.row();

        loginGooglePlayLabel=Utils.Ui.createLabel("Login",Assets.I.smallLabelStyle);
        googleAchievementsLabel=Utils.Ui.createLabel("Achievements",Assets.I.smallLabelStyle);
        googleLeaderboardLabel=Utils.Ui.createLabel("Leaderboards",Assets.I.smallLabelStyle);

       bottomTable.add(loginGooglePlayLabel);
       bottomTable.add(googleAchievementsLabel);
       bottomTable.add(googleLeaderboardLabel);

        tableMenu.add(bottomTable).height(128).center().expand().align(Align.bottom);*/


    }



    public void addMenuEntry(MenuEntry entry){
        tableMenu.addEntry(entry);
    }

    @Override
    protected void update(float delta) {
      /* if(GameNG.I.GPGS_ActionResolver.getSignedInGPGS()){
            if(!googleAchievements.isVisible()) {
                googleAchievements.setVisible(true);
                googleAchievementsLabel.setVisible(true);
            }
            if(!googleLeaderboard.isVisible()) {
                googleLeaderboard.setVisible(true);
                googleLeaderboardLabel.setVisible(true);
            }
        } else {
            if(googleAchievements.isVisible()) {
                googleAchievements.setVisible(false);
                googleAchievementsLabel.setVisible(false);
            }
            if(googleLeaderboard.isVisible()){
                googleLeaderboard.setVisible(false);
                googleLeaderboardLabel.setVisible(false);
            }

        }*/
    }

    private void setupGamePad() {

        ieManager.addInputResponseEvent(new InputResponceEvent("up") {
            @Override
            public void onAction(float value) {
                prevMenuEntry();
            }
        });


        ieManager.addInputResponseEvent(new InputResponceEvent("down") {
            @Override
            public void onAction(float value) {
                Log.d("down");
                nextMenuEntry();
            }
        });



        ieManager.bindKeyDown(Input.Keys.UP,"up");
        ieManager.bindKeyDown(Input.Keys.DOWN,"down");

        try {
            ieManager.bindGamePadPOV(0,PovDirection.north,"up");
            ieManager.bindGamePadPOV(0,PovDirection.south,"down");
        } catch (Exception e) {
            e.printStackTrace();
        }


        ieManager.addInputResponseEvent(new InputResponceEvent("ok") {
            @Override
            public void onAction(float value) {
                if(entry!=null) {
                    entry.onAction();
                }
            }
        });

        ieManager.bindKeyDown(Input.Keys.ENTER,"ok");
        try {
            ieManager.bindGamePadButton(0,XboxController.A,"ok");
        } catch (Exception e) {
            e.printStackTrace();
        }



    }



    private void prevMenuEntry() {
        SnapshotArray<Actor> menuEntries=tableMenu.getChildren();
        if(entry==null){
            if(menuEntries.size>0) {
                entry=((MenuEntry)menuEntries.get(0));
                entry.select();

            }
        } else {
            for (int i = menuEntries.size-1; i>=0;i--){
                if(menuEntries.get(i)==entry){
                    entry.deselect();
                    if(i>0){
                        entry=((MenuEntry)menuEntries.get(i-1));
                    } else {
                        entry=((MenuEntry)menuEntries.get(menuEntries.size-1));
                    }
                    entry.select();
                    break;
                }
            }
        }
    }

    private void nextMenuEntry() {
        SnapshotArray<Actor> menuEntries=tableMenu.getChildren();
        if(entry==null){
            if(menuEntries.size>0) {
                entry=((MenuEntry)menuEntries.get(0));
                entry.select();

            }
        } else {
            for (int i = 0; i<menuEntries.size;i++){
                if(menuEntries.get(i)==entry){
                    entry.deselect();
                    Log.d("deselect[" + i + "]: " + entry);
                    if(i<menuEntries.size-1){
                        entry=((MenuEntry)menuEntries.get(i+1));
                    } else {
                        entry=((MenuEntry)menuEntries.get(0));
                    }
                    Log.d("select[" + i + "]: " + entry);
                    entry.select();
                    break;
                }
            }
        }
    }

    @Override
    public void show() {
        super.show();
        if(!GameNG.I.ongoingTransition) {
            WindowNG win = new WindowNG("Title", Assets.I.skin);
            win.pack();
            stage.addActor(win);
            setupGamePad();
        }
    }

    @Override
    public void hide() {
        super.hide();
        ieManager.removeEvent("ok");
        ieManager.removeEvent("up");
        ieManager.removeEvent("down");
    }


}
