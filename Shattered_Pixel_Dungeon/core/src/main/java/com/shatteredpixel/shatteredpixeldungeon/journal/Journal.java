

package com.shatteredpixel.shatteredpixeldungeon.journal;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.IOException;

public class Journal {

    public static final String JOURNAL_FILE = "journal.dat";

    private static boolean loaded = false;

    public static void loadGlobal() {
        if (loaded) {
            return;
        }

        Bundle bundle;
        try {
            bundle = FileUtils.bundleFromFile(JOURNAL_FILE);
        } catch (IOException e) {
            bundle = new Bundle();
        }

        Catalog.restore(bundle);
        Bestiary.restore(bundle);
        Document.restore(bundle);

        loaded = true;
    }

    //package-private
    static boolean saveNeeded = false;

    public static void saveGlobal() {
        saveGlobal(false);
    }

    public static void saveGlobal(boolean force) {
        if (!force && !saveNeeded) {
            return;
        }

        Bundle bundle = new Bundle();

        Catalog.store(bundle);
        Bestiary.store(bundle);
        Document.store(bundle);

        try {
            FileUtils.bundleToFile(JOURNAL_FILE, bundle);
            saveNeeded = false;
        } catch (IOException e) {
            ShatteredPixelDungeon.reportException(e);
        }
    }
}
