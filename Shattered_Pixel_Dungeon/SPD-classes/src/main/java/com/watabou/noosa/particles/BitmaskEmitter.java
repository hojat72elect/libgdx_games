

package com.watabou.noosa.particles;

import com.watabou.gltextures.SmartTexture;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;
import com.watabou.utils.RectF;

public class BitmaskEmitter extends Emitter {

    // DON'T USE WITH COMPLETELY TRANSPARENT IMAGES!!!

    private final SmartTexture map;
    private final int mapW;
    private final int mapH;

    public BitmaskEmitter(Image target) {
        super();

        this.target = target;

        map = target.texture;
        mapW = map.bitmap.getWidth();
        mapH = map.bitmap.getHeight();
    }

    @Override
    protected void emit(int index) {

        RectF frame = ((Image) target).frame();
        float ofsX = frame.left * mapW;
        float ofsY = frame.top * mapH;

        float x, y;
        do {
            x = Random.Float(frame.width()) * mapW;
            y = Random.Float(frame.height()) * mapH;
        } while ((map.bitmap.getPixel((int) (x + ofsX), (int) (y + ofsY)) & 0x000000FF) == 0);

        factory.emit(this, index,
                target.x + x * target.scale.x,
                target.y + y * target.scale.y);
    }
}