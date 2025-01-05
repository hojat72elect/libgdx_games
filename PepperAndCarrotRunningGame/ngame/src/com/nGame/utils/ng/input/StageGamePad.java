package com.nGame.utils.ng.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * Created by oli on 06.03.14.
 */
class StageGamePad extends Stage {

    private ControllerAdapter controllerAdapter;

    private GamePadMapping mapping;


    public StageGamePad(GamePadMapping mapping) {
        super();
        this.mapping = mapping;
        initControllerAdapter();
    }

    public StageGamePad(GamePadMapping mapping, float width, float height) {
        super(new ExtendViewport(width, height));
        this.mapping = mapping;
        initControllerAdapter();
    }


    private void initControllerAdapter() {
        if (Controllers.getControllers().size > 0) {
            controllerAdapter = new ControllerAdapter() {

                @Override
                public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
                    switch (value) {
                        case north:
                            return keyDown(Input.Keys.UP);

                        case east:
                            return keyDown(Input.Keys.RIGHT);

                        case south:
                            return keyDown(Input.Keys.DOWN);

                        case west:
                            return keyDown(Input.Keys.LEFT);

                    }
                    return false;
                }

                @Override
                public boolean buttonDown(Controller controller, int buttonIndex) {
                    return keyDown(mapping.getKeycode(buttonIndex));
                }

                @Override
                public boolean buttonUp(Controller controller, int buttonIndex) {
                    return keyUp(mapping.getKeycode(buttonIndex));
                }
            };
        }
    }
}
