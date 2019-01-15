package lain.mods.peacefulsurface.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lain.mods.peacefulsurface.api.interfaces.IEntityObj;
import lain.mods.peacefulsurface.api.interfaces.IEntitySpawnFilter;
import lain.mods.peacefulsurface.api.interfaces.IWorldObj;

public class JsonRule implements IEntitySpawnFilter
{

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static Collection<JsonRule> fromDirectory(File dir) throws IOException
    {
        return fromDirectory(dir, ignored -> true);
    }

    public static Collection<JsonRule> fromDirectory(File dir, Predicate<? super IOException> handler) throws IOException
    {
        List<JsonRule> list = new ArrayList<JsonRule>();
        String newLine = System.getProperty("line.separator");
        for (File file : dir.listFiles(file -> {
            return file.getName().toLowerCase().endsWith(".json");
        }))
        {
            try
            {
                list.add(fromJson(Files.lines(file.toPath(), StandardCharsets.UTF_8).collect(Collectors.joining(newLine))));
            }
            catch (IOException e)
            {
                if (!handler.test(e))
                    throw e;
            }
        }
        return list;
    }

    public static JsonRule fromJson(String json)
    {
        return gson.fromJson(json, JsonRule.class);
    }

    public boolean Disabled;
    public boolean Living;
    public boolean Mob;
    public boolean Animal;
    public boolean Tameable;
    public boolean Checking_LightLevel;
    public boolean Raining;
    public boolean Thundering;
    public boolean Day;
    public boolean Night;
    public boolean InvertedMobFilter;
    public boolean InvertedDimensionFilter;
    public boolean InvertedLightLevelChecking;
    public String mobFilter = "";
    public String dimensionFilter = "";
    public int LightLevel;
    public int MoonPhase;
    public boolean DisabledUnderBloodmoon;

    private transient boolean valid = false;

    private transient Pattern _mobFilter;
    private transient Pattern _dimensionFilter;

    @Override
    public boolean enabled()
    {
        return !Disabled;
    }

    @Override
    public boolean filterEntity(IEntityObj entity, IWorldObj world, double x, double y, double z)
    {
        validate();

        if (DisabledUnderBloodmoon && world.isBloodMoon())
            return false;
        if (MoonPhase != 0 && world.getMoonPhase() != (MoonPhase - 1))
            return false;
        if (Living && !entity.isLiving())
            return false;
        if (Mob && !entity.isMonster())
            return false;
        if (Animal && !entity.isAnimal())
            return false;
        if (Tameable && !entity.isTameable())
            return false;
        String mobName = entity.getEntityName();
        if (mobName == null)
            return false;
        if (InvertedMobFilter)
        {
            if (!_mobFilter.matcher(mobName).lookingAt())
                return false;
        }
        else
        {
            if (_mobFilter.matcher(mobName).lookingAt())
                return false;
        }
        if (InvertedDimensionFilter)
        {
            String dimensionName = world.getWorldName();
            String dimensionName2 = String.format("DIM%d", world.getWorldID());
            if (!_dimensionFilter.matcher(dimensionName).lookingAt() && !_dimensionFilter.matcher(dimensionName2).lookingAt())
                return false;
        }
        else
        {
            String dimensionName = world.getWorldName();
            String dimensionName2 = String.format("DIM%d", world.getWorldID());
            if (_dimensionFilter.matcher(dimensionName).lookingAt() || _dimensionFilter.matcher(dimensionName2).lookingAt())
                return false;
        }
        if (Checking_LightLevel)
        {
            int n = world.getLightLevel(x, y, z);
            if (InvertedLightLevelChecking)
            {
                if (n <= LightLevel)
                    return true;
            }
            else
            {
                if (n > LightLevel)
                    return true;
            }
        }
        if (Raining && !world.isRaining())
            return true;
        if (Thundering && !world.isThundering())
            return true;
        if (Day && !world.isDayTime())
            return true;
        if (Night && world.isDayTime())
            return true;
        return false;
    }

    public void invalidate()
    {
        valid = false;
    }

    public void validate()
    {
        if (valid)
            return;
        if (MoonPhase < 0 || MoonPhase > 8)
            MoonPhase = 0;
        _mobFilter = Pattern.compile(mobFilter);
        _dimensionFilter = Pattern.compile(dimensionFilter);
        valid = true;
    }

}
