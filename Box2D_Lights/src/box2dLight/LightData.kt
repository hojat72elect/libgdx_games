package box2dLight

class LightData {

    val height: Float
    var shadow = false
    var shadowsDropped = 0

    constructor(h: Float) {
        height = h
    }

    constructor(h: Float, shadow: Boolean) {
        height = h
        this.shadow = shadow
    }

    fun getLimit(distance: Float, lightHeight: Float, lightRange: Float): Float {
        var limit: Float
        if (lightHeight > height) {
            limit = distance * height / (lightHeight - height)
            val diff = lightRange - distance
            if (limit > diff) {
                limit = diff
            }
        } else if (lightHeight == 0f) {
            limit = lightRange
        } else {
            limit = lightRange - distance
        }

        return if (limit > 0) limit else 0f
    }

}
