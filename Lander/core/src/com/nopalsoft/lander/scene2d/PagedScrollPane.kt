package com.nopalsoft.lander.scene2d

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Array

class PagedScrollPane : ScrollPane(null) {
    private var wasPanDragFling = false

    private var content = Table()

    init {
        content.defaults().space(250f)
        super.setWidget(content)
    }

    fun addPage(page: Actor?) {
        content.add<Actor?>(page).expandY().fillY()
    }

    override fun act(delta: Float) {
        super.act(delta)

        if (wasPanDragFling && !isPanning && !isDragging && !isFlinging) {
            wasPanDragFling = false

            scrollToPage()
        } else {
            if (isPanning || isDragging || isFlinging) {
                wasPanDragFling = true
            }
        }
    }

    override fun setWidth(width: Float) {
        super.setWidth(width)

        for (cell in content.cells) {
            cell.width(width)
        }

        content.invalidate()
    }

    fun setPageSpacing(pageSpacing: Float) {
        content.defaults().space(pageSpacing)

        for (cell in content.cells) {
            cell.space(pageSpacing)
        }

        content.invalidate()
    }

    private fun scrollToPage() {
        val width = getWidth()

        val scrollX = getScrollX()

        val maxX = getMaxX()

        if (scrollX >= maxX || scrollX <= 0) return

        val pages: Array<Actor> = content.getChildren()

        var pageX = 0f

        var pageWidth = 0f
        if (pages.size > 0) {
            for (a in pages) {
                pageX = a.getX()
                pageWidth = a.getWidth()
                if (scrollX < (pageX + pageWidth * 0.5)) {
                    break
                }
            }

            setScrollX(MathUtils.clamp(pageX - (width - pageWidth) / 2, 0f, maxX))
        }
    }
}
