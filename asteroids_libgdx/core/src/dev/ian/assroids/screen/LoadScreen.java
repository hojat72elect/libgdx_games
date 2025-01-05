package dev.ian.assroids.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;

import dev.ian.assroids.AssRoids;
import dev.ian.assroids.asset.Asset;
import dev.ian.assroids.utility.TextureUtil;

/**
 * Created by: Ian Parcon
 * Date created: Sep 14, 2018
 * Time created: 6:55 PM
 */
public class LoadScreen extends BaseScreen {

    private Animation<TextureRegion> loadingAnimation;
    private Texture loadTexture;

    private boolean isFinishLoading;
    private float stateTime;
    private BitmapFont font;

    private String[] dots = {" ", " . ", " . . ", " . . . "};
    private float dotsTimer;
    private int dotsIndex;

    private float loadFrameX;
    private float loadFrameY;
    private TextureRegion loadFrame;

    public LoadScreen(AssRoids game) {
        super(game);
    }

    @Override
    public void show() {
        font = new BitmapFont();
        initLoadAnimation();
        initLoader();
    }

    private void initLoadAnimation() {
        loadTexture = new Texture(Asset.load(Asset.LOAD_TEXTURE));
        TextureRegion[] loadingFrame = TextureUtil.create(loadTexture, 2, 2);
        loadingAnimation = new Animation<TextureRegion>(0.2f, loadingFrame);
    }

    @Override
    public void update(float delta) {
        updateLoadDots(delta);
        updateAssetLoad(delta);
        updateLoadFrames();
    }

    private void updateLoadFrames() {
        loadFrame = loadingAnimation.getKeyFrame(stateTime, true);
        loadFrameX = (width / 2) - loadFrame.getRegionWidth() / 2;
        loadFrameY = (height / 2) - loadFrame.getRegionHeight() / 2;
    }

    private void updateLoadDots(float delta) {
        dotsTimer += delta;
        if (dotsTimer >= 0.25f) {
            dotsTimer = 0;
            dotsIndex++;
            if (dotsIndex > dots.length - 1) dotsIndex = 0;
        }
    }

    private void updateAssetLoad(float delta) {
        stateTime += delta;
        if (asset.isLoading() && isFinishLoading) {
            game.setScreen(new PlayScreen(game));
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        batch.draw(loadFrame, loadFrameX, loadFrameY);
        font.draw(batch, "Loading assets" + dots[dotsIndex], loadFrameX - 10, loadFrameY);
        batch.end();
    }

    private void initLoader() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                asset.load();
                isFinishLoading = true;
            }
        }, 1, 0, 1);
    }

    @Override
    public void dispose() {
        super.dispose();
        loadTexture.dispose();
        font.dispose();
    }
}
