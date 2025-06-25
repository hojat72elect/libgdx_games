

package com.watabou.noosa.particles;

import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Visual;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Emitter extends Group {

    protected boolean lightMode = false;

    public float x;
    public float y;
    public float width;
    public float height;

    protected Visual target;
    public boolean fillTarget = true;

    protected float interval;
    protected int quantity;

    public boolean on = false;

    private boolean started = false;
    public boolean autoKill = true;

    protected int count;
    protected float time;

    protected Factory factory;

    public void pos(float x, float y) {
        pos(x, y, 0, 0);
    }

    public void pos(PointF p) {
        pos(p.x, p.y, 0, 0);
    }

    public void pos(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        target = null;
    }

    public void pos(Visual target) {
        this.target = target;
    }

    public void pos(Visual target, float x, float y, float width, float height) {
        pos(x, y, width, height);
        pos(target);
    }

    public void burst(Factory factory, int quantity) {
        start(factory, 0, quantity);
    }

    public void pour(Factory factory, float interval) {
        start(factory, interval, 0);
    }

    public void start(Factory factory, float interval, int quantity) {

        this.factory = factory;
        this.lightMode = factory.lightMode();

        this.interval = interval;
        this.quantity = quantity;

        count = 0;
        time = Random.Float(interval);

        on = true;
        started = true;
    }

    public static boolean freezeEmitters = false;

    protected boolean isFrozen() {
        return Game.timeTotal > 1 && freezeEmitters;
    }

    @Override
    public void update() {

        if (isFrozen()) {
            return;
        }

        if (on) {
            time += Game.elapsed;
            while (time > interval) {
                time -= interval;
                emit(count++);
                if (quantity > 0 && count >= quantity) {
                    on = false;
                    break;
                }
            }
        } else if (started && autoKill && countLiving() == 0) {
            kill();
        }

        super.update();
    }

    @Override
    public void revive() {
        //ensure certain emitter variables default to true
        started = false;
        visible = true;
        fillTarget = true;
        autoKill = true;
        super.revive();
    }

    protected void emit(int index) {
        if (target == null) {
            factory.emit(
                    this,
                    index,
                    x + Random.Float(width),
                    y + Random.Float(height));
        } else {
            if (fillTarget) {
                factory.emit(
                        this,
                        index,
                        target.x + Random.Float(target.width),
                        target.y + Random.Float(target.height));
            } else {
                factory.emit(
                        this,
                        index,
                        target.x + x + Random.Float(width),
                        target.y + y + Random.Float(height));
            }
        }
    }

    @Override
    public void draw() {
        if (lightMode) {
            Blending.setLightMode();
            super.draw();
            Blending.setNormalMode();
        } else {
            super.draw();
        }
    }

    abstract public static class Factory {

        abstract public void emit(Emitter emitter, int index, float x, float y);

        public boolean lightMode() {
            return false;
        }
    }
}
