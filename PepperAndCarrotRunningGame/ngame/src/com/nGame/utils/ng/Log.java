package com.nGame.utils.ng;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

/**
 * NG Log wrapper
 */
public class Log {

    /**
     * Print Debug Message
     * @param message the message text
     */
    public static void d(String message){
        log(Application.LOG_DEBUG,message);
    }

    /**
     * Print Debug Message
     * @param message the message text
     */
    public static void i(String message){
        log(Application.LOG_INFO,message);
    }

    /**
     * Print Debug Message
     * @param message the message text
     */
    public static void e(String message){
        log(Application.LOG_ERROR,message);
    }

    /**
     * Prints first log message
     * example output: typemarker[Classname:linenumber|Method]
     * @param logtype
     * @param text
     */
    private static void log(int logtype, String text) {
        try {
            String name;
            String caller;
            int lineNumber;

            int stackdeep = 3;

            if(Gdx.app.getType() == Application.ApplicationType.Android) {
                stackdeep+=1;
            }

            name = Thread.currentThread().getStackTrace()[stackdeep].getClassName();
            caller = Thread.currentThread().getStackTrace()[stackdeep].getMethodName();
            lineNumber = Thread.currentThread().getStackTrace()[stackdeep].getLineNumber();

            switch (logtype){
                case Application.LOG_DEBUG:
                    Gdx.app.debug("D[" + name + ":" + lineNumber + "|" + caller + "()]", text);
                    break;
                case Application.LOG_ERROR:
                    Gdx.app.error("E[" + name + ":" + lineNumber + "|" + caller + "()]", text);
                    break;
                case Application.LOG_INFO:
                    Gdx.app.log("I[" + name + ":" + lineNumber + "|" + caller + "()]", text);
                    break;
            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
