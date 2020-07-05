package lain.mods.peacefulsurface.init.fabric;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.commons.io.FileUtils;
import lain.mods.peacefulsurface.api.PeaceAPI;
import lain.mods.peacefulsurface.impl.JsonRule;
import lain.mods.peacefulsurface.impl.fabric.FabricEntityObj;
import lain.mods.peacefulsurface.impl.fabric.FabricWorldObj;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.WorldAccess;

public class FabricPeacefulSurface implements ModInitializer
{

    private static final Map<EntityType<?>, FabricEntityObj> entities = new WeakHashMap<>();
    private static final Map<WorldAccess, FabricWorldObj> worlds = new WeakHashMap<>();

    public static FabricEntityObj wrapEntity(EntityType<?> entity)
    {
        if (!entities.containsKey(entity))
            entities.put(entity, new FabricEntityObj(entity));
        return entities.get(entity);
    }

    public static FabricWorldObj wrapWorld(WorldAccess world)
    {
        if (!worlds.containsKey(world))
            worlds.put(world, new FabricWorldObj(world));
        return worlds.get(world);
    }

    @Override
    public void onInitialize()
    {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            server.getCommandManager().getDispatcher().register(CommandManager.literal("reloadpeace").requires(source -> {
                return source.hasPermissionLevel(3);
            }).executes(context -> {
                server.execute(() -> {
                    reloadConfig();
                    context.getSource().sendFeedback(new TranslatableText("commands.reloadpeace.done", new Object[0]).setStyle(Style.EMPTY.withColor(Formatting.YELLOW)), true);
                });
                return 0;
            }));
        });

        reloadConfig();
    }

    public void reloadConfig()
    {
        try
        {
            System.out.println("[PeacefulSurface] Loading filters...");
            PeaceAPI.clearFilters();
            File dir = Paths.get(".", "config", "PeacefulSurface_Rules").toFile();

            if (!dir.exists())
            {
                if (dir.mkdirs())
                {
                    try
                    {
                        System.out.println("[PeacefulSurface] Writing DefaultRule...");
                        FileUtils.copyInputStreamToFile(FabricPeacefulSurface.class.getResourceAsStream("/DefaultRule.json"), new File(dir, "DefaultRule.json"));
                        System.out.println("[PeacefulSurface] Successfully wrote DefaultRule.");
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        System.err.println("[PeacefulSurface] Failed to write DefaultRule.");
                    }
                }
            }

            JsonRule.fromDirectory(dir).forEach(PeaceAPI::addFilter);
            PeaceAPI.notifyReloadListeners();
            System.out.println(String.format("[PeacefulSurface] Loaded %d filter%s.", PeaceAPI.countFilters(), PeaceAPI.countFilters() == 1 ? "" : "s"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("[PeacefulSurface] Failed to load filters.");
            PeaceAPI.clearFilters();
        }
    }

}
