

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;

public class RedButton extends StyledButton {

    public RedButton(String label) {
        this(label, 9);
    }

    public RedButton(String label, int size) {
        super(Chrome.Type.RED_BUTTON, label, size);
    }
}
