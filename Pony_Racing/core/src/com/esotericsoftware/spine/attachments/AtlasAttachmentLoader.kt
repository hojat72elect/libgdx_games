package com.esotericsoftware.spine.attachments

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.esotericsoftware.spine.Skin

class AtlasAttachmentLoader(val atlas: TextureAtlas) : AttachmentLoader {

    init {
        requireNotNull(atlas) { "atlas cannot be null." }
    }

    override fun newRegionAttachment(skin: Skin, name: String, path: String): RegionAttachment? {
        val attachment = RegionAttachment(name)
        attachment.path = path
        val region = atlas.findRegion(path)
        if (region == null) throw RuntimeException("Region not found in atlas: $attachment (region attachment: $name)")
        attachment.region = region
        return attachment
    }

    override fun newMeshAttachment(skin: Skin, name: String, path: String): MeshAttachment? {
        val attachment = MeshAttachment(name)
        attachment.path = path
        val region = atlas.findRegion(path)
        if (region == null) throw RuntimeException("Region not found in atlas: $attachment (region attachment: $name)")
        attachment.region = region
        return attachment
    }

    override fun newBoundingBoxAttachment(skin: Skin, name: String): BoundingBoxAttachment? {
        return BoundingBoxAttachment(name)
    }
}