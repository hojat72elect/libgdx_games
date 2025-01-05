package com.nGame.utils.ng;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.nGame.utils.Utils;
import com.nGame.utils.ng.input.InputResponceEvent;
import com.nGame.utils.ng.input.XboxController;
import com.nGame.utils.scene2d.ProgressBar;

/**
 * Created with IntelliJ IDEA.
 *
 * @author oli
 *         Date: 02.11.13 - 03:21
 */
public class LoadingScreen extends ScreenNG {

    private static String pack = "data/loading.atlas";
    private TextureAtlas atlas;
    private ProgressBar progressBar;
    private Image floppy;
    private boolean hasToReload;
    private ScreenNG followingScreen;
    private State state;
    private Label pressLable;
    private boolean isRealoading;


    enum State {
        LOADING,
        WAITFORINPUT,
        RELOAD
    }

    public LoadingScreen() {
        super();
        TAG = "Loading";

        Assets.I.manager.load(pack, TextureAtlas.class);
        Assets.I.initFonts();
        Table table = Utils.Ui.createTable(true);

        //table.debug();


        table.pad(10);
        stage.addActor(table);

        //logo = new Image(Assets.I.atlasGame.findRegion("logo"));
        //logo.setAlign(Align.center);
        //stage.addActor(logo);
        // table.add(logo).expand().center();
        // table.row();




        // Wait until they are finished loading
        Assets.I.manager.finishLoading();
        Assets.I.setupStyles();
        Label title = new Label("nGames", Assets.I.titleLabelStyle);
        //title.setFontScale(2.5f);

        table.add(title).center();
        table.row();


        Label copyright = Utils.Ui.createLabel("01.2015",Assets.I.defaultLabelStyle);
        //copyright.setFontScale(1);
        table.add(copyright).padBottom(50);
        table.row();
        atlas = Assets.I.manager.get(pack, TextureAtlas.class);

        floppy = new Image(atlas.findRegion("floppy"));


        table.add(floppy).bottom().right().padRight(20);
        table.row();
        //floppy.setPosition(Gdx.graphics.getWidth() / 2f - floppy.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - floppy.getHeight() / 2f);

        progressBar = new ProgressBar(atlas);
        //progressBar.setPosition(Gdx.graphics.getWidth() / 2f - progressBar.getWidth() / 2f, Gdx.graphics.getHeight() / 3f);
        //Gdx.app.log("DBG", "w/h: " + progressBar.getWidth() + "/" + height);
        table.add(progressBar);
        stage.addActor(table);


        table.row();
        pressLable = Utils.Ui.createLabel("START");
        //pressLable.setFontScale(1);
        pressLable.setVisible(false);

        table.add(pressLable);

        setState(State.LOADING);
    }

    /**
     * Called when this screen becomes the current screen for first {@link com.badlogic.gdx.Game}.
     */
    @Override
    public void show() {
        super.show();
        floppy.addAction(Actions.sequence(
                Actions.fadeOut(0),
                Actions.fadeIn(1f, Interpolation.bounceIn)));

        resetScreen();
        progressBar.setProgress(0);
        //stage.addAction(Actions.moveTo(480,0));


        if(hasToReload) {
            isRealoading=true;
            hasToReload = false;
            //Assets.I.manager.clear();

        }

        if(isRealoading){

            Assets.I.manager.load(pack, TextureAtlas.class);
            // Wait until they are finished loading
            Assets.I.manager.finishLoading();
            atlas = Assets.I.manager.get(pack, TextureAtlas.class);
        }



        Log.d("start loading");
        setState(State.LOADING);


    }

    /**
     * Called when this screen is no longer the current screen for first {@link com.badlogic.gdx.Game}.
     */
    @Override
    public void hide() {
        hasToReload = true;
        //Assets.I.manager.unload(pack);
        super.hide();
    }


    private void setState(State state){
        this.state=state;

        switch (state){
            case LOADING:
                Assets.I.loadSkin();
                progressBar.setVisible(true);
                floppy.setVisible(true);
                ////////////////////////////////////////////
                //load your assets into manager



               // Assets.I.manager.load("textures/googlegames.atlas", TextureAtlas.class);


                break;
            case WAITFORINPUT:
                ieManager.addInputResponseEvent(new InputResponceEvent("start") {
                    @Override
                    public void onAction(float value) {
                        GameNG.I.switchToScreen(GameNG.menuScreen, GameNG.Transition.FADE);
                    }
                });
                try {
                    ieManager.bindGamePadButton(0,XboxController.A,"start");
                    ieManager.bindGamePadButton(0,XboxController.B,"start");
                    ieManager.bindGamePadButton(0,XboxController.X,"start");
                    ieManager.bindGamePadButton(0,XboxController.Y,"start");
                    ieManager.bindGamePadButton(0,XboxController.START,"start");
                    ieManager.bindGamePadButton(0,XboxController.BACK,"start");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                GameNG.I.loadScreens();
                pressLable.setVisible(true);
                break;
            case RELOAD:
                break;
        }

    }

    @Override
    public void update(float delta) {




        switch (state){
            case LOADING:

                if (Assets.I.manager.update()) {
                    floppy.addAction(Actions.fadeOut(1f, Interpolation.bounceOut));
                    Log.d("done loading");

                    GameNG.I.doneLoading();
                    //Assets.I.atlasGame = Assets.I.manager.get("data/game.atlas", TextureAtlas.class);
                    //Assets.I.atlasGoogle = Assets.I.manager.get("textures/googlegames.atlas", TextureAtlas.class);
                    if(isRealoading&&followingScreen!=this){
                        setState(State.RELOAD);
                        isRealoading=false;
                    } else {
                        setState(State.WAITFORINPUT);
                    }
                } /*else {
                    Utils.debug("load");
                }*/
                progressBar.setProgress(Assets.I.manager.getProgress());
                break;
            case WAITFORINPUT:
                progressBar.setProgress(Assets.I.manager.getProgress());


                if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
                    GameNG.I.switchToScreen(GameNG.menuScreen, GameNG.Transition.FADE);
                }
                break;
            case RELOAD:
                progressBar.setProgress(Assets.I.manager.getProgress());
                floppy.addAction(Actions.fadeIn(0f));

                //if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
                GameNG.I.switchToScreen(followingScreen, GameNG.Transition.FADE);
                //}

                break;
        }





    }



    public void setNextScreen(ScreenNG screen) {
        this.followingScreen = screen;
    }

}
