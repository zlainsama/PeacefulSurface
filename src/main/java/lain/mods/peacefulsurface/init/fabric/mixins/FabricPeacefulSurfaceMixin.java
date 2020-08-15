package lain.mods.peacefulsurface.init.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import lain.mods.peacefulsurface.api.PeaceAPI;
import lain.mods.peacefulsurface.init.fabric.FabricPeacefulSurface;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

@Mixin(SpawnHelper.class)
public abstract class FabricPeacefulSurfaceMixin
{

    @Inject(method = "canSpawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/world/biome/SpawnSettings$SpawnEntry;Lnet/minecraft/util/math/BlockPos$Mutable;D)Z", at = @At("RETURN"), cancellable = true)
    private static void onCanSpawn_nBXjeY(ServerWorld world, SpawnGroup group, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, SpawnSettings.SpawnEntry spawnEntry, BlockPos.Mutable pos, double squaredDistance, CallbackInfoReturnable<Boolean> info)
    {
        if (info.getReturnValue() && PeaceAPI.filterEntity(FabricPeacefulSurface.wrapEntity(spawnEntry.type), FabricPeacefulSurface.wrapWorld(world), pos.getX(), pos.getY(), pos.getZ()))
            info.setReturnValue(false);
    }

}
