package com.bitfire.uracer.game.logic.gametasks.hud.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitfire.uracer.configuration.UserProfile;
import com.bitfire.uracer.game.logic.gametasks.hud.HudElement;
import com.bitfire.uracer.game.logic.gametasks.hud.elements.player.BasicInfo;

public class HudPlayerStatic extends HudElement {
    private final BasicInfo basicInfo;

    public HudPlayerStatic(UserProfile userProfile) {
        basicInfo = new BasicInfo(userProfile);
    }

    @Override
    public void dispose() {
        basicInfo.dispose();
    }

    @Override
    public void onRender(SpriteBatch batch, float cameraZoom) {
        if (hasPlayer) {
            basicInfo.render(batch);
        }
    }
}
