package com.nopalsoft.sokoban.scene2d

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
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.Settings
import com.nopalsoft.sokoban.screens.MainMenuScreen
import com.nopalsoft.sokoban.screens.Screens

/**
 * This is the first page of the game; which shows pages of levels to the user, and they can
 * pick any of those levels to play.
 */
class LevelSelector(currentScreen: Screens) : Group() {

    private val menuScreen = currentScreen as MainMenuScreen

    private val vtLevel = DialogLevel(currentScreen)
    private val labelTitle = Label("Levels", LabelStyle(Assets.fontDefault, Color.WHITE))
    private val tableContainer = Table()
    private val scrollPane = ScrollPane(tableContainer)

    // Current page (each page has 15 levels)
    private var currentPage = 0
    private var totalStars = 0

    init {
        setSize(600f, 385f)
        setPosition(Screens.SCREEN_WIDTH / 2f - width / 2f, 70f)

        setBackGround(Assets.windowBackground)

        val tableTitle = Table()
        tableTitle.setSize(300f, 50f)
        tableTitle.setPosition(width / 2f - tableTitle.width / 2f, 324f)
        tableTitle.add(labelTitle)

        scrollPane.setSize(width - 100, height - 100)
        scrollPane.setPosition(width / 2f - scrollPane.width / 2f, 30f)
        scrollPane.setScrollingDisabled(false, true)

        tableContainer.defaults().padLeft(5f).padRight(5f)

        for (i in Settings.arrLevel!!.indices) {
            totalStars += Settings.arrLevel!![i]!!.numStars
        }
        totalStars += 2 // By default, I already have 3 stars


        // total number of pages
        var numPages = Settings.NUM_MAPS / 15
        numPages++

        for (col in 0..<numPages) {
            tableContainer.add(getListLevel(col))
        }

        currentPage = 0

        addActor(tableTitle)
        addActor(scrollPane)

        scrollToPage(0)
    }

    private fun setBackGround(imageBackground: TextureRegionDrawable) {
        val img = Image(imageBackground)
        img.setSize(width, height)
        addActor(img)
    }

    // Each list has 15 items.
    private fun getListLevel(list: Int): Table {
        val content = Table()

        var level = list * 15
        content.defaults().pad(7f, 12f, 7f, 12f)
        for (col in 0..14) {
            val oButton: Button = getLevelButton(level)
            content.add(oButton).size(76f, 83f)
            level++

            if (level % 5 == 0) content.row()

            // to hide worlds that do not exist
            if (level > Settings.NUM_MAPS) oButton.isVisible = false
        }
        return content
    }

    private fun scrollToPage(page: Int) {
        val tabToScrollTo = tableContainer.children[page] as Table
        scrollPane.scrollTo(tabToScrollTo.x, tabToScrollTo.y, tabToScrollTo.width, tabToScrollTo.height)
    }

    fun nextPage() {
        currentPage++
        if (currentPage >= tableContainer.children.size)
            currentPage = tableContainer.children.size - 1
        scrollToPage(currentPage)
    }

    fun previousPage() {
        currentPage--
        if (currentPage < 0)
            currentPage = 0
        scrollToPage(currentPage)
    }

    private fun getLevelButton(level: Int): Button {
        val button: TextButton

        val skullsToNextLevel = level  // I only need 1 star to unlock the next level

        if (totalStars < skullsToNextLevel) {
            button = TextButton("", Assets.styleTextButtonLevelLocked)
            button.isDisabled = true
        } else {
            button = TextButton((level + 1).toString(), Assets.styleTextButtonLevel)

            // I'm adding worlds that don't exist to fill the table, that's why I'm putting this fix.
            var completed = false
            if (level < Settings.arrLevel!!.size) {
                if (Settings.arrLevel!![level]!!.numStars == 1) completed = true
            }

            val imgLevel = if (completed) Image(Assets.levelStar)
            else Image(Assets.levelOff)

            button.row()
            button.add(imgLevel).size(10f).padBottom(2f)
        }

        button.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (!button.isDisabled) {
                    vtLevel.show(stage, level, Settings.arrLevel!![level]!!.bestMoves, Settings.arrLevel!![level]!!.bestTime)
                }
            }
        })

        menuScreen.addEfectoPress(button)

        return button
    }
}
