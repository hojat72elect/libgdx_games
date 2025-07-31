package com.nopalsoft.zombiekiller.shop

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.I18NBundle
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.MainZombie
import com.nopalsoft.zombiekiller.Settings

class GetGemsSubMenu(var game: MainZombie, containerTable: Table) {
    var facebookLikeReward: Int = 1500

    // Common
    var buttonLikeFacebook: TextButton?
    var buttonInviteFacebook: TextButton?

    // iOS
    var buttonBuy5kCoins: TextButton?
    var buttonBuy15k: TextButton?
    var buttonBuy30kCoins: TextButton?
    var buttonBuy50kCoins: TextButton?

    var languagesBundle: I18NBundle = game.idiomas!!
    var textBuy: String?

    init {
        containerTable.clear()

        textBuy = languagesBundle.get("buy")

        buttonLikeFacebook = TextButton(languagesBundle.get("like_us"), Assets.styleTextButtonBuy)
        if (Settings.didLikeFacebook) buttonLikeFacebook = TextButton(languagesBundle.get("visit_us"), Assets.styleTextButtonPurchased)
        addPressEffect(buttonLikeFacebook!!)

        buttonInviteFacebook = TextButton(languagesBundle.get("invite"), Assets.styleTextButtonBuy)
        addPressEffect(buttonInviteFacebook!!)

        buttonBuy5kCoins = TextButton(textBuy, Assets.styleTextButtonBuy)
        addPressEffect(buttonBuy5kCoins!!)


        buttonBuy15k = TextButton(textBuy, Assets.styleTextButtonBuy)
        addPressEffect(buttonBuy15k!!)


        buttonBuy30kCoins = TextButton(textBuy, Assets.styleTextButtonBuy)
        addPressEffect(buttonBuy30kCoins!!)

        buttonBuy50kCoins = TextButton(textBuy, Assets.styleTextButtonBuy)
        addPressEffect(buttonBuy50kCoins!!)


        val faceLikeDescription = languagesBundle.format("facebook_like_description", facebookLikeReward)
        val faceInviteDescription = languagesBundle.format("facebook_invite_description", Settings.NUM_GEMS_INVITE_FACEBOOK)


        containerTable.add<Table?>(createPlayerTable(facebookLikeReward, Assets.btFacebook, faceLikeDescription, buttonLikeFacebook)).expandX().fill()
        containerTable.row()

        containerTable.add<Table?>(createPlayerTable(Settings.NUM_GEMS_INVITE_FACEBOOK, Assets.btFacebook, faceInviteDescription, buttonInviteFacebook))
            .expandX().fill()
        containerTable.row()


        val coinDrawable = TextureRegionDrawable(Assets.itemGem)

        // Coin sale

        // Buy 5 thousand
        containerTable.add<Table?>(createPlayerTable(5000, coinDrawable, languagesBundle.get("coin_simple_pack"), buttonBuy5kCoins)).expandX().fill()
        containerTable.row()

        // Buy 15 thousand
        containerTable.add<Table?>(createPlayerTable(15000, coinDrawable, languagesBundle.get("coin_super_pack"), buttonBuy15k)).expandX().fill()
        containerTable.row()

        containerTable.add<Table?>(createPlayerTable(30000, coinDrawable, languagesBundle.get("coin_mega_pack"), buttonBuy30kCoins)).expandX().fill()
        containerTable.row()

        containerTable.add<Table?>(createPlayerTable(50000, coinDrawable, languagesBundle.get("coin_super_mega_pack"), buttonBuy50kCoins)).expandX().fill()
        containerTable.row()
    }

    private fun createPlayerTable(numCoinsToAward: Int, playerDrawable: TextureRegionDrawable?, description: String?, button: TextButton?): Table {
        val coinImage = Image(Assets.itemGem)
        val playerImage = Image(playerDrawable)

        val titleBarTable = Table()
        titleBarTable.add(Label(languagesBundle.format("get_num", numCoinsToAward), Assets.labelStyleChico)).left().padLeft(5f)
        titleBarTable.add(coinImage).left().expandX().padLeft(5f).size(20f)

        val descriptionTable = Table()
        descriptionTable.add(playerImage).left().pad(10f).size(55f, 45f)
        val labelDescription = Label(description, Assets.labelStyleChico)
        labelDescription.setWrap(true)
        labelDescription.setFontScale(.9f)
        descriptionTable.add(labelDescription).expand().fill().padLeft(5f)

        val tableContent = Table()
        tableContent.pad(0f)
        tableContent.defaults().padLeft(20f).padRight(20f)
        tableContent.setBackground(Assets.storeTableBackground)
        tableContent.add(titleBarTable).expandX().fill().colspan(2).padTop(20f)
        tableContent.row().colspan(2)
        tableContent.add(descriptionTable).expandX().fill()
        tableContent.row().colspan(2)

        tableContent.add<TextButton?>(button).right().padBottom(20f).size(120f, 45f)

        return tableContent
    }

    private fun addPressEffect(actor: Actor) {
        actor.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                actor.setPosition(actor.getX(), actor.getY() - 3)
                event.stop()
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                actor.setPosition(actor.getX(), actor.getY() + 3)
            }
        })
    }
}
