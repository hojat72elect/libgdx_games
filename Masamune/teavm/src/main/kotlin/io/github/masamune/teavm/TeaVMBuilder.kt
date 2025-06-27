package io.github.masamune.teavm

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.github.xpenatan.gdx.backends.teavm.config.AssetFileHandle
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuildConfiguration
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuilder
import com.github.xpenatan.gdx.backends.teavm.config.plugins.TeaReflectionSupplier
import com.github.xpenatan.gdx.backends.teavm.gen.SkipClass
import com.rafaskoberg.gdx.typinglabel.TypingGlyph
import com.rafaskoberg.gdx.typinglabel.effects.BlinkEffect
import com.rafaskoberg.gdx.typinglabel.effects.EaseEffect
import com.rafaskoberg.gdx.typinglabel.effects.FadeEffect
import com.rafaskoberg.gdx.typinglabel.effects.JumpEffect
import com.rafaskoberg.gdx.typinglabel.effects.RainbowEffect
import com.rafaskoberg.gdx.typinglabel.effects.ShakeEffect
import com.ray3k.tenpatch.TenPatchDrawable
import io.github.masamune.ui.view.DialogViewStyle
import io.github.masamune.ui.widget.DialogOptionStyle
import org.teavm.tooling.TeaVMTargetType
import org.teavm.vm.TeaVMOptimizationLevel
import java.io.File

/** Builds the TeaVM/HTML application. */
@SkipClass
object TeaVMBuilder {
    @JvmStatic
    fun main(arguments: Array<String>) {
        val teaBuildConfiguration = TeaBuildConfiguration().apply {
            assetsPath.add(AssetFileHandle("../assets"))
            webappPath = File("build/dist").canonicalPath
            // Register any extra classpath assets here:
            // additionalAssetsClasspathFiles += "io/github/masamune/asset.extension"
            htmlTitle = "Masamune"
            htmlWidth = 960
            htmlHeight = 540
        }

        // Register any classes or packages that require reflection here:
        // Scene2D style classes require reflection
        TeaReflectionSupplier.addReflectionClass(DialogViewStyle::class.java)
        TeaReflectionSupplier.addReflectionClass(DialogOptionStyle::class.java)
        TeaReflectionSupplier.addReflectionClass(TenPatchDrawable::class.java)
        // Typing label effects require reflection
        TeaReflectionSupplier.addReflectionClass(TypingGlyph::class.java)
        TeaReflectionSupplier.addReflectionClass(FadeEffect::class.java)
        TeaReflectionSupplier.addReflectionClass(JumpEffect::class.java)
        TeaReflectionSupplier.addReflectionClass(RainbowEffect::class.java)
        TeaReflectionSupplier.addReflectionClass(EaseEffect::class.java)
        TeaReflectionSupplier.addReflectionClass(BlinkEffect::class.java)
        TeaReflectionSupplier.addReflectionClass(ShakeEffect::class.java)
        // free type font
        TeaReflectionSupplier.addReflectionClass(FreeTypeFontGenerator::class.java)

        val tool = TeaBuilder.config(teaBuildConfiguration)
        tool.mainClass = "io.github.masamune.teavm.TeaVMLauncher"
        // For many (or most) applications, using the highest optimization won't add much to build time.
        // If your builds take too long, and runtime performance doesn't matter, you can change FULL to SIMPLE .
        tool.optimizationLevel = TeaVMOptimizationLevel.ADVANCED
        tool.setObfuscated(true)
        tool.targetType = TeaVMTargetType.WEBASSEMBLY_GC
        TeaBuilder.build(tool)
    }
}
