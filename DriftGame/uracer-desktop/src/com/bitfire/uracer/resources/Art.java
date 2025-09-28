package com.bitfire.uracer.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;
import com.bitfire.uracer.configuration.GraphicsUtils;
import com.bitfire.uracer.configuration.Storage;
import com.bitfire.utils.ShaderLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class Art {
    // fonts
    public static final int DebugFontWidth = 6;
    public static final int DebugFontHeight = 6;
    public static TextureRegion[][] debugFont;
    // tileset friction maps
    public static Pixmap frictionMapDesert;
    // 3d
    public static Texture meshMissing;
    public static Texture meshTreeTrunk;
    public static ObjectMap<String, Texture> meshCar;
    public static Texture[] meshTreeLeavesSpring;
    public static Texture meshTrackWall;
    // cars
    public static TextureAtlas cars;
    public static TextureRegion skidMarksFront, skidMarksRear;
    public static Texture wrongWay;
    public static TextureAtlas fontAtlas;

    // post-processor
    // public static ShaderProgram depthMapGen, depthMapGenTransparent;
    public static Texture postXpro;
    public static Texture postLensFlare;

    // screens
    public static Texture scrBackground;
    public static Skin scrSkin;
    public static Texture scrPanel;
    // circle progress
    public static Texture texCircleProgress;
    public static Texture texCircleProgressMask;
    public static Texture texCircleProgressHalf;
    public static Texture texCircleProgressHalfMask;
    public static Texture texRadLinesProgress;
    // particle effects
    public static TextureAtlas particles;
    private static TextureAtlas skinAtlas;

    // hide constructor
    private Art() {
    }

    public static void init() {
        ShaderLoader.INSTANCE.setBasePath("data/shaders/");
        loadFonts();
        loadCarGraphics();
        loadParticlesGraphics();
        loadMeshesGraphics();
        loadFrictionMaps();
        loadPostProcessorMaps();
        loadScreensData();
        loadCircleProgress();
    }

    public static void dispose() {
        disposeFonts();
        disposeParticlesGraphics();
        disposeCarGraphics();
        disposeMeshesGraphics();
        disposeFrictionMaps();
        disposePostProcessorMaps();
        disposeScreensData();
        disposeCircleProgress();
    }

    //
    // circle progress
    //
    private static void loadCircleProgress() {
        texCircleProgress = Art.newTexture("data/base/progress/circle-progress-full.png", true);
        texCircleProgress.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);

        texCircleProgressHalf = Art.newTexture("data/base/progress/circle-progress-half.png", true);
        texCircleProgressHalf.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);

        texCircleProgressHalfMask = Art.newTexture("data/base/progress/circle-progress-half-mask.png", true);
        texCircleProgressHalfMask.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);

        texRadLinesProgress = Art.newTexture("data/base/progress/radlines-progress-full.png", true);
        texRadLinesProgress.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);

        texCircleProgressMask = Art.newTexture("data/base/progress/circle-progress-mask.png", true);
        texCircleProgressMask.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
    }

    private static void disposeCircleProgress() {
        texCircleProgress.dispose();
        texCircleProgressMask.dispose();
        texRadLinesProgress.dispose();
        texCircleProgressHalf.dispose();
        texCircleProgressHalfMask.dispose();
    }

    //
    // screens
    //
    public static void loadScreensData() {
        scrBackground = newTexture("data/base/titlescreen.png", true);

        // kenney
        String skinPath = Storage.UI + "kenney/";
        skinAtlas = new TextureAtlas(Gdx.files.internal(skinPath + "pack.atlas"));
        scrSkin = new Skin(Gdx.files.internal(skinPath + "kenney.json"), skinAtlas);

        // brushed texture
        scrPanel = newTexture("data/base/panel.png", false);
    }

    public static void disposeScreensData() {
        scrSkin.dispose();
        scrBackground.dispose();
        skinAtlas.dispose();
        scrPanel.dispose();
    }

    private static void loadPostProcessorMaps() {
        postXpro = newTexture("data/base/xpro-lut.png", false);
        postXpro.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);

        postLensFlare = newTexture("data/base/lenscolor.png", false);
        postLensFlare.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
    }

    private static void disposePostProcessorMaps() {
        postXpro.dispose();
        postLensFlare.dispose();
    }

    private static void loadFrictionMaps() {
        frictionMapDesert = new Pixmap(Gdx.files.internal("data/levels/tileset/desert-friction-easy.png"));
    }

    private static void disposeFrictionMaps() {
        frictionMapDesert.dispose();
    }

    private static void loadMeshesGraphics() {
        meshTrackWall = newTexture("data/track/wall_4.png", GraphicsUtils.EnableMipMapping);
        meshTrackWall.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);

        meshMissing = newTexture("data/3d/textures/missing-mesh.png", GraphicsUtils.EnableMipMapping);

        meshCar = new ObjectMap<>();
        meshCar.put("car", newTexture("data/3d/textures/car.png", GraphicsUtils.EnableMipMapping));
        meshCar.put("car_yellow", newTexture("data/3d/textures/car_yellow.png", GraphicsUtils.EnableMipMapping));

        meshTreeTrunk = newTexture("data/3d/textures/trunk_6_col.png", GraphicsUtils.EnableMipMapping);
        meshTreeLeavesSpring = new Texture[7];
        for (int i = 0; i < 7; i++) {
            meshTreeLeavesSpring[i] = newTexture("data/3d/textures/leaves_" + (i + 1) + "_spring_1.png", GraphicsUtils.EnableMipMapping);
        }
    }

    private static void disposeMeshesGraphics() {
        meshMissing.dispose();
        meshTrackWall.dispose();

        for (Texture t : meshCar.values()) {
            t.dispose();
        }
        meshCar.clear();

        for (int i = 0; i < 7; i++) {
            meshTreeLeavesSpring[i].dispose();
        }

        meshTreeTrunk.dispose();
    }

    private static void loadCarGraphics() {
        cars = new TextureAtlas("data/cars/pack.atlas");

        skidMarksFront = cars.findRegion("skid-marks-front");
        skidMarksRear = cars.findRegion("skid-marks-rear");

        wrongWay = newTexture("data/base/wrong-way.png", false);
        wrongWay.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }

    private static void disposeCarGraphics() {
        wrongWay.dispose();
        cars.dispose();
    }

    private static void loadParticlesGraphics() {
        particles = new TextureAtlas("data/partfx/textures/pack.atlas");
    }

    private static void disposeParticlesGraphics() {
        particles.dispose();
    }

    private static void loadFonts() {
        // debug font, no need to scale it
        debugFont = split();
        debugFont[0][0].getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);


        fontAtlas = new TextureAtlas("data/font/pack.atlas");
    }

    private static void disposeFonts() {
        debugFont[0][0].getTexture().dispose();
        fontAtlas.dispose();
    }

    public static Texture getFlag(String countryCode) {
        String filename = countryCode + ".png";
        FileHandle zip = Gdx.files.internal("data/flags.zip");
        ZipInputStream zin = new ZipInputStream(zip.read());
        ZipEntry ze;
        try {
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.getName().equals(filename)) {
                    ByteArrayOutputStream streamBuilder = new ByteArrayOutputStream();
                    int bytesRead;
                    byte[] tempBuffer = new byte[8192 * 2];
                    while ((bytesRead = zin.read(tempBuffer)) != -1) {
                        streamBuilder.write(tempBuffer, 0, bytesRead);
                    }

                    Pixmap px = new Pixmap(streamBuilder.toByteArray(), 0, streamBuilder.size());

                    streamBuilder.close();
                    zin.close();

                    Texture t = new Texture(px);

                    t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

                    px.dispose();
                    return t;
                }
            }
        } catch (IOException ignored) {
        }

        return null;
    }

    private static TextureRegion[][] split() {
        return split(false);
    }

    private static TextureRegion[][] split(boolean mipMap) {
        Texture texture = newTexture("data/base/debug-font.png", mipMap);
        int xSlices = texture.getWidth() / Art.DebugFontWidth;
        int ySlices = texture.getHeight() / Art.DebugFontHeight;
        TextureRegion[][] res = new TextureRegion[xSlices][ySlices];
        for (int x = 0; x < xSlices; x++) {
            for (int y = 0; y < ySlices; y++) {
                res[x][y] = new TextureRegion(texture, x * Art.DebugFontWidth, y * Art.DebugFontHeight, Art.DebugFontWidth, Art.DebugFontHeight);
                res[x][y].flip(false, true);
            }
        }
        return res;
    }

    public static Texture newTexture(String name, boolean mipMap) {
        Texture t = new Texture(Gdx.files.internal(name), Format.RGBA8888, mipMap);

        if (mipMap) {
            t.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);
        } else {
            t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        }

        return t;
    }
}
