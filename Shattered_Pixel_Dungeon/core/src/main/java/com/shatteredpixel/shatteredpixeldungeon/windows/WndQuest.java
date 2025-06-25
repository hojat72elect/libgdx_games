

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class WndQuest extends WndTitledMessage {

    public WndQuest(NPC questgiver, String text) {
        super(questgiver.sprite(), Messages.titleCase(questgiver.name()), text);
    }
}
