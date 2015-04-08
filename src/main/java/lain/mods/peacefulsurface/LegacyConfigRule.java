package lain.mods.peacefulsurface;

import java.io.File;
import java.util.regex.Pattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class LegacyConfigRule implements IEntitySpawnFilter
{

    public static final int DISABLED = 0;
    public static final int LIVING = 1;
    public static final int MOB = 2;
    public static final int CHECKING_LIGHTLEVEL = 4;
    public static final int RAINING = 8;
    public static final int THUNDERING = 16;
    public static final int DAY = 32;
    public static final int NIGHT = 64;

    int filteringRules = MOB | CHECKING_LIGHTLEVEL;
    String mobFilter = "(\\bSlime\\b)";
    String dimensionFilter = "(\\bThe End\\b)|(\\bNether\\b)";
    int LIGHTLEVEL = 0;

    Pattern _mobFilter;
    Pattern _dimensionFilter;

    public LegacyConfigRule(File config)
    {
        Configuration cfg = new Configuration(config);

        filteringRules = cfg.get(Configuration.CATEGORY_GENERAL, "filteringRules", filteringRules).getInt();
        mobFilter = cfg.get(Configuration.CATEGORY_GENERAL, "mobFilter", mobFilter).getString();
        dimensionFilter = cfg.get(Configuration.CATEGORY_GENERAL, "dimensionFilter", dimensionFilter).getString();
        LIGHTLEVEL = cfg.get(Configuration.CATEGORY_GENERAL, "LIGHTLEVEL", LIGHTLEVEL).getInt();

        _mobFilter = Pattern.compile(mobFilter);
        _dimensionFilter = Pattern.compile(dimensionFilter);

        // if (cfg.hasChanged())
        // cfg.save();
    }

    @Override
    public boolean enabled()
    {
        return (filteringRules != DISABLED);
    }

    @Override
    public boolean filterEntity(Entity entity, World world, float x, float y, float z)
    {
        int f = filteringRules;
        if ((f & LIVING) != 0 && !(entity instanceof EntityLivingBase))
            return false;
        if ((f & MOB) != 0 && !(entity instanceof IMob))
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
        if ((f & CHECKING_LIGHTLEVEL) != 0 && world.getSavedLightValue(EnumSkyBlock.Sky, MathHelper.floor_float(x), MathHelper.floor_float(y), MathHelper.floor_float(z)) > LIGHTLEVEL)
            return true;
        else if ((f & RAINING) != 0 && !world.isRaining())
            return true;
        else if ((f & THUNDERING) != 0 && !world.isThundering())
            return true;
        else if ((f & DAY) != 0 && !world.isDaytime())
            return true;
        else if ((f & NIGHT) != 0 && world.isDaytime())
            return true;
        return false;
    }

}
