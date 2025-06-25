

package com.watabou.noosa;

public class Gizmo {

    public boolean exists;
    public boolean alive;
    public boolean active;
    public boolean visible;

    public Group parent;

    public Camera camera;

    public Gizmo() {
        exists = true;
        alive = true;
        active = true;
        visible = true;
    }

    public void destroy() {
        parent = null;
    }

    public void update() {
    }

    public void draw() {
    }

    public void kill() {
        alive = false;
        exists = false;
    }

    // Not exactly opposite to "kill" method
    public void revive() {
        alive = true;
        exists = true;
    }

    public Camera camera() {
        if (camera != null) {
            return camera;
        } else if (parent != null) {
            return this.camera = parent.camera();
        } else {
            return null;
        }
    }

    public boolean isVisible() {
        if (parent == null) {
            return visible;
        } else {
            return visible && parent.isVisible();
        }
    }

    public boolean isActive() {
        if (parent == null) {
            return active;
        } else {
            return active && parent.isActive();
        }
    }

    public void killAndErase() {
        kill();
        if (parent != null) {
            parent.erase(this);
        }
    }

    public void remove() {
        if (parent != null) {
            parent.remove(this);
        }
    }
}
