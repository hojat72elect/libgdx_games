package ca.hojat.sokoban.scene2d

import ca.hojat.sokoban.Assets
import ca.hojat.sokoban.Settings
import ca.hojat.sokoban.SokobanGame
import ca.hojat.sokoban.screens.MainMenuScreen
import ca.hojat.sokoban.screens.Screens
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.I18NBundle

class LevelSelector(currentScreen: Screens) : Group() {
    private var menuScreen: MainMenuScreen
    private var languagesBundle: I18NBundle?
    private var game: SokobanGame

    private var labelTitle: Label

    private var scrollPane: ScrollPane
    private var tableLevelsContainer: Table

    /**
     * Current page (each page has 15 levels).
     */
    private var actualPage: Int
    private var totalStars: Int = 0

    var vtLevel: WindowGroupLevel

    init {
        setSize(600f, 385f)
        setPosition(Screens.SCREEN_WIDTH / 2f - width / 2f, 70f)
        menuScreen = currentScreen as MainMenuScreen
        game = currentScreen.game
        languagesBundle = game.languages

        setBackGround(Assets.windowBackground)

        vtLevel = WindowGroupLevel(currentScreen)
        val tbTitulo = Table()

        tbTitulo.setSize(300f, 50f)
        tbTitulo.setPosition(width / 2f - tbTitulo.width / 2f, 324f)

        labelTitle = Label("Levels", LabelStyle(Assets.font, Color.WHITE))
        tbTitulo.add(labelTitle)

        tableLevelsContainer = Table()
        scrollPane = ScrollPane(tableLevelsContainer)
        scrollPane.setSize(width - 100, height - 100)
        scrollPane.setPosition(width / 2f - scrollPane.width / 2f, 30f)
        scrollPane.setScrollingDisabled(false, true)

        tableLevelsContainer.defaults().padLeft(5f).padRight(5f)

        for (i in Settings.levels.indices) {
            totalStars += Settings.levels[i]!!.numStars
        }
        totalStars += 2 // By default I already have 3 stars.

        var numeroPages = (Settings.NUM_MAPS / 15f).toInt()
        if (Settings.NUM_MAPS % 15f != 0f) numeroPages++

        for (col in 0..<numeroPages) {
            tableLevelsContainer.add(getListLevel(col))
        }

        actualPage = 0

        addActor(tbTitulo)
        addActor(scrollPane)

        scrollToPage(0)
    }

    private fun setBackGround(imageBackground: TextureRegionDrawable?) {
        val img = Image(imageBackground)
        img.setSize(width, height)
        addActor(img)
    }

    /**
     * Each list has 15 items.
     */
    private fun getListLevel(list: Int): Table {
        val content = Table()

        var level = list * 15
        content.defaults().pad(7f, 12f, 7f, 12f)
        for (col in 0..14) {
            val oButton = getLevelButton(level)
            content.add(oButton).size(76f, 83f)
            level++

            if (level % 5 == 0) content.row()

            // to hide worlds that do not exist.
            if (level > Settings.NUM_MAPS) oButton.isVisible = false
        }
        return content
    }

    private fun scrollToPage(page: Int) {
        val tabToScrollTo = tableLevelsContainer.children[page] as Table
        scrollPane.scrollTo(tabToScrollTo.x, tabToScrollTo.y, tabToScrollTo.width, tabToScrollTo.height)
    }

    fun nextPage() {
        actualPage++
        if (actualPage >= tableLevelsContainer.children.size) actualPage = tableLevelsContainer.children.size - 1
        scrollToPage(actualPage)
    }

    fun previousPage() {
        actualPage--
        if (actualPage < 0) actualPage = 0
        scrollToPage(actualPage)
    }

    private fun getLevelButton(level: Int): Button {
        val button: TextButton

        val skullsToNextLevel = (level * 1f).toInt() // I only need 1 star to unlock the next level

        if (totalStars < skullsToNextLevel) {
            button = TextButton("", Assets.styleTextButtonLevelLocked)
            button.isDisabled = true
        } else {
            button = TextButton("" + (level + 1), Assets.styleTextButtonLevel)

            // I'm adding worlds that don't exist so I can fill the table, that's why I'm putting this fix.
            var completed = false
            if (level < Settings.levels.size) {
                if (Settings.levels[level]!!.numStars == 1) completed = true
            }

            val imgLevel = if (completed) Image(Assets.levelStarDrawable)
            else Image(Assets.levelOffDrawable)

            button.row()
            button.add(imgLevel).size(10f).padBottom(2f)
        }

        button.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (!button.isDisabled) {
                    vtLevel.show(stage, level, Settings.levels[level]!!.bestMoves, Settings.levels[level]!!.bestTime)
                }
            }
        })

        menuScreen.addPressEffect(button)

        return button
    }
}
