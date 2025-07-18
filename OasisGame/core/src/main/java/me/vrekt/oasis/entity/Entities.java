package me.vrekt.oasis.entity;

import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

import me.vrekt.oasis.OasisGame;
import me.vrekt.oasis.entity.enemy.EntityEnemy;
import me.vrekt.oasis.entity.enemy.easy.roach.GrungyRoachEnemy;
import me.vrekt.oasis.entity.interactable.EntityInteractable;
import me.vrekt.oasis.entity.npc.chick.ChickEntity;
import me.vrekt.oasis.entity.npc.tutorial.BasicFishEntity;
import me.vrekt.oasis.entity.npc.tutorial.LyraEntity;
import me.vrekt.oasis.entity.npc.tutorial.MiraEntity;
import me.vrekt.oasis.entity.npc.tutorial.ThaliaEntity;
import me.vrekt.oasis.entity.npc.tutorial.WrynnEntity;
import me.vrekt.oasis.world.GameWorld;

/**
 * All entities within the game
 */
public final class Entities {

    private static final Map<String, EntityProvider> ENTITIES = new HashMap<>();

    public static void init() {
        ENTITIES.put(WrynnEntity.ENTITY_KEY, WrynnEntity::new);
        ENTITIES.put(ThaliaEntity.ENTITY_KEY, ThaliaEntity::new);
        ENTITIES.put(LyraEntity.ENTITY_KEY, LyraEntity::new);
        ENTITIES.put(MiraEntity.ENTITY_KEY, MiraEntity::new);
        ENTITIES.put(BasicFishEntity.ENTITY_KEY, BasicFishEntity::new);
        ENTITIES.put(GrungyRoachEnemy.ENTITY_KEY, GrungyRoachEnemy::new);
        ENTITIES.put(ChickEntity.ENTITY_KEY, ChickEntity::new);
    }

    /**
     * Create an interactable entity
     *
     * @param key      key
     * @param world    world
     * @param position position at
     * @param game     game
     * @return the entity
     */
    public static EntityInteractable interactable(String key, GameWorld world, Vector2 position, OasisGame game) {
        return (EntityInteractable) ENTITIES.get(key).create(world, position, game);
    }

    /**
     * Create an enemy
     *
     * @param key      key
     * @param world    world
     * @param position position at
     * @param game     game
     * @return the entity
     */
    public static EntityEnemy enemy(String key, GameWorld world, Vector2 position, OasisGame game) {
        return (EntityEnemy) ENTITIES.get(key).create(world, position, game);
    }

    /**
     * Create a generic/basic entity
     *
     * @param key      key
     * @param world    world
     * @param position position at
     * @param game     game
     * @return the entity
     */
    public static GameEntity generic(String key, GameWorld world, Vector2 position, OasisGame game) {
        return ENTITIES.get(key).create(world, position, game);
    }


    /**
     * Basic provider
     */
    public interface EntityProvider {
        GameEntity create(GameWorld world, Vector2 position, OasisGame game);
    }
}


