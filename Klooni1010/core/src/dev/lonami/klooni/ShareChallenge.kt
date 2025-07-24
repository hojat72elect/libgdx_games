package dev.lonami.klooni

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.BufferUtils
import com.badlogic.gdx.utils.ScreenUtils
import java.io.File

abstract class ShareChallenge {

    /**
     *  Meant to return the file path to which the image will be saved
     * On some platforms it might be as simple as Gdx.files.local().file()
     *
     * And it will return the file path to which the image will be saved
     */
    abstract val shareImageFilePath: File

    /**
     * Meant to share the saved screenshot at getShareImageFilePath()
     */
    abstract fun shareScreenshot(saveResult: Boolean)

    /**
     * Saves the "Challenge me" shareable image to getShareImageFilePath()
     */
    fun saveChallengeImage(score: Int, timeMode: Boolean): Boolean {
        val saveAt = this.shareImageFilePath
        if (!saveAt.getParentFile().isDirectory()) if (!saveAt.mkdirs()) return false

        val output = FileHandle(saveAt)

        val shareBase = Texture(Gdx.files.internal("share.png"))
        val width = shareBase.width
        val height = shareBase.height

        val frameBuffer = FrameBuffer(Pixmap.Format.RGB888, width, height, false)
        frameBuffer.begin()

        // Render the base share texture
        val batch = SpriteBatch()
        val matrix = Matrix4()
        matrix.setToOrtho2D(0f, 0f, width.toFloat(), height.toFloat())
        batch.setProjectionMatrix(matrix)

        Gdx.gl.glClearColor(Color.GOLD.r, Color.GOLD.g, Color.GOLD.b, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        batch.draw(shareBase, 0f, 0f)

        // Render the achieved score
        val style = LabelStyle()
        style.font = BitmapFont(Gdx.files.internal("font/x1.0/geosans-light64.fnt"))
        val label = Label("just scored $score on", style)
        label.setColor(Color.BLACK)
        label.setPosition(40f, 500f)
        label.draw(batch, 1f)

        label.setText("try to beat me if you can")
        label.setPosition(40f, 40f)
        label.draw(batch, 1f)

        if (timeMode) {
            val timeModeTexture = Texture("ui/x1.5/stopwatch.png")
            batch.setColor(Color.BLACK)
            batch.draw(timeModeTexture, 200f, 340f)
        }

        batch.end()

        // Get the framebuffer pixels and write them to a local file
        val pixels = ScreenUtils.getFrameBufferPixels(0, 0, width, height, true)

        val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)

        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.size)
        PixmapIO.writePNG(output, pixmap)

        // Dispose everything
        pixmap.dispose()
        shareBase.dispose()
        batch.dispose()
        frameBuffer.end()

        return true
    }
}
