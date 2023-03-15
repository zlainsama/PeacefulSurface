package lain.mods.peacefulsurface.api.interfaces;

public interface IEntitySpawnFilter {

    /**
     * @return if false, this filter will be skipped.
     */
    boolean enabled();

    /**
     * @param entity the entity object that is about to spawn.
     * @param world  the world object which the entity is about to spawn in.
     * @param x      the x coordinates the entity is about to spawn at.
     * @param y      the y coordinates the entity is about to spawn at.
     * @param z      the z coordinates the entity is about to spawn at.
     * @return if true, the entity will be prevented from spawning.
     */
    boolean filterEntity(IEntityObj entity, IWorldObj world, int x, int y, int z);

}
