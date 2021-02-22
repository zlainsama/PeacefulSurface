package lain.mods.peacefulsurface.impl.fabric;

import corgitaco.enchancedcelestials.data.world.LunarData;
import corgitaco.enchancedcelestials.lunarevent.BloodMoon;
import corgitaco.enchancedcelestials.lunarevent.LunarEventSystem;
import lain.mods.peacefulsurface.api.interfaces.IWorldObj;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

public class FabricWorldObj implements IWorldObj {

    private static final AtomicBoolean failedCompat_BloodMoon_EnhancedCelestials = new AtomicBoolean(!FabricLoader.getInstance().isModLoaded("enhancedcelestials"));

    private final WeakReference<ServerWorld> w;
    private final String name;

    public FabricWorldObj(ServerWorld world) {
        if (world == null)
            throw new IllegalArgumentException("world must not be null");
        w = new WeakReference<>(world);
        name = world.getRegistryKey().getValue().toString();
    }

    @Override
    public String getBiomeName(double x, double y, double z) {
        ServerWorld o;
        if ((o = w.get()) == null) // gc
            return "UNKNOWN";
        return o.getBiome(new BlockPos(x, y, z)).toString();
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
        ServerWorld o;
        if ((o = w.get()) == null)
            return false;

        if (!failedCompat_BloodMoon_EnhancedCelestials.get()) {
            try {
                return LunarEventSystem.LUNAR_EVENTS_MAP.get(LunarData.get(o).getEvent()) instanceof BloodMoon;
            } catch (Throwable t) {
                System.err.println("error checking BloodMoon");
                t.printStackTrace();
                failedCompat_BloodMoon_EnhancedCelestials.set(true);
            }
        }

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
