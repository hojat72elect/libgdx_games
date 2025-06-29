package me.vrekt.oasis.gui.guis.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.github.tommyettinger.textra.TypingLabel;
import com.kotcrab.vis.ui.widget.Tooltip;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisImageTextButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSplitPane;
import com.kotcrab.vis.ui.widget.VisTable;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import me.vrekt.oasis.GameManager;
import me.vrekt.oasis.asset.sound.Sounds;
import me.vrekt.oasis.entity.player.sp.PlayerSP;
import me.vrekt.oasis.entity.player.sp.attribute.Attribute;
import me.vrekt.oasis.gui.GuiManager;
import me.vrekt.oasis.gui.GuiType;
import me.vrekt.oasis.gui.Styles;
import me.vrekt.oasis.gui.guis.inventory.actions.InventorySlotSource;
import me.vrekt.oasis.gui.guis.inventory.actions.InventorySlotTarget;
import me.vrekt.oasis.gui.guis.inventory.utility.InventoryGuiSlot;
import me.vrekt.oasis.gui.guis.inventory.utility.ItemInformationContainer;
import me.vrekt.oasis.item.Item;
import me.vrekt.oasis.item.ItemEquippable;
import me.vrekt.oasis.item.ItemRarity;
import me.vrekt.oasis.item.artifact.ItemArtifact;
import me.vrekt.oasis.item.consumables.ItemConsumable;
import me.vrekt.oasis.item.usable.ItemUsable;
import me.vrekt.oasis.item.weapons.ItemWeapon;

public final class PlayerInventoryGui extends InventoryGui {

    private final PlayerSP player;
    private final LinkedList<InventoryGuiSlot> guiSlots = new LinkedList<>();
    private final Map<ItemRarity, TextureRegionDrawable> rarityIcons = new HashMap<>();
    private final TypingLabel itemNameHeader, itemDescriptionHeader;

    // attributes and stats of the item
    private final Array<ItemInformationContainer> informationContainers = new Array<>();

    private final VisImage itemRarityIcon;
    private final VisLabel itemRarityText;
    private final VisImageTextButton itemActionButton;

    private final VisTable rarityIconContainer;
    private final VisTable parentItemInformationContainer;
    private final VisTable itemInformationComponents;
    private final VisTable buttonTable;

    // currently selected item
    private Item selectedItem;

    public PlayerInventoryGui(GuiManager guiManager) {
        super(GuiType.INVENTORY, guiManager);
        this.player = guiManager.getGame().player();

        hideWhenVisible.add(GuiType.HUD);
        disablePlayerMovement = true;

        initializeRarityIcons();

        rootTable.setBackground(new TextureRegionDrawable(asset("inventory", 2)));
        rootTable.setFillParent(true);
        rootTable.setVisible(false);
        rootTable.setTransform(true);

        final VisTable left = new VisTable(true);
        final VisTable right = new VisTable(true);

        right.top().padTop(36).padLeft(36).left();
        left.top().padTop(52).padLeft(84);

        itemNameHeader = new TypingLabel(StringUtils.EMPTY, Styles.getLargeBlack());
        itemNameHeader.setVisible(true);
        itemNameHeader.setWrap(true);
        itemNameHeader.setWidth(150);

        itemDescriptionHeader = new TypingLabel(StringUtils.EMPTY, Styles.getMediumWhiteMipMapped());
        itemDescriptionHeader.setVisible(true);
        itemDescriptionHeader.setWrap(true);
        itemDescriptionHeader.setColor(Color.DARK_GRAY);
        itemDescriptionHeader.setWidth(175);

        rarityIconContainer = new VisTable();
        rarityIconContainer.left();

        rarityIconContainer.setBackground(Styles.getThemePadded());
        rarityIconContainer.add(itemRarityIcon = new VisImage()).size(36, 36);
        rarityIconContainer.add(itemRarityText = new VisLabel(StringUtils.EMPTY, Styles.getMediumWhiteMipMapped())).padLeft(8);

        // add name + rarity icon
        final VisTable headerTable = new VisTable();
        headerTable.left();
        headerTable.add(itemNameHeader).width(150).left();

        // add header table + description
        right.add(headerTable).padTop(6).left();
        right.row();
        right.add(itemDescriptionHeader)
                .width(175)
                .padTop(8)
                .left();
        right.row().padTop(16);

        // Item info: Info and stats
        parentItemInformationContainer = new VisTable();
        itemInformationComponents = new VisTable();

        parentItemInformationContainer.left();
        itemInformationComponents.left();

        // Maximum of 3 allowed attributes or stats
        for (int i = 0; i < 3; i++) {
            final VisImage icon = new VisImage();
            final Tooltip tooltip = new Tooltip.Builder(StringUtils.EMPTY)
                    .target(icon)
                    .style(Styles.getTooltipStyle())
                    .build();

            final ItemInformationContainer container = new ItemInformationContainer(icon, tooltip);
            informationContainers.add(container);

            itemInformationComponents.add(icon).size(36, 36).padRight(4).left();
        }
        // Use item button
        buttonTable = new VisTable();
        buttonTable.left();

        // Init use item buttons and styles
        itemActionButton = new VisImageTextButton(StringUtils.EMPTY, Styles.getImageTextButtonStyle());
        itemActionButton.setVisible(false);

        buttonTable.add(itemActionButton);

        // Finally, add item information to right table
        right.add(parentItemInformationContainer).left();

        addItemActionButtonListener();

        for (int i = 1; i < player.getInventory().getSize(); i++) {
            // (i - 1) for compatibility with splitting the table in rows of 3
            final InventoryUiComponent component = createSlotComponents((i - 1), (i - 1) < 6, false);
            guiSlots.add(new InventoryGuiSlot(guiManager, this, component, (i - 1)));

            left.add(component.container()).size(48, 48);
            // line break for hotbar components
            if (i == 6) {
                final VisTable separator = new VisTable();
                separator.addSeparator();
                // Unable do it any other way because first column of,
                // slots will expand to fit the separator size.
                left.row();
                left.add(separator);
            }

            // split table in rows of 3
            if (i % 3 == 0) left.row();
        }

        // Add split pane for left and right tables
        final VisSplitPane pane = new VisSplitPane(left, right, false);

        // only allow elements to be touched, since split pane
        // is programming choice for easier UI, the split pane should not be used
        pane.setTouchable(Touchable.enabled);
        rootTable.add(pane).fill().expand();

        initializeSlotActions();

        guiManager.addGui(rootTable);
    }

    @Override
    protected InventoryGuiSlot getPlayerSlot(int index) {
        return guiSlots.get(index);
    }

    @Override
    public void itemTransferred(int from, int to) {
        super.itemTransferred(from, to);

        // EM-97: Keep the information page opened if we transferred slots
        // Indirectly re-fixed EM-90
        guiSlots.get(from).resetSlot();
        guiSlots.get(to).setOccupiedItem(player.getInventory().get(to));

        handleSlotClicked(guiSlots.get(to));
    }

    @Override
    public void update() {
        for (IntMap.Entry<Item> entry : player.getInventory().items()) {
            final InventoryGuiSlot slot = guiSlots.get(entry.key);
            if (!entry.value.compare(slot.getLastItemKey())) {
                slot.setOccupiedItem(entry.value);
            } else if (entry.value.amount() != slot.itemTextAmount()) {
                // EM-119: Update item amounts
                slot.updateItem(entry.value);
            }
        }
    }

    /**
     * Initialize components for the drag and drop actions within a slot
     */
    private void initializeSlotActions() {
        final DragAndDrop action = new DragAndDrop();
        action.setDragTime(100);

        for (InventoryGuiSlot guiSlot : guiSlots) {
            action.addSource(new InventorySlotSource(this, guiSlot));
            action.addTarget(new InventorySlotTarget(this, guiSlot, player.getInventory()));
        }
    }

    /**
     * Initialize icon drawables
     */
    private void initializeRarityIcons() {
        for (ItemRarity rarity : ItemRarity.values()) {
            if (rarity.getTexture() != null) {
                rarityIcons.put(rarity, new TextureRegionDrawable(guiManager.getAsset().get(rarity.getTexture())));
            }
        }
    }

    /**
     * Handle assigning actions to the item action button.
     */
    private void addItemActionButtonListener() {
        itemActionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedItem instanceof ItemConsumable consumable) {
                    consumable.useItem(player);
                    if (selectedItem.amount() <= 0) itemActionButton.setVisible(false);
                } else if (selectedItem instanceof ItemEquippable equippable) {
                    if (equippable.canEquip(player)) {
                        equippable.equip(player);
                        itemActionButton.setText("Unequip");
                    } else if (equippable.isEquipped()) {
                        // TODO: Un-equip
                    } else if (selectedItem instanceof ItemArtifact) {
                        // item was activated
                        // TODO: De-activate maybe.
                        itemActionButton.setVisible(false);
                    }
                } else if (selectedItem instanceof ItemUsable itemUsable) {
                    // button should not be visible anyway, but just in case
                    if (itemUsable.isUsable(player)) {
                        itemUsable.use(player);
                    }
                }
            }
        });
    }

    @Override
    public void handleSlotClicked(InventoryGuiSlot slot) {
        hideItemInformation();
        selectedItem = slot.getItem();

        // Update item name
        itemNameHeader.setText(selectedItem.rarity().getColorName() + selectedItem.name());
        itemNameHeader.setVisible(true);
        itemNameHeader.restart();

        // Update item description
        itemDescriptionHeader.setText(selectedItem.description());
        itemDescriptionHeader.setVisible(true);
        itemDescriptionHeader.restart();

        enableItemActionButton(selectedItem);
        populateWeaponOrArtifactStats(selectedItem);
        updateItemRarityIcon();
    }

    /**
     * Update rarity icons for items
     */
    private void updateItemRarityIcon() {
        final ItemRarity rarity = selectedItem.rarity();
        itemRarityText.setText(rarity.getRarityName());
        populateRarityComponents();

        if (rarityIcons.containsKey(rarity)) {
            itemRarityIcon.setVisible(true);
            itemRarityIcon.setDrawable(rarityIcons.get(rarity));
        } else {
            itemRarityIcon.setDrawable((Drawable) null);
            itemRarityIcon.setVisible(false);
        }
    }

    private void populateItemAttributeComponents() {
        parentItemInformationContainer.row().padTop(-16);
        parentItemInformationContainer.add(itemInformationComponents).left();
    }

    private void populateRarityComponents() {
        parentItemInformationContainer.row();
        parentItemInformationContainer.add(rarityIconContainer).padTop(6).left();
        parentItemInformationContainer.row();
        parentItemInformationContainer.add(buttonTable).padTop(8).left();
    }

    /**
     * Enable the button for item actions
     *
     * @param item the item
     */
    private void enableItemActionButton(Item item) {
        if (item instanceof ItemConsumable) {
            itemActionButton.setVisible(true);
            itemActionButton.setText("Eat");
        } else if (item instanceof ItemWeapon weapon) {
            populateItemStats(weapon);
            itemActionButton.setVisible(true);
            itemActionButton.setText("Equip");
        } else if (item instanceof ItemArtifact) {
            itemActionButton.setVisible(true);
            itemActionButton.setText("Activate");
        } else if (item instanceof ItemUsable usable) {
            if (usable.isUsable(player)) {
                itemActionButton.setVisible(true);
                itemActionButton.setText(usable.inventoryTag());
            }
        }

        if (itemActionButton.isVisible()) fadeIn(itemActionButton, 1.0f);
    }

    /**
     * Choose whether to populate weapon stats, or, artifact stats.
     *
     * @param item the item
     */
    private void populateWeaponOrArtifactStats(Item item) {
        if (item instanceof ItemWeapon weapon) {
            populateItemStats(weapon);
        } else if (item instanceof ItemArtifact artifact) {
            populateArtifactStats(artifact);
        } else if (item instanceof ItemConsumable) {
            int index = 0;
            for (Attribute attribute : item.getItemAttributes().values()) {
                if (attribute.texture() == null) continue;
                populateAttributeInformation(attribute, index);
                index++;
            }
        }
    }

    /**
     * Populate attribute information
     *
     * @param attribute attribute
     * @param index     current index, should not exceed 2
     */
    private void populateAttributeInformation(Attribute attribute, int index) {
        populateItemAttributeComponents();

        fadeIn(informationContainers.get(index).updateAttribute(attribute), 1.5f);
    }

    /**
     * Populate item stats for a weapon
     *
     * @param item the item
     */
    private void populateItemStats(ItemWeapon item) {
        populateItemAttributeComponents();

        fadeIn(informationContainers.get(0).updateRange(item), 1.5f);
        fadeIn(informationContainers.get(1).updateDamage(item), 1.5f);
        fadeIn(informationContainers.get(2).updateCriticalChance(item), 1.5f);
    }

    /**
     * Populate artifact stats
     *
     * @param item the artifact
     */
    private void populateArtifactStats(ItemArtifact item) {
        populateItemAttributeComponents();

        fadeIn(informationContainers.get(0).updateArtifact(item), 1.5f);
    }

    /**
     * Hide item information
     */
    private void hideItemInformation() {
        itemActionButton.setVisible(false);
        informationContainers.forEach(ItemInformationContainer::hide);
        parentItemInformationContainer.clear();
    }

    /**
     * Remove this item from the provided slot.
     *
     * @param slot the slot number
     */
    public void removeItemFromSlot(int slot) {
        guiSlots.get(slot).resetSlot();

        itemNameHeader.setVisible(false);
        itemDescriptionHeader.setVisible(false);
        hideItemInformation();
    }

    @Override
    public void show() {
        super.show();
        GameManager.playSound(Sounds.OPEN_INVENTORY, .15f, -1.0f, 0.0f);
        rootTable.setVisible(true);
    }

    @Override
    public void hide() {
        super.hide();
        rootTable.setVisible(false);
    }

    @Override
    public void hideRelatedGuis() {
        guiManager.hideGui(GuiType.QUEST);
        guiManager.hideGui(GuiType.QUEST_ENTRY);
        guiManager.hideGui(GuiType.CONTAINER);
    }
}
