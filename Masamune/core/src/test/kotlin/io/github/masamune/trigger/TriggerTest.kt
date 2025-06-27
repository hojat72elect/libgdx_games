package io.github.masamune.trigger

import com.badlogic.gdx.utils.GdxRuntimeException
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.configureWorld
import io.github.masamune.dialog.DialogConfigurator
import io.github.masamune.event.EventService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.mockk
import kotlin.test.Test

class TriggerTest {

    private val dialogCfgMock = mockk<DialogConfigurator>(relaxed = true)
    private val eventServiceMock = mockk<EventService>(relaxed = true)
    private val testWorld = configureWorld {
        injectables {
            add(dialogCfgMock)
            add(eventServiceMock)
        }
    }
    private val testTriggeringEntity = Entity(0, 0U)
    private val testTriggerName = "testTrigger"

    @Test
    fun `trigger without actions should throw exception`() {
        shouldThrow<GdxRuntimeException> {
            trigger(testTriggerName, testWorld, testTriggeringEntity) {}
        }
    }

    @Test
    fun `test trigger creation with a single action`() {
        val script = trigger(testTriggerName, testWorld, testTriggeringEntity) {
            this.world shouldBe testWorld
            this.triggeringEntity shouldBe testTriggeringEntity

            actionRemove(Entity.NONE)
        }

        script.name shouldBe testTriggerName
        script.world shouldBe testWorld
        script.numActions shouldBe 1
    }

    @Test
    fun `test trigger creation with two actions`() {
        val script = trigger(testTriggerName, testWorld, testTriggeringEntity) {
            this.world shouldBe testWorld
            this.triggeringEntity shouldBe testTriggeringEntity

            actionRemove(Entity.NONE)
            actionDialog("dialog")
        }

        script.name shouldBe testTriggerName
        script.world shouldBe testWorld
        script.numActions shouldBe 2
        script.getAction<TriggerActionRemoveEntity>(0) shouldNotBe null
        script.getAction<TriggerActionDialog>(1) shouldNotBe null
    }

    @Test
    fun `test remove entity action`() {
        val entityToRemove = Entity(1, 0U)

        val script = trigger(testTriggerName, testWorld, testTriggeringEntity) {
            actionRemove(entityToRemove)
        }

        script.getAction<TriggerActionRemoveEntity>(0).entity shouldBe entityToRemove
    }

    @Test
    fun `test dialog action`() {
        val dialogCloseAction: (Int) -> Unit = {}

        val script = trigger(testTriggerName, testWorld, testTriggeringEntity) {
            actionDialog("dialog", withSound = false, dialogCloseAction)
        }

        val action = script.getAction<TriggerActionDialog>(0)
        action.triggeringEntity shouldBe testTriggeringEntity
        action.eventService shouldBe eventServiceMock
        action.closeAction shouldBe dialogCloseAction
    }
}
