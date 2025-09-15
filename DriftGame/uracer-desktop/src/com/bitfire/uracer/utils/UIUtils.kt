package com.bitfire.uracer.utils

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.FitViewport
import com.bitfire.uracer.URacer
import com.bitfire.uracer.resources.Art

object UIUtils {

    const val DEBUG = false

    @JvmStatic
    fun newFittedStage() = Stage(FitViewport(ScaleUtils.PlayWidth.toFloat(), ScaleUtils.PlayHeight.toFloat()))

    @JvmStatic
    fun newTextButton(text: String, listener: ClickListener): TextButton {
        val btn = TextButton(text, Art.scrSkin)
        btn.addListener(listener)
        return btn
    }

    @JvmStatic
    fun newCheckBox(text: String, checked: Boolean, listener: ChangeListener?): CheckBox {
        val cb = CheckBox(" $text", Art.scrSkin)
        cb.setChecked(checked)
        if (listener != null) {
            cb.addListener(listener)
        }
        return cb
    }

    @JvmStatic
    fun newSelectBox(items: Array<String>, listener: ChangeListener?): SelectBox<String> {
        val sb = SelectBox<String>(Art.scrSkin)
        if (listener != null) {
            sb.addListener(listener)
        }

        sb.setItems(*items)
        return sb
    }

    @JvmStatic
    @JvmOverloads
    fun newListBox(items: Array<String>, listener: ChangeListener? = null): List<String> {
        val list = List<String>(Art.scrSkin)
        if (listener != null) {
            list.addListener(listener)
        }

        list.setItems(*items)
        return list
    }

    @JvmStatic
    fun newSlider(min: Float, max: Float, step: Float, value: Float, listener: ChangeListener?): Slider {
        val s = Slider(min, max, step, false, Art.scrSkin)
        s.value = value
        if (listener != null) {
            s.addListener(listener)
        }
        return s
    }

    @JvmStatic
    fun newLabel(text: String, wrap: Boolean): Label {
        val l = Label(text, Art.scrSkin)
        l.setWrap(wrap)
        return l
    }

    @JvmStatic
    fun newScrollPane() = ScrollPane(null, Art.scrSkin)

    @JvmStatic
    fun newButton(text: String, listener: ClickListener?): TextButton {
        val b = TextButton(text, Art.scrSkin)
        if (listener != null) {
            b.addListener(listener)
        }
        return b
    }

    @JvmStatic
    fun newTable(): Table {
        val t = Table()
        if (DEBUG) t.debug()
        return t
    }

    @JvmStatic
    fun newWindow(title: String): Window {
        val w = Window(title, Art.scrSkin)
        if (DEBUG) w.debug()
        return w
    }

    @JvmStatic
    fun newVersionInfoLabel() = newLabel("uRacer ${URacer.versionInfo}", false)

}
