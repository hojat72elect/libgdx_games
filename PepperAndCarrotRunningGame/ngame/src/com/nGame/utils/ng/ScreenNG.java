package com.nGame.utils.ng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.nGame.utils.ng.input.InputEventManager;
import com.nGame.utils.ng.input.InputResponceEvent;
import com.nGame.utils.ng.input.XboxController;
import com.nGame.utils.scene2d.ControllerDialog;

/**
 * Created by oli on 17.04.2014.
 */
public abstract class ScreenNG implements Screen {
    public final InputEventManager ieManager;
    protected String TAG = ScreenNG.class.getSimpleName();
    public Stage stage;
    ConfirmationDialog exitDialog;

    protected ScreenNG() {
        //stage=new Stage();
        stage = new Stage(new ExtendViewport(Conf.Video.WIDTH, Conf.Video.HEIGHT));

        ieManager = new InputEventManager();


        ieManager.addInputResponseEvent(new InputResponceEvent("back") {
            @Override
            public void onAction(float value) {
                GameNG.I.switchToPreviousScreen();
            }
        });

        try {
            ieManager.bindGamePadButton(0,XboxController.BACK,"back");
            ieManager.bindKeyDown(Input.Keys.ESCAPE, "back");
        } catch (Exception e) {
            e.printStackTrace();
        }


        exitDialog = new ConfirmationDialog("Exit Game",Assets.I.skin){
            @Override
            protected void response(boolean result) {
                if(result){
                    GameNG.I.shutdown("Bye bye!");
                }
            }

            {
                text("You are leaving?");
            }
        };
    }


    protected abstract void update(float delta);


    @Override
    public void render(float delta) {


        // Set the viewport to the whole screen.
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw anywhere on the screen.


        update(delta);
        stage.act(delta);
        stage.draw();
        //Table.drawDebug(stage);
        // Restore the stage's viewport.
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {

        if(!GameNG.I.ongoingTransition) {
            Log.d("show activate input");
            stage.addListener(ieManager);
            Gdx.input.setInputProcessor(stage);
            ieManager.activateGamePad();
        } else {
            Log.d("input not activated");
        }
    }

    @Override
    public void hide() {
        Log.d("hide");
        ieManager.deactivateGamePad();
        stage.removeListener(ieManager);
        Gdx.input.setInputProcessor(null);

    }

    @Override
    public void pause() {
        Gdx.app.debug(TAG, "pause");
    }

    @Override
    public void resume() {
        Gdx.app.debug(TAG, "resume");
    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "close game");
        stage.dispose();
    }

    void resetScreen(){

        stage.addAction(Actions.parallel(Actions.fadeIn(0), Actions.moveTo(0, 0), Actions.color(Color.WHITE)));
    }


    protected abstract class ConfirmationDialog extends Dialog{

        public ConfirmationDialog(String title, Skin skin) {
            super(title, skin);
        }

        public ConfirmationDialog(String title, Skin skin, String windowStyleName) {
            super(title, skin, windowStyleName);
        }

        public ConfirmationDialog(String title, WindowStyle windowStyle) {
            super(title, windowStyle);
        }
        {
            button("Cancel",false);
            button("Ok",true);
        }

        @Override
        public Dialog show(Stage stage) {
            //register controller
            return super.show(stage);
        }

        @Override
        public boolean remove() {
            //deregister controller
            return super.remove();
        }

        @Override
        protected void result(Object object) {
            boolean result = (Boolean)object;
            response(result);
            super.result(object);
        }

        protected abstract void response(boolean result);
    }



    protected void exitGame() {
        exitDialog.show(stage);
    }

}
