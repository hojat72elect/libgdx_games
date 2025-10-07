package ca.hojat.shark_adventure

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class SharkAdventureGame : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var image: Texture

    override fun create() {
        batch = SpriteBatch()
        image = Texture("libgdx.png")
    }

    override fun render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)
        batch.begin()
        batch.draw(image, 140f, 210f)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        image.dispose()
    }
}
