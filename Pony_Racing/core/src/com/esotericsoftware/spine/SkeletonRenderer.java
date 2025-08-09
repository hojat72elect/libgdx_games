package com.esotericsoftware.spine;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.MeshAttachment;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.esotericsoftware.spine.attachments.SkeletonAttachment;

public class SkeletonRenderer {
    static private final short[] quadTriangle = {0, 1, 2, 2, 3, 0};

    public void draw(PolygonSpriteBatch batch, Skeleton skeleton) {
        boolean premultipliedAlpha = false;
        int srcFunc = GL20.GL_SRC_ALPHA;
        batch.setBlendFunction(srcFunc, GL20.GL_ONE_MINUS_SRC_ALPHA);

        boolean additive = false;

        float[] vertices;
        short[] triangles;
        Texture texture;
        Array<Slot> drawOrder = skeleton.drawOrder;
        for (int i = 0, n = drawOrder.size; i < n; i++) {
            Slot slot = drawOrder.get(i);
            Attachment attachment = slot.attachment;
            if (attachment instanceof RegionAttachment) {
                RegionAttachment region = (RegionAttachment) attachment;
                region.updateWorldVertices(slot, premultipliedAlpha);
                vertices = region.getWorldVertices();
                triangles = quadTriangle;
                texture = region.getRegion().getTexture();

                if (slot.data.getAdditiveBlending() != additive) {
                    additive = !additive;
                    if (additive)
                        batch.setBlendFunction(srcFunc, GL20.GL_ONE);
                    else
                        batch.setBlendFunction(srcFunc, GL20.GL_ONE_MINUS_SRC_ALPHA);
                }

                batch.draw(texture, vertices, 0, vertices.length, triangles, 0, triangles.length);
            } else if (attachment instanceof MeshAttachment) {
                MeshAttachment mesh = (MeshAttachment) attachment;
                mesh.updateWorldVertices(slot, true);
                vertices = mesh.getWorldVertices();
                triangles = mesh.getTriangles();
                texture = mesh.getRegion().getTexture();
                batch.draw(texture, vertices, 0, vertices.length, triangles, 0, triangles.length);
            } else if (attachment instanceof SkeletonAttachment) {
                Skeleton attachmentSkeleton = ((SkeletonAttachment) attachment).getSkeleton();
                if (attachmentSkeleton == null) continue;
                Bone bone = slot.bone;
                Bone rootBone = attachmentSkeleton.getRootBone();
                float oldScaleX = rootBone.scaleX;
                float oldScaleY = rootBone.scaleY;
                float oldRotation = rootBone.rotation;
                attachmentSkeleton.setX(skeleton.getX() + bone.worldX);
                attachmentSkeleton.setY(skeleton.getY() + bone.worldY);
                rootBone.scaleX = 1 + bone.worldScaleX - oldScaleX;
                rootBone.scaleY = 1 + bone.worldScaleY - oldScaleY;
                rootBone.rotation = oldRotation + bone.worldRotation;
                attachmentSkeleton.updateWorldTransform();

                draw(batch, attachmentSkeleton);

                attachmentSkeleton.setX(0);
                attachmentSkeleton.setY(0);
                rootBone.scaleX = oldScaleX;
                rootBone.scaleY = oldScaleY;
                rootBone.rotation = oldRotation;
            }
        }
    }

    public void draw(Batch batch, Skeleton skeleton) {
        boolean premultipliedAlpha = false;
        int srcFunc = GL20.GL_SRC_ALPHA;
        batch.setBlendFunction(srcFunc, GL20.GL_ONE_MINUS_SRC_ALPHA);

        boolean additive = false;

        Array<Slot> drawOrder = skeleton.drawOrder;
        for (int i = 0, n = drawOrder.size; i < n; i++) {
            Slot slot = drawOrder.get(i);
            Attachment attachment = slot.attachment;
            if (attachment instanceof RegionAttachment) {
                RegionAttachment regionAttachment = (RegionAttachment) attachment;
                regionAttachment.updateWorldVertices(slot, premultipliedAlpha);
                float[] vertices = regionAttachment.getWorldVertices();
                if (slot.data.getAdditiveBlending() != additive) {
                    additive = !additive;
                    if (additive)
                        batch.setBlendFunction(srcFunc, GL20.GL_ONE);
                    else
                        batch.setBlendFunction(srcFunc, GL20.GL_ONE_MINUS_SRC_ALPHA);
                }
                batch.draw(regionAttachment.getRegion().getTexture(), vertices, 0, 20);
            } else if (attachment instanceof SkeletonAttachment) {
                Skeleton attachmentSkeleton = ((SkeletonAttachment) attachment).getSkeleton();
                if (attachmentSkeleton == null) continue;
                Bone bone = slot.bone;
                Bone rootBone = attachmentSkeleton.getRootBone();
                float oldScaleX = rootBone.scaleX;
                float oldScaleY = rootBone.scaleY;
                float oldRotation = rootBone.rotation;
                attachmentSkeleton.setX(skeleton.getX() + bone.worldX);
                attachmentSkeleton.setY(skeleton.getY() + bone.worldY);
                rootBone.scaleX = 1 + bone.worldScaleX - oldScaleX;
                rootBone.scaleY = 1 + bone.worldScaleY - oldScaleY;
                rootBone.rotation = oldRotation + bone.worldRotation;
                attachmentSkeleton.updateWorldTransform();

                draw(batch, attachmentSkeleton);

                attachmentSkeleton.setX(0);
                attachmentSkeleton.setY(0);
                rootBone.scaleX = oldScaleX;
                rootBone.scaleY = oldScaleY;
                rootBone.rotation = oldRotation;
            }
        }
    }

}
