package io.github.masamune

import com.badlogic.gdx.graphics.g2d.Batch
import io.github.masamune.asset.AssetService
import io.github.masamune.asset.ShaderService
import io.github.masamune.audio.AudioService
import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.event.EventService
import io.github.masamune.screen.ScreenTransitionService
import io.github.masamune.tiledmap.ImmediateMapTransitionService
import io.github.masamune.tiledmap.MapTransitionService
import io.github.masamune.tiledmap.TiledService
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test

class LazyServiceLocatorTest {

    @Test
    fun `verify that initializer args are properly called`() {
        val batchInitializer = mockk<() -> Batch>()
        val assetServiceInitializer = mockk<() -> AssetService>()
        val eventServiceInitializer = mockk<() -> EventService>()
        val tiledServiceInitializer = mockk<(AssetService, EventService) -> TiledService>()
        val shaderServiceInitializer = mockk<() -> ShaderService>()
        val mapTransitionServiceInitializer = mockk<(TiledService) -> MapTransitionService>()
        val audioServiceInitializer = mockk<(AssetService) -> AudioService>()
        val screenTransitionServiceInitializer = mockk<(Batch, ShaderService) -> ScreenTransitionService>()
        val actionExecutorServiceInitializer = mockk<(AudioService, EventService) -> ActionExecutorService>()
        val batch = mockk<Batch>()
        val assetService = mockk<AssetService>()
        val eventService = mockk<EventService>()
        val tiledService = mockk<TiledService>()
        val shaderService = mockk<ShaderService>()
        val mapTransitionService = mockk<MapTransitionService>()
        val audioService = mockk<AudioService>()
        val screenTransitionService = mockk<ScreenTransitionService>()
        val actionExecutorService = mockk<ActionExecutorService>()
        every { batchInitializer.invoke() } returns batch
        every { assetServiceInitializer.invoke() } returns assetService
        every { eventServiceInitializer.invoke() } returns eventService
        every { tiledServiceInitializer.invoke(assetService, eventService) } returns tiledService
        every { shaderServiceInitializer.invoke() } returns shaderService
        every { mapTransitionServiceInitializer.invoke(tiledService) } returns mapTransitionService
        every { audioServiceInitializer.invoke(assetService) } returns audioService
        every { screenTransitionServiceInitializer.invoke(batch, shaderService) } returns screenTransitionService
        every { actionExecutorServiceInitializer.invoke(audioService, eventService) } returns actionExecutorService

        val serviceLocator = LazyServiceLocator(
            batchInitializer,
            assetServiceInitializer,
            eventServiceInitializer,
            tiledServiceInitializer,
            shaderServiceInitializer,
            mapTransitionServiceInitializer,
            audioServiceInitializer,
            screenTransitionServiceInitializer,
            actionExecutorServiceInitializer,
        )

        batch shouldBeSameInstanceAs serviceLocator.batch
        assetService shouldBeSameInstanceAs serviceLocator.asset
        eventService shouldBeSameInstanceAs serviceLocator.event
        tiledService shouldBeSameInstanceAs serviceLocator.tiled
        shaderService shouldBeSameInstanceAs serviceLocator.shader
        mapTransitionService shouldBeSameInstanceAs serviceLocator.mapTransition
        audioService shouldBeSameInstanceAs serviceLocator.audio
        screenTransitionService shouldBeSameInstanceAs serviceLocator.screenTransition
        actionExecutorService shouldBeSameInstanceAs serviceLocator.actionExecutor
    }

    @Test
    fun `verify TiledService gets correct asset and event service instance`() {
        val assetServiceInitializer = mockk<() -> AssetService>()
        val eventServiceInitializer = mockk<() -> EventService>()
        val assetService = mockk<AssetService>()
        val eventService = mockk<EventService>()
        every { assetServiceInitializer.invoke() } returns assetService
        every { eventServiceInitializer.invoke() } returns eventService

        val serviceLocator = LazyServiceLocator(
            assetServiceInitializer = assetServiceInitializer,
            eventServiceInitializer = eventServiceInitializer,
            tiledServiceInitializer = { asset, event -> TiledService(asset, event) }
        )

        serviceLocator.tiled shouldNotBe null
        serviceLocator.tiled.assetService shouldBeSameInstanceAs serviceLocator.asset
        serviceLocator.tiled.eventService shouldBeSameInstanceAs serviceLocator.event
    }

    @Test
    fun `verify MapTransitionService gets correct tiled service instance`() {
        val serviceLocator = LazyServiceLocator(
            tiledServiceInitializer = { asset, event -> TiledService(asset, event) },
            mapTransitionServiceInitializer = { tiled -> ImmediateMapTransitionService(tiled) }
        )

        serviceLocator.mapTransition shouldNotBe null
        serviceLocator.mapTransition.tiledService shouldBeSameInstanceAs serviceLocator.tiled
    }

    @Test
    fun `verify AudioService gets correct asset service instance`() {
        val serviceLocator = LazyServiceLocator(
            assetServiceInitializer = { AssetService() },
            audioServiceInitializer = { asset -> AudioService(asset) }
        )

        serviceLocator.audio shouldNotBe null
        serviceLocator.audio.assetService shouldBeSameInstanceAs serviceLocator.asset
    }

    @Test
    fun `verify ScreenTransitionService gets correct batch and shader service instance`() {
        val serviceLocator = LazyServiceLocator(
            batchInitializer = { mockk<Batch>() },
            shaderServiceInitializer = { mockk<ShaderService>() },
            screenTransitionServiceInitializer = { batch, shaderService ->
                ScreenTransitionService(batch, shaderService)
            }
        )

        serviceLocator.screenTransition shouldNotBe null
        serviceLocator.screenTransition.batch shouldBeSameInstanceAs serviceLocator.batch
        serviceLocator.screenTransition.shaderService shouldBeSameInstanceAs serviceLocator.shader
    }

    @Test
    fun `verify ActionExecutorService gets correct audio and event service instance`() {
        val serviceLocator = LazyServiceLocator(
            audioServiceInitializer = { mockk<AudioService>() },
            eventServiceInitializer = { mockk<EventService>() },
            actionExecutorServiceInitializer = { audioService, eventService ->
                ActionExecutorService(audioService, eventService)
            }
        )

        serviceLocator.actionExecutor shouldNotBe null
        serviceLocator.actionExecutor.audioService shouldBeSameInstanceAs serviceLocator.audio
        serviceLocator.actionExecutor.eventService shouldBeSameInstanceAs serviceLocator.event
    }

}
