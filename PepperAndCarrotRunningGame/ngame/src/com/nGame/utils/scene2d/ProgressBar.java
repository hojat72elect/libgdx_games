package com.nGame.utils.scene2d;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created with IntelliJ IDEA.
 *
 * @author oli
 *         Date: 05.11.13 - 22:13
 */
public class ProgressBar extends Group {

    private Image bg;
    private Image bar;
    private Image bar_overlay;
    private Image overlay_shadow;
    private Image overlay;
    private float percent;
    private float bar_width;

    /**
     * @param atlas
     */
    public ProgressBar(TextureAtlas atlas) {
        bar = new Image(atlas.findRegion("progress_bar2"));

        bar_width = bar.getWidth();
        bar.setPosition(17, 12);
        addActor(bar);

        bg = new Image(atlas.findRegion("progress_bg"));
        bg.setPosition(17, 12);
        addActor(bg);


        bar_overlay = new Image(atlas.findRegion("progress_bar_overlay"));
        addActor(bar_overlay);
        overlay_shadow = new Image(atlas.findRegion("progress_overlay_shadow"));
        addActor(overlay_shadow);
        overlay = new Image(atlas.findRegion("progress_overlay"));
        addActor(overlay);

    setHeight(100);
        setProgress(0f);
        setWidth(overlay.getWidth());
    }

    public void setProgress(float progress) {
        //calc progress
        //Gdx.app.log("Loading", "progress: " + progress);
        percent = Interpolation.linear.apply(percent, progress, 0.1f);


        bar_overlay.setPosition(bar.getX() + (bar_width * percent) - bar_overlay.getWidth(), bar.getY());
        bg.setPosition(bar_overlay.getX() + bar_overlay.getWidth(), bar_overlay.getY());
        bg.setSize(bar_width - (bar_width * percent), bar_overlay.getHeight());
        // bar.invalidate();
        bar_overlay.invalidate();
    }

}
