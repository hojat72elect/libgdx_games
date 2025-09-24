package com.bitfire.uracer.game.world

class WorldDefs {

    /**
     * Attributes for Tiled's object properties.
     */
    enum class ObjectProperties(@JvmField val mnemonic: String) {
        MeshScale("scale");
    }

    /**
     * Tiled's world tile layers
     */
    enum class Layer(@JvmField val mnemonic: String) {
        Track("track");
    }

    /**
     * Tiled's world object layer/group
     */
    enum class ObjectGroup(@JvmField val mnemonic: String) {
        Lights("lights"),
        StaticMeshes("static-meshes"),
        Trees("trees"),
        Walls("walls"),
        Route("route"),
        Sectors("sectors");
    }
}
