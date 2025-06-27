package io.github.masamune.event

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.configureWorld
import io.github.masamune.ui.model.ViewModel
import io.github.masamune.ui.view.View
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ktx.collections.gdxArrayOf
import kotlin.test.Test

class EventServiceTest {

    private abstract class ListenerSystem : IntervalSystem(), EventListener

    private fun testWorld(): Pair<ListenerSystem, World> {
        val normalSystem = mockk<IntervalSystem>()
        every { normalSystem.onInit() } returns Unit
        val listenerSystem = mockk<ListenerSystem>()
        every { listenerSystem.onInit() } returns Unit
        val world = configureWorld {
            systems {
                add(normalSystem)
                add(listenerSystem)
            }
        }
        return Pair(listenerSystem, world)
    }

    @Test
    fun `add a single listener`() {
        val listener = mockk<EventListener>()
        val service = EventService()

        service += listener

        service.numListeners shouldBe 1
        (listener in service) shouldBe true
    }

    @Test
    fun `add the same listener twice should only add it once`() {
        val listener = mockk<EventListener>()
        val service = EventService()

        service += listener
        service += listener

        service.numListeners shouldBe 1
        (listener in service) shouldBe true
    }

    @Test
    fun `add a world should register all EventListener systems`() {
        val (listenerSystem, world) = testWorld()
        val service = EventService()

        service += world

        service.numListeners shouldBe 1
        (listenerSystem in service) shouldBe true
    }

    @Test
    fun `remove a world should unregister all EventListener systems`() {
        val (listenerSystem, world) = testWorld()
        val service = EventService()
        service += world

        service -= world

        service.numListeners shouldBe 0
        (listenerSystem in service) shouldBe false
    }

    @Test
    fun `remove an already added listener`() {
        val listener = mockk<EventListener>()
        val service = EventService()
        service += listener

        service -= listener

        service.numListeners shouldBe 0
        (listener in service) shouldBe false
    }

    @Test
    fun `remove a listener that is not part of the service should not fail`() {
        val listener = mockk<EventListener>()
        val service = EventService()

        service -= listener

        service.numListeners shouldBe 0
        (listener in service) shouldBe false
    }

    @Test
    fun `fire an event`() {
        val gdxApp = mockk<Application>()
        every { gdxApp.logLevel } returns Application.LOG_NONE
        Gdx.app = gdxApp
        val event = mockk<Event>()
        val listener = mockk<EventListener>()
        every { listener.onEvent(event) } returns Unit
        val service = EventService()

        service += listener
        service.fire(event)

        verify(exactly = 1) { listener.onEvent(event) }
        confirmVerified(listener)
    }

    private fun testStage(): Stage {
        val stageMock = mockk<Stage>()
        val viewModelMock = mockk<ViewModel>()
        val testViewMock = mockk<View<ViewModel>>()
        every { testViewMock.viewModel } returns viewModelMock
        every { stageMock.actors } returns gdxArrayOf(testViewMock)
        return stageMock
    }

    @Test
    fun `add a stage should add all EventListener actors and view models`() {
        val stage = testStage()
        val service = EventService()
        val view = stage.actors.filterIsInstance<View<ViewModel>>().single()
        val viewModel = view.viewModel

        service += stage

        service.numListeners shouldBe 2
        (view in service) shouldBe true
        (viewModel in service) shouldBe true
    }

}
