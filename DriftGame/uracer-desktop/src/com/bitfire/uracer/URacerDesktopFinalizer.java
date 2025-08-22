package com.bitfire.uracer;

import com.badlogic.gdx.backends.lwjgl.audio.OpenALAudio;
import com.bitfire.uracer.URacer.URacerFinalizer;
import com.bitfire.uracer.configuration.BootConfig;

import org.lwjgl.opengl.Display;

public class URacerDesktopFinalizer implements URacerFinalizer {
    BootConfig boot = null;
    private OpenALAudio audio = null;

    public URacerDesktopFinalizer(BootConfig boot, OpenALAudio audio) {
        this.boot = boot;
        this.audio = audio;
    }

    @Override
    public void dispose() {
        boot.load();
        boot.setWindowX(Display.getX());
        boot.setWindowY(Display.getY());
        boot.store();

        // destroy display
        Display.destroy();

        // destroy audio, if any
        if (this.audio != null) {
            this.audio.dispose();
            this.audio = null;
        }
    }
}
