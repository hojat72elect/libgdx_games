package com.nopalsoft.thetruecolor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.I18NBundle
import com.nopalsoft.thetruecolor.scene2d.DialogHelpSettings.Languages
import java.util.Locale

object Assets {

    lateinit var languages: I18NBundle

    lateinit var fontSmall: BitmapFont

    lateinit var fontExtraLarge: BitmapFont

    lateinit var title: TextureRegionDrawable

    lateinit var header: AtlasRegion

    lateinit var pixelBlack: NinePatchDrawable

    lateinit var barTimer: NinePatchDrawable

    lateinit var dialogRanking: NinePatchDrawable

    lateinit var dialogWindow: NinePatchDrawable

    lateinit var photoFrame: TextureRegionDrawable

    lateinit var buttonFacebook: TextureRegionDrawable

    lateinit var buttonFacebookText: NinePatchDrawable

    lateinit var buttonGoogle: TextureRegionDrawable

    lateinit var buttonGoogleText: NinePatchDrawable

    lateinit var buttonAmazon: TextureRegionDrawable

    lateinit var one: TextureRegionDrawable

    lateinit var two: TextureRegionDrawable

    lateinit var three: TextureRegionDrawable

    lateinit var buttonRate: TextureRegionDrawable

    lateinit var buttonAchievement: TextureRegionDrawable

    lateinit var buttonLeaderboard: TextureRegionDrawable


    lateinit var buttonHelp: TextureRegionDrawable


    lateinit var buttonTrue: TextureRegionDrawable


    lateinit var buttonFalse: TextureRegionDrawable


    lateinit var buttonBack: TextureRegionDrawable


    lateinit var buttonTryAgain: TextureRegionDrawable


    lateinit var buttonShare: TextureRegionDrawable


    lateinit var buttonPlay: NinePatchDrawable
    private lateinit var buttonEnabled: NinePatchDrawable
    private lateinit var buttonDisabled: NinePatchDrawable


    lateinit var play: TextureRegionDrawable


    lateinit var flagChinese_TW: TextureRegionDrawable


    lateinit var flagRussian: TextureRegionDrawable


    lateinit var flagSpanish: TextureRegionDrawable


    lateinit var flagEnglish: TextureRegionDrawable


    lateinit var flagFrench: TextureRegionDrawable


    lateinit var flagJapanese: TextureRegionDrawable

    lateinit var flagPortugese: TextureRegionDrawable

    lateinit var flagMore: TextureRegionDrawable

    lateinit var textButtonStyle: TextButtonStyle

    fun load() {
        val atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        fontSmall = BitmapFont(Gdx.files.internal("data/font32.fnt"), atlas.findRegion("font32"))
        fontSmall.data.markupEnabled = true

        fontExtraLarge =
            BitmapFont(Gdx.files.internal("data/font100.fnt"), atlas.findRegion("font100"))

        title = TextureRegionDrawable(atlas.findRegion("titulo"))
        header = atlas.findRegion("header")

        pixelBlack = NinePatchDrawable(NinePatch(atlas.findRegion("pixelNegro"), 1, 1, 0, 0))
        barTimer = NinePatchDrawable(NinePatch(atlas.findRegion("barTimer"), 4, 4, 5, 4))
        dialogRanking =
            NinePatchDrawable(NinePatch(atlas.findRegion("dialogRanking"), 40, 40, 63, 30))
        dialogWindow =
            NinePatchDrawable(NinePatch(atlas.findRegion("dialogVentana"), 33, 33, 33, 33))

        buttonPlay = NinePatchDrawable(NinePatch(atlas.findRegion("btJugar"), 30, 30, 25, 25))
        buttonEnabled = NinePatchDrawable(NinePatch(atlas.findRegion("btEnabled"), 9, 9, 7, 7))
        buttonDisabled = NinePatchDrawable(NinePatch(atlas.findRegion("btDisabled"), 9, 9, 7, 7))
        play = TextureRegionDrawable(atlas.findRegion("play"))

        buttonFacebook = TextureRegionDrawable(atlas.findRegion("btFacebook"))
        buttonFacebookText =
            NinePatchDrawable(NinePatch(atlas.findRegion("btFacebookText"), 55, 20, 0, 0))
        buttonGoogle = TextureRegionDrawable(atlas.findRegion("btGoogle"))
        buttonGoogleText =
            NinePatchDrawable(NinePatch(atlas.findRegion("btGoogleText"), 60, 20, 0, 0))
        buttonAmazon = TextureRegionDrawable(atlas.findRegion("btAmazon"))
        photoFrame = TextureRegionDrawable(atlas.findRegion("photoFrame"))

        one = TextureRegionDrawable(atlas.findRegion("one"))
        two = TextureRegionDrawable(atlas.findRegion("two"))
        three = TextureRegionDrawable(atlas.findRegion("three"))

        buttonRate = TextureRegionDrawable(atlas.findRegion("btRate"))
        buttonAchievement = TextureRegionDrawable(atlas.findRegion("btAchievement"))
        buttonLeaderboard = TextureRegionDrawable(atlas.findRegion("btLeaderboard"))
        buttonHelp = TextureRegionDrawable(atlas.findRegion("btHelp"))
        buttonTrue = TextureRegionDrawable(atlas.findRegion("btTrue"))
        buttonFalse = TextureRegionDrawable(atlas.findRegion("btFalse"))
        buttonBack = TextureRegionDrawable(atlas.findRegion("btBack"))
        buttonTryAgain = TextureRegionDrawable(atlas.findRegion("btTryAgain"))
        buttonShare = TextureRegionDrawable(atlas.findRegion("btShare"))

        textButtonStyle = TextButtonStyle(buttonDisabled, buttonEnabled, buttonEnabled, fontSmall)

        flagChinese_TW = TextureRegionDrawable(atlas.findRegion("flags/flag_twd"))
        flagEnglish = TextureRegionDrawable(atlas.findRegion("flags/flag_gbp"))
        flagSpanish = TextureRegionDrawable(atlas.findRegion("flags/flag_esp"))
        flagRussian = TextureRegionDrawable(atlas.findRegion("flags/flag_rub"))
        flagFrench = TextureRegionDrawable(atlas.findRegion("flags/flag_frf"))
        flagJapanese = TextureRegionDrawable(atlas.findRegion("flags/flag_jpy"))
        flagPortugese = TextureRegionDrawable(atlas.findRegion("flags/flag_pte"))
        flagMore = TextureRegionDrawable(atlas.findRegion("flags/flag_more"))

        loadAssetsWithSettings()
    }

    @JvmStatic
    fun loadAssetsWithSettings() {
        val locale = when (Settings.selectedLanguage) {
            Languages.ENGLISH -> Locale.ROOT
            Languages.SPANISH -> Locale("es")
            Languages.RUSSIAN -> Locale("ru")
            Languages.FRENCH -> Locale("fr")
            Languages.JAPANESE -> Locale("ja")
            Languages.PORTUGUESE -> Locale("pt")
            Languages.CHINESE_TAIWAN -> Locale("zh", "TW")
            Languages.DEFAULT -> Locale.getDefault()
        }
        languages = I18NBundle.createBundle(Gdx.files.internal("strings/strings"), locale)
    }
}
