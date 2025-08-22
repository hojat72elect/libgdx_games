package com.bitfire.uracer.u3d.loaders;

import com.badlogic.gdx.files.FileHandle;
import com.bitfire.uracer.u3d.still.StillModel;

/**
 * Interface for loaders loading {@link StillModel} instances.
 */
public interface StillModelLoader extends ModelLoader {
    @Override
    StillModel load(FileHandle handle, ModelLoaderHints hints);
}
