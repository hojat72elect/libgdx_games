package com.nopalsoft.zombiekiller.scene2d

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.zombiekiller.Assets

class SkullBar(x: Float, y: Float) : Table() {
    var numOfSkulls: Int = 0

    var arrSkulls: Array<Image?>

    init {
        this.setBounds(x - WIDTH / 2f, y, WIDTH, HEIGHT)
        setBackground(Assets.skullBarBackground)
        arrSkulls = arrayOfNulls<Image>(3)

        for (i in arrSkulls.indices) {
            arrSkulls[i] = Image()
            add<Image?>(arrSkulls[i]).size(40f).pad(6f)
        }
    }

    fun tryToUpdateSkulls(actualNumSkulls: Int) {
        if (numOfSkulls < actualNumSkulls) {
            numOfSkulls = actualNumSkulls
            for (i in 0..<numOfSkulls) {
                arrSkulls[i]!!.setDrawable(TextureRegionDrawable(Assets.itemSkull))
            }
        }
    }

    companion object {
        const val WIDTH: Float = 180f
        const val HEIGHT: Float = 60f
    }
}
