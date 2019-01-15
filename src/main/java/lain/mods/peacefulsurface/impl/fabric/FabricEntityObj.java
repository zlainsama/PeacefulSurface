package lain.mods.peacefulsurface.impl.fabric;

import java.lang.ref.WeakReference;
import lain.mods.peacefulsurface.api.interfaces.IEntityObj;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.registry.Registry;

public class FabricEntityObj implements IEntityObj
{

    private WeakReference<EntityType<?>> e;

    public FabricEntityObj(EntityType<?> entity)
    {
        e = new WeakReference<EntityType<?>>(entity);
    }

    @Override
    public String getEntityName()
    {

        return Registry.ENTITY_TYPE.getId(e.get()).toString();
    }

    @Override
    public Object getObject()
    {
        return e.get();
    }

    @Override
    public boolean isAnimal()
    {
        return e.get().getEntityClass().isAssignableFrom(AnimalEntity.class);
    }

    @Override
    public boolean isLiving()
    {
        return e.get().getEntityClass().isAssignableFrom(LivingEntity.class);
    }

    @Override
    public boolean isMonster()
    {
        return e.get().getEntityClass().isAssignableFrom(MobEntity.class);
    }

    @Override
    public boolean isTameable()
    {
        return e.get().getEntityClass().isAssignableFrom(TameableEntity.class);
    }

}
