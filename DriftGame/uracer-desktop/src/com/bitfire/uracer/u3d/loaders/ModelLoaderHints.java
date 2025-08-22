package com.bitfire.uracer.u3d.loaders;

/**
 * Hints passed to a loader which might ignore them.
 */
public class ModelLoaderHints {
    /**
     * whether to flip the v texture coordinate
     **/
    public final boolean flipV;

    public ModelLoaderHints(boolean flipV) {
        this.flipV = flipV;
    }
}
