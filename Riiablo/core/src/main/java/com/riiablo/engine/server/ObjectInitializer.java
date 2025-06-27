package com.riiablo.engine.server;

import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.badlogic.gdx.Gdx;
import com.riiablo.codec.excel.Objects;
import com.riiablo.engine.Engine;
import com.riiablo.engine.server.component.Classname;
import com.riiablo.engine.server.component.Object;

@All(Object.class)
public class ObjectInitializer extends BaseEntitySystem {
  private static final String TAG = "ObjectInitializer";

  protected ComponentMapper<Object> mObject;
  protected ComponentMapper<Classname> mClassname;

  protected CofManager cofs;

  @Override
  protected void inserted(int entityId) {
    initialize(entityId);
  }

  @Override
  protected void processSystem() {}

  public void initialize(int entityId) {
    Objects.Entry base = mObject.get(entityId).base;
    switch (base.InitFn) {
      case 0:
        break;
      case 1: case 2: case 3: case 4: case 5: case 6: case 7:
        break;
      case 8: // torch
        cofs.setMode(entityId, Engine.Object.MODE_ON);

        // FIXME: Set random start frame?
        //int framesPerDir = animation.getNumFramesPerDir();
        //animation.setFrame(MathUtils.random(0, framesPerDir - 1));
        break;
      case 9 : case 10: case 11: case 12: case 13: case 14: case 15: case 16:
        break;
      case 17: // waypoint
        // TODO: Set ON based on save file
        cofs.setMode(entityId, Engine.Object.MODE_ON);
        break;
      case 18:
      case 19: case 20: case 21: case 22: case 23: case 24: case 25: case 26: case 27: case 28:
      case 29: case 30: case 31: case 32: case 33: case 34: case 35: case 36: case 37: case 38:
      case 39: case 40: case 41: case 42: case 43: case 44: case 45: case 46: case 47: case 48:
      case 49: case 50: case 51: case 52: case 53: case 54: case 55: case 56: case 57: case 58:
      case 59: case 60: case 61: case 62: case 63: case 64: case 65: case 66: case 67: case 68:
      case 69: case 70: case 71: case 72: case 73: case 74: case 75: case 76: case 77: case 78:
      case 79:
        break;
      default:
        Gdx.app.error(TAG, "Invalid InitFn for " + mClassname.get(entityId).classname + ": " + base.InitFn);
    }
  }
}
