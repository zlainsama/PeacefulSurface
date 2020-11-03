package lain.mods.peacefulsurface.impl.fabric;

import lain.mods.peacefulsurface.api.interfaces.IWorldObj;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.lang.ref.WeakReference;

public class FabricWorldObj implements IWorldObj {

    private WeakReference<ServerWorld> w;
    private String name;

    public FabricWorldObj(ServerWorld world) {
        if (world == null)
            throw new IllegalArgumentException("world must not be null");
        w = new WeakReference<>(world);
        name = world.getRegistryKey().getValue().toString();
    }

    @Override
    public int getLightLevel(double x, double y, double z) {
        ServerWorld o;
        if ((o = w.get()) == null) // gc
            return 0;
        return o.isThundering() ? o.getLightLevel(new BlockPos(x, y, z), 10) : o.getLightLevel(new BlockPos(x, y, z));
    }

    @Override
    public int getMoonPhase() {
        ServerWorld o;
        if ((o = w.get()) == null) // gc
            return 0;
        return o.getDimension().getMoonPhase(o.getLunarTime());
    }

    @Override
    public String getWorldName() {
        return name;
    }

    @Override
    public boolean isBloodMoon() {
        // not implemented
        return false;
    }

    @Override
    public boolean isDayTime() {
        ServerWorld o;
        if ((o = w.get()) == null) // gc
            return false;
        return o.isDay();
    }

    @Override
    public boolean isRaining() {
        ServerWorld o;
        if ((o = w.get()) == null) // gc
            return false;
        return o.isRaining();
    }

    @Override
    public boolean isThundering() {
        ServerWorld o;
        if ((o = w.get()) == null) // gc
            return false;
        return o.isThundering();
    }

}
