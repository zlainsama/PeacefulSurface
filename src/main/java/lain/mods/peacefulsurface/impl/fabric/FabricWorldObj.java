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

    public FabricWorldObj(ViewableWorld world)
    {
        w = new WeakReference<ViewableWorld>(world);
    }

    @Override
    public int getLightLevel(double x, double y, double z)
    {
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
        return w.get().getDimension().getType().getRawId();
    }

    @Override
    public String getWorldName()
    {
        return Registry.DIMENSION.getId(w.get().getDimension().getType()).toString();
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
