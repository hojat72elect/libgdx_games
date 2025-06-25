

package com.watabou.gltextures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.watabou.glwrap.Texture;
import com.watabou.noosa.Game;

import java.util.HashMap;

public class TextureCache {

    private static final HashMap<Object, SmartTexture> all = new HashMap<>();

    public synchronized static SmartTexture createSolid(int color) {
        final String key = "1x1:" + color;

        if (all.containsKey(key)) {

            return all.get(key);
        } else {

            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            // convert from Noosa ARGB to libGdx RGBA
            pixmap.setColor((color << 8) | (color >>> 24));
            pixmap.fill();

            SmartTexture tx = new SmartTexture(pixmap);
            all.put(key, tx);

            return tx;
        }
    }

    public synchronized static SmartTexture createGradient(int... colors) {

        final String key = "" + colors;

        if (all.containsKey(key)) {

            return all.get(key);
        } else {

            Pixmap pixmap = new Pixmap(colors.length, 1, Pixmap.Format.RGBA8888);
            for (int i = 0; i < colors.length; i++) {
                // convert from Noosa ARGB to libGdx RGBA
                pixmap.drawPixel(i, 0, (colors[i] << 8) | (colors[i] >>> 24));
            }
            SmartTexture tx = new SmartTexture(pixmap);

            tx.filter(Texture.LINEAR, Texture.LINEAR);
            tx.wrap(Texture.CLAMP, Texture.CLAMP);

            all.put(key, tx);
            return tx;
        }
    }

    //texture is created at given size, but size is not enforced if it already exists
    //texture contents are also not enforced, make sure you know the texture's state!
    public synchronized static SmartTexture create(Object key, int width, int height) {

        if (all.containsKey(key)) {

            return all.get(key);
        } else {

            SmartTexture tx = new SmartTexture(new Pixmap(width, height, Pixmap.Format.RGBA8888));

            tx.filter(Texture.LINEAR, Texture.LINEAR);
            tx.wrap(Texture.CLAMP, Texture.CLAMP);

            all.put(key, tx);

            return tx;
        }
    }

    public synchronized static void remove(Object key) {
        SmartTexture tx = all.get(key);
        if (tx != null) {
            all.remove(key);
            tx.delete();
        }
    }

    public synchronized static SmartTexture get(Object src) {

        if (all.containsKey(src)) {

            return all.get(src);
        } else if (src instanceof SmartTexture) {

            return (SmartTexture) src;
        } else {

            SmartTexture tx = new SmartTexture(getBitmap(src));
            all.put(src, tx);
            return tx;
        }
    }

    public synchronized static void clear() {

        for (Texture txt : all.values()) {
            txt.delete();
        }
        all.clear();
    }

    public synchronized static void reload() {
        for (SmartTexture tx : all.values()) {
            tx.reload();
        }
    }

    public static Pixmap getBitmap(Object src) {

        try {
            if (src instanceof Integer) {

                //libGDX does not support android resource integer handles, and they were
                //never used by the game anyway, should probably remove this entirely
                return null;
            } else if (src instanceof String) {

                return new Pixmap(Gdx.files.internal((String) src));
            } else if (src instanceof Pixmap) {

                return (Pixmap) src;
            } else {

                return null;
            }
        } catch (Exception e) {

            Game.reportException(e);
            return null;
        }
    }

    public synchronized static boolean contains(Object key) {
        return all.containsKey(key);
    }
}
