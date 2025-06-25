

package com.watabou.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.watabou.noosa.Game;
import com.watabou.utils.PointF;

public class InputHandler extends InputAdapter {

    private final InputMultiplexer multiplexer;

    public InputHandler(Input input) {
        //An input multiplexer, with additional coord tweaks for power saver mode
        multiplexer = new InputMultiplexer() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                screenX /= (Game.dispWidth / (float) Game.width);
                screenY /= (Game.dispHeight / (float) Game.height);
                return super.touchDown(screenX, screenY, pointer, button);
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                screenX /= (Game.dispWidth / (float) Game.width);
                screenY /= (Game.dispHeight / (float) Game.height);
                return super.touchDragged(screenX, screenY, pointer);
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                screenX /= (Game.dispWidth / (float) Game.width);
                screenY /= (Game.dispHeight / (float) Game.height);
                return super.touchUp(screenX, screenY, pointer, button);
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                screenX /= (Game.dispWidth / (float) Game.width);
                screenY /= (Game.dispHeight / (float) Game.height);
                return super.mouseMoved(screenX, screenY);
            }
        };
        input.setInputProcessor(multiplexer);
        addInputProcessor(this);
        input.setCatchKey(Input.Keys.BACK, true);
        input.setCatchKey(Input.Keys.MENU, true);
    }

    public void addInputProcessor(InputProcessor processor) {
        multiplexer.addProcessor(0, processor);
    }

    public void removeInputProcessor(InputProcessor processor) {
        multiplexer.removeProcessor(processor);
    }

    public void emulateTouch(int id, int button, boolean down) {
        PointF hoverPos = PointerEvent.currentHoverPos();
        if (down) {
            multiplexer.touchDown((int) hoverPos.x, (int) hoverPos.y, id, button);
        } else {
            multiplexer.touchUp((int) hoverPos.x, (int) hoverPos.y, id, button);
        }
    }

    public void emulateDrag(int id) {
        PointF hoverPos = PointerEvent.currentHoverPos();
        multiplexer.touchDragged((int) hoverPos.x, (int) hoverPos.y, id);
    }

    public void processAllEvents() {
        PointerEvent.processPointerEvents();
        KeyEvent.processKeyEvents();
        ScrollEvent.processScrollEvents();
    }

    // *********************
    // *** Pointer Input ***
    // *********************

    @Override
    public synchronized boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screenX < 0 || screenX > Game.width || screenY < 0 || screenY > Game.height) {
            return true;
        }

        if (pointer != ControllerHandler.CONTROLLER_POINTER_ID) {
            ControllerHandler.setControllerPointer(false);
            ControllerHandler.controllerActive = false;
        }

        if (button >= 3 && KeyBindings.isKeyBound(button + 1000)) {
            KeyEvent.addKeyEvent(new KeyEvent(button + 1000, true));
        } else if (button < 3) {
            PointerEvent.addPointerEvent(new PointerEvent(screenX, screenY, pointer, PointerEvent.Type.DOWN, button));
        }
        return true;
    }

    @Override
    public synchronized boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (button >= 3 && KeyBindings.isKeyBound(button + 1000)) {
            KeyEvent.addKeyEvent(new KeyEvent(button + 1000, false));
        } else if (button < 3) {
            PointerEvent.addPointerEvent(new PointerEvent(screenX, screenY, pointer, PointerEvent.Type.UP, button));
        }
        return true;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {

        if (button >= 3 && KeyBindings.isKeyBound(button + 1000)) {
            KeyEvent.addKeyEvent(new KeyEvent(button + 1000, false));
        } else if (button < 3) {
            PointerEvent.addPointerEvent(new PointerEvent(screenX, screenY, pointer, PointerEvent.Type.CANCEL, button));
        }
        return true;
    }

    @Override
    public synchronized boolean touchDragged(int screenX, int screenY, int pointer) {
        PointerEvent.addIfExisting(new PointerEvent(screenX, screenY, pointer, PointerEvent.Type.DOWN));
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (ControllerHandler.controllerPointerActive()) {
            ControllerHandler.setControllerPointer(false);
            PointF hover = ControllerHandler.getControllerPointerPos();
            screenX = (int) hover.x;
            screenY = (int) hover.y;
        }
        PointerEvent.addPointerEvent(new PointerEvent(screenX, screenY, -1, PointerEvent.Type.HOVER));
        return true;
    }

    // *****************
    // *** Key Input ***
    // *****************

    @Override
    public synchronized boolean keyDown(int keyCode) {
        if (KeyBindings.isKeyBound(keyCode)) {
            KeyEvent.addKeyEvent(new KeyEvent(keyCode, true));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized boolean keyUp(int keyCode) {
        if (KeyBindings.isKeyBound(keyCode)) {
            KeyEvent.addKeyEvent(new KeyEvent(keyCode, false));
            return true;
        } else {
            return false;
        }
    }

    // ********************
    // *** Scroll Input ***
    // ********************

    @Override
    public boolean scrolled(float amountX, float amountY) {
        ScrollEvent.addScrollEvent(new ScrollEvent(PointerEvent.currentHoverPos(), amountY));
        return true;
    }
}
