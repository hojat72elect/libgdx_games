package com.salvai.centrum.screens

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.salvai.centrum.utils.Constants
import com.salvai.centrum.utils.RandomUtil

class NewBackground(private val starTexture: Texture) {
    private val stars: Array<Star> = Array<Star>()
    private val starsCreationDelay: Array<Int?> = Array<Int?>()

    init {
        for (i in 0..9) {
            starsCreationDelay.add(10 * i)
        }

        //create first stars
        for (i in 0..<Constants.MAX_STARS - starsCreationDelay.size) stars.addAll(Star(starTexture, true))
    }

    private fun update(delta: Float) {
        checkDelays(delta)
        updateStars()
        createStars()
    }

    private fun checkDelays(delta: Float) {
        for (i in 0..<starsCreationDelay.size) {
            var value = starsCreationDelay.get(i)!!
            value = (value - delta).toInt()
            starsCreationDelay.set(i, value)
            if (starsCreationDelay.get(i)!! <= 0) starsCreationDelay.removeIndex(i)
        }
    }

    private fun createStars() {
        for (i in 0..<Constants.MAX_STARS - stars.size - starsCreationDelay.size) stars.add(Star(starTexture, false))
    }

    private fun updateStars() {
        for (star in stars) if (star.move()) {
            stars.removeValue(star, false)
            starsCreationDelay.add(40) // new delay
        }
    }

    fun draw(delta: Float, batch: Batch?) {
        update(delta)
        for (star in stars) star.sprite.draw(batch)
    }

    fun drawPause(batch: Batch?) {
        for (star in stars) star.sprite.draw(batch)
    }

    private class Star(starTexture: Texture, initial: Boolean) {
        var sprite = Sprite(starTexture)
        var position = if (initial) {
            Vector2(RandomUtil.getRandomBackgroundStarXCoordinate().toFloat(), RandomUtil.getRandomBackgroundStarYCoordinate().toFloat())
        } else {
            Vector2(Constants.SCREEN_WIDTH.toFloat(), RandomUtil.getRandomBackgroundStarYCoordinate().toFloat())
        }
        var speed = RandomUtil.getRandomStarSpeed()

        init {
            sprite.setBounds(position.x, position.y, Constants.STAR_SIZE.toFloat(), Constants.STAR_SIZE.toFloat())
            sprite.setColor(RandomUtil.getRandomColor())
            sprite.setAlpha(RandomUtil.getRandomAlpha())
        }


        fun move(): Boolean {
            position.x -= speed.toFloat()
            sprite.setX(position.x)
            return position.x <= 0
        }
    }
}
