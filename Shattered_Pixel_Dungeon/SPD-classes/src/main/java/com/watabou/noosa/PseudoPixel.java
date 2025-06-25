

package com.watabou.noosa;

import com.watabou.gltextures.TextureCache;

public class PseudoPixel extends Image {

    public PseudoPixel() {
        super(TextureCache.createSolid(0xFFFFFFFF));
    }

    public PseudoPixel(float x, float y, int color) {

        this();

        this.x = x;
        this.y = y;
        color(color);
    }

    public void size(float w, float h) {
        scale.set(w, h);
    }

    public void size(float value) {
        scale.set(value);
    }
}
