package net.exmo.excareerwar.entity;

import net.exmo.excareerwar.Excareerwar;
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
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.PlayMessages;

import static net.exmo.excareerwar.content.CareerSkill.isSameTeam;

public class MistArrowEntity extends AbstractArrow {
    public MistArrowEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    public MistArrowEntity(Level level, LivingEntity shooter) {
        super(CareerWarModEntities.MistArrow.get(), shooter, level);
        this.setOwner(shooter);
        this.setBaseDamage(0.0); // 不造成直接伤害
    }

    public MistArrowEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(CareerWarModEntities.MistArrow.get(), world);
    }

    @Override
    public void tick() {
        super.tick();
        if (level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        if (target instanceof LivingEntity livingTarget) {
            applyEffects(livingTarget);
        }
        explode();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!level().isClientSide) {
            explode();
        }
    }

    private void applyEffects(LivingEntity entity) {
        // 添加减速效果
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
        // 添加毒素伤害
        entity.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 1));
        entity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 60, 1));
        entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 1));
    }
    private void applyEffects2(LivingEntity entity) {
        // 添加减速效果
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 2));
        // 添加毒素伤害

        entity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 60, 1));

    }

    private void explode() {
        Level level = level();
        MistArrowEffectEntity mistArrowEffectEntity = new MistArrowEffectEntity(CareerWarModEntities.MistArrowEffect.get(), level());
        mistArrowEffectEntity.setOwner(this.getOwner());
        mistArrowEffectEntity.setPos(this.getX(), this.getY(), this.getZ());
        mistArrowEffectEntity.getEntityData().set(MistArrowEffectEntity.DURING, getRandomDuration());
        level.addFreshEntity(mistArrowEffectEntity);

//        if (!level.isClientSide) {
//            // 创建烟雾区域
//            int duration = getRandomDuration();
//            for (int i = 0; i < duration; i += 20) {
//                int finalI = i;
//                Excareerwar.queueServerWork(i, () -> {
//                    if (this==null)return;
//                    if (this.isRemoved())return;
//                    ServerLevel serverLevel = (ServerLevel) level;
//                    serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(), 20, 3, 3, 3, 0);
//
//                    // 每秒对范围内的实体施加效果
//                    if (finalI % 20 == 0) {
//                        applyAreaEffects();
//                    }
//                });
//
//            }
//        }
        this.discard(); // 爆炸后销毁箭矢
    }

    private void applyAreaEffects() {
        // 获取半径4-5格范围内的所有实体
        java.util.List<Entity> entities = level().getEntities(this, this.getBoundingBox().inflate(5));
        for (Entity entity : entities) {
            if (entity != this){
            if (entity instanceof LivingEntity livingEntity && !entity.equals(this.getOwner())) {
                if (!isSameTeam(entity, this.getOwner())) {
                    applyEffects(livingEntity);
                } else {
                    applyEffects2(livingEntity);
                }
            }
            }
        }
    }

    // 生成随机持续时间（5-7秒）
    private int getRandomDuration() {
        return 100 + new java.util.Random().nextInt(41);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY; // 不可捡起
    }
}