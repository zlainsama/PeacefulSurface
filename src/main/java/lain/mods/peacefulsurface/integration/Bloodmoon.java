package lain.mods.peacefulsurface.integration;

import lain.mods.peacefulsurface.PeacefulSurface;
import lumien.bloodmoon.server.BloodmoonHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class Bloodmoon
{

    public static boolean isBloodmoonActive()
    {
        if (!failed)
            try
            {
                if (BloodmoonHandler.INSTANCE != null && BloodmoonHandler.INSTANCE.isBloodmoonActive())
                    return true;
            }
            catch (Throwable t)
            {
                failed = true;

                Logger logger = PeacefulSurface.instance.getLogger();
                logger.info("Disabled Bloodmoon integration due to following error.");
                logger.catching(Level.INFO, t);
            }
        return false;
    }

    private static boolean failed = false;

}