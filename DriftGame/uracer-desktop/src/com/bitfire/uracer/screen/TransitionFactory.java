package com.bitfire.uracer.screen;

import com.badlogic.gdx.utils.LongMap;
import com.bitfire.uracer.screen.transitions.CrossFader;
import com.bitfire.uracer.screen.transitions.Fader;
import com.bitfire.utils.Hash;

public final class TransitionFactory {

    private static final LongMap<ScreenTransition> transitions = new LongMap<ScreenTransition>();
    private static ScreenFactory screenFactory = null;

    private TransitionFactory() {
    }

    public static void init(ScreenFactory factory) {
        screenFactory = factory;
    }

    public static ScreenTransition getTransition(TransitionType transitionType) {
        ScreenTransition transition = transitions.get(transitionType.hash);

        if (transition == null) {
            transition = createTransition(transitionType);
            transitions.put(transitionType.hash, transition);
        } else {
            transition.reset();
        }

        return transition;
    }

    private static ScreenTransition createTransition(TransitionType transitionType) {
        switch (transitionType) {
            case CrossFader:
                return new CrossFader(screenFactory);
            case Fader:
                return new Fader(screenFactory);
            default:
            case None:
                return null;
        }
    }

    public static void dispose() {
        for (ScreenTransition t : transitions.values()) {
            t.dispose();
        }
    }

    public enum TransitionType {
        None, CrossFader, Fader;

        public long hash;

        TransitionType() {
            hash = Hash.APHash(this.name());
        }
    }
}
