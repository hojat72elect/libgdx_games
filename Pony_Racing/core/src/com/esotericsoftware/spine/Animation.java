/******************************************************************************
 * Spine Runtimes Software License
 * Version 2
 *
 * Copyright (c) 2013, Esoteric Software
 * All rights reserved.
 *
 * You are granted a perpetual, non-exclusive, non-sublicensable and
 * non-transferable license to install, execute and perform the Spine Runtimes
 * Software (the "Software") solely for internal use. Without the written
 * permission of Esoteric Software, you may not (a) modify, translate, adapt or
 * otherwise create derivative works, improvements of the Software or develop
 * new applications using the Software or (b) remove, delete, alter or obscure
 * any trademarks or any copyright, trademark, patent or other intellectual
 * property or proprietary rights notices on or in the Software, including
 * any copy thereof. Redistributions in binary or source form must include
 * this license and terms. THIS SOFTWARE IS PROVIDED BY ESOTERIC SOFTWARE
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL ESOTERIC SOFTARE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *****************************************************************************/

package com.esotericsoftware.spine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.esotericsoftware.spine.attachments.MeshAttachment;

public class Animation {
    final String name;
    private final Array<Timeline> timelines;
    private float duration;

    public Animation(String name, Array<Timeline> timelines, float duration) {
        if (name == null) throw new IllegalArgumentException("name cannot be null.");
        if (timelines == null) throw new IllegalArgumentException("timelines cannot be null.");
        this.name = name;
        this.timelines = timelines;
        this.duration = duration;
    }

    /**
     * @param target After the first and before the last value.
     * @return index of first value greater than the target.
     */
    static int binarySearch(float[] values, float target, int step) {
        int low = 0;
        int high = values.length / step - 2;
        if (high == 0) return step;
        int current = high >>> 1;
        while (true) {
            if (values[(current + 1) * step] <= target)
                low = current + 1;
            else
                high = current;
            if (low == high) return (low + 1) * step;
            current = (low + high) >>> 1;
        }
    }

    static int linearSearch(float[] values, float target, int step) {
        for (int i = 0, last = values.length - step; i <= last; i += step)
            if (values[i] > target) return i;
        return -1;
    }

    public Array<Timeline> getTimelines() {
        return timelines;
    }

    /**
     * Returns the duration of the animation in seconds.
     */
    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    /**
     * Poses the skeleton at the specified time for this animation.
     *
     * @param lastTime The last time the animation was applied.
     * @param events   Any triggered events are added.
     */
    public void apply(Skeleton skeleton, float lastTime, float time, boolean loop, Array<Event> events) {
        if (skeleton == null) throw new IllegalArgumentException("skeleton cannot be null.");

        if (loop && duration != 0) {
            time %= duration;
            lastTime %= duration;
        }

        Array<Timeline> timelines = this.timelines;
        for (int i = 0, n = timelines.size; i < n; i++)
            timelines.get(i).apply(skeleton, lastTime, time, events, 1);
    }

    /**
     * Poses the skeleton at the specified time for this animation mixed with the current pose.
     *
     * @param lastTime The last time the animation was applied.
     * @param events   Any triggered events are added.
     * @param alpha    The amount of this animation that affects the current pose.
     */
    public void mix(Skeleton skeleton, float lastTime, float time, boolean loop, Array<Event> events, float alpha) {
        if (skeleton == null) throw new IllegalArgumentException("skeleton cannot be null.");

        if (loop && duration != 0) {
            lastTime %= duration;
            time %= duration;
        }

        Array<Timeline> timelines = this.timelines;
        for (int i = 0, n = timelines.size; i < n; i++)
            timelines.get(i).apply(skeleton, lastTime, time, events, alpha);
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public interface Timeline {
        /**
         * Sets the value(s) for the specified time.
         */
        void apply(Skeleton skeleton, float lastTime, float time, Array<Event> events, float alpha);
    }

    /**
     * Base class for frames that use an interpolation bezier curve.
     */
    abstract static public class CurveTimeline implements Timeline {
        static public final float LINEAR = 0;
        static public final float STEPPED = -1;
        static public final float BEZIER = -2;
        static private final int BEZIER_SEGMENTS = 10;

        private final float[] curves; // dfx, dfy, ddfx, ddfy, dddfx, dddfy, ...

        public CurveTimeline(int frameCount) {
            curves = new float[(frameCount - 1) * 6];
        }

        public int getFrameCount() {
            return curves.length / 6 + 1;
        }

        public void setLinear(int frameIndex) {
            curves[frameIndex * 6] = LINEAR;
        }

        public void setStepped(int frameIndex) {
            curves[frameIndex * 6] = STEPPED;
        }

        public float getCurveType(int frameIndex) {
            int index = frameIndex * 6;
            if (index == curves.length) return LINEAR;
            float type = curves[index];
            if (type == LINEAR) return LINEAR;
            if (type == STEPPED) return STEPPED;
            return BEZIER;
        }

        /**
         * Sets the control handle positions for an interpolation bezier curve used to transition from this keyframe to the next.
         * cx1 and cx2 are from 0 to 1, representing the percent of time between the two keyframes. cy1 and cy2 are the percent of
         * the difference between the keyframe's values.
         */
        public void setCurve(int frameIndex, float cx1, float cy1, float cx2, float cy2) {
            float subdiv_step = 1f / BEZIER_SEGMENTS;
            float subdiv_step2 = subdiv_step * subdiv_step;
            float subdiv_step3 = subdiv_step2 * subdiv_step;
            float pre1 = 3 * subdiv_step;
            float pre2 = 3 * subdiv_step2;
            float pre4 = 6 * subdiv_step2;
            float pre5 = 6 * subdiv_step3;
            float tmp1x = -cx1 * 2 + cx2;
            float tmp1y = -cy1 * 2 + cy2;
            float tmp2x = (cx1 - cx2) * 3 + 1;
            float tmp2y = (cy1 - cy2) * 3 + 1;
            int i = frameIndex * 6;
            float[] curves = this.curves;
            curves[i] = cx1 * pre1 + tmp1x * pre2 + tmp2x * subdiv_step3;
            curves[i + 1] = cy1 * pre1 + tmp1y * pre2 + tmp2y * subdiv_step3;
            curves[i + 2] = tmp1x * pre4 + tmp2x * pre5;
            curves[i + 3] = tmp1y * pre4 + tmp2y * pre5;
            curves[i + 4] = tmp2x * pre5;
            curves[i + 5] = tmp2y * pre5;
        }

        public float getCurvePercent(int frameIndex, float percent) {
            int curveIndex = frameIndex * 6;
            float[] curves = this.curves;
            float dfx = curves[curveIndex];
            if (dfx == LINEAR) return percent;
            if (dfx == STEPPED) return 0;
            float dfy = curves[curveIndex + 1];
            float ddfx = curves[curveIndex + 2];
            float ddfy = curves[curveIndex + 3];
            float dddfx = curves[curveIndex + 4];
            float dddfy = curves[curveIndex + 5];
            float x = dfx, y = dfy;
            int i = BEZIER_SEGMENTS - 2;
            while (true) {
                if (x >= percent) {
                    float prevX = x - dfx;
                    float prevY = y - dfy;
                    return prevY + (y - prevY) * (percent - prevX) / (x - prevX);
                }
                if (i == 0) break;
                i--;
                dfx += ddfx;
                dfy += ddfy;
                ddfx += dddfx;
                ddfy += dddfy;
                x += dfx;
                y += dfy;
            }
            return y + (1 - y) * (percent - x) / (1 - x); // Last point is 1,1.
        }
    }

    static public class RotateTimeline extends CurveTimeline {
        static private final int PREV_FRAME_TIME = -2;
        static private final int FRAME_VALUE = 1;
        private final float[] frames; // time, angle, ...
        int boneIndex;

        public RotateTimeline(int frameCount) {
            super(frameCount);
            frames = new float[frameCount * 2];
        }

        public int getBoneIndex() {
            return boneIndex;
        }

        public void setBoneIndex(int boneIndex) {
            this.boneIndex = boneIndex;
        }

        public float[] getFrames() {
            return frames;
        }

        /**
         * Sets the time and angle of the specified keyframe.
         */
        public void setFrame(int frameIndex, float time, float angle) {
            frameIndex *= 2;
            frames[frameIndex] = time;
            frames[frameIndex + 1] = angle;
        }

        public void apply(Skeleton skeleton, float lastTime, float time, Array<Event> events, float alpha) {
            float[] frames = this.frames;
            if (time < frames[0]) return; // Time is before first frame.

            Bone bone = skeleton.bones.get(boneIndex);

            if (time >= frames[frames.length - 2]) { // Time is after last frame.
                float amount = bone.data.rotation + frames[frames.length - 1] - bone.rotation;
                while (amount > 180)
                    amount -= 360;
                while (amount < -180)
                    amount += 360;
                bone.rotation += amount * alpha;
                return;
            }

            // Interpolate between the previous frame and the current frame.
            int frameIndex = binarySearch(frames, time, 2);
            float prevFrameValue = frames[frameIndex - 1];
            float frameTime = frames[frameIndex];
            float percent = MathUtils.clamp(1 - (time - frameTime) / (frames[frameIndex + PREV_FRAME_TIME] - frameTime), 0, 1);
            percent = getCurvePercent(frameIndex / 2 - 1, percent);

            float amount = frames[frameIndex + FRAME_VALUE] - prevFrameValue;
            while (amount > 180)
                amount -= 360;
            while (amount < -180)
                amount += 360;
            amount = bone.data.rotation + (prevFrameValue + amount * percent) - bone.rotation;
            while (amount > 180)
                amount -= 360;
            while (amount < -180)
                amount += 360;
            bone.rotation += amount * alpha;
        }
    }

    static public class TranslateTimeline extends CurveTimeline {
        static final int PREV_FRAME_TIME = -3;
        static final int FRAME_X = 1;
        static final int FRAME_Y = 2;
        final float[] frames; // time, x, y, ...
        int boneIndex;

        public TranslateTimeline(int frameCount) {
            super(frameCount);
            frames = new float[frameCount * 3];
        }

        public int getBoneIndex() {
            return boneIndex;
        }

        public void setBoneIndex(int boneIndex) {
            this.boneIndex = boneIndex;
        }

        public float[] getFrames() {
            return frames;
        }

        /**
         * Sets the time and value of the specified keyframe.
         */
        public void setFrame(int frameIndex, float time, float x, float y) {
            frameIndex *= 3;
            frames[frameIndex] = time;
            frames[frameIndex + 1] = x;
            frames[frameIndex + 2] = y;
        }

        public void apply(Skeleton skeleton, float lastTime, float time, Array<Event> events, float alpha) {
            float[] frames = this.frames;
            if (time < frames[0]) return; // Time is before first frame.

            Bone bone = skeleton.bones.get(boneIndex);

            if (time >= frames[frames.length - 3]) { // Time is after last frame.
                bone.x += (bone.data.x + frames[frames.length - 2] - bone.x) * alpha;
                bone.y += (bone.data.y + frames[frames.length - 1] - bone.y) * alpha;
                return;
            }

            // Interpolate between the previous frame and the current frame.
            int frameIndex = binarySearch(frames, time, 3);
            float prevFrameX = frames[frameIndex - 2];
            float prevFrameY = frames[frameIndex - 1];
            float frameTime = frames[frameIndex];
            float percent = MathUtils.clamp(1 - (time - frameTime) / (frames[frameIndex + PREV_FRAME_TIME] - frameTime), 0, 1);
            percent = getCurvePercent(frameIndex / 3 - 1, percent);

            bone.x += (bone.data.x + prevFrameX + (frames[frameIndex + FRAME_X] - prevFrameX) * percent - bone.x) * alpha;
            bone.y += (bone.data.y + prevFrameY + (frames[frameIndex + FRAME_Y] - prevFrameY) * percent - bone.y) * alpha;
        }
    }

    static public class ScaleTimeline extends TranslateTimeline {
        public ScaleTimeline(int frameCount) {
            super(frameCount);
        }

        public void apply(Skeleton skeleton, float lastTime, float time, Array<Event> events, float alpha) {
            float[] frames = this.frames;
            if (time < frames[0]) return; // Time is before first frame.

            Bone bone = skeleton.bones.get(boneIndex);
            if (time >= frames[frames.length - 3]) { // Time is after last frame.
                bone.scaleX += (bone.data.scaleX - 1 + frames[frames.length - 2] - bone.scaleX) * alpha;
                bone.scaleY += (bone.data.scaleY - 1 + frames[frames.length - 1] - bone.scaleY) * alpha;
                return;
            }

            // Interpolate between the previous frame and the current frame.
            int frameIndex = binarySearch(frames, time, 3);
            float prevFrameX = frames[frameIndex - 2];
            float prevFrameY = frames[frameIndex - 1];
            float frameTime = frames[frameIndex];
            float percent = MathUtils.clamp(1 - (time - frameTime) / (frames[frameIndex + PREV_FRAME_TIME] - frameTime), 0, 1);
            percent = getCurvePercent(frameIndex / 3 - 1, percent);

            bone.scaleX += (bone.data.scaleX - 1 + prevFrameX + (frames[frameIndex + FRAME_X] - prevFrameX) * percent - bone.scaleX)
                    * alpha;
            bone.scaleY += (bone.data.scaleY - 1 + prevFrameY + (frames[frameIndex + FRAME_Y] - prevFrameY) * percent - bone.scaleY)
                    * alpha;
        }
    }

    static public class ColorTimeline extends CurveTimeline {
        static private final int PREV_FRAME_TIME = -5;
        static private final int FRAME_R = 1;
        static private final int FRAME_G = 2;
        static private final int FRAME_B = 3;
        static private final int FRAME_A = 4;
        private final float[] frames; // time, r, g, b, a, ...
        int slotIndex;

        public ColorTimeline(int frameCount) {
            super(frameCount);
            frames = new float[frameCount * 5];
        }

        public int getSlotIndex() {
            return slotIndex;
        }

        public void setSlotIndex(int slotIndex) {
            this.slotIndex = slotIndex;
        }

        public float[] getFrames() {
            return frames;
        }

        /**
         * Sets the time and value of the specified keyframe.
         */
        public void setFrame(int frameIndex, float time, float r, float g, float b, float a) {
            frameIndex *= 5;
            frames[frameIndex] = time;
            frames[frameIndex + 1] = r;
            frames[frameIndex + 2] = g;
            frames[frameIndex + 3] = b;
            frames[frameIndex + 4] = a;
        }

        public void apply(Skeleton skeleton, float lastTime, float time, Array<Event> events, float alpha) {
            float[] frames = this.frames;
            if (time < frames[0]) return; // Time is before first frame.

            Color color = skeleton.slots.get(slotIndex).color;

            if (time >= frames[frames.length - 5]) { // Time is after last frame.
                int i = frames.length - 1;
                float r = frames[i - 3];
                float g = frames[i - 2];
                float b = frames[i - 1];
                float a = frames[i];
                color.set(r, g, b, a);
                return;
            }

            // Interpolate between the previous frame and the current frame.
            int frameIndex = binarySearch(frames, time, 5);
            float prevFrameR = frames[frameIndex - 4];
            float prevFrameG = frames[frameIndex - 3];
            float prevFrameB = frames[frameIndex - 2];
            float prevFrameA = frames[frameIndex - 1];
            float frameTime = frames[frameIndex];
            float percent = MathUtils.clamp(1 - (time - frameTime) / (frames[frameIndex + PREV_FRAME_TIME] - frameTime), 0, 1);
            percent = getCurvePercent(frameIndex / 5 - 1, percent);

            float r = prevFrameR + (frames[frameIndex + FRAME_R] - prevFrameR) * percent;
            float g = prevFrameG + (frames[frameIndex + FRAME_G] - prevFrameG) * percent;
            float b = prevFrameB + (frames[frameIndex + FRAME_B] - prevFrameB) * percent;
            float a = prevFrameA + (frames[frameIndex + FRAME_A] - prevFrameA) * percent;
            if (alpha < 1)
                color.add((r - color.r) * alpha, (g - color.g) * alpha, (b - color.b) * alpha, (a - color.a) * alpha);
            else
                color.set(r, g, b, a);
        }
    }

    static public class AttachmentTimeline implements Timeline {
        private final float[] frames; // time, ...
        private final String[] attachmentNames;
        int slotIndex;

        public AttachmentTimeline(int frameCount) {
            frames = new float[frameCount];
            attachmentNames = new String[frameCount];
        }

        public int getFrameCount() {
            return frames.length;
        }

        public int getSlotIndex() {
            return slotIndex;
        }

        public void setSlotIndex(int slotIndex) {
            this.slotIndex = slotIndex;
        }

        public float[] getFrames() {
            return frames;
        }

        public String[] getAttachmentNames() {
            return attachmentNames;
        }

        /**
         * Sets the time and value of the specified keyframe.
         */
        public void setFrame(int frameIndex, float time, String attachmentName) {
            frames[frameIndex] = time;
            attachmentNames[frameIndex] = attachmentName;
        }

        public void apply(Skeleton skeleton, float lastTime, float time, Array<Event> events, float alpha) {
            float[] frames = this.frames;
            if (time < frames[0]) return; // Time is before first frame.

            int frameIndex;
            if (time >= frames[frames.length - 1]) // Time is after last frame.
                frameIndex = frames.length - 1;
            else
                frameIndex = binarySearch(frames, time, 1) - 1;

            String attachmentName = attachmentNames[frameIndex];
            skeleton.slots.get(slotIndex).setAttachment(
                    attachmentName == null ? null : skeleton.getAttachment(slotIndex, attachmentName));
        }
    }

    static public class EventTimeline implements Timeline {
        private final float[] frames; // time, ...
        private final Event[] events;

        public EventTimeline(int frameCount) {
            frames = new float[frameCount];
            events = new Event[frameCount];
        }

        public int getFrameCount() {
            return frames.length;
        }

        public float[] getFrames() {
            return frames;
        }

        public Event[] getEvents() {
            return events;
        }

        /**
         * Sets the time of the specified keyframe.
         */
        public void setFrame(int frameIndex, float time, Event event) {
            frames[frameIndex] = time;
            events[frameIndex] = event;
        }

        /**
         * Fires events for frames > lastTime and <= time.
         */
        public void apply(Skeleton skeleton, float lastTime, float time, Array<Event> firedEvents, float alpha) {
            if (firedEvents == null) return;
            float[] frames = this.frames;
            int frameCount = frames.length;

            if (lastTime > time) { // Fire events after last time for looped animations.
                apply(skeleton, lastTime, Integer.MAX_VALUE, firedEvents, alpha);
                lastTime = -1f;
            } else if (lastTime >= frames[frameCount - 1]) // Last time is after last frame.
                return;
            if (time < frames[0]) return; // Time is before first frame.

            int frameIndex;
            if (lastTime < frames[0])
                frameIndex = 0;
            else {
                frameIndex = binarySearch(frames, lastTime, 1);
                float frame = frames[frameIndex];
                while (frameIndex > 0) { // Fire multiple events with the same frame.
                    if (frames[frameIndex - 1] != frame) break;
                    frameIndex--;
                }
            }
            for (; frameIndex < frameCount && time >= frames[frameIndex]; frameIndex++)
                firedEvents.add(events[frameIndex]);
        }
    }

    static public class DrawOrderTimeline implements Timeline {
        private final float[] frames; // time, ...
        private final int[][] drawOrders;

        public DrawOrderTimeline(int frameCount) {
            frames = new float[frameCount];
            drawOrders = new int[frameCount][];
        }

        public int getFrameCount() {
            return frames.length;
        }

        public float[] getFrames() {
            return frames;
        }

        public int[][] getDrawOrders() {
            return drawOrders;
        }

        /**
         * Sets the time of the specified keyframe.
         *
         * @param drawOrder May be null to use bind pose draw order.
         */
        public void setFrame(int frameIndex, float time, int[] drawOrder) {
            frames[frameIndex] = time;
            drawOrders[frameIndex] = drawOrder;
        }

        public void apply(Skeleton skeleton, float lastTime, float time, Array<Event> firedEvents, float alpha) {
            float[] frames = this.frames;
            if (time < frames[0]) return; // Time is before first frame.

            int frameIndex;
            if (time >= frames[frames.length - 1]) // Time is after last frame.
                frameIndex = frames.length - 1;
            else
                frameIndex = binarySearch(frames, time, 1) - 1;

            Array<Slot> drawOrder = skeleton.drawOrder;
            Array<Slot> slots = skeleton.slots;
            int[] drawOrderToSetupIndex = drawOrders[frameIndex];
            if (drawOrderToSetupIndex == null)
                System.arraycopy(slots.items, 0, drawOrder.items, 0, slots.size);
            else {
                for (int i = 0, n = drawOrderToSetupIndex.length; i < n; i++)
                    drawOrder.set(i, slots.get(drawOrderToSetupIndex[i]));
            }
        }
    }

    static public class FfdTimeline extends CurveTimeline {
        private final float[] frames; // time, ...
        private final float[][] frameVertices;
        int slotIndex;
        MeshAttachment meshAttachment;

        public FfdTimeline(int frameCount) {
            super(frameCount);
            frames = new float[frameCount];
            frameVertices = new float[frameCount][];
        }

        public int getSlotIndex() {
            return slotIndex;
        }

        public void setSlotIndex(int slotIndex) {
            this.slotIndex = slotIndex;
        }

        public MeshAttachment getMeshAttachment() {
            return meshAttachment;
        }

        public void setMeshAttachment(MeshAttachment attachment) {
            this.meshAttachment = attachment;
        }

        public float[] getFrames() {
            return frames;
        }

        public float[][] getVertices() {
            return frameVertices;
        }

        /**
         * Sets the time of the specified keyframe.
         */
        public void setFrame(int frameIndex, float time, float[] vertices) {
            frames[frameIndex] = time;
            frameVertices[frameIndex] = vertices;
        }

        public void apply(Skeleton skeleton, float lastTime, float time, Array<Event> firedEvents, float alpha) {
            Slot slot = skeleton.slots.get(slotIndex);
            if (slot.getAttachment() != meshAttachment) return;

            FloatArray verticesArray = slot.getAttachmentVertices();
            verticesArray.size = 0;

            float[] frames = this.frames;
            if (time < frames[0]) return; // Time is before first frame.

            float[][] frameVertices = this.frameVertices;
            int vertexCount = frameVertices[0].length;
            verticesArray.ensureCapacity(vertexCount);
            verticesArray.size = vertexCount;
            float[] vertices = verticesArray.items;

            if (time >= frames[frames.length - 1]) { // Time is after last frame.
                System.arraycopy(frameVertices[frames.length - 1], 0, vertices, 0, vertexCount);
                return;
            }

            // Interpolate between the previous frame and the current frame.
            int frameIndex = binarySearch(frames, time, 1);
            float frameTime = frames[frameIndex];
            float percent = MathUtils.clamp(1 - (time - frameTime) / (frames[frameIndex - 1] - frameTime), 0, 1);
            percent = getCurvePercent(frameIndex - 1, percent);

            float[] prevVertices = frameVertices[frameIndex - 1];
            float[] nextVertices = frameVertices[frameIndex];

            // BOZO - FFD, use alpha for mixing?
            for (int i = 0; i < vertexCount; i++) {
                float prev = prevVertices[i];
                vertices[i] = prev + (nextVertices[i] - prev) * percent;
            }
        }
    }
}
