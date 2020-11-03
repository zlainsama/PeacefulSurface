package lain.mods.peacefulsurface.api.interfaces;

public interface IEntityObj {

    /**
     * @return the name of the Entity in the Game Registry.
     */
    String getEntityName();

    /**
     * @return true if the entity is considered Animal.
     */
    boolean isAnimal();

    /**
     * @return true if the entity is considered Living.
     */
    boolean isLiving();

    /**
     * @return true if the entity is considered Monster.
     */
    boolean isMonster();

}
