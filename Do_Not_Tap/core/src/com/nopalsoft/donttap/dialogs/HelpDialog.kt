package com.nopalsoft.donttap.dialogs

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.donttap.Assets
import com.nopalsoft.donttap.screens.Screens

class HelpDialog(var screen: Screens) : Group() {

    var dim: Image? = null

    init {

        setSize(430f, 460f)
        setOrigin(getWidth() / 2f, getHeight() / 2f)
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 200f)

        val background = Image(Assets.scoresBackgroundDrawable)
        background.setSize(getWidth(), getHeight())
        addActor(background)
        getColor().a = 0f

        val tableHelp = Table()
        tableHelp.setFillParent(true)

        val scrollStyle = ScrollPaneStyle(
            null, null, null,
            null, Assets.scoresBackgroundDrawable
        )
        val scroll = ScrollPane(tableHelp, scrollStyle)
        scroll.setSize(getWidth(), getHeight() - 50)
        scroll.setPosition(0f, 50f)

        val labelClassic = Label("Classic:", Assets.labelStyleBlack)
        labelClassic.setWrap(true)

        val labelTime = Label("Time trial:", Assets.labelStyleBlack)
        labelTime.setWrap(true)

        val labelEndless = Label("Endless:", Assets.labelStyleBlack)
        labelEndless.setWrap(true)

        val lbHelpClassic = Label(
            "Tap 100 tiles as fast as you can",
            Assets.labelStyleSmall
        )
        lbHelpClassic.setWrap(true)

        val labelHelpTime = Label(
            "Tap as fast as you can for 1 minute",
            Assets.labelStyleSmall
        )
        labelHelpTime.setWrap(true)

        val labelHelpEndless = Label(
            "How many tiles can you tap?",
            Assets.labelStyleSmall
        )
        labelHelpEndless.setWrap(true)

        tableHelp.add<Label?>(labelClassic).left().padLeft(5f).width(135f)
        tableHelp.add<Label?>(lbHelpClassic).left().expandX().fill()

        tableHelp.row().padTop(15f)
        tableHelp.add<Label?>(labelTime).left().padLeft(5f).width(135f)
        tableHelp.add<Label?>(labelHelpTime).left().expandX().fill()

        tableHelp.row().padTop(15f)
        tableHelp.add<Label?>(labelEndless).left().padLeft(5f).width(135f)
        tableHelp.add<Label?>(labelHelpEndless).left().expandX().fill()

        val buttonOk = TextButton(
            "OK",
            Assets.textButtonStyleSmall
        )
        buttonOk.setPosition(getWidth() / 2f - buttonOk.getWidth() / 2f, 5f)
        screen.addPressEffect(buttonOk)
        buttonOk.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
            }
        })

        addAction(
            Actions.sequence(
                Actions.alpha(1f, fadeDuration),
                Actions.run {
                    addActor(scroll)
                    addActor(buttonOk)
                }
            )
        )
    }

    fun show(stage: Stage) {
        dim = Image(Assets.blackPixel)
        dim!!.setFillParent(true)
        dim!!.getColor().a = 0f
        dim!!.addAction(Actions.alpha(.7f, fadeDuration - .5f))

        stage.addActor(dim)

        stage.addActor(this)
    }

    fun hide() {
        dim!!.addAction(
            Actions.sequence(
                Actions.alpha(0f, fadeDuration - .5f),
                Actions.removeActor()
            )
        )
        addAction(
            Actions.sequence(
                Actions.alpha(0f, fadeDuration),
                Actions.removeActor()
            )
        )
    }

    companion object {
        var fadeDuration: Float = 0.25f
    }
}
