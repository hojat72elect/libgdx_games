

package com.watabou.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.watabou.input.ControllerHandler;
import com.watabou.noosa.Game;

import java.util.HashMap;

public abstract class PlatformSupport {

    public abstract void updateDisplaySize();

    public abstract void updateSystemUI();

    public abstract boolean connectedToUnmeteredNetwork();

    public abstract boolean supportsVibration();

    public void vibrate(int millis) {
        if (ControllerHandler.isControllerConnected()) {
            ControllerHandler.vibrate(millis);
        } else {
            Gdx.input.vibrate(millis);
        }
    }

    public void setHonorSilentSwitch(boolean value) {
        //does nothing by default
    }

    public boolean openURI(String uri) {
        return Gdx.net.openURI(uri);
    }

    public void setOnscreenKeyboardVisible(boolean value) {
        Gdx.input.setOnscreenKeyboardVisible(value);
    }

    //TODO should consider spinning this into its own class, rather than platform support getting ever bigger
    protected static HashMap<FreeTypeFontGenerator, HashMap<Integer, BitmapFont>> fonts;

    protected int pageSize;
    protected PixmapPacker packer;
    protected boolean systemfont;

    public abstract void setupFontGenerators(int pageSize, boolean systemFont);

    protected abstract FreeTypeFontGenerator getGeneratorForString(String input);

    public abstract String[] splitforTextBlock(String text, boolean multiline);

    public void resetGenerators() {
        resetGenerators(true);
    }

    public void resetGenerators(boolean setupAfter) {
        if (fonts != null) {
            for (FreeTypeFontGenerator generator : fonts.keySet()) {
                for (BitmapFont f : fonts.get(generator).values()) {
                    f.dispose();
                }
                fonts.get(generator).clear();
                generator.dispose();
            }
            fonts.clear();
            if (packer != null) {
                for (PixmapPacker.Page p : packer.getPages()) {
                    p.getTexture().dispose();
                }
                packer.dispose();
            }
            fonts = null;
        }
        if (setupAfter) setupFontGenerators(pageSize, systemfont);
    }

    public void reloadGenerators() {
        if (packer != null) {
            for (FreeTypeFontGenerator generator : fonts.keySet()) {
                for (BitmapFont f : fonts.get(generator).values()) {
                    f.dispose();
                }
                fonts.get(generator).clear();
            }
            if (packer != null) {
                for (PixmapPacker.Page p : packer.getPages()) {
                    p.getTexture().dispose();
                }
                packer.dispose();
            }
            packer = new PixmapPacker(pageSize, pageSize, Pixmap.Format.RGBA8888, 1, false);
        }
    }

    //flipped is needed because Shattered's graphics are y-down, while GDX graphics are y-up.
    //this is very confusing, I know.
    public BitmapFont getFont(int size, String text, boolean flipped, boolean border) {
        FreeTypeFontGenerator generator = getGeneratorForString(text);

        if (generator == null) {
            return null;
        }

        int key = size;
        if (border) key += Short.MAX_VALUE; //surely we'll never have a size above 32k
        if (flipped) key = -key;
        if (!fonts.get(generator).containsKey(key)) {
            FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameters.size = size;
            parameters.flip = flipped;
            if (border) {
                parameters.borderWidth = parameters.size / 10f;
            }
            if (size >= 20) {
                parameters.renderCount = 2;
            } else {
                parameters.renderCount = 3;
            }
            parameters.hinting = FreeTypeFontGenerator.Hinting.None;
            parameters.spaceX = -(int) parameters.borderWidth;
            parameters.incremental = true;
            parameters.characters = "�";
            parameters.packer = packer;

            try {
                BitmapFont font = generator.generateFont(parameters);
                font.getData().missingGlyph = font.getData().getGlyph('�');
                fonts.get(generator).put(key, font);
            } catch (Exception e) {
                Game.reportException(e);
                return null;
            }
        }

        return fonts.get(generator).get(key);
    }
}
