package io.github.masamune.dialog

import ktx.app.gdxError

sealed interface Action

data object ActionNext : Action

data object ActionExit : Action

data object ActionPrevious : Action

data class ActionGoTo(val pageIdx: Int) : Action

data class Option(
    val text: String,
    val action: Action,
)

data class Page(
    val pageIdx: Int,
    val text: String,
    val options: List<Option>,
    val image: String? = null,
    val imageCaption: String? = null,
)

data class Dialog(val name: String, val pages: List<Page>) {
    var activePage = pages.first()
        private set

    var isFinished = false
        private set

    var lastOptionIdx: Int = -1
        private set

    fun triggerOption(optionIdx: Int): Boolean {
        if (optionIdx !in activePage.options.indices) {
            gdxError("Dialog $name with page ${activePage.pageIdx} has no option with index $optionIdx")
        }

        lastOptionIdx = optionIdx
        when (val triggerAction = activePage.options[optionIdx].action) {
            is ActionExit -> {
                isFinished = true
                return true
            }

            is ActionNext -> goToPage(activePage.pageIdx + 1)
            is ActionPrevious -> goToPage(activePage.pageIdx - 1)
            is ActionGoTo -> goToPage(triggerAction.pageIdx)
        }

        return false
    }

    private fun goToPage(pageIdx: Int) {
        if (pageIdx !in pages.indices) {
            gdxError("Dialog $name with page ${activePage.pageIdx} links to invalid page $pageIdx")
        }

        activePage = pages[pageIdx]
    }

    override fun toString(): String {
        return "Dialog(name='$name', isFinished=$isFinished, lastOptionIdx=$lastOptionIdx)"
    }

}
