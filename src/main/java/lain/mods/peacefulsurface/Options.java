package lain.mods.peacefulsurface;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class Options
{

    public static int filteringRules = Flags.MOB | Flags.CHECKING_LIGHTLEVEL;
    public static String mobFilter = "(\\bSlime\\b)";
    public static String dimensionFilter = "(\\bThe End\\b)|(\\bNether\\b)";
    public static int LIGHTLEVEL = 0;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void loadConfig(Configuration config, Logger logger)
    {
        try
        {
            for (Field f : Options.class.getDeclaredFields())
            {
                try
                {
                    int mod = f.getModifiers();
                    if (!Modifier.isPublic(mod) || !Modifier.isStatic(mod) || Modifier.isFinal(mod))
                        continue;
                    if (!f.isAccessible())
                        f.setAccessible(true);
                    Class type = f.getType();
                    if (type.isAssignableFrom(boolean.class))
                    {
                        boolean obj = f.getBoolean(null);
                        f.setBoolean(null, config.get(Configuration.CATEGORY_GENERAL, f.getName(), obj).getBoolean(obj));
                    }
                    else if (type.isAssignableFrom(double.class))
                    {
                        double obj = f.getDouble(null);
                        f.setDouble(null, config.get(Configuration.CATEGORY_GENERAL, f.getName(), obj).getDouble(obj));
                    }
                    else if (type.isAssignableFrom(int.class))
                    {
                        int obj = f.getInt(null);
                        f.setInt(null, config.get(Configuration.CATEGORY_GENERAL, f.getName(), obj).getInt(obj));
                    }
                    else if (type.isAssignableFrom(String.class))
                    {
                        String obj = (String) f.get(null);
                        f.set(null, config.get(Configuration.CATEGORY_GENERAL, f.getName(), obj).getString());
                    }
                }
                catch (Exception e)
                {
                    if (logger != null)
                        logger.catching(Level.WARN, e);
                }
            }
        }
        catch (Exception e)
        {
            if (logger != null)
                logger.catching(Level.ERROR, e);
        }
    }

}
