package lain.mods.peacefulsurface.impl.forge;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lain.mods.peacefulsurface.api.interfaces.IEntityObj;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimal;

public class ForgeEntityObj implements IEntityObj
{

    private static final ForgeEntityObj dummy = new ForgeEntityObj()
    {

        {
            name = "[Dummy]";
            animal = false;
            living = false;
            monster = false;
        }

    };
    private static final LoadingCache<EntityType<? extends Entity>, ForgeEntityObj> cache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<EntityType<? extends Entity>, ForgeEntityObj>()
    {

        @Override
        public ForgeEntityObj load(EntityType<? extends Entity> key) throws Exception
        {
            try
            {
                ForgeEntityObj obj = new ForgeEntityObj();
                obj.name = key.getRegistryName().toString();
                obj.animal = IAnimal.class.isAssignableFrom(key.getEntityClass());
                obj.living = EntityLivingBase.class.isAssignableFrom(key.getEntityClass());
                obj.monster = IMob.class.isAssignableFrom(key.getEntityClass());
                return obj;
            }
            catch (Throwable t)
            {
                return dummy;
            }
        }

    });

    public static ForgeEntityObj get(Entity entity)
    {
        if (entity == null)
            return dummy;
        return cache.getUnchecked(entity.getType());
    }

    String name;
    boolean animal;
    boolean living;
    boolean monster;

    private ForgeEntityObj()
    {
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
