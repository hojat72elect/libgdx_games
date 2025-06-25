

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.gltextures.SmartTexture;
import com.watabou.noosa.NinePatch;

public class ShadowBox extends NinePatch {

    public static final float SIZE = 16;

    public ShadowBox() {
        super(Assets.Interfaces.SHADOW, 1);

        //If this is the first time the texture is generated, set the filtering
        if (texture.id == -1)
            texture.filter(SmartTexture.LINEAR, SmartTexture.LINEAR);

        scale.set(SIZE, SIZE);
    }

    @Override
    public void size(float width, float height) {
        super.size(width / SIZE, height / SIZE);
    }

    public void boxRect(float x, float y, float width, float height) {
        this.x = x - SIZE;
        this.y = y - SIZE;
        size(width + SIZE * 2, height + SIZE * 2);
    }
}
