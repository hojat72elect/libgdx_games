

package com.watabou.input;

import com.watabou.utils.PointF;
import com.watabou.utils.Signal;

import java.util.ArrayList;

public class ScrollEvent {

    public PointF pos;
    public float amount;

    public ScrollEvent(PointF mousePos, float amount) {
        this.amount = amount;
        this.pos = mousePos;
    }

    // **********************
    // *** Static members ***
    // **********************

    private static final Signal<ScrollEvent> scrollSignal = new Signal<>(true);

    public static void addScrollListener(Signal.Listener<ScrollEvent> listener) {
        scrollSignal.add(listener);
    }

    public static void removeScrollListener(Signal.Listener<ScrollEvent> listener) {
        scrollSignal.remove(listener);
    }

    public static void clearListeners() {
        scrollSignal.removeAll();
    }

    //Accumulated key events
    private static final ArrayList<ScrollEvent> scrollEvents = new ArrayList<>();

    public static synchronized void addScrollEvent(ScrollEvent event) {
        scrollEvents.add(event);
    }

    public static synchronized void processScrollEvents() {
        if (scrollEvents.isEmpty()) {
            return;
        }

        for (ScrollEvent k : scrollEvents) {
            scrollSignal.dispatch(k);
        }
        scrollEvents.clear();
    }
}
