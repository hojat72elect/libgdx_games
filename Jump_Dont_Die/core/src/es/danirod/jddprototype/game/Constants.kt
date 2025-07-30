package es.danirod.jddprototype.game

/**
 * Some class for defining constant values used in the game, so that they can be changed from
 * a single location instead of lurking the code to find the values.
 */
object Constants {
    /**
     * How many pixels there are in a meter. As explained in the video, this is important, because
     * your simulation is in meters but you have to somehow convert these meters to pixels so that
     * they can be rendered at a size visible by the user.
     */
    const val PIXELS_IN_METER: Float = 90f

    /**
     * The force in N/s that the player uses to jump in an impulse. This force will also be applied
     * in the opposite direction to make the player fall faster multiplied by some value to make
     * it stronger.
     */
    const val IMPULSE_JUMP: Int = 20

    /**
     * This is the speed that the player has. The larger this value is, the faster the player will
     * go. Don't make this value very high without putting more distance between every obstacle
     * in the circuit.
     */
    const val PLAYER_SPEED: Float = 8f
}
