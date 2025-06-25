

package com.watabou.noosa;

import com.watabou.input.GameAction;
import com.watabou.input.KeyBindings;
import com.watabou.input.KeyEvent;
import com.watabou.utils.Signal;

public class Scene extends Group {

    private Signal.Listener<KeyEvent> keyListener;

    public void create() {
        KeyEvent.addKeyListener(keyListener = new Signal.Listener<KeyEvent>() {
            @Override
            public boolean onSignal(KeyEvent event) {
                if (Game.instance != null && event.pressed) {
                    if (KeyBindings.getActionForKey(event) == GameAction.BACK) {
                        onBackPressed();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void destroy() {
        KeyEvent.removeKeyListener(keyListener);
        super.destroy();
    }

    public void onPause() {

    }

    public void onResume() {

    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public Camera camera() {
        return Camera.main;
    }

    protected void onBackPressed() {
        Game.instance.finish();
    }
}
