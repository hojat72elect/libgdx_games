package box2dLight;

import com.badlogic.gdx.Gdx;

/**
 * Helper class that stores source and destination factors for blending.
 */
public class BlendFunc {

    final int defaultSourceFactor;
    final int defaultDestinationFactor;
    int sourceFactor;
    int destinationFactor;

    public BlendFunc(int sourceFactor, int destinationFactor) {
        this.defaultSourceFactor = sourceFactor;
        this.defaultDestinationFactor = destinationFactor;
        this.sourceFactor = sourceFactor;
        this.destinationFactor = destinationFactor;
    }

    /**
     * Sets source and destination blending factors
     */
    public void set(int sourceFactor, int destinationFactor) {
        this.sourceFactor = sourceFactor;
        this.destinationFactor = destinationFactor;
    }

    /**
     * Resets source and destination blending factors to default values
     * that were set on instance creation
     */
    public void reset() {
        sourceFactor = defaultSourceFactor;
        destinationFactor = defaultDestinationFactor;
    }

    /**
     * Calls glBlendFunc with own source and destination factors
     */
    public void apply() {
        Gdx.gl20.glBlendFunc(sourceFactor, destinationFactor);
    }
}

