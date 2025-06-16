package com.nopalsoft.invaders

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

class DialogSignInGoogleGameServices(val game: GalaxyInvadersGame, var stage: Stage) {
    var dialogSignIn: Dialog? = null
    var dialogRate: Dialog? = null

    fun showDialogSignIn() {
        dialogSignIn = Dialog(Assets.languagesBundle?.get("sign_in"), Assets.styleDialogPause)
        val labelContents = Label(Assets.languagesBundle?.get("sign_in_with_google_to_share_your_scores_and_achievements_with_your_friends"), Assets.styleLabelDialog)
        labelContents.wrap = true

        dialogSignIn!!.contentTable.add(labelContents).width(300f).height(120f)

        val style = TextButtonStyle(Assets.buttonSignInUp, Assets.buttonSignInDown, null, Assets.font15)
        val buttonSignIn = TextButton(Assets.languagesBundle?.get("sign_in"), style)
        buttonSignIn.label.wrap = true
        val buttonNotNow = TextButton(Assets.languagesBundle?.get("not_now"), Assets.styleTextButton)

        buttonNotNow.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                dialogSignIn!!.hide()
            }
        })

        buttonSignIn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                dialogSignIn!!.hide()
            }
        })

        dialogSignIn!!.buttonTable.add(buttonSignIn).minWidth(140f).fill()
        dialogSignIn!!.buttonTable.add(buttonNotNow).minWidth(140f).fill()
        dialogSignIn!!.show(stage)
    }

    fun showDialogRate() {
        dialogRate = Dialog(Assets.languagesBundle?.get("please_rate_the_app"), Assets.styleDialogPause)
        val labelContent = Label(Assets.languagesBundle?.get("thank_you_for_playing_if_you_like_this_game_please"), Assets.styleLabelDialog)
        labelContent.wrap = true

        dialogRate!!.contentTable.add(labelContent).width(300f).height(150f)

        val rate = TextButton(Assets.languagesBundle?.get("rate"), Assets.styleTextButton)
        val buttonNotNow = TextButton(Assets.languagesBundle?.get("not_now"), Assets.styleTextButton)
        rate.height = 10f

        buttonNotNow.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                dialogRate!!.hide()
            }
        })

        rate.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                dialogRate!!.hide()
            }
        })

        dialogRate!!.buttonTable.add(rate).minWidth(140f).minHeight(40f).fill()
        dialogRate!!.buttonTable.add(buttonNotNow).minWidth(140f).minHeight(40f).fill()
        dialogRate!!.show(stage)
    }

    val isDialogShown: Boolean
        get() = stage.actors.contains(dialogRate, true) || stage.actors.contains(dialogSignIn, true)

    fun dismissAll() {
        if (stage.actors.contains(dialogRate, true)) dialogRate!!.hide()

        if (stage.actors.contains(dialogSignIn, true)) dialogSignIn!!.hide()
    }
}
