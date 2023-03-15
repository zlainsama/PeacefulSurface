package lain.mods.peacefulsurface.api.interfaces;

public interface IWorldObj {

    /**
     * @param x the x coordinates to query about BiomeName.
     * @param y the y coordinates to query about BiomeName.
     * @param z the z coordinates to query about BiomeName.
     * @return the BiomeName of the queried position.
     */
    String getBiomeName(int x, int y, int z);

    /**
     * @param x the x coordinates to query about LightLevel.
     * @param y the y coordinates to query about LightLevel.
     * @param z the z coordinates to query about LightLevel.
     * @return the LightLevel of the queried position.
     */
    int getLightLevel(int x, int y, int z);

    /**
     * @param x the x coordinates to query about BlockLight.
     * @param y the y coordinates to query about BlockLight.
     * @param z the z coordinates to query about BlockLight.
     * @return the BlockLight of the queried position.
     */
    int getBlockLight(int x, int y, int z);

    /**
     * @param x the x coordinates to query about SkyLight.
     * @param y the y coordinates to query about SkyLight.
     * @param z the z coordinates to query about SkyLight.
     * @return the SkyLight of the queried position.
     */
    int getSkyLight(int x, int y, int z);

    /**
     * @return the current MoonPhase of the world.
     */
    int getMoonPhase();

    /**
     * @return the name of the world.
     */
    String getWorldName();

    /**
     * @return true if the world is currently in a event of BloodMoon. (if the BloodMoon mod is not present, this will always return false)
     */
    boolean isBloodMoon();

    /**
     * @return true if the world is in DayTime.
     */
    boolean isDayTime();

    /**
     * @return true if the world is currently Raining.
     */
    boolean isRaining();

    /**
     * @return true if the world is currently Thundering.
     */
    boolean isThundering();

}
