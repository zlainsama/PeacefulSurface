package lain.mods.peacefulsurface.impl.forge;

import java.lang.ref.WeakReference;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lain.mods.peacefulsurface.api.interfaces.IWorldObj;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.dimension.DimensionType;

public class ForgeWorldObj implements IWorldObj
{

    private static final ForgeWorldObj dummy = new ForgeWorldObj()
    {

        {
            w = new WeakReference<>(null);
            id = 999;
            name = "[Dummy]";
        }

    };
    private static final LoadingCache<IWorld, ForgeWorldObj> cache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<IWorld, ForgeWorldObj>()
    {

        @Override
        public ForgeWorldObj load(IWorld key) throws Exception
        {
            try
            {
                ForgeWorldObj obj = new ForgeWorldObj();
                obj.w = new WeakReference<>(key);
                obj.id = key.getDimension().getType().getId();
                obj.name = DimensionType.getKey(key.getDimension().getType()).toString();
                return obj;
            }
            catch (Throwable t)
            {
                return dummy;
            }
        }

    });

    public static ForgeWorldObj get(IWorld world)
    {
        if (world == null)
            return dummy;
        return cache.getUnchecked(world);
    }

    WeakReference<IWorld> w;
    int id;
    String name;

    private ForgeWorldObj()
    {
    }

    @Override
    public int getLightLevel(double x, double y, double z)
    {
        IWorld o;
        if ((o = w.get()) == null)
            return 0;
        return o.func_226658_a_(LightType.SKY, new BlockPos(x, y, z));
    }

    @Override
    public int getMoonPhase()
    {
        IWorld o;
        if ((o = w.get()) == null)
            return 0;
        return o.getDimension().getMoonPhase(o.getWorld().getDayTime());
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
        return false; // Not implemented
    }

    @Override
    public boolean isDayTime()
    {
        IWorld o;
        if ((o = w.get()) == null)
            return false;
        return o.getWorld().isDaytime();
    }

    @Override
    public boolean isRaining()
    {
        IWorld o;
        if ((o = w.get()) == null)
            return false;
        return o.getWorld().isRaining();
    }

    @Override
    public boolean isThundering()
    {
        IWorld o;
        if ((o = w.get()) == null)
            return false;
        return o.getWorld().isThundering();
    }

}