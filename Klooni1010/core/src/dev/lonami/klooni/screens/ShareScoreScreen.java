package dev.lonami.klooni.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import dev.lonami.klooni.Klooni;

// Screen where the user can customize the look and feel of the game
class ShareScoreScreen implements Screen {


    private final Klooni game;
    private final Label infoLabel;
    private final SpriteBatch spriteBatch;

    private final int score;
    private final boolean timeMode;

    private final Screen lastScreen;
    //region Constructor

    ShareScoreScreen(final Klooni game, final Screen lastScreen,
                     final int score, final boolean timeMode
    ) {
        this.game = game;
        this.lastScreen = lastScreen;

        this.score = score;
        this.timeMode = timeMode;

        final Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = game.skin.getFont("font_small");

        infoLabel = new Label("Generating image...", labelStyle);
        infoLabel.setColor(Klooni.theme.textColor);
        infoLabel.setAlignment(Align.center);
        infoLabel.layout();
        infoLabel.setPosition(
                (Gdx.graphics.getWidth() - infoLabel.getWidth()) * 0.5f,
                (Gdx.graphics.getHeight() - infoLabel.getHeight()) * 0.5f
        );

        spriteBatch = new SpriteBatch();
    }
    //region Private methods

    private void goBack() {
        game.transitionTo(lastScreen);
    }
    //region Public methods

    @Override
    public void show() {
        final boolean ok = game.shareChallenge.saveChallengeImage(score, timeMode);
        game.shareChallenge.shareScreenshot(ok);
        goBack();
    }

    @Override
    public void render(float delta) {
        Klooni.theme.glClearBackground();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        infoLabel.draw(spriteBatch, 1);
        spriteBatch.end();
    }
    //region Empty methods

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    
}
