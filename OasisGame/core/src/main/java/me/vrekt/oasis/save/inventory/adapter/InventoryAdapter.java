package me.vrekt.oasis.save.inventory.adapter;

import com.badlogic.gdx.utils.IntMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import me.vrekt.oasis.entity.inventory.AbstractInventory;
import me.vrekt.oasis.entity.inventory.InventoryType;
import me.vrekt.oasis.entity.inventory.container.ContainerInventory;
import me.vrekt.oasis.entity.player.sp.inventory.PlayerInventory;
import me.vrekt.oasis.item.Item;
import me.vrekt.oasis.save.inventory.InventorySave;
import me.vrekt.oasis.save.inventory.ItemSave;

/**
 * Handles deserializing inventories and items
 */
public final class InventoryAdapter {

    /**
     * Adapt this inventory to JSON
     */
    public static final class InventoryPropertiesSerializer implements JsonSerializer<InventorySave> {
        @Override
        public JsonElement serialize(InventorySave src, Type typeOfSrc, JsonSerializationContext context) {
            final JsonObject base = new JsonObject();
            final JsonArray items = new JsonArray();

            base.addProperty("type", src.inventory().type().name());
            base.addProperty("size", src.inventory().getSize());

            for (IntMap.Entry<Item> entry : src.inventory().items()) {
                items.add(context.serialize(new ItemSave(entry.key, entry.value)));
            }

            base.add("contents", items);
            return base;
        }
    }

    /**
     * Deserialize an inventory
     */
    public static final class InventoryPropertiesDeserializer implements JsonDeserializer<InventorySave> {
        @Override
        public InventorySave deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final InventorySave save = new InventorySave();
            final JsonObject src = json.getAsJsonObject();

            final InventoryType type = InventoryType.valueOf(src.get("type").getAsString());
            final int size = src.get("size").getAsInt();

            final AbstractInventory inventory = (type == InventoryType.PLAYER) ? new PlayerInventory() : new ContainerInventory(size);
            save.setInventory(inventory);

            final JsonArray contents = src.getAsJsonArray("contents");
            for (JsonElement element : contents) {
                final ItemSave item = context.deserialize(element, ItemSave.class);
                inventory.putSavedItem(item.type(), item.name(), item.slot(), item.amount());
            }

            return save;
        }
    }
}
