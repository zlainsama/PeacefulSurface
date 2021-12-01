package lain.mods.peacefulsurface.impl.forge;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.lunarevent.BloodMoon;
import lain.mods.peacefulsurface.api.interfaces.IWorldObj;
import lain.mods.peacefulsurface.init.forge.ForgePeacefulSurface;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.fml.ModList;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

public class ForgeWorldObj implements IWorldObj {

    private static final ForgeWorldObj dummy = new ForgeWorldObj() {

        {
            w = new WeakReference<>(null);
            name = "[Dummy]";
        }

    };
    private static final LoadingCache<ServerLevel, ForgeWorldObj> cache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<ServerLevel, ForgeWorldObj>() {

        @Override
        public ForgeWorldObj load(ServerLevel key) throws Exception {
            try {
                ForgeWorldObj obj = new ForgeWorldObj();
                obj.w = new WeakReference<>(key);
                obj.name = key.dimension().location().toString();
                return obj;
            } catch (Throwable t) {
                return dummy;
            }
        }

    });
    private static final AtomicBoolean failedCompat_BloodMoon_EnhancedCelestials = new AtomicBoolean(!ModList.get().isLoaded("enhancedcelestials"));

    WeakReference<ServerLevel> w;
    String name;

    private ForgeWorldObj() {
    }

    public static ForgeWorldObj get(ServerLevel world) {
        if (world == null)
            return dummy;
        return cache.getUnchecked(world);
    }

    @Override
    public String getBiomeName(double x, double y, double z) {
        ServerLevel o;
        if ((o = w.get()) == null)
            return "UNKNOWN";
        return o.getBiome(new BlockPos(x, y, z)).toString();
    }

    @Override
    public int getLightLevel(double x, double y, double z) {
        ServerLevel o;
        if ((o = w.get()) == null)
            return 0;
        return o.isThundering() ? o.getMaxLocalRawBrightness(new BlockPos(x, y, z), 10) : o.getMaxLocalRawBrightness(new BlockPos(x, y, z));
    }

    @Override
    public int getBlockLight(double x, double y, double z) {
        ServerLevel o;
        if ((o = w.get()) == null)
            return 0;
        return o.getBrightness(LightLayer.BLOCK, new BlockPos(x, y, z));
    }

    @Override
    public int getSkyLight(double x, double y, double z) {
        ServerLevel o;
        if ((o = w.get()) == null)
            return 0;
        return o.getBrightness(LightLayer.SKY, new BlockPos(x, y, z)) - (o.isThundering() ? 10 : o.getSkyDarken());
    }

    @Override
    public int getMoonPhase() {
        ServerLevel o;
        if ((o = w.get()) == null)
            return 0;
        return o.dimensionType().moonPhase(o.dayTime());
    }

    @Override
    public String getWorldName() {
        return name;
    }

    @Override
    public boolean isBloodMoon() {
        ServerLevel o;
        if ((o = w.get()) == null)
            return false;

        if (!failedCompat_BloodMoon_EnhancedCelestials.get()) {
            try {
                LunarContext context = ((EnhancedCelestialsWorldData) o).getLunarContext();
                return context != null && context.getCurrentEvent() instanceof BloodMoon;
            } catch (Throwable t) {
                ForgePeacefulSurface.getLogger().error("error checking BloodMoon", t);
                failedCompat_BloodMoon_EnhancedCelestials.set(true);
            }
        }

        return false;
    }

    @Override
    public boolean isDayTime() {
        ServerLevel o;
        if ((o = w.get()) == null)
            return false;
        return o.isDay();
    }

    @Override
    public boolean isRaining() {
        ServerLevel o;
        if ((o = w.get()) == null)
            return false;
        return o.isRaining();
    }

    @Override
    public boolean isThundering() {
        ServerLevel o;
        if ((o = w.get()) == null)
            return false;
        return o.isThundering();
    }

}
