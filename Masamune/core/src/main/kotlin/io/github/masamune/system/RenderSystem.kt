package io.github.masamune.system

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.github.quillraven.fleks.collection.compareEntityBy
import io.github.masamune.Masamune.Companion.UNIT_SCALE
import io.github.masamune.asset.ShaderService
import io.github.masamune.component.Dissolve
import io.github.masamune.component.Graphic
import io.github.masamune.component.Outline
import io.github.masamune.component.Transform
import io.github.masamune.event.Event
import io.github.masamune.event.EventListener
import io.github.masamune.event.MapChangeEvent
import io.github.masamune.event.MapTransitionBeginEvent
import io.github.masamune.tiledmap.MapTransitionType
import ktx.math.component1
import ktx.math.component2
import ktx.math.vec2
import ktx.tiled.height
import ktx.tiled.use
import ktx.tiled.width

class RenderSystem(
    private val batch: Batch = inject(),
    private val gameViewport: Viewport = inject(),
    private val shaderService: ShaderService = inject(),
) : IteratingSystem(
    family = family { all(Transform, Graphic) },
    comparator = compareEntityBy(Transform)
), EventListener {

    private val camera: OrthographicCamera = gameViewport.camera as OrthographicCamera
    private val mapRenderer = OrthogonalTiledMapRenderer(null, UNIT_SCALE, batch)
    private val backgroundLayers = mutableListOf<TiledMapTileLayer>()
    private val foregroundLayers = mutableListOf<TiledMapTileLayer>()
    private val batchOrigColor = Color()

    // optional map layers for map transition
    private val transitionBackground = mutableListOf<TiledMapTileLayer>()
    private val transitionForeground = mutableListOf<TiledMapTileLayer>()
    val transitionOffset = vec2()

    override fun onTick() {
        gameViewport.apply()
        batchOrigColor.set(batch.color)
        mapRenderer.use(camera) { renderer ->
            backgroundLayers.forEach(renderer::renderTileLayer)
            renderer.renderTransitionMapLayers(transitionBackground)

            // render all entities
            super.onTick()
            batch.setColor(batchOrigColor.r, batchOrigColor.g, batchOrigColor.b, batchOrigColor.a)

            foregroundLayers.forEach { renderer.renderTileLayer(it) }
            renderer.renderTransitionMapLayers(transitionForeground)
        }
    }

    private fun OrthogonalTiledMapRenderer.renderTransitionMapLayers(layers: List<TiledMapTileLayer>) {
        if (layers.isEmpty()) {
            return
        }

        // temporarily update camera position to render the next map next to the current map
        // both maps are connected via their edges (e.g. active map's top edge is connected to next map's bottom edge)
        val origX = camera.position.x
        val origY = camera.position.y
        camera.position.x += transitionOffset.x
        camera.position.y += transitionOffset.y
        camera.update()
        setView(camera)
        layers.forEach(this::renderTileLayer)

        // reset camera position to its original location for the normal active map rendering
        camera.position.x = origX
        camera.position.y = origY
        camera.update()
        setView(camera)
    }

    /**
     * Set entity's sprite position, scaling and rotation to its transform values
     * and draw the sprite.
     */
    override fun onTickEntity(entity: Entity) {
        val graphic = entity[Graphic]
        val transform = entity[Transform]

        // fill texture inside transform size by keeping aspect ratio
        val (regSizeX, regSizeY) = graphic.regionSize
        val (sizeX, sizeY) = transform.size
        val realSize = Scaling.fill.apply(regSizeX, regSizeY, sizeX, sizeY)

        val dissolveCmp = entity.getOrNull(Dissolve)
        if (dissolveCmp != null) {
            val (_, uvOffset, uvMax, numFragments, value) = dissolveCmp
            shaderService.useDissolveShader(batch, value, uvOffset, uvMax, numFragments) {
                batch.drawEntity(graphic, transform, realSize)
            }
        } else {
            batch.drawEntity(graphic, transform, realSize)
            entity.getOrNull(Outline)?.let { outline ->
                shaderService.useOutlineShader(batch, outline.color, graphic.region.texture) {
                    batch.drawEntity(graphic, transform, realSize)
                }
            }
        }
    }

    private fun Batch.drawEntity(graphic: Graphic, transform: Transform, realSize: Vector2) {
        val (texRegion, color) = graphic
        val (position, _, scale, rotationDeg, offset) = transform

        batch.setColor(
            color.r * batchOrigColor.r,
            color.g * batchOrigColor.g,
            color.b * batchOrigColor.b,
            color.a * batchOrigColor.a
        )
        this.draw(
            texRegion,
            position.x + offset.x, position.y + offset.y,
            realSize.x * 0.5f, realSize.y * 0.5f,
            realSize.x, realSize.y,
            scale, scale,
            rotationDeg
        )
    }

    private fun updateMapLayers(
        fgdLayers: MutableList<TiledMapTileLayer>,
        bgdLayers: MutableList<TiledMapTileLayer>,
        tiledMap: TiledMap
    ) {
        bgdLayers.clear()
        fgdLayers.clear()
        var activeLayers = bgdLayers
        tiledMap.layers.forEach { layer ->
            if (layer::class == MapLayer::class) {
                activeLayers = fgdLayers
            }
            if (layer !is TiledMapTileLayer) {
                return@forEach
            }

            activeLayers += layer
        }
    }

    private fun onMapTransition(
        offset: Vector2,
        fromTiledMap: TiledMap,
        toTiledMap: TiledMap,
        type: MapTransitionType
    ) {
        updateMapLayers(transitionForeground, transitionBackground, toTiledMap)

        transitionOffset.setZero()
        when (type) {
            MapTransitionType.LEFT_TO_RIGHT -> {
                // portal on left edge of map
                transitionOffset.x = toTiledMap.width.toFloat()
                transitionOffset.y = offset.y
            }

            MapTransitionType.BOTTOM_TO_TOP -> {
                // portal on bottom edge of map
                transitionOffset.x = offset.x
                transitionOffset.y = toTiledMap.height.toFloat()
            }

            MapTransitionType.TOP_TO_BOTTOM -> {
                // portal on top edge of map
                transitionOffset.x = offset.x
                transitionOffset.y = -fromTiledMap.height.toFloat()
            }

            MapTransitionType.RIGHT_TO_LEFT -> {
                // portal on right edge of map
                transitionOffset.x = -fromTiledMap.width.toFloat()
                transitionOffset.y = offset.y
            }
        }
    }

    override fun onEvent(event: Event) {
        when (event) {
            is MapChangeEvent -> {
                transitionBackground.clear()
                transitionForeground.clear()
                updateMapLayers(foregroundLayers, backgroundLayers, event.tiledMap)
            }

            is MapTransitionBeginEvent -> {
                onMapTransition(event.mapOffset, event.fromTiledMap, event.toTiledMap, event.type)
            }

            else -> Unit
        }
    }

    fun clearMapLayer() {
        transitionBackground.clear()
        transitionForeground.clear()
        foregroundLayers.clear()
        backgroundLayers.clear()
        mapRenderer.map = null
    }

    override fun onDispose() {
        mapRenderer.dispose()
    }
}
