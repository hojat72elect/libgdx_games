package com.nopalsoft.zombiekiller.scene2d

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.Settings
import com.nopalsoft.zombiekiller.game.GameScreen
import com.nopalsoft.zombiekiller.screens.Screens

class DialogSelectLevel(currentScreen: Screens) : Dialog(currentScreen, 650f, 450f, 20f, Assets.backgroundBigWindow) {
    var contenedor: Table
    var totalSkulls: Int = 0 // Cada nivel necesita (nivel * 2) skulls para ser accecible.

    init {
        setCloseButton(570f, 320f, 65f)

        val lbShop = Label(idiomas!!.get("select_a_level"), Assets.labelStyleGrande)
        lbShop.setPosition(getWidth() / 2f - lbShop.getWidth() / 2f, 355f)
        lbShop.setAlignment(Align.center)
        lbShop.setFontScale(1.2f)
        addActor(lbShop)

        contenedor = Table()
        val scroll = ScrollPane(contenedor, Assets.styleScrollPane)
        scroll.setFadeScrollBars(false)
        scroll.setSize(465f, 280f)
        scroll.setPosition(91f, 53f)
        scroll.variableSizeKnobs = false

        for (i in Settings.arrSkullsMundo.indices) {
            totalSkulls += Settings.arrSkullsMundo[i]
        }

        if (Settings.isTest) totalSkulls += Settings.arrSkullsMundo.size * 3

        var level = 0
        contenedor.defaults().pad(7f, 5f, 7f, 5f)
        for (col in 0..<Settings.NUM_MAPS) {
            contenedor.add<Button?>(getLevelButton(level)).size(93f, 113f)
            level++

            if (level % 4 == 0) contenedor.row()
        }

        addActor(scroll)
    }

    fun getLevelButton(level: Int): Button {
        val button: TextButton

        val skullsToNextLevel = (level * 2.5f).toInt()

        if (totalSkulls < skullsToNextLevel) {
            button = TextButton("" + (level + 1), Assets.styleTextButtontLevelLocked)
            button.setDisabled(true)
        } else {
            button = TextButton("" + (level + 1), Assets.styleTextButtontLevel)

            val stars = Settings.arrSkullsMundo[level]

            val starTable = Table()
            starTable.defaults().pad(1f)
            if (stars >= 0) {
                for (star in 0..2) {
                    if (stars > star) {
                        starTable.add<Image?>(Image(Assets.itemSkull)).width(20f).height(20f)
                    } else {
                        starTable.add<Image?>(Image(Assets.upgradeOff)).width(20f).height(20f)
                    }
                }
            }

            button.row()
            button.add<Table?>(starTable).height(30f).padBottom(10f)
        }

        button.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (button.isDisabled()) {
                    DialogWarning(screen, idiomas!!.format("warning_level_window", skullsToNextLevel)).show(screen.stage!!)
                } else {
                    hide()
                    screen.changeScreenWithFadeOut(GameScreen::class.java, level, game)
                }
            }
        })

        screen.addPressEffect(button)

        return button
    }

}
