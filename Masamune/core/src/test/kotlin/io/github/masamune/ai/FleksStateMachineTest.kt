package io.github.masamune.ai

import com.github.quillraven.fleks.configureWorld
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test

class FleksStateMachineTest {

    private val testWorld = configureWorld { }
    private val testEntity = testWorld.entity { }
    private val testCurrentState = mockk<FsmState>(relaxed = true)
    private val testChangeState = mockk<FsmState>(relaxed = true)
    private val testGlobalState = mockk<FsmState>(relaxed = true)

    @Test
    fun `test state machine creation`() {
        val stateMachine = FleksStateMachine(testWorld, testEntity, testCurrentState, testGlobalState)

        stateMachine.previousState shouldBe DefaultFsmState
        stateMachine.currentState shouldBe testCurrentState
        stateMachine.globalState shouldBe testGlobalState
        // on enter is called for the initial states
        verify(exactly = 1) { testCurrentState.run { testWorld.onEnter(testEntity) } }
        verify(exactly = 0) { testCurrentState.run { testWorld.onExit(testEntity) } }
        verify(exactly = 0) { testCurrentState.run { testWorld.onUpdate(testEntity) } }
        verify(exactly = 1) { testGlobalState.run { testWorld.onEnter(testEntity) } }
        verify(exactly = 0) { testGlobalState.run { testWorld.onExit(testEntity) } }
        verify(exactly = 0) { testGlobalState.run { testWorld.onUpdate(testEntity) } }
    }

    @Test
    fun `test change state`() {
        val stateMachine = FleksStateMachine(testWorld, testEntity, testCurrentState, testGlobalState)

        stateMachine.changeState(testChangeState)

        stateMachine.previousState shouldBe testCurrentState
        stateMachine.currentState shouldBe testChangeState
        stateMachine.globalState shouldBe testGlobalState
        // enter and exit is called for initial state
        verify(exactly = 1) { testCurrentState.run { testWorld.onEnter(testEntity) } }
        verify(exactly = 1) { testCurrentState.run { testWorld.onExit(testEntity) } }
        verify(exactly = 0) { testCurrentState.run { testWorld.onUpdate(testEntity) } }
        // enter is called for new state
        verify(exactly = 1) { testChangeState.run { testWorld.onEnter(testEntity) } }
        verify(exactly = 0) { testChangeState.run { testWorld.onExit(testEntity) } }
        verify(exactly = 0) { testChangeState.run { testWorld.onUpdate(testEntity) } }
        // enter is called for global state
        verify(exactly = 1) { testGlobalState.run { testWorld.onEnter(testEntity) } }
        verify(exactly = 0) { testGlobalState.run { testWorld.onExit(testEntity) } }
        verify(exactly = 0) { testGlobalState.run { testWorld.onUpdate(testEntity) } }
    }

    @Test
    fun `test update state machine`() {
        val stateMachine = FleksStateMachine(testWorld, testEntity, testCurrentState, testGlobalState)

        stateMachine.update()

        verify(exactly = 1) { testCurrentState.run { testWorld.onEnter(testEntity) } }
        verify(exactly = 0) { testCurrentState.run { testWorld.onExit(testEntity) } }
        verify(exactly = 1) { testCurrentState.run { testWorld.onUpdate(testEntity) } }
        verify(exactly = 1) { testGlobalState.run { testWorld.onEnter(testEntity) } }
        verify(exactly = 0) { testGlobalState.run { testWorld.onExit(testEntity) } }
        verify(exactly = 1) { testGlobalState.run { testWorld.onUpdate(testEntity) } }
    }

}
