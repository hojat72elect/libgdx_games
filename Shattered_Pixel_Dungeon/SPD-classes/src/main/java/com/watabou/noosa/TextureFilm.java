

package com.watabou.noosa;

import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.utils.RectF;

import java.util.HashMap;

public class TextureFilm {

    private static final RectF FULL = new RectF(0, 0, 1, 1);

    private final int texWidth;
    private final int texHeight;

    protected HashMap<Object, RectF> frames = new HashMap<>();

    public TextureFilm(Object tx) {

        SmartTexture texture = TextureCache.get(tx);

        texWidth = texture.width;
        texHeight = texture.height;

        add(null, FULL);
    }

    public TextureFilm(SmartTexture texture, int width) {
        this(texture, width, texture.height);
    }

    public TextureFilm(Object tx, int width, int height) {

        SmartTexture texture = TextureCache.get(tx);

        texWidth = texture.width;
        texHeight = texture.height;

        float uw = (float) width / texWidth;
        float vh = (float) height / texHeight;
        int cols = texWidth / width;
        int rows = texHeight / height;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                RectF rect = new RectF(j * uw, i * vh, (j + 1) * uw, (i + 1) * vh);
                add(i * cols + j, rect);
            }
        }
    }

    public TextureFilm(TextureFilm atlas, Object key, int width, int height) {

        texWidth = atlas.texWidth;
        texHeight = atlas.texHeight;

        RectF patch = atlas.get(key);

        float uw = (float) width / texWidth;
        float vh = (float) height / texHeight;
        int cols = (int) (width(patch) / width);
        int rows = (int) (height(patch) / height);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                RectF rect = new RectF(j * uw, i * vh, (j + 1) * uw, (i + 1) * vh);
                rect.shift(patch.left, patch.top);
                add(i * cols + j, rect);
            }
        }
    }

    public void add(Object id, RectF rect) {
        frames.put(id, rect);
    }

    public void add(Object id, float left, float top, float right, float bottom) {
        frames.put(id,
                new RectF(
                        left / texWidth,
                        top / texHeight,
                        right / texWidth,
                        bottom / texHeight));
    }

    public RectF get(Object id) {
        return frames.get(id);
    }

    public float width(Object id) {
        return width(get(id));
    }

    public float width(RectF frame) {
        return frame.width() * texWidth;
    }

    public float height(Object id) {
        return height(get(id));
    }

    public float height(RectF frame) {
        return frame.height() * texHeight;
    }
}