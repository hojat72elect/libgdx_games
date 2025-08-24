package aurelienribon.tweenengine

import aurelienribon.tweenengine.paths.CatmullRom
import aurelienribon.tweenengine.paths.Linear

/**
 * Collection of built-in paths.
 */
interface TweenPaths {
    companion object {
        val linear = Linear()
        val catmullRom = CatmullRom()
    }
}
