
package com.gamestudio24.martianrun.actors.menu;

import com.badlogic.gdx.math.Rectangle;
import com.gamestudio24.martianrun.utils.Constants;

public class ShareButton extends GameButton {

    public interface ShareButtonListener {
        void onShare();
    }

    private final ShareButtonListener listener;

    public ShareButton(Rectangle bounds, ShareButtonListener listener) {
        super(bounds);
        this.listener = listener;
    }

    @Override
    protected String getRegionName() {
        return Constants.SHARE_REGION_NAME;
    }

    @Override
    public void touched() {
        listener.onShare();
    }

}
