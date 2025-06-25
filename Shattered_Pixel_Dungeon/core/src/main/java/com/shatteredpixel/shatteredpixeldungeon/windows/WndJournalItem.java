

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;

public class WndJournalItem extends WndTitledMessage {

    public WndJournalItem(Image icon, String title, String message) {
        super(icon, title, message);

        PointerArea blocker = new PointerArea(0, 0, PixelScene.uiCamera.width, PixelScene.uiCamera.height) {
            @Override
            protected void onClick(PointerEvent event) {
                onBackPressed();
            }
        };
        blocker.camera = PixelScene.uiCamera;
        add(blocker);
    }
}
