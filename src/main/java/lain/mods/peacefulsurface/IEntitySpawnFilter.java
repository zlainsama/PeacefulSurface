package lain.mods.peacefulsurface;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface IEntitySpawnFilter
{

    /**
     * @return return false to skip this filter
     */
    boolean enabled();

    /**
     * @param entity
     * @param world
     * @param x
     * @param y
     * @param z
     * @return return true to stop the entity from spawning
     */
    boolean filterEntity(Entity entity, World world, float x, float y, float z);

}
