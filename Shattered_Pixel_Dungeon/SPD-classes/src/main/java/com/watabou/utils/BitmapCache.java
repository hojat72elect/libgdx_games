

package com.watabou.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.watabou.noosa.Game;

import java.util.HashMap;

public class BitmapCache {

    private static final String DEFAULT = "__default";

    private static final HashMap<String, Layer> layers = new HashMap<>();

    public static Pixmap get(String assetName) {
        return get(DEFAULT, assetName);
    }

    public static Pixmap get(String layerName, String assetName) {

        Layer layer;
        if (!layers.containsKey(layerName)) {
            layer = new Layer();
            layers.put(layerName, layer);
        } else {
            layer = layers.get(layerName);
        }

        if (layer.containsKey(assetName)) {
            return layer.get(assetName);
        } else {

            try {
                Pixmap bmp = new Pixmap(Gdx.files.internal(assetName));
                layer.put(assetName, bmp);
                return bmp;
            } catch (Exception e) {
                Game.reportException(e);
                return null;
            }
        }
    }

    //Unused, libGDX does not support resource Ids
	/*
	public static Pixmap get( int resID ) {
		return get( DEFAULT, resID );
	}
	
	public static Pixmap get( String layerName, int resID ) {
		
		Layer layer;
		if (!layers.containsKey( layerName )) {
			layer = new Layer();
			layers.put( layerName, layer );
		} else {
			layer = layers.get( layerName );
		}
		
		if (layer.containsKey( resID )) {
			return layer.get( resID );
		} else {
			
			Bitmap bmp = BitmapFactory.decodeResource( context.getResources(), resID );
			layer.put( resID, bmp );
			return bmp;
			
		}
	}*/

    public static void clear(String layerName) {
        if (layers.containsKey(layerName)) {
            layers.get(layerName).clear();
            layers.remove(layerName);
        }
    }

    public static void clear() {
        for (Layer layer : layers.values()) {
            layer.clear();
        }
        layers.clear();
    }

    @SuppressWarnings("serial")
    private static class Layer extends HashMap<Object, Pixmap> {

        @Override
        public void clear() {
            for (Pixmap bmp : values()) {
                bmp.dispose();
            }
            super.clear();
        }
    }
}
