package ca.hojat.sokoban.teavm;

import com.github.xpenatan.gdx.backends.teavm.config.AssetFileHandle;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuildConfiguration;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuilder;

import org.teavm.tooling.TeaVMTool;
import org.teavm.vm.TeaVMOptimizationLevel;

import java.io.File;
import java.io.IOException;

/** Builds the TeaVM/HTML application. */
public class TeaVMBuilder {
    public static void main(String[] args) throws IOException {
        TeaBuildConfiguration teaBuildConfiguration = new TeaBuildConfiguration();
        teaBuildConfiguration.assetsPath.add(new AssetFileHandle("../assets"));
        teaBuildConfiguration.webappPath = new File("build/dist").getCanonicalPath();

        // Register any extra classpath assets here:
        // teaBuildConfiguration.additionalAssetsClasspathFiles.add("ca/hojat/sokoban/asset.extension");

        // Register any classes or packages that require reflection here:
        // TeaReflectionSupplier.addReflectionClass("ca.hojat.sokoban.reflect");

        TeaVMTool tool = TeaBuilder.config(teaBuildConfiguration);
        // You can uncomment the line below to use WASM instead of JavaScript as a target.
        // Some code can see very significant performance benefits from WASM, and some won't.
//        tool.setTargetType(TeaVMTargetType.WEBASSEMBLY_GC);
        tool.setMainClass(TeaVMLauncher.class.getName());
        // For many (or most) applications, using a high optimization won't add much to build time.
        // If your builds take too long, and runtime performance doesn't matter, you can change ADVANCED to SIMPLE .
        tool.setOptimizationLevel(TeaVMOptimizationLevel.ADVANCED);
        // The line below should use tool.setObfuscated(false) if you want clear debugging info.
        // You can change it to tool.setObfuscated(true) when you are preparing to release, to try to hide your original code.
        tool.setObfuscated(false);
        TeaBuilder.build(tool);
    }
}
