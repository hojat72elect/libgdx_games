package com.bitfire.uracer.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.bitfire.uracer.configuration.Config;
import com.bitfire.uracer.configuration.Storage;
import com.bitfire.uracer.game.logic.replaying.ReplayInfo;

public final class ReplayUtils {

    public static boolean areValidIds(ReplayInfo info) {
        if (info != null) {
            return DigestUtils.isValidDigest(info.getId()) && !info.getUserId().isEmpty() && !info.getTrackId().isEmpty();
        }

        return false;
    }

    public static String getDestinationDir(ReplayInfo info) {
        if (areValidIds(info)) {
            return Storage.ReplaysRoot + info.getTrackId() + "/" + info.getUserId() + "/";
        }

        return "";
    }

    public static String getFullPath(ReplayInfo info) {
        return getDestinationDir(info) + info.getId();
    }

    public static void pruneReplay(ReplayInfo info) {
        if (ReplayUtils.areValidIds(info)) {
            String rid = info.getId();
            if (!rid.isEmpty()) {
                String path = getFullPath(info);
                if (!path.isEmpty()) {
                    FileHandle hf = Gdx.files.external(path);
                    if (hf.exists()) {
                        hf.delete();
                        Gdx.app.log("ReplayUtils", "Pruned #" + rid);
                    } else {
                        Gdx.app.error("ReplayUtils", "Couldn't prune #" + rid);
                    }
                }
            }
        }
    }

    public static int ticksToMilliseconds(int ticks) {
        return (int) (ticks * Config.Physics.Dt * AMath.ONE_ON_CMP_EPSILON);
    }

    public static float ticksToSeconds(int ticks) {
        return (float) ticksToMilliseconds(ticks) / 1000f;
    }
}
