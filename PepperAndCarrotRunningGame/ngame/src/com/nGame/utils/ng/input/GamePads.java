package com.nGame.utils.ng.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;
import com.nGame.utils.ng.Log;

import java.util.ArrayList;

/**
 * Created by n0iz on 01.02.2015.
 */
public class GamePads {
    public static GamePads I;

    static String TAG = "GamePads";
    public enum GamePadType{
        XBOX360,
        PS3,
        USB
    }

    int gamepadCount=0;

    public class GamePad {
        public int padID;
        public GamePadType padType;
        public Controller controller;
        public GamePad(Controller controller, int id, GamePads.GamePadType padType){
            super();
            this.controller=controller;
            this.padID=id;
            this.padType=padType;
        }
    }

    ArrayList<GamePad> controllerMap;



   public GamePads(){
       I=this;
       init();
   }

    public void init (){
        controllerMap = new ArrayList<GamePad>();
        Array<Controller> controllers = Controllers.getControllers();
        gamepadCount = controllers.size;
        for (int i = 0; i< gamepadCount; i++){
            Controller ctrl = controllers.get(i);
            Log.d("Found Controller: " + ctrl);
            String toCheck = ctrl.getName().toLowerCase();
            if(toCheck.contains("xbox") &&  toCheck.contains("360")){
                Log.i("XBox360 pad detected");
                controllerMap.add(new GamePad(ctrl, i, GamePadType.XBOX360));
            } else {
                Log.i("unknown pad detected. use default usb mapping...");
                controllerMap.add(new GamePad(ctrl, i, GamePadType.USB));
            }
        }
    }

    public boolean existID(int gamePadID){
        return gamePadID>=0&&gamePadID<gamepadCount;
    }
    public int getGamePadID(Controller controller) throws Exception {
        GamePad pad = getGamePad(controller);
        if (pad!=null) {
            return pad.padID;
        } else {
            Log.e("controller unknown, use id 0");
            return 0;
            //throw new Exception("wrong Controller: " + controller.getName());
        }
    }

    public GamePad getGamePad(int gamePadID){
        if(existID(gamePadID)){
            return controllerMap.get(gamePadID);
        }
        Log.e("no GamePad instance for this id:" + gamePadID + "use id 0");
        return controllerMap.get(0);
    }

    public GamePad getGamePad(Controller controller){
        for (GamePad pad: controllerMap){
            if(pad.controller.equals(controller)){
                return pad;
            } /*else {
                Log.d("gamepad not found :" + pad.controller.getName() + " != "+ controller.getName());
            }*/
        }
        Log.e("Controller has no GamePad instance. use id 0");
        return controllerMap.get(0);
    }

    private String getControllerIdentifier(Controller controller){
        GamePad pad = getGamePad(controller);
        return pad.padID+"|"+pad.padType+"|"+controller.getName();
    }

    public void addAdapter(ControllerAdapter controlleradapter) {
        addAdapter(0,controlleradapter);
    }

    /**
     * Add an implemented {@link ControllerAdapter} to first specific GamePad
     * @param gamePadID
     * @param controlleradapter
     */
    public void addAdapter(int gamePadID, ControllerAdapter controlleradapter) {
        if(!existID(gamePadID)){
            Log.e("GamePadId does not exist:" + gamePadID);
        }
        Controllers.getControllers().get(gamePadID).addListener(controlleradapter);
    }
}
