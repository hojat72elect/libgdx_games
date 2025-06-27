package com.riiablo.map;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class DT1Loader extends AsynchronousAssetLoader<DT1, DT1Loader.DT1LoaderParameters> {

  DT1 dt1;

  public DT1Loader(FileHandleResolver resolver) {
    super(resolver);
  }

  @Override
  public void loadAsync(AssetManager assets, String fileName, FileHandle file, DT1LoaderParameters params) {
    dt1 = DT1.loadFromStream(fileName, file.read());
  }

  @Override
  public DT1 loadSync(AssetManager assets, String fileName, FileHandle file, DT1LoaderParameters params) {
    DT1 dt1 = this.dt1;
    if (dt1 == null) {
      dt1 = DT1.loadFromStream(fileName, file.read());
    } else {
      this.dt1 = null;
    }

    dt1.prepareTextures();
//    if (params != null) params.dt1s.add(dt1); // See below note
    return dt1;
  }

  @Override
  public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, DT1LoaderParameters params) {
    return null;
  }

  public static class DT1LoaderParameters extends AssetLoaderParameters<DT1> {
// This was never implemented -- should be handled by map -- kept in case it is eventually needed
//    public DT1s dt1s;
//
//    public static DT1LoaderParameters newInstance(DT1s dt1s) {
//      DT1LoaderParameters params = new DT1LoaderParameters();
//      params.dt1s = dt1s;
//      return params;
//    }
  }

}
