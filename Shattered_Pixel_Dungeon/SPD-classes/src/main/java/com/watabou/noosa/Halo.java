

package com.watabou.noosa;

import com.badlogic.gdx.graphics.Pixmap;
import com.watabou.gltextures.TextureCache;

public class Halo extends Image {

    private static final Object CACHE_KEY = Halo.class;

    protected static final int RADIUS = 128;

    protected float radius = RADIUS;
    protected float brightness = 1;

    public Halo() {
        super();

        if (!TextureCache.contains(CACHE_KEY)) {

            Pixmap pixmap = TextureCache.create(CACHE_KEY, 2 * RADIUS + 1, 2 * RADIUS + 1).bitmap;

            pixmap.setColor(0x00000000);
            pixmap.fill();

            pixmap.setColor(0xFFFFFF08);
            for (int i = 0; i < RADIUS; i += 2) {
                pixmap.fillCircle(RADIUS, RADIUS, (RADIUS - i));
            }
        }

        texture(CACHE_KEY);
    }

    public Halo(float radius, int color, float brightness) {

        this();

        hardlight(color);
        alpha(this.brightness = brightness);
        radius(radius);
    }

    public Halo point(float x, float y) {
        this.x = x - (width() / 2f);
        this.y = y - (height() / 2f);
        return this;
    }

    @Override
    public void alpha(float value) {
        brightness = value;
        super.alpha(value);
    }

    public void radius(float value) {
        scale.set((this.radius = value) / RADIUS);
    }
}
