

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.ui.Component;

public class Archs extends Component {

    private static final float SCROLL_SPEED = 20f;

    private SkinnedBlock arcsBg;
    private SkinnedBlock arcsFg;
    private Image darkness;

    private static float offsB = 0;
    private static float offsF = 0;

    public boolean reversed = false;

    @Override
    protected void createChildren() {
        arcsBg = new SkinnedBlock(1, 1, Assets.Interfaces.ARCS_BG) {
            @Override
            protected NoosaScript script() {
                return NoosaScriptNoLighting.get();
            }

            @Override
            public void draw() {
                //arch bg has no alpha component, this improves performance
                Blending.disable();
                super.draw();
                Blending.enable();
            }
        };
        arcsBg.autoAdjust = true;
        arcsBg.offsetTo(0, offsB);
        add(arcsBg);

        arcsFg = new SkinnedBlock(1, 1, Assets.Interfaces.ARCS_FG) {
            @Override
            protected NoosaScript script() {
                return NoosaScriptNoLighting.get();
            }
        };
        arcsFg.autoAdjust = true;
        arcsFg.offsetTo(0, offsF);
        add(arcsFg);

        darkness = new Image(TextureCache.createGradient(0x00000000, 0x22000000, 0x55000000, 0x99000000, 0xEE000000));
        darkness.angle = 90;
        add(darkness);
    }

    @Override
    protected void layout() {
        arcsBg.size(width, height);
        arcsBg.offset(arcsBg.texture.width / 4 - (width % arcsBg.texture.width) / 2, 0);

        arcsFg.size(width, height);
        arcsFg.offset(arcsFg.texture.width / 4 - (width % arcsFg.texture.width) / 2, 0);

        darkness.x = width;
        darkness.scale.x = height / 5f;
        darkness.scale.y = width;
    }

    @Override
    public void update() {

        super.update();

        float shift = Game.elapsed * SCROLL_SPEED;
        if (reversed) {
            shift = -shift;
        }

        arcsBg.offset(0, shift);
        arcsFg.offset(0, shift * 2);

        offsB = arcsBg.offsetY();
        offsF = arcsFg.offsetY();
    }
}
