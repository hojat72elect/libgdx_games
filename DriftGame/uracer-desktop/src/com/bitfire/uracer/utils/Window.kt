package com.bitfire.uracer.utils

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.BitmapFontCache
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.Align
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * A table that can be dragged and act as a modal window. The top padding is used as the window's title height.
 *
 * The preferred size of a window is the preferred size of the title text and the children as laid out by the table. After adding
 * children to the window, it can be convenient to call [.pack] to size the window to the size of the children.
 */
open class Window(title: String, style: WindowStyle) : Table() {

    private val tmpColor = Color()
    protected var titleCache: BitmapFontCache? = null
    protected var titleAlignment = Align.center
    var isMovable = true
    var isModal = false
    var isResizable = false
    var resizeBorder = 8
    var dragging = false
    var keepWithinStage = true
    open var buttonTable: Table? = null
    private var style: WindowStyle? = null
    private var title: String?

    constructor(title: String, skin: Skin) : this(title, skin.get<WindowStyle>(WindowStyle::class.java)) {
        setSkin(skin)
    }

    init {
        this.title = title
        setTouchable(Touchable.enabled)
        clip = true
        setStyle(style)
        setWidth(150f)
        setHeight(150f)
        setTitle(title)

        buttonTable = Table()
        addActor(buttonTable)

        addCaptureListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                toFront()
                return false
            }
        })

        addListener(object : InputListener() {
            var edge: Int = 0
            var startX: Float = 0f
            var startY: Float = 0f
            var lastX: Float = 0f
            var lastY: Float = 0f

            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                if (button == 0) {
                    var border = resizeBorder
                    val width = getWidth()
                    val height = getHeight()
                    edge = 0
                    if (isResizable) {
                        if (x < border) edge = edge or Align.left
                        if (x > width - border) edge = edge or Align.right
                        if (y < border) edge = edge or Align.bottom
                        if (y > height - border) edge = edge or Align.top
                        if (edge != 0) border += 25
                        if (x < border) edge = edge or Align.left
                        if (x > width - border) edge = edge or Align.right
                        if (y < border) edge = edge or Align.bottom
                        if (y > height - border) edge = edge or Align.top
                    }
                    if (isMovable && edge == 0 && y <= height && y >= height - getPadTop() && x >= 0 && x <= width) edge = MOVE
                    dragging = edge != 0
                    startX = x
                    startY = y
                    lastX = x
                    lastY = y
                }
                return edge != 0 || isModal
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                dragging = false
            }

            override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                if (!dragging) return
                var width = getWidth()
                var height = getHeight()
                var windowX = getX()
                var windowY = getY()

                val minWidth = getMinWidth()
                val minHeight = getMinHeight()
                val stage = getStage()
                val clampPosition = keepWithinStage && getParent() === stage.root

                if ((edge and MOVE) != 0) {
                    val amountX = x - startX
                    val amountY = y - startY
                    windowX += amountX
                    windowY += amountY
                }
                if ((edge and Align.left) != 0) {
                    var amountX = x - startX
                    if (width - amountX < minWidth) amountX = -(minWidth - width)
                    if (clampPosition && windowX + amountX < 0) amountX = -windowX
                    width -= amountX
                    windowX += amountX
                }
                if ((edge and Align.bottom) != 0) {
                    var amountY = y - startY
                    if (height - amountY < minHeight) amountY = -(minHeight - height)
                    if (clampPosition && windowY + amountY < 0) amountY = -windowY
                    height -= amountY
                    windowY += amountY
                }
                if ((edge and Align.right) != 0) {
                    var amountX = x - lastX
                    if (width + amountX < minWidth) amountX = minWidth - width
                    if (clampPosition && windowX + width + amountX > stage.width) amountX = stage.width - windowX - width
                    width += amountX
                }
                if ((edge and Align.top) != 0) {
                    var amountY = y - lastY
                    if (height + amountY < minHeight) amountY = minHeight - height
                    if (clampPosition && windowY + height + amountY > stage.height) amountY = stage.height - windowY - height
                    height += amountY
                }
                lastX = x
                lastY = y
                setBounds(windowX.roundToInt().toFloat(), windowY.roundToInt().toFloat(), width.roundToInt().toFloat(), height.roundToInt().toFloat())
            }

            override fun mouseMoved(event: InputEvent?, x: Float, y: Float): Boolean {
                return isModal
            }

            override fun scrolled(event: InputEvent?, x: Float, y: Float, amount: Int): Boolean {
                return isModal
            }

            override fun keyDown(event: InputEvent?, keycode: Int): Boolean {
                return isModal
            }

            override fun keyUp(event: InputEvent?, keycode: Int): Boolean {
                return isModal
            }

            override fun keyTyped(event: InputEvent?, character: Char): Boolean {
                return isModal
            }
        })
    }

    fun setStyle(style: WindowStyle) {
        this.style = style
        background = style.background
        titleCache = BitmapFontCache(style.titleFont)
        titleCache!!.color = style.titleFontColor
        if (title != null) setTitle(title!!)
        invalidateHierarchy()
    }

    fun keepWithinStage() {
        if (!keepWithinStage) return
        val stage = getStage()
        if (getParent() === stage.root) {
            val parentWidth = stage.width
            val parentHeight = stage.height
            if (getX() < 0) setX(0f)
            if (right > parentWidth) setX(parentWidth - getWidth())
            if (getY() < 0) setY(0f)
            if (top > parentHeight) setY(parentHeight - getHeight())
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        keepWithinStage()

        if (style!!.stageBackground != null) {
            val color = getColor()
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha)
            val stage = getStage()
            stageToLocalCoordinates(tmpPosition.set(0f, 0f))
            stageToLocalCoordinates(tmpSize.set(stage.width, stage.height))
            style!!.stageBackground!!.draw(batch, getX() + tmpPosition.x, getY() + tmpPosition.y, getX() + tmpSize.x, getY() + tmpSize.y)
        }

        super.draw(batch, parentAlpha)
    }

    override fun drawBackground(batch: Batch?, parentAlpha: Float, x: Float, y: Float) {
        var x = x
        var y = y
        val width = getWidth()
        val height = getHeight()
        val padTop = getPadTop()

        super.drawBackground(batch, parentAlpha, x, y)

        buttonTable?.getColor()?.a = getColor().a
        buttonTable?.pack()
        buttonTable?.setPosition(width - (buttonTable?.getWidth() ?: 0F), min(height - padTop, height - (buttonTable?.getHeight() ?: 0F)))
        buttonTable?.draw(batch, parentAlpha)

        y += height
        val bounds = titleCache!!.bounds
        x += if ((titleAlignment and Align.left) != 0) getPadLeft()
        else if ((titleAlignment and Align.right) != 0) width - bounds.width - getPadRight()
        else (width - bounds.width) / 2

        if ((titleAlignment and Align.top) == 0) {
            y -= if ((titleAlignment and Align.bottom) != 0) padTop - bounds.height
            else (padTop - bounds.height) / 2
        }
        titleCache!!.setColors(tmpColor.set(getColor()).mul(style!!.titleFontColor))
        titleCache!!.setPosition(x, y - 15F) // HACK for Kenney's skin only!!
        titleCache!!.draw(batch, parentAlpha)
    }

    override fun hit(x: Float, y: Float, touchable: Boolean): Actor? {
        val hit = super.hit(x, y, touchable)
        if (hit == null && isModal && (!touchable || getTouchable() == Touchable.enabled)) return this
        return hit
    }

    fun setTitle(title: String) {
        this.title = title
        titleCache!!.setMultiLineText(title, 0F, 0F)
    }

    val titleWidth = titleCache!!.bounds.width

    override fun getPrefWidth() = max(super.getPrefWidth(), this.titleWidth + getPadLeft() + getPadRight())

    /**
     * The style for a window, see [Window].
     */
    class WindowStyle {
        var background: Drawable? = null
        var titleFont: BitmapFont? = null
        var titleFontColor = Color(1F, 1F, 1F, 1F)
        var stageBackground: Drawable? = null
    }

    companion object {
        private val tmpPosition = Vector2()
        private val tmpSize = Vector2()
        private const val MOVE = 1 shl 5
    }
}
