package lain.mods.peacefulsurface.impl.fabric;

import java.lang.ref.WeakReference;
import lain.mods.peacefulsurface.api.interfaces.IWorldObj;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

public class FabricWorldObj implements IWorldObj
{

    private WeakReference<WorldAccess> w;
    private String name;

    public FabricWorldObj(WorldAccess world)
    {
        if (world == null)
            throw new IllegalArgumentException("world must not be null");
        w = new WeakReference<WorldAccess>(world);
        name = world.getWorld().getRegistryKey().getValue().toString();
    }

    @Override
    public int getLightLevel(double x, double y, double z)
    {
        WorldAccess o;
        if ((o = w.get()) == null) // gc
            return 0;
        return o.getWorld().isThundering() ? o.getLightLevel(new BlockPos(x, y, z), 10) : o.getLightLevel(new BlockPos(x, y, z));
    }

    @Override
    public int getMoonPhase()
    {
        WorldAccess o;
        if ((o = w.get()) == null) // gc
            return 0;
        return o.getDimension().method_28531(o.getLevelProperties().getTimeOfDay());
    }

    @Override
    public String getWorldName()
    {
        return name;
    }

    @Override
    public boolean isBloodMoon()
    {
        // not implemented
        return false;
    }

    @Override
    public boolean isDayTime()
    {
        WorldAccess o;
        if ((o = w.get()) == null) // gc
            return false;
        return o.getWorld().isDay();
    }

    @Override
    public boolean isRaining()
    {
        WorldAccess o;
        if ((o = w.get()) == null) // gc
            return false;
        return o.getWorld().isRaining();
    }

    @Override
    public boolean isThundering()
    {
        WorldAccess o;
        if ((o = w.get()) == null) // gc
            return false;
        return o.getWorld().isThundering();
    }

}
