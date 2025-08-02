package com.salvai.snake.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.salvai.snake.SnakeIt
import com.salvai.snake.input.CatchBackKeyProcessor
import com.salvai.snake.utils.Constants
import com.salvai.snake.utils.Text

class LevelChooseScreen(val game: SnakeIt) : ScreenAdapter() {
    private val COLUMNS = 4
    private val TABS = 3
    private val levelTables: Array<Table?> = Array<Table?>()
    private val levelLabels: Array<Label?> = Array<Label?>()
    private val tabs: ButtonGroup<TextButton?> = ButtonGroup<TextButton?>()
    var width: Float = game.worldWidth
    var height: Float = game.worldHeight
    private var scrollPane: ScrollPane? = null


    init {

        game.stage!!.clear()
        game.setUpTopBar(Constants.SCREEN.LEVELCHOOSE)

        setUpTabs()

        setUpLevelTables()


        setUpScrollPane()

        setUpInputMultiplexer()


        game.stage!!.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(Constants.FADE_TIME)))
    }

    private fun setUpScrollPane() {
        scrollPane = ScrollPane(levelTables.get(game.selectedLevelTab), game.skin)
        scrollPane!!.setSize(width, height * 0.7f)
        scrollPane!!.setPosition(0f, height * 0.1f)
        game.stage!!.addActor(scrollPane)
    }


    private fun setUpInputMultiplexer() {
        Gdx.input.isCatchBackKey = true
        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(game.stage)
        multiplexer.addProcessor(game.topBarStage)
        multiplexer.addProcessor(CatchBackKeyProcessor(game, this)) // Your screen
        Gdx.input.inputProcessor = multiplexer
    }

    private fun setUpTabs() {
        val tableTabs = Table(game.skin)
        tableTabs.setSize(width, height * 0.1f)
        tableTabs.setPosition(0f, height * 0.8f)
        tableTabs.defaults().uniformX().spaceBottom(height * 0.1f).size(width * 0.33f, height * 0.05f)
        tableTabs.row()
        for (i in 0..<TABS) {
            val tabIndex = i
            val tab = TextButton(Text.LEVEL_TABS[i], game.skin)

            if (game.selectedLevelTab == i) tab.setChecked(true)

            tab.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    val changeTable: Action = object : Action() {
                        override fun act(delta: Float): Boolean {
                            scrollPane!!.setActor(levelTables.get(tabIndex))
                            //                            updateTabColor();
                            return true
                        }
                    }

                    scrollPane!!.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), changeTable, Actions.fadeIn(Constants.FADE_TIME)))

                    game.updateSelectedLevelTab(tabIndex)
                }
            })
            tabs.add(tab)

            tableTabs.add(tab)
        }
        game.stage!!.addActor(tableTabs)
    }


    private fun setUpLevelTables() {
        var levelCounter = 0
        for (i in 0..<TABS) {
            val levelTable = Table(game.skin)
            levelTable.defaults().space(0f, width * 0.05f, 0f, width * 0.05f)
            levelTable.row()
            for (j in 0..<Constants.MAX_LEVEL / TABS) {
                setUpLevelPreviews(levelTable, levelCounter)
                if ((j + 1) % COLUMNS == 0) {
                    levelTable.row()
                    setUpLevelLabels(levelTable, levelCounter)
                }
                levelCounter++
            }
            levelTables.add(levelTable)
        }
    }

    private fun setUpLevelPreviews(levelTable: Table, level: Int) {
        val levelPreview = Image(game.assetsManager!!.manager.get(Constants.LEVEL_PREVIEW + (level + 1) + ".png", Texture::class.java))
        levelPreview.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.level = level
                game.stage!!.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run {
                    game.setScreen(GameScreen(game))
                    dispose()
                }))
            }
        })
        levelTable.add(levelPreview).size(width * 0.2f, height * 0.2f)
    }

    private fun setUpLevelLabels(levelTable: Table, end: Int) {
        for (i in end - (COLUMNS - 1)..end) {
            val levelLabel = Label("" + game.highScores[i], game.skin, "level")
            levelLabel.setAlignment(Align.center)
            levelTable.add(levelLabel).size(width * 0.2f, height * 0.04f).spaceBottom(width * 0.05f)
            levelLabels.add(levelLabel)
        }
        levelTable.row()
    }


    override fun render(delta: Float) {
        setupScreen()
        game.draw(delta)
    }

    private fun setupScreen() {
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g, Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    override fun resize(width: Int, height: Int) {
        game.stage!!.viewport.update(width, height, true)
    }
}
