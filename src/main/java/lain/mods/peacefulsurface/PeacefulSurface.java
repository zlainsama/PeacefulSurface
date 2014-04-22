package lain.mods.peacefulsurface;

import net.minecraft.entity.monster.EntitySlime;
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

    @Mod.Instance("PeacefulSurface")
    public static PeacefulSurface instance = new PeacefulSurface();

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
        if (!(event.entity instanceof IMob))
            return;
        if (event.entity instanceof EntitySlime)
            return;
        if (event.world.provider.dimensionId == -1 || event.world.provider.dimensionId == 1)
            return;
        if (event.world.getSavedLightValue(EnumSkyBlock.Sky, MathHelper.floor_float(event.x), MathHelper.floor_float(event.y), MathHelper.floor_float(event.z)) > 0)
            event.setResult(Result.DENY);
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
        }
        catch (Exception e)
        {
            logger.catching(Level.ERROR, e);
        }
    }

}
