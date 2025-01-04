package dev.lonami.klooni.game;

import dev.lonami.klooni.Klooni;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import dev.lonami.klooni.serializer.BinSerializable;

// Used to keep track of the current and maximum
// score, and to also display it on the screen.
// The maximum score is NOT saved automatically.
public class Scorer extends BaseScorer implements BinSerializable {


    private int highScore;
    //region Constructor

    // The board size is required when calculating the score
    public Scorer(final Klooni game, GameLayout layout) {
        super(game, layout, Klooni.getMaxScore());
        highScore = Klooni.getMaxScore();
    }
    //region Public methods

    public void saveScore() {
        if (isNewRecord()) {
            Klooni.setMaxScore(currentScore);
        }
    }

    @Override
    protected boolean isNewRecord() {
        return currentScore > highScore;
    }

    @Override
    public boolean isGameOver() {
        return false;
    }
    //region Serialization

    @Override
    public void write(DataOutputStream outputStream) throws IOException {
        // currentScore, highScore
        outputStream.writeInt(currentScore);
        outputStream.writeInt(highScore);
    }

    @Override
    public void read(DataInputStream inputStream) throws IOException {
        currentScore = inputStream.readInt();
        highScore = inputStream.readInt();
    }

}