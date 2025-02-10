package dev.ian.snakeboi

object Scorer {

    private var score = 0

    @JvmStatic
    fun score() {
        score += 10
    }

    @JvmStatic
    fun getScore() = score

    @JvmStatic
    fun reset() {
        score = 0
    }

}