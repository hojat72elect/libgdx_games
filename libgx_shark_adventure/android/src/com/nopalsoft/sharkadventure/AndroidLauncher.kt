package com.nopalsoft.sharkadventure

import android.os.Bundle
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.nopalsoft.sharkadventure.handlers.FacebookHandler
import com.nopalsoft.sharkadventure.handlers.RequestHandler

class AndroidLauncher : AndroidApplication(), RequestHandler, FacebookHandler {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        initialize(MainShark(this, this), config)
    }

    override fun facebookSignIn() {
    }

    override fun showFacebook() {
        Gdx.net.openURI("https://www.facebook.com")
    }


    override fun showInterstitial() {
    }

    override fun shareOnTwitter(message: String) {
        val tweetUrl = "https://twitter.com/intent/tweet?text=$message Download it from &url=https://play.google.com/&hashtags=SharkAdventure"
        Gdx.net.openURI(tweetUrl)
    }

    override fun showAdBanner() {
    }

    override fun hideAdBanner() {
    }
}
