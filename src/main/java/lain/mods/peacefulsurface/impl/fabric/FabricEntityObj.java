package lain.mods.peacefulsurface.impl.fabric;

import lain.mods.peacefulsurface.api.interfaces.IEntityObj;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;

public class FabricEntityObj implements IEntityObj
{

    private String name;
    private boolean animal;
    private boolean living;
    private boolean monster;

    public FabricEntityObj(EntityType<?> entity)
    {
        if (entity == null)
            throw new IllegalArgumentException("entity must not be null");
        name = EntityType.getId(entity).toString();
        animal = entity.getEntityClass().isAnimal();
        living = entity.getEntityClass() != EntityCategory.MISC;
        monster = !entity.getEntityClass().isPeaceful();
    }

    @Override
    public String getEntityName()
    {
        return name;
    }

    @Override
    public boolean isAnimal()
    {
        return animal;
    }

    @Override
    public boolean isLiving()
    {
        return living;
    }

    @Override
    public boolean isMonster()
    {
        return monster;
    }

}
