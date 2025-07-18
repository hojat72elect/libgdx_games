

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.Visual;
import com.watabou.utils.PointF;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class CircleArc extends Visual {

    private float duration = 0;
    private float lifespan;

    //1f is an entire 360 degree sweep
    private float sweep;
    private boolean dirty;

    private boolean lightMode = true;

    private final SmartTexture texture;

    protected float[] vertices;
    private final FloatBuffer verticesBuffer;
    private final ShortBuffer indices;

    private final int nTris;
    private final float rad;

    //more triangles means a more precise visual
    public CircleArc(int triangles, float radius) {

        super(0, 0, 0, 0);

        texture = TextureCache.createSolid(0xFFFFFFFF);

        this.nTris = triangles;
        this.rad = radius;

        vertices = new float[4];
        verticesBuffer = ByteBuffer.
                allocateDirect((nTris * 2 + 1) * 4 * (Float.SIZE / 8)).
                order(ByteOrder.nativeOrder()).
                asFloatBuffer();

        indices = ByteBuffer.
                allocateDirect(nTris * 3 * Short.SIZE / 8).
                order(ByteOrder.nativeOrder()).
                asShortBuffer();

        sweep = 1f;
        updateTriangles();
    }

    public CircleArc color(int color, boolean lightMode) {
        this.lightMode = lightMode;
        hardlight(color);

        return this;
    }

    public CircleArc show(Visual visual, float duration) {
        point(visual.center());
        visual.parent.addToBack(this);

        lifespan = this.duration = duration;

        return this;
    }

    public CircleArc show(Group parent, PointF pos, float duration) {
        point(pos);
        parent.add(this);

        lifespan = this.duration = duration;

        return this;
    }

    public void setSweep(float sweep) {
        if (sweep != this.sweep) {
            this.sweep = sweep;
            dirty = true;
        }
    }

    public float getSweep() {
        return sweep;
    }

    private void updateTriangles() {

        dirty = false;

        ((Buffer) indices).position(0);
        ((Buffer) verticesBuffer).position(0);

        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0.25f;
        vertices[3] = 0;
        verticesBuffer.put(vertices);

        vertices[2] = 0.75f;
        vertices[3] = 0;

        //starting position is very top by default, use angle to adjust this.
        double start = 2 * (Math.PI - Math.PI * sweep) - Math.PI / 2.0;

        for (int i = 0; i < nTris; i++) {

            double a = start + i * Math.PI * 2 / nTris * sweep;
            vertices[0] = (float) Math.cos(a) * rad;
            vertices[1] = (float) Math.sin(a) * rad;
            verticesBuffer.put(vertices);

            a += 3.1415926f * 2 / nTris * sweep;
            vertices[0] = (float) Math.cos(a) * rad;
            vertices[1] = (float) Math.sin(a) * rad;
            verticesBuffer.put(vertices);

            indices.put((short) 0);
            indices.put((short) (1 + i * 2));
            indices.put((short) (2 + i * 2));
        }

        ((Buffer) indices).position(0);
    }

    @Override
    public void update() {
        super.update();

        if (duration > 0) {
            if ((lifespan -= Game.elapsed) > 0) {
                sweep = lifespan / duration;
                dirty = true;
            } else {
                killAndErase();
            }
        }
    }

    @Override
    public void draw() {

        super.draw();

        if (dirty) {
            updateTriangles();
        }

        if (lightMode) Blending.setLightMode();

        NoosaScript script = NoosaScript.get();

        texture.bind();

        script.uModel.valueM4(matrix);
        script.lighting(
                rm, gm, bm, am,
                ra, ga, ba, aa);

        script.camera(camera);
        script.drawElements(verticesBuffer, indices, nTris * 3);

        if (lightMode) Blending.setNormalMode();
    }
}
