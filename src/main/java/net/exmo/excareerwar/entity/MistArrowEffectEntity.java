
package net.exmo.excareerwar.entity;

import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.init.CareerWarModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.PlayMessages;

import static net.exmo.excareerwar.content.CareerSkill.isSameTeam;

public class MistArrowEffectEntity extends Projectile {
    public MistArrowEffectEntity(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
        this.setInvisible(true);
    }
    public static final EntityDataAccessor<Integer> DURING = SynchedEntityData.defineId(MistArrowEffectEntity.class, EntityDataSerializers.INT);

    public MistArrowEffectEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(CareerWarModEntities.MistArrowEffect.get(), world);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DURING, 120);
    }

    @Override
    public void tick() {
        super.tick();
        if (level() instanceof ServerLevel serverLevel) {
            explode();
        }
    }


    private void applyEffects(LivingEntity entity) {
        // 添加减速效果
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 2));
        // 添加毒素伤害
        entity.addEffect(new MobEffectInstance(MobEffects.POISON, 2, 1));
        entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 40, 1));
    }
    private void applyEffects2(LivingEntity entity) {
        // 添加减速效果
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1, 2));
        // 添加毒素伤害

        entity.addEffect(new MobEffectInstance(MobEffectRegistry.TRUE_INVISIBILITY.get(), 1, 1));

    }

    private void explode() {
        Level level = level();
        if (entityData.get(DURING)<tickCount) {
            this.discard();
        }
        if (!level.isClientSide) {
            // 创建烟雾区域




                    if (this==null)return;
                    if (this.isRemoved())return;
                    ServerLevel serverLevel = (ServerLevel) level;
                    serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), 8, 1.75, 1.75, 1.75, 0);

                    // 每秒对范围内的实体施加效果
                applyAreaEffects();



        }
    }

    private void applyAreaEffects() {
        // 获取半径4-5格范围内的所有实体
        java.util.List<Entity> entities = level().getEntities(this, this.getBoundingBox().inflate(5));
        for (Entity entity : entities) {
            if (entity != this){
            if (entity instanceof LivingEntity livingEntity ) {
                if (isSameTeam(livingEntity, this.getOwner()) || livingEntity.equals(this.getOwner())) {
                    applyEffects2(livingEntity);
                } else {
                    applyEffects(livingEntity);
                }
            }
            }
        }
    }

    // 生成随机持续时间（5-7秒）
    private int getRandomDuration() {
        return 100 + new java.util.Random().nextInt(41);
    }


}