package com.bitfire.uracer.utils

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener
import com.badlogic.gdx.utils.ObjectMap
import kotlin.math.roundToInt

/**
 * Displays a dialog, which is a modal window containing a content table with a button table underneath it. Methods are provided
 * to add a label to the content table and buttons to the button table, but any widgets can be added. When a button is clicked,
 * [result] is called and the dialog is removed from the stage.
 */
open class Dialog(title: String, private val skin: Skin, windowStyleName: String) : Window(title, skin.get(windowStyleName, WindowStyle::class.java)) {

    private var contentTable: Table? = null
    override var buttonTable: Table? = null
    var values = ObjectMap<Actor, Any?>()
    var cancelHide = false
    var previousKeyboardFocus: Actor? = null
    var previousScrollFocus: Actor? = null
    var ignoreTouchDown = object : InputListener() {
        override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
            event.cancel()
            return false
        }
    }

    init {
        setSkin(skin)
        initialize()
    }

    private fun initialize() {
        isModal = true

        defaults().space(6F)
        add(Table(skin).also { contentTable = it }).expand().fill()
        row()
        add(Table(skin).also { buttonTable = it })

        contentTable!!.defaults().space(6F)
        buttonTable!!.defaults().space(6F)

        buttonTable!!.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                var currentActor = actor
                if (!values.containsKey(currentActor)) return
                while (currentActor.getParent() !== buttonTable) currentActor = currentActor.getParent()
                result(values.get(currentActor))
                if (!cancelHide) hide()
                cancelHide = false
            }
        })

        addListener(object : FocusListener() {
            override fun keyboardFocusChanged(event: FocusEvent, actor: Actor, focused: Boolean) {
                if (!focused) focusChanged(event)
            }

            override fun scrollFocusChanged(event: FocusEvent, actor: Actor, focused: Boolean) {
                if (!focused) focusChanged(event)
            }

            private fun focusChanged(event: FocusEvent) {
                val stage = getStage()
                if (isModal && stage != null && stage.root.getChildren().size > 0 && stage.root.getChildren().peek() === this@Dialog) {
                    // The Dialog is the top most actor.
                    val newFocusedActor = event.relatedActor
                    if (newFocusedActor != null && !newFocusedActor.isDescendantOf(this@Dialog)) event.cancel()
                }
            }
        })
    }

    fun getContentTable() = contentTable!!

    /**
     * Adds a label to the content table. The dialog must have been constructed with a skin to use this method.
     */
    fun text(text: String) = text(text, skin.get(LabelStyle::class.java))

    /**
     * Adds a label to the content table.
     */
    fun text(text: String, labelStyle: LabelStyle) = text(Label(text, labelStyle))

    /**
     * Adds the given Label to the content table
     */
    fun text(label: Label): Dialog {
        contentTable!!.add(label)
        return this
    }

    /**
     * Adds a text button to the button table. Null will be passed to [result] if this button is clicked. The dialog
     * must have been constructed with a skin to use this method.
     */
    @JvmOverloads
    fun button(text: String, buttonListener: Any? = null) = button(text, buttonListener, skin.get(TextButtonStyle::class.java))

    /**
     * Adds a text button to the button table.
     * @param `object` The object that will be passed to [result] if this button is clicked. May be null.
     */
    fun button(text: String, buttonListener: Any?, buttonStyle: TextButtonStyle) = button(TextButton(text, buttonStyle), buttonListener)

    /**
     * Adds the given button to the button table.
     *
     * @param `object` The object that will be passed to [result] if this button is clicked. May be null.
     */
    @JvmOverloads
    fun button(button: Button, buttonListener: Any? = null): Dialog {
        buttonTable!!.add(button)
        setObject(button, buttonListener)
        return this
    }

    /**
     * [Packs][pack] the dialog and adds it to the stage, centered.
     */
    fun show(stage: Stage): Dialog {
        clearActions()
        removeCaptureListener(ignoreTouchDown)

        previousKeyboardFocus = null
        var actor = stage.keyboardFocus
        if (actor != null && !actor.isDescendantOf(this)) previousKeyboardFocus = actor

        previousScrollFocus = null
        actor = stage.scrollFocus
        if (actor != null && !actor.isDescendantOf(this)) previousScrollFocus = actor

        setPosition(((stage.width - getWidth()) / 2).roundToInt().toFloat(), ((stage.height - getHeight()) / 2).roundToInt().toFloat())
        stage.addActor(this)
        stage.keyboardFocus = this
        stage.scrollFocus = this
        if (fadeDuration > 0) {
            getColor().a = 0F
            addAction(Actions.fadeIn(fadeDuration, Interpolation.fade))
        }
        return this
    }

    /**
     * Hides the dialog. Called automatically when a button is clicked. The default implementation fades out the dialog over
     * [.fadeDuration] seconds and then removes it from the stage.
     */
    fun hide() {
        val stage = getStage()
        if (stage != null) {
            if (previousKeyboardFocus != null && previousKeyboardFocus!!.stage == null) previousKeyboardFocus = null
            var actor = stage.keyboardFocus
            if (actor == null || actor.isDescendantOf(this)) stage.keyboardFocus = previousKeyboardFocus

            if (previousScrollFocus != null && previousScrollFocus!!.stage == null) previousScrollFocus = null
            actor = stage.scrollFocus
            if (actor == null || actor.isDescendantOf(this)) stage.scrollFocus = previousScrollFocus
        }
        if (fadeDuration > 0) {
            addCaptureListener(ignoreTouchDown)
            addAction(
                Actions.sequence(
                    Actions.fadeOut(fadeDuration, Interpolation.fade), Actions.removeListener(ignoreTouchDown, true),
                    Actions.removeActor()
                )
            )
        } else remove()
    }

    fun setObject(actor: Actor, actorInfo: Any?) {
        values.put(actor, actorInfo)
    }

    /**
     * If this key is pressed, [result] is called with the specified object.
     */
    fun key(keycode: Int, keyListener: Any?): Dialog {
        addListener(object : InputListener() {
            override fun keyDown(event: InputEvent, keycode2: Int): Boolean {
                if (keycode == keycode2) {
                    result(keyListener)
                    if (!cancelHide) hide()
                    cancelHide = false
                }
                return false
            }
        })
        return this
    }

    /**
     * Called when a button is clicked.
     *
     * @param listener The object specified when the button was added.
     */
    protected open fun result(listener: Any?) {
    }

    companion object {
        /**
         * The time in seconds that dialogs will fade in and out. Set to zero to disable fading.
         */
        @JvmField
        var fadeDuration = 0.4F
    }
}
