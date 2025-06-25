

package com.watabou.utils;

public class RectF {

    public float left;
    public float top;
    public float right;
    public float bottom;

    public RectF() {
        this(0, 0, 0, 0);
    }

    public RectF(RectF rect) {
        this(rect.left, rect.top, rect.right, rect.bottom);
    }

    public RectF(Rect rect) {
        this(rect.left, rect.top, rect.right, rect.bottom);
    }

    public RectF(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public float width() {
        return right - left;
    }

    public float height() {
        return bottom - top;
    }

    public float square() {
        return width() * height();
    }

    public RectF set(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        return this;
    }

    public RectF set(Rect rect) {
        return set(rect.left, rect.top, rect.right, rect.bottom);
    }

    public RectF setPos(float x, float y) {
        return set(x, y, x + (right - left), y + (bottom - top));
    }

    public RectF shift(float x, float y) {
        return set(left + x, top + y, right + x, bottom + y);
    }

    public RectF resize(float w, float h) {
        return set(left, top, left + w, top + h);
    }

    public boolean isEmpty() {
        return right <= left || bottom <= top;
    }

    public RectF setEmpty() {
        left = right = top = bottom = 0;
        return this;
    }

    public RectF intersect(RectF other) {
        RectF result = new RectF();
        result.left = Math.max(left, other.left);
        result.right = Math.min(right, other.right);
        result.top = Math.max(top, other.top);
        result.bottom = Math.min(bottom, other.bottom);
        return result;
    }

    public RectF union(RectF other) {
        RectF result = new RectF();
        result.left = Math.min(left, other.left);
        result.right = Math.max(right, other.right);
        result.top = Math.min(top, other.top);
        result.bottom = Math.max(bottom, other.bottom);
        return result;
    }

    public RectF union(float x, float y) {
        if (isEmpty()) {
            return set(x, y, x + 1, y + 1);
        } else {
            if (x < left) {
                left = x;
            } else if (x >= right) {
                right = x + 1;
            }
            if (y < top) {
                top = y;
            } else if (y >= bottom) {
                bottom = y + 1;
            }
            return this;
        }
    }

    public RectF union(Point p) {
        return union(p.x, p.y);
    }

    public boolean inside(Point p) {
        return p.x >= left && p.x < right && p.y >= top && p.y < bottom;
    }

    public RectF shrink(float d) {
        return new RectF(left + d, top + d, right - d, bottom - d);
    }

    public RectF shrink() {
        return shrink(1);
    }

    public RectF scale(float d) {
        return new RectF(left * d, top * d, right * d, bottom * d);
    }
}
