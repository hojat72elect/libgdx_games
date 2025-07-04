package me.vrekt.oasis.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages data about an entities current texture or animation to draw.
 */
public final class EntityTextureComponent implements Component {
    public Map<String, TextureRegion> textureRegions = new HashMap<>();
    public Map<String, Texture> textures = new HashMap<>();

    public void add(String name, TextureRegion region) {
        textureRegions.put(name, region);
    }

    public TextureRegion get(String name) {
        return textureRegions.get(name);
    }
}
