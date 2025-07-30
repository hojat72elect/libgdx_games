package es.danirod.jddprototype.box2d;

import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Helper class for creating body definitions without having all that code in Box2DScreen.
 * Yep, in the videos this code is in Box2DScreen. I thought it was a good idea to move this
 * code outside that class to make it easier to see.
 */
public class BodyDefFactory {

    public static BodyDef createPlayer() {
        BodyDef def = new BodyDef();
        def.position.set(0, 0.5f);

        // Remember to make this body dynamic. We need to make it dynamic so that it can move
        // and be affected by forces. Static bodies (this is the default) don't react to forces
        // although they still are used in contacts.
        def.type = BodyDef.BodyType.DynamicBody;
        return def;
    }

    public static BodyDef createSpikes(float x) {
        // We give the spikes the position that the user wants. Vertically it is always placed in
        // 0.5 meters. Because the spikes are 1 meter tall, and because the position is always
        // given in terms of half-width and half-height, this will make the base of the spikes
        // be over the floor.
        BodyDef def = new BodyDef();
        def.position.set(x, 0.5f);
        return def;
    }

    public static BodyDef createFloor() {
        BodyDef def = new BodyDef();
        def.position.set(0, -1);
        return def;
    }
}
