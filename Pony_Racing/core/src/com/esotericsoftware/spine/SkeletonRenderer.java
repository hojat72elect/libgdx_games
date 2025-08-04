

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

    private boolean premultipliedAlpha;

    public void draw(PolygonSpriteBatch batch, Skeleton skeleton) {
        boolean premultipliedAlpha = this.premultipliedAlpha;
        int srcFunc = premultipliedAlpha ? GL20.GL_ONE : GL20.GL_SRC_ALPHA;
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
                Bone bone = slot.getBone();
                Bone rootBone = attachmentSkeleton.getRootBone();
                float oldScaleX = rootBone.getScaleX();
                float oldScaleY = rootBone.getScaleY();
                float oldRotation = rootBone.getRotation();
                attachmentSkeleton.setX(skeleton.getX() + bone.getWorldX());
                attachmentSkeleton.setY(skeleton.getY() + bone.getWorldY());
                rootBone.setScaleX(1 + bone.getWorldScaleX() - oldScaleX);
                rootBone.setScaleY(1 + bone.getWorldScaleY() - oldScaleY);
                rootBone.setRotation(oldRotation + bone.getWorldRotation());
                attachmentSkeleton.updateWorldTransform();

                draw(batch, attachmentSkeleton);

                attachmentSkeleton.setX(0);
                attachmentSkeleton.setY(0);
                rootBone.setScaleX(oldScaleX);
                rootBone.setScaleY(oldScaleY);
                rootBone.setRotation(oldRotation);
            }
        }
    }

    public void draw(Batch batch, Skeleton skeleton) {
        boolean premultipliedAlpha = this.premultipliedAlpha;
        int srcFunc = premultipliedAlpha ? GL20.GL_ONE : GL20.GL_SRC_ALPHA;
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
                Bone bone = slot.getBone();
                Bone rootBone = attachmentSkeleton.getRootBone();
                float oldScaleX = rootBone.getScaleX();
                float oldScaleY = rootBone.getScaleY();
                float oldRotation = rootBone.getRotation();
                attachmentSkeleton.setX(skeleton.getX() + bone.getWorldX());
                attachmentSkeleton.setY(skeleton.getY() + bone.getWorldY());
                rootBone.setScaleX(1 + bone.getWorldScaleX() - oldScaleX);
                rootBone.setScaleY(1 + bone.getWorldScaleY() - oldScaleY);
                rootBone.setRotation(oldRotation + bone.getWorldRotation());
                attachmentSkeleton.updateWorldTransform();

                draw(batch, attachmentSkeleton);

                attachmentSkeleton.setX(0);
                attachmentSkeleton.setY(0);
                rootBone.setScaleX(oldScaleX);
                rootBone.setScaleY(oldScaleY);
                rootBone.setRotation(oldRotation);
            }
        }
    }

    public void setPremultipliedAlpha(boolean premultipliedAlpha) {
        this.premultipliedAlpha = premultipliedAlpha;
    }
}
