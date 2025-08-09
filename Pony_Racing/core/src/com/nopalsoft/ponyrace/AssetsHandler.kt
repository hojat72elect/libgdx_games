package com.nopalsoft.ponyrace

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader
import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader.AtlasTiledMapLoaderParameters
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.esotericsoftware.spine.Skeleton
import com.esotericsoftware.spine.SkeletonData
import com.esotericsoftware.spine.SkeletonJson
import java.util.Random

class AssetsHandler : AssetManager() {
    var musicaMenus: Music? = null
    var musicaTiled: Music? = null

    @JvmField
    var nube: Animation<TextureRegion?>? = null

    @JvmField
    var btSonidoON: NinePatchDrawable? = null

    @JvmField
    var btSonidoOff: NinePatchDrawable? = null

    @JvmField
    var btMusicaON: NinePatchDrawable? = null

    @JvmField
    var btMusicaOff: NinePatchDrawable? = null
    var nombrePonys: LinkedHashMap<Int?, String> = LinkedHashMap(6)
    var skin: Skin? = null
    var btSignInUp: NinePatchDrawable? = null
    var btSignInDown: NinePatchDrawable? = null
    var btShareFacebookUp: NinePatchDrawable? = null
    var btShareFacebookDown: NinePatchDrawable? = null

    @JvmField
    var fontGde: BitmapFont? = null

    @JvmField
    var fontChco: BitmapFont? = null

    var fondoMainMenu: AtlasRegion? = null
    var btnFacebook: NinePatchDrawable? = null
    var skeletonMenuTitle: Skeleton? = null
    var animationMenuTitle: com.esotericsoftware.spine.Animation? = null

    var skeletonTiendaTitle: Skeleton? = null
    var animationTiendaTitle: com.esotericsoftware.spine.Animation? = null
    var fondoTienda: AtlasRegion? = null
    var monedaTienda: AtlasRegion? = null
    var bananaSpikeTienda: AtlasRegion? = null
    var bombaTienda: AtlasRegion? = null
    var chocolateTienda: AtlasRegion? = null
    var globoTienda: AtlasRegion? = null
    var chileTienda: AtlasRegion? = null
    var cronometroTienda: AtlasRegion? = null
    var btNubeUpTienda: NinePatchDrawable? = null
    var btNubeDownTienda: NinePatchDrawable? = null
    var miniNubeScroll: NinePatchDrawable? = null
    var barraScroll: NinePatchDrawable? = null
    var btMenuLeftUp: NinePatchDrawable? = null
    var btMenuLeftDown: NinePatchDrawable? = null
    var perfilCloud: TextureRegionDrawable? = null
    var perfilNatylol: TextureRegionDrawable? = null
    var perfilIgnis: TextureRegionDrawable? = null
    var perfilcientifico: TextureRegionDrawable? = null
    var perfilLAlba: TextureRegionDrawable? = null
    var perfilenemigo: TextureRegionDrawable? = null

    /**
     * new struf
     */
    @JvmField
    var tiledWorldMap: TiledMap? = null

    @JvmField
    var bolaSkeleton: Skeleton? = null

    @JvmField
    var bolaAnim: com.esotericsoftware.spine.Animation? = null

    @JvmField
    var rayoSkeleton: Skeleton? = null

    @JvmField
    var rayoAnim: com.esotericsoftware.spine.Animation? = null

    @JvmField
    var humoVolcanSkeleton: Skeleton? = null

    @JvmField
    var humoVolvanAnimation: com.esotericsoftware.spine.Animation? = null

    @JvmField
    var btDerUp: NinePatchDrawable? = null

    @JvmField
    var btDerDown: NinePatchDrawable? = null

    @JvmField
    var btIzqUp: NinePatchDrawable? = null

    /**
     * ################################################################## WORLD MAP #############################################################
     */
    @JvmField
    var btIzqDown: NinePatchDrawable? = null

    // fin new stuff
    var fondo: AtlasRegion? = null // Lo estoy cargando en atlasComun para ahorrar espacio OMG! ajaj

    @JvmField
    var tiledMap: TiledMap? = null
    var ponySkeletonData: SkeletonData? = null
    var skeletonBombData: SkeletonData? = null

    @JvmField
    var bombAnim: com.esotericsoftware.spine.Animation? = null

    @JvmField
    var bombExAnim: com.esotericsoftware.spine.Animation? = null
    var skeletonMonedaData: SkeletonData? = null

    @JvmField
    var monedaAnim: com.esotericsoftware.spine.Animation? = null

    @JvmField
    var monedaTomadaAnim: com.esotericsoftware.spine.Animation? = null
    var chileSkeletonData: SkeletonData? = null

    @JvmField
    var chileAnim: com.esotericsoftware.spine.Animation? = null

    @JvmField
    var chileTomadaAnim: com.esotericsoftware.spine.Animation? = null
    var globoSkeletonData: SkeletonData? = null

    @JvmField
    var globoAnim: com.esotericsoftware.spine.Animation? = null

    @JvmField
    var globoTomadaAnim: com.esotericsoftware.spine.Animation? = null
    var dulceSkeletonData: SkeletonData? = null

    @JvmField
    var dulceAnim: com.esotericsoftware.spine.Animation? = null

    @JvmField
    var dulceTomadaAnim: com.esotericsoftware.spine.Animation? = null

    @JvmField
    var fondoSkeleton: Skeleton? = null

    @JvmField
    var fondoAnim: com.esotericsoftware.spine.Animation? = null

    @JvmField
    var fogataSkeleton: Skeleton? = null

    @JvmField
    var fogataAnim: com.esotericsoftware.spine.Animation? = null

    @JvmField
    var plumaSkeleton: Skeleton? = null

    @JvmField
    var plumaAnim: com.esotericsoftware.spine.Animation? = null

    @JvmField
    var bloodStoneSkeleton: Skeleton? = null

    @JvmField
    var bloodStoneAnim: com.esotericsoftware.spine.Animation? = null

    @JvmField
    var bloodStone2Skeleton: Skeleton? = null

    @JvmField
    var bloodStone2Anim: com.esotericsoftware.spine.Animation? = null
    var medallaPrimerLugar: AtlasRegion? = null
    var medallaSegundoLugar: AtlasRegion? = null
    var medallaTercerLugar: AtlasRegion? = null
    var congratulations: AtlasRegion? = null
    var youLose: AtlasRegion? = null
    var timeUp: AtlasRegion? = null

    @JvmField
    var padIzq: NinePatchDrawable? = null

    @JvmField
    var padDer: NinePatchDrawable? = null

    @JvmField
    var btBombaUp: NinePatchDrawable? = null

    @JvmField
    var btBombaDown: NinePatchDrawable? = null

    @JvmField
    var btTroncoUp: NinePatchDrawable? = null

    @JvmField
    var btTroncoDown: NinePatchDrawable? = null

    @JvmField
    var btJumpUp: NinePatchDrawable? = null

    @JvmField
    var btJumpDown: NinePatchDrawable? = null

    @JvmField
    var btPauseUp: NinePatchDrawable? = null
    var fondoVentanas: NinePatchDrawable? = null

    @JvmField
    var indicador: AtlasRegion? = null

    @JvmField
    var indicadorCloud: AtlasRegion? = null

    @JvmField
    var indicadorCientifico: AtlasRegion? = null

    @JvmField
    var indicadorMinion: AtlasRegion? = null

    @JvmField
    var indicadorNatylol: AtlasRegion? = null

    @JvmField
    var indicadorLighthingAlba: AtlasRegion? = null

    @JvmField
    var indicadorIgnis: AtlasRegion? = null

    @JvmField
    var lugaresMarco: AtlasRegion? = null

    @JvmField
    var moneda: AtlasRegion? = null

    @JvmField
    var perfilRegionCloud: AtlasRegion? = null

    @JvmField
    var perfilRegionNatylol: AtlasRegion? = null

    @JvmField
    var perfilRegionIgnis: AtlasRegion? = null

    @JvmField
    var perfilRegionCientifico: AtlasRegion? = null

    @JvmField
    var perfilRegionLAlba: AtlasRegion? = null

    @JvmField
    var perfilRegionEnemigo: AtlasRegion? = null
    var tronco: AtlasRegion? = null

    @JvmField
    var tachuelas: AtlasRegion? = null

    @JvmField
    var platano: AtlasRegion? = null

    @JvmField
    var pickCoin: Sound? = null

    @JvmField
    var jump: Sound? = null
    var atlasComun: String = "data/atlasComun.txt"
    var atlasMenusRuta: String = "data/menus/atlasMenus.txt"
    var atlasWorldTiledScreenRuta: String = "data/worldMap/worldmap.tmx"

    var rutaTiled: String? = null
    var rutaMusica: String? = null
    var atlasTiledStuff: String = "data/animaciones/animacionesJuego.txt"

    init {
        nombrePonys.put(0, "Cloud")
        nombrePonys.put(1, "Natylol")
        nombrePonys.put(2, "Ignis")
        nombrePonys.put(3, "cientifico")
        nombrePonys.put(4, "LAlba")
        nombrePonys.put(5, "enemigo")

        if (usarPacked) this.setLoader<TiledMap?, AtlasTiledMapLoaderParameters?>(TiledMap::class.java, AtlasTmxMapLoader(InternalFileHandleResolver()))
        else this.setLoader<TiledMap?, TmxMapLoader.Parameters?>(TiledMap::class.java, TmxMapLoader(InternalFileHandleResolver()))
        cargarFont()

        load<TextureAtlas?>(atlasComun, TextureAtlas::class.java)
    }

    fun cargarComun() {
        val atlas = get(atlasComun, TextureAtlas::class.java)
        val nube0 = atlas.findRegion("nube0")
        val nube1 = atlas.findRegion("nube1")
        val nube2 = atlas.findRegion("nube2")
        val nube3 = atlas.findRegion("nube3")
        val nube4 = atlas.findRegion("nube4")
        val nube5 = atlas.findRegion("nube5")
        val nube6 = atlas.findRegion("nube6")
        nube = Animation<TextureRegion?>(.075f, nube0, nube1, nube2, nube3, nube4, nube5, nube6)

        btSonidoON = NinePatchDrawable(NinePatch(atlas.findRegion("soundpausa")))
        btSonidoOff = NinePatchDrawable(NinePatch(atlas.findRegion("soundoffpausa")))
        btMusicaON = NinePatchDrawable(NinePatch(atlas.findRegion("musicpausa")))
        btMusicaOff = NinePatchDrawable(NinePatch(atlas.findRegion("musicoffpausa")))

        fondoVentanas = NinePatchDrawable(NinePatch(atlas.createPatch("fondoVentana")))

        val size = 80
        btSonidoOff!!.minHeight = size.toFloat()
        btSonidoOff!!.minWidth = size.toFloat()
        btSonidoON!!.minHeight = size.toFloat()
        btSonidoON!!.minWidth = size.toFloat()
        btMusicaOff!!.minHeight = size.toFloat()
        btMusicaOff!!.minWidth = size.toFloat()
        btMusicaON!!.minHeight = size.toFloat()
        btMusicaON!!.minWidth = size.toFloat()

        skin = Skin(Gdx.files.internal("data/menus/uiskin.json"), atlas)
        skin!!.getFont("default-font").getData().setScale(.5f)
        btSignInUp = NinePatchDrawable(NinePatch(atlas.createPatch("btSignInUp")))
        btSignInDown = NinePatchDrawable(NinePatch(atlas.createPatch("btSignInDown")))

        btShareFacebookUp = NinePatchDrawable(NinePatch(atlas.createPatch("btShareFacebookUp")))
        btShareFacebookDown = NinePatchDrawable(NinePatch(atlas.createPatch("btShareFacebookDown")))
    }

    fun cargarFont() {
        val texturaFont = Texture(Gdx.files.internal("data/fonts/fontMenus2.png"))
        texturaFont.setFilter(TextureFilter.Linear, TextureFilter.Linear)

        fontGde = BitmapFont(Gdx.files.internal("data/fonts/fontMenus2.fnt"), TextureRegion(texturaFont), false)
        fontGde!!.setUseIntegerPositions(false)
        fontGde!!.setFixedWidthGlyphs("0123456789")

        fontChco = BitmapFont(Gdx.files.internal("data/fonts/fontMenus2.fnt"), TextureRegion(texturaFont), false)
        fontChco!!.setUseIntegerPositions(false)
        fontChco!!.setFixedWidthGlyphs("0123456789")
    }

    fun loadMenus() {
        if (!isLoaded("data/musica/00.mp3")) load("data/musica/00.mp3", Music::class.java)

        if (!isLoaded(atlasMenusRuta)) load(atlasMenusRuta, TextureAtlas::class.java)

        if (!isLoaded(atlasWorldTiledScreenRuta)) load(atlasWorldTiledScreenRuta, TiledMap::class.java)
    }

    fun cargarMenus() {
        cargarComun()
        musicaMenus = get("data/musica/00.mp3", Music::class.java)
        musicaMenus!!.isLooping = true

        playMusicMenus()

        val atlas = get(atlasMenusRuta, TextureAtlas::class.java)
        fondoMainMenu = atlas.findRegion("fondoMenu")
        btnFacebook = NinePatchDrawable(NinePatch(atlas.findRegion("botonFace")))

        val json = SkeletonJson(atlas)

        json.setScale(.7f)
        val skeletonData = json.readSkeletonData(Gdx.files.internal("data/menus/titleponyracing.json"))
        animationMenuTitle = skeletonData.findAnimation("flag")
        skeletonMenuTitle = Skeleton(skeletonData)

        cargarWorldMap(atlas)
        cargarTienda(atlas)
    }

    fun unLoadMenus() {
        musicaMenus!!.stop()
        unload("data/musica/00.mp3")
        unload(atlasMenusRuta)
        unload(atlasWorldTiledScreenRuta)
        System.gc()
    }

    fun cargarTienda(atlas: TextureAtlas) {
        cargarComun()
        fondoTienda = atlas.findRegion("fondoTienda")

        monedaTienda = atlas.findRegion("minicointienda")
        bananaSpikeTienda = atlas.findRegion("bananaSpikes")
        bombaTienda = atlas.findRegion("minibombatienda")
        cronometroTienda = atlas.findRegion("cronometro")
        chocolateTienda = atlas.findRegion("miniChocolate")
        globoTienda = atlas.findRegion("miniGlobos")
        chileTienda = atlas.findRegion("miniChile")

        btNubeUpTienda = NinePatchDrawable(NinePatch(atlas.findRegion("botonBuy")))
        btNubeDownTienda = NinePatchDrawable(NinePatch(atlas.findRegion("botonBuyPresionado")))
        miniNubeScroll = NinePatchDrawable(NinePatch(atlas.findRegion("mininubescroll")))
        barraScroll = NinePatchDrawable(NinePatch(atlas.findRegion("barradescroll")))

        btMenuLeftUp = NinePatchDrawable(NinePatch(atlas.findRegion("btSinPresionar")))
        btMenuLeftDown = NinePatchDrawable(NinePatch(atlas.findRegion("btPresionado")))

        perfilCloud = TextureRegionDrawable(atlas.findRegion("perfiles/cloud"))
        perfilNatylol = TextureRegionDrawable(atlas.findRegion("perfiles/natylol"))
        perfilIgnis = TextureRegionDrawable(atlas.findRegion("perfiles/ignis"))
        perfilcientifico = TextureRegionDrawable(atlas.findRegion("perfiles/scientist"))
        perfilLAlba = TextureRegionDrawable(atlas.findRegion("perfiles/lightingalba"))
        perfilenemigo = TextureRegionDrawable(atlas.findRegion("perfiles/enemy"))

        val json = SkeletonJson(atlas)

        json.setScale(1f)
        val skeletonData = json.readSkeletonData(Gdx.files.internal("data/menus/shoptitle.json"))
        animationTiendaTitle = skeletonData.findAnimation("animation")
        skeletonTiendaTitle = Skeleton(skeletonData)
    }

    fun cargarWorldMap(atlas: TextureAtlas) {
        cargarComun()
        val json = SkeletonJson(atlas)

        tiledWorldMap = get(atlasWorldTiledScreenRuta, TiledMap::class.java)


        json.setScale(.007f)
        var skeletonData = json.readSkeletonData(Gdx.files.internal("data/menus/ball.json"))
        bolaAnim = skeletonData.findAnimation("pulse")
        bolaSkeleton = Skeleton(skeletonData)

        json.setScale(.0225f)
        skeletonData = json.readSkeletonData(Gdx.files.internal("data/menus/thunder.json"))
        rayoAnim = skeletonData.findAnimation("floating")
        rayoSkeleton = Skeleton(skeletonData)

        json.setScale(.025f)
        skeletonData = json.readSkeletonData(Gdx.files.internal("data/menus/humovolcan.json"))
        humoVolvanAnimation = skeletonData.findAnimation("humo")
        humoVolcanSkeleton = Skeleton(skeletonData)

        btDerUp = NinePatchDrawable(NinePatch(atlas.findRegion("derSinPresionar")))
        btDerDown = NinePatchDrawable(NinePatch(atlas.findRegion("derPresionado")))
        btIzqUp = NinePatchDrawable(NinePatch(atlas.findRegion("izqSinPresionar")))
        btIzqDown = NinePatchDrawable(NinePatch(atlas.findRegion("izqPresionado")))
    }

    /**
     * Primero se llama el load, cuando se termina de cargar se llama cargar
     */
    fun loadGameScreenTiled(nivelTiled: Int) {
        cargarComun()
        var carpeta = "tiled"
        if (usarPacked) carpeta = "tiledp"

        when (nivelTiled) {
            1 -> {
                rutaTiled = "data/$carpeta/mundo01.tmx"
                rutaMusica = "data/musica/01.mp3"
            }

            2 -> {
                rutaTiled = "data/$carpeta/mundo02.tmx"
                rutaMusica = "data/musica/02.mp3"
            }

            3 -> {
                rutaTiled = "data/$carpeta/mundo03.tmx"
                rutaMusica = "data/musica/03.mp3"
            }

            4 -> {
                rutaTiled = "data/$carpeta/mundo04.tmx"
                rutaMusica = "data/musica/04.mp3"
            }

            5 -> {
                rutaTiled = "data/$carpeta/mundo05.tmx"
                rutaMusica = "data/musica/05.mp3"
            }

            6 -> {
                rutaTiled = "data/$carpeta/mundo06.tmx"
                rutaMusica = "data/musica/01.mp3"
            }

            7 -> {
                rutaTiled = "data/$carpeta/mundo07.tmx"
                rutaMusica = "data/musica/02.mp3"
            }

            8 -> {
                rutaTiled = "data/$carpeta/mundo08.tmx"
                rutaMusica = "data/musica/03.mp3"
            }

            9 -> {
                rutaTiled = "data/$carpeta/mundo09.tmx"
                rutaMusica = "data/musica/04.mp3"
            }

            10 -> {
                rutaTiled = "data/$carpeta/mundo10.tmx"
                rutaMusica = "data/musica/05.mp3"
            }

            11 -> {
                rutaTiled = "data/$carpeta/mundo11.tmx"
                rutaMusica = "data/musica/01.mp3"
            }

            12 -> {
                rutaTiled = "data/$carpeta/mundo12.tmx"
                rutaMusica = "data/musica/02.mp3"
            }

            13 -> {
                rutaTiled = "data/$carpeta/mundo13.tmx"
                rutaMusica = "data/musica/04.mp3"
            }

            14 -> {
                rutaTiled = "data/$carpeta/mundo14.tmx"
                rutaMusica = "data/musica/05.mp3"
            }

            15 -> {
                rutaTiled = "data/$carpeta/mundo15.tmx"
                rutaMusica = "data/musica/01.mp3"
            }

            16 -> {
                rutaTiled = "data/$carpeta/mundo16.tmx"
                rutaMusica = "data/musica/02.mp3"
            }

            17 -> {
                rutaTiled = "data/$carpeta/mundo17.tmx"
                rutaMusica = "data/musica/03.mp3"
            }

            1000 -> { // Mundo de muchas monedas
                val mundo = Random().nextInt(2)

                rutaTiled = if (mundo == 0) {
                    "data/$carpeta/especial01.tmx"
                } else {
                    "data/$carpeta/especial02.tmx"
                }

                rutaMusica = "data/musica/01.mp3"
            }
        }

        if (!isLoaded(rutaMusica)) load(rutaMusica, Music::class.java)

        if (!isLoaded(atlasTiledStuff)) load(atlasTiledStuff, TextureAtlas::class.java)

        if (!isLoaded(rutaTiled)) load(rutaTiled, TiledMap::class.java)

        if (!isLoaded("data/musica/coin.mp3")) load("data/musica/coin.mp3", Sound::class.java)

        if (!isLoaded("data/musica/salto.mp3")) load("data/musica/salto.mp3", Sound::class.java)
    }

    /**
     * Antes de llamar a este metodo se debe llamar loadMenuPrincipal
     */
    fun cargarGameScreenTiled() {
        musicaTiled = get(rutaMusica, Music::class.java)
        musicaTiled!!.isLooping = true
        platMusicInGame()

        val atlas = get(atlasTiledStuff, TextureAtlas::class.java)
        tiledMap = get(rutaTiled, TiledMap::class.java)

        val json = SkeletonJson(atlas)
        json.setScale(.01f)
        ponySkeletonData = json.readSkeletonData(Gdx.files.internal("data/animaciones/personajes.json"))
        json.setScale(.004f)
        skeletonBombData = json.readSkeletonData(Gdx.files.internal("data/animaciones/bombs.json"))
        bombAnim = skeletonBombData!!.findAnimation("b1")
        bombExAnim = skeletonBombData!!.findAnimation("b2x")

        json.setScale(.005f)
        skeletonMonedaData = json.readSkeletonData(Gdx.files.internal("data/animaciones/coin.json"))
        monedaAnim = skeletonMonedaData!!.findAnimation("normal")
        monedaTomadaAnim = skeletonMonedaData!!.findAnimation("plus1")

        json.setScale(.009f)
        chileSkeletonData = json.readSkeletonData(Gdx.files.internal("data/animaciones/chile.json"))
        chileAnim = chileSkeletonData!!.findAnimation("normal")
        chileTomadaAnim = chileSkeletonData!!.findAnimation("toospicy")

        json.setScale(.009f)
        globoSkeletonData = json.readSkeletonData(Gdx.files.internal("data/animaciones/ballons.json"))
        globoAnim = globoSkeletonData!!.findAnimation("normal")
        globoTomadaAnim = globoSkeletonData!!.findAnimation("plus5")

        json.setScale(.009f)
        dulceSkeletonData = json.readSkeletonData(Gdx.files.internal("data/animaciones/chocolate.json"))
        dulceAnim = dulceSkeletonData!!.findAnimation("normal")
        dulceTomadaAnim = dulceSkeletonData!!.findAnimation("speedup")

        medallaPrimerLugar = atlas.findRegion("imagenes/podio/1stplacetrophy")
        medallaSegundoLugar = atlas.findRegion("imagenes/podio/2ndplace")
        medallaTercerLugar = atlas.findRegion("imagenes/podio/3rdplace")
        congratulations = atlas.findRegion("imagenes/podio/congratulations")
        youLose = atlas.findRegion("imagenes/podio/youlose")
        timeUp = atlas.findRegion("imagenes/podio/timeup")

        json.setScale(.01f)
        val fondoSkeletonData = json.readSkeletonData(Gdx.files.internal("data/animaciones/background.json"))
        fondoAnim = fondoSkeletonData.findAnimation("animation")
        fondoSkeleton = Skeleton(fondoSkeletonData)

        fondo = atlas.findRegion("imagenes/fondo")

        padIzq = NinePatchDrawable(NinePatch(atlas.findRegion("Interfaz/pad_izq")))
        padDer = NinePatchDrawable(NinePatch(atlas.findRegion("Interfaz/pad_derecha")))
        btBombaDown = NinePatchDrawable(NinePatch(atlas.findRegion("Interfaz/bombasalpresionar")))
        btBombaUp = NinePatchDrawable(NinePatch(atlas.findRegion("Interfaz/bombasinpresionar")))

        btJumpDown = NinePatchDrawable(NinePatch(atlas.findRegion("Interfaz/saltoalpresionar")))
        btJumpUp = NinePatchDrawable(NinePatch(atlas.findRegion("Interfaz/saltosinpresionar")))

        btTroncoUp = NinePatchDrawable(NinePatch(atlas.findRegion("Interfaz/btPlatanoTachuelas")))
        btTroncoDown = NinePatchDrawable(NinePatch(atlas.findRegion("Interfaz/btPlatanoTachuelasPresionado")))

        btPauseUp = NinePatchDrawable(NinePatch(atlas.findRegion("Interfaz/pause")))

        indicador = atlas.findRegion("Interfaz/indicador")
        indicadorCloud = atlas.findRegion("Interfaz/icono000")
        indicadorCientifico = atlas.findRegion("Interfaz/icono001")
        indicadorMinion = atlas.findRegion("Interfaz/icono002")
        indicadorNatylol = atlas.findRegion("Interfaz/icono003")
        indicadorLighthingAlba = atlas.findRegion("Interfaz/icono004")
        indicadorIgnis = atlas.findRegion("Interfaz/icono005")

        perfilRegionCloud = atlas.findRegion("perfiles/cloud")
        perfilRegionNatylol = atlas.findRegion("perfiles/natylol")
        perfilRegionIgnis = atlas.findRegion("perfiles/ignis")
        perfilRegionCientifico = atlas.findRegion("perfiles/scientist")
        perfilRegionLAlba = atlas.findRegion("perfiles/lightingalba")
        perfilRegionEnemigo = atlas.findRegion("perfiles/enemy")

        lugaresMarco = atlas.findRegion("Interfaz/lugares")

        moneda = atlas.findRegion("moneda")

        tronco = atlas.findRegion("tachuelas")
        tachuelas = atlas.findRegion("tachuelas")
        platano = atlas.findRegion("platano")

        pickCoin = get<Sound?>("data/musica/coin.mp3")
        jump = get<Sound?>("data/musica/salto.mp3")
    }

    fun unloadGameScreenTiled() {
        musicaTiled!!.stop()
        unload(rutaMusica)
        unload(atlasTiledStuff)
        unload(rutaTiled)
        unload("data/musica/coin.mp3")
        unload("data/musica/salto.mp3")
        System.gc()
    }

    fun playMusicMenus() {
        if (Settings.isMusicOn) {
            if (!musicaMenus!!.isPlaying) musicaMenus!!.play()
        } else {
            musicaMenus!!.stop()
        }
    }

    fun platMusicInGame() {
        if (Settings.isMusicOn) {
            if (!musicaTiled!!.isPlaying) musicaTiled!!.play()
        } else {
            musicaTiled!!.stop()
        }
    }

    fun pauseMusic() {
        if (musicaMenus != null) musicaMenus!!.pause()
        if (musicaTiled != null) musicaTiled!!.pause()
    }

    fun playSound(sound: Sound, volumen: Float) {
        if (Settings.isSoundOn) sound.play(volumen)
    }

    fun playSound(sound: Sound) {
        if (Settings.isSoundOn) sound.play(1f)
    }

    companion object {
        var usarPacked: Boolean = true

        @JvmField
        var drawDebugLines: Boolean = false

        @JvmField
        var mundoMaximo: Int = 17
    }
}
