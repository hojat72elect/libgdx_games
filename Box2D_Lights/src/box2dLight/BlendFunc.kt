package box2dLight

import com.badlogic.gdx.Gdx

/**
 * Helper class that stores source and destination factors for blending.
 */
class BlendFunc(val defaultSourceFactor: Int, val defaultDestinationFactor: Int) {
    var sourceFactor: Int
    var destinationFactor: Int

    init {
        this.sourceFactor = defaultSourceFactor
        this.destinationFactor = defaultDestinationFactor
    }

    /**
     * Sets source and destination blending factors
     */
    fun set(sourceFactor: Int, destinationFactor: Int) {
        this.sourceFactor = sourceFactor
        this.destinationFactor = destinationFactor
    }

    /**
     * Resets source and destination blending factors to default values
     * that were set on instance creation
     */
    fun reset() {
        sourceFactor = defaultSourceFactor
        destinationFactor = defaultDestinationFactor
    }

    /**
     * Calls glBlendFunc with own source and destination factors
     */
    fun apply() {
        Gdx.gl20.glBlendFunc(sourceFactor, destinationFactor)
    }
}

