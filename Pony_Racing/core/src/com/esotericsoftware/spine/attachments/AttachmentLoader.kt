package com.esotericsoftware.spine.attachments

import com.esotericsoftware.spine.Skin

interface AttachmentLoader {
    /**
     * @return May be null to not load any attachment.
     */
    fun newRegionAttachment(skin: Skin, name: String, path: String): RegionAttachment?

    /**
     * @return May be null to not load any attachment.
     */
    fun newMeshAttachment(skin: Skin, name: String, path: String): MeshAttachment?

    /**
     * @return May be null to not load any attachment.
     */
    fun newBoundingBoxAttachment(skin: Skin, name: String): BoundingBoxAttachment?
}