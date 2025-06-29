package me.vrekt.oasis.save;

import com.badlogic.gdx.utils.Disposable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import me.vrekt.oasis.GameManager;
import me.vrekt.oasis.save.player.PlayerSave;
import me.vrekt.oasis.save.settings.GameSettingsSave;

/**
 * Contains all data within a players game save state
 */
public final class GameSave implements Disposable {

    @Expose
    private final String name;
    @Expose
    private final int slot;
    @Expose
    private final float progress;
    @Expose
    private final String date;
    @Expose
    @SerializedName("multiplayer")
    private final boolean isMultiplayer;
    @Expose
    private final GameSettingsSave settings;
    @Expose
    private final PlayerSave player;

    /**
     * Create a new save
     *
     * @param name          the name
     * @param progress      the progress of the player
     * @param isMultiplayer if the game was previously multiplayer
     * @param slot          the save slot
     */
    public GameSave(String name, int slot, float progress, boolean isMultiplayer) {
        this.name = name;
        this.slot = slot;
        this.progress = progress;
        this.isMultiplayer = isMultiplayer;

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm a");
        this.date = LocalDateTime.now().format(formatter);
        this.settings = new GameSettingsSave();
        this.player = new PlayerSave(GameManager.game(), GameManager.player());
    }

    /**
     * @return name of this save
     */
    public String name() {
        return name;
    }

    /**
     * @return game progress
     */
    public float progress() {
        return progress;
    }

    /**
     * @return local date
     */
    public String date() {
        return date;
    }

    /**
     * @return if this save was previously multiplayer
     */
    public boolean isMultiplayer() {
        return isMultiplayer;
    }

    /**
     * @return save slot number
     */
    public int slot() {
        return slot;
    }

    /**
     * @return settings
     */
    public GameSettingsSave settings() {
        return settings;
    }

    /**
     * @return all player data
     */
    public PlayerSave player() {
        return player;
    }

    @Override
    public void dispose() {
        player.dispose();
    }
}
