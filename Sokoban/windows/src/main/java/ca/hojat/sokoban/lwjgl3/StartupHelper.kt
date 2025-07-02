package ca.hojat.sokoban.lwjgl3

import com.badlogic.gdx.Version
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3NativesLoader
import org.lwjgl.system.JNI
import org.lwjgl.system.macosx.LibC
import org.lwjgl.system.macosx.ObjCRuntime
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.management.ManagementFactory
import java.util.Locale

class StartupHelper private constructor() {
    init {
        throw UnsupportedOperationException()
    }

    companion object {
        private const val JVM_RESTARTED_ARG = "jvmIsRestarted"

        @JvmOverloads
        fun startNewJvmIfRequired(redirectOutput: Boolean = true): Boolean {
            val osName = System.getProperty("os.name").lowercase(Locale.getDefault())
            if (!osName.contains("mac")) {
                if (osName.contains("windows")) {
// Here, we are trying to work around an issue with how LWJGL3 loads its extracted .dll files.
// By default, LWJGL3 extracts to the directory specified by "java.io.tmpdir", which is usually the user's home.
// If the user's name has non-ASCII (or some non-alphanumeric) characters in it, that would fail.
// By extracting to the relevant "ProgramData" folder, which is usually "C:\ProgramData", we avoid this.
// We also temporarily change the "user.name" property to one without any chars that would be invalid.
// We revert our changes immediately after loading LWJGL3 natives.
                    var programData = System.getenv("ProgramData")
                    if (programData == null) programData = "C:\\Temp\\" // if ProgramData isn't set, try some fallback.

                    val prevTmpDir = System.getProperty("java.io.tmpdir", programData)
                    val prevUser = System.getProperty("user.name", "libGDX_User")
                    System.setProperty("java.io.tmpdir", "$programData/libGDX-temp")
                    System.setProperty("user.name", ("User_" + prevUser.hashCode() + "_GDX" + Version.VERSION).replace('.', '_'))
                    Lwjgl3NativesLoader.load()
                    System.setProperty("java.io.tmpdir", prevTmpDir)
                    System.setProperty("user.name", prevUser)
                }
                return false
            }


            if (System.getProperty("org.graalvm.nativeimage.imagecode", "").isNotEmpty()) {
                return false
            }

            val objc_msgSend = ObjCRuntime.getLibrary().getFunctionAddress("objc_msgSend")
            val NSThread = ObjCRuntime.objc_getClass("NSThread")
            val currentThread = JNI.invokePPP(NSThread, ObjCRuntime.sel_getUid("currentThread"), objc_msgSend)
            val isMainThread = JNI.invokePPZ(currentThread, ObjCRuntime.sel_getUid("isMainThread"), objc_msgSend)
            if (isMainThread) return false

            val pid = LibC.getpid()


            if ("1" == System.getenv("JAVA_STARTED_ON_FIRST_THREAD_$pid")) {
                return false
            }

            if ("true" == System.getProperty(JVM_RESTARTED_ARG)) {
                System.err.println(
                    "There was a problem evaluating whether the JVM was started with the -XstartOnFirstThread argument."
                )
                return false
            }


            val jvmArgs = ArrayList<String?>()
            val separator = System.getProperty("file.separator", "/")

            val javaExecPath = System.getProperty("java.home") + separator + "bin" + separator + "java"


            if (!(File(javaExecPath)).exists()) {
                System.err.println(
                    "A Java installation could not be found. If you are distributing this app with a bundled JRE, be sure to set the -XstartOnFirstThread argument manually!"
                )
                return false
            }

            jvmArgs.add(javaExecPath)
            jvmArgs.add("-XstartOnFirstThread")
            jvmArgs.add("-D$JVM_RESTARTED_ARG=true")
            jvmArgs.addAll(ManagementFactory.getRuntimeMXBean().inputArguments)
            jvmArgs.add("-cp")
            jvmArgs.add(System.getProperty("java.class.path"))
            var mainClass = System.getenv("JAVA_MAIN_CLASS_$pid")
            if (mainClass == null) {
                val trace = Thread.currentThread().stackTrace
                if (trace.isNotEmpty()) {
                    mainClass = trace[trace.size - 1].className
                } else {
                    System.err.println("The main class could not be determined.")
                    return false
                }
            }
            jvmArgs.add(mainClass)

            try {
                if (!redirectOutput) {
                    val processBuilder = ProcessBuilder(jvmArgs)
                    processBuilder.start()
                } else {
                    val process = (ProcessBuilder(jvmArgs))
                        .redirectErrorStream(true).start()
                    val processOutput = BufferedReader(
                        InputStreamReader(process.inputStream)
                    )
                    var line: String?

                    while ((processOutput.readLine().also { line = it }) != null) {
                        println(line)
                    }

                    process.waitFor()
                }
            } catch (e: Exception) {
                System.err.println("There was a problem restarting the JVM")
                e.printStackTrace()
            }

            return true
        }
    }
}