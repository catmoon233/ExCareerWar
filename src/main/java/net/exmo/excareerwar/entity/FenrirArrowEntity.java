package net.exmo.excareerwar.entity;

import net.exmo.excareerwar.init.CareerWarModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.PlayMessages;

import java.util.List;

import static net.exmo.excareerwar.content.CareerSkill.isSameTeam;

public class FenrirArrowEntity extends AbstractArrow {
    public FenrirArrowEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    public FenrirArrowEntity(Level level, LivingEntity shooter) {
        super(CareerWarModEntities.FenrirArrow.get(), level);
        this.setOwner(shooter);
        this.setBaseDamage(5.0);
        // 基础伤害
    }
    public FenrirArrowEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(CareerWarModEntities.FenrirArrow.get(), world);
    }
    @Override
    public void tick() {
        super.tick();
        if (level() instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(ParticleTypes.CLOUD, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult p_36755_) {
        super.onHitBlock(p_36755_);
        Entity owner = this.getOwner();
        List<Entity> entities = this.level().getEntities(owner, AABB.ofSize(position(), 7, 7, 7));
        if (this.level() instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(ParticleTypes.CLOUD, this.getX(), this.getY(), this.getZ(), 15, 1, 1, 1, 1);
        }
        for (Entity entity : entities){
            if (entity instanceof LivingEntity livingEntity && livingEntity.isAlive() && livingEntity!= owner && !isSameTeam(entity, owner)){
                double dx = -this.getDeltaMovement().z;
                double dz = this.getDeltaMovement().x;
                double dist = Math.sqrt(dx * dx + dz * dz);
                if (dist > 1e-6) {
                    double knockbackStrength = 1;
                    livingEntity.push(dx / dist * knockbackStrength, 0.5, dz / dist * knockbackStrength);
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1));
                }
            }

        }
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        if (target instanceof LivingEntity livingTarget) {
            // 主目标100%伤害
            Entity owner = getOwner();
            livingTarget.hurt(this.damageSources().arrow(this, owner), (float) this.getBaseDamage()*0.5F);
            
            // 穿透效果
            if (this.getPierceLevel() > 0) {
                // 对身后目标造成70%伤害
                // 需要实现范围检测逻辑
                this.setPierceLevel((byte)(this.getPierceLevel() - 1));
            }
            
            // 击退效果
            List<Entity> entities = livingTarget.level().getEntities(owner, AABB.ofSize(position(), 7, 7, 7));
            if (this.level() instanceof ServerLevel serverLevel){
                serverLevel.sendParticles(ParticleTypes.CLOUD, this.getX(), this.getY(), this.getZ(), 15, 1, 1, 1, 1);
            }
            for (Entity entity : entities){
                if (entity instanceof LivingEntity livingEntity && livingEntity.isAlive() && livingEntity!= owner && !isSameTeam(entity, owner)){
                    double dx = -this.getDeltaMovement().z;
                    double dz = this.getDeltaMovement().x;
                    double dist = Math.sqrt(dx * dx + dz * dz);
                    if (dist > 1e-6) {
                        double knockbackStrength = 1;
                        livingEntity.push(dx / dist * knockbackStrength, 0.5, dz / dist * knockbackStrength);
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1));
                    }
                }

                }

            
            // 减速效果

        }
        
        super.onHitEntity(result);

    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY; // 不可捡起
    }
}