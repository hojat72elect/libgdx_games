package com.bitfire.uracer.utils

import com.bitfire.uracer.configuration.BootConfig
import com.bitfire.uracer.configuration.BootConfig.BootConfigFlag
import gnu.getopt.Getopt
import gnu.getopt.LongOpt

object CommandLine {

    /**
     * Checks if a string is only containing integer (I guess kotlin has a utility for doing this more easily).
     */
    private fun isInt(value: String?): Boolean {
        if (value != null) {
            val len = value.length
            for (i in 0..<len) {
                if (!Character.isDigit(value[i])) {
                    return false
                }
            }
            return true
        }
        return false
    }

    fun applyLaunchFlags(argv: Array<String>, boot: BootConfig): Boolean {
        var c: Int
        var arg: String

        val opts = arrayOf(
            LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'.code),
            LongOpt("resolution", LongOpt.REQUIRED_ARGUMENT, null, 'r'.code),
            LongOpt("enable-vsync", LongOpt.NO_ARGUMENT, null, 'V'.code),
            LongOpt("disable-vsync", LongOpt.NO_ARGUMENT, null, 'v'.code),
            LongOpt("enable-fullscreen", LongOpt.NO_ARGUMENT, null, 'F'.code),
            LongOpt("disable-fullscreen", LongOpt.NO_ARGUMENT, null, 'f'.code),
            LongOpt("enable-undecorated", LongOpt.NO_ARGUMENT, null, 'U'.code),
            LongOpt("disable-undecorated", LongOpt.NO_ARGUMENT, null, 'u'.code),
        )

        val g = Getopt("URacer", argv, "", opts)
        g.setOpterr(false)
        while ((g.getopt().also { c = it }) != -1) {
            arg = g.getOptarg()

            when (c.toChar()) {
                'r' -> {
                    val res: Array<String> = arg.split("x".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    var w = 0
                    var h = 0

                    if (res.size == 2 && isInt(res[0]) && isInt(res[1])) {
                        w = res[0].toInt()
                        h = res[1].toInt()
                    } else {
                        when (arg) {
                            "low" -> {
                                w = 800
                                h = 480
                            }

                            "mid" -> {
                                w = 1280
                                h = 800
                            }

                            "high" -> {
                                w = 1920
                                h = 1080
                            }

                            else -> println("Invalid resolution specified ($arg)")
                        }
                    }

                    if (w > 0 && h > 0) {
                        boot.setInt(BootConfigFlag.WIDTH, w)
                        boot.setInt(BootConfigFlag.HEIGHT, h)

                        // automatically compute the default x/y window position (centered) if not present already
                        if (boot.getWindowX() == -1) {
                            boot.windowX = getCenteredXOnDisplay(w)
                        }

                        if (boot.getWindowY() == -1) {
                            boot.windowY = getCenteredYOnDisplay(h)
                        }
                    }
                }

                'h' -> {
                    println("Valid command-line options:")
                    println("  --help\t\tshows this help")
                    println("  --resolution=RES\tspecify the resolution to use: you can either specify")
                    println("  \t\t\ta real resolution, e.g. --resolution=800x600, or use ")
                    println("  \t\t\ta built-in shortcut (one of \"low\", \"mid\" or \"high\").")
                    println("  \t\t\t(low=800x480, mid=1280x800, high=1920x1080)")
                    println("  --enable-vsync, --disable-vsync\t\tenable/disable vertical sync")
                    println("  --enable-fullscreen, --disable-fullscreen\tenable/disable fullscreen")
                    println("  --enable-undecorated, --disable-undecorated\twhether or not to create a window without the window manager's decorations")
                    println()
                    return false
                }

                'V' -> boot.setBoolean(BootConfigFlag.VSYNC, true)
                'v' -> boot.setBoolean(BootConfigFlag.VSYNC, false)
                'F' -> boot.setBoolean(BootConfigFlag.FULLSCREEN, true)
                'f' -> boot.setBoolean(BootConfigFlag.FULLSCREEN, false)
                'U' -> boot.setBoolean(BootConfigFlag.UNDECORATED, true)
                'u' -> boot.setBoolean(BootConfigFlag.UNDECORATED, false)
                '?' -> {
                    print("The specified parameter is not valid.\nTry --help for a list of valid parameters.")
                    return false
                }

                ':' -> {
                    print("The specified argument is missing some values.\nTry --help for more information.")
                    return false
                }

                else -> {
                    print("getopt() returned " + c + " (" + c.toChar() + ")\n")
                    return false
                }
            }
        }

        return true
    }
}
