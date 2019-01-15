package lain.mods.peacefulsurface.api.interfaces;

public interface IWorldObj
{

    /**
     * @param x the x coordinates to query about LightLevel.
     * @param y the y coordinates to query about LightLevel.
     * @param z the z coordinates to query about LightLevel.
     * @return the LightLevel of the queried position.
     */
    int getLightLevel(double x, double y, double z);

    /**
     * @return the current MoonPhase of the world.
     */
    int getMoonPhase();

    /**
     * @return actual game object.
     */
    Object getObject();

    /**
     * @return the ID of the world.
     */
    int getWorldID();

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
