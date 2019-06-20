package lain.mods.peacefulsurface;

import java.util.regex.Pattern;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lain.mods.peacefulsurface.integration.Bloodmoon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class JsonRule implements IEntitySpawnFilter
{

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private transient boolean valid = false;

    public boolean Disabled;
    public boolean Living;
    public boolean Mob;
    public boolean Animal;
    public boolean Tameable;
    public boolean Checking_LightLevel;
    public boolean Sunny;
    public boolean Raining;
    public boolean Thundering;
    public boolean Day;
    public boolean Night;
    public boolean InvertedMobFilter;
    public boolean InvertedDimensionFilter;
    public boolean InvertedLightLevelChecking;
    public String mobFilter = "";
    public String dimensionFilter = "";
    public int LightLevel;
    public int MoonPhase;
    public boolean DisabledUnderBloodmoon;
    public boolean DisabledWhenSunny;
    public boolean DisabledWhenRaining;
    public boolean DisabledWhenThundering;
    public boolean DisabledWhenDay;
    public boolean DisabledWhenNight;

    private transient Pattern _mobFilter;
    private transient Pattern _dimensionFilter;

    @Override
    public boolean enabled()
    {
        return !Disabled;
    }

    @Override
    public boolean filterEntity(Entity entity, World world, float x, float y, float z)
    {
        validate();

        if (DisabledUnderBloodmoon && Bloodmoon.isBloodmoonActive())
            return false;
        if (DisabledWhenSunny && (!world.isRaining() && !world.isThundering()))
            return false;
        if (DisabledWhenRaining && world.isRaining())
            return false;
        if (DisabledWhenThundering && world.isThundering())
            return false;
        if (DisabledWhenDay && world.isDaytime())
            return false;
        if (DisabledWhenNight && !world.isDaytime())
            return false;
        if (MoonPhase != 0 && world.provider.getMoonPhase(world.getWorldInfo().getWorldTime()) != (MoonPhase - 1))
            return false;
        if (Living && !(entity instanceof EntityLivingBase))
            return false;
        if (Mob && !(entity instanceof IMob))
            return false;
        if (Animal && !(entity instanceof IAnimals))
            return false;
        if (Tameable && !(entity instanceof IEntityOwnable))
            return false;
        String mobName = EntityList.getEntityString(entity);
        if (mobName == null)
            return false;
        if (InvertedMobFilter)
        {
            if (!_mobFilter.matcher(mobName).lookingAt())
                return false;
        }
        else
        {
            if (_mobFilter.matcher(mobName).lookingAt())
                return false;
        }
        if (InvertedDimensionFilter)
        {
            String dimensionName = world.provider.getDimensionType().getName();
            String dimensionName2 = String.format("DIM%d", world.provider.getDimension());
            if (!_dimensionFilter.matcher(dimensionName).lookingAt() && !_dimensionFilter.matcher(dimensionName2).lookingAt())
                return false;
        }
        else
        {
            String dimensionName = world.provider.getDimensionType().getName();
            String dimensionName2 = String.format("DIM%d", world.provider.getDimension());
            if (_dimensionFilter.matcher(dimensionName).lookingAt() || _dimensionFilter.matcher(dimensionName2).lookingAt())
                return false;
        }
        if (Checking_LightLevel)
        {
            int n = world.getLight(new BlockPos(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z)), false);
            if (InvertedLightLevelChecking)
            {
                if (n <= LightLevel)
                    return true;
            }
            else
            {
                if (n > LightLevel)
                    return true;
            }
        }
        if (Sunny && (world.isRaining() || world.isThundering()))
            return true;
        if (Raining && !world.isRaining())
            return true;
        if (Thundering && !world.isThundering())
            return true;
        if (Day && !world.isDaytime())
            return true;
        if (Night && world.isDaytime())
            return true;
        return false;
    }

    public void invalidate()
    {
        valid = false;
    }

    public void validate()
    {
        if (valid)
            return;
        if (MoonPhase < 0 || MoonPhase > 8)
            MoonPhase = 0;
        _mobFilter = Pattern.compile(mobFilter);
        _dimensionFilter = Pattern.compile(dimensionFilter);
        valid = true;
    }

}
