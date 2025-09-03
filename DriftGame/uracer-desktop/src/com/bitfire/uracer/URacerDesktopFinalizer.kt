package com.bitfire.uracer

import com.badlogic.gdx.backends.lwjgl.audio.OpenALAudio
import com.bitfire.uracer.URacer.URacerFinalizer
import com.bitfire.uracer.configuration.BootConfig
import org.lwjgl.opengl.Display

class URacerDesktopFinalizer(val boot: BootConfig, private var audio: OpenALAudio?) : URacerFinalizer {

    override fun dispose() {
        boot.load()
        boot.windowX = Display.getX()
        boot.windowY = Display.getY()
        boot.store()

        // destroy display
        Display.destroy()

        // destroy audio, if any
        if (this.audio != null) {
            this.audio?.dispose()
            this.audio = null
        }
    }
}
