package me.vrekt.oasis.gui.guis.inventory;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import org.apache.commons.lang3.StringUtils;

import me.vrekt.oasis.gui.Gui;
import me.vrekt.oasis.gui.GuiManager;
import me.vrekt.oasis.gui.GuiType;
import me.vrekt.oasis.gui.Styles;
import me.vrekt.oasis.gui.guis.inventory.utility.InventoryGuiSlot;

/**
 * Inventory GUi for players or containers
 */
public abstract class InventoryGui extends Gui {

    protected TextureRegion draggingItem;
    protected int draggingSlot;
    protected float dragX, dragY, dragWidth, dragHeight;

    public abstract void handleSlotClicked(InventoryGuiSlot slot);

    public InventoryGui(GuiType type, GuiManager guiManager) {
        super(type, guiManager);
    }

    /**
     * Item started being dragged
     *
     * @param slot slot
     * @param x    first X
     * @param y    first Y
     */
    public void itemDragStarted(InventoryGuiSlot slot, float x, float y) {
        draggingItem = slot.getItem().sprite();
        draggingSlot = slot.getSlotNumber();
        dragWidth = slot.getSlotIcon().getImageWidth() * slot.getSlotIcon().getScaleX();
        dragHeight = slot.getSlotIcon().getImageHeight() * slot.getSlotIcon().getScaleY();
        dragX = x - (dragWidth / 2f);
        dragY = y - (dragHeight / 2f);
    }

    /**
     * Update drag position
     *
     * @param x x
     * @param y y
     */
    public void updateItemDragPosition(float x, float y) {
        dragX = x - (dragWidth / 2f);
        dragY = y - (dragHeight / 2f);
    }

    /**
     * An item drag was cancelled
     */
    public void itemDragCancelled() {
        draggingItem = null;
    }

    /**
     * An item was transferred between slots
     *
     * @param from the from slot
     * @param to   the to slot
     */
    public void itemTransferred(int from, int to) {
        draggingItem = null;
    }

    /**
     * Item was transferred between inventories
     *
     * @param isContainerTransfer if this was a container -> player transfer
     * @param from                from slot
     * @param to                  to slot
     */
    public void itemTransferredBetweenInventories(boolean isContainerTransfer, int from, int to) {
        draggingItem = null;
    }

    /**
     * An item stack was transferred between inventories
     *
     * @param isContainerTransfer if this was a container -> player transfer
     * @param from                from slot
     * @param to                  to slot
     */
    public void itemSwappedBetweenInventories(boolean isContainerTransfer, int from, int to) {
        draggingItem = null;
    }

    /**
     * Create slot components for as many {@code slots}
     *
     * @param slot         the slot
     * @param hotbar       if this new slot is a hotbar
     * @param forContainer if this component is for a container
     */
    protected InventoryUiComponent createSlotComponents(int slot, boolean hotbar, boolean forContainer) {
        final VisImage background = new VisImage(Styles.slots().normal());

        final VisImage item = new VisImage();
        item.setOrigin(16 / 2f, 16 / 2f);

        final VisTable itemContainer = new VisTable(false);
        itemContainer.add(item).size(32, 32);

        final VisTable amountContainer = new VisTable(true);
        final VisLabel amountLabel = new VisLabel(StringUtils.EMPTY, Styles.getSmallBlack());
        amountLabel.setVisible(false);

        amountContainer.bottom().right();
        amountContainer.add(amountLabel).right().padBottom(4).padRight(4);

        final Stack container = new Stack(background, itemContainer, amountContainer);
        registerSlotListener(container, background, slot, forContainer);

        if (hotbar) {
            final VisTable hotbarContainer = new VisTable(true);
            final VisLabel hotbarSlot = new VisLabel(Integer.toString(slot + 1), Styles.getSmallBlack());

            hotbarContainer.top().left();
            hotbarContainer.add(hotbarSlot).top().left().padLeft(3);
            container.add(hotbarContainer);
        }
        return new InventoryUiComponent(container, background, item, amountLabel, slot);
    }

    /**
     * Register slot listeners to change the background when hovering
     *
     * @param container    container
     * @param background   background
     * @param forContainer if this listener is for a container
     */
    private void registerSlotListener(Stack container, VisImage background, int index, boolean forContainer) {
        container.addListener(new ClickListener() {
            private boolean drawableChanged;

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (!drawableChanged) {
                    if (forContainer) {
                        background.setDrawable(getContainerSlot(index).getSlotStyle(true));
                    } else {
                        background.setDrawable(getPlayerSlot(index).getSlotStyle(true));
                    }

                    drawableChanged = true;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (drawableChanged) {
                    if (forContainer) {
                        background.setDrawable(getContainerSlot(index).getSlotStyle(false));
                    } else {
                        background.setDrawable(getPlayerSlot(index).getSlotStyle(false));
                    }
                }
                drawableChanged = false;
            }
        });
    }

    /**
     * Get a slot from the extending class
     *
     * @param index the index
     * @return (hopefully) the slot
     */
    protected abstract InventoryGuiSlot getPlayerSlot(int index);

    /**
     * Get a slot from the container
     *
     * @param index index
     * @return (hopefully) the slot
     */
    protected InventoryGuiSlot getContainerSlot(int index) {
        return null;
    }

    @Override
    public void show() {
        if (guiManager.isGuiVisible(GuiType.DIALOG)) guiManager.hideGui(GuiType.DIALOG);
        if (guiManager.getHintComponent().isHintActive()) guiManager.getHintComponent().pauseCurrentHint();
        super.show();
    }

    @Override
    public void hide() {
        if (guiManager.getHintComponent().isHintPaused()) guiManager.getHintComponent().resumeCurrentHint();
        super.hide();
    }

    @Override
    public void draw(Batch batch) {
        if (draggingItem != null) {
            batch.draw(draggingItem, dragX, dragY, dragX, dragY, dragWidth, dragHeight, 1.0f, 1.0f, 1f);
        }
    }

    /**
     * Represents the data within a slot... within an inventory ui
     */
    public record InventoryUiComponent(Stack container, VisImage background, VisImage item, VisLabel amountLabel,
                                       int slot) {
    }
}
