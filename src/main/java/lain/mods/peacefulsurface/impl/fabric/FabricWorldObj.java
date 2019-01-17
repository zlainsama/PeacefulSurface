package lain.mods.peacefulsurface.impl.fabric;

import java.lang.ref.WeakReference;
import lain.mods.peacefulsurface.api.interfaces.IWorldObj;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.LightType;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.World;

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
        name = Registry.DIMENSION.getId(world.getDimension().getType()).toString();
    }

    @Override
    public int getLightLevel(double x, double y, double z)
    {
        if (w.get() == null) // gc
            return 0;
        return w.get().getLightLevel(LightType.SKY_LIGHT, new BlockPos(x, y, z));
    }

    @Override
    public int getMoonPhase()
    {
        if (w.get() instanceof World)
            return w.get().getDimension().method_12454(((World) w.get()).getTimeOfDay());
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
