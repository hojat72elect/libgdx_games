package com.bitfire.uracer.u3d.loaders;

import com.badlogic.gdx.files.FileHandle;
import com.bitfire.uracer.u3d.model.Model;

/**
 * Interface for all loaders. Loaders that need more files need to derrive the other file names by the given file. A bit of a
 * hack, but most formats are self contained.
 */
public interface ModelLoader {
    Model load(FileHandle file, ModelLoaderHints hints);
}
