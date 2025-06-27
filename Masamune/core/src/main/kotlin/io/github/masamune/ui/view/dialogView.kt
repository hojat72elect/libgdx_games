package io.github.masamune.ui.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Align
import com.rafaskoberg.gdx.typinglabel.TypingLabel
import io.github.masamune.ui.model.DialogUiContent
import io.github.masamune.ui.model.DialogViewModel
import io.github.masamune.ui.widget.FrameImage
import io.github.masamune.ui.widget.OptionTable
import io.github.masamune.ui.widget.frameImage
import io.github.masamune.ui.widget.optionTable
import ktx.actors.txt
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.defaultStyle
import ktx.scene2d.label
import ktx.scene2d.table
import ktx.scene2d.verticalGroup

data class DialogViewStyle(
    val background: Drawable = BaseDrawable(),
    val imageFrame: Drawable = BaseDrawable(),
    val imageCaptionStyle: String = defaultStyle,
    val contentStyle: String = defaultStyle,
    val optionStyle: String = defaultStyle,
)

/**
 * DialogView is a 2x2 table where:
 * - top left is a vertical group with an image stack (frame+image) and an image caption
 * - top right is the main content which is a typing label
 * - bottom left is an empty cell
 * - bottom right is another table with a select image and an option text per row
 *
 * ```
 * +-------------+----------------+
 * | IMAGE       | CONTENT LABEL  |
 * |             |                |
 * | CAPTION_LBL |                |
 * +-------------+----------------+
 * |             | OPTION 1       |
 * |             | OPTION 2       |
 * +-------------+----------------+
 * ```
 */
@Scene2dDsl
class DialogView(
    model: DialogViewModel,
    skin: Skin,
    styleName: String = defaultStyle,
) : View<DialogViewModel>(skin, model), KTable {

    private val style = skin[styleName, DialogViewStyle::class.java]

    private val imageGroup: VerticalGroup
    private val image: FrameImage
    private val imageCaption: Label

    private val content: TypingLabel

    private val optionTable: OptionTable

    init {
        setFillParent(true)

        // nested inner table to center the content of the dialog automatically
        table(skin) {
            background = this@DialogView.style.background

            // first cell = vertical group for an image and image caption
            this@DialogView.imageGroup = verticalGroup { vGroupCell ->
                align(Align.topLeft)
                expand().fill()

                // image is a stack of an image frame + the image itself
                this@DialogView.image = frameImage(skin, this@DialogView.style.imageFrame)

                this@DialogView.imageCaption = label("", this@DialogView.style.imageCaptionStyle, skin) {
                    setAlignment(Align.top, Align.center)
                    wrap = false
                }

                vGroupCell.padLeft(5f).padRight(15f).fill().minWidth(MIN_IMAGE_WIDTH)
            }

            // second cell is the main content label
            this@DialogView.content = typingLabel("", this@DialogView.style.contentStyle, skin) { contentCell ->
                val defaultTextColor = "#695454"
                defaultToken = "{COLOR=$defaultTextColor}"
                clearColor.set(Color.valueOf(defaultTextColor))

                setAlignment(Align.topLeft)
                wrap = true
                contentCell.grow().minWidth(MIN_CONTENT_WIDTH).minHeight(MIN_CONTENT_HEIGHT).padBottom(10f)
            }
            row()

            // third cell (=bottom left) is an empty cell
            add()
            // fourth cell is the option table
            this@DialogView.optionTable = optionTable(skin) { optionTableCell ->
                optionTableCell.fill().align(Align.left)
            }
        }

        registerOnPropertyChanges()
    }

    override fun registerOnPropertyChanges() {
        viewModel.onPropertyChange(DialogViewModel::content) { dialogContent ->
            if (dialogContent == DialogUiContent.EMPTY_CONTENT) {
                isVisible = false
                return@onPropertyChange
            }

            isVisible = true
            val (txt, options, image, caption) = dialogContent
            text(txt)
            image(image, caption)
            optionTable.clearOptions()
            options.forEach { optionTable.option(it) }
        }
    }

    fun text(txt: String) {
        content.restart("{SLOW}{FADE}$txt")
    }

    fun image(drawableName: String?, caption: String? = null) {
        if (drawableName.isNullOrBlank()) {
            imageGroup.isVisible = false
            imageGroup.inCell.width(0f)
        } else {
            imageGroup.isVisible = true
            image.imageDrawable(drawableName)
            imageCaption.txt = caption?.replace(' ', '\n') ?: ""
            imageGroup.inCell.minWidth(MIN_IMAGE_WIDTH)
        }
    }

    override fun onUpPressed() {
        if (optionTable.nextOption()) {
            viewModel.playSndMenuClick()
        }
    }

    override fun onDownPressed() {
        if (optionTable.prevOption()) {
            viewModel.playSndMenuClick()
        }
    }

    override fun onSelectPressed() {
        if (!content.hasEnded()) {
            content.skipToTheEnd()
        } else {
            viewModel.triggerOption(optionTable.selectedOption)
        }
    }

    companion object {
        private const val MIN_IMAGE_WIDTH = 115f
        private const val MIN_CONTENT_WIDTH = 482f
        private const val MIN_CONTENT_HEIGHT = 120f
    }
}

@Scene2dDsl
fun <S> KWidget<S>.dialogView(
    model: DialogViewModel,
    skin: Skin,
    style: String = defaultStyle,
    init: (@Scene2dDsl DialogView).(S) -> Unit = {},
): DialogView = actor(DialogView(model, skin, style), init)
