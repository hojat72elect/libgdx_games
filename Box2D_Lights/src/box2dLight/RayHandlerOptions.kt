package box2dLight

class RayHandlerOptions {
    var gammaCorrection = false
    var isDiffuse = false
    private var pseudo3d = false
    var shadowColorInterpolation = false

    fun setPseudo3d(pseudo3d: Boolean) {
        setPseudo3d(pseudo3d, false)
    }

    fun setPseudo3d(pseudo3d: Boolean, shadowColorInterpolation: Boolean) {
        this.pseudo3d = pseudo3d
        this.shadowColorInterpolation = shadowColorInterpolation
    }

    fun getPseudo3d() = pseudo3d
}
