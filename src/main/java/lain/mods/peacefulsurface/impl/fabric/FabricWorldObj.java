package lain.mods.peacefulsurface.impl.fabric;

import java.lang.ref.WeakReference;
import lain.mods.peacefulsurface.api.interfaces.IWorldObj;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.WorldView;
import net.minecraft.world.dimension.DimensionType;

public class FabricWorldObj implements IWorldObj
{

    private WeakReference<WorldView> w;
    private int id;
    private String name;

    public FabricWorldObj(WorldView world)
    {
        if (world == null)
            throw new IllegalArgumentException("world must not be null");
        w = new WeakReference<WorldView>(world);
        id = world.getDimension().getType().getRawId();
        name = DimensionType.getId(world.getDimension().getType()).toString();
    }

    @Override
    public int getLightLevel(double x, double y, double z)
    {
        WorldView o;
        if ((o = w.get()) == null) // gc
            return 0;
        return ((IWorld) o).getWorld().isThundering() ? o.getLightLevel(new BlockPos(x, y, z), 10) : o.getLightLevel(new BlockPos(x, y, z));
    }

    @Override
    public int getMoonPhase()
    {
        WorldView o;
        if ((o = w.get()) == null) // gc
            return 0;
        return o.getDimension().getMoonPhase(((IWorld) o).getWorld().getTimeOfDay());
    }

    @Override
    public int getWorldID()
    {
        return id;
    }

    @Override
    public String getWorldName()
    {
        return name;
    }

    @Override
    public boolean isBloodMoon()
    {
        // no implementation in 1.14
        return false;
    }

    @Override
    public boolean isDayTime()
    {
        WorldView o;
        if ((o = w.get()) == null) // gc
            return false;
        return ((IWorld) o).getWorld().isDay();
    }

    @Override
    public boolean isRaining()
    {
        WorldView o;
        if ((o = w.get()) == null) // gc
            return false;
        return ((IWorld) o).getWorld().isRaining();
    }

    @Override
    public boolean isThundering()
    {
        WorldView o;
        if ((o = w.get()) == null) // gc
            return false;
        return ((IWorld) o).getWorld().isThundering();
    }

}
