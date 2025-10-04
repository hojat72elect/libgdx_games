package com.bitfire.uracer.game.world.models

import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import com.bitfire.uracer.game.world.WorldDefs
import com.bitfire.uracer.game.world.WorldDefs.ObjectGroup
import com.bitfire.uracer.utils.VectorMathUtils.truncateToInt

class MapUtils(private val map: TiledMap, tileWidth: Int, private val mapHeight: Int, val worldSizePx: Vector2) : Disposable {

    val cachedGroups: MutableMap<String, MapLayer> = HashMap(10)
    val cachedLayers: MutableMap<String, TiledMapTileLayer?> = HashMap(10)
    private val invTileWidth = 1F / tileWidth
    private val retTile = Vector2()

    override fun dispose() {
        cachedLayers.clear()
        cachedGroups.clear()
    }

    fun getLayer(layer: WorldDefs.Layer): TiledMapTileLayer {
        var cached = cachedLayers[layer.mnemonic]
        if (cached == null) {
            cached = map.layers.get(layer.mnemonic) as TiledMapTileLayer?
            cachedLayers[layer.mnemonic] = cached
        }

        return cached!!
    }

    fun getObjectGroup(group: ObjectGroup): MapLayer? {
        var cached = cachedGroups[group.mnemonic]
        if (cached == null) {
            cached = map.layers.get(group.mnemonic)
            cachedGroups[group.mnemonic] = cached
        }

        return cached
    }

    fun hasObjectGroup(group: ObjectGroup) = getObjectGroup(group) != null


    fun pxToTile(x: Float, y: Float): Vector2 {
        retTile.set(x, y)
        retTile.scl(invTileWidth)
        retTile.y = mapHeight - retTile.y
        truncateToInt(retTile)
        return retTile
    }

    companion object {
        @JvmStatic
        fun extractPolyData(vertices: FloatArray): MutableList<Vector2> {
            val points: MutableList<Vector2> = ArrayList()
            val num_verts = vertices.size
            var i = 0
            while (i < num_verts) {
                points.add(Vector2(vertices[i], vertices[i + 1]))
                i += 2
            }

            return points
        }
    }
}
