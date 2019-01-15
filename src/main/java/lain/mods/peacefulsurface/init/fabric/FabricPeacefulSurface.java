package lain.mods.peacefulsurface.init.fabric;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lain.mods.peacefulsurface.api.PeaceAPI;
import lain.mods.peacefulsurface.impl.JsonRule;
import lain.mods.peacefulsurface.impl.fabric.FabricEntityObj;
import lain.mods.peacefulsurface.impl.fabric.FabricWorldObj;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.events.ServerEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.text.Style;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.world.ViewableWorld;

public class FabricPeacefulSurface implements ModInitializer
{

    private static LoadingCache<ViewableWorld, FabricWorldObj> worlds = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<ViewableWorld, FabricWorldObj>()
    {

        @Override
        public FabricWorldObj load(ViewableWorld key) throws Exception
        {
            return new FabricWorldObj(key);
        }

    });

    private static LoadingCache<EntityType<?>, FabricEntityObj> entities = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<EntityType<?>, FabricEntityObj>()
    {

        @Override
        public FabricEntityObj load(EntityType<?> key) throws Exception
        {
            return new FabricEntityObj(key);
        }

    });

    public static FabricEntityObj wrapEntity(EntityType<?> entity)
    {
        return entities.getUnchecked(entity);
    }

    public static FabricWorldObj wrapWorld(ViewableWorld world)
    {
        return worlds.getUnchecked(world);
    }

    @Override
    public void onInitialize()
    {
        ServerEvent.START.register(server -> {
            server.getCommandManager().getDispatcher().register(ServerCommandManager.literal("reloadpeace").requires(source -> {
                return source.hasPermissionLevel(3);
            }).executes(context -> {
                server.execute(() -> {
                    reloadConfig();
                    context.getSource().sendFeedback(new TranslatableTextComponent("commands.reloadpeace.done", new Object[0]).setStyle(new Style().setColor(TextFormat.YELLOW)), true);
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
