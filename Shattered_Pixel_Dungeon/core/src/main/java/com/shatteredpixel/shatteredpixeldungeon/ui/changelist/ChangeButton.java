

package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

//not actually a button, but functions as one.
public class ChangeButton extends Component {

    protected Image icon;
    protected String title;
    protected String[] messages;

    public ChangeButton(Image icon, String title, String... messages) {
        super();

        this.icon = icon;
        add(this.icon);

        this.title = Messages.titleCase(title);
        this.messages = messages;

        layout();
    }

    public ChangeButton(Item item, String message) {
        this(new ItemSprite(item), item.name(), message);
    }

    protected void onClick() {
        ChangesScene.showChangeInfo(new Image(icon), title, messages);
    }

    @Override
    protected void layout() {
        super.layout();

        icon.x = x + (width - icon.width()) / 2f;
        icon.y = y + (height - icon.height()) / 2f;
        PixelScene.align(icon);
    }
}
