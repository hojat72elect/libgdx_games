package com.nopalsoft.impossibledial

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.I18NBundle

object Assets {
    @JvmField
    var idiomas: I18NBundle? = null

    @JvmField
    var fontChico: BitmapFont? = null

    @JvmField
    var titulo: TextureRegionDrawable? = null

    @JvmField
    var header: AtlasRegion? = null


    @JvmField
    var pixelNegro: NinePatchDrawable? = null
    var dialogRanking: NinePatchDrawable? = null

    @JvmField
    var dialogVentana: NinePatchDrawable? = null

    @JvmField
    var btFacebook: TextureRegionDrawable? = null

    @JvmField
    var one: TextureRegionDrawable? = null

    @JvmField
    var two: TextureRegionDrawable? = null

    @JvmField
    var three: TextureRegionDrawable? = null

    @JvmField
    var arrowGreen: TextureRegionDrawable? = null

    @JvmField
    var arrowRed: TextureRegionDrawable? = null

    @JvmField
    var arrowBlue: TextureRegionDrawable? = null

    @JvmField
    var arrowYellow: TextureRegionDrawable? = null

    @JvmField
    var arrowBlack: TextureRegionDrawable? = null

    @JvmField
    var arrowBrown: TextureRegionDrawable? = null

    @JvmField
    var arrowPurple: TextureRegionDrawable? = null

    @JvmField
    var arrowCyan: TextureRegionDrawable? = null

    @JvmField
    var circle: TextureRegionDrawable? = null

    @JvmField
    var circleHard: TextureRegionDrawable? = null


    @JvmField
    var btRate: TextureRegionDrawable? = null

    @JvmField
    var btAchievement: TextureRegionDrawable? = null

    @JvmField
    var btLeaderboard: TextureRegionDrawable? = null
    var btTrue: TextureRegionDrawable? = null

    @JvmField
    var btFalse: TextureRegionDrawable? = null

    @JvmField
    var btBack: TextureRegionDrawable? = null

    @JvmField
    var btTryAgain: TextureRegionDrawable? = null

    @JvmField
    var btShare: TextureRegionDrawable? = null

    @JvmField
    var btJugar: NinePatchDrawable? = null
    var btEnabled: NinePatchDrawable? = null
    var btDisabled: NinePatchDrawable? = null

    @JvmField
    var play: TextureRegionDrawable? = null


    var txtButtonStyle: TextButtonStyle? = null

    fun load() {
        val atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        fontChico = BitmapFont(Gdx.files.internal("data/font32.fnt"), atlas.findRegion("font32"))
        fontChico!!.getData().markupEnabled = true

        titulo = TextureRegionDrawable(atlas.findRegion("titulo"))
        header = atlas.findRegion("header")

        pixelNegro = NinePatchDrawable(NinePatch(atlas.findRegion("pixelNegro"), 1, 1, 0, 0))
        dialogRanking = NinePatchDrawable(NinePatch(atlas.findRegion("dialogRanking"), 40, 40, 63, 30))
        dialogVentana = NinePatchDrawable(NinePatch(atlas.findRegion("dialogVentana"), 33, 33, 33, 33))

        btJugar = NinePatchDrawable(NinePatch(atlas.findRegion("btJugar"), 30, 30, 25, 25))
        btEnabled = NinePatchDrawable(NinePatch(atlas.findRegion("btEnabled"), 9, 9, 7, 7))
        btDisabled = NinePatchDrawable(NinePatch(atlas.findRegion("btDisabled"), 9, 9, 7, 7))
        play = TextureRegionDrawable(atlas.findRegion("play"))

        btFacebook = TextureRegionDrawable(atlas.findRegion("btFacebook"))


        one = TextureRegionDrawable(atlas.findRegion("one"))
        two = TextureRegionDrawable(atlas.findRegion("two"))
        three = TextureRegionDrawable(atlas.findRegion("three"))

        arrowBlue = TextureRegionDrawable(atlas.findRegion("arrowBlue"))
        arrowGreen = TextureRegionDrawable(atlas.findRegion("arrowGreen"))
        arrowRed = TextureRegionDrawable(atlas.findRegion("arrowRed"))
        arrowYellow = TextureRegionDrawable(atlas.findRegion("arrowYellow"))
        arrowBlack = TextureRegionDrawable(atlas.findRegion("arrowNegro"))
        arrowBrown = TextureRegionDrawable(atlas.findRegion("arrowBrown"))
        arrowPurple = TextureRegionDrawable(atlas.findRegion("arrowMorado"))
        arrowCyan = TextureRegionDrawable(atlas.findRegion("arrowCyan"))
        circle = TextureRegionDrawable(atlas.findRegion("circulo"))
        circleHard = TextureRegionDrawable(atlas.findRegion("circuloHard"))


        btRate = TextureRegionDrawable(atlas.findRegion("btRate"))
        btAchievement = TextureRegionDrawable(atlas.findRegion("btAchievement"))
        btLeaderboard = TextureRegionDrawable(atlas.findRegion("btLeaderboard"))
        btTrue = TextureRegionDrawable(atlas.findRegion("btTrue"))
        btFalse = TextureRegionDrawable(atlas.findRegion("btFalse"))
        btBack = TextureRegionDrawable(atlas.findRegion("btBack"))
        btTryAgain = TextureRegionDrawable(atlas.findRegion("btTryAgain"))
        btShare = TextureRegionDrawable(atlas.findRegion("btShare"))

        txtButtonStyle = TextButtonStyle(btDisabled, btEnabled, btEnabled, fontChico)


        idiomas = I18NBundle.createBundle(Gdx.files.internal("strings/strings"))
    }
}
