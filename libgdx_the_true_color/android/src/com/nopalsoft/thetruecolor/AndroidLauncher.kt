package com.nopalsoft.thetruecolor

import android.os.Bundle
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Net
import com.badlogic.gdx.Net.HttpResponseListener
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.PixmapTextureData
import com.nopalsoft.thetruecolor.handlers.HandlerGWT
import com.nopalsoft.thetruecolor.handlers.HandlerGWT.OnTextureLoaded

class AndroidLauncher : AndroidApplication(), HandlerGWT {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        val game = TrueColorGame()
        initialize(game, config)
    }

    override fun getTextureFromFacebook(url: String?, onTextureLoaded: OnTextureLoaded?) {
        val request = Net.HttpRequest(Net.HttpMethods.GET)
        request.url = url
        Gdx.net.sendHttpRequest(request, object : HttpResponseListener {
            override fun handleHttpResponse(httpResponse: Net.HttpResponse) {
                val bytes = httpResponse.result
                Gdx.app.postRunnable {
                    val pixmap = Pixmap(bytes, 0, bytes.size)
                    val texture = Texture(PixmapTextureData(pixmap, pixmap.format, false, false, true))
                    pixmap.dispose()
                    onTextureLoaded?.onTextureLoaded(texture)
                }
            }

            override fun failed(t: Throwable) {
                Gdx.app.log("EmptyDownloadTest", "Failed", t)
            }

            override fun cancelled() {
                Gdx.app.log("EmptyDownloadTest", "Cancelled")
            }
        })
    }


}
