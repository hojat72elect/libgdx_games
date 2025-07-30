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
    @JvmField
    var languagesBundle: I18NBundle? = null

    @JvmField
    var fontSmall: BitmapFont? = null

    @JvmField
    var fontLarge: BitmapFont? = null


    @JvmField
    var header: AtlasRegion? = null

    @JvmField
    var blackPixelDrawable: NinePatchDrawable? = null

    @JvmField
    var barTimerDrawable: NinePatchDrawable? = null

    @JvmField
    var rankingDialogDrawable: NinePatchDrawable? = null

    @JvmField
    var dialogDrawable: NinePatchDrawable? = null

    @JvmField
    var buttonFacebookTextDrawable: NinePatchDrawable? = null

    @JvmField
    var buttonGoogleTextDrawable: NinePatchDrawable? = null

    @JvmField
    var buttonPlayDrawable: NinePatchDrawable? = null
    var buttonEnabledDrawable: NinePatchDrawable? = null
    var buttonDisabledDrawable: NinePatchDrawable? = null

    @JvmField
    var titleDrawable: TextureRegionDrawable? = null

    var photoFrameDrawable: TextureRegionDrawable? = null

    @JvmField
    var buttonFacebookDrawable: TextureRegionDrawable? = null

    @JvmField
    var buttonGoogleDrawable: TextureRegionDrawable? = null

    @JvmField
    var buttonAmazonDrawable: TextureRegionDrawable? = null

    @JvmField
    var oneDrawable: TextureRegionDrawable? = null

    @JvmField
    var twoDrawable: TextureRegionDrawable? = null

    @JvmField
    var threeDrawable: TextureRegionDrawable? = null

    @JvmField
    var buttonRateDrawable: TextureRegionDrawable? = null

    @JvmField
    var buttonAchievementDrawable: TextureRegionDrawable? = null

    @JvmField
    var buttonLeaderBoardDrawable: TextureRegionDrawable? = null

    @JvmField
    var buttonHelpDrawable: TextureRegionDrawable? = null

    @JvmField
    var buttonTrueDrawable: TextureRegionDrawable? = null

    @JvmField
    var buttonFalseDrawable: TextureRegionDrawable? = null

    @JvmField
    var buttonBackDrawable: TextureRegionDrawable? = null

    @JvmField
    var buttonTryAgainDrawable: TextureRegionDrawable? = null

    @JvmField
    var buttonShareDrawable: TextureRegionDrawable? = null

    @JvmField
    var playDrawable: TextureRegionDrawable? = null

    @JvmField
    var flagChineseDrawable: TextureRegionDrawable? = null

    @JvmField
    var flagRussianDrawable: TextureRegionDrawable? = null

    @JvmField
    var flagSpanishDrawable: TextureRegionDrawable? = null

    @JvmField
    var flagEnglishDrawable: TextureRegionDrawable? = null

    @JvmField
    var flagFrenchDrawable: TextureRegionDrawable? = null

    @JvmField
    var flagJapaneseDrawable: TextureRegionDrawable? = null

    @JvmField
    var flagPortugueseDrawable: TextureRegionDrawable? = null

    @JvmField
    var flagMoreDrawable: TextureRegionDrawable? = null

    @JvmField
    var textButtonStyle: TextButtonStyle? = null

    fun load() {
        val atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        fontSmall = BitmapFont(Gdx.files.internal("data/font32.fnt"), atlas.findRegion("font32"))
        fontSmall!!.getData().markupEnabled = true

        fontLarge = BitmapFont(Gdx.files.internal("data/font100.fnt"), atlas.findRegion("font100"))

        titleDrawable = TextureRegionDrawable(atlas.findRegion("titulo"))
        header = atlas.findRegion("header")

        blackPixelDrawable = NinePatchDrawable(NinePatch(atlas.findRegion("pixelNegro"), 1, 1, 0, 0))
        barTimerDrawable = NinePatchDrawable(NinePatch(atlas.findRegion("barTimer"), 4, 4, 5, 4))
        rankingDialogDrawable = NinePatchDrawable(NinePatch(atlas.findRegion("dialogRanking"), 40, 40, 63, 30))
        dialogDrawable = NinePatchDrawable(NinePatch(atlas.findRegion("dialogVentana"), 33, 33, 33, 33))

        buttonPlayDrawable = NinePatchDrawable(NinePatch(atlas.findRegion("btJugar"), 30, 30, 25, 25))
        buttonEnabledDrawable = NinePatchDrawable(NinePatch(atlas.findRegion("btEnabled"), 9, 9, 7, 7))
        buttonDisabledDrawable = NinePatchDrawable(NinePatch(atlas.findRegion("btDisabled"), 9, 9, 7, 7))
        playDrawable = TextureRegionDrawable(atlas.findRegion("play"))

        buttonFacebookDrawable = TextureRegionDrawable(atlas.findRegion("btFacebook"))
        buttonFacebookTextDrawable = NinePatchDrawable(NinePatch(atlas.findRegion("btFacebookText"), 55, 20, 0, 0))
        buttonGoogleDrawable = TextureRegionDrawable(atlas.findRegion("btGoogle"))
        buttonGoogleTextDrawable = NinePatchDrawable(NinePatch(atlas.findRegion("btGoogleText"), 60, 20, 0, 0))
        buttonAmazonDrawable = TextureRegionDrawable(atlas.findRegion("btAmazon"))
        photoFrameDrawable = TextureRegionDrawable(atlas.findRegion("photoFrame"))

        oneDrawable = TextureRegionDrawable(atlas.findRegion("one"))
        twoDrawable = TextureRegionDrawable(atlas.findRegion("two"))
        threeDrawable = TextureRegionDrawable(atlas.findRegion("three"))

        buttonRateDrawable = TextureRegionDrawable(atlas.findRegion("btRate"))
        buttonAchievementDrawable = TextureRegionDrawable(atlas.findRegion("btAchievement"))
        buttonLeaderBoardDrawable = TextureRegionDrawable(atlas.findRegion("btLeaderboard"))
        buttonHelpDrawable = TextureRegionDrawable(atlas.findRegion("btHelp"))
        buttonTrueDrawable = TextureRegionDrawable(atlas.findRegion("btTrue"))
        buttonFalseDrawable = TextureRegionDrawable(atlas.findRegion("btFalse"))
        buttonBackDrawable = TextureRegionDrawable(atlas.findRegion("btBack"))
        buttonTryAgainDrawable = TextureRegionDrawable(atlas.findRegion("btTryAgain"))
        buttonShareDrawable = TextureRegionDrawable(atlas.findRegion("btShare"))

        textButtonStyle = TextButtonStyle(buttonDisabledDrawable, buttonEnabledDrawable, buttonEnabledDrawable, fontSmall)

        flagChineseDrawable = TextureRegionDrawable(atlas.findRegion("flags/flag_twd"))
        flagEnglishDrawable = TextureRegionDrawable(atlas.findRegion("flags/flag_gbp"))
        flagSpanishDrawable = TextureRegionDrawable(atlas.findRegion("flags/flag_esp"))
        flagRussianDrawable = TextureRegionDrawable(atlas.findRegion("flags/flag_rub"))
        flagFrenchDrawable = TextureRegionDrawable(atlas.findRegion("flags/flag_frf"))
        flagJapaneseDrawable = TextureRegionDrawable(atlas.findRegion("flags/flag_jpy"))
        flagPortugueseDrawable = TextureRegionDrawable(atlas.findRegion("flags/flag_pte"))
        flagMoreDrawable = TextureRegionDrawable(atlas.findRegion("flags/flag_more"))

        loadAssetsWithSettings()
    }

    @JvmStatic
    fun loadAssetsWithSettings() {
        val locale: Locale
        when (Settings.selectedLanguage) {
            Languages.ENGLISH -> locale = Locale.ROOT
            Languages.SPANISH -> locale = Locale("es")
            Languages.RUSSIAN -> locale = Locale("ru")
            Languages.FRENCH -> locale = Locale("fr")
            Languages.JAPANESE -> locale = Locale("ja")
            Languages.PORTUGUESE -> locale = Locale("pt")
            Languages.CHINESE -> locale = Locale("zh", "TW")
            Languages.DEFAULT -> locale = Locale.getDefault()
            else -> locale = Locale.getDefault()
        }

        languagesBundle = I18NBundle.createBundle(Gdx.files.internal("strings/strings"), locale)
    }
}
