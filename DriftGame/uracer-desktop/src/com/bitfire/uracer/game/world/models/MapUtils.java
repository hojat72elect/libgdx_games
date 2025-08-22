package com.bitfire.uracer.game.world.models;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.bitfire.uracer.game.world.WorldDefs.Layer;
import com.bitfire.uracer.game.world.WorldDefs.ObjectGroup;
import com.bitfire.uracer.utils.VMath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MapUtils implements Disposable {
    // cache
    public final Map<String, MapLayer> cachedGroups = new HashMap<>(10);
    public final Map<String, TiledMapTileLayer> cachedLayers = new HashMap<>(10);

    private final TiledMap map;
    private final float invTileWidth;
    private final int mapHeight;
    private final Vector2 retTile = new Vector2();

    public MapUtils(TiledMap map, int tileWidth, int mapHeight, Vector2 worldSizePx) {
        this.map = map;
        this.mapHeight = mapHeight;
        Vector2 worldSizePx1 = new Vector2();
        worldSizePx1.set(worldSizePx);

        invTileWidth = 1f / (float) tileWidth;
    }

    public static List<Vector2> extractPolyData(float[] vertices) {
        List<Vector2> points = new ArrayList<>();
        int num_verts = vertices.length;
        for (int i = 0; i < num_verts; i += 2) {
            points.add(new Vector2(vertices[i], vertices[i + 1]));
        }

        return points;
    }

    @Override
    public void dispose() {
        cachedLayers.clear();
        cachedGroups.clear();
    }

    public TiledMapTileLayer getLayer(Layer layer) {
        TiledMapTileLayer cached = cachedLayers.get(layer.mnemonic);
        if (cached == null) {
            cached = (TiledMapTileLayer) map.getLayers().get(layer.mnemonic);
            cachedLayers.put(layer.mnemonic, cached);
        }

        return cached;
    }

    public MapLayer getObjectGroup(ObjectGroup group) {
        MapLayer cached = cachedGroups.get(group.mnemonic);
        if (cached == null) {
            cached = map.getLayers().get(group.mnemonic);
            cachedGroups.put(group.mnemonic, cached);
        }

        return cached;
    }

    public boolean hasObjectGroup(ObjectGroup group) {
        return getObjectGroup(group) != null;
    }

    public Vector2 pxToTile(float x, float y) {
        retTile.set(x, y);
        retTile.scl(invTileWidth);
        retTile.y = mapHeight - retTile.y;
        VMath.truncateToInt(retTile);
        return retTile;
    }
}
