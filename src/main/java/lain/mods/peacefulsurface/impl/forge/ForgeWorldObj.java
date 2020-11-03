package lain.mods.peacefulsurface.impl.forge;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lain.mods.peacefulsurface.api.interfaces.IWorldObj;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.lang.ref.WeakReference;

public class ForgeWorldObj implements IWorldObj {

    private static final ForgeWorldObj dummy = new ForgeWorldObj() {

        {
            w = new WeakReference<>(null);
            name = "[Dummy]";
        }

    };
    private static final LoadingCache<ServerWorld, ForgeWorldObj> cache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<ServerWorld, ForgeWorldObj>() {

        @Override
        public ForgeWorldObj load(ServerWorld key) throws Exception {
            try {

                ForgeWorldObj obj = new ForgeWorldObj();
                obj.w = new WeakReference<>(key);
                obj.name = key.getDimensionKey().getLocation().toString();
                return obj;
            } catch (Throwable t) {
                return dummy;
            }
        }

    });

    WeakReference<ServerWorld> w;
    String name;

    private ForgeWorldObj() {
    }

    public static ForgeWorldObj get(ServerWorld world) {
        if (world == null)
            return dummy;
        return cache.getUnchecked(world);
    }

    @Override
    public int getLightLevel(double x, double y, double z) {
        ServerWorld o;
        if ((o = w.get()) == null)
            return 0;
        return o.isThundering() ? o.getNeighborAwareLightSubtracted(new BlockPos(x, y, z), 10) : o.getLight(new BlockPos(x, y, z));
    }

    @Override
    public int getMoonPhase() {
        ServerWorld o;
        if ((o = w.get()) == null)
            return 0;
        return o.getDimensionType().getMoonPhase(o.func_241851_ab());
    }

    @Override
    public String getWorldName() {
        return name;
    }

    @Override
    public boolean isBloodMoon() {
        return false; // Not implemented
    }

    @Override
    public boolean isDayTime() {
        ServerWorld o;
        if ((o = w.get()) == null)
            return false;
        return o.isDaytime();
    }

    @Override
    public boolean isRaining() {
        ServerWorld o;
        if ((o = w.get()) == null)
            return false;
        return o.isRaining();
    }

    @Override
    public boolean isThundering() {
        ServerWorld o;
        if ((o = w.get()) == null)
            return false;
        return o.isThundering();
    }

}
