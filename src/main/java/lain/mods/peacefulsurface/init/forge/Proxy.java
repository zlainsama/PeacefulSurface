package lain.mods.peacefulsurface.init.forge;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.common.io.Resources;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import lain.mods.peacefulsurface.api.PeaceAPI;
import lain.mods.peacefulsurface.impl.JsonRule;
import lain.mods.peacefulsurface.impl.forge.ForgeEntityObj;
import lain.mods.peacefulsurface.impl.forge.ForgeWorldObj;
import net.minecraft.command.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

enum Proxy
{

    INSTANCE;

    Logger logger = LogManager.getLogger(ForgePeacefulSurface.class);

    void handleCheckSpawn(LivingSpawnEvent.CheckSpawn event)
    {
        if (event.isSpawner() || !PeaceAPI.filterEntity(ForgeEntityObj.get(event.getEntity()), ForgeWorldObj.get(event.getWorld()), event.getX(), event.getY(), event.getZ()))
            return;
        event.setResult(Result.DENY);
    }

    void handleServerStartingEvent(FMLServerStartingEvent event)
    {
        MinecraftServer server = event.getServer();
        event.getCommandDispatcher().register(Commands.literal("reloadpeace").requires(source -> source.hasPermissionLevel(3)).executes(context -> {
            Futures.addCallback(server.addScheduledTask(this::reloadRules), new FutureCallback<Object>()
            {

                @Override
                public void onFailure(Throwable t)
                {
                }

                @Override
                public void onSuccess(Object result)
                {
                    context.getSource().sendFeedback(new TextComponentTranslation("commands.reloadpeace.done"), true);
                }

            }, MoreExecutors.directExecutor());
            return 0;
        }));
    }

    void init()
    {
        reloadRules();
        MinecraftForge.EVENT_BUS.addListener(this::handleCheckSpawn);
        MinecraftForge.EVENT_BUS.addListener(this::handleServerStartingEvent);
    }

    void reloadRules()
    {
        try
        {
            logger.info("[PeacefulSurface] Loading filters...");
            PeaceAPI.clearFilters();
            File dir = Paths.get(".", "config", "PeacefulSurface_Rules").toFile();

            if (!dir.exists())
            {
                if (dir.mkdirs())
                {
                    try
                    {
                        logger.info("[PeacefulSurface] Writing DefaultRule...");
                        FileUtils.copyURLToFile(Resources.getResource("/DefaultRule.json"), new File(dir, "DefaultRule.json"));
                        logger.info("[PeacefulSurface] Successfully wrote DefaultRule.");
                    }
                    catch (IOException e)
                    {
                        logger.error("[PeacefulSurface] Failed to write DefaultRule.", e);
                    }
                }
            }

            JsonRule.fromDirectory(dir).forEach(PeaceAPI::addFilter);
            PeaceAPI.notifyReloadListeners();
            logger.info("[PeacefulSurface] Loaded {} filter{}.", PeaceAPI.countFilters(), PeaceAPI.countFilters() == 1 ? "" : "s");
        }
        catch (Throwable t)
        {
            logger.error("[PeacefulSurface] Failed to load filters.", t);
            PeaceAPI.clearFilters();
        }
    }

}
