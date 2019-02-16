package lain.mods.peacefulsurface.api;

import java.util.ArrayList;
import java.util.List;
import lain.mods.peacefulsurface.api.interfaces.IEntityObj;
import lain.mods.peacefulsurface.api.interfaces.IEntitySpawnFilter;
import lain.mods.peacefulsurface.api.interfaces.IWorldObj;

public class PeaceAPI
{

    private static final List<IEntitySpawnFilter> filters = new ArrayList<IEntitySpawnFilter>();
    private static final List<Runnable> listeners = new ArrayList<Runnable>();

    /**
     * @param filter the filter to add.
     * @return true if successful.
     */
    public static boolean addFilter(IEntitySpawnFilter filter)
    {
        return filters.add(filter);
    }

    /**
     * clears all filters.
     */
    public static void clearFilters()
    {
        filters.clear();
    }

    /**
     * clear all reload listeners.
     */
    public static void clearReloadListeners()
    {
        listeners.clear();
    }

    /**
     * @return the size of filters.
     */
    public static int countFilters()
    {
        return filters.size();
    }

    /**
     * @param entity the entity object that is about to spawn.
     * @param world  the world object which the entity is about to spawn in.
     * @param x      the x coordinates the entity is about to spawn at.
     * @param y      the y coordinates the entity is about to spawn at.
     * @param z      the z coordinates the entity is about to spawn at.
     * @return true if any filters returned true.
     */
    public static boolean filterEntity(IEntityObj entity, IWorldObj world, double x, double y, double z)
    {
        return filters.stream().filter(IEntitySpawnFilter::enabled).anyMatch(filter -> {
            return filter.filterEntity(entity, world, x, y, z);
        });
    }

    /**
     * notify all reload listeners.
     */
    public static void notifyReloadListeners()
    {
        listeners.forEach(Runnable::run);
    }

    /**
     * @param listener the listener to register.
     * @return true if successful.
     */
    public static boolean registerReloadListener(Runnable listener)
    {
        return listeners.add(listener);
    }

    /**
     * @param filter the filter to remove.
     * @return true if successful.
     */
    public static boolean removeFilter(IEntitySpawnFilter filter)
    {
        return filters.remove(filter);
    }

    /**
     * @param listener the listener to unregister.
     * @return true if successful.
     */
    public static boolean unregisterReloadListener(Runnable listener)
    {
        return listeners.remove(listener);
    }

}
