package dev.ian.assroids.utility;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by: Ian Parcon
 * Date created: Sep 15, 2018
 * Time created: 10:45 AM
 */
public class TextureUtil {

    public static TextureRegion[] create(Texture texture, int frameWidth, int frameHeight) {
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 2, texture.getHeight() / 2);
        TextureRegion[] frames = new TextureRegion[frameWidth * frameHeight];
        int index = 0;
        for (int i = 0; i < frameWidth; i++) {
            for (int j = 0; j < frameHeight; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        return frames;
    }
}
