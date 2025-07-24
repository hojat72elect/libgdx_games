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
import dev.lonami.klooni.Klooni.Companion.shouldSnapToGrid
import dev.lonami.klooni.Klooni.Companion.soundsEnabled
import dev.lonami.klooni.Klooni.Companion.toggleSnapToGrid
import dev.lonami.klooni.Klooni.Companion.toggleSound
import dev.lonami.klooni.Theme.Companion.getThemes
import dev.lonami.klooni.actors.EffectCard
import dev.lonami.klooni.actors.MoneyBuyBand
import dev.lonami.klooni.actors.ShopCard
import dev.lonami.klooni.actors.SoftButton
import dev.lonami.klooni.actors.ThemeCard
import dev.lonami.klooni.game.GameLayout
import kotlin.math.min


// Screen where the user can customize the look and feel of the game
internal class CustomizeScreen(private val game: Klooni, private val lastScreen: Screen?) : Screen {
    private val stage: Stage
    private val table: Table
    private val toggleShopButton: SoftButton
    private val shopGroup: VerticalGroup // Showing available themes or effects
    private val shopScroll: ScrollPane
    private val buyBand: MoneyBuyBand
    private var showingEffectsShop = false

    private var showcaseIndex = 0
    private var shopDragStartX = 0f
    private var shopDragStartY = 0f

    init {
        stage = Stage()

        table = Table()
        table.setFillParent(true)
        stage.addActor(table)

        val optionsGroup = HorizontalGroup()
        optionsGroup.space(12f)

        // Back to the previous screen
        val backButton = SoftButton(1, "back_texture")
        backButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                goBack()
            }
        })
        optionsGroup.addActor(backButton)

        // Turn sound on/off
        val soundButton = SoftButton(
            0, if (soundsEnabled()) "sound_on_texture" else "sound_off_texture"
        )

        soundButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val enabled = toggleSound()
                soundButton.image = this@CustomizeScreen.game.skin!!.getDrawable(
                    if (enabled) "sound_on_texture" else "sound_off_texture"
                )

                buyBand.setTempText("sound " + (if (enabled) "on" else "off"))
            }
        })
        optionsGroup.addActor(soundButton)

        // Toggle the current shop (themes or effects)
        toggleShopButton = SoftButton(2, "effects_texture")
        toggleShopButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
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
        val snapButton = SoftButton(
            1, if (shouldSnapToGrid()) "snap_on_texture" else "snap_off_texture"
        )

        snapButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val shouldSnap = toggleSnapToGrid()
                snapButton.image = this@CustomizeScreen.game.skin!!.getDrawable(
                    if (shouldSnap) "snap_on_texture" else "snap_off_texture"
                )

                buyBand.setTempText("snap to grid " + (if (shouldSnap) "on" else "off"))
            }
        })
        optionsGroup.addActor(snapButton)

        // Issues
        val issuesButton = SoftButton(3, "issues_texture")
        issuesButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                Gdx.net.openURI("https://github.com/LonamiWebs/Klooni1010/issues")
            }
        })
        optionsGroup.addActor(issuesButton)

        // Website
        val webButton = SoftButton(2, "web_texture")
        webButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                Gdx.net.openURI("https://lonamiwebs.github.io")
            }
        })
        optionsGroup.addActor(webButton)

        // Use the same height as the buttons (for instance, the back button)
        table.add<ScrollPane?>(ScrollPane(optionsGroup))
            .pad(20f, 4f, 12f, 4f).height(backButton.getHeight())

        buyBand = MoneyBuyBand(game)
        table.row()

        // Load all the available themes as the default "shop"
        shopGroup = VerticalGroup()
        shopScroll = ScrollPane(shopGroup)
        table.add<ScrollPane?>(shopScroll).expand().fill()
        loadShop()

        // Show the current money row
        table.row()
        table.add<MoneyBuyBand?>(buyBand).expandX().fillX()
    }

    private fun goBack() {
        this@CustomizeScreen.game.transitionTo(lastScreen)
    }

    private fun loadShop() {
        showcaseIndex = 0 // Reset the index

        val layout = GameLayout()
        shopGroup.clear()

        if (showingEffectsShop) for (effect in Klooni.EFFECTS) addCard(EffectCard(game, layout, effect))
        else  // showingThemesShop
            for (theme in getThemes()) addCard(ThemeCard(game, layout, theme))

        // Scroll to the currently selected item
        table.layout()
        for (a in shopGroup.getChildren()) {
            val c = a as ShopCard
            if (c.isUsed) {
                shopScroll.scrollTo(
                    c.getX(), c.getY() + c.getHeight(),
                    c.getWidth(), c.getHeight()
                )
                break
            }
            c.usedItemUpdated()
        }
    }

    private fun addCard(card: ShopCard) {
        card.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                shopDragStartX = x
                shopDragStartY = y
                return true
            }

            // We could actually rely on touchDragged not being called,
            // but perhaps it would be hard for some people not to move
            // their fingers even the slightest bit, so we use a custom
            // drag limit
            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                var x = x
                var y = y
                x -= shopDragStartX
                y -= shopDragStartY
                val distSq = x * x + y * y
                if (distSq < DRAG_LIMIT_SQ) {
                    if (card.isBought) card.use()
                    else buyBand.askBuy(card)

                    for (a in shopGroup.getChildren()) {
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
        Klooni.theme!!.glClearBackground()
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stage.act(min(Gdx.graphics.deltaTime, MIN_DELTA))
        stage.draw()

        // After everything is drawn, showcase the current shop item
        val children = shopGroup.getChildren()
        if (children.size > 0) {
            val card = children.get(showcaseIndex) as ShopCard

            val batch = stage.batch
            batch.begin()
            // For some really strange reason, we need to displace the particle effect
            // by "buyBand.height", or it will render exactly that height below where
            // it should.
            // TODO Fix this - maybe use the same project matrix as stage.draw()?
            // batch.setProjectionMatrix(stage.getViewport().getCamera().combined)
            if (!card.showcase(batch, buyBand.getHeight())) {
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

    override fun dispose() {
        stage.dispose()
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
    }

    companion object {
        // As the examples show on the LibGdx wiki
        private val MIN_DELTA = 1 / 30f
        private val DRAG_LIMIT_SQ = (20 * 20).toFloat()
    }
}
