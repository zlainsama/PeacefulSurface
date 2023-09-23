package lain.mods.peacefulsurface.init.forge;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Logger;

@Mod("peacefulsurface")
public class ForgePeacefulSurface {

    public ForgePeacefulSurface() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    public static Logger getLogger() {
        return Proxy.INSTANCE.logger;
    }

    private void setup(FMLCommonSetupEvent event) {
        Proxy.INSTANCE.init();
    }

}
