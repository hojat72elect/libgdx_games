package es.danirod.jddprototype.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * Helper class for creating fixture definitions without having all that code in Box2DScreen.
 * Yep, in the videos this code is in Box2DScreen. I thought it was a good idea to move this
 * code outside that class to make it easier to see.
 */
public class FixtureFactory {
    public static Fixture createPlayerFixture(Body playerBody) {
        PolygonShape minijoeShape = new PolygonShape();
        minijoeShape.setAsBox(0.5f, 0.5f);
        Fixture fixture = playerBody.createFixture(minijoeShape, 3);
        minijoeShape.dispose();
        return fixture;
    }

    public static Fixture createFloorFixture(Body floorBody) {
        PolygonShape box = new PolygonShape();
        box.setAsBox(500, 1);
        Fixture fixture = floorBody.createFixture(box, 1);
        box.dispose();
        return fixture;
    }

    public static Fixture createSpikeFixture(Body pinchoBody) {
        // This is the harder shape because it is not a box. I have to design the shape in
        // terms of vertices. So I create a vertex array to give the shape all the vertices.
        Vector2[] vertices = new Vector2[3];
        vertices[0] = new Vector2(-0.5f, -0.5f);
        vertices[1] = new Vector2(0.5f, -0.5f);
        vertices[2] = new Vector2(0, 0.5f);

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);
        Fixture fixture = pinchoBody.createFixture(shape, 1);
        shape.dispose();
        return fixture;
    }
}
