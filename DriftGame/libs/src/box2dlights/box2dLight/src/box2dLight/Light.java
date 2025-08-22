package box2dLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Light {

    public static final short MaskConsiderAllFixtures = -1;
    static final Color DefaultColor = new Color(0.75f, 0.75f, 0.5f, 0.75f);
    static final float zero = Color.toFloatBits(0f, 0f, 0f, 0f);
    final Vector2 tmpPosition = new Vector2();
    protected boolean soft = true;
    protected boolean xray = false;
    protected boolean staticLight = false;
    protected float softShadowLenght = 2.5f;
    // bitmask of the affected box2d fixtures
    protected short maskBits = MaskConsiderAllFixtures;
    protected RayHandler rayHandler;
    protected boolean culled = false;
    protected int rayNum;
    protected int vertexNum;
    protected float distance;
    protected float direction;
    protected Color color = new Color();
    protected Mesh lightMesh;
    protected Mesh softShadowMesh;
    protected float colorF;
    private boolean active = true;

    Light(RayHandler rayHandler, int rays, Color color, float directionDegree,
          float distance) {
        rayHandler.lightList.add(this);
        this.rayHandler = rayHandler;
        setRayNum(rays);
        this.direction = directionDegree;
        distance *= RayHandler.gammaCorrectionParameter;
        this.distance = Math.max(distance, 0.01f);
        setColor(color);
    }

    public void setMaskBits(int mask) {
        this.maskBits = (short) mask;
        this.xray = (mask == 0);
        if (staticLight)
            staticUpdate();
    }

    /**
     * set Color(float r, float g, float b, float a) rgb set the color and alpha
     * set intesity NOTE: you can also use colorless light with shadows(EG
     * 0,0,0,1)
     *
     * @param r red
     * @param g green
     * @param b blue
     * @param a alpha intensity
     */
    public void setColor(float r, float g, float b, float a) {
        color.set(r, g, b, a);
        colorF = color.toFloatBits();
        if (staticLight)
            staticUpdate();
    }

    abstract void update();

    abstract void render();

    public abstract void setDirection(float directionDegree);

    public void remove() {
        rayHandler.lightList.removeValue(this, false);
        lightMesh.dispose();
        softShadowMesh.dispose();
    }

    /**
     * @return attached body or null if not set.
     * <p>
     * NOTE: directional light allways return null
     */
    public abstract Body getBody();

    /**
     * set light starting position
     * <p>
     * NOTE: does absolute nothing if directional light
     */
    public abstract void setPosition(float x, float y);

    /**
     * starting position of light in world coordinates. directional light return
     * zero vector.
     * <p>
     * NOTE: changing this vector does nothing
     *
     * @return posX
     */
    public Vector2 getPosition() {
        return tmpPosition;
    }

    /**
     * set light starting position
     * <p>
     * NOTE: does absolute nothing if directional light
     */
    public abstract void setPosition(Vector2 position);

    /**
     * @return posX
     */
    public abstract float getX();

    /**
     * @return posY
     */
    public abstract float getY();

    /**
     * vertical starting position of light in world coordinates. directional
     * light return 0
     */

    void staticUpdate() {
        boolean tmp = rayHandler.culling;
        staticLight = !staticLight;
        rayHandler.culling = false;
        update();
        rayHandler.culling = tmp;
        staticLight = !staticLight;
    }

    public final boolean isActive() {
        return active;
    }

    /**
     * disable/enables this light updates and rendering.
     */
    public final void setActive(boolean active) {
        if (active == this.active)
            return;

        if (active) {
            rayHandler.lightList.add(this);
            rayHandler.disabledLights.removeValue(this, true);
        } else {
            rayHandler.disabledLights.add(this);
            rayHandler.lightList.removeValue(this, true);
        }

        this.active = active;
    }

    /**
     * disables/enables staticness for light. Static light do not get any
     * automatic updates but setting any parameters will update it. Static
     * lights are usefull for lights that you want to collide with static
     * geometry but ignore all the dynamic objects. Reduce cpu burden of light
     * about 90%.
     */
    public final void setStaticLight(boolean staticLight) {
        this.staticLight = staticLight;
        if (staticLight)
            staticUpdate();
    }

    /**
     * disable/enables softness on tips of lights beams.
     */
    public final void setSoft(boolean soft) {
        this.soft = soft;
        if (staticLight)
            staticUpdate();
    }

    private void setRayNum(int rays) {
        if (rays > rayHandler.MAX_RAYS) {
            rays = rayHandler.MAX_RAYS;
        }
        if (rays < RayHandler.MIN_RAYS) {
            rays = RayHandler.MIN_RAYS;
        }
        rayNum = rays;
        vertexNum = rays + 1;
    }

    /**
     * Color getColor
     *
     * @return current lights color
     */
    public Color getColor() {
        return color;
    }

    /**
     * setColor(Color newColor) { rgb set the color and alpha set intesity NOTE:
     * you can also use colorless light with shadows(EG 0,0,0,1)
     */
    public void setColor(Color newColor) {
        if (newColor != null) {
            color.set(newColor);
            colorF = color.toFloatBits();
        } else {
            color = DefaultColor;
            colorF = DefaultColor.toFloatBits();
        }
        if (staticLight)
            staticUpdate();
    }

    /**
     * float getDistance()
     *
     * @return light rays distance.
     */
    public float getDistance() {
        return distance / RayHandler.gammaCorrectionParameter;
    }

    /**
     * setDistance(float dist) MIN capped to 1cm
     */
    public void setDistance(float dist) {
    }
}
