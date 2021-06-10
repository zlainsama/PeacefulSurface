package lain.mods.peacefulsurface.impl.fabric;

import lain.mods.peacefulsurface.api.interfaces.IEntityObj;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class FabricEntityObj implements IEntityObj {

    private final String name;
    private final boolean animal;
    private final boolean living;
    private final boolean monster;

    public FabricEntityObj(EntityType<?> entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must not be null");
        name = EntityType.getId(entity).toString();
        animal = entity.getSpawnGroup().isRare() && entity.getSpawnGroup().isPeaceful();
        living = entity.getSpawnGroup() != SpawnGroup.MISC;
        monster = !entity.getSpawnGroup().isPeaceful();
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
