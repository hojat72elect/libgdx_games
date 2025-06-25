

package com.watabou.noosa;

import com.watabou.input.ScrollEvent;
import com.watabou.utils.Signal;

//pointer area with additional support for detecting scrolling events
public class ScrollArea extends PointerArea {

    public ScrollArea(Visual target) {
        super(target);
        ScrollEvent.addScrollListener(scrollListener);
    }

    public ScrollArea(float x, float y, float width, float height) {
        super(x, y, width, height);
        ScrollEvent.addScrollListener(scrollListener);
    }

    private final Signal.Listener<ScrollEvent> scrollListener = new Signal.Listener<ScrollEvent>() {
        @Override
        public boolean onSignal(ScrollEvent event) {

            boolean hit = event != null && target.overlapsScreenPoint((int) event.pos.x, (int) event.pos.y);

            if (!isActive()) {
                return (hit && blockLevel == ALWAYS_BLOCK);
            }

            if (hit) {
                onScroll(event);
                return true;
            }
            return false;
        }
    };

    protected void onScroll(ScrollEvent event) {
    }

    @Override
    public void destroy() {
        super.destroy();
        ScrollEvent.removeScrollListener(scrollListener);
    }
}
