package com.nopalsoft.sharkadventure.handlers

interface RequestHandler {
    fun showInterstitial()

    fun shareOnTwitter(message: String)

    fun showAdBanner()

    fun hideAdBanner()
}
