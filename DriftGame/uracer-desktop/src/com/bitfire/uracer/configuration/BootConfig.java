package com.bitfire.uracer.configuration;

import com.bitfire.uracer.utils.AwtUtilsKt;
import com.bitfire.uracer.utils.SortedProperties;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Represents boot-time configuration values: complex flags, such as display position, are handled via specialized methods.
 */
public class BootConfig {

    private final SortedProperties props = new SortedProperties();

    public BootConfig() {
        load();
    }

    /**
     * Loads the configuration from disk, if present, else set it to some sensible defaults
     */
    public void load() {
        try {
            props.load(new FileInputStream(Storage.BOOT_CONFIG_FILE));
        } catch (IOException e) {
            System.out.print("NO BOOT CONFIG AVAILABLE\n\n");
            toDefaults();
        }
    }

    /**
     * Stores the current instance to disk
     */
    public void store() {
        try {
            props.store(new FileOutputStream(Storage.BOOT_CONFIG_FILE), null);
        } catch (IOException e) {
            System.out.print("CANNOT STORE BOOT CONFIG\n\n");
        }
    }

    /**
     * Resets the current instance to the default values and save them
     */
    private void toDefaults() {
        setInt(BootConfigFlag.WIDTH, BootConfigFlag.WIDTH.defaultInt);
        setInt(BootConfigFlag.HEIGHT, BootConfigFlag.HEIGHT.defaultInt);
        setBoolean(BootConfigFlag.VSYNC, BootConfigFlag.VSYNC.defaultBoolean);
        setBoolean(BootConfigFlag.CPUSYNC, BootConfigFlag.CPUSYNC.defaultBoolean);
        setBoolean(BootConfigFlag.FULLSCREEN, false);
        setBoolean(BootConfigFlag.UNDECORATED, false);

        // auto-detect centered window position
        setWindowX(AwtUtilsKt.getCenteredXOnDisplay(BootConfigFlag.WIDTH.defaultInt));
        setWindowY(AwtUtilsKt.getCenteredYOnDisplay(BootConfigFlag.HEIGHT.defaultInt));
    }

    /**
     * Returns an hash key composed by the current display resolution's width/height values and the specified auxiliary key
     */
    private String getDisplayResolutionKey(String axis_key) {
        return "win." + getInt(BootConfigFlag.WIDTH) + "x" + getInt(BootConfigFlag.HEIGHT) + "." + axis_key;
    }

    /**
     * Returns the stored window's position x-coord, if any, else -1
     */
    public int getWindowX() {
        String v = props.getProperty(getDisplayResolutionKey("x"));
        if (v == null) {
            return -1;
        }

        return Integer.parseInt(v);
    }

    /**
     * Sets the window's position x-coord to the specified value
     */
    public void setWindowX(int x) {
        props.setProperty(getDisplayResolutionKey("x"), "" + x);
    }

    /**
     * Returns the stored window's position y-coord, if any, else -1
     */
    public int getWindowY() {
        String v = props.getProperty(getDisplayResolutionKey("y"));
        if (v == null) {
            // compute it
            return -1;
        }

        return Integer.parseInt(v);
    }

    /**
     * Sets the window's position y-coord to the specified value
     */
    public void setWindowY(int y) {
        props.setProperty(getDisplayResolutionKey("y"), "" + y);
    }

    /**
     * Returns the int value associated with the specified flag
     */
    public int getInt(BootConfigFlag flag) {
        String v = props.getProperty(flag.name);
        if (v == null) {
            return flag.defaultInt;
        }

        return Integer.parseInt(v);
    }

    /**
     * Returns the boolean value associated with the specified flag
     */
    public boolean getBoolean(BootConfigFlag flag) {
        String v = props.getProperty(flag.name);
        if (v == null) {
            return flag.defaultBoolean;
        }

        return Boolean.parseBoolean(v);
    }

    /**
     * Sets the int value associated with the specified flag to the specified value
     */
    public void setInt(BootConfigFlag flag, int value) {
        props.setProperty(flag.name, "" + value);
    }

    /**
     * Sets the boolean value associated with the specified flag to the specified value
     */
    public void setBoolean(BootConfigFlag flag, boolean value) {
        props.setProperty(flag.name, "" + value);
    }

    /**
     * Available boot config flags
     */
    public enum BootConfigFlag {
        WIDTH("width", 1280),
        HEIGHT("height", 720),
        VSYNC("vsync", true),
        CPUSYNC("cpusync", false),
        FULLSCREEN("fullscreen", false),
        UNDECORATED("undecorated", false),
        ;

        public int defaultInt;
        String name;
        boolean defaultBoolean;

        BootConfigFlag(String name, int defaultInt) {
            this.name = name;
            this.defaultInt = defaultInt;
        }

        BootConfigFlag(String name, boolean defaultBoolean) {
            this.name = name;
            this.defaultBoolean = defaultBoolean;
        }
    }
}
