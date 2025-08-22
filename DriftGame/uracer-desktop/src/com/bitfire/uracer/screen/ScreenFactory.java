package com.bitfire.uracer.screen;

public interface ScreenFactory {
    Screen createScreen(ScreenId screenId);

    interface ScreenId {
        int id();
    }
}
