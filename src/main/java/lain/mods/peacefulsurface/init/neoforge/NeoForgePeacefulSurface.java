package lain.mods.peacefulsurface.init.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.Logger;

@Mod("peacefulsurface")
public class NeoForgePeacefulSurface {

    public NeoForgePeacefulSurface(IEventBus bus) {
        bus.addListener(this::setup);
    }

    public static Logger getLogger() {
        return Proxy.INSTANCE.logger;
    }

    private void setup(FMLCommonSetupEvent event) {
        Proxy.INSTANCE.init();
    }

}
