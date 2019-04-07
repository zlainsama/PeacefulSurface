package lain.mods.peacefulsurface.impl.fabric;

import java.lang.ref.WeakReference;
import lain.mods.peacefulsurface.api.interfaces.IWorldObj;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class FabricWorldObj implements IWorldObj
{

    private WeakReference<ViewableWorld> w;
    private int id;
    private String name;

    public FabricWorldObj(ViewableWorld world)
    {
        if (world == null)
            throw new IllegalArgumentException("world must not be null");
        w = new WeakReference<ViewableWorld>(world);
        id = world.getDimension().getType().getRawId();
        name = DimensionType.getId(world.getDimension().getType()).toString();
    }

    @Override
    public int getLightLevel(double x, double y, double z)
    {
        if (w.get() == null) // gc
            return 0;
        return w.get().getLightLevel(LightType.SKY, new BlockPos(x, y, z));
    }

    @Override
    public int getMoonPhase()
    {
        if (w.get() instanceof World)
            return w.get().getDimension().getMoonPhase(((World) w.get()).getTimeOfDay());
        return 0;
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
        if (w.get() instanceof World)
            return ((World) w.get()).isDaylight();
        return false;
    }

    @Override
    public boolean isRaining()
    {
        if (w.get() instanceof World)
            return ((World) w.get()).isRaining();
        return false;
    }

    @Override
    public boolean isThundering()
    {
        if (w.get() instanceof World)
            return ((World) w.get()).isThundering();
        return false;
    }

}
