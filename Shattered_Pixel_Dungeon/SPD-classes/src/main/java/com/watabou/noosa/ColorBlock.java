

package com.watabou.noosa;

import com.watabou.gltextures.TextureCache;

public class ColorBlock extends Image implements Resizable {

    public ColorBlock(float width, float height, int color) {
        super(TextureCache.createSolid(color));
        scale.set(width, height);
        origin.set(0, 0);
    }

    @Override
    public void size(float width, float height) {
        scale.set(width, height);
    }

    @Override
    public float width() {
        return scale.x;
    }

    @Override
    public float height() {
        return scale.y;
    }
}
