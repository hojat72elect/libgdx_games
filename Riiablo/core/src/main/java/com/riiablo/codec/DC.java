package com.riiablo.codec;

import java.util.Arrays;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

import com.riiablo.codec.util.BBox;
import com.riiablo.graphics.PaletteIndexedBatch;
import com.riiablo.logger.LogManager;
import com.riiablo.logger.Logger;

public abstract class DC implements Disposable {
  private static final Logger log = LogManager.getLogger(DC.class);

  public static final String[] EXTS = {DC6.EXT, DCC.EXT }; // I think uncompressed first is better

  public static DC loadFromFile(FileHandle handle) {
    String path = handle.toString();
    switch (path.charAt(path.length() - 1)) {
      case 'c': return DCC.loadFromFile(handle);
      case '6': return DC6.loadFromFile(handle);
      default:  throw new IllegalArgumentException("Invalid handle given -- not one of " + Arrays.toString(EXTS));
    }
  }

  public abstract int getNumDirections();
  public abstract Direction getDirection(int d);

  public abstract int getNumFramesPerDir();
  public final int getNumPages() {
    return getNumPages(0);
  }
  public int getNumPages(int d) {
    return getNumFramesPerDir();
  }
  public abstract Frame getFrame(int d, int f);

  public abstract BBox getBox();
  public abstract BBox getBox(int d);
  public abstract BBox getBox(int d, int f);

  public abstract boolean isLoaded(int d);
  public final void loadDirection(int d) {
    loadDirection(d, false);
  }
  public abstract void loadDirection(int d, boolean combineFrames);
  public final void loadDirections() {
    loadDirections(false);
  }
  public abstract void loadDirections(boolean combineFrames);
  public final TextureRegion getTexture() {
    return getTexture(0, 0);
  }
  public final TextureRegion getTexture(int i) {
    return getTexture(0, i);
  }
  public abstract TextureRegion getTexture(int d, int i);

  TextureRegion[][] regions;
  public final TextureRegion[] getRegions(int d) {
    return getRegions()[d];
  }
  public final TextureRegion[][] getRegions() {
    return regions;
  }

  public abstract boolean isPreloaded(int d);
  public final void preloadDirection(int d) {
    preloadDirection(d, false);
  }
  public abstract void preloadDirection(int d, boolean combineFrames);
  public final void preloadDirections() {
    preloadDirections(false);
  }
  public abstract void preloadDirections(boolean combineFrames);
  public abstract Pixmap getPixmap(int d, int f);

  public void draw(PaletteIndexedBatch batch, float x, float y) {
    draw(batch, 0, 0, x, y);
  }
  public void draw(PaletteIndexedBatch batch, int i, float x, float y) {
    draw(batch, 0, i, x, y);
  }
  public void draw(PaletteIndexedBatch batch, int d, int i, float x, float y) {
    // TODO: offset to box
    batch.draw(getTexture(), x, y);
  }

  public static abstract class Direction {
    BBox box;

    public static int toRealDir(int d, int numDirs) {
      switch (numDirs) {
        case 1:
        case 2:
        case 4:  return d;
        case 8:  return toRealDir8(d);
        case 16: return toRealDir16(d);
        case 32: return toRealDir32(d);
        default: // FIXME: see #113
          log.error("numDirs({}) invalid: should be one of {1,2,4,8,16,32}", numDirs);
          return 0;
      }
    }

    public static int toRealDir8(int d) {
      switch (d) {
        case 0: return 1;
        case 1: return 3;
        case 2: return 5;
        case 3: return 7;

        case 4: return 0;
        case 5: return 2;
        case 6: return 4;
        case 7: return 6;

        default: // FIXME: see #113
          log.error("d({}) invalid: should be in [{}..{})", d, 0, 8);
          return 0;
      }
    }

    public static int toRealDir16(int d) {
      switch (d) {
        case 0:  return 2;
        case 1:  return 6;
        case 2:  return 10;
        case 3:  return 14;

        case 4:  return 0;
        case 5:  return 4;
        case 6:  return 8;
        case 7:  return 12;

        case 8:  return 1;
        case 9:  return 3;
        case 10: return 5;
        case 11: return 7;
        case 12: return 9;
        case 13: return 11;
        case 14: return 13;
        case 15: return 15;

        default: // FIXME: see #113
          log.error("d({}) invalid: should be in [{}..{})", d, 0, 16);
          return 0;
      }
    }

    public static int toRealDir32(int d) {
      switch (d) {
        case 0:  return 4;
        case 1:  return 12;
        case 2:  return 20;
        case 3:  return 28;

        case 4:  return 0;
        case 5:  return 8;
        case 6:  return 16;
        case 7:  return 24;

        case 8:  return 2;
        case 9:  return 6;
        case 10: return 10;
        case 11: return 14;
        case 12: return 18;
        case 13: return 22;
        case 14: return 26;
        case 15: return 30;

        case 16: return 1;
        case 17: return 3;
        case 18: return 5;
        case 19: return 7;
        case 20: return 9;
        case 21: return 11;
        case 22: return 13;
        case 23: return 15;
        case 24: return 17;
        case 25: return 19;
        case 26: return 21;
        case 27: return 23;
        case 28: return 25;
        case 29: return 27;
        case 30: return 29;
        case 31: return 31;

        default: // FIXME: see #113
          log.error("d({}) invalid: should be in [{}..{})", d, 0, 32);
          return 0;
      }
    }
  }
  public static abstract class Frame {
    int flip;
    int width;
    int height;
    int xOffset;
    int yOffset;

    BBox box;
    byte colormap[];

    /**
     * read-only workaround for mpq viewer
     */
    public int getWidth() {
      return width;
    }
  }
}
