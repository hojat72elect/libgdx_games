package com.salvador.bricks.Objects;

import static com.salvador.bricks.Objects.ResourceManager.getFont;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MenuText extends Actor {

    BitmapFont font;
    float textWidth;
    float x, y;
    GlyphLayout layout;
    private String text;

    public MenuText(String text, String fontName, float x, float y) {

        this.x = x;
        this.y = y;
        this.text = text;

        font = getFont(fontName);
        layout = new GlyphLayout();
        layout.setText(font, text);
        textWidth = layout.width;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        layout.setText(font, text);
        textWidth = layout.width;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font.draw(batch, text, 450F / 2 - textWidth / 2, y);
    }
}
