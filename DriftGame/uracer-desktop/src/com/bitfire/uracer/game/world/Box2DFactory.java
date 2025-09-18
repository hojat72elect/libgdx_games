package com.bitfire.uracer.game.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.bitfire.uracer.configuration.DebugUtils;
import com.bitfire.uracer.game.collisions.CollisionFilters;

public final class Box2DFactory {

    private Box2DFactory() {
    }

    /**
     * Creates a wall by constructing a rectangle whose corners are (xmin,ymin) and (xmax,ymax), and rotating the box
     * counterclockwise through the given angle, with specified restitution.
     */
    public static void createWall(World world, float xmin, float ymin, float xmax, float ymax, float angle, float restitution) {
        float cx = (xmin + xmax) / 2;
        float cy = (ymin + ymax) / 2;
        float hx = (xmax - xmin) / 2;
        float hy = (ymax - ymin) / 2;
        if (hx < 0) {
            hx = -hx;
        }

        if (hy < 0) {
            hy = -hy;
        }

        PolygonShape wallshape = new PolygonShape();
        wallshape.setAsBox(hx, hy, new Vector2(0f, 0f), angle);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = wallshape;
        fdef.density = 1.0f;
        fdef.friction = 0.02f;

        fdef.filter.groupIndex = CollisionFilters.GroupTrackWalls;
        fdef.filter.categoryBits = CollisionFilters.CategoryTrackWalls;
        fdef.filter.maskBits = CollisionFilters.INSTANCE.getMaskWalls();

        if (DebugUtils.TraverseWalls) {
            fdef.filter.groupIndex = CollisionFilters.GroupNoCollisions;
        }

        if (restitution > 0) {
            fdef.restitution = restitution;
        }

        BodyDef bd = new BodyDef();
        bd.position.set(cx, cy);
        Body wall = world.createBody(bd);
        wall.createFixture(fdef);
        wall.setType(BodyDef.BodyType.StaticBody);
    }

    /**
     * Creates a segment-like thin wall with 0.05 thickness going from (x1,y1) to (x2,y2)
     */
    public static void createWall(World world, Vector2 from, Vector2 to, float size, float restitution) {
        // determine center point and rotation angle for createWall
        float halfSize = size / 2f;
        float cx = (from.x + to.x) / 2;
        float cy = (from.y + to.y) / 2;
        float angle = (float) Math.atan2(to.y - from.y, to.x - from.x);
        float mag = (float) Math.sqrt((to.x - from.x) * (to.x - from.x) + (to.y - from.y) * (to.y - from.y));
        createWall(world, cx - mag / 2, cy - halfSize, cx + mag / 2, cy + halfSize, angle, restitution);
    }
}
