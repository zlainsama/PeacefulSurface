package lain.mods.peacefulsurface.init.forge;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.common.io.Resources;
import lain.mods.peacefulsurface.api.PeaceAPI;
import lain.mods.peacefulsurface.impl.JsonRule;
import lain.mods.peacefulsurface.impl.forge.ForgeEntityObj;
import lain.mods.peacefulsurface.impl.forge.ForgeWorldObj;
import net.minecraft.command.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

enum Proxy
{

    INSTANCE;

    Logger logger = LogManager.getLogger(ForgePeacefulSurface.class);

    void handleCheckSpawn(LivingSpawnEvent.CheckSpawn event)
    {
        if (event.isSpawner() || !PeaceAPI.filterEntity(ForgeEntityObj.get(event.getEntity()), ForgeWorldObj.get(((IServerWorld) event.getWorld()).getWorld()), event.getX(), event.getY(), event.getZ()))
            return;
        event.setResult(Result.DENY);
    }

    void handleRegisterCommands(RegisterCommandsEvent event)
    {
        event.getDispatcher().register(Commands.literal("reloadpeace").requires(source -> source.hasPermissionLevel(3)).executes(context -> {
            LogicalSidedProvider.INSTANCE.<MinecraftServer>get(LogicalSide.SERVER).execute(() -> {
                reloadRules();
                context.getSource().sendFeedback(new TranslationTextComponent("commands.reloadpeace.done"), true);
            });
            return 0;
        }));
    }

    void init()
    {
        reloadRules();
        MinecraftForge.EVENT_BUS.addListener(this::handleCheckSpawn);
        MinecraftForge.EVENT_BUS.addListener(this::handleRegisterCommands);
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
