package com.esotericsoftware.spine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.attachments.Attachment;

public class Skeleton {
    final SkeletonData data;
    final Array<Bone> bones;
    final Array<Slot> slots;
    final Color color;
    Array<Slot> drawOrder;
    Skin skin;
    float time;
    boolean flipX, flipY;
    float x, y;

    public Skeleton(SkeletonData data) {
        if (data == null) throw new IllegalArgumentException("data cannot be null.");
        this.data = data;

        bones = new Array(data.bones.size);
        for (BoneData boneData : data.bones) {
            Bone parent = boneData.getParent() == null ? null : bones.get(data.bones.indexOf(boneData.getParent(), true));
            bones.add(new Bone(boneData, parent));
        }

        slots = new Array(data.slots.size);
        drawOrder = new Array(data.slots.size);
        for (SlotData slotData : data.slots) {
            Bone bone = bones.get(data.bones.indexOf(slotData.getBoneData(), true));
            Slot slot = new Slot(slotData, this, bone);
            slots.add(slot);
            drawOrder.add(slot);
        }

        color = new Color(1, 1, 1, 1);
    }

    /**
     * Copy constructor.
     */
    public Skeleton(Skeleton skeleton) {
        if (skeleton == null) throw new IllegalArgumentException("skeleton cannot be null.");
        data = skeleton.data;

        bones = new Array(skeleton.bones.size);
        for (Bone bone : skeleton.bones) {
            Bone parent = bone.parent == null ? null : bones.get(skeleton.bones.indexOf(bone.parent, true));
            bones.add(new Bone(bone, parent));
        }

        slots = new Array(skeleton.slots.size);
        for (Slot slot : skeleton.slots) {
            Bone bone = bones.get(skeleton.bones.indexOf(slot.bone, true));
            Slot newSlot = new Slot(slot, this, bone);
            slots.add(newSlot);
        }

        drawOrder = new Array(slots.size);
        for (Slot slot : skeleton.drawOrder)
            drawOrder.add(slots.get(skeleton.slots.indexOf(slot, true)));

        skin = skeleton.skin;
        color = new Color(skeleton.color);
        time = skeleton.time;
    }

    /**
     * Updates the world transform for each bone.
     */
    public void updateWorldTransform() {
        boolean flipX = this.flipX;
        boolean flipY = this.flipY;
        Array<Bone> bones = this.bones;
        for (int i = 0, n = bones.size; i < n; i++)
            bones.get(i).updateWorldTransform(flipX, flipY);
    }

    /**
     * Sets the bones and slots to their setup pose values.
     */
    public void setToSetupPose() {
        setBonesToSetupPose();
        setSlotsToSetupPose();
    }

    public void setBonesToSetupPose() {
        Array<Bone> bones = this.bones;
        for (int i = 0, n = bones.size; i < n; i++)
            bones.get(i).setToSetupPose();
    }

    public void setSlotsToSetupPose() {
        Array<Slot> slots = this.slots;
        System.arraycopy(slots.items, 0, drawOrder.items, 0, slots.size);
        for (int i = 0, n = slots.size; i < n; i++)
            slots.get(i).setToSetupPose(i);
    }

    public SkeletonData getData() {
        return data;
    }

    /**
     * @return May return null.
     */
    public Bone getRootBone() {
        if (bones.size == 0) return null;
        return bones.first();
    }

    /**
     * Sets a skin by name.
     *
     * @see #setSkin(Skin)
     */
    public void setSkin(String skinName) {
        Skin skin = data.findSkin(skinName);
        if (skin == null) throw new IllegalArgumentException("Skin not found: " + skinName);
        setSkin(skin);
    }

    /**
     * Sets the skin used to look up attachments not found in the {@link SkeletonData#getDefaultSkin() default skin}. Attachments
     * from the new skin are attached if the corresponding attachment from the old skin was attached.
     *
     * @param newSkin May be null.
     */
    public void setSkin(Skin newSkin) {
        if (skin != null && newSkin != null) newSkin.attachAll(this, skin);
        skin = newSkin;
    }

    /**
     * @return May be null.
     */
    public Attachment getAttachment(int slotIndex, String attachmentName) {
        if (attachmentName == null) throw new IllegalArgumentException("attachmentName cannot be null.");
        if (skin != null) {
            Attachment attachment = skin.getAttachment(slotIndex, attachmentName);
            if (attachment != null) return attachment;
        }
        if (data.defaultSkin != null) return data.defaultSkin.getAttachment(slotIndex, attachmentName);
        return null;
    }

    public Color getColor() {
        return color;
    }

    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public void update(float delta) {
        time += delta;
    }

    public String toString() {
        return data.name != null ? data.name : super.toString();
    }
}
