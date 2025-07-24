package net.exmo.excareerwar.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author canyuesama
 */
@Mixin(Projectile.class)
public abstract class ProjectileMixin {
    @Unique
    private int exCareerWar$invulnerableTime2;

    @Shadow protected abstract boolean canHitEntity(Entity p_37250_);

    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void hitEntity(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (entityHitResult.getType()== HitResult.Type.MISS)return;
        if (entityHitResult.getEntity() instanceof LivingEntity livingEntity){
            if (canHitEntity(livingEntity)){
                exCareerWar$invulnerableTime2 = livingEntity.invulnerableTime;
                livingEntity.invulnerableTime = 0;
            }
        }

    }
    @Inject(method = "onHitEntity", at = @At("TAIL"))
    private void hitEntity2(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (entityHitResult.getType()== HitResult.Type.MISS)return;
        if (entityHitResult.getEntity() instanceof LivingEntity livingEntity){
            if (canHitEntity(livingEntity)){
                livingEntity.invulnerableTime = exCareerWar$invulnerableTime2;
            }
        }

    }
}
