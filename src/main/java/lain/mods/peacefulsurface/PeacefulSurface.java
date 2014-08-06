package lain.mods.peacefulsurface;

import java.util.regex.Pattern;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = "PeacefulSurface", useMetadata = true)
public class PeacefulSurface
{

    Logger logger;
    Configuration config;

    Pattern mobFilter;
    Pattern dimensionFilter;

    @Mod.Instance("PeacefulSurface")
    public static PeacefulSurface instance;

    public static void setDisabled()
    {
        MinecraftForge.EVENT_BUS.unregister(instance);
    }

    public static void setEnabled()
    {
        MinecraftForge.EVENT_BUS.register(instance);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void CheckSpawn(LivingSpawnEvent.CheckSpawn event)
    {
        int f = Options.filteringRules;
        if (f == Flags.DISABLED)
            return;
        if ((f & Flags.LIVING) != 0 && !(event.entity instanceof EntityLivingBase))
            return;
        if ((f & Flags.MOB) != 0 && !(event.entity instanceof IMob))
            return;
        String mobName = EntityList.getEntityString(event.entity);
        if (mobName == null || mobFilter.matcher(mobName).lookingAt())
            return;
        String dimensionName = event.world.provider.getDimensionName();
        if (dimensionName != null && dimensionFilter.matcher(dimensionName).lookingAt())
            return;
        dimensionName = String.format("DIM%d", event.world.provider.dimensionId);
        if (dimensionFilter.matcher(dimensionName).lookingAt())
            return;
        if ((f & Flags.CHECKING_LIGHTLEVEL) != 0 && event.world.getSavedLightValue(EnumSkyBlock.Sky, MathHelper.floor_float(event.x), MathHelper.floor_float(event.y), MathHelper.floor_float(event.z)) > Options.LIGHTLEVEL)
            event.setResult(Result.DENY);
        else if ((f & Flags.RAINING) != 0 && !event.world.isRaining())
            event.setResult(Result.DENY);
        else if ((f & Flags.THUNDERING) != 0 && !event.world.isThundering())
            event.setResult(Result.DENY);
        else if ((f & Flags.DAY) != 0 && !event.world.isDaytime())
            event.setResult(Result.DENY);
        else if ((f & Flags.NIGHT) != 0 && event.world.isDaytime())
            event.setResult(Result.DENY);
        // if (!(event.entity instanceof IMob))
        // return;
        // if (event.entity instanceof EntitySlime)
        // return;
        // if (event.world.provider.dimensionId == -1 || event.world.provider.dimensionId == 1)
        // return;
        // if (event.world.getSavedLightValue(EnumSkyBlock.Sky, MathHelper.floor_float(event.x), MathHelper.floor_float(event.y), MathHelper.floor_float(event.z)) > 0)
        // event.setResult(Result.DENY);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        setEnabled();
    }

    @Mod.EventHandler
    public void loadConfig(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        config = new Configuration(event.getSuggestedConfigurationFile());

        try
        {
            config.load();
            Options.loadConfig(config, logger);
            config.save();

            mobFilter = Pattern.compile(Options.mobFilter);
            dimensionFilter = Pattern.compile(Options.dimensionFilter);
        }
        catch (Exception e)
        {
            logger.catching(Level.ERROR, e);
        }
    }

}
