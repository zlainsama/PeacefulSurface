package lain.mods.peacefulsurface.impl.forge;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import dev.corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import dev.corgitaco.enhancedcelestials.api.ECLunarEventTags;
import dev.corgitaco.enhancedcelestials.core.EnhancedCelestialsContext;
import dev.corgitaco.enhancedcelestials.lunarevent.LunarForecast;
import lain.mods.peacefulsurface.api.interfaces.IWorldObj;
import lain.mods.peacefulsurface.init.forge.ForgePeacefulSurface;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.fml.ModList;

import java.lang.ref.WeakReference;
import java.util.Optional;
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
    public String getBiomeName(int x, int y, int z) {
        ServerLevel o;
        if ((o = w.get()) == null)
            return "UNKNOWN";
        return o.getBiome(new BlockPos(x, y, z)).toString();
    }

    @Override
    public int getLightLevel(int x, int y, int z) {
        ServerLevel o;
        if ((o = w.get()) == null)
            return 0;
        return o.isThundering() ? o.getMaxLocalRawBrightness(new BlockPos(x, y, z), 10) : o.getMaxLocalRawBrightness(new BlockPos(x, y, z));
    }

    @Override
    public int getBlockLight(int x, int y, int z) {
        ServerLevel o;
        if ((o = w.get()) == null)
            return 0;
        return o.getBrightness(LightLayer.BLOCK, new BlockPos(x, y, z));
    }

    @Override
    public int getSkyLight(int x, int y, int z) {
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
    public int getDifficulty() {
        ServerLevel o;
        if ((o = w.get()) == null)
            return -1;
        return o.getDifficulty().getId();
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
                return Optional.ofNullable(((EnhancedCelestialsWorldData) o).getLunarContext())
                        .map(EnhancedCelestialsContext::getLunarForecast)
                        .map(LunarForecast::currentLunarEvent)
                        .map(lunarEventHolder -> {
                            if (lunarEventHolder.is(ECLunarEventTags.BLOOD_MOON))
                                return Boolean.TRUE;
                            return lunarEventHolder.unwrapKey()
                                    .map(ResourceKey::location)
                                    .map(ResourceLocation::toString)
                                    .map(location -> {
                                        if ("enhancedcelestials:blood_moon".equals(location) || "enhancedcelestials:super_blood_moon".equals(location))
                                            return Boolean.TRUE;
                                        return Boolean.FALSE;
                                    }).orElse(Boolean.FALSE);
                        }).orElse(Boolean.FALSE) == Boolean.TRUE;
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
