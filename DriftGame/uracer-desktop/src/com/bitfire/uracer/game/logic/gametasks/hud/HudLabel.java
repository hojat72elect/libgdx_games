package com.bitfire.uracer.game.logic.gametasks.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitfire.uracer.game.tween.GameTweener;
import com.bitfire.uracer.resources.BitmapFontFactory;
import com.bitfire.uracer.resources.BitmapFontFactory.FontFace;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Linear;

public final class HudLabel extends Positionable {
    private final Color color = new Color(Color.WHITE);
    public float alpha;
    public TextBounds textBounds = new TextBounds();
    private String text;
    private BitmapFont font;
    private boolean isStatic;

    public HudLabel(FontFace fontFace, String text, boolean isStatic) {
        this.text = text;
        alpha = 1f;
        this.isStatic = isStatic;
        this.font = BitmapFontFactory.get(fontFace);
        scale = 1;
    }

    public boolean isVisible() {
        return (alpha > 0);
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public void setFont(FontFace fontFace) {
        this.font = BitmapFontFactory.get(fontFace);
        updateBounds();
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public void setColor(float r, float g, float b) {
        this.color.set(r, g, b, 0);
    }

    public void setString(String string) {
        setString(string, false);
    }

    public void setString(String string, boolean computeBounds) {
        text = string;
        if (computeBounds) {
            updateBounds();
        }
    }

    private void updateBounds() {
        font.setScale(scale);
        textBounds.set(font.getMultiLineBounds(text));
    }

    @Override
    public float getWidth() {
        updateBounds();
        return textBounds.width;
    }

    @Override
    public float getHeight() {
        updateBounds();
        return textBounds.height;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float value) {
        alpha = value;
    }

    public void render(SpriteBatch batch) {
        if (alpha > 0) {
            font.setUseIntegerPositions(isStatic);
            font.setScale(scale);
            font.setColor(color.r, color.g, color.b, alpha);
            updateBounds();
            font.drawMultiLine(batch, text, position.x - textBounds.width / 2, position.y - textBounds.height / 2);
        }
    }

    public void fadeIn(int milliseconds) {
        GameTweener.stop(this);
        GameTweener.start(Timeline.createSequence().push(
                Tween.to(this, HudLabelAccessor.OPACITY, milliseconds).target(1f).ease(Linear.Companion.getINOUT())));
    }

    public void fadeOut(int milliseconds) {
        GameTweener.stop(this);
        GameTweener.start(Timeline.createSequence().push(
                Tween.to(this, HudLabelAccessor.OPACITY, milliseconds).target(0f).ease(Linear.Companion.getINOUT())));
    }
}
