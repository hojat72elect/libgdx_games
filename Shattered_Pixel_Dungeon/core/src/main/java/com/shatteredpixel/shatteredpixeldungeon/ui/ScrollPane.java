

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.input.GameAction;
import com.watabou.input.KeyBindings;
import com.watabou.input.KeyEvent;
import com.watabou.input.PointerEvent;
import com.watabou.input.ScrollEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.ScrollArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.GameMath;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Signal;

public class ScrollPane extends Component {

    protected static final int THUMB_COLOR = 0xFF7b8073;
    protected static final float THUMB_ALPHA = 0.5f;

    protected PointerController controller;
    protected Signal.Listener<KeyEvent> keyListener;
    protected Component content;
    protected ColorBlock thumb;

    private float keyScroll = 0;

    public ScrollPane(Component content) {
        super();

        this.content = content;
        addToBack(content);

        width = content.width();
        height = content.height();

        content.camera = new Camera(0, 0, 1, 1, PixelScene.defaultZoom);
        Camera.add(content.camera);

        KeyEvent.addKeyListener(keyListener = new Signal.Listener<KeyEvent>() {
            @Override
            public boolean onSignal(KeyEvent keyEvent) {
                GameAction action = KeyBindings.getActionForKey(keyEvent);
                if (action == SPDAction.ZOOM_IN) {
                    if (keyEvent.pressed) {
                        keyScroll += 1;
                    } else {
                        keyScroll -= 1;
                    }
                    keyScroll = GameMath.gate(-1f, keyScroll, +1f);
                    return true;
                } else if (action == SPDAction.ZOOM_OUT) {
                    if (keyEvent.pressed) {
                        keyScroll -= 1;
                    } else {
                        keyScroll += 1;
                    }
                    keyScroll = GameMath.gate(-1f, keyScroll, +1f);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void destroy() {
        super.destroy();
        Camera.remove(content.camera);
        KeyEvent.removeKeyListener(keyListener);
    }

    public void scrollTo(float x, float y) {
        Camera c = content.camera;
        c.scroll.set(x, y);
        if (c.scroll.x + width > content.width()) {
            c.scroll.x = content.width() - width;
        }
        if (c.scroll.x < 0) {
            c.scroll.x = 0;
        }
        if (c.scroll.y + height > content.height()) {
            c.scroll.y = content.height() - height;
        }
        if (c.scroll.y < 0) {
            c.scroll.y = 0;
        }
        thumb.y = this.y + height * c.scroll.y / content.height();
    }

    @Override
    public synchronized void update() {
        super.update();
        if (keyScroll != 0) {
            scrollTo(content.camera.scroll.x, content.camera.scroll.y + (keyScroll * 150 * Game.elapsed));
        }
    }

    @Override
    protected void createChildren() {
        controller = new PointerController();
        add(controller);

        thumb = new ColorBlock(1, 1, THUMB_COLOR);
        thumb.am = THUMB_ALPHA;
        add(thumb);
    }

    @Override
    protected void layout() {

        content.setPos(0, 0);
        controller.x = x;
        controller.y = y;
        controller.width = width;
        controller.height = height;

        Point p = camera().cameraToScreen(x, y);
        Camera cs = content.camera;
        cs.x = p.x;
        cs.y = p.y;
        cs.resize((int) width, (int) height);

        thumb.visible = height < content.height();
        if (thumb.visible) {
            thumb.scale.set(2, height * height / content.height());
            thumb.x = right() - thumb.width();
            thumb.y = y + height * content.camera.scroll.y / content.height();
        }
    }

    public Component content() {
        return content;
    }

    public void onClick(float x, float y) {
    }

    public class PointerController extends ScrollArea {

        private final float dragThreshold;

        public PointerController() {
            super(0, 0, 0, 0);
            dragThreshold = PixelScene.defaultZoom * 8;
        }

        @Override
        protected void onScroll(ScrollEvent event) {
            PointF newPt = new PointF(lastPos);
            newPt.y -= event.amount * content.camera.zoom * 10;
            scroll(newPt);
            dragging = false;
        }

        @Override
        protected void onPointerUp(PointerEvent event) {
            if (dragging) {

                dragging = false;
                thumb.am = THUMB_ALPHA;
            } else {

                PointF p = content.camera.screenToCamera((int) event.current.x, (int) event.current.y);
                ScrollPane.this.onClick(p.x, p.y);
            }
        }

        private boolean dragging = false;
        private final PointF lastPos = new PointF();

        @Override
        protected void onDrag(PointerEvent event) {
            if (dragging) {

                scroll(event.current);
            } else if (PointF.distance(event.current, event.start) > dragThreshold) {

                dragging = true;
                lastPos.set(event.current);
                thumb.am = 1;
            }
        }

        private void scroll(PointF current) {

            Camera c = content.camera;

            c.shift(PointF.diff(lastPos, current).invScale(c.zoom));
            if (c.scroll.x + width > content.width()) {
                c.scroll.x = content.width() - width;
            }
            if (c.scroll.x < 0) {
                c.scroll.x = 0;
            }
            if (c.scroll.y + height > content.height()) {
                c.scroll.y = content.height() - height;
            }
            if (c.scroll.y < 0) {
                c.scroll.y = 0;
            }

            thumb.y = y + height * c.scroll.y / content.height();

            lastPos.set(current);
        }
    }
}
