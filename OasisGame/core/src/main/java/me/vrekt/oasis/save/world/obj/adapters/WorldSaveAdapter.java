package me.vrekt.oasis.save.world.obj.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import me.vrekt.oasis.save.world.AbstractWorldSaveState;
import me.vrekt.oasis.save.world.DefaultWorldSave;
import me.vrekt.oasis.save.world.InteriorWorldSave;

/**
 * Handle deserializing world saves
 */
public final class WorldSaveAdapter implements JsonDeserializer<AbstractWorldSaveState> {
    @Override
    public AbstractWorldSaveState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject src = json.getAsJsonObject();
        final boolean interior = src.get("interior").getAsBoolean();
        if (interior) {
            return context.deserialize(json, InteriorWorldSave.class);
        } else {
            return context.deserialize(json, DefaultWorldSave.class);
        }
    }
}