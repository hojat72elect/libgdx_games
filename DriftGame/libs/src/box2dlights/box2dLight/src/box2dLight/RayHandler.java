package box2dLight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;

import box2dLight.shaders.LightShaderKt;

public class RayHandler implements Disposable {

    final static int MIN_RAYS = 3;
    final static String HIGH = "highp";
    final static String MED = "mediump";
    private static final int DEFAULT_MAX_RAYS = 1023;
    static String colorPrecision = MED;
    static boolean gammaCorrection = false;
    static float gammaCorrectionParameter = 1f;
    static boolean isDiffuse = false;
    /**
     * This Array contain all the lights.
     * NOTE: DO NOT MODIFY THIS LIST
     */
    final public Array<Light> lightList = new Array<>(false, 16, Light.class);
    /**
     * This Array contain all the disabled lights.
     * NOTE: DO NOT MODIFY THIS LIST
     */
    final public Array<Light> disabledLights = new Array<>(false, 16, Light.class);
    final LightRayCastCallback ray = new LightRayCastCallback();
    /**
     * matrix that include projection and translation matrices
     */
    final private Matrix4 combined = new Matrix4();
    /**
     * how many lights passed culling and rendered to scene
     */
    public int lightRenderedLastFrame = 0;
    boolean culling = true;
    boolean shadows = true;
    boolean blur = true;
    int blurNum = 1;
    Color ambientLight = new Color();
    int MAX_RAYS;
    World world;
    ShaderProgram lightShader;
    boolean depthMasking;
    /**
     * camera matrix corners
     */
    float x1, x2, y1, y2;
    float[] m_segments;
    float[] m_x;
    float[] m_y;
    float[] m_f;
    int m_index = 0;
    private final LightMap lightMap;

    /**
     * Construct handler that manages everything related to updating and
     * rendering the lights MINIMUM parameters needed are world where collision
     * geometry is taken.
     * <p>
     * Default setting: culling = true, shadows = true, blur =
     * true(GL2.0),blurNum = 1, ambientLight = 0.0f;
     * <p>
     * NOTE1: rays number per lights are capped to 1023. For different size use
     * other constructor
     * <p>
     * NOTE2: On GL 2.0 FBO size is 1/4 * screen size and used by default. For
     * different sizes use other constructor
     */
    public RayHandler(World world, boolean depthMasking) {
        this(world, DEFAULT_MAX_RAYS, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4, depthMasking);
    }

    /**
     * Construct handler that manages everything related to updating and
     * rendering the lights MINIMUM parameters needed are world where collision
     * geometry is taken.
     * <p>
     * Default setting: culling = true, shadows = true, blur =
     * true(GL2.0),blurNum = 1, ambientLight = 0.0f;
     */
    public RayHandler(World world, int maxRayCount, int fboWidth, int fboHeigth, boolean depthMasking) {
        this.world = world;
        this.depthMasking = depthMasking;

        MAX_RAYS = Math.max(maxRayCount, MIN_RAYS);

        m_segments = new float[maxRayCount * 8];
        m_x = new float[maxRayCount];
        m_y = new float[maxRayCount];
        m_f = new float[maxRayCount];

        lightMap = new LightMap(this, fboWidth, fboHeigth, depthMasking);
        lightShader = LightShaderKt.createLightShader();
    }

    /**
     * set color precision to highp. Overkill quality. NOTE: this must be set
     * before rayHandler is constructed
     */
    public static void setColorPrecisionHighp() {
        colorPrecision = HIGH;
    }

    /**
     * return current color precision Note: if changed after RayHandler is
     * initialized, returned String is not what rayHandler is using
     *
     * @return colorPrecision
     */
    public static String getColorPrecision() {
        return colorPrecision;
    }

    /**
     * return is gamma correction enabled
     */
    public static boolean getGammaCorrection() {
        return gammaCorrection;
    }

    /**
     * EXPERT USE Set combined camera matrix. Matrix will be copied and used for
     * rendering lights, culling. Matrix must be set to work in box2d
     * coordinates. Matrix has to be updated every frame(if camera is changed)
     * <p>
     * NOTE: this work with rotated cameras.
     *
     * @param combined       matrix that include projection and translation matrices
     * @param x              combined matrix position
     * @param y              combined matrix position
     * @param viewPortWidth  NOTE!! use actual size, remember to multiple with zoom value
     *                       if pulled from OrthoCamera
     * @param viewPortHeight NOTE!! use actual size, remember to multiple with zoom value
     *                       if pulled from OrthoCamera
     */
    public void setCombinedMatrix(Matrix4 combined, float x, float y, float viewPortWidth, float viewPortHeight) {
        System.arraycopy(combined.val, 0, this.combined.val, 0, 16);
        // updateCameraCorners
        final float halfViewPortWidth = viewPortWidth * 0.5f;
        x1 = x - halfViewPortWidth;
        x2 = x + halfViewPortWidth;

        final float halfViewPortHeight = viewPortHeight * 0.5f;
        y1 = y - halfViewPortHeight;
        y2 = y + halfViewPortHeight;
    }

    boolean intersect(float x, float y, float side) {
        return (x1 < (x + side) && x2 > (x - side) && y1 < (y + side) && y2 > (y - side));
    }

    /**
     * Manual update method for all lights. Use this if you have less physic
     * steps than rendering steps.
     */
    public final void update() {
        final int size = lightList.size;
        for (int j = 0; j < size; j++) {
            lightList.items[j].update();
        }
    }

    /**
     * Manual rendering method for all lights.
     * <p>
     * NOTE! Remember to call updateRays if you use this method. * Remember
     * setCombinedMatrix(Matrix4 combined) before drawing.
     * <p>
     * <p>
     * Don't call this inside of any begin/end statements. Call this method
     * after you have rendered background but before UI. Box2d bodies can be
     * rendered before or after depending how you want x-ray light interact with
     * bodies
     */
    public void updateLightMap() {

        lightRenderedLastFrame = 0;

        if (depthMasking) {
            Gdx.gl.glDepthMask(true);
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

        renderWithShaders();
    }

    void renderWithShaders() {

        if (shadows || blur) {
            lightMap.frameBuffer.begin();
            Gdx.gl20.glClearColor(0, 0, 0, 0);

            if (depthMasking) {
                Gdx.gl20.glClearDepthf(1);
                Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            } else {
                Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
            }
        }

        lightShader.begin();
        {
            lightShader.setUniformMatrix("u_projTrans", combined);

            final Light[] list = lightList.items;
            for (int i = 0, size = lightList.size; i < size; i++) {
                list[i].render();
            }
        }
        lightShader.end();

        if (shadows || blur) {
            lightMap.frameBuffer.end();
        }
    }

    public void renderLightMap(Rectangle viewport, FrameBuffer dest) {
        lightMap.render(viewport, dest);
    }

    @Override
    public void dispose() {

        for (int i = 0; i < lightList.size; i++) {
            lightList.items[i].lightMesh.dispose();
            lightList.items[i].softShadowMesh.dispose();
        }
        lightList.clear();

        for (int i = 0; i < disabledLights.size; i++) {
            disabledLights.items[i].lightMesh.dispose();
            disabledLights.items[i].softShadowMesh.dispose();
        }
        disabledLights.clear();

        if (lightMap != null)
            lightMap.dispose();
        if (lightShader != null)
            lightShader.dispose();
    }

    final void doRaycast(Light requestingLight, Vector2 start, Vector2 end) {
        ray.requestingLight = requestingLight;
        world.rayCast(ray, start, end);
    }

    /**
     * Disables/enables culling. This save cpu and gpu time when world is bigger
     * than screen.
     * <p>
     * Default = true
     *
     * @param culling the culling to set
     */
    public final void setCulling(boolean culling) {
        this.culling = culling;
    }

    /**
     * Disables/enables gaussian blur. This make lights much more softer and
     * realistic look but also cost some precious shader time. With default fbo
     * size on android cost around 1ms
     * <p>
     * default = true;
     *
     * @param blur the blur to set
     */
    public final void setBlur(boolean blur) {
        this.blur = blur;
    }

    /**
     * Set number of gaussian blur passes. Blurring can be pretty heavy weight
     * operation, 1-3 should be safe. Setting this to 0 is same as
     * setBlur(false)
     * <p>
     * default = 1
     *
     * @param blurNum the blurNum to set
     */
    public final void setBlurNum(int blurNum) {
        this.blurNum = blurNum;
    }

    /**
     * Disables/enables shadows. NOTE: If gl1.1 android you need to change
     * render target to contain alpha channel* default = true
     *
     * @param shadows the shadows to set
     */
    public final void setShadows(boolean shadows) {
        this.shadows = shadows;
    }

    /**
     * Ambient light color is how dark and what colored the shadows are. clamped
     * to 0-1 NOTE: color is changed only in gles2.0 default = 0;
     */
    public final void setAmbientLight(float r, float g, float b, float a) {
        this.ambientLight.set(r, g, b, a);
    }

    /**
     * Ambient light color is how dark and what colored the shadows are. clamped
     * to 0-1 NOTE: color is changed only in gles2.0 default = 0,0,0,0;
     */
    public final void setAmbientLight(Color ambientLightColor) {
        this.ambientLight.set(ambientLightColor);
    }

    /**
     * @param world the world to set
     */
    public final void setWorld(World world) {
        this.world = world;
    }

    class LightRayCastCallback implements RayCastCallback {
        public Light requestingLight = null;
        private final HashMap<Fixture, Filter> map = new HashMap<>();

        @Override
        final public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

            if ((requestingLight.maskBits != Light.MaskConsiderAllFixtures) && !considerFixture(fixture))
                return -1;
            m_x[m_index] = point.x;
            m_y[m_index] = point.y;
            m_f[m_index] = fraction;
            return fraction;
        }

        final boolean considerFixture(Fixture fixture) {
            Filter filter = map.get(fixture);
            if (filter == null) {
                filter = fixture.getFilterData();
                map.put(fixture, filter);
            }

            return ((requestingLight.maskBits & filter.categoryBits) != 0);
        }
    }
}
