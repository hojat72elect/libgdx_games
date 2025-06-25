

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;

public class WndError extends WndTitledMessage {

    public WndError(String message) {
        super(Icons.WARNING.get(), Messages.get(WndError.class, "title"), message);
    }
}
