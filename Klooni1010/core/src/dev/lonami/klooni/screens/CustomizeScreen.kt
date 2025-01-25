package dev.lonami.klooni.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.Theme
import dev.lonami.klooni.actors.EffectCard
import dev.lonami.klooni.actors.MoneyBuyBand
import dev.lonami.klooni.actors.ShopCard
import dev.lonami.klooni.actors.SoftButton
import dev.lonami.klooni.actors.ThemeCard
import dev.lonami.klooni.game.GameLayout
import kotlin.math.min

/**
 * Screen where the user can customize the look and feel of the game.
 */
class CustomizeScreen(val game: Klooni, private val lastScreen: Screen) : Screen {

    private val stage = Stage()
    private val table = Table()
    private val toggleShopButton = SoftButton(2, "effects_texture")

    // Showing available themes or effects. Load all the available themes as the default "shop".
    private val shopGroup = VerticalGroup()

    private val shopScroll = ScrollPane(shopGroup)
    private val buyBand = MoneyBuyBand(game)
    private var showingEffectsShop = false
    private var showcaseIndex = 0
    private var shopDragStartX = 0f
    private var shopDragStartY = 0f

    init {
        table.setFillParent(true)
        stage.addActor(table)

        val optionsGroup = HorizontalGroup()
        optionsGroup.space(12f)


        // Back to the previous screen
        val backButton = SoftButton(1, "back_texture")
        backButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                goBack()
            }
        })
        optionsGroup.addActor(backButton)


        // Turn sound on/off
        val soundButton = SoftButton(0, if (Klooni.soundsEnabled()) "sound_on_texture" else "sound_off_texture")
        soundButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                val enabled = Klooni.toggleSound()
                soundButton.image = game.skin.getDrawable(
                    if (enabled) "sound_on_texture" else "sound_off_texture"
                )

                buyBand.setTempText("sound " + (if (enabled) "on" else "off"))
            }
        })
        optionsGroup.addActor(soundButton)

        toggleShopButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                showingEffectsShop = !showingEffectsShop
                if (showingEffectsShop) {
                    toggleShopButton.updateImage("palette_texture")
                } else {
                    toggleShopButton.updateImage("effects_texture")
                }
                loadShop()
            }
        })
        optionsGroup.addActor(toggleShopButton)


        // Snap to grid on/off
        val snapButton = SoftButton(1, if (Klooni.shouldSnapToGrid()) "snap_on_texture" else "snap_off_texture")
        snapButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                val shouldSnap = Klooni.toggleSnapToGrid()
                snapButton.image = game.skin.getDrawable(
                    if (shouldSnap) "snap_on_texture" else "snap_off_texture"
                )

                buyBand.setTempText("snap to grid " + (if (shouldSnap) "on" else "off"))
            }
        })
        optionsGroup.addActor(snapButton)


        // Issues
        val issuesButton = SoftButton(3, "issues_texture")
        issuesButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                Gdx.net.openURI("https://github.com/LonamiWebs/Klooni1010/issues")
            }
        })
        optionsGroup.addActor(issuesButton)


        // Website
        val webButton = SoftButton(2, "web_texture")
        webButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                Gdx.net.openURI("https://lonamiwebs.github.io")
            }
        })
        optionsGroup.addActor(webButton)

        // Use the same height as the buttons (for instance, the back button)
        table.add(ScrollPane(optionsGroup)).pad(20f, 4f, 12f, 4f).height(backButton.height)

        table.row()
        table.add(shopScroll).expand().fill()
        loadShop()


        // Show the current money row
        table.row()
        table.add(buyBand).expandX().fillX()
    }

    private fun goBack() {
        game.transitionTo(lastScreen)
    }

    private fun loadShop() {
        showcaseIndex = 0 // Reset the index

        val layout = GameLayout()
        shopGroup.clear()

        if (showingEffectsShop) for (effect in Klooni.EFFECTS) addCard(EffectCard(game, layout, effect))
        else  // showingThemesShop
            for (theme in Theme.getThemes()) addCard(ThemeCard(game, layout, theme))

        // Scroll to the currently selected item
        table.layout()
        for (a in shopGroup.children) {
            val c = a as ShopCard
            if (c.isUsed()) {
                shopScroll.scrollTo(
                    c.x, c.y + c.height,
                    c.width, c.height
                )
                break
            }
            c.usedItemUpdated()
        }
    }

    private fun addCard(card: ShopCard) {
        card.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                shopDragStartX = x
                shopDragStartY = y
                return true
            }

            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                var temporaryX = x
                var temporaryY = y
                temporaryX -= shopDragStartX
                temporaryY -= shopDragStartY
                val distSq = temporaryX * temporaryX + temporaryY * temporaryY
                if (distSq < DRAG_LIMIT_SQ) {
                    if (card.isBought()) card.use()
                    else buyBand.askBuy(card)

                    for (a in shopGroup.children) {
                        (a as ShopCard).usedItemUpdated()
                    }
                }
            }
        })

        shopGroup.addActor(card)
    }

    override fun show() {
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        Klooni.theme.glClearBackground()
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stage.act(min(Gdx.graphics.deltaTime, MIN_DELTA))
        stage.draw()

        // After everything is drawn, showcase the current shop item
        val children = shopGroup.children
        if (children.size > 0) {
            val card = children[showcaseIndex] as ShopCard

            val batch = stage.batch
            batch.begin()
            if (!card.showcase(batch, buyBand.height)) {
                showcaseIndex = (showcaseIndex + 1) % children.size
            }
            batch.end()
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            goBack()
        }

    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {
        stage.dispose()
    }

    companion object {
        private const val MIN_DELTA = 1F / 30
        private const val DRAG_LIMIT_SQ = 400F
    }
}