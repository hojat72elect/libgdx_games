package me.vrekt.oasis.world.tutorial.interior.wrynn;

import com.badlogic.gdx.math.Rectangle;

import me.vrekt.oasis.asset.game.Asset;
import me.vrekt.oasis.gui.input.Cursor;
import me.vrekt.oasis.questing.quests.QuestType;
import me.vrekt.oasis.world.GameWorld;
import me.vrekt.oasis.world.GameWorldInterior;
import me.vrekt.oasis.world.interior.Interior;
import me.vrekt.oasis.world.obj.interaction.WorldInteractionType;
import me.vrekt.oasis.world.obj.interaction.impl.container.WrynnOfficeContainerInteraction;
import me.vrekt.oasis.world.obj.interaction.impl.misc.WrynnLeverInteraction;

/**
 * Wrynn tutorial house.
 */
public final class WrynnHouseInterior extends GameWorldInterior {

    public static final int WORLD_ID = 2;

    public WrynnHouseInterior(GameWorld parentWorld, String interiorMap, Interior type, Cursor cursor, Rectangle entranceBounds) {
        super(parentWorld, interiorMap, type, cursor, entranceBounds);

        this.worldMap = Asset.WRYNN_OFFICE;
        this.worldId = WORLD_ID;
    }

    @Override
    protected void init() {
        interactionManager.registerInteraction(WorldInteractionType.CONTAINER, "wrynn:container", WrynnOfficeContainerInteraction::new);
        interactionManager.registerInteraction(WorldInteractionType.WRYNN_LEVER, "wrynn:lever", WrynnLeverInteraction::new);
    }

    @Override
    public void enterWorld() {
        super.enterWorld();

        player.getQuestManager().advanceQuest(QuestType.A_NEW_HORIZON);
        guiManager.getHintComponent().clearHints();
    }
}