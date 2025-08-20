package box2dLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntArray;

/**
 * Light is data container for all the light parameters. When created lights
 * are automatically added to rayHandler and could be removed by calling
 * {@link #remove()} and added manually.
 *
 * <p>Implements {@link Disposable}
 *
 * @author kalle_h
 */
public abstract class Light implements Disposable {

    static final Color DefaultColor = new Color(0.75f, 0.75f, 0.5f, 0.75f);
    static final float zeroColorBits = Color.toFloatBits(0f, 0f, 0f, 0f);
    static final float oneColorBits = Color.toFloatBits(1f, 1f, 1f, 1f);
    static final int MIN_RAYS = 3;
    /**
     * Global lights filter
     **/
    static private final Filter globalFilterA = null;
    protected final Color color = new Color();
    protected final Vector2 tmpPosition = new Vector2();
    protected final Array<Mesh> dynamicShadowMeshes = new Array<>();
    //Should never be cleared except when the light changes position (not direction). Prevents shadows from disappearing when fixture is out of sight.
    protected final Array<Fixture> affectedFixtures = new Array<>();
    protected final Array<Vector2> tmpVerts = new Array<>();
    protected final IntArray ind = new IntArray();
    protected final Vector2 tmpStart = new Vector2();
    protected final Vector2 tmpEnd = new Vector2();
    protected final Vector2 tmpVec = new Vector2();
    protected final Vector2 center = new Vector2();
    protected RayHandler rayHandler;
    protected boolean active = true;
    protected boolean soft = true;
    protected boolean xray = false;
    protected boolean staticLight = false;
    protected boolean culled = false;
    protected boolean dirty = true;
    protected boolean ignoreBody = false;
    protected int rayNum;
    protected int vertexNum;
    protected float distance;
    protected float direction;
    protected float colorF;
    protected float softShadowLength = 2.5f;
    protected Mesh lightMesh;
    protected Mesh softShadowMesh;
    protected float[] segments;
    protected float[] mx;
    protected float[] my;
    protected float[] f;
    protected int m_index = 0;
    protected float pseudo3dHeight = 0f;
    /**
     * This light specific filter
     **/
    private final Filter filterA = null;
    final RayCastCallback ray = new RayCastCallback() {
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point,
                                      Vector2 normal, float fraction) {

            if ((globalFilterA != null) && !globalContactFilter(fixture))
                return -1;

            if ((filterA != null) && !contactFilter(fixture))
                return -1;

            if (ignoreBody && fixture.getBody() == getBody())
                return -1;

            mx[m_index] = point.x;
            my[m_index] = point.y;
            f[m_index] = fraction;
            return fraction;
        }
    };
    final QueryCallback dynamicShadowCallback = fixture -> {
        if (!onDynamicCallback(fixture)) {
            return true;
        }
        affectedFixtures.add(fixture);
        if (fixture.getUserData() instanceof LightData) {
            LightData data = (LightData) fixture.getUserData();
            data.setShadowsDropped(data.getShadowsDropped() + 1);
        }
        return true;
    };

    /**
     * Creates new active light and automatically adds it to the specified
     * {@link RayHandler} instance.
     *
     * @param rayHandler      not null instance of RayHandler
     * @param rays            number of rays - more rays make light to look more realistic
     *                        but will decrease performance, can't be less than MIN_RAYS
     * @param color           light color
     * @param distance        light distance (if applicable), soft shadow length is set to distance * 0.1f
     * @param directionDegree direction in degrees (if applicable)
     */
    public Light(RayHandler rayHandler, int rays, Color color,
                 float distance, float directionDegree) {
        rayHandler.lightList.add(this);
        this.rayHandler = rayHandler;
        setRayNum(rays);
        setColor(color);
        setDistance(distance);
        setSoftnessLength(distance * 0.1f);
        setDirection(directionDegree);
    }


    /**
     * Updates this light
     */
    abstract void update();

    /**
     * Render this light
     */
    abstract void render();

    /**
     * Render this light shadow
     */
    protected void dynamicShadowRender() {
        for (Mesh m : dynamicShadowMeshes) {
            m.render(rayHandler.lightShader, GL20.GL_TRIANGLE_STRIP);
        }
    }


    /**
     * @return attached body or {@code null}
     */
    public abstract Body getBody();


    /**
     * @return horizontal starting position of light in world coordinates
     */
    public abstract float getX();

    /**
     * @return vertical starting position of light in world coordinates
     */
    public abstract float getY();


    /**
     * Sets light color
     *
     * <p>NOTE: you can also use colorless light with shadows, e.g. (0,0,0,1)
     *
     * @param r lights color red component
     * @param g lights color green component
     * @param b lights color blue component
     * @param a lights shadow intensity
     * @see #setColor(Color)
     */
    public void setColor(float r, float g, float b, float a) {
        color.set(r, g, b, a);
        colorF = color.toFloatBits();
        if (staticLight) dirty = true;
    }

    /**
     * Removes light from specified RayHandler and disposes it
     */
    public void remove() {
        remove(true);
    }

    /**
     * Removes light from specified RayHandler and disposes it if requested
     */
    public void remove(boolean doDispose) {
        if (active) {
            rayHandler.lightList.removeValue(this, false);
        } else {
            rayHandler.disabledLights.removeValue(this, false);
        }
        rayHandler = null;
        if (doDispose) dispose();
    }

    /**
     * Disposes all light resources
     */
    public void dispose() {
        affectedFixtures.clear();
        lightMesh.dispose();
        softShadowMesh.dispose();
        for (Mesh mesh : dynamicShadowMeshes) {
            mesh.dispose();
        }
        dynamicShadowMeshes.clear();
    }


    /**
     * Sets softness value for beams tips
     *
     * <p>Default: {@code 2.5f}
     */
    public void setSoftnessLength(float softShadowLength) {
        this.softShadowLength = softShadowLength;
        if (staticLight) dirty = true;
    }

    /**
     * Sets light color
     *
     * <p>NOTE: you can also use colorless light with shadows, e.g. (0,0,0,1)
     *
     * @param newColor RGB set the color and Alpha set intensity
     * @see #setColor(float, float, float, float)
     */
    public void setColor(Color newColor) {
        if (newColor != null) {
            color.set(newColor);
        } else {
            color.set(DefaultColor);
        }
        colorF = color.toFloatBits();
        if (staticLight) dirty = true;
    }

    /**
     * Sets light distance
     *
     * <p>NOTE: MIN value should be capped to 0.1f meter
     */
    public abstract void setDistance(float dist);

    /**
     * Sets light direction
     */
    public abstract void setDirection(float directionDegree);

    /**
     * Checks if given point is inside of this light area
     *
     * @param x - horizontal position of point in world coordinates
     * @param y - vertical position of point in world coordinates
     */
    public boolean contains(float x, float y) {
        return false;
    }

    public void setHeight(float height) {
        this.pseudo3dHeight = height;
    }

    /**
     * Internal method for mesh update depending on ray number
     */
    void setRayNum(int rays) {
        if (rays < MIN_RAYS)
            rays = MIN_RAYS;

        rayNum = rays;
        vertexNum = rays + 1;

        segments = new float[vertexNum * 8];
        mx = new float[vertexNum];
        my = new float[vertexNum];
        f = new float[vertexNum];
    }

    boolean contactFilter(Fixture fixtureB) {
        Filter filterB = fixtureB.getFilterData();

        if (filterA.groupIndex != 0 &&
                filterA.groupIndex == filterB.groupIndex)
            return filterA.groupIndex > 0;

        return (filterA.maskBits & filterB.categoryBits) != 0 &&
                (filterA.categoryBits & filterB.maskBits) != 0;
    }

    boolean globalContactFilter(Fixture fixtureB) {
        Filter filterB = fixtureB.getFilterData();

        if (globalFilterA.groupIndex != 0 &&
                globalFilterA.groupIndex == filterB.groupIndex)
            return globalFilterA.groupIndex > 0;

        return (globalFilterA.maskBits & filterB.categoryBits) != 0 &&
                (globalFilterA.categoryBits & filterB.maskBits) != 0;
    }

    protected boolean onDynamicCallback(Fixture fixture) {

        if ((globalFilterA != null) && !globalContactFilter(fixture)) {
            return false;
        }

        if ((filterA != null) && !contactFilter(fixture)) {
            return false;
        }

        if (ignoreBody && fixture.getBody() == getBody()) {
            return false;
        }
        //We only add the affectedFixtures once
        return !affectedFixtures.contains(fixture, true);
    }
}
