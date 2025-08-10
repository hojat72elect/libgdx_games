package com.nopalsoft.lander

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.lander.Settings.load

object Assets {
    @JvmField
    var isDebug = false

    lateinit var titulo: TextureRegionDrawable
    lateinit var nave: AtlasRegion
    lateinit var naveFly: Animation<TextureRegion>
    lateinit var explosion: Animation<TextureRegion>
    lateinit var laser: Animation<TextureRegion>
    lateinit var laserVertical: Animation<TextureRegion>
    lateinit var fondo: AtlasRegion
    lateinit var candado: AtlasRegion
    lateinit var gas: AtlasRegion
    lateinit var star: AtlasRegion
    lateinit var starOff: AtlasRegion
    lateinit var bomba: AtlasRegion
    lateinit var upgradeOn: AtlasRegion
    lateinit var upgradeOff: AtlasRegion
    lateinit var marcoStats: TextureRegionDrawable
    lateinit var barraMarcadorRojo: AtlasRegion
    lateinit var barraMarcadorVerde: AtlasRegion
    lateinit var font: BitmapFont
    lateinit var mundos: LinkedHashMap<Int, String>
    @JvmField
    var map: TiledMap? = null
    lateinit var styleTextButtonMenu: TextButtonStyle
    lateinit var styleTextButtonLevels: TextButtonStyle
    lateinit var styleLabelMediana: LabelStyle
    lateinit var styleImageButtonPause: ImageButtonStyle
    lateinit var styleImageButtonUpgradePlus: ImageButtonStyle
    lateinit var styleDialogGameOver: WindowStyle

    private fun loadSceneStyles(atlas: TextureAtlas) {
        styleLabelMediana = LabelStyle(font, Color.BLACK)

        // Botones del menu
        val botonMenu = TextureRegionDrawable(atlas.findRegion("btMenu"))
        val botonMenuDown = TextureRegionDrawable(atlas.findRegion("btMenuDown"))
        styleTextButtonMenu = TextButtonStyle(botonMenu, botonMenuDown, null, font)
        styleTextButtonMenu.fontColor = Color.GREEN

        // Boton + de la ventana upgrades
        val btUp = TextureRegionDrawable(atlas.findRegion("btUpgrade"))
        val btUpDown = TextureRegionDrawable(atlas.findRegion("btUpgradeDown"))
        styleImageButtonUpgradePlus = ImageButtonStyle(btUp, btUpDown, null, null, null, null)

        // Dialogo GameOVer, Paused, Tienda
        val recuadroGame = NinePatchDrawable(atlas.createPatch("recuadroGameOver"))
        val dialogDim = atlas.findRegion("dim")
        styleDialogGameOver = WindowStyle(font, Color.GREEN, recuadroGame)
        styleDialogGameOver.stageBackground = NinePatchDrawable(NinePatch(dialogDim))

        // Botton Pausa
        val btPause = TextureRegionDrawable(atlas.findRegion("btPause"))
        val btPauseDown = TextureRegionDrawable(atlas.findRegion("btPauseDown"))
        styleImageButtonPause = ImageButtonStyle(btPause, btPauseDown, null, null, null, null)

        // Botones levels
        val btLevels = NinePatchDrawable(atlas.createPatch("btnLevels"))
        val btLevelsDown = NinePatchDrawable(atlas.createPatch("btLevelsDown"))
        styleTextButtonLevels = TextButtonStyle(btLevels, btLevelsDown, null, font)
        styleTextButtonLevels.fontColor = Color.GREEN
    }

    fun cargar() {
        val atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        font = BitmapFont()
        loadSceneStyles(atlas)

        titulo = TextureRegionDrawable(atlas.findRegion("titulo"))

        nave = atlas.findRegion("nave")

        val an1 = atlas.findRegion("nave1")
        val an2 = atlas.findRegion("nave2")
        val an3 = atlas.findRegion("nave3")
        val an4 = atlas.findRegion("nave4")
        val an5 = atlas.findRegion("nave5")
        naveFly = Animation<TextureRegion>(.15f, an1, an2, an3, an4, an5)

        barraMarcadorRojo = atlas.findRegion("barraMarcadorRojo")
        barraMarcadorVerde = atlas.findRegion("barraMarcadorVerde")
        marcoStats = TextureRegionDrawable(atlas.findRegion("marcador"))

        fondo = atlas.findRegion("fondo")

        val newExpl1 = atlas.findRegion("newExplosion1")
        val newExpl2 = atlas.findRegion("newExplosion2")
        val newExpl3 = atlas.findRegion("newExplosion3")
        val newExpl4 = atlas.findRegion("newExplosion4")
        val newExpl5 = atlas.findRegion("newExplosion5")
        val newExpl6 = atlas.findRegion("newExplosion6")
        val newExpl7 = atlas.findRegion("newExplosion7")
        val newExpl8 = atlas.findRegion("newExplosion8")
        val newExpl9 = atlas.findRegion("newExplosion9")
        val newExpl10 = atlas.findRegion("newExplosion10")
        val newExpl11 = atlas.findRegion("newExplosion11")
        val newExpl12 = atlas.findRegion("newExplosion12")
        val newExpl13 = atlas.findRegion("newExplosion13")
        val newExpl14 = atlas.findRegion("newExplosion14")
        val newExpl15 = atlas.findRegion("newExplosion15")
        val newExpl16 = atlas.findRegion("newExplosion16")
        val newExpl17 = atlas.findRegion("newExplosion17")
        val newExpl18 = atlas.findRegion("newExplosion18")
        val newExpl19 = atlas.findRegion("newExplosion19")
        explosion = Animation<TextureRegion>(
            0.05f,
            newExpl1,
            newExpl2,
            newExpl3,
            newExpl4,
            newExpl5,
            newExpl6,
            newExpl7,
            newExpl8,
            newExpl9,
            newExpl10,
            newExpl11,
            newExpl12,
            newExpl13,
            newExpl14,
            newExpl15,
            newExpl16,
            newExpl17,
            newExpl18,
            newExpl19
        )

        var laser0 = atlas.findRegion("rayo0")
        var laser1 = atlas.findRegion("rayo1")
        var laser2 = atlas.findRegion("rayo2")
        var laser3 = atlas.findRegion("rayo3")
        laser = Animation<TextureRegion>(0.1f, laser0, laser1, laser2, laser3)

        laser0 = atlas.findRegion("rayo0Vertical")
        laser1 = atlas.findRegion("rayo1Vertical")
        laser2 = atlas.findRegion("rayo2Vertical")
        laser3 = atlas.findRegion("rayo3Vertical")
        laserVertical = Animation<TextureRegion>(0.1f, laser0, laser1, laser2, laser3)

        gas = atlas.findRegion("gas")
        star = atlas.findRegion("star")
        starOff = atlas.findRegion("starOff")
        bomba = atlas.findRegion("bomba")
        candado = atlas.findRegion("candado")

        upgradeOn = atlas.findRegion("upgradeOn")
        upgradeOff = atlas.findRegion("upgradeOff")

        mundos = LinkedHashMap()
        mundos.put(0, "data/mundos/mundo00.tmx")
        mundos.put(1, "data/mundos/mundo01.tmx")
        mundos.put(2, "data/mundos/mundo02.tmx")
        mundos.put(3, "data/mundos/mundo03.tmx")
        mundos.put(4, "data/mundos/mundo04.tmx")
        mundos.put(5, "data/mundos/mundo05.tmx")
        mundos.put(6, "data/mundos/mundo06.tmx")
        mundos.put(7, "data/mundos/mundo07.tmx")
        mundos.put(8, "data/mundos/mundo08.tmx")
        mundos.put(9, "data/mundos/mundo09.tmx")
        mundos.put(10, "data/mundos/mundo10.tmx")
        mundos.put(11, "data/mundos/mundo11.tmx")

        load(500)
    }

    @JvmStatic
    fun cargarMapa(numeroMundo: Int) {
        if (map != null) {
            map!!.dispose()
            map = null
        }
        map = TmxMapLoader().load(mundos.get(numeroMundo))
    }
}
