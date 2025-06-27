package io.github.masamune.dialog

import com.badlogic.gdx.utils.GdxRuntimeException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import kotlin.test.Test

class DialogTest {

    @Test
    fun `empty dialog should throw a DialogCfgException`() {
        shouldThrow<DialogCfgException> {
            dialog("testDialog") {}
        }
    }

    @Test
    fun `page without options should throw a PageCfgException`() {
        shouldThrow<PageCfgException> {
            dialog("testDialog") {
                page("First page") {}
            }
        }
    }

    @Test
    fun `dialog without exit action should throw a DialogCfgException`() {
        shouldThrow<DialogCfgException> {
            dialog("testDialog") {
                page("First page") {
                    option("Next", ActionNext)
                }
            }
        }
    }

    @Test
    fun `should create dialog with one page and an exit action`() {
        val dialog = dialog("testDialog") {
            page("First", "image", "caption") {
                option("Quit", ActionExit)
            }
        }

        dialog.name shouldBe "testDialog"
        dialog.pages.size shouldBe 1
        dialog.isFinished shouldBe false
        dialog.lastOptionIdx shouldBe -1
        val firstPage = dialog.pages.first()
        firstPage.pageIdx shouldBe 0
        firstPage.text shouldBe "First"
        firstPage.image shouldBe "image"
        firstPage.imageCaption shouldBe "caption"
        firstPage.options.size shouldBe 1
        val firstOption = firstPage.options.first()
        firstOption.text shouldBe "Quit"
        firstOption.action shouldBeSameInstanceAs ActionExit
    }

    @Test
    fun `trigger option of invalid index should throw an error`() {
        val dialog = dialog("testDialog") {
            page("First") {
                option("Quit", ActionExit)
            }
        }

        shouldThrow<GdxRuntimeException> {
            dialog.triggerOption(-1)
        }
    }

    @Test
    fun `trigger 'next' option of last page should throw an error`() {
        val dialog = dialog("testDialog") {
            page("First") {
                option("Next", ActionNext)
                option("Quit", ActionExit)
            }
        }

        shouldThrow<GdxRuntimeException> {
            dialog.triggerOption(0)
        }
    }

    @Test
    fun `trigger 'previous' option of first page should throw an error`() {
        val dialog = dialog("testDialog") {
            page("First") {
                option("Prev", ActionPrevious)
                option("Quit", ActionExit)
            }
        }

        shouldThrow<GdxRuntimeException> {
            dialog.triggerOption(0)
        }
    }

    @Test
    fun `trigger 'next' option of page 1 changes dialog to page 2`() {
        val dialog = dialog("testDialog") {
            page("First") {
                option("Next", ActionNext)
            }
            page("Second") {
                option("Quit", ActionExit)
            }
        }

        dialog.activePage.pageIdx shouldBe 0
        val actual = dialog.triggerOption(0)

        dialog.activePage.pageIdx shouldBe 1
        actual shouldBe false
    }

    @Test
    fun `trigger 'previous' option of page 2 changes dialog to page 1`() {
        val dialog = dialog("testDialog") {
            page("First") {
                option("Next", ActionNext)
            }
            page("Second") {
                option("Prev", ActionPrevious)
                option("Quit", ActionExit)
            }
        }

        dialog.triggerOption(0)
        dialog.activePage.pageIdx shouldBe 1
        val actual = dialog.triggerOption(0)

        dialog.activePage.pageIdx shouldBe 0
        actual shouldBe false
    }

    @Test
    fun `trigger 'goTo 2' option of page 1 changes dialog to page 2`() {
        val dialog = dialog("testDialog") {
            page("First") {
                option("GoTo", ActionGoTo(currentPageIdx + 1))
            }

            page("Second") {
                option("Prev", ActionPrevious)
                option("Quit", ActionExit)
            }
        }

        val actual = dialog.triggerOption(0)

        dialog.activePage.pageIdx shouldBe 1
        actual shouldBe false
    }

    @Test
    fun `trigger 'exit' option of a page returns true`() {
        val dialog = dialog("testDialog") {
            page("First") {
                option("Quit", ActionExit)
            }
        }

        val actual = dialog.triggerOption(0)

        dialog.activePage.pageIdx shouldBe 0
        dialog.isFinished shouldBe true
        dialog.lastOptionIdx shouldBe 0
        actual shouldBe true
    }

}
