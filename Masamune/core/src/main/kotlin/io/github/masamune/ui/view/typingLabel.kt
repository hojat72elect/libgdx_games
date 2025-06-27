package io.github.masamune.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.rafaskoberg.gdx.typinglabel.TypingLabel
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.defaultStyle
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@Scene2dDsl
@OptIn(ExperimentalContracts::class)
inline fun <S> KWidget<S>.typingLabel(
    text: CharSequence,
    style: String = defaultStyle,
    skin: Skin,
    init: (@Scene2dDsl TypingLabel).(S) -> Unit = {},
): TypingLabel {
    contract { callsInPlace(init, InvocationKind.EXACTLY_ONCE) }
    return actor(TypingLabel(text, skin, style), init)
}
