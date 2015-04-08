package lain.mods.peacefulsurface;

import java.util.regex.Pattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    public boolean Raining;
    public boolean Thundering;
    public boolean Day;
    public boolean Night;
    public boolean InvertedMobFilter;
    public boolean InvertedDimensionFilter;
    public boolean InvertedLightLevelChecking;
    public String mobFilter;
    public String dimensionFilter;
    public int LightLevel;

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

        if (Living && !(entity instanceof EntityLivingBase))
            return false;
        if (Mob && !(entity instanceof IMob))
            return false;
        if (Animal && !(entity instanceof IAnimals))
            return false;
        if (Tameable && !(entity instanceof IEntityOwnable))
            return false;
        String mobName = EntityList.getEntityString(entity);
        if (mobName == null || _mobFilter.matcher(mobName).lookingAt())
            return false;
        String dimensionName = world.provider.getDimensionName();
        if (dimensionName != null && _dimensionFilter.matcher(dimensionName).lookingAt())
            return false;
        dimensionName = String.format("DIM%d", world.provider.dimensionId);
        if (_dimensionFilter.matcher(dimensionName).lookingAt())
            return false;
        if (Checking_LightLevel)
        {
            int n = world.getSavedLightValue(EnumSkyBlock.Sky, MathHelper.floor_float(x), MathHelper.floor_float(y), MathHelper.floor_float(z));
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
        _mobFilter = Pattern.compile(mobFilter);
        _dimensionFilter = Pattern.compile(dimensionFilter);
        valid = true;
    }

}
