package com.riiablo.engine.client;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.annotations.Wire;
import com.artemis.utils.IntBag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;

import com.riiablo.Riiablo;
import com.riiablo.camera.IsometricCamera;
import com.riiablo.engine.Engine;
import com.riiablo.engine.client.component.Hovered;
import com.riiablo.engine.server.Actioneer;
import com.riiablo.engine.server.component.Interactable;
import com.riiablo.engine.server.component.Position;
import com.riiablo.engine.server.component.Target;
import com.riiablo.item.Item;
import com.riiablo.logger.LogManager;
import com.riiablo.logger.Logger;
import com.riiablo.map.Map;
import com.riiablo.map.RenderSystem;
import com.riiablo.profiler.ProfilerSystem;
import com.riiablo.save.ItemController;

public class CursorMovementSystem extends BaseSystem {
  private static final Logger log = LogManager.getLogger(CursorMovementSystem.class);

  protected ComponentMapper<Target> mTarget;
  protected ComponentMapper<Position> mPosition;
  protected ComponentMapper<Interactable> mInteractable;

  protected RenderSystem renderer;
  protected MenuManager menuManager;
  protected DialogManager dialogManager;
  protected ProfilerSystem profiler;
  protected Actioneer actioneer;

  @Wire(name = "iso")
  protected IsometricCamera iso;

  @Wire(name = "map")
  protected Map map;

  @Wire(name = "stage")
  protected Stage stage;

  @Wire(name = "scaledStage")
  protected Stage scaledStage;

  @Wire(name = "itemController")
  protected ItemController itemController;

  EntitySubscription hoveredSubscriber;
  boolean requireRelease;

  private final Vector2 tmpVec2 = new Vector2();

  @Override
  protected void initialize() {
    hoveredSubscriber = world.getAspectSubscriptionManager().get(Aspect.all(Hovered.class));
  }

  @Override
  protected void processSystem() {
    if (profiler != null && profiler.hit()) return;
    stage.screenToStageCoordinates(tmpVec2.set(Gdx.input.getX(), Gdx.input.getY()));
    Actor hit1 = stage.hit(tmpVec2.x, tmpVec2.y, true);
    scaledStage.screenToStageCoordinates(tmpVec2.set(Gdx.input.getX(), Gdx.input.getY()));
    Actor hit2 = scaledStage.hit(tmpVec2.x, tmpVec2.y, true);
    boolean hit = hit1 != null || hit2 != null;
    if (hit) return;

    final boolean leftPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
    if ((leftPressed && UIUtils.shift()) || Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
      final int playerId = renderer.getSrc();
      final int skillId = Riiablo.charData.getAction(leftPressed ? Input.Buttons.LEFT : Input.Buttons.RIGHT);
      iso.agg(tmpVec2.set(Gdx.input.getX(), Gdx.input.getY())).unproject().toWorld();
      final int targetId = getHovered(playerId);
      actioneer.cast(playerId, skillId, targetId, tmpVec2);
    } else {
      updateLeft();
    }
  }

  private void updateLeft() {
    int src = renderer.getSrc();
    boolean pressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
    if (pressed && !requireRelease) {
      Item cursor = Riiablo.cursor.getItem();
      if (cursor != null) {
        itemController.cursorToGround();
        requireRelease = true;
        return;
      }

      // exiting dialog should block all input until button is released to prevent menu from closing the following frame
      if (dialogManager.getDialog() != null) {
        dialogManager.setDialog(null);
        requireRelease = true;
        return;
      } else if (menuManager.getMenu() != null) {
        menuManager.setMenu(null, Engine.INVALID_ENTITY);
      }

      // set target entity -- unsets and interacts when within range
      boolean touched = touchDown(src);
      if (!touched && actioneer.canInterrupt(src)) {
        iso.agg(tmpVec2.set(Gdx.input.getX(), Gdx.input.getY())).unproject().toWorld();
        actioneer.moveTo(src, tmpVec2);
      }
    } else if (!pressed && actioneer.canInterrupt(src)) {
      //pathfinder.findPath(src, null);
      requireRelease = false;
      Target target = mTarget.get(src);
      if (target != null) {
        int targetId = target.target;
        Vector2 srcPos = mPosition.get(src).position;
        if (mPosition.get(targetId) == null) {
          actioneer.moveTo(src, Engine.INVALID_ENTITY);
          return;
        }
        Vector2 targetPos = mPosition.get(targetId).position;
        // not interactable -> attacking? check weapon range to auto attack or cast spell
        Interactable interactable = mInteractable.get(targetId);
        final float dst = srcPos.dst(targetPos);
        if (interactable != null && dst <= interactable.range) {
          actioneer.moveTo(src, Engine.INVALID_ENTITY);
          interactable.interactor.interact(src, targetId);
        } else if (interactable == null && dst <= 3) { // TODO: change to check targetability of targetId
          actioneer.cast(src, Riiablo.charData.getAction(Input.Buttons.LEFT), targetId, targetPos);
        }
      }
    }
  }

  private int getHovered(int src) {
    IntBag hoveredEntities = hoveredSubscriber.getEntities();
    if (hoveredEntities.size() == 0) return Engine.INVALID_ENTITY;
    return hoveredEntities.get(0);
  }

  private boolean touchDown(int src) {
    int target = getHovered(src);
    if (target == Engine.INVALID_ENTITY) return false;
    actioneer.moveTo(src, target);
    return true;
  }
}
