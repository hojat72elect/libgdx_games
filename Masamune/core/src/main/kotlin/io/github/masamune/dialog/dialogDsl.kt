package io.github.masamune.dialog

import com.badlogic.gdx.utils.GdxRuntimeException

class DialogCfgException(message: String) : GdxRuntimeException(message)

class PageCfgException(page: Page, message: String) : GdxRuntimeException("Page ${page.pageIdx}: $message")

@DslMarker
annotation class DialogDsl

fun dialog(name: String, cfg: DialogCfg.() -> Unit): Dialog {
    val pages = mutableListOf<Page>()
    DialogCfg(pages).apply(cfg)
    if (pages.isEmpty()) {
        throw DialogCfgException("Dialog $name must have at least one page.")
    }
    val allOptions = pages.flatMap { it.options }
    if (allOptions.none { it.action is ActionExit }) {
        throw DialogCfgException("Dialog $name must have an exit action.")
    }

    return Dialog(name, pages)
}

@DialogDsl
class DialogCfg(private val pages: MutableList<Page>) {
    fun page(text: String, image: String? = null, caption: String? = null, cfg: PageCfg.() -> Unit): Page {
        val options = mutableListOf<Option>()
        val pageIdx = pages.size
        PageCfg(pageIdx, options).apply(cfg)
        val page = Page(pageIdx, text, options, image, caption)
        if (options.isEmpty()) {
            throw PageCfgException(page, "Page must have at least one option")
        }

        pages += page
        return page
    }
}

@DialogDsl
class PageCfg(val currentPageIdx: Int, private val options: MutableList<Option>) {
    fun option(text: String, action: Action) {
        options += Option(text, action)
    }
}
