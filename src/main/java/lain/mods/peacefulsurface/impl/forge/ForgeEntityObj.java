package lain.mods.peacefulsurface.impl.forge;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lain.mods.peacefulsurface.api.interfaces.IEntityObj;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;

public class ForgeEntityObj implements IEntityObj {

    private static final ForgeEntityObj dummy = new ForgeEntityObj() {

        {
            name = "[Dummy]";
            animal = false;
            living = false;
            monster = false;
        }

    };
    private static final LoadingCache<EntityType<? extends Entity>, ForgeEntityObj> cache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<EntityType<? extends Entity>, ForgeEntityObj>() {

        @Override
        public ForgeEntityObj load(EntityType<? extends Entity> key) throws Exception {
            try {
                ForgeEntityObj obj = new ForgeEntityObj();
                obj.name = EntityType.getKey(key).toString();
                obj.animal = key.getCategory().isPersistent() && key.getCategory().isFriendly();
                obj.living = key.getCategory() != EntityClassification.MISC;
                obj.monster = !key.getCategory().isFriendly();
                return obj;
            } catch (Throwable t) {
                return dummy;
            }
        }

    });

    String name;
    boolean animal;
    boolean living;
    boolean monster;

    private ForgeEntityObj() {
    }

    public static ForgeEntityObj get(Entity entity) {
        if (entity == null)
            return dummy;
        return cache.getUnchecked(entity.getType());
    }

    @Override
    public String getEntityName() {
        return name;
    }

    @Override
    public boolean isAnimal() {
        return animal;
    }

    @Override
    public boolean isLiving() {
        return living;
    }

    @Override
    public boolean isMonster() {
        return monster;
    }

}
