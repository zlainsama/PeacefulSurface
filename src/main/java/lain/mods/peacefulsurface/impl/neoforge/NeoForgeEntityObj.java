package lain.mods.peacefulsurface.impl.neoforge;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lain.mods.peacefulsurface.api.interfaces.IEntityObj;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class NeoForgeEntityObj implements IEntityObj {

    private static final NeoForgeEntityObj dummy = new NeoForgeEntityObj() {

        {
            name = "[Dummy]";
            animal = false;
            living = false;
            monster = false;
        }

    };
    private static final LoadingCache<EntityType<? extends Entity>, NeoForgeEntityObj> cache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<EntityType<? extends Entity>, NeoForgeEntityObj>() {

        @Override
        public NeoForgeEntityObj load(EntityType<? extends Entity> key) throws Exception {
            try {
                NeoForgeEntityObj obj = new NeoForgeEntityObj();
                obj.name = EntityType.getKey(key).toString();
                obj.animal = key.getCategory().isPersistent() && key.getCategory().isFriendly();
                obj.living = key.getCategory() != MobCategory.MISC;
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

    private NeoForgeEntityObj() {
    }

    public static NeoForgeEntityObj get(Entity entity) {
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
