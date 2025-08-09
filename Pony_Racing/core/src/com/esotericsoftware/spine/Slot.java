package com.esotericsoftware.spine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.FloatArray;
import com.esotericsoftware.spine.attachments.Attachment;

public class Slot {
    final SlotData data;
    final Bone bone;
    final Color color;
    private final Skeleton skeleton;
    private final FloatArray attachmentVertices = new FloatArray();
    Attachment attachment;
    private float attachmentTime;

    public Slot(SlotData data, Skeleton skeleton, Bone bone) {
        if (data == null) throw new IllegalArgumentException("data cannot be null.");
        if (skeleton == null) throw new IllegalArgumentException("skeleton cannot be null.");
        if (bone == null) throw new IllegalArgumentException("bone cannot be null.");
        this.data = data;
        this.skeleton = skeleton;
        this.bone = bone;
        color = new Color();
        setToSetupPose();
    }

    /**
     * Copy constructor.
     */
    public Slot(Slot slot, Skeleton skeleton, Bone bone) {
        if (slot == null) throw new IllegalArgumentException("slot cannot be null.");
        if (skeleton == null) throw new IllegalArgumentException("skeleton cannot be null.");
        if (bone == null) throw new IllegalArgumentException("bone cannot be null.");
        data = slot.data;
        this.skeleton = skeleton;
        this.bone = bone;
        color = new Color(slot.color);
        attachment = slot.attachment;
        attachmentTime = slot.attachmentTime;
    }

    public Skeleton getSkeleton() {
        return skeleton;
    }

    public Bone getBone() {
        return bone;
    }

    public Color getColor() {
        return color;
    }

    /**
     * @return May be null.
     */
    public Attachment getAttachment() {
        return attachment;
    }

    /**
     *
     * @param attachment May be null.
     */
    public void setAttachment(Attachment attachment) {
        if (this.attachment == attachment) return;
        this.attachment = attachment;
        attachmentTime = skeleton.time;
        attachmentVertices.clear();
    }

    public FloatArray getAttachmentVertices() {
        return attachmentVertices;
    }

    void setToSetupPose(int slotIndex) {
        color.set(data.getColor());
        setAttachment(data.getAttachmentName() == null ? null : skeleton.getAttachment(slotIndex, data.getAttachmentName()));
    }

    public void setToSetupPose() {
        setToSetupPose(skeleton.data.slots.indexOf(data, true));
    }

    public String toString() {
        return data.getName();
    }
}
