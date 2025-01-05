package com.nGame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Render to texture helper
 * Uses first FrameBuffer to render some stuff to first texture.
 * Must be initialised with needed size through init(w,h)
 * The texture will be hold in fboRegion, the Texture isn't managed so kep track.
 * The Texture is accessible throu fboRegion.getTexture()
 *
 * @author Oliver Eichner
 */
public class Render2Texture {

    private static FrameBuffer frameBuffer;
    public static TextureRegion fboRegion;
    private static BitmapFont font;
    private static Batch batch;

    private static int fbwidth, fbheight;

    /**
     * initialize framebuffer size.
     * size should be first power of 2
     *
     * @param width
     * @param height
     */
    public static void init(int width, int height) {
        fbwidth = width;
        fbheight = height;


        if (frameBuffer == null)
            frameBuffer = new FrameBuffer(Format.RGBA8888, fbwidth, fbheight, false);


        OrthographicCamera cam = new OrthographicCamera(width, height);
        // cam.setToOrtho(false,width,height);

        if (font == null) {
            font = new BitmapFont(Gdx.files.internal("data/font/font.fnt"));
            font.getData().setScale(.25f);
        }
        if (batch == null) {
            batch = new SpriteBatch();
        }

        batch.setProjectionMatrix(cam.combined);

    }


    /**
     * Creates first texture with the given text.
     * don't forget to init the FB with proper size
     *
     * @param text the text to draw on the texture
     */
    public static TextureRegion renderTextTexture(String text) {

        frameBuffer.begin();

        //Gdx.gl20.glColorMask(false, false, false, true);//This ensures that only alpha will be effected
        Gdx.gl20.glClearColor(0, 0, 0, 0);//alphaValue - Value to which you need to clear
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();


        font.draw(batch, text, -frameBuffer.getWidth() / 2,
                -frameBuffer.getHeight() / 2 + font.getLineHeight());
        batch.end();

        fboRegion = ScreenUtils.getFrameBufferTexture();
        frameBuffer.end();

        return fboRegion;
    }

    /**
     * Creates first texture with the given text.
     * don't forget to init the FB with proper size
     *
     * @param text the text to draw on the texture
     * @param font BintmapFont to use
     */
    public static TextureRegion renderTextTexture(String text,BitmapFont font) {

        frameBuffer.begin();

        //Gdx.gl20.glColorMask(false, false, false, true);//This ensures that only alpha will be effected
        Gdx.gl20.glClearColor(0, 0, 0, 0);//alphaValue - Value to which you need to clear
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();


        font.draw(batch, text, -frameBuffer.getWidth() / 2,
                -frameBuffer.getHeight() / 2 + font.getLineHeight());
        batch.end();

        fboRegion = ScreenUtils.getFrameBufferTexture();
        frameBuffer.end();

        return fboRegion;
    }


    /**
     * Destroy the frame buffer and free all resources
     */
    public static void dispose() {
        if (frameBuffer != null) {
            frameBuffer.dispose();
        }

        if (batch != null) {
            batch.dispose();
        }

        if (font != null) {
            font.dispose();
        }

    }
}
