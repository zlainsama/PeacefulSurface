package lain.mods.peacefulsurface.init.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import lain.mods.peacefulsurface.api.PeaceAPI;
import lain.mods.peacefulsurface.init.fabric.FabricPeacefulSurface;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.WorldView;

@Mixin(SpawnHelper.class)
public abstract class FabricPeacefulSurfaceMixin
{

    @Inject(method = "canSpawn(Lnet/minecraft/entity/SpawnRestriction$Location;Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/EntityType;)Z", at = @At("RETURN"), cancellable = true)
    private static void onCanSpawn_nBXjeY(SpawnRestriction.Location location, WorldView world, BlockPos pos, EntityType<?> entitytype, CallbackInfoReturnable<Boolean> info)
    {
        if (info.getReturnValue() && PeaceAPI.filterEntity(FabricPeacefulSurface.wrapEntity(entitytype), FabricPeacefulSurface.wrapWorld(world), pos.getX(), pos.getY(), pos.getZ()))
            info.setReturnValue(false);
    }

}
