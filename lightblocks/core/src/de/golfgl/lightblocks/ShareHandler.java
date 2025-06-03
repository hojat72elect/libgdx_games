package de.golfgl.lightblocks;

import com.badlogic.gdx.Gdx;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Teilen einer Nachricht. Im Android-Projekt übersteuert
 * <p>
 * Created by Benjamin Schulte on 08.02.2017.
 */

public class ShareHandler {

    public void shareText(String message, String title) {

        String uri = LightBlocksGame.GAME_URL + "share.php?u=";

        uri += URLEncoder.encode(message, StandardCharsets.UTF_8);

        if (title != null)
            uri += "&subject=" + URLEncoder.encode(title);

        Gdx.net.openURI(uri);
    }
}
