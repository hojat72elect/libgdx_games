package dev.lonami.klooni.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import dev.lonami.klooni.Klooni;
import dev.lonami.klooni.SkinLoader;
import dev.lonami.klooni.serializer.BinSerializable;

public abstract class BaseScorer implements BinSerializable {

    final Label currentScoreLabel;
    final Label highScoreLabel;
    final Texture cupTexture;
    final Rectangle cupArea;
    private final Color cupColor;
    int currentScore;
    // To interpolate between shown score -> real score
    private float shownScore;

    // The board size is required when calculating the score
    BaseScorer(final Klooni game, GameLayout layout, int highScore) {
        cupTexture = SkinLoader.loadPng("cup.png");
        cupColor = Klooni.theme.currentScore.cpy();
        cupArea = new Rectangle();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = game.skin.getFont("font");

        currentScoreLabel = new Label("0", labelStyle);
        currentScoreLabel.setAlignment(Align.right);

        highScoreLabel = new Label(Integer.toString(highScore), labelStyle);

        layout.update(this);
    }

    // The original game seems to work as follows:
    // If < 1 were cleared, score = 0
    // If = 1  was cleared, score = cells cleared
    // If > 1 were cleared, score = cells cleared + score(cleared - 1)
    final int calculateClearScore(int stripsCleared, int boardSize) {
        if (stripsCleared < 1) return 0;
        if (stripsCleared == 1) return boardSize;
        else return boardSize * stripsCleared + calculateClearScore(stripsCleared - 1, boardSize);
    }

    // Adds the score a given piece would give
    public void addPieceScore(final int areaPut) {
        currentScore += areaPut;
    }

    // Adds the score given by the board, this is, the count of cleared strips
    public int addBoardScore(int stripsCleared, int boardSize) {
        int score = calculateClearScore(stripsCleared, boardSize);
        currentScore += score;
        return score;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void pause() {
    }

    public void resume() {
    }

    abstract public boolean isGameOver();

    abstract protected boolean isNewRecord();

    public String gameOverReason() {
        return "";
    }

    abstract public void saveScore();

    public void draw(SpriteBatch batch) {
        // If we beat a new record, the cup color will linear interpolate to the high score color
        cupColor.lerp(isNewRecord() ? Klooni.theme.highScore : Klooni.theme.currentScore, 0.05f);
        batch.setColor(cupColor);
        batch.draw(cupTexture, cupArea.x, cupArea.y, cupArea.width, cupArea.height);

        int roundShown = MathUtils.round(shownScore);
        if (roundShown != currentScore) {
            shownScore = Interpolation.linear.apply(shownScore, currentScore, 0.1f);
            currentScoreLabel.setText(Integer.toString(MathUtils.round(shownScore)));
        }

        currentScoreLabel.setColor(Klooni.theme.currentScore);
        currentScoreLabel.draw(batch, 1f);

        highScoreLabel.setColor(Klooni.theme.highScore);
        highScoreLabel.draw(batch, 1f);
    }
}
