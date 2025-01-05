package com.nGame.utils.ng;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nGame.utils.ng.input.GamePads;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by oli on 29.09.2014.
 */
public abstract class GameNG implements ApplicationListener {
    public static GameNG I;
    public boolean ongoingTransition = false;
    private ScreenNG screen;
    private ScreenNG nextScreen;

    private static LoadingScreen loadingScreen;
    public static MenuScreenNG menuScreen;
    public static PreferendeScreenNG preferenceScreen;
    public static ScreenNG gameScreen;
    public static ScreenNG creditScreen;
    public static ScreenNG preGameScreen;

    private Stack<ScreenNG> previousScreens=new Stack<ScreenNG>();

    private HashMap<String,ScreenNG> screenMap = new HashMap<String, ScreenNG>();

    public static GamePads gp;
    public GameNG(){
        I=this;

    }

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Log.d("static log create");
        Conf.loadConf();
        gp = new GamePads();
        loadingScreen=new LoadingScreen();
        setScreen(loadingScreen);



    }

    public  void doneLoading() {
        Assets.I.updateMaps();
    }

    public enum Transition {
        MOVE_UP,
        MOVE_DOWN,
        MOVE_LEFT,
        MOVE_RIGHT,
        FADE
    }

    ScreenNG popPreviousScreen(){
        try {
            return previousScreens.pop();
        } catch (EmptyStackException e){
            return null;
        }

    }

    public void pushPreviousScreen(ScreenNG previousScreen){
        previousScreens.push(previousScreen);
    }



    public void shutdown(String because) {
        //TODO save stuff
        Conf.saveConf();

        Log.i("Shutdown: " + because);
        Gdx.app.exit();
    }
    @Override
    public void pause() {
        if (screen != null) screen.pause();
    }

    @Override
    public void resume() {

        if (screen != null) {
            loadingScreen.setNextScreen(screen);
            setScreen(loadingScreen);
        }
    }

    @Override
    public void render() {

        if (screen != null) screen.render(Gdx.graphics.getDeltaTime());
        if (ongoingTransition && nextScreen != null) nextScreen.render(Gdx.graphics.getDeltaTime());


    }

    @Override
    public void resize(int width, int height) {
        if (screen != null) screen.resize(width, height);
    }

    protected void addScreen(String key, ScreenNG screen){
        if(screenMap.containsKey(key)){
            Log.d("deleting existing key: " + key);
            screenMap.remove(key);
        }
        Log.d("add screen: : " + key);
        screenMap.put(key,screen);
    }

    public void switchToPreviousScreen(){
        ScreenNG back = popPreviousScreen();
        if(back!=null){
            switchToScreen(back, Conf.Visual.SCREENTRANSITIONBACK);
        }
    }

    public void switchToScreen(ScreenNG screen) {
        switchToScreen(screen, Conf.Visual.SCREENTRANSITION);
    }

    public void switchToScreen(String screen) {
        switchToScreen(screenMap.get(screen), Conf.Visual.SCREENTRANSITION);
    }


    void switchToScreen(ScreenNG screen, Transition transition) {
        if(screen==this.screen){
            return;
        }
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        ongoingTransition = true;
        Gdx.input.setInputProcessor(null);
        this.nextScreen = screen;
        switch (transition) {
            case MOVE_UP:
                this.screen.stage.addAction(Actions.moveTo(0, height, Conf.Visual.SCREENTRANSITIONTIME, Interpolation.exp10));
                this.nextScreen.stage.addAction(Actions.sequence(Actions.moveTo(0, -height),
                        Actions.moveTo(0, 0, Conf.Visual.SCREENTRANSITIONTIME, Interpolation.exp10),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                ongoingTransition = false;
                                setScreen(nextScreen);
                            }
                        })
                ));
                break;
            case MOVE_DOWN:
                this.screen.stage.addAction(Actions.moveTo(0, -height, Conf.Visual.SCREENTRANSITIONTIME, Interpolation.exp10));
                this.nextScreen.stage.addAction(Actions.sequence(Actions.moveTo(0, height),
                        Actions.moveTo(0, 0, Conf.Visual.SCREENTRANSITIONTIME, Interpolation.exp10),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                ongoingTransition = false;
                                setScreen(nextScreen);
                            }
                        })
                ));
                break;

            case MOVE_LEFT:
                this.screen.stage.addAction(Actions.moveTo(-width, 0, Conf.Visual.SCREENTRANSITIONTIME, Interpolation.exp10));
                this.nextScreen.stage.addAction(Actions.sequence(Actions.moveTo(width, 0),
                        Actions.moveTo(0, 0, Conf.Visual.SCREENTRANSITIONTIME, Interpolation.exp10),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                ongoingTransition = false;
                                setScreen(nextScreen);
                            }
                        })
                ));
                break;
            case MOVE_RIGHT:
                this.screen.stage.addAction(Actions.moveTo(width, 0, Conf.Visual.SCREENTRANSITIONTIME, Interpolation.exp10));
                this.nextScreen.stage.addAction(Actions.sequence(Actions.moveTo(-width, 0),
                        Actions.moveTo(0, 0, Conf.Visual.SCREENTRANSITIONTIME, Interpolation.exp10),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                ongoingTransition = false;
                                setScreen(nextScreen);
                            }
                        })
                ));
                break;
            case FADE:
                this.screen.stage.addAction(Actions.fadeOut(Conf.Visual.SCREENTRANSITIONTIME, Interpolation.exp10));
                this.nextScreen.stage.addAction(Actions.sequence(Actions.fadeOut(0),
                        Actions.fadeIn(Conf.Visual.SCREENTRANSITIONTIME, Interpolation.exp10),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                ongoingTransition = false;
                                setScreen(nextScreen);
                            }
                        })
                ));
                break;
        }

    }

    /**
     * @return the currently active {@link ScreenNG}.
     */
    public ScreenNG getScreen() {
        return screen;
    }

    /**
     * Sets the current screen. {@link ScreenNG#hide()} is called on any old screen, and {@link ScreenNG#show()} is called on the new
     * screen, if any.
     *
     * @param screenNext may be {@code null}
     */
    void setScreen(ScreenNG screenNext) {


        if (screen != null) {
            screen.hide();
        }

        screen = screenNext;
        if (screen != null) {
            screen.show();
            screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    public abstract void loadScreens();
}
