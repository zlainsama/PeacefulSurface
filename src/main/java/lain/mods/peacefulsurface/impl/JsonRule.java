package lain.mods.peacefulsurface.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lain.mods.peacefulsurface.api.interfaces.IEntityObj;
import lain.mods.peacefulsurface.api.interfaces.IEntitySpawnFilter;
import lain.mods.peacefulsurface.api.interfaces.IWorldObj;

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

public class JsonRule implements IEntitySpawnFilter {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public boolean Disabled;
    public boolean Living;
    public boolean Monster;
    public boolean Animal;
    public boolean UseMobFilter;
    public boolean UseDimensionFilter;
    public boolean UseBiomeFilter;
    public boolean Checking_LightLevel;
    public boolean Checking_Altitude;
    public boolean Checking_BlockLight;
    public boolean Checking_SkyLight;
    public boolean Sunny;
    public boolean Raining;
    public boolean Thundering;
    public boolean Day;
    public boolean Night;
    public boolean InvertedMobFilter;
    public boolean InvertedDimensionFilter;
    public boolean InvertedBiomeFilter;
    public boolean InvertedLightLevelChecking;
    public boolean InvertedAltitudeChecking;
    public boolean InvertedBlockLightChecking;
    public boolean InvertedSkyLightChecking;
    public String mobFilter = "";
    public String dimensionFilter = "";
    public String biomeFilter = "";
    public int LightLevel;
    public int Altitude;
    public int BlockLight;
    public int SkyLight;
    public int MoonPhase;
    public boolean DisabledUnderBloodmoon;
    public boolean DisabledWhenSunny;
    public boolean DisabledWhenRaining;
    public boolean DisabledWhenThundering;
    public boolean DisabledWhenDay;
    public boolean DisabledWhenNight;

    private transient boolean valid = false;
    private transient Pattern _mobFilter;
    private transient Pattern _dimensionFilter;
    private transient Pattern _biomeFilter;

    public static Collection<JsonRule> fromDirectory(File dir) throws IOException {
        return fromDirectory(dir, ignored -> true);
    }

    public static Collection<JsonRule> fromDirectory(File dir, Predicate<? super IOException> handler) throws IOException {
        List<JsonRule> list = new ArrayList<JsonRule>();
        String newLine = System.getProperty("line.separator");
        for (File file : dir.listFiles(file -> {
            return file.getName().toLowerCase().endsWith(".json");
        })) {
            try {
                list.add(fromJson(Files.lines(file.toPath(), StandardCharsets.UTF_8).collect(Collectors.joining(newLine))));
            } catch (IOException e) {
                if (!handler.test(e))
                    throw e;
            }
        }
        return list;
    }

    public static JsonRule fromJson(String json) {
        return gson.fromJson(json, JsonRule.class);
    }

    @Override
    public boolean enabled() {
        return !Disabled;
    }

    @Override
    public boolean filterEntity(IEntityObj entity, IWorldObj world, double x, double y, double z) {
        validate();

        if (DisabledUnderBloodmoon && world.isBloodMoon())
            return false;
        if (DisabledWhenSunny && (!world.isRaining() && !world.isThundering()))
            return false;
        if (DisabledWhenRaining && world.isRaining())
            return false;
        if (DisabledWhenThundering && world.isThundering())
            return false;
        if (DisabledWhenDay && world.isDayTime())
            return false;
        if (DisabledWhenNight && !world.isDayTime())
            return false;
        if (MoonPhase != 0 && world.getMoonPhase() != (MoonPhase - 1))
            return false;
        if (Living && !entity.isLiving())
            return false;
        if (Monster && !entity.isMonster())
            return false;
        if (Animal && !entity.isAnimal())
            return false;
        String mobName = entity.getEntityName();
        if (mobName == null)
            return false;
        if (UseMobFilter) {
            if (InvertedMobFilter) {
                if (!_mobFilter.matcher(mobName).lookingAt())
                    return false;
            } else {
                if (_mobFilter.matcher(mobName).lookingAt())
                    return false;
            }
        }
        if (UseDimensionFilter) {
            if (InvertedDimensionFilter) {
                String dimensionName = world.getWorldName();
                if (!_dimensionFilter.matcher(dimensionName).lookingAt())
                    return false;
            } else {
                String dimensionName = world.getWorldName();
                if (_dimensionFilter.matcher(dimensionName).lookingAt())
                    return false;
            }
        }
        if (UseBiomeFilter) {
            if (InvertedBiomeFilter) {
                String biomeName = world.getBiomeName(x, y, z);
                if (!_biomeFilter.matcher(biomeName).lookingAt())
                    return false;
            } else {
                String biomeName = world.getBiomeName(x, y, z);
                if (_biomeFilter.matcher(biomeName).lookingAt())
                    return false;
            }
        }
        if (Checking_LightLevel) {
            int n = world.getLightLevel(x, y, z);
            if (InvertedLightLevelChecking) {
                if (n <= LightLevel)
                    return true;
            } else {
                if (n > LightLevel)
                    return true;
            }
        }
        if (Checking_Altitude) {
            if (InvertedAltitudeChecking) {
                if (y <= Altitude)
                    return true;
            } else {
                if (y > Altitude)
                    return true;
            }
        }
        if (Checking_BlockLight) {
            int n = world.getBlockLight(x, y, z);
            if (InvertedBlockLightChecking) {
                if (n <= BlockLight)
                    return true;
            } else {
                if (n > BlockLight)
                    return true;
            }
        }
        if (Checking_SkyLight) {
            int n = world.getSkyLight(x, y, z);
            if (InvertedSkyLightChecking) {
                if (n <= SkyLight)
                    return true;
            } else {
                if (n > SkyLight)
                    return true;
            }
        }
        if (Sunny && (world.isRaining() || world.isThundering()))
            return true;
        if (Raining && !world.isRaining())
            return true;
        if (Thundering && !world.isThundering())
            return true;
        if (Day && !world.isDayTime())
            return true;
        return Night && world.isDayTime();
    }

    public void invalidate() {
        valid = false;
    }

    public void validate() {
        if (valid)
            return;
        if (MoonPhase < 0 || MoonPhase > 8)
            MoonPhase = 0;
        _mobFilter = Pattern.compile(mobFilter);
        _dimensionFilter = Pattern.compile(dimensionFilter);
        _biomeFilter = Pattern.compile(biomeFilter);
        valid = true;
    }

}
