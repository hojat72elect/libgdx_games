package com.bitfire.uracer.game.world;

public final class WorldDefs {

    /**
     * Attributes for Tiled's object properties.
     */
    public enum ObjectProperties {
        // @off
        MeshScale("scale");
        // @on

        public final String mnemonic;

        ObjectProperties(String mnemonic) {
            this.mnemonic = mnemonic;
        }
    }

    /**
     * Tiled's world tile layers
     */
    public enum Layer {
        // @off
        Track("track"),
//		Lights("lights"), 
//		StaticMeshes("static-meshes"), 
//		Trees("trees"), 
//		Walls("walls"),
//		Route("route"),
//		Sectors("sectors")
        ;
        // @on

        public final String mnemonic;

        Layer(String mnemonic) {
            this.mnemonic = mnemonic;
        }
    }

    /**
     * Tiled's world object layer/group
     */
    public enum ObjectGroup {
        // @off
        Lights("lights"),
        StaticMeshes("static-meshes"),
        Trees("trees"),
        Walls("walls"),
        Route("route"),
        Sectors("sectors");
        // @on

        public final String mnemonic;

        ObjectGroup(String mnemonic) {
            this.mnemonic = mnemonic;
        }
    }
}
