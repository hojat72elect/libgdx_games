

package com.esotericsoftware.spine.attachments;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.esotericsoftware.spine.Skin;

public class AtlasAttachmentLoader implements AttachmentLoader {
    private final TextureAtlas atlas;

    public AtlasAttachmentLoader(TextureAtlas atlas) {
        if (atlas == null) throw new IllegalArgumentException("atlas cannot be null.");
        this.atlas = atlas;
    }

    public RegionAttachment newRegionAttachment(Skin skin, String name, String path) {
        RegionAttachment attachment = new RegionAttachment(name);
        attachment.setPath(path);
        AtlasRegion region = atlas.findRegion(path);
        if (region == null)
            throw new RuntimeException("Region not found in atlas: " + attachment + " (region attachment: " + name + ")");
        attachment.setRegion(region);
        return attachment;
    }

    public MeshAttachment newMeshAttachment(Skin skin, String name, String path) {
        MeshAttachment attachment = new MeshAttachment(name);
        attachment.setPath(path);
        AtlasRegion region = atlas.findRegion(path);
        if (region == null)
            throw new RuntimeException("Region not found in atlas: " + attachment + " (region attachment: " + name + ")");
        attachment.setRegion(region);
        return attachment;
    }

    public BoundingBoxAttachment newBoundingBoxAttachment(Skin skin, String name) {
        return new BoundingBoxAttachment(name);
    }
}
