package com.riiablo;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.riiablo.graphics.BlendMode;
import com.riiablo.codec.FontTBL;
import com.riiablo.loader.BitmapFontLoader;

public class Fonts {
  public final BitmapFont         consolas12;
  public final BitmapFont         consolas16;
  public final FontTBL.BitmapFont font6;
  public final FontTBL.BitmapFont font8;
  public final FontTBL.BitmapFont font16;
  public final FontTBL.BitmapFont font24;
  public final FontTBL.BitmapFont font30;
  public final FontTBL.BitmapFont font42;
  public final FontTBL.BitmapFont fontformal10;
  public final FontTBL.BitmapFont fontformal11;
  public final FontTBL.BitmapFont fontformal12;
  public final FontTBL.BitmapFont fontexocet10;
  public final FontTBL.BitmapFont fontridiculous;
  public final FontTBL.BitmapFont ReallyTheLastSucker;

  public Fonts(AssetManager assets) {
    consolas12   = loadEx(assets, "consolas12.fnt");
    consolas16   = loadEx(assets, "consolas16.fnt");
    font6        = load(assets, "font6",  BlendMode.LUMINOSITY_TINT);
    font8        = load(assets, "font8",  BlendMode.LUMINOSITY_TINT);
    font16       = load(assets, "font16", BlendMode.LUMINOSITY_TINT);
    font24       = load(assets, "font24", BlendMode.ID);
    font30       = load(assets, "font30", BlendMode.ID);
    font42       = load(assets, "font42", BlendMode.ID);
    fontformal10 = load(assets, "fontformal10", BlendMode.LUMINOSITY_TINT);
    fontformal11 = load(assets, "fontformal11", BlendMode.LUMINOSITY_TINT);
    fontformal12 = load(assets, "fontformal12", BlendMode.LUMINOSITY_TINT);
    fontexocet10 = load(assets, "fontexocet10", BlendMode.TINT_BLACKS);
    fontridiculous = load(assets, "fontridiculous", BlendMode.TINT_BLACKS);
    ReallyTheLastSucker = load(assets, "ReallyTheLastSucker", BlendMode.ID);

    BitmapFont.BitmapFontData data;
    data = font8.getData();
    data.lineHeight = data.xHeight = data.capHeight = 12;
    data.ascent = 16;
    data.down = -12;

    data = font16.getData();
    data.lineHeight = data.xHeight = data.capHeight = 14;
    data.ascent = 17;
    data.down = -16;

    data = font42.getData();
    data.lineHeight = data.xHeight = data.capHeight = 31;
    data.ascent = 48;
    data.down = -31;

    data = fontformal10.getData();
    data.lineHeight = data.xHeight = data.capHeight = 14;
    data.ascent = 17;
    data.down = -14;

    data = fontformal11.getData();
    data.lineHeight = data.xHeight = data.capHeight = 18;
    data.ascent = 18;
    data.down = -18;

    data = fontformal12.getData();
    data.lineHeight = data.xHeight = data.capHeight = 16;
    data.ascent = 42;
    data.down = -20;

    data = ReallyTheLastSucker.getData();
    data.lineHeight = data.xHeight = data.capHeight = 8;
    data.ascent = 11;
    data.down = -8;
  }

  private BitmapFont loadEx(AssetManager assets, String fontName) {
    assets.load(fontName, BitmapFont.class);
    assets.finishLoadingAsset(fontName);
    return assets.get(fontName);
  }

  private FontTBL.BitmapFont load(AssetManager assets, String fontName, int blendMode) {
    AssetDescriptor<FontTBL.BitmapFont> descriptor = getDescriptor(fontName, blendMode);
    assets.load(descriptor);
    assets.finishLoadingAsset(descriptor);
    return assets.get(descriptor);
  }

  private static AssetDescriptor<FontTBL.BitmapFont> getDescriptor(String fontName, int blendMode) {
    return new AssetDescriptor<>("data\\local\\font\\latin\\" + fontName + ".TBL", FontTBL.BitmapFont.class, BitmapFontLoader.Params.of(blendMode));
  }
}
