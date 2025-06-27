package me.vrekt.oasis.save;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Data about a save slot
 */
public class GameSaveSlotProperty {

    @Expose
    private final String name;
    @Expose
    private final float progress;
    @Expose
    private final String date;
    @Expose
    @SerializedName("multiplayer")
    private final boolean isMultiplayer;
    @Expose
    private final int slot;

    public GameSaveSlotProperty(String name, float progress, String date, boolean isMultiplayer, int slot) {
        this.name = name;
        this.progress = progress;
        this.date = date;
        this.isMultiplayer = isMultiplayer;
        this.slot = slot;
    }

    public String name() {
        return name;
    }

    public float progress() {
        return progress;
    }

    public String date() {
        return date;
    }

    public int slot() {
        return slot;
    }
}
