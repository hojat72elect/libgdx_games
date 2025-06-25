

package com.watabou.noosa.tweeners;

public class Delayer extends Tweener {

    public Delayer() {
        super(null, 0);
    }

    public Delayer(float time) {
        super(null, time);
    }

    @Override
    protected void updateValues(float progress) {
    }
}
