package lain.mods.peacefulsurface.init.fabric;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import lain.mods.peacefulsurface.api.PeaceAPI;
import net.minecraft.entity.EntityType;
import net.minecraft.sortme.SpawnHelper;
import net.minecraft.sortme.SpawnRestriction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ViewableWorld;

@Mixin(SpawnHelper.class)
public class FabricPeacefulSurfaceMixin
{

    @Inject(method = "canSpawn(Lnet/minecraft/sortme/SpawnRestriction$Location;Lnet/minecraft/world/ViewableWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/EntityType;)Z", at = @At("RETURN"), cancellable = true, require = 1)
    private void onCanSpawn_nBXjeY(SpawnRestriction.Location location, ViewableWorld world, BlockPos pos, EntityType<?> entitytype, CallbackInfoReturnable<Boolean> info)
    {
        if (info.getReturnValue() && PeaceAPI.filterEntity(FabricPeacefulSurface.wrapEntity(entitytype), FabricPeacefulSurface.wrapWorld(world), pos.getX(), pos.getY(), pos.getZ()))
            info.setReturnValue(false);
    }

}
