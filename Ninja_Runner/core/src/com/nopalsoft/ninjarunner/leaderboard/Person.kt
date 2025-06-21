package com.nopalsoft.ninjarunner.leaderboard

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Net
import com.badlogic.gdx.Net.HttpMethods
import com.badlogic.gdx.Net.HttpResponseListener
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.PixmapTextureData
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

class Person(var accountType: AccountType, val id: String, var name: String?, var score: Long, var urlImage: String?) : Comparable<Person?> {
    enum class AccountType {
        GOOGLE_PLAY, AMAZON, FACEBOOK
    }

    interface DownloadImageCompleteListener {
        fun imageDownloaded()

        fun imageDownloadFail()
    }

    var image: TextureRegionDrawable? = null

    var isMe: Boolean = false // Indicates that this person is the user

    fun downloadImage(listener: DownloadImageCompleteListener?) {
        if (image != null)  //Why download it again?
            return
        val request = Net.HttpRequest(HttpMethods.GET)
        request.url = urlImage
        Gdx.net.sendHttpRequest(request, object : HttpResponseListener {
            override fun handleHttpResponse(httpResponse: Net.HttpResponse) {
                val bytes = httpResponse.result
                Gdx.app.postRunnable(Runnable {
                    val pixmap = Pixmap(bytes, 0, bytes.size)
                    val texture = Texture(PixmapTextureData(pixmap, pixmap.format, false, false, true))
                    pixmap.dispose()
                    image = TextureRegionDrawable(TextureRegion(texture))
                    listener?.imageDownloaded()
                })
            }

            override fun failed(t: Throwable?) {
                listener?.imageDownloadFail()
                Gdx.app.log("EmptyDownloadTest", "Failed", t)
            }

            override fun cancelled() {
                Gdx.app.log("EmptyDownloadTest", "Cancelled")
            }
        })
    }

    val scoreWithFormat: String
        // see: http://stackoverflow.com/a/15329259/3479489
        get() {
            var str = score.toString()
            val floatPos = if (str.contains(".")) str.length - str.indexOf(".") else 0
            val nGroups = (str.length - floatPos - 1 - (if (str.contains("-")) 1 else 0)) / 3
            for (i in 0..<nGroups) {
                val commaPos = str.length - i * 4 - 3 - floatPos
                str = str.substring(0, commaPos) + "," + str.substring(commaPos)
            }
            return str
        }

    override fun equals(obj: Any?): Boolean {
        return if (obj is Person) {
            id == obj.id && accountType == obj.accountType
        } else false
    }

    override fun compareTo(otherPerson: Person?): Int {
        return otherPerson!!.score.compareTo(score)
    }

    override fun hashCode(): Int {
        var result = score.hashCode()
        result = 31 * result + isMe.hashCode()
        result = 31 * result + (accountType?.hashCode() ?: 0)
        result = 31 * result + id.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (urlImage?.hashCode() ?: 0)
        result = 31 * result + (image?.hashCode() ?: 0)
        result = 31 * result + scoreWithFormat.hashCode()
        return result
    }
}
