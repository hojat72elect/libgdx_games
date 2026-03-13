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

    var idiomas: I18NBundle? = null
    var fontChico: BitmapFont? = null
    var titulo: TextureRegionDrawable? = null
    var header: AtlasRegion? = null
    var pixelNegro: NinePatchDrawable? = null
    var dialogRanking: NinePatchDrawable? = null
    var dialogVentana: NinePatchDrawable? = null
    var btFacebook: TextureRegionDrawable? = null
    var one: TextureRegionDrawable? = null
    var two: TextureRegionDrawable? = null
    var three: TextureRegionDrawable? = null
    var arrowGreen: TextureRegionDrawable? = null
    var arrowRed: TextureRegionDrawable? = null
    var arrowBlue: TextureRegionDrawable? = null
    var arrowYellow: TextureRegionDrawable? = null
    var arrowBlack: TextureRegionDrawable? = null
    var arrowBrown: TextureRegionDrawable? = null
    var arrowPurple: TextureRegionDrawable? = null
    var arrowCyan: TextureRegionDrawable? = null
    var circle: TextureRegionDrawable? = null
    var circleHard: TextureRegionDrawable? = null
    var btRate: TextureRegionDrawable? = null
    var btAchievement: TextureRegionDrawable? = null
    var btLeaderboard: TextureRegionDrawable? = null
    var btTrue: TextureRegionDrawable? = null
    var btFalse: TextureRegionDrawable? = null
    var btBack: TextureRegionDrawable? = null
    var btTryAgain: TextureRegionDrawable? = null
    var btShare: TextureRegionDrawable? = null
    var btJugar: NinePatchDrawable? = null
    var btEnabled: NinePatchDrawable? = null
    var btDisabled: NinePatchDrawable? = null
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
