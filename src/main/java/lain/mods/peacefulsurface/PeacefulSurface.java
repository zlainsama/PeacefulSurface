package lain.mods.peacefulsurface;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import com.google.common.collect.Lists;

@Mod(modid = "PeacefulSurface", useMetadata = true, acceptedMinecraftVersions = "[1.11]")
public class PeacefulSurface
{

    public static void setDisabled()
    {
        MinecraftForge.EVENT_BUS.unregister(instance);
    }

    public static void setEnabled()
    {
        MinecraftForge.EVENT_BUS.register(instance);
    }

    Logger logger;
    File configFile;
    File dirRules;

    final List<IEntitySpawnFilter> filters = Lists.newArrayList();

    @Mod.Instance("PeacefulSurface")
    public static PeacefulSurface instance;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void CheckSpawn(LivingSpawnEvent.CheckSpawn event)
    {
        for (IEntitySpawnFilter filter : filters)
        {
            if (!filter.enabled())
                continue;
            if (filter.filterEntity(event.getEntity(), event.getWorld(), event.getX(), event.getY(), event.getZ()))
            {
                event.setResult(Result.DENY);
                break;
            }
        }
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
        configFile = event.getSuggestedConfigurationFile();
        dirRules = new File(event.getModConfigurationDirectory(), "PeacefulSurface_Rules");
        if (!dirRules.exists())
        {
            dirRules.mkdirs();

            if (!configFile.exists())
            {
                try
                {
                    logger.info("Writing DefaultRule.json...");
                    FileUtils.copyInputStreamToFile(PeacefulSurface.class.getResourceAsStream("/DefaultRule.json"), new File(dirRules, "DefaultRule.json"));
                    logger.info("DefaultRule.json is successfully written.");
                }
                catch (IOException e)
                {
                    logger.catching(Level.ERROR, e);
                }
            }
        }

        reloadConfig();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandReloadPeace());
    }

    protected void reloadConfig()
    {
        try
        {
            logger.info("Loading filters...");
            filters.clear();
            for (File f : dirRules.listFiles(new FileFilter()
            {

                @Override
                public boolean accept(File pathname)
                {
                    if (pathname.getName().toLowerCase().endsWith(".json"))
                        return true;
                    return false;
                }

            }))
                filters.add(JsonRule.gson.fromJson(Files.newBufferedReader(f.toPath(), StandardCharsets.UTF_8), JsonRule.class));
            if (configFile.exists())
                filters.add(new LegacyConfigRule(configFile));
            logger.info(String.format("Loaded %d filter%s.", filters.size(), filters.size() > 1 ? "s" : ""));
        }
        catch (Exception e)
        {
            logger.catching(Level.ERROR, e);
        }
    }

}
