package lain.mods.peacefulsurface.init.forge;

import com.google.common.io.Resources;
import lain.mods.peacefulsurface.api.PeaceAPI;
import lain.mods.peacefulsurface.impl.JsonRule;
import lain.mods.peacefulsurface.impl.forge.ForgeEntityObj;
import lain.mods.peacefulsurface.impl.forge.ForgeWorldObj;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.fml.LogicalSide;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

enum Proxy {

    INSTANCE;

    Logger logger = LogManager.getLogger(ForgePeacefulSurface.class);

    void handleCheckSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (event.getSpawnReason() != EntitySpawnReason.NATURAL || !PeaceAPI.filterEntity(ForgeEntityObj.get(event.getEntity()), ForgeWorldObj.get(event.getLevel().getLevel()), Mth.floor(event.getX()), Mth.floor(event.getY()), Mth.floor(event.getZ())))
            return;
        event.setSpawnCancelled(true);
        event.setCanceled(true);
    }

    void handleRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("reloadpeace").requires(source -> source.hasPermission(3)).executes(context -> {
            LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER).execute(() -> {
                reloadRules();
                context.getSource().sendSuccess(() -> Component.translatable("commands.reloadpeace.done"), true);
            });
            return 0;
        }));
    }

    void init() {
        reloadRules();
        MinecraftForge.EVENT_BUS.addListener(this::handleCheckSpawn);
        MinecraftForge.EVENT_BUS.addListener(this::handleRegisterCommands);
    }

    void reloadRules() {
        try {
            logger.info("[PeacefulSurface] Loading filters...");
            PeaceAPI.clearFilters();
            File dir = Paths.get(".", "config", "PeacefulSurface_Rules").toFile();

            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    try {
                        logger.info("[PeacefulSurface] Writing DefaultRule...");
                        FileUtils.copyURLToFile(Resources.getResource("/DefaultRule.json"), new File(dir, "DefaultRule.json"));
                        logger.info("[PeacefulSurface] Successfully wrote DefaultRule.");
                    } catch (IOException e) {
                        logger.error("[PeacefulSurface] Failed to write DefaultRule.", e);
                    }
                }
            }

            JsonRule.fromDirectory(dir).forEach(PeaceAPI::addFilter);
            PeaceAPI.notifyReloadListeners();
            logger.info("[PeacefulSurface] Loaded {} filter{}.", PeaceAPI.countFilters(), PeaceAPI.countFilters() == 1 ? "" : "s");
        } catch (Throwable t) {
            logger.error("[PeacefulSurface] Failed to load filters.", t);
            PeaceAPI.clearFilters();
        }
    }

}
