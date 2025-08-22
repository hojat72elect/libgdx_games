package com.bitfire.uracer.game.logic.gametasks.hud.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitfire.uracer.configuration.Config;
import com.bitfire.uracer.game.logic.gametasks.hud.HudElement;
import com.bitfire.uracer.game.logic.gametasks.hud.HudLabel;
import com.bitfire.uracer.game.logic.replaying.LapManager;
import com.bitfire.uracer.game.tween.GameTweener;
import com.bitfire.uracer.resources.BitmapFontFactory.FontFace;
import com.bitfire.uracer.utils.BoxedFloat;
import com.bitfire.uracer.utils.BoxedFloatAccessor;
import com.bitfire.uracer.utils.ReplayUtils;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Linear;

public class HudLapInfo extends HudElement {

    private final HudLabel curr;
    private final LapManager lapManager;
    private final BoxedFloat r;
    private final BoxedFloat g;
    private final BoxedFloat b;
    private boolean isValid;

    public HudLapInfo(LapManager lapManager) {
        this.lapManager = lapManager;

        curr = new HudLabel(FontFace.LcdWhite, "99.99", true);
        curr.setScale(1.5f);
        curr.setPosition((Config.Graphics.ReferenceScreenWidth / 2F), Config.Graphics.ReferenceScreenHeight - curr.getHeight() / 2
                - 10);

        r = new BoxedFloat(1);
        g = new BoxedFloat(1);
        b = new BoxedFloat(1);
        isValid = true;
    }

    @Override
    public void dispose() {
    }

    public void toDefaultColor() {
        toColor(1, 1, 1);
    }

    public void toColor(float red, float green, float blue) {
        toColor(500, red, green, blue);
    }

    public void toColor(int millisecs, float red, float green, float blue) {
        Timeline seq = Timeline.createParallel();

        GameTweener.stop(r);
        GameTweener.stop(g);
        GameTweener.stop(b);

        //@off
        seq
                .push(Tween.to(r, BoxedFloatAccessor.VALUE, millisecs).target(red).ease(Linear.INOUT))
                .push(Tween.to(g, BoxedFloatAccessor.VALUE, millisecs).target(green).ease(Linear.INOUT))
                .push(Tween.to(b, BoxedFloatAccessor.VALUE, millisecs).target(blue).ease(Linear.INOUT))
        ;
        //@on

        GameTweener.start(seq);
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public void setInvalid(String message) {
        isValid = false;
        curr.setString(message, true);
    }

    @Override
    public void onRender(SpriteBatch batch, float cameraZoom) {
        // current lap time
        if (isValid) {
            String elapsed = String.format("%.03f", ReplayUtils.ticksToSeconds(lapManager.getCurrentReplayTicks()));
            curr.setString(elapsed, true);
        }

        curr.setColor(r.value, g.value, b.value);
        curr.render(batch);
    }

    @Override
    public void onRestart() {
        toDefaultColor();
        setValid(true);
    }
}
