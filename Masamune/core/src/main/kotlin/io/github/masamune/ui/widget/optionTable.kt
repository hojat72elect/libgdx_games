package io.github.masamune.ui.widget

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.defaultStyle
import ktx.scene2d.scene2d

@Scene2dDsl
class OptionTable(
    skin: Skin,
    options: List<String>,
) : Table(skin) {

    var selectedOption: Int = 0
        private set

    val numOptions: Int
        get() = children.size

    init {
        align(Align.left)
        options.forEach { option(it) }
    }

    fun clearOptions() {
        clear()
        selectedOption = 0
    }

    fun option(text: String, userObject: Any? = null, optionStyle: String = defaultStyle) {
        val option = scene2d.dialogOption(text, skin, optionStyle) {
            this.userObject = userObject
            select(!this@OptionTable.hasChildren())
        }
        add(option).uniformX().left().fillX().padBottom(5f).row()
    }

    fun prevOption(): Boolean = selectOption(selectedOption - 1)

    fun nextOption(): Boolean = selectOption(selectedOption + 1)

    fun lastOption(): Boolean = selectOption(children.size - 1)

    /**
     * Selects the first option of the [OptionTable].
     */
    fun firstOption(): Boolean = selectOption(0)

    private fun selectOption(idx: Int): Boolean {
        val realIdx = when {
            idx < 0 -> children.size - 1
            idx >= children.size -> 0
            else -> idx
        }

        if (realIdx == selectedOption) {
            return false
        }

        (this.getChild(selectedOption) as DialogOptionWidget).select(false)
        selectedOption = realIdx
        (this.getChild(selectedOption) as DialogOptionWidget).select(true)
        return true
    }

    inline fun <reified T : Any> selectedUserObject(): T {
        return getChild(selectedOption).userObject as T
    }

    fun stopSelectAnimation() {
        (this.getChild(selectedOption) as DialogOptionWidget).stopSelectAnimation()
    }

    fun resumeSelectAnimation() {
        (this.getChild(selectedOption) as DialogOptionWidget).resumeSelectAnimation()
    }

}

@Scene2dDsl
fun <S> KWidget<S>.optionTable(
    skin: Skin,
    options: List<String> = emptyList(),
    init: (@Scene2dDsl OptionTable).(S) -> Unit = {},
): OptionTable = actor(OptionTable(skin, options), init)
