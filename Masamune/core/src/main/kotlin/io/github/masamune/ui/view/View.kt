package io.github.masamune.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.masamune.event.Event
import io.github.masamune.event.EventListener
import io.github.masamune.event.UiBackEvent
import io.github.masamune.event.UiDownEvent
import io.github.masamune.event.UiLeftEvent
import io.github.masamune.event.UiRightEvent
import io.github.masamune.event.UiSelectEvent
import io.github.masamune.event.UiUpEvent
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.model.UIStats
import io.github.masamune.ui.model.ViewModel

abstract class View<T : ViewModel>(
    skin: Skin,
    val viewModel: T,
) : Table(skin), EventListener {

    open fun onUpPressed() = Unit
    open fun onDownPressed() = Unit
    open fun onRightPressed() = Unit
    open fun onLeftPressed() = Unit
    open fun onSelectPressed() = Unit
    open fun onBackPressed() = Unit

    fun i18nTxt(key: I18NKey): String = viewModel.i18nTxt(key)

    override fun onEvent(event: Event) {
        if (!isVisible) {
            return
        }

        when (event) {
            is UiUpEvent -> onUpPressed()
            is UiDownEvent -> onDownPressed()
            is UiRightEvent -> onRightPressed()
            is UiLeftEvent -> onLeftPressed()
            is UiSelectEvent -> onSelectPressed()
            is UiBackEvent -> onBackPressed()
            else -> Unit
        }
    }

    abstract fun registerOnPropertyChanges()
}

fun Map<UIStats, String>.of(stat: UIStats): String = this[stat] ?: "???"

fun Map<UIStats, String>.zeroIfMissing(stat: UIStats): String = this[stat] ?: "0"
