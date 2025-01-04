package dev.lonami.klooni.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import dev.lonami.klooni.actors.Band;
import dev.lonami.klooni.actors.ShopCard;

// Helper class to calculate the size of each element
//
// TODO In a future, perhaps this could handle landscape mode differently
// For example, the boardHeight on the left and the piece holder on the right
public class GameLayout {


    private float screenWidth, marginWidth, availableWidth;
    private float screenHeight, logoHeight, scoreHeight, boardHeight, pieceHolderHeight, shopCardHeight;
    //region Constructor

    public GameLayout() {
        calculate();
    }
    //region Private methods

    private void calculate() {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        // Widths
        marginWidth = screenWidth * 0.05f;
        availableWidth = screenWidth - marginWidth * 2f;

        // Heights
        logoHeight = screenHeight * 0.10f;
        scoreHeight = screenHeight * 0.15f;
        boardHeight = screenHeight * 0.50f;
        pieceHolderHeight = screenHeight * 0.25f;

        shopCardHeight = screenHeight * 0.15f;
    }
    //region Update layout methods

    // These methods take any of the custom objects used in the game
    // and positions them accordingly on the screen, by using relative
    // coordinates. Since these objects are not actors and we cannot
    // add them to a table (and would probably be harder), this approach
    // was used. Note that all these are using Y-up coordinates.
    void update(BaseScorer scorer) {
        float cupSize = Math.min(scoreHeight, scorer.cupTexture.getHeight());
        final Rectangle area = new Rectangle(
                marginWidth, pieceHolderHeight + boardHeight,
                availableWidth, scoreHeight
        );

        scorer.cupArea.set(
                area.x + area.width * 0.5f - cupSize * 0.5f, area.y,
                cupSize, cupSize
        );

        scorer.currentScoreLabel.setBounds(
                area.x, area.y,
                area.width * 0.5f - cupSize * 0.5f, area.height
        );

        scorer.highScoreLabel.setBounds(
                area.x + area.width * 0.5f + cupSize * 0.5f, area.y,
                area.width * 0.5f - cupSize * 0.5f, area.height
        );
    }

    // Special case, we want to position the label on top of the cup
    void updateTimeLeftLabel(Label timeLeftLabel) {
        timeLeftLabel.setBounds(0, screenHeight - logoHeight, screenWidth, logoHeight);
    }

    void update(Board board) {
        // We can't leave our area, so pick the minimum between available
        // height and width to determine an appropriated cell size
        float boardSize = Math.min(availableWidth, boardHeight);
        board.cellSize = boardSize / board.cellCount;

        // Now that we know the board size, we can center the board on the screen
        board.pos.set(
                screenWidth * 0.5f - boardSize * 0.5f, pieceHolderHeight);
    }

    void update(PieceHolder holder) {
        holder.area.set(
                marginWidth, 0f,
                availableWidth, pieceHolderHeight
        );
    }

    public void update(Band band) {
        final Rectangle area = new Rectangle(
                0, pieceHolderHeight + boardHeight,
                screenWidth, scoreHeight
        );

        band.setBounds(area.x, area.y, area.width, area.height);
        // Let the band have the following shape:
        // 10% (100) padding
        // 35% (90%) score label
        // 10% (55%) padding
        // 35% (45%) info label
        // 10% (10%) padding
        band.scoreBounds.set(area.x, area.y + area.height * 0.55f, area.width, area.height * 0.35f);
        band.infoBounds.set(area.x, area.y + area.height * 0.10f, area.width, area.height * 0.35f);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public void update(ShopCard card) {
        card.setSize(availableWidth - marginWidth, shopCardHeight);
        card.cellSize = shopCardHeight * 0.2f;

        // X offset from the cells (5 cells = shopCardHeight)
        card.nameBounds.set(
                shopCardHeight, card.cellSize,
                availableWidth - shopCardHeight, shopCardHeight
        );

        card.priceBounds.set(
                shopCardHeight, -card.cellSize,
                availableWidth - shopCardHeight, shopCardHeight
        );
    }

    
}
