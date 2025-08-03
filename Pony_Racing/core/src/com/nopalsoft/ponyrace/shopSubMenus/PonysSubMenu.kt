package com.nopalsoft.ponyrace.shopSubMenus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.ponyrace.AssetsHandler
import com.nopalsoft.ponyrace.Settings
import com.nopalsoft.ponyrace.screens.ShopScreen

class PonysSubMenu(shop: ShopScreen, var contenedor: Table) {
    val NOMBRE_SKIN_CLOUD: String = "Cloud"
    val NOMBRE_SKIN_CIENTIFICO: String = "cientifico"
    val NOMBRE_SKIN_ENEMIGO: String = "enemigo"
    val NOMBRE_SKIN_NATYLOL: String = "Natylol"
    val NOMBRE_SKIN_IGNIS: String = "Ignis"
    val NOMBRE_SKIN_LALBA: String = "LAlba"
    val PRECIO_DESBLOQUEAR_PONY: Int = 5000
    val BUY: String = "Buy"
    val USE: String = "Use"
    private val prefName = "com.nopalsoft.ponyRace.ponysSubMenu"
    private val prefAchiv: Preferences = Gdx.app.getPreferences(prefName)
    var oAssetsHandler: AssetsHandler = shop.game!!.assetsHandler!!
    var btUnlockCloud: TextButton? = null
    var btUnlockCientifico: TextButton? = null
    var btUnlockEnemigo: TextButton? = null
    var btUnlockNatylol: TextButton? = null
    var btUnlockIgnis: TextButton? = null
    var btUnlockLalba: TextButton? = null
    var dentroCloud: Table? = null
    var dentroCientifico: Table? = null
    var dentroEnemigo: Table? = null
    var dentroNatylol: Table? = null
    var dentroIgnis: Table? = null
    var dentroLalba: Table? = null
    private var isCientificoUnlocked: Boolean
    private var isEnemigoUnlocked: Boolean
    private var isNatylolUnlocked: Boolean
    private var isIgnisUnlocked: Boolean
    private var isLalbaUnlocked: Boolean

    init {

        isCientificoUnlocked = prefAchiv.getBoolean("isCientificoUnlocked", false)
        isEnemigoUnlocked = prefAchiv.getBoolean("isEnemigoUnlocked", false)
        isNatylolUnlocked = prefAchiv.getBoolean("isNatylolUnlocked", false)
        isIgnisUnlocked = prefAchiv.getBoolean("isIgnisUnlocked", false)
        isLalbaUnlocked = prefAchiv.getBoolean("isLalbaUnlocked", false)

        contenedor.clear()

        inicializarBotones()
        setPonys()
    }

    fun guardar() {
        prefAchiv.putBoolean("isCientificoUnlocked", isCientificoUnlocked)
        prefAchiv.putBoolean("isEnemigoUnlocked", isEnemigoUnlocked)
        prefAchiv.putBoolean("isNatylolUnlocked", isNatylolUnlocked)
        prefAchiv.putBoolean("isIgnisUnlocked", isIgnisUnlocked)
        prefAchiv.putBoolean("isLalbaUnlocked", isLalbaUnlocked)
        prefAchiv.flush()
    }

    private fun inicializarBotones() {
        val btStyle = TextButtonStyle(oAssetsHandler.btNubeUpTienda, oAssetsHandler.btNubeDownTienda, null, oAssetsHandler.fontChco)

        btUnlockCloud = TextButton(USE, btStyle)
        if (Settings.selectedSkin == NOMBRE_SKIN_CLOUD) btUnlockCloud!!.isVisible = false
        btUnlockCloud!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.selectedSkin = NOMBRE_SKIN_CLOUD
                setVisibleButtonsBut(btUnlockCloud!!)
            }
        })

        // Cientifico
        var textBoton = BUY
        if (isCientificoUnlocked) textBoton = USE
        btUnlockCientifico = TextButton(textBoton, btStyle)
        if (Settings.selectedSkin == NOMBRE_SKIN_CIENTIFICO) btUnlockCientifico!!.isVisible = false
        btUnlockCientifico!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (isCientificoUnlocked) {
                    Settings.selectedSkin = NOMBRE_SKIN_CIENTIFICO
                    setVisibleButtonsBut(btUnlockCientifico!!)
                } else if (Settings.numeroMonedasActual - PRECIO_DESBLOQUEAR_PONY > 0) {
                    Settings.numeroMonedasActual -= PRECIO_DESBLOQUEAR_PONY
                    isCientificoUnlocked = true
                    btUnlockCientifico!!.setText(USE)
                    removePrecio(dentroCientifico!!)
                    guardar()
                }
            }
        })

        // Enemigo
        textBoton = BUY
        if (isEnemigoUnlocked) textBoton = USE
        btUnlockEnemigo = TextButton(textBoton, btStyle)
        if (Settings.selectedSkin == NOMBRE_SKIN_ENEMIGO) btUnlockEnemigo!!.isVisible = false
        btUnlockEnemigo!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (isEnemigoUnlocked) {
                    Settings.selectedSkin = NOMBRE_SKIN_ENEMIGO
                    setVisibleButtonsBut(btUnlockEnemigo!!)
                } else if (Settings.numeroMonedasActual - PRECIO_DESBLOQUEAR_PONY > 0) {
                    Settings.numeroMonedasActual -= PRECIO_DESBLOQUEAR_PONY
                    isEnemigoUnlocked = true
                    btUnlockEnemigo!!.setText(USE)
                    removePrecio(dentroEnemigo!!)
                    guardar()
                }
            }
        })

        // Natylol
        textBoton = BUY
        if (isNatylolUnlocked) textBoton = USE
        btUnlockNatylol = TextButton(textBoton, btStyle)
        if (Settings.selectedSkin == NOMBRE_SKIN_NATYLOL) btUnlockNatylol!!.isVisible = false
        btUnlockNatylol!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (isNatylolUnlocked) {
                    Settings.selectedSkin = NOMBRE_SKIN_NATYLOL
                    setVisibleButtonsBut(btUnlockNatylol!!)
                } else if (Settings.numeroMonedasActual - PRECIO_DESBLOQUEAR_PONY > 0) {
                    Settings.numeroMonedasActual -= PRECIO_DESBLOQUEAR_PONY
                    isNatylolUnlocked = true
                    btUnlockNatylol!!.setText(USE)
                    removePrecio(dentroNatylol!!)
                    guardar()
                }
            }
        })

        // Ignis
        textBoton = BUY
        if (isIgnisUnlocked) textBoton = USE
        btUnlockIgnis = TextButton(textBoton, btStyle)
        if (Settings.selectedSkin == NOMBRE_SKIN_IGNIS) btUnlockIgnis!!.isVisible = false
        btUnlockIgnis!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (isIgnisUnlocked) {
                    Settings.selectedSkin = NOMBRE_SKIN_IGNIS
                    setVisibleButtonsBut(btUnlockIgnis!!)
                } else if (Settings.numeroMonedasActual - PRECIO_DESBLOQUEAR_PONY > 0) {
                    Settings.numeroMonedasActual -= PRECIO_DESBLOQUEAR_PONY
                    isIgnisUnlocked = true
                    btUnlockIgnis!!.setText(USE)
                    removePrecio(dentroIgnis!!)
                    guardar()
                }
            }
        })

        // Ignis
        textBoton = BUY
        if (isLalbaUnlocked) textBoton = USE
        btUnlockLalba = TextButton(textBoton, btStyle)
        if (Settings.selectedSkin == NOMBRE_SKIN_LALBA) btUnlockLalba!!.isVisible = false
        btUnlockLalba!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (isLalbaUnlocked) {
                    Settings.selectedSkin = NOMBRE_SKIN_LALBA
                    setVisibleButtonsBut(btUnlockLalba!!)
                } else if (Settings.numeroMonedasActual - PRECIO_DESBLOQUEAR_PONY > 0) {
                    Settings.numeroMonedasActual -= PRECIO_DESBLOQUEAR_PONY
                    isLalbaUnlocked = true
                    btUnlockLalba!!.setText(USE)
                    removePrecio(dentroLalba!!)
                    guardar()
                }
            }
        })
    }

    private fun removePrecio(tableDentro: Table) {
        val ite: MutableIterator<Actor?> = tableDentro.getChildren().iterator()
        while (ite.hasNext()) {
            val obj = ite.next()
            if (obj is Image || obj is Label) ite.remove()
        }
    }

    /**
     * Pone todos los botones en visible y un boton en invisible
     *
     * @param setInvisible el boton que no se va ver
     */
    private fun setVisibleButtonsBut(setInvisible: TextButton) {
        // Voy a iterar a dentro del contenedor, luego dentro de tablaDentro y luego de dentro cloud para poner los botones visibles

        for (actor in contenedor.getChildren()) {
            val tbDentro = actor as Table

            for (dentroTbdentro in tbDentro.getChildren()) {
                if (dentroTbdentro is Table) {
                    for (obj in dentroTbdentro.getChildren()) {
                        if (obj is TextButton) {
                            obj.isVisible = true
                        }
                    }
                }
            }
        }
        setInvisible.isVisible = false
    }

    private fun setPonys() {
        val lblStyle = LabelStyle(oAssetsHandler.fontChco, Color.WHITE)

        // Cloud
        var tbDentro = Table()
        tbDentro.add(Image(oAssetsHandler.perfilCloud)).size(65f, 60f).padLeft(10f).padRight(10f)
        var descripcion = Label("Play using cloud pony", lblStyle)
        descripcion.setWrap(true)
        tbDentro.add(descripcion).expand().fill()
        dentroCloud = Table()
        dentroCloud!!.add<TextButton?>(btUnlockCloud).size(120f, 70f)

        tbDentro.add<Table?>(dentroCloud)
        contenedor.add(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)

        // Cientifico
        tbDentro = Table()
        tbDentro.add(Image(oAssetsHandler.perfilcientifico)).size(65f, 60f).padLeft(10f).padRight(10f)
        descripcion = Label("Play using scientisg pony", lblStyle)
        descripcion.setWrap(true)
        tbDentro.add(descripcion).expandX().fill()
        dentroCientifico = Table()
        if (!isCientificoUnlocked) {
            dentroCientifico!!.add(Image(oAssetsHandler.monedaTienda))
            val precio = Label(PRECIO_DESBLOQUEAR_PONY.toString() + "", lblStyle)
            dentroCientifico!!.add(precio).left()
            dentroCientifico!!.row().colspan(2)
        }
        dentroCientifico!!.add<TextButton?>(btUnlockCientifico).size(120f, 70f)
        tbDentro.add<Table?>(dentroCientifico)
        contenedor.add(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)

        // Enemigo
        tbDentro = Table()
        tbDentro.add(Image(oAssetsHandler.perfilenemigo)).size(65f, 60f).padLeft(10f).padRight(10f)
        descripcion = Label("Play using Enemy pony", lblStyle)
        descripcion.setWrap(true)
        tbDentro.add(descripcion).expandX().fill()
        dentroEnemigo = Table()
        if (!isEnemigoUnlocked) {
            dentroEnemigo!!.add(Image(oAssetsHandler.monedaTienda))
            val precio = Label(PRECIO_DESBLOQUEAR_PONY.toString() + "", lblStyle)
            dentroEnemigo!!.add(precio).left()
            dentroEnemigo!!.row().colspan(2)
        }
        dentroEnemigo!!.add<TextButton?>(btUnlockEnemigo).size(120f, 70f)
        tbDentro.add<Table?>(dentroEnemigo)
        contenedor.add(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)

        // Natylol
        tbDentro = Table()
        tbDentro.add(Image(oAssetsHandler.perfilNatylol)).size(65f, 60f).padLeft(10f).padRight(10f)
        descripcion = Label("Play using Natylol pony", lblStyle)
        descripcion.setWrap(true)
        tbDentro.add(descripcion).expandX().fill()
        dentroNatylol = Table()
        if (!isNatylolUnlocked) {
            dentroNatylol!!.add(Image(oAssetsHandler.monedaTienda))
            val precio = Label(PRECIO_DESBLOQUEAR_PONY.toString() + "", lblStyle)
            dentroNatylol!!.add(precio).left()
            dentroNatylol!!.row().colspan(2)
        }
        dentroNatylol!!.add<TextButton?>(btUnlockNatylol).size(120f, 70f)
        tbDentro.add<Table?>(dentroNatylol)
        contenedor.add(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)

        // Ignis
        tbDentro = Table()
        tbDentro.add(Image(oAssetsHandler.perfilIgnis)).size(65f, 60f).padLeft(10f).padRight(10f)
        descripcion = Label("Play using Ignis pony", lblStyle)
        descripcion.setWrap(true)
        tbDentro.add(descripcion).expandX().fill()
        dentroIgnis = Table()
        if (!isIgnisUnlocked) {
            dentroIgnis!!.add(Image(oAssetsHandler.monedaTienda))
            val precio = Label(PRECIO_DESBLOQUEAR_PONY.toString() + "", lblStyle)
            dentroIgnis!!.add(precio).left()
            dentroIgnis!!.row().colspan(2)
        }
        dentroIgnis!!.add<TextButton?>(btUnlockIgnis).size(120f, 70f)
        tbDentro.add<Table?>(dentroIgnis)
        contenedor.add(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)

        // Lalba
        tbDentro = Table()
        tbDentro.add(Image(oAssetsHandler.perfilLAlba)).size(65f, 60f).padLeft(10f).padRight(10f)
        descripcion = Label("Play using Lalba pony", lblStyle)
        descripcion.setWrap(true)
        tbDentro.add(descripcion).expandX().fill()
        dentroLalba = Table()
        if (!isLalbaUnlocked) {
            dentroLalba!!.add(Image(oAssetsHandler.monedaTienda))
            val precio = Label(PRECIO_DESBLOQUEAR_PONY.toString() + "", lblStyle)
            dentroLalba!!.add(precio).left()
            dentroLalba!!.row().colspan(2)
        }
        dentroLalba!!.add<TextButton?>(btUnlockLalba).size(120f, 70f)
        tbDentro.add<Table?>(dentroLalba)
        contenedor.add(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)
    }
}
