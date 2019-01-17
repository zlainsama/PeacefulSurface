package lain.mods.peacefulsurface.impl.fabric;

import lain.mods.peacefulsurface.api.interfaces.IEntityObj;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.registry.Registry;

public class FabricEntityObj implements IEntityObj
{

    private String name;
    private boolean animal;
    private boolean living;
    private boolean monster;
    private boolean tameable;

    public FabricEntityObj(EntityType<?> entity)
    {
        if (entity == null)
            throw new IllegalArgumentException("entity must not be null");
        name = Registry.ENTITY_TYPE.getId(entity).toString();
        animal = AnimalEntity.class.isAssignableFrom(entity.getEntityClass());
        living = LivingEntity.class.isAssignableFrom(entity.getEntityClass());
        monster = MobEntity.class.isAssignableFrom(entity.getEntityClass());
        tameable = TameableEntity.class.isAssignableFrom(entity.getEntityClass());
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

    @Override
    public boolean isTameable()
    {
        return tameable;
    }

}
