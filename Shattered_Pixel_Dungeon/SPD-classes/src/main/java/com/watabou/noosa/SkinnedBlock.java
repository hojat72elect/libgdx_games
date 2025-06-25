

package com.watabou.noosa;

import com.watabou.glwrap.Texture;
import com.watabou.utils.RectF;

public class SkinnedBlock extends Image {

    protected float scaleX;
    protected float scaleY;

    protected float offsetX;
    protected float offsetY;

    public boolean autoAdjust = false;

    public SkinnedBlock(float width, float height, Object tx) {
        super(tx);

        texture.wrap(Texture.REPEAT, Texture.REPEAT);

        size(width, height);
    }

    @Override
    public void frame(RectF frame) {
        scaleX = 1;
        scaleY = 1;

        offsetX = 0;
        offsetY = 0;

        super.frame(new RectF(0, 0, 1, 1));
    }

    @Override
    protected void updateFrame() {

        if (autoAdjust) {
            while (offsetX > texture.width) {
                offsetX -= texture.width;
            }
            while (offsetX < -texture.width) {
                offsetX += texture.width;
            }
            while (offsetY > texture.height) {
                offsetY -= texture.height;
            }
            while (offsetY < -texture.height) {
                offsetY += texture.height;
            }
        }

        float tw = 1f / texture.width;
        float th = 1f / texture.height;

        float u0 = offsetX * tw;
        float v0 = offsetY * th;
        float u1 = u0 + width * tw / scaleX;
        float v1 = v0 + height * th / scaleY;

        vertices[2] = u0;
        vertices[3] = v0;

        vertices[6] = u1;
        vertices[7] = v0;

        vertices[10] = u1;
        vertices[11] = v1;

        vertices[14] = u0;
        vertices[15] = v1;

        dirty = true;
    }

    public void offsetTo(float x, float y) {
        offsetX = x;
        offsetY = y;
        updateFrame();
    }

    public void offset(float x, float y) {
        offsetX += x;
        offsetY += y;
        updateFrame();
    }

    public float offsetX() {
        return offsetX;
    }

    public float offsetY() {
        return offsetY;
    }

    public void scale(float x, float y) {
        scaleX = x;
        scaleY = y;
        updateFrame();
    }

    public void size(float w, float h) {
        this.width = w;
        this.height = h;
        updateFrame();
        updateVertices();
    }
}
